
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
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Chat"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (args.length == 1)
				{
					if (!ip.isInfChatting())
					{
						ip.setInfChatting(true);
						p.sendMessage("Commands_Toggled_InfChat(<state>)");
					} else
					{
						ip.setInfChatting(false);
						p.sendMessage("Commands_Toggled_InfChat(<state>)");
					}
				} else
				{
					StringBuilder message = new StringBuilder(args[1]);
					for (int arg = 2; arg < args.length; arg++)
						message.append(" ").append(args[arg]);

					for (Player u : Bukkit.getOnlinePlayers())
						if (ip.getTeam() == InfPlayerManager.getInfPlayer(p).getTeam() || u.hasPermission("Infected.Chat.Spy"))
							u.sendMessage("Game_InfChat_Format(<player> <message>)");
				}
			}

			// //////////////////////////////////////////////-CLASSES-///////////////////////////////////////
			else if (args.length >= 1 && args[0].equalsIgnoreCase("Classes"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (!Files.getClasses().getBoolean("Enabled"))
					p.sendMessage("Error_Classes disabled");

				else
					Menus.chooseClassTeam(p);
			}

			// ///////////////////////////////////////////-JOIN-//////////////////////////////////////////

			else if (args.length > 0 && args[0].equalsIgnoreCase("Join"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Join"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (Lobby.getGameState() == GameState.Disabled)
					p.sendMessage("Error_Infected_Is_Disabled");

				else if (!plugin.getConfig().contains("Lobby"))
					p.sendMessage("Error_Missing_A_Lobby");

				else if (Lobby.getArenas().isEmpty() || !Lobby.isArenaValid(Lobby.getArenas().get(0)))
					p.sendMessage("Error_Missing_A_Valid_Arena");

				else
				{
					for (Player u : Lobby.getInGame())
						u.sendMessage(Msgs.Game_They_Joined_A_Game.getString("<player>", p.getName()));

					ip.setInfo();
					ip.tpToLobby();

					p.sendMessage(Msgs.Game_You_Joined_A_Game.getString());

					if (Lobby.getGameState() == GameState.InLobby && Lobby.getInGame().size() >= Settings.getRequiredPlayers())
					{
						Lobby.timerStartVote();
					} else if (Lobby.getGameState() == GameState.Voting)
					{
						p.sendMessage("Game_Vote_For_An_Arena");
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
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (Lobby.getGameState() == GameState.Disabled)
					p.sendMessage("Error_Infected_Is_Disabled");

				else
				{
					sender.sendMessage("");
					sender.sendMessage(Msgs.Format_Header.getString("<title>", "Status"));
					sender.sendMessage("Msgs.Info_In_Game - String.valueOf(Infected.listInGame().size())");
					sender.sendMessage("Msgs_Info_Game_Status");
					sender.sendMessage("Msgs_Inf_Time_Left");
				}
			}

			// /////////////////////////////////////////////////////////////
			// SUICIDE
			else if (args.length > 0 && args[0].equalsIgnoreCase("Suicide"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Join"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (Lobby.getGameState() != GameState.Started)
					p.sendMessage("Error_Games_Not_Started");

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
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Join"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (Lobby.getGameState() != GameState.Started)
					p.sendMessage("Error_Games_Not_Started");

				else
				{
					Menus.openShopMenu(p);
				}
			}
			// /////////////////////////////////////////////////-GRENADES-///////////////////////////////////////////////
			else if (args.length > 0 && (args[0].equalsIgnoreCase("Grenades") || args[0].equalsIgnoreCase("Grenade")))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Grenades"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (Lobby.getGameState() != GameState.Started)
					p.sendMessage("Error_Games_Not_Started");
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
									p.sendMessage("Just bought grenade");

								} else
									p.sendMessage("You don't have enough points to make this purchase!");
							} else
								p.sendMessage("Invalid Grenade Number");
						} else
							p.sendMessage("Invalid Grenade Number");
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
									p.sendMessage("Just bought <Amount> grenades");
								} else
									p.sendMessage("You don't have enough points to make this purchase!");
							} else
								p.sendMessage("Invalid Grenade Number");
						} else
							p.sendMessage("Invalid Grenade Number");
					} else
					{
						p.sendMessage(Msgs.Format_Header.getString("<title>", "Grenades"));

						int i = 1;
						for (Grenade g : GrenadeManager.getGrenades())
						{
							p.sendMessage("Grenages <number> <name> <cost>");
							i++;
						}

						p.sendMessage(Main.I + "To Buy Type: " + ChatColor.YELLOW + ChatColor.BOLD + "/Infected Grenades <ID>");
					}
				}
			}

			// /////////////////////////////////////////////////////-SETLOBBY-/////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("SetLobby"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Setup"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else
				{
					Lobby.setLocation(p.getLocation());
					p.sendMessage("Lobby Set");
				}
			}

			// ////////////////////////////////////////////////////-LIST-/////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("List"))
			{

				if (!p.hasPermission("Infected.List"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				if (args.length != 1)
				{
					if (args[1].equalsIgnoreCase("Playing"))
					{
						p.sendMessage(Msgs.Format_Header.getString("<title>", "Playing"));
						for (Player u : Lobby.getInGame())
							p.sendMessage(Main.I + ChatColor.YELLOW + "> " + u.getDisplayName());
					} else if (args[1].equalsIgnoreCase("Humans"))
					{
						p.sendMessage(Msgs.Format_Header.getString("<title>", "Humans"));
						for (Player u : Lobby.getHumans())
							p.sendMessage(Main.I + ChatColor.YELLOW + "> " + u.getDisplayName());

					} else if (args[1].equalsIgnoreCase("Zombies"))
					{
						p.sendMessage(Msgs.Format_Header.getString("<title>", "Zombies"));
						for (Player u : Lobby.getZombies())
							p.sendMessage(Main.I + ChatColor.YELLOW + "> " + u.getDisplayName());

					} else
						p.sendMessage("Unkown List <lists>");

				} else
					p.sendMessage("Unkown List <lists>");
			}
			// ///////////////////////////////////////////////-LEAVE-////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Leave"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Leave"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else
					ip.leaveInfected();

			}

			// ////////////////////////////////-HELP-///////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Help"))
			{
				if (!sender.hasPermission("Infected.Help"))
					sender.sendMessage(Msgs.Error_No_Permission.getString());

				else
				{
					if (args.length != 1)
					{
						sender.sendMessage("");
						sender.sendMessage(Main.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected Help" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
						sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join" + ChatColor.WHITE + " - Join Infected");
						sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave" + ChatColor.WHITE + " - Leave Infected");
						sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote" + ChatColor.WHITE + " - Vote for a map");
						sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes" + ChatColor.WHITE + " - Choose a class");
						if (sender.hasPermission("Infected.Grenades"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades" + ChatColor.WHITE + " - See the purchasable grenades");
						if (sender.hasPermission("Infected.Chat"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat" + ChatColor.WHITE + " - Chat in your team's chat");
						if (sender.hasPermission("Infected.Stats"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats" + ChatColor.WHITE + " - Check a p's stats");
						if (sender.hasPermission("Infected.Suicide"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide" + ChatColor.WHITE + " - Suicide if you're stuck");
						if (sender.hasPermission("Infected.Info"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info" + ChatColor.WHITE + " - Check Infected's status");
						if (sender.hasPermission("Infected.Top"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top" + ChatColor.WHITE + " - Check the top 5 ps");
						if (sender.hasPermission("Infected.Arenas"))
							sender.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas" + ChatColor.WHITE + " - See all possible arenas");
						if (sender.hasPermission("Infected.SetUp"))
						{
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby" + ChatColor.WHITE + " - Set the main lobby");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn" + ChatColor.WHITE + " - Set the spawn for the selected arena");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns" + ChatColor.WHITE + " - List the number of spawns for a map");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn" + ChatColor.WHITE + " - Tp to a spawn ID");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn" + ChatColor.WHITE + " - Delete the spawn ID");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena" + ChatColor.WHITE + " - Select an arena");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create" + ChatColor.WHITE + " - Create an arena");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove" + ChatColor.WHITE + " - Remove an Arena");
						}
						if (p.hasPermission("Infected.Admin"))
						{
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Start" + ChatColor.WHITE + " - Force the game to start");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "End" + ChatColor.WHITE + " - Force the game to end");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Admin" + ChatColor.WHITE + " - Show the admin menu");
							p.sendMessage(Main.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Refresh" + ChatColor.WHITE + " - Refresh all the ps");
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
								p.sendMessage(Main.I + "You already have the help book!");

						} else
						{
							p.sendMessage("");
							p.sendMessage(Main.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
							p.sendMessage(Main.I + ChatColor.YELLOW + "For Help type: /Infected Help 1");
						}
					}
				}
			}

			// ///////////////////////////////////////////////////////-VOTE-////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Vote"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Leave"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else if (!Lobby.getInGame().contains(p))
					p.sendMessage(Msgs.Error_Not_In_A_Game.getString());

				else if (Lobby.getGameState() != GameState.Voting)
					p.sendMessage("No Vote Now");

				else if (ip.getVote() != null)
					p.sendMessage("Already Voted");

				else
					Menus.openVotingMenu(p);

			}

			// //////////////////////////////////////////////////-START-////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Start"))
			{
				if (!sender.hasPermission("Infected.Force.Start"))
					sender.sendMessage(Msgs.Error_No_Permission.getString());

				else if (Lobby.getGameState() != GameState.InLobby)
					sender.sendMessage("No Start Now");

				else
					Lobby.timerStartVote();
			}

			// //////////////////////////////////////////////-END-////////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("End"))
			{
				if (!sender.hasPermission("Infected.Force.End"))
					sender.sendMessage(Msgs.Error_No_Permission.getString());

				else if (Lobby.getGameState() != GameState.InLobby)
					sender.sendMessage("No Start Now");

				else
					Game.endGame(true);
			}

			// ////////////////////////////////////////////////-ARENAS-/////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Arenas"))
			{

				if (!sender.hasPermission("Infected.Arenas"))
					sender.sendMessage(Msgs.Error_No_Permission.getString());

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

					sender.sendMessage("Possible Arenas  <valid> <invalid> ");
				}
			}
			// ///////////////////////////////////////////////////-ADMIN-///////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Admin"))
			{

				if (!sender.hasPermission("Infected.Admin"))
					sender.sendMessage(Msgs.Error_No_Permission.getString());

				if (args.length == 2)
				{
					// SHUTDOWN
					if (args[1].equalsIgnoreCase("Shutdown"))
					{
						if (Lobby.getGameState() != GameState.Disabled)
						{
							Lobby.setGameState(GameState.Disabled);
							p.sendMessage("Join Set False");
						} else
						{
							Lobby.setGameState(GameState.InLobby);
							p.sendMessage("Joining Infected has been enabled.");
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
						p.sendMessage("Infecteds Files have been reloaded");

					}
					// CODE
					else if (args[1].equalsIgnoreCase("Code"))
					{
						p.sendMessage(Main.I + "Code: " + ChatColor.WHITE + ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
						p.sendMessage(Main.I + "This code has also been sent to your console to allow for copy and paste!");
						System.out.println(ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
					} else
						p.sendMessage(Main.I + ChatColor.RED + "Unknown Admin Command, Type /Infected Admin");
				} else if (args.length == 3)
				{
					// KICK
					if (args[1].equalsIgnoreCase("Kick"))
					{
						Player u = Bukkit.getPlayer(args[2]);
						if (u == null || !Lobby.isInGame(u))
							u.sendMessage("not playing Infected");
						else
						{
							p.performCommand("Infected Leave");
							u.sendMessage("Kicked");
							p.sendMessage("kicked " + u.getName());
						}
					} else
						p.sendMessage(Main.I + ChatColor.RED + "Unknown Admin Command, Type /Infected Admin");

				} else if (args.length == 4)
				{
					String user = args[2];

					int i = Integer.parseInt(args[3]);
					if (args[1].equalsIgnoreCase("Points"))
					{
						int newValue = Stats.getPoints(user) + i;
						Stats.setPoints(user, newValue, Settings.VaultEnabled());
						p.sendMessage(user + "'s new points is: " + newValue);
					} else if (args[1].equalsIgnoreCase("Score"))
					{
						int newValue = Stats.getScore(user) + i;
						Stats.setScore(user, newValue);
						p.sendMessage(user + "'s new score is: " + newValue);
					} else if (args[1].equalsIgnoreCase("kStats"))
					{
						int newValue = Stats.getKills(user) + i;
						Stats.setKills(user, newValue);
						p.sendMessage(user + "'s new kill count is: " + newValue);
					} else if (args[1].equalsIgnoreCase("DStats"))
					{
						int newValue = Stats.getDeaths(user) + i;
						Stats.setDeaths(user, newValue);
						p.sendMessage(user + "'s new death count is: " + newValue);
					} else
						p.sendMessage(Main.I + ChatColor.RED + "Unknown Admin Command, Type /Infected Admin");

				} else
				{
					p.sendMessage(Msgs.Format_Header.getString("<title>", "Admin CMDs"));
					p.sendMessage(Main.I + ChatColor.AQUA + "/Inf Admin Points <Player> <#>");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add points to a p(Also goes negative)");
					p.sendMessage(Main.I + ChatColor.BLUE + "/Inf Admin Score <Player> <#>");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add score to a p(Also goes negative)");
					p.sendMessage(Main.I + ChatColor.DARK_AQUA + "/Inf Admin KStats <Player> <#>");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add kills to a p(Also goes negative)");
					p.sendMessage(Main.I + ChatColor.DARK_BLUE + "/Inf Admin DStats <Player> <#>");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add deaths to a p(Also goes negative)");
					p.sendMessage(Main.I + ChatColor.DARK_GRAY + "/Inf Admin Kick <Player>");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Kick a p out of Infected");
					p.sendMessage(Main.I + ChatColor.DARK_PURPLE + "/Inf Admin Shutdown");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Prevent joining Infected");
					p.sendMessage(Main.I + ChatColor.DARK_RED + "/Inf Admin Reload");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Reload the config");
					p.sendMessage(Main.I + ChatColor.GOLD + "/Inf Admin Code");
					p.sendMessage(Main.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "See Infected's item code for the item in hand");
				}
			}
			// /////////////////////////////////////////-STATS-///////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Stats"))
			{

				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");

				else if (!p.hasPermission("Infected.Stats"))
					p.sendMessage(Msgs.Error_No_Permission.getString());

				else
				{
					if (args.length != 1)
					{

						if (!p.hasPermission("Infected.Stats.Other"))
							p.sendMessage(Msgs.Error_No_Permission.getString());

						else
						{

							String user = args[1];

							p.sendMessage("");
							p.sendMessage(Msgs.Format_Header.getString("<title>", user));
							p.sendMessage(Main.I + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(user) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(user));
							p.sendMessage(Main.I + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getTime((long) Stats.getPlayingTime(user)));
							p.sendMessage(Main.I + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(user) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(user) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + MiscStats.KD(user));
							p.sendMessage(Main.I + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(user));
						}
					} else
					{
						String user = sender.getName();
						p.sendMessage("");
						p.sendMessage(Msgs.Format_Header.getString("<title>", user));
						p.sendMessage(Main.I + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(user) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(user));
						p.sendMessage(Main.I + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getTime((long) Stats.getPlayingTime(user)));
						p.sendMessage(Main.I + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(user) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(user) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + MiscStats.KD(user));
						p.sendMessage(Main.I + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(user));
					}
				}
			}

			// //////////////////////////////////////////-TPSPAWN-///////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("TpSpawn"))
			{
				
					if (p == null)
						sender.sendMessage("Msgs.Error_Not_A_Player");
				
					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_No_Permission.getString());

					else if(ip.getCreating() == null)
						p.sendMessage(Msgs.Error_Not_An_Arena.getString());
					
					else{
				if (args.length != 1)
				{

					Arena a = Lobby.getArena(ip.getCreating());
					int i = Integer.valueOf(args[1]) - 1;
					if (i < a.getSpawns().size())
					{
						p.teleport(LocationHandler.getPlayerLocation(a.getSpawns().get(i)));
						sender.sendMessage("You have teleported to spawn number " + (i + 1));
					} else
						sender.sendMessage("Infected doesn't know where your tying to go, check how many spawns this arena has again!");
				
					}else
					sender.sendMessage(Main.I + ChatColor.RED + "/Inf TpSpawn <ID>");
					}
			}
			// /////////////////////////////////////////////-TPLOBBY-////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("TpLobby"))
			{
					if (p == null)
						sender.sendMessage("Msgs.Error_Not_A_Player");
				
					else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_No_Permission.getString());
					
					else{
				p.teleport(Lobby.getLocation());
				p.sendMessage(Main.I + "You have teleported to the lobby.");
					}
			}

			// ////////////////////////////////////-DELSPAWN-////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("DelSpawn"))
			{		
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");
			
				else if (!p.hasPermission("Infected.Setup"))
						p.sendMessage(Msgs.Error_No_Permission.getString());

					else if(ip.getCreating() == null)
						p.sendMessage(Msgs.Error_Not_An_Arena.getString());
					
					else{
				if (args.length != 1)
				{

					Arena a = Lobby.getArena(ip.getCreating());
					int i = Integer.valueOf(args[1]) - 1;
					if (i < a.getSpawns().size())
					{
						List<String> spawns = a.getSpawns();
						spawns.remove(i);
						a.setSpawns(spawns);
						p.sendMessage(Main.I + ChatColor.RED + "You have removed spawn number " + (i + 1) + ".");
					} else
						p.sendMessage(Main.I + ChatColor.RED + "Check how many spawns this arena has again!");
				} else
				{
					p.sendMessage(Main.I + ChatColor.RED + "/Inf DelSpawn <ID>");
				}
					}
			}

			// //////////////////////////////////-SPAWNS-//////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Spawns"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");
			
				if (!p.hasPermission("Infected.Setup"))
					p.sendMessage(Msgs.Error_No_Permission.getString());
				
				else if(ip.getCreating() == null)
					p.sendMessage(Msgs.Error_Not_An_Arena.getString());
				
				else{

				Arena a = Lobby.getArena(ip.getCreating());
				p.sendMessage(ip.getCreating() + " has " + a.getSpawns().size() + " spawns.");
				}
			}

			// ////////////////////////////////////////////-SETSPAWN-//////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("SetSpawn"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");
			
				else if (!p.hasPermission("Infected.Setup"))
					p.sendMessage(Msgs.Error_No_Permission.getString());
				
				else if(ip.getCreating() == null)
					p.sendMessage(Msgs.Error_Not_An_Arena.getString());
				
				else{

				Location l = p.getLocation();
				String s = LocationHandler.getLocationToString(l);
				Arena a = Lobby.getArena(ip.getCreating());
				List<String> list = a.getSpawns();
				list.add(s);
				a.setSpawns(list);
				
				p.sendMessage(ip.getCreating() + " spawn #" +list.size() +  " set at your location!");
			}
			}

			// /////////////////////////////////////////-CREATE-///////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Create"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");
			
				else if (!p.hasPermission("Infected.Setup"))
					p.sendMessage(Msgs.Error_No_Permission.getString());
				
				else
				{
					if (args.length != 1)
					{
						
						String arena = StringUtil.getWord(args[1]);
						
						if(Lobby.getArena(arena) != null)
							p.sendMessage("This arena already exists");
					
						else{
							p.sendMessage(Main.I + ChatColor.DARK_AQUA + "Type " + ChatColor.YELLOW + "/Inf SetSpawn" + ChatColor.DARK_AQUA + " to finish the arena!");
				
							if (args.length == 3)
								Files.getArenas().set("Arenas." + arena + ".Creator", args[2]);
							else
								Files.getArenas().set("Arenas." + arena + ".Creator", "Unkown");

							Files.saveArenas();
							ip.setCreating(arena);
							p.sendMessage(Main.I + "Arena " + ChatColor.WHITE + ChatColor.BOLD + arena + ChatColor.LIGHT_PURPLE + " created.");
						}
					} 
					else
						p.sendMessage(Main.I + ChatColor.RED + "/Infected Create <Arena Name> [Creator]");
				}
			}

			// //////////////////////////////////////-REMOVE-////////////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Remove"))
			{

				if (!p.hasPermission("Infected.Setup"))
					p.sendMessage(Msgs.Error_No_Permission.getString());
				
				else
				{
					if (args.length != 1)
					{
						
						String arena = args[1];
						
						if (Lobby.getArena(arena) == null)
							sender.sendMessage(Main.I + "That arena doesn't exists!");
						
						else
						{
							Lobby.removeArena(Lobby.getArena(arena));
							sender.sendMessage(Main.I + "Arena deleted.");
							return true;
						}
					} else
						sender.sendMessage(Main.I + ChatColor.RED + "/Infected Remove <Arena Name>");
				}
			}

			// /////////////////////////////////////////////////-TOP-/////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Top"))
			{

				if (!p.hasPermission("Infected.Top"))
					p.sendMessage(Msgs.Error_No_Permission.getString());
				
				else
				{
					if (args.length != 1)
					{

						String stat = args[1].toLowerCase();
						if (StatType.valueOf(stat) == null)
							sender.sendMessage("Not A Stat, use " + StatType.values());
						else{
							StatType type = StatType.valueOf(stat);
							HashMap<String, Integer> top = MiscStats.getTop5(type);
							
							int i = 1;
							for(Entry<String, Integer> set : top.entrySet()){
								if(i == 1)
									sender.sendMessage(""+ChatColor.YELLOW + i +". " + set.getKey() +" - " + set.getValue());
								else if(i == 2)
									sender.sendMessage(""+ChatColor.GRAY + i +". " + set.getKey() +" - " + set.getValue());
								else if(i == 3)
									sender.sendMessage(""+ChatColor.GOLD + i +". " + set.getKey() +" - " + set.getValue());
								else
									sender.sendMessage(""+ChatColor.WHITE + i +". " + set.getKey() +" - " + set.getValue());
								i++;
								
								if(i ==6)
									break;
							}
								
						}
					}
				}
			}

			// ////////////////////////////////-SETARENA-/////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("SetArena"))
			{
				if (p == null)
					sender.sendMessage("Msgs.Error_Not_A_Player");
			
				else if (!p.hasPermission("Infected.Setup"))
					p.sendMessage(Msgs.Error_No_Permission.getString());
			 else
				{
					if (args.length != 1)
					{
						String arena = StringUtil.getWord(args[1]);
						
						if (Lobby.getArenas().isEmpty())
							p.sendMessage(Main.I + ChatColor.RED + " No arenas created!");
						
						else if (!Lobby.getArenas().contains(arena))
							p.sendMessage(Main.I + ChatColor.RED + "Invalid Arena");
						
						else
						{
							ip.setCreating(arena);
							p.sendMessage(Main.I + ChatColor.YELLOW + "Arena Set. Choosen Arena: " + ChatColor.GRAY + arena);
							return true;
						}
					} else
						p.sendMessage(Main.I + ChatColor.RED + "/Infected SetArena <Arena Name>");
					
				}
			}
			///////////////////////////////////////////////-ADDONS-/////////////////////////////////////////
			else if (args.length > 0 && args[0].equalsIgnoreCase("Addons"))
			{
				
				p.sendMessage("");
				p.sendMessage(Main.I + ChatColor.GRAY + "Disguise Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.DisguisesEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				if(Settings.DisguisesEnabled())
					p.sendMessage(Main.I + ChatColor.GRAY + "Disguise Plugin:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + Main.Disguiser.getName());
				p.sendMessage(Main.I + ChatColor.GRAY + "CrackShot Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.CrackShotEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Main.I + ChatColor.GRAY + "TagAPI Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.TagAPIEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Main.I + ChatColor.GRAY + "Factions Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.FactionsEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Main.I + ChatColor.GRAY + "mcMMO Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.mcMMOEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
				p.sendMessage(Main.I + ChatColor.GRAY + "Vault Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.VaultEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			}
			/////////////////////////////////////////////////-ELSE-//////////////////////////////////////////////
			else
			{
				p.sendMessage("");
				p.sendMessage(Main.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
				if (Main.update)
					p.sendMessage(Main.I + ChatColor.RED + ChatColor.BOLD + "Update Available: " + ChatColor.WHITE + ChatColor.BOLD + Main.name);
				p.sendMessage("");
				p.sendMessage(Main.I + ChatColor.GRAY + "Author: " + ChatColor.GREEN + ChatColor.BOLD + "xXSniperzzXx_SD");
				p.sendMessage(Main.I + ChatColor.GRAY + "Version: " + ChatColor.GREEN + ChatColor.BOLD + Main.v);
				p.sendMessage(Main.I + ChatColor.GRAY + "BukkitDev: " + ChatColor.GREEN + ChatColor.BOLD + "http://bit.ly/QN6Xg5");
				p.sendMessage(Main.I + ChatColor.YELLOW + "For Help type: /Infected Help");
				p.sendMessage(Main.I + ChatColor.YELLOW + "For Addons type: /Infected Addons");

				return true;
			}
		}
		return true;
	}
}