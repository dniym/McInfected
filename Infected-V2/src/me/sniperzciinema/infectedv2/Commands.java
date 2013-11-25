
package me.sniperzciinema.infectedv2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import me.sniperzciinema.infectedv2.Extras.Menus;
import me.sniperzciinema.infectedv2.Extras.ScoreBoard;
import me.sniperzciinema.infectedv2.GameMechanics.DeathType;
import me.sniperzciinema.infectedv2.GameMechanics.Deaths;
import me.sniperzciinema.infectedv2.GameMechanics.Equip;
import me.sniperzciinema.infectedv2.GameMechanics.MiscStats;
import me.sniperzciinema.infectedv2.GameMechanics.Stats;
import me.sniperzciinema.infectedv2.GameMechanics.Stats.StatType;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;
import me.sniperzciinema.infectedv2.Handlers.Arena.Arena;
import me.sniperzciinema.infectedv2.Handlers.Grenades.Grenade;
import me.sniperzciinema.infectedv2.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infectedv2.Handlers.Misc.ItemHandler;
import me.sniperzciinema.infectedv2.Handlers.Misc.LocationHandler;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;
import me.sniperzciinema.infectedv2.Messages.Msgs;
import me.sniperzciinema.infectedv2.Messages.StringUtil;
import me.sniperzciinema.infectedv2.Messages.Time;
import me.sniperzciinema.infectedv2.Tools.Files;
import me.sniperzciinema.infectedv2.Tools.Settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
					p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString("<arena>", "Default"));

				else
				{
					for (Player u : Lobby.getInGame())
						u.sendMessage(Msgs.Game_Joined_They.getString("<player>", p.getName()));

					ip.setInfo();
					ip.tpToLobby();

					p.sendMessage(Msgs.Game_Joined_You.getString());

					if (Lobby.getGameState() == GameState.InLobby && Lobby.getInGame().size() >= Settings.getRequiredPlayers())
					{
						Lobby.timerStartVote();
					} else if (Lobby.getGameState() == GameState.Voting)
					{
						p.sendMessage(Msgs.Help_Vote.getString());
						p.performCommand("Infected Arenas");

					} else if (Lobby.getGameState() == GameState.Infecting)
					{
						ip.respawn();
						Equip.equip(p);
					} else if (Lobby.getGameState() == GameState.Started)
					{
						ip.respawn();
						ip.Infect();
					}
					ScoreBoard.updateScoreBoard();
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
					p.sendMessage(Msgs.Error_Game_Started.getString());

				else
				{
					if (ip.getTeam() == Team.Human)
						ip.Infect();

					Deaths.playerDies(DeathType.Other, null, p);
					ip.respawn();
				}
			}
			// //////////////////////////////////////////////////////////////////////////////
			// SHOP / STORE
			else if (args.length > 0 && (args[0].equalsIgnoreCase("Shop") || args[0].equalsIgnoreCase("Store")))
			{
				if (p == null)
					sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

				else if (!p.hasPermission("Infected.Join"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Game_Not_In.getString());

				else if (Lobby.getGameState() != GameState.Started)
					p.sendMessage(Msgs.Error_Game_Started.getString());

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

				else if (Lobby.getGameState() != GameState.Started)
					p.sendMessage(Msgs.Error_Game_Started.getString());
				else
				{
					if (args.length == 1)
					{
						if (args[1].matches("[0-9]+"))
						{
							int gi = Integer.parseInt(args[1]) - 1;
							if (GrenadeManager.getGrenades().get(gi) != null)
							{
								if (ip.getPoints() >= GrenadeManager.getGrenade(gi).getCost())
								{
									Grenade grenade = GrenadeManager.getGrenade(gi);
									p.getInventory().addItem(grenade.getItemStack());
									ip.setPoints(ip.getPoints() - grenade.getCost(), Settings.VaultEnabled());
									p.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));

								} else
									p.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
							} else
								p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
						} else
							p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
					} else if (args.length == 2)
					{
						int amount = Integer.parseInt(args[2]);

						if (args[1].matches("[0-9]+"))
						{
							int gi = Integer.parseInt(args[1]) - 1;
							if (GrenadeManager.getGrenades().get(gi) != null)
							{
								if (ip.getPoints() >= GrenadeManager.getGrenade(gi).getCost() * amount)
								{
									Grenade grenade = GrenadeManager.getGrenade(gi);
									ItemStack g = grenade.getItemStack();
									g.setAmount(amount);
									p.getInventory().addItem(g);
									ip.setPoints(ip.getPoints() - (grenade.getCost() * amount), Settings.VaultEnabled());
									p.sendMessage(Msgs.Grenades_Bought.getString("<name>", grenade.getName()));
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
							p.sendMessage(Msgs.Grenades_List.getString("<id>", String.valueOf(i), "<name>", g.getName(), "<cost>", String.valueOf(g.getCost())));
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
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected Help" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join" + ChatColor.WHITE + " - Join Infected");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave" + ChatColor.WHITE + " - Leave Infected");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote" + ChatColor.WHITE + " - Vote for a map");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes" + ChatColor.WHITE + " - Choose a class");
						if (sender.hasPermission("Infected.Grenades"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades" + ChatColor.WHITE + " - See the purchasable grenades");
						if (sender.hasPermission("Infected.Chat"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat" + ChatColor.WHITE + " - Chat in your team's chat");
						if (sender.hasPermission("Infected.Stats"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats" + ChatColor.WHITE + " - Check a p's stats");
						if (sender.hasPermission("Infected.Suicide"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide" + ChatColor.WHITE + " - Suicide if you're stuck");
						if (sender.hasPermission("Infected.Info"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info" + ChatColor.WHITE + " - Check Infected's status");
						if (sender.hasPermission("Infected.Top"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top" + ChatColor.WHITE + " - Check the top 5 ps");
						if (sender.hasPermission("Infected.Arenas"))
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas" + ChatColor.WHITE + " - See all possible arenas");
						if (sender.hasPermission("Infected.SetUp"))
						{
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby" + ChatColor.WHITE + " - Set the main lobby");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn" + ChatColor.WHITE + " - Set the spawn for the selected arena");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns" + ChatColor.WHITE + " - List the number of spawns for a map");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn" + ChatColor.WHITE + " - Tp to a spawn ID");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn" + ChatColor.WHITE + " - Delete the spawn ID");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena" + ChatColor.WHITE + " - Select an arena");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create" + ChatColor.WHITE + " - Create an arena");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove" + ChatColor.WHITE + " - Remove an Arena");
						}
						if (p.hasPermission("Infected.Admin"))
						{
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Start" + ChatColor.WHITE + " - Force the game to start");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "End" + ChatColor.WHITE + " - Force the game to end");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Admin" + ChatColor.WHITE + " - Show the admin menu");
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Refresh" + ChatColor.WHITE + " - Refresh all the ps");
						}
					} else
					{
						if (p != null && Settings.useBookForHelp())
						{
							ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
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

				else if (Lobby.getGameState() != GameState.Voting)
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

				else if (Lobby.getGameState() != GameState.InLobby)
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

					sender.sendMessage(Msgs.Command_Arenas.getString("<valid>", valid.toString(), "<invalid>", inValid.toString()));
				}
			}
			// ///////////////////////////////////////////////////-ADMIN-///////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Admin"))
			{

				if (!sender.hasPermission("Infected.Admin"))
					sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

				if (args.length == 2)
				{
					// SHUTDOWN
					if (args[1].equalsIgnoreCase("Shutdown"))
					{
						if (Lobby.getGameState() != GameState.Disabled)
						{
							Lobby.setGameState(GameState.Disabled);
							p.sendMessage(Msgs.Command_Admin_Shutdown.getString("<state>", "Disabled"));
						} else
						{
							Lobby.setGameState(GameState.InLobby);
							p.sendMessage(Msgs.Command_Admin_Shutdown.getString("<state>", "Enabled"));
						}
					}
					// RELOAD
					else if (args[1].equalsIgnoreCase("Reload"))
					{
						System.out.println("===== Infected =====");
						Files.reloadAbilities();
						Files.reloadArenas();
						Files.reloadClasses();
						Files.reloadConfig();
						Files.reloadGrenades();
						Files.reloadKills();
						Files.reloadMessages();
						Files.reloadPlayers();
						Files.reloadShop();
						Files.reloadSigns();
						Main.addon.getAddons();
						System.out.println("====================");
						p.sendMessage(Msgs.Command_Admin_Reload.getString());

					}
					// CODE
					else if (args[1].equalsIgnoreCase("Code"))
					{
						p.sendMessage(Msgs.Format_Prefix.getString() + "Code: " + ChatColor.WHITE + ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
						p.sendMessage(Msgs.Format_Prefix.getString() + "This code has also been sent to your console to allow for copy and paste!");
						System.out.println(ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
					} else
						p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());
				} else if (args.length == 3)
				{
					// KICK
					if (args[1].equalsIgnoreCase("Kick"))
					{
						Player u = Bukkit.getPlayer(args[2]);
						if (u == null || !Lobby.isInGame(u))
							u.sendMessage(Msgs.Error_Game_Not_In.getString());
						else
						{
							p.performCommand("Infected Leave");
							u.sendMessage(Msgs.Command_Admin_Kicked_You.getString());
							p.sendMessage(Msgs.Command_Admin_Kicked_Them.getString("<player>", u.getName()));
						}
					} else
						p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());

				} else if (args.length == 4)
				{
					String user = args[2];

					int i = Integer.parseInt(args[3]);
					if (args[1].equalsIgnoreCase("Points"))
					{
						int newValue = Stats.getPoints(user) + i;
						Stats.setPoints(user, newValue, Settings.VaultEnabled());
						p.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "stat", "points", "<value>", String.valueOf(newValue)));
					} else if (args[1].equalsIgnoreCase("Score"))
					{
						int newValue = Stats.getScore(user) + i;
						Stats.setScore(user, newValue);
						p.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "stat", "score", "<value>", String.valueOf(newValue)));
					} else if (args[1].equalsIgnoreCase("kStats"))
					{
						int newValue = Stats.getKills(user) + i;
						Stats.setKills(user, newValue);
						p.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "stat", "kills", "<value>", String.valueOf(newValue)));
					} else if (args[1].equalsIgnoreCase("DStats"))
					{
						int newValue = Stats.getDeaths(user) + i;
						Stats.setDeaths(user, newValue);
						p.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "stat", "deaths", "<value>", String.valueOf(newValue)));
					} else
						p.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());

				} else
				{
					p.sendMessage(Msgs.Format_Header.getString("<title>", "Admin CMDs"));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.AQUA + "/Inf Admin Points <Player> <#>");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add points to a player(Also goes negative)");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.BLUE + "/Inf Admin Score <Player> <#>");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add score to a player(Also goes negative)");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + "/Inf Admin KStats <Player> <#>");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add kills to a player(Also goes negative)");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_BLUE + "/Inf Admin DStats <Player> <#>");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add deaths to a player(Also goes negative)");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_GRAY + "/Inf Admin Kick <Player>");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Kick a player out of Infected");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_PURPLE + "/Inf Admin Shutdown");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Prevent joining Infected");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_RED + "/Inf Admin Reload");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Reload the config");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GOLD + "/Inf Admin Code");
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "See Infected's item code for the item in hand");
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
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(user) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(user));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getTime((long) Stats.getPlayingTime(user)));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(user) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(user) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + MiscStats.KD(user));
							p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(user));
						}
					} else
					{
						String user = sender.getName();
						p.sendMessage("");
						p.sendMessage(Msgs.Format_Header.getString("<title>", user));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(user) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(user));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getTime((long) Stats.getPlayingTime(user)));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(user) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(user) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + MiscStats.KD(user));
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
							Lobby.addArena(arena);
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
					if (args.length != 1)
					{

						String stat = args[1].toLowerCase();
						if (StatType.valueOf(stat) == null)
							sender.sendMessage(Msgs.Error_Top_Not_Stat.getString("<stats>", String.valueOf(StatType.values())));
						else
						{
							StatType type = StatType.valueOf(stat);
							HashMap<String, Integer> top = MiscStats.getTop5(type);

							int i = 1;
							for (Entry<String, Integer> set : top.entrySet())
							{
								if (i == 1)
									sender.sendMessage("" + ChatColor.YELLOW + i + ". " + set.getKey() + " - " + set.getValue());
								else if (i == 2)
									sender.sendMessage("" + ChatColor.GRAY + i + ". " + set.getKey() + " - " + set.getValue());
								else if (i == 3)
									sender.sendMessage("" + ChatColor.GOLD + i + ". " + set.getKey() + " - " + set.getValue());
								else
									sender.sendMessage("" + ChatColor.WHITE + i + ". " + set.getKey() + " - " + set.getValue());
								i++;

								if (i == 6)
									break;
							}

						}
					}else
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

						else if (!Lobby.getArenas().contains(arena))
							p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

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
			// /////////////////////////////////////////////-ADDONS-/////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Addons"))
			{

				p.sendMessage("");
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.DisguisesEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				if (Settings.DisguisesEnabled())
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Plugin:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + Main.Disguiser.getName());
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "CrackShot Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.CrackShotEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "TagAPI Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.TagAPIEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Factions Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.FactionsEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "mcMMO Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.mcMMOEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Vault Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.VaultEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			}
			// ///////////////////////////////////////////////-ELSE-//////////////////////////////////////////////
			else
			{
				p.sendMessage("");
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
				if (Main.update)
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + ChatColor.BOLD + "Update Available: " + ChatColor.WHITE + ChatColor.BOLD + Main.name);
				p.sendMessage("");
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Author: " + ChatColor.GREEN + ChatColor.BOLD + "xXSniperzzXx_SD");
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Version: " + ChatColor.GREEN + ChatColor.BOLD + Main.v);
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "BukkitDev: " + ChatColor.GREEN + ChatColor.BOLD + "http://bit.ly/QN6Xg5");
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Help type: /Infected Help");
				p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Addons type: /Infected Addons");

				return true;
			}
		}
		return true;
	}
}