
package me.sniperzciinema.infected;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.Events.InfectedCommandEvent;
import me.sniperzciinema.infected.Events.InfectedJoinEvent;
import me.sniperzciinema.infected.Extras.Menus;
import me.sniperzciinema.infected.GameMechanics.Deaths;
import me.sniperzciinema.infected.GameMechanics.Equip;
import me.sniperzciinema.infected.GameMechanics.KDRatio;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.GameMechanics.Sort;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.GameMechanics.Stats.StatType;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.Grenade;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Messages.Time;
import me.sniperzciinema.infected.Tools.AddonManager;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;


public class Commands implements CommandExecutor {

	Main plugin;

	public Commands(Main plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("Infected"))
		{
			Player p = null;
			InfPlayer ip = null;
			if (sender instanceof Player)
			{
				p = (Player) sender;
				ip = InfPlayerManager.getInfPlayer(p);
			}
			InfectedCommandEvent ce = new InfectedCommandEvent(args, p, ip);
			Bukkit.getPluginManager().callEvent(ce);
			if (!ce.isCancelled())
			{
				if (args.length >= 1 && args[0].equalsIgnoreCase("Chat"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Chat"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else if (args.length == 1)
					{
						if (!ip.isInfChatting())
						{
							ip.setInfChatting(true);
							p.sendMessage(Msgs.Command_InfChat.getString("<state>", "in to"));
						} else
						{
							ip.setInfChatting(false);
							p.sendMessage(Msgs.Command_InfChat.getString("<state>", "out of"));
						}
					} else
					{
						StringBuilder message = new StringBuilder(args[1]);
						for (int arg = 2; arg < args.length; arg++)
							message.append(" ").append(args[arg]);

						for (Player u : Bukkit.getOnlinePlayers())
							if (ip.getTeam() == InfPlayerManager.getInfPlayer(p).getTeam() || u.hasPermission("Infected.Chat.Spy"))
								u.sendMessage(Msgs.Format_InfChat.getString("<team>", ip.getTeam().toString(), "<player>", p.getName(), "<message>", message.toString()));
					}
				}

				// //////////////////////////////////////////////-CLASSES-///////////////////////////////////////
				else if (args.length >= 1 && args[0].equalsIgnoreCase("Classes"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else if (Lobby.getGameState() == GameState.Infecting || Lobby.getGameState() == GameState.Started)
						p.sendMessage(Msgs.Error_Game_Started.getString());

					else
						Menus.chooseClassTeam(p);
				}

				// ///////////////////////////////////////////-JOIN-//////////////////////////////////////////

				else if (args.length > 0 && args[0].equalsIgnoreCase("Join"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Join"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (Lobby.getGameState() == GameState.Disabled)
						p.sendMessage(Msgs.Error_Misc_Plugin_Disabled.getString());

					else if (Lobby.getLocation() == null)
						p.sendMessage(Msgs.Error_Lobby_Doesnt_Exist.getString());

					else if (Lobby.getArenas().isEmpty() || !Lobby.isArenaValid(Lobby.getArenas().get(0)))
						p.sendMessage(Msgs.Error_Arena_No_Valid.getString());

					else if (Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_In.getString());

					else
					{
						InfectedJoinEvent je = new InfectedJoinEvent(p);
						Bukkit.getPluginManager().callEvent(je);
						
						for (Player u : Lobby.getInGame())
							u.sendMessage(Msgs.Game_Joined_They.getString("<player>", p.getName()));

						ip.setInfo();
						Lobby.addPlayerInGame(p);
						ip.tpToLobby();

						p.sendMessage(Msgs.Game_Joined_You.getString());

						if (Lobby.getGameState() == GameState.InLobby && Lobby.getInGame().size() >= Settings.getRequiredPlayers())

							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
							{

								@Override
								public void run() {

									Lobby.timerStartVote();
								}
							}, 100L);
						else if (Lobby.getGameState() == GameState.Voting)
							p.sendMessage(Msgs.Help_Vote.getString());

						else if (Lobby.getGameState() == GameState.Infecting)
						{
							ip.respawn();
							Equip.equip(p);

						} else if (Lobby.getGameState() == GameState.Started)
						{
							Deaths.playerDies(DeathType.Other, null, p);
						}
					}
				}

				// //////////////////////////////////////-INFO-/////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Info"))
				{
					if (!p.hasPermission("Infected.Join"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (Lobby.getGameState() == GameState.Disabled)
						p.sendMessage(Msgs.Error_Misc_Plugin_Disabled.getString());

					else
					{
						sender.sendMessage("");
						sender.sendMessage(Msgs.Format_Header.getString("<title>", "Status"));
						sender.sendMessage(Msgs.Command_Info_Players.getString("<players>", String.valueOf(Lobby.getInGame().size())));
						sender.sendMessage(Msgs.Command_Info_State.getString("<state>", Lobby.getGameState().toString()));
						sender.sendMessage(Msgs.Command_Info_Time_Left.getString("<time>", Time.getTime((long) Lobby.getTimeLeft())));
					}
				}

				// /////////////////////////////////////////////////////////////
				// SUICIDE
				else if (args.length > 0 && args[0].equalsIgnoreCase("Suicide"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Join"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else if (Lobby.getGameState() != GameState.Started)
						p.sendMessage(Msgs.Error_Game_Not_Started.getString());

					else
					{
						if (ip.getTeam() == Team.Human)
							ip.Infect();

						Deaths.playerDies(DeathType.Other, null, p);
						ip.respawn();
					}
				}
				// ////////////////////////////////////////////////////-SHOP-STORE-/////////////////////////////
				else if (args.length > 0 && (args[0].equalsIgnoreCase("Shop") || args[0].equalsIgnoreCase("Store")))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Join"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else
					{
						Menus.openShopMenu(p);
					}
				}
				// /////////////////////////////////////////////////-GRENADES-///////////////////////////////////////////////
				else if (args.length > 0 && (args[0].equalsIgnoreCase("Grenades") || args[0].equalsIgnoreCase("Grenade")))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Grenades"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else
					{
						if (args.length == 2)
						{
							if (args[1].matches("[0-9]+"))
							{
								int gi = Integer.parseInt(args[1]) - 1;
								if (GrenadeManager.getGrenades().get(gi) != null)
								{
									if (ip.getPoints(Settings.VaultEnabled()) >= GrenadeManager.getGrenades().get(gi).getCost())
									{
										Grenade grenade = GrenadeManager.getGrenades().get(gi);
										p.getInventory().addItem(grenade.getItemStack());
										ip.setPoints(ip.getPoints(Settings.VaultEnabled()) - grenade.getCost(), Settings.VaultEnabled());
										p.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));

									} else
										p.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
								} else
									p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
							} else
								p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
						} else if (args.length == 3)
						{
							int amount = Integer.parseInt(args[2]);

							if (args[1].matches("[0-9]+"))
							{
								int gi = Integer.parseInt(args[1]) - 1;
								if (GrenadeManager.getGrenades().get(gi) != null)
								{
									if (ip.getPoints(Settings.VaultEnabled()) >= GrenadeManager.getGrenades().get(gi).getCost() * amount)
									{
										Grenade grenade = GrenadeManager.getGrenades().get(gi);
										ItemStack g = grenade.getItemStack();
										g.setAmount(amount);
										p.getInventory().addItem(g);
										ip.setPoints(ip.getPoints(Settings.VaultEnabled()) - (grenade.getCost() * amount), Settings.VaultEnabled());
										p.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));
									} else
										p.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
								} else
									p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
							} else
								p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
						} else
						{
							p.sendMessage(Msgs.Format_Header.getString("<title>", "Grenades"));

							int i = 1;
							for (Grenade g : GrenadeManager.getGrenades())
							{
								p.sendMessage(Msgs.Format_Grenades_List.getString("<id>", String.valueOf(i), "<name>", g.getName(), "<cost>", String.valueOf(g.getCost())));
								i++;
							}

							p.sendMessage(Msgs.Help_Grenades.getString());
						}
					}
				}

				// /////////////////////////////////////////////////////-SETLOBBY-/////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("SetLobby"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						Lobby.setLocation(p.getLocation());
						p.sendMessage(Msgs.Command_Lobby_Set.getString());
					}
				}

				// ////////////////////////////////////////////////////-LIST-/////////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("List"))
				{

					if (!p.hasPermission("Infected.List"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					if (args.length != 1)
					{
						if (args[1].equalsIgnoreCase("Playing"))
						{
							p.sendMessage(Msgs.Format_Header.getString("<title>", "Playing"));
							for (Player u : Lobby.getInGame())
								p.sendMessage(Msgs.Format_List.getString("<player>", u.getDisplayName()));
						} else if (args[1].equalsIgnoreCase("Humans"))
						{
							p.sendMessage(Msgs.Format_Header.getString("<title>", "Humans"));
							for (Player u : Lobby.getHumans())
								p.sendMessage(Msgs.Format_List.getString("<player>", u.getDisplayName()));

						} else if (args[1].equalsIgnoreCase("Zombies"))
						{
							p.sendMessage(Msgs.Format_Header.getString("<title>", "Zombies"));
							for (Player u : Lobby.getZombies())
								p.sendMessage(Msgs.Format_List.getString("<player>", u.getDisplayName()));

						} else
							p.sendMessage(Msgs.Help_Lists.getString("<lists>", "Playing, Humans, Zombies"));

					} else
						p.sendMessage(Msgs.Help_Lists.getString("<lists>", "Playing, Humans, Zombies"));
				}
				// ///////////////////////////////////////////////-LEAVE-////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Leave"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Leave"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else
						ip.leaveInfected();

				}

				// ////////////////////////////////-HELP-///////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Help"))
				{
					if (!sender.hasPermission("Infected.Help"))
						sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						if (args.length != 1)
						{

							sender.sendMessage("");
							sender.sendMessage(Msgs.Format_Header.getString("<title>", "Infected Help " + args[1] + ""));

							if (args[1].equalsIgnoreCase("1"))
							{
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join" + ChatColor.WHITE + " - Join Infected");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave" + ChatColor.WHITE + " - Leave Infected");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote" + ChatColor.WHITE + " - Vote for a map");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes" + ChatColor.WHITE + " - Choose a class");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades" + ChatColor.WHITE + " - See the purchasable grenades");
								if (sender.hasPermission("Infected.Chat"))
									sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat [Msg]" + ChatColor.WHITE + " - Chat in your team's chat");
								if (sender.hasPermission("Infected.Stats"))
									sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats [Player]" + ChatColor.WHITE + " - Check a player's stats");
								if (sender.hasPermission("Infected.Suicide"))
									sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide" + ChatColor.WHITE + " - Suicide if you're stuck");
								if (sender.hasPermission("Infected.Info"))
									sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info" + ChatColor.WHITE + " - Check Infected's status");
								if (sender.hasPermission("Infected.Top"))
									sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top [Stat]" + ChatColor.WHITE + " - Check the top 5 ps");
								if (sender.hasPermission("Infected.Arenas"))
									sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas" + ChatColor.WHITE + " - See all possible arenas");
							} else if (args[1].equals("2"))
							{
								if (sender.hasPermission("Infected.SetUp"))
								{
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby" + ChatColor.WHITE + " - Set the main lobby");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn" + ChatColor.WHITE + " - Set the spawn for the selected arena");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns" + ChatColor.WHITE + " - List the number of spawns for a map");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn [#]" + ChatColor.WHITE + " - Tp to a spawn ID");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn [ #]" + ChatColor.WHITE + " - Delete the spawn ID");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena [Arena]" + ChatColor.WHITE + " - Select an arena");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create [Arena]" + ChatColor.WHITE + " - Create an arena");
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove[Arena]" + ChatColor.WHITE + " - Remove an Arena");
								}
								if (sender.hasPermission("Infected.Admin"))
								{
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Admin" + ChatColor.WHITE + " - View the admin menu");
								}
								if (sender.hasPermission("Infected.Files"))
								{
									p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Files" + ChatColor.WHITE + " - Edit Files in Game");
								}
							}
							sender.sendMessage(Msgs.Format_Line.getString());
						} else
						{
							if (p != null && Settings.useBookForHelp())
							{
								ItemStack is = new ItemStack(
										Material.WRITTEN_BOOK);
								BookMeta b = (BookMeta) is.getItemMeta();
								ArrayList<String> pages = new ArrayList<String>();
								pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join\n" + ChatColor.GOLD + " - Join Infected\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave\n" + ChatColor.GOLD + " - Leave Infected\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote <Arena>\n" + ChatColor.GOLD + " - Vote for a map\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades [Id]\n" + ChatColor.GOLD + " - See the purchasable grenades\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide \n" + ChatColor.GOLD + " - Suicide if your stuck");
								pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "   Commands (2)" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat <Msg>\n" + ChatColor.GOLD + " - Chat in your team's chat\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats [Player]\n" + ChatColor.GOLD + " - Check a player's stats\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas\n" + ChatColor.GOLD + " - See all possible arenas\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top <Category>\n" + ChatColor.GOLD + " - Check the top 5 players");
								pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "   Commands (3)" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info\n" + ChatColor.GOLD + " - See The current status\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes\n" + ChatColor.GOLD + " - Choose a class\n");
								if (p.hasPermission("Infected.SetUp"))
									pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " Infected Admin\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby\n" + ChatColor.DARK_AQUA + " - Set the main lobby\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create <Arena>\n" + ChatColor.DARK_AQUA + " - Create an arena\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove <Arena>\n" + ChatColor.DARK_AQUA + " - Remove an Arena\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena <Arena>\n" + ChatColor.DARK_AQUA + " - Select an arena\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn\n" + ChatColor.DARK_AQUA + " - Set the spawn");
								if (p.hasPermission("Infected.SetUp"))
									pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " Infected Admin\n" + "   Commands (2)" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn <ID>\n" + ChatColor.DARK_AQUA + " - Teleport to the spawn ID(Number)\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn <ID>\n" + ChatColor.DARK_AQUA + " - Remove the spawn ID(Number)\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns\n" + ChatColor.DARK_AQUA + " - List how many spawns an arena has\n" + ChatColor.GREEN + " /Inf TpLobby" + ChatColor.DARK_GRAY + ". Tp to the lobby.\n");
								if (p.hasPermission("Infected.Admin"))
									pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " Infected Force\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Start\n" + ChatColor.BLACK + " - Force the game to start\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "End\n" + ChatColor.BLACK + " - Force the game to end\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Refresh\n" + ChatColor.BLACK + " - Force all the ps to refresh eachother\n");
								if (p.hasPermission("Infected.Admin"))
									pages.add(ChatColor.GREEN + "" + ChatColor.BOLD + " Infected Admin\n" + "       Menu" + "\n\n" + ChatColor.AQUA + "/Inf Admin Points <Player> <#>\n" + ChatColor.BLACK + "/Inf Admin Score <Player> <#>\n" + ChatColor.BLUE + "/Inf Admin KStats <Player> <#>\n" + ChatColor.DARK_AQUA + "/Inf Admin DStats <Player> <#>\n" + ChatColor.DARK_GREEN + "/Inf Admin Kick <Player>\n" + ChatColor.DARK_BLUE + "/Inf Admin Reload\n");
								if (p.hasPermission("Infected.Admin"))
									pages.add(ChatColor.GREEN + "" + ChatColor.BOLD + " Infected Admin\n" + "     Menu (2)" + "\n\n" + ChatColor.DARK_PURPLE + "/Inf Admin Reset <Player>\n" + ChatColor.GOLD + "/Inf Admin Shutdown\n" + ChatColor.LIGHT_PURPLE + "/Inf Admin Code\n");
								if (p.hasPermission("Infected.SetUp"))
									pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " How To Set Up \n" + "     Infected" + "\n\n" + ChatColor.RED + "1. " + ChatColor.DARK_GRAY + "Build a lobby\n" + ChatColor.RED + "2. " + ChatColor.DARK_GRAY + "Set the lobby spawn point where you're standing using the" + ChatColor.GREEN + " /Inf SetLobby" + ChatColor.DARK_GRAY + " command\n" + ChatColor.RED + "3. " + ChatColor.DARK_GRAY + "Build an arena\n");
								if (p.hasPermission("Infected.SetUp"))
									pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " How To Set Up \n" + "  Infected (2)" + "\n\n" + ChatColor.RED + "4. " + ChatColor.DARK_GRAY + "Create the arena on Infected using " + ChatColor.GREEN + " /Inf Create <Arena Name>\n" + ChatColor.RED + "5. " + ChatColor.DARK_GRAY + "Set the first arena spawn point where you're standing using " + ChatColor.GREEN + " /Inf SetSpawn" + ChatColor.DARK_GRAY + ". You can keep retyping this command to set more spawn points.\n");
								b.setAuthor(ChatColor.DARK_AQUA + "SniperzCiinema(xXSniperzzXx_SD)");
								b.setTitle(ChatColor.DARK_RED + "Infected Help Book");
								b.setPages(pages);
								is.setItemMeta(b);
								if (!p.getInventory().contains(is))
									p.getInventory().addItem(is);
								else
									p.sendMessage(Msgs.Format_Prefix.getString() + "You already have the help book!");

							} else
							{
								p.sendMessage("");
								p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
								p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Help type: /Infected Help 1");
							}
						}
					}
				}

				// ///////////////////////////////////////////////////////-VOTE-////////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Vote"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Leave"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (!Lobby.getInGame().contains(p))
						p.sendMessage(Msgs.Error_Game_Not_In.getString());

					else if (Lobby.getGameState() != GameState.Voting && Lobby.getGameState() != GameState.InLobby)
						p.sendMessage(Msgs.Error_Game_Started.getString());

					else if (ip.getVote() != null)
						p.sendMessage(Msgs.Error_Already_Voted.getString());

					else
						Menus.openVotingMenu(p);

				}

				// //////////////////////////////////////////////////-START-////////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Start"))
				{
					if (!sender.hasPermission("Infected.Force.Start"))
						sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (Lobby.getGameState() != GameState.InLobby)
						sender.sendMessage(Msgs.Error_Game_Started.getString());

					else
						Lobby.timerStartVote();
				}

				// //////////////////////////////////////////////-END-////////////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("End"))
				{
					if (!sender.hasPermission("Infected.Force.End"))
						sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (Lobby.getGameState() == GameState.InLobby)
						sender.sendMessage(Msgs.Error_Game_Started.getString());

					else
						Game.endGame(true);
				}

				// ////////////////////////////////////////////////-ARENAS-/////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Arenas"))
				{

					if (!sender.hasPermission("Infected.Arenas"))
						sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						p.sendMessage(Msgs.Format_Header.getString("<title>", "Arenas"));

						StringBuilder valid = new StringBuilder();
						for (Object o : Lobby.getValidArenas())
						{
							valid.append(o.toString());
							if (Lobby.getValidArenas().size() > 1)
								valid.append(", ");
						}

						StringBuilder inValid = new StringBuilder();
						for (Object o : Lobby.getInValidArenas())
						{
							inValid.append(o.toString());
							if (Lobby.getInValidArenas().size() > 1)
								inValid.append(", ");
						}

						sender.sendMessage(Msgs.Command_Arena_List.getString("<valid>", valid.toString(), "<invalid>", inValid.toString()));
					}
				}
				// ///////////////////////////////////////////////////-ADMIN-///////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Admin"))
				{

					if (!sender.hasPermission("Infected.Admin"))
						sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						if (args.length == 2)
						{
							// SHUTDOWN
							if (args[1].equalsIgnoreCase("Shutdown"))
							{
								if (Lobby.getGameState() != GameState.Disabled)
								{
									Lobby.setGameState(GameState.Disabled);
									sender.sendMessage(Msgs.Command_Admin_Shutdown.getString("<state>", "Disabled"));
								} else
								{
									Lobby.setGameState(GameState.InLobby);
									sender.sendMessage(Msgs.Command_Admin_Shutdown.getString("<state>", "Enabled"));
								}
							}
							// RELOAD
							else if (args[1].equalsIgnoreCase("Reload"))
							{
								System.out.println("===== Infected =====");
								Files.reloadAll();
								AddonManager.getAddons();

								InfClassManager.loadConfigClasses();
								GrenadeManager.loadConfigGrenades();

								System.out.println("====================");
								sender.sendMessage(Msgs.Command_Admin_Reload.getString());

							}
							// CODE
							else if (args[1].equalsIgnoreCase("Code"))
							{
								if (p != null)
								{
									p.sendMessage(Msgs.Format_Prefix.getString() + "Code: " + ChatColor.WHITE + ItemHandler.getItemStackToString(p.getItemInHand()));
									p.sendMessage(Msgs.Format_Prefix.getString() + "This code has also been sent to your console to allow for copy and paste!");
									System.out.println(ItemHandler.getItemStackToString(p.getItemInHand()));
								} else
									sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());
							} else
								sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());
						} else if (args.length == 3)
						{
							// KICK
							if (args[1].equalsIgnoreCase("Kick"))
							{
								Player u = Bukkit.getPlayer(args[2]);
								if (u == null || !Lobby.isInGame(u))
									sender.sendMessage(Msgs.Error_Game_Not_In.getString());
								else
								{
									u.performCommand("Infected Leave");
									u.sendMessage(Msgs.Command_Admin_Kicked_You.getString());
									sender.sendMessage(Msgs.Command_Admin_Kicked_Them.getString("<player>", u.getName()));
								}
							} else
								sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());

						} else if (args.length == 4)
						{
							String user = args[2];

							int i = Integer.parseInt(args[3]);
							if (args[1].equalsIgnoreCase("Points"))
							{
								int newValue = Stats.getPoints(user, Settings.VaultEnabled()) + i;
								Stats.setPoints(user, newValue, Settings.VaultEnabled());
								sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "points", "<value>", String.valueOf(newValue)));
							} else if (args[1].equalsIgnoreCase("Score"))
							{
								int newValue = Stats.getScore(user) + i;
								Stats.setScore(user, newValue);
								sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "score", "<value>", String.valueOf(newValue)));
							} else if (args[1].equalsIgnoreCase("Kills"))
							{
								int newValue = Stats.getKills(user) + i;
								Stats.setKills(user, newValue);
								sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "kills", "<value>", String.valueOf(newValue)));
							} else if (args[1].equalsIgnoreCase("Deaths"))
							{
								int newValue = Stats.getDeaths(user) + i;
								Stats.setDeaths(user, newValue);
								sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "deaths", "<value>", String.valueOf(newValue)));
							} else
								sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());

						} else
						{
							sender.sendMessage(Msgs.Format_Header.getString("<title>", "Admin CMDs"));
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.AQUA + "/Inf Admin Points <Player> <#>");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add points to a player(Also goes negative)");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.BLUE + "/Inf Admin Score <Player> <#>");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add score to a player(Also goes negative)");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + "/Inf Admin Kills <Player> <#>");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add kills to a player(Also goes negative)");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_BLUE + "/Inf Admin Deaths <Player> <#>");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add deaths to a player(Also goes negative)");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_GRAY + "/Inf Admin Kick <Player>");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Kick a player out of Infected");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_PURPLE + "/Inf Admin Shutdown");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Prevent joining Infected");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_RED + "/Inf Admin Reload");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Reload the config");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GOLD + "/Inf Admin Code");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "See Infected's item code for the item in hand");
						}
					}
				}
				// /////////////////////////////////////////-STATS-///////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Stats"))
				{

					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Stats"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						if (args.length != 1)
						{

							if (!p.hasPermission("Infected.Stats.Other"))
								p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

							else
							{
								String user = args[1];

								p.sendMessage("");
								p.sendMessage(Msgs.Format_Header.getString("<title>", user));
								p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(user, Settings.VaultEnabled()) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(user));
								p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getOnlineTime((long) Stats.getPlayingTime(user)));
								p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(user) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(user) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + KDRatio.KD(user));
								p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(user));
							}
						} else
						{
							String user = sender.getName();
							p.sendMessage("");
							p.sendMessage(Msgs.Format_Header.getString("<title>", user));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(user, Settings.VaultEnabled()) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(user));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getOnlineTime((long) Stats.getPlayingTime(user)));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(user) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(user) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + KDRatio.KD(user));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(user));
						}
					}
				}

				// //////////////////////////////////////////-TPSPAWN-///////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("TpSpawn"))
				{

					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (ip.getCreating() == null)
						p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

					else
					{
						if (args.length != 1)
						{

							Arena a = Lobby.getArena(ip.getCreating());
							int i = Integer.valueOf(args[1]) - 1;
							if (i < a.getSpawns().size())
							{
								p.teleport(LocationHandler.getPlayerLocation(a.getSpawns().get(i)));
								sender.sendMessage(Msgs.Command_Spawn_Spawns.getString("<spawns>", String.valueOf(i + 1)));
							} else
								sender.sendMessage(Msgs.Help_TpSpawn.getString());

						} else
							sender.sendMessage(Msgs.Help_TpSpawn.getString());
					}
				}
				// /////////////////////////////////////////////-TPLOBBY-////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("TpLobby"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						p.teleport(Lobby.getLocation());
						p.sendMessage(Msgs.Command_Lobby_Tp.getString());
					}
				}

				// ////////////////////////////////////-DELSPAWN-////////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("DelSpawn"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (ip.getCreating() == null)
						p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

					else
					{
						if (args.length != 1)
						{

							Arena a = Lobby.getArena(ip.getCreating());
							int i = Integer.valueOf(args[1]) - 1;
							if (i < a.getSpawns().size())
							{
								List<String> spawns = a.getSpawns();
								spawns.remove(i);
								a.setSpawns(spawns);
								p.sendMessage(Msgs.Command_Spawn_Deleted.getString("<spawn>", String.valueOf(i + 1)));
							} else
								p.sendMessage(Msgs.Help_DelSpawn.getString());
						} else
						{
							p.sendMessage(Msgs.Help_DelSpawn.getString());
						}
					}
				}

				// //////////////////////////////////-SPAWNS-//////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Spawns"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (ip.getCreating() == null)
						p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

					else
					{
						Arena a = Lobby.getArena(ip.getCreating());
						p.sendMessage(Msgs.Command_Spawn_Spawns.getString("<spawns", String.valueOf(a.getSpawns().size())));
					}
				}

				// ////////////////////////////////////////////-SETSPAWN-//////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("SetSpawn"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else if (ip.getCreating() == null)
						p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

					else
					{
						Location l = p.getLocation();
						String s = LocationHandler.getLocationToString(l);
						Arena a = Lobby.getArena(ip.getCreating());
						List<String> list = a.getSpawns();
						list.add(s);
						a.setSpawns(list);

						p.sendMessage(Msgs.Command_Spawn_Set.getString("<spawn>", String.valueOf(list.size())));
					}
				}

				// /////////////////////////////////////////-CREATE-///////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Create"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						if (args.length != 1)
						{

							String arena = StringUtil.getWord(args[1]);

							if (Lobby.getArena(arena) != null)
								p.sendMessage(Msgs.Error_Arena_Already_Exists.getString());

							else
							{
								p.sendMessage(Msgs.Help_SetSpawn.getString());

								if (args.length == 3)
									Files.getArenas().set("Arenas." + arena + ".Creator", args[2]);
								else
									Files.getArenas().set("Arenas." + arena + ".Creator", "Unkown");
								Arena a = new Arena(arena);
								Lobby.addArena(a);
								Block b = p.getLocation().add(0, -1, 0).getBlock();
								a.setBlock(ItemHandler.getItemStackToString(b.getState().getData().toItemStack()));

								Files.saveArenas();
								ip.setCreating(arena);
								p.sendMessage(Msgs.Command_Arena_Created.getString("<arena>", arena));
							}
						} else
							p.sendMessage(Msgs.Help_Create.getString());
					}
				}

				// //////////////////////////////////////-REMOVE-////////////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Remove"))
				{

					if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						if (args.length != 1)
						{

							String arena = args[1];

							if (Lobby.getArena(arena) == null)
								sender.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

							else
							{
								Lobby.removeArena(Lobby.getArena(arena));
								sender.sendMessage(Msgs.Command_Arena_Removed.getString("<arena>", arena));
								return true;
							}
						} else
							sender.sendMessage(Msgs.Help_Remove.getString());
					}
				}

				// /////////////////////////////////////////////////-TOP-/////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Top"))
				{

					if (!p.hasPermission("Infected.Top"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						if (args.length == 2)
						{
							String stat = args[1].toLowerCase();
							System.out.println(stat);
							if (stat.equals("kills") || stat.equals("deaths") || stat.equals("score") || stat.equals("time") || stat.equals("points") || stat.equals("killstreak"))
							{
								StatType type = StatType.valueOf(stat);

								int i = 1;
								sender.sendMessage(Msgs.Format_Header.getString("<title>", "Top " + stat.toString()));
								for (String name : Sort.topStats(type, 5))
								{
									if (name != " ")
									{
										if (i == 1)
											sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + i + ". " + ChatColor.GOLD + ChatColor.BOLD + (name.length() == 16 ? name : (name + "                 ").substring(0, 16)) + ChatColor.GREEN + " =-= " + ChatColor.GRAY + (type == StatType.time ? Time.getOnlineTime((long) Stats.getStat(type, name)) : Stats.getStat(type, name)));
										else if (i == 2 || i == 3)
											sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + i + ". " + ChatColor.GRAY + ChatColor.BOLD + (name.length() == 16 ? name : (name + "                ").substring(0, 16)) + ChatColor.GREEN + " =-= " + ChatColor.GRAY + (type == StatType.time ? Time.getOnlineTime((long) Stats.getStat(type, name)) : Stats.getStat(type, name)));
										else
											sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + i + ". " + ChatColor.WHITE + ChatColor.BOLD + (name.length() == 16 ? name : (name + "                 ").substring(0, 16)) + ChatColor.GREEN + " =-= " + ChatColor.DARK_GRAY + (type == StatType.time ? Time.getOnlineTime((long) Stats.getStat(type, name)) : Stats.getStat(type, name)));
									}
									i++;

									if (i == 6)
										break;
								}

							} else
								sender.sendMessage(Msgs.Error_Top_Not_Stat.getString());
						} else
							sender.sendMessage(Msgs.Help_Top.getString());
					}
				}

				// ////////////////////////////////-SETARENA-/////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("SetArena"))
				{
					if (p == null)
						sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					else
					{
						if (args.length != 1)
						{
							String arena = StringUtil.getWord(args[1]);

							if (Lobby.getArenas().isEmpty())
								p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString("<arena>", "Default"));

							else if (Lobby.getArena(arena) == null)
								p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString("<arena>", arena));

							else
							{
								ip.setCreating(arena);
								p.sendMessage(Msgs.Command_Arena_Set.getString("<arena>", arena));
								return true;
							}
						} else
							p.sendMessage(Msgs.Help_SetArena.getString());

					}
				}
				// /////////////////////////////////////////////-FILES-/////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Files"))
				{
					if (!sender.hasPermission("Infected.Files"))
						sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					else
					{
						Configuration config = null;

						if (args.length > 1)
						{
							if (args[1].equalsIgnoreCase("Config"))
								config = Files.getConfig();
							else if (args[1].equalsIgnoreCase("Arenas"))
								config = Files.getArenas();
							else if (args[1].equalsIgnoreCase("Classes"))
								config = Files.getClasses();
							else if (args[1].equalsIgnoreCase("Grenades"))
								config = Files.getGrenades();
							else if (args[1].equalsIgnoreCase("Messages"))
								config = Files.getMessages();
							else if (args[1].equalsIgnoreCase("Players"))
								config = Files.getPlayers();
							else if (args[1].equalsIgnoreCase("Shop"))
								config = Files.getShop();
							else if (args[1].equalsIgnoreCase("Signs"))
								config = Files.getSigns();
							else
								sender.sendMessage(Msgs.Error_Misc_Not_A_File.getString("<files>", "Config, Arenas, Classes, Grenades, Messages, Players, Shop, Signs"));

						}
						if (args.length == 2)
						{

							if (config != null)
							{
								for (String path : config.getConfigurationSection("").getKeys(true))
								{
									if (!config.getString(path).startsWith("MemorySection"))
										sender.sendMessage(ChatColor.YELLOW + path.replaceAll(" ", "_") + ChatColor.WHITE + ": " + ChatColor.GRAY + config.getString(path).replaceAll(" ", "_"));
								}
							}
						} else if (args.length == 3)
						{
							String path = args[2].replaceAll("_", " ");

							if (config != null)
							{
								sender.sendMessage(Msgs.Command_Files_Value.getString("<path>", path, "<value>", config.getString(path).replaceAll("_", " ")));
							}
						} else if (args.length == 4)
						{
							String path = args[2].replaceAll("_", " ");
							String newvalue = args[3].replaceAll("_", " ");

							if (config != null)
							{
								if (config.get(path) != null)
								{
									sender.sendMessage(Msgs.Command_Files_Changed.getString("<path>", path, "<value>", config.getString(path), "<newvalue>", newvalue));
									if (newvalue.equalsIgnoreCase("True") || newvalue.equalsIgnoreCase("False"))
										config.set(path.replaceAll("_", " "), Boolean.valueOf(newvalue.toUpperCase()));
									else if (newvalue.startsWith(String.valueOf('[')) && newvalue.endsWith("]"))
									{
										p.sendMessage("List");
										String[] list = (newvalue.replaceAll("\\[", "").replaceAll("]", "")).split(",");
										config.set(path, list);
									} else
										try
										{
											int i = Integer.valueOf(newvalue);
											config.set(path, i);
										} catch (Exception ex)
										{
											config.set(path, newvalue);
										}
									Files.saveAll();
								} else
									sender.sendMessage(Msgs.Error_Misc_Not_A_Path.getString());

							} else
								sender.sendMessage(Msgs.Help_Files.getString("<files>", "Config, Abilities, Arenas, Classes, Grenades, Messages, Players, Shop, Signs"));

						} else
							sender.sendMessage(Msgs.Help_Files.getString("<files>", "Config, Abilities, Arenas, Classes, Grenades, Messages, Players, Shop, Signs"));
					}
				}
				// /////////////////////////////////////////////-ADDONS-/////////////////////////////////////////
				else if (args.length > 0 && args[0].equalsIgnoreCase("Addons"))
				{

					p.sendMessage("");
					p.sendMessage(Msgs.Format_Header.getString("<title>", " Addons "));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.DisguisesEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					if (Settings.DisguisesEnabled())
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Plugin:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + Main.Disguiser.getName());
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "CrackShot Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.CrackShotEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Factions Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.FactionsEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "mcMMO Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.mcMMOEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Vault Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.VaultEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					p.sendMessage(Msgs.Format_Line.getString());
				}
				// ///////////////////////////////////////////////-ELSE-//////////////////////////////////////////////
				else
				{
					if (args.length == 0)
					{
						p.sendMessage("");
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
						if (Main.update)
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + ChatColor.BOLD + "Update Available: " + ChatColor.WHITE + ChatColor.BOLD + Main.updateName);
						p.sendMessage("");
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_RED + "Author: " + ChatColor.GREEN + ChatColor.BOLD + "Sniperz");
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Version: " + ChatColor.GREEN + ChatColor.BOLD + Main.version);
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.WHITE + "BukkitDev: " + ChatColor.GREEN + ChatColor.BOLD + "http://bit.ly/QN6Xg5");
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Help type: /Infected Help");
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Addons type: /Infected Addons");
						p.sendMessage(Msgs.Format_Line.getString());
						return true;
					} else
						p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());
				}
			}
		}
		return true;
	}
}