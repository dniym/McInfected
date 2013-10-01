
package me.xxsniperzzxx_sd.infected;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.Main.GameState;
import me.xxsniperzzxx_sd.infected.Listeners.CrackShotApi;
import me.xxsniperzzxx_sd.infected.Listeners.TagApi;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerJoinEvent;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.Game;
import me.xxsniperzzxx_sd.infected.GameMechanics.Menus;
import me.xxsniperzzxx_sd.infected.GameMechanics.Reset;
import me.xxsniperzzxx_sd.infected.GameMechanics.ScoreBoard;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Updater;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Commands implements CommandExecutor {

	Main plugin;

	public Commands(Main plugin)
	{
		this.plugin = plugin;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("Infected"))
		{
			// Set the basics
			String creating = plugin.Creating.get(sender.getName());
			/*if (sender.getName().equalsIgnoreCase("xXSniperzzXx_SD") && args.length >= 1 && args[0].equalsIgnoreCase("Test"))
			{
				// TESTING STUFF
			}*/
			// /////////////////////////////////////////////////////////////////////////////////////////////////
			// CHAT
			 if (args.length >= 1 && args[0].equalsIgnoreCase("Chat"))
			{
				// Is the sender a player
				if (!(sender instanceof Player))
				{
					sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
					return true;
				}
				Player player = (Player) sender;
				if (!player.hasPermission("Infected.Chat"))
				{
					player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
					return true;
				} else if (!Infected.isPlayerInGame(player) || Infected.isPlayerInLobby(player) || Infected.getGameState() != GameState.STARTED)
				{
					player.sendMessage(Methods.sendMessage("Error_NotInGame", player, null, null));
					return true;
				} else if (args.length == 1)
				{
					if (!plugin.infectedChat.contains(player.getName()))
					{
						plugin.infectedChat.add(player.getName());
						player.sendMessage(plugin.I + "Toggled " + ChatColor.GREEN + "in " + ChatColor.GRAY + "to Infected's Chat");
					} else if (plugin.infectedChat.contains(player.getName()))
					{
						player.sendMessage(plugin.I + "Toggled " + ChatColor.RED + "out " + ChatColor.GRAY + "of Infected's Chat");
						plugin.infectedChat.remove(player.getName());
					}

					return true;
				}
				StringBuilder message = new StringBuilder(args[1]);
				for (int arg = 2; arg < args.length; arg++)
				{
					message.append(" ").append(args[arg]);
				}
				for (Player all : Bukkit.getOnlinePlayers())
				{
					System.out.println(Infected.playerGetGroup(all) + " " + all.getDisplayName());
					if (Infected.playerGetGroup(player) == Infected.playerGetGroup(all) || all.hasPermission("Infected.Chat.Spy"))
					{
						all.sendMessage(plugin.I + ChatColor.GRAY + "[" + Infected.playerGetGroup(player) + "] " + ChatColor.DARK_GREEN + player.getName() + ": " + ChatColor.WHITE + message);
					}
				}
			}
			// //////////////////////////////////////////////////////////////
			// CLASSES
			else if (args.length >= 1 && args[0].equalsIgnoreCase("Classes"))
			{

				if (!(sender instanceof Player))
				{
					sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
					return true;
				}
				Player player = (Player) sender;
				if (Infected.getGameState() == GameState.STARTED)
				{
					player.sendMessage(Methods.sendMessage("Error_GameStarted", player, null, null));
					return true;
				}
				if (!Infected.isPlayerInLobby(player))
				{
					player.sendMessage(Methods.sendMessage("Error_NotInGame", player, null, null));
					return true;
				}
				if (!plugin.getConfig().getBoolean("Class Support"))
				{
					player.sendMessage(Methods.sendMessage("Error_NoClassSupport", player, null, null));
					return true;
				} else if (args.length == 2)
				{
					if (!(Infected.filesGetClasses().getConfigurationSection("Classes.Zombie") == null) && !(Infected.filesGetClasses().getConfigurationSection("Classes.Human") == null))
					{
						if (args[1].equalsIgnoreCase("Human"))
						{
							Menus.openHumanMenu(player);
						} else if (args[1].equalsIgnoreCase("Zombie"))
						{

							Menus.openZombieMenu(player);
						} else
							player.sendMessage(plugin.I + ChatColor.RED + "/Inf Classes <Zombie/Human>");
					} else
						player.sendMessage(plugin.I + ChatColor.RED + "No Classes were found...");
				} else
					player.sendMessage(plugin.I + ChatColor.RED + "/Inf Classes <Zombie/Human>");
			}
			// ///////////////////////////////////////////////////////////////////////////////////////////////////
			// JOIN
			else if (args.length == 1)
			{
				if (args[0].equalsIgnoreCase("Join"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (Infected.getGameState() == GameState.DISABLED)
					{
						player.sendMessage(Methods.sendMessage("Error_InfectedDisabled", null, null, null));
						return true;
					} else if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else if (plugin.inLobby.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Error_AlreadyInGame", null, null, null));
						return true;
					} else if (plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Error_AlreadyInGame", null, null, null));
						return true;
					} else if (!plugin.getConfig().contains("Lobby"))
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Missing Lobby's positions!");
						return true;
					}
					Infected.filesReloadArenas();
					plugin.possibleArenas.clear();
					if (Infected.filesGetArenas().getConfigurationSection("Arenas") == null)
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Missing an arena!");
						return true;
					}
					for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
					{
						// Check if the string matchs an arena
						if (!parenas.contains("."))
						{
							plugin.possibleArenas.add(parenas);
						}
						if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
						{
							plugin.possibleArenas.remove(parenas);
						}
					}
					if (plugin.possibleArenas.isEmpty())
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Missing an arena with spawn points!");
						return true;
					}
					if (plugin.getConfig().getBoolean("Prevent Joining During Game"))
					{
						if (Infected.getGameState() == GameState.STARTED)
						{
							player.sendMessage(Methods.sendMessage("Error_JoinWellStartedBlocked", null, null, null));
							return true;
						}
					}
					for (Player all : Bukkit.getServer().getOnlinePlayers())
					{
						if (plugin.inGame.contains(all.getName()))
						{
							all.sendMessage(Methods.sendMessage("Lobby_OtherJoinedLobby", player, null, null));
						}
					}

					// Safe player's stats/stuff
					Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerJoinEvent(
							player,
							plugin.inLobby,
							plugin.getConfig().getInt("Automatic Start.Minimum Players")));
					plugin.inGame.add(player.getName());
					plugin.Spot.put(player.getName(), player.getLocation());
					plugin.Health.put(player.getName(), player.getHealth());
					plugin.Food.put(player.getName(), player.getFoodLevel());
					player.teleport(Methods.getLocation(plugin.getConfig().getString("Lobby")));
					plugin.Levels.put(player.getName(), player.getLevel());
					plugin.Exp.put(player.getName(), player.getExp());
					plugin.Inventory.put(player.getName(), player.getInventory().getContents());
					plugin.Armor.put(player.getName(), player.getInventory().getArmorContents());
					plugin.Winners.clear();
					plugin.inLobby.add(player.getName());
					plugin.gamemode.put(player.getName(), player.getGameMode().toString());

					if (plugin.config.getBoolean("ScoreBoard Support"))
					{
						ScoreBoard.updateScoreBoard();
					}

					if (plugin.config.getBoolean("Disguise Support.Enabled"))
						if (Disguises.isPlayerDisguised(player))
							Disguises.unDisguisePlayer(player);

					// Prepare player
					player.setMaxHealth(20.0);
					player.setHealth(20.0);
					player.setFoodLevel(20);
					for (PotionEffect reffect : player.getActivePotionEffects())
					{
						player.removePotionEffect(reffect.getType());
					}
					Methods.resetPlayersInventory(player);
					Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
					if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
						if (Infected.filesGetShop().getBoolean("Save Items") && Infected.playerGetShopInventory(player) != null)
							player.getInventory().setContents(Infected.playerGetShopInventory(player));
					}
					player.sendMessage(Methods.sendMessage("Lobby_JoinLobby", null, null, null));
					
					player.setGameMode(GameMode.ADVENTURE);
					player.setFlying(false);

					if(!Infected.playergetLastHumanClass(player).equalsIgnoreCase("None"))
						plugin.humanClasses.put(player.getName(), Infected.playergetLastHumanClass(player));
					
					if(!Infected.playergetLastZombieClass(player).equalsIgnoreCase("None"))
						plugin.zombieClasses.put(player.getName(), Infected.playergetLastZombieClass(player));

					if (!plugin.KillStreaks.containsKey(player.getName()))
						plugin.KillStreaks.put(player.getName(), Integer.valueOf("0"));

					if (plugin.inGame.size() >= plugin.getConfig().getInt("Automatic Start.Minimum Players") && Infected.getGameState() == GameState.INLOBBY && plugin.getConfig().getBoolean("Automatic Start.Use"))
					{
						Game.START();
						return true;
					}
					if (Infected.getGameState() == GameState.VOTING)
					{
						player.sendMessage(Methods.sendMessage("Vote_HowToVote", null, null, null));
						player.performCommand("Infected Arenas");
						return true;
					}
					if (Infected.getGameState() == GameState.STARTED)
					{
						player.setGameMode(GameMode.SURVIVAL);
						Methods.joinInfectHuman(player);
						Infected.delPlayerInLobby(player);
						return true;
					}
					if (Infected.getGameState() == GameState.BEFOREINFECTED)
					{
						if (plugin.config.getBoolean("ScoreBoard Support"))
						{
							player.setGameMode(GameMode.SURVIVAL);
							ScoreBoard.updateScoreBoard();
						}
						Methods.respawn(player);
						if (!plugin.Winners.contains(player.getName()))
						{
							plugin.Winners.add(player.getName());
						}
						plugin.Timein.put(player.getName(), System.currentTimeMillis() / 1000);
						if (!plugin.KillStreaks.containsKey(player.getName()))
							plugin.KillStreaks.put(player.getName(), Integer.valueOf("0"));
						player.setHealth(20.0);
						player.setFoodLevel(20);
						player.playEffect(player.getLocation(), Effect.SMOKE, BlockFace.UP);
						Equip.equipHumans(player);
						Infected.delPlayerInLobby(player);
						return true;
					}

					return true;
				}

				// /////////////////////////////////////////////////////////////////////////////////////////Refresh
				else if (args[0].equalsIgnoreCase("Refresh"))
				{

					if (!sender.hasPermission("Infected.Refresh"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					for (Player playing : Bukkit.getOnlinePlayers())
					{
						if (plugin.inGame.contains(playing.getName()) || plugin.inLobby.contains(playing.getName()))
						{
							playing.teleport(playing.getLocation());
						}
					}
					sender.sendMessage("Refreshed all the players everyone playing should see.");
				}
				// /////////////////////////////////////////////////////////////////////////////////////////INFO
				else if (args[0].equalsIgnoreCase("Info"))
				{

					if (!sender.hasPermission("Infected.Info"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}

					String status = "";
					if (Infected.getGameState() == GameState.VOTING)
						status = "Voting";
					if (Infected.getGameState() == GameState.BEFOREINFECTED)
						status = "B4 Infected";
					if (Infected.getGameState() == GameState.STARTED)
						status = "Started";
					else
						status = "In Lobby";
					int time = plugin.currentTime;
					sender.sendMessage("");
					sender.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected Status" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
					sender.sendMessage(plugin.I + ChatColor.GREEN + "Currently in Infected: " + ChatColor.DARK_GREEN + String.valueOf(Infected.listInGame().size()));
					sender.sendMessage(plugin.I + ChatColor.AQUA + "Current game status: " + ChatColor.GOLD + status);
					if (status.equalsIgnoreCase("Started"))
						sender.sendMessage(plugin.I + ChatColor.WHITE + "Time Left: " + ChatColor.YELLOW + String.valueOf(time));
					sender.sendMessage("");
				}
				// /////////////////////////////////////////////////////////////////////////////////////////SUICIDE
				else if (args[0].equalsIgnoreCase("Suicide"))
				{

					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					} else if (!sender.hasPermission("Infected.Suicide"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					Player player = (Player) sender;

					if (plugin.inGame.contains(player.getName()) && Infected.getGameState() == GameState.STARTED)
					{
						if (plugin.humans.contains(player))
							for (Player playing : Bukkit.getServer().getOnlinePlayers())
							{
								if ((!(playing == player)) && plugin.inGame.contains(playing.getName()))
									playing.sendMessage(Methods.sendMessage("Game_GotInfected", player, null, null));
							}
						plugin.humans.remove(player.getName());
						if (!plugin.zombies.contains(player.getName()))
							plugin.zombies.add(player.getName());
						plugin.Winners.remove(player.getName());
						plugin.Lasthit.remove(player.getName());
						player.sendMessage(plugin.I + "You have become infected!");
						player.addPotionEffect(new PotionEffect(
								PotionEffectType.CONFUSION, 20, 2));
						Equip.equipZombies(player);
						player.setHealth(20.0);
						player.setFoodLevel(20);
						Methods.stats(player, 0, 1);
						if (plugin.KillStreaks.get(player.getName()) > Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".KillStreak"))
						{
							Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(player.getName()));
							Files.savePlayers();
						}

						plugin.KillStreaks.put(player.getName(), 0);

						if (plugin.humans.size() == 0)
						{
							Game.endGame(false);
						} else
						{
							player.setFallDistance(0F);
							Methods.respawn(player);
							player.setFallDistance(0F);
						}
						Methods.zombifyPlayer(player);
					} else
					{
						// If the player tries to suicide and they arnt in the
						// lobby
						player.sendMessage(Methods.sendMessage("Error_NotInGame", player, null, null));
						return true;
					}
				}
				// /////////////////////////////////////////////////////////////////////////////////////////SHOP
				else if (args[0].equalsIgnoreCase("Shop"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!Infected.filesGetGrenades().getBoolean("Use"))
					{
						player.sendMessage(Methods.sendMessage("Grenade_Disabled", null, null, null));
					} else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Grenade_OnlyBuyInGame", null, null, null));
						return true;
					} else if (!player.hasPermission("Infected.Grenades"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else if (plugin.getConfig().getBoolean("Grenades.Only Humans Can Use") && plugin.zombies.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Grenades_NoZombies", null, null, null));
						return true;
					} else
					{
						player.sendMessage(plugin.I + ChatColor.GREEN + ChatColor.UNDERLINE + "======" + ChatColor.GOLD + " Infected's Grenades Shop" + ChatColor.GREEN + ChatColor.UNDERLINE + " ======");

						if (Infected.filesGetGrenades().getKeys(true) == null)
						{
							player.sendMessage(plugin.I + ChatColor.RED + " No Grenades were found...");
							return true;
						}
						String gname = null;
						int gcost = 0;
						int i = 0;
						for (String grenades : Infected.filesGetGrenades().getKeys(true))
						{
							// Check if the string matchs an arena
							if (!grenades.contains("."))
							{
								if (grenades.matches("[0-9]+"))
								{
									gname = Infected.filesGetGrenades().getString(grenades + ".Name");
									if (player.hasPermission("Infected.Grenades") || player.hasPermission("Infected.Grenades." + gname))
									{
										gcost = Infected.filesGetGrenades().getInt(grenades + ".Cost");
										player.sendMessage(ChatColor.GRAY + String.valueOf(i) + ". " + ChatColor.DARK_AQUA + gname + ChatColor.DARK_GRAY + " - " + ChatColor.AQUA + gcost);
										i++;
									}
								}
							}
						}
						player.sendMessage(plugin.I + "To Buy Type: " + ChatColor.YELLOW + "/Infected Shop <ID>");
					}
				}
				// /////////////////////////////////////////////////////////////////////////////////////////////////
				// SETLOBBY
				else if (args[0].equalsIgnoreCase("SetLobby"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.setup"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// Set Lobby's Warp
					Location loc = player.getLocation();
					int ix = (int) loc.getX();
					int iy = (int) loc.getY();
					int iz = (int) loc.getZ();
					World world = loc.getWorld();
					float yaw = loc.getYaw();
					float pitch = loc.getPitch();
					String s = world.getName() + "," + ix + "," + iy + "," + iz + "," + yaw + "," + pitch;
					plugin.getConfig().set("Lobby", s);
					plugin.saveConfig();
					player.sendMessage(plugin.I + "Lobby set!");
					return true;
					// ///////////////////////////////////////////////////////////////////////////////////////////
					// LIST without args
				} else if (args[0].equalsIgnoreCase("List"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// List possible lists
					player.sendMessage(plugin.I + ChatColor.RED + "Unknown List, Possible Lists: Playing, Humans, Zombies");
					return true;

					// ///////////////////////////////////////////////////////////////////////////////////////////////////
					// LEAVE
				} else if (args[0].equalsIgnoreCase("Leave"))
				{
					if (plugin.getConfig().getBoolean("Force Join.Enable"))
					{
						if (plugin.getConfig().getBoolean("Force Join.Disable Leave Command"))
							return true;
					}
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else if (plugin.inGame.contains(player.getName()) || plugin.inLobby.contains(player.getName()))
					{

						ScoreBoard.updateScoreBoard();

						// If a player logs out well playing, make sure it
						// doesn't effect the game, end the game if it does
						plugin.inGame.remove(player.getName());
						// Leave well everyones in the lobby
						if (Infected.getGameState() == GameState.INLOBBY)
						{
							if (plugin.config.getBoolean("Debug"))
							{
								System.out.println("Leave: Leaving, wellin lobby, no timers active");
							}
							player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
							Reset.resetp(player);
							for (Player players : Bukkit.getOnlinePlayers())
							{
								if (Infected.isPlayerInGame(players))
									players.sendMessage(Methods.sendMessage("Leave_NoEffect", player, null, null));
							}
						}
						// Voting has started, less then 2 people left
						else if (Infected.getGameState() == GameState.VOTING)
						{
							if (plugin.config.getBoolean("Debug"))
							{
								System.out.println("Leave: Before Voting");
							}
							if (plugin.inGame.size() == 1)
							{
								if (plugin.config.getBoolean("Debug"))
								{
									System.out.println("Leave: Before Voting(Triggered)");
								}
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", player, null, null));
										Reset.tp2LobbyAfter(players);
									}
								}
								Reset.resetInf();
							} else
							{
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NoEffect", player, null, null));
									}
								}
							}
						}
						// In Arena, before first Infected
						else if (Infected.getGameState() == GameState.BEFOREINFECTED)
						{
							if (plugin.config.getBoolean("Debug"))
							{
								System.out.println("Leave: In Arena Before Infected");
							}
							if (plugin.inGame.size() == 1)
							{
								if (plugin.config.getBoolean("Debug"))
								{
									System.out.println("Leave: In Arena Before Infected(Triggered)");
								}
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", player, null, null));
										Reset.tp2LobbyAfter(players);
									}
								}
								Reset.resetInf();
							} else
							{
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NoEffect", player, null, null));
									}
								}
							}
						}
						// In Arena, Game has started
						else if (Infected.getGameState() == GameState.STARTED)
						{
							if (plugin.config.getBoolean("Debug"))
							{
								System.out.println("Leave: In Arena, Game Has Started");
							}
							if (plugin.inGame.size() == 1)
							{
								if (plugin.config.getBoolean("Debug"))
								{
									System.out.println("Leave: In Arena, Game Has Started (Not Enough Players)");
								}
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", player, null, null));
										Reset.tp2LobbyAfter(players);
									}
								}
								Reset.resetInf();
							}
							// If Not Enough zombies remain
							else if (plugin.zombies.size() == 1 && Infected.isPlayerZombie(player))
							{
								if (plugin.config.getBoolean("Debug"))
								{
									System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
								}
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								Methods.newZombieSetUpEveryOne();
							}
							// Not enough humans left
							else if (plugin.humans.size() == 1 && plugin.humans.contains(player.getName()))
							{
								if (plugin.config.getBoolean("Debug"))
								{
									System.out.println("Leave: In Arena, Game Has Started (Not Enough Humans)");
								}
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", player, null, null));
										Reset.tp2LobbyAfter(players);
									}
								}
								Reset.resetInf();
							} else
							{
								player.sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
								Reset.resetp(player);
								for (Player players : Bukkit.getOnlinePlayers())
								{
									if (Infected.isPlayerInGame(players))
									{
										players.sendMessage(Methods.sendMessage("Leave_NoEffect", player, null, null));
									}
								}
							}
						}
					} else
					{
						// If the player tries to leave
						// without joining first
						player.sendMessage(plugin.I + "Your not even in a game!");
						return true;
					}
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// HELP
				else if (args[0].equalsIgnoreCase("Help"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else
					{
						if (sender instanceof Player && plugin.getConfig().getBoolean("Book For Help"))
						{
							Player p = (Player) sender;
							ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
							BookMeta b = (BookMeta) is.getItemMeta();
							ArrayList<String> pages = new ArrayList<String>();
							pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join\n" + ChatColor.GOLD + " - Join Infected\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave\n" + ChatColor.GOLD + " - Leave Infected\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote <Arena>\n" + ChatColor.GOLD + " - Vote for a map\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Shop [Id]\n" + ChatColor.GOLD + " - See the purchasable grenades\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide \n" + ChatColor.GOLD + " - Suicide if your stuck");
							pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "   Commands (2)" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat <Msg>\n" + ChatColor.GOLD + " - Chat in your team's chat\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats [Player]\n" + ChatColor.GOLD + " - Check a player's stats\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas\n" + ChatColor.GOLD + " - See all possible arenas\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top <Category>\n" + ChatColor.GOLD + " - Check the top 5 players");
							pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "   Commands (3)" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info\n" + ChatColor.GOLD + " - See The current status\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes\n" + ChatColor.GOLD + " - Choose a class\n");
							if (player.hasPermission("Infected.SetUp"))
								pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " Infected Admin\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby\n" + ChatColor.DARK_AQUA + " - Set the main lobby\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create <Arena>\n" + ChatColor.DARK_AQUA + " - Create an arena\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove <Arena>\n" + ChatColor.DARK_AQUA + " - Remove an Arena\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena <Arena>\n" + ChatColor.DARK_AQUA + " - Select an arena\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn\n" + ChatColor.DARK_AQUA + " - Set the spawn");
							if (player.hasPermission("Infected.SetUp"))
								pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " Infected Admin\n" + "   Commands (2)" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn <ID>\n" + ChatColor.DARK_AQUA + " - Teleport to the spawn ID(Number)\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn <ID>\n" + ChatColor.DARK_AQUA + " - Remove the spawn ID(Number)\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns\n" + ChatColor.DARK_AQUA + " - List how many spawns an arena has\n" + ChatColor.GREEN + " /Inf TpLobby" + ChatColor.DARK_GRAY + ". Tp to the lobby.\n");
							if (player.hasPermission("Infected.Admin"))
								pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " Infected Force\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Start\n" + ChatColor.BLACK + " - Force the game to start\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "End\n" + ChatColor.BLACK + " - Force the game to end\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Refresh\n" + ChatColor.BLACK + " - Force all the players to refresh eachother\n");
							if (player.hasPermission("Infected.Admin"))
								pages.add(ChatColor.GREEN + "" + ChatColor.BOLD + " Infected Admin\n" + "       Menu" + "\n\n" + ChatColor.AQUA + "/Inf Admin Points <Player> <#>\n" + ChatColor.BLACK + "/Inf Admin Score <Player> <#>\n" + ChatColor.BLUE + "/Inf Admin KStats <Player> <#>\n" + ChatColor.DARK_AQUA + "/Inf Admin DStats <Player> <#>\n" + ChatColor.DARK_GREEN + "/Inf Admin Kick <Player>\n" + ChatColor.DARK_BLUE + "/Inf Admin Reload\n");
							if (player.hasPermission("Infected.Admin"))
								pages.add(ChatColor.GREEN + "" + ChatColor.BOLD + " Infected Admin\n" + "     Menu (2)" + "\n\n" + ChatColor.DARK_PURPLE + "/Inf Admin Reset <Player>\n" + ChatColor.GOLD + "/Inf Admin Shutdown\n");
							if (player.hasPermission("Infected.SetUp"))
								pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " How To Set Up \n" + "     Infected" + "\n\n" + ChatColor.RED + "1. " + ChatColor.DARK_GRAY + "Build a lobby\n" + ChatColor.RED + "2. " + ChatColor.DARK_GRAY + "Set the lobby spawn point where you're standing using the" + ChatColor.GREEN + " /Inf SetLobby" + ChatColor.DARK_GRAY + " command\n" + ChatColor.RED + "3. " + ChatColor.DARK_GRAY + "Build an arena\n");
							if (player.hasPermission("Infected.SetUp"))
								pages.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + " How To Set Up \n" + "  Infected (2)" + "\n\n" + ChatColor.RED + "4. " + ChatColor.DARK_GRAY + "Create the arena on Infected using " + ChatColor.GREEN + " /Inf Create <Arena Name>\n" + ChatColor.RED + "5. " + ChatColor.DARK_GRAY + "Set the first arena spawn point where you're standing using " + ChatColor.GREEN + " /Inf SetSpawn" + ChatColor.DARK_GRAY + ". You can keep retyping this command to set more spawn points.\n");
							b.setAuthor(ChatColor.DARK_AQUA + "xXSniperzzXx_SD");
							b.setTitle(ChatColor.DARK_RED + "Infected Help Book");
							b.setPages(pages);
							is.setItemMeta(b);
							if (!p.getInventory().contains(is))
							{
								p.getInventory().addItem(is);
							} else
							{
								p.sendMessage(plugin.I + "You already have the help book!");
							}

						} else
						{
							player.sendMessage("");
							player.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
							player.sendMessage(plugin.I + ChatColor.YELLOW + "For Help type: /Infected Help");
							player.sendMessage("");
						}
					}
					// Show the the help page
					return true;
				}
				// /////////////////////////////////////////////////////////////////////////////////
				// VOTE if no args are present
				else if (args[0].equalsIgnoreCase("Vote"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Error_NotInGame", null, null, null));
						return true;
					} else if (Infected.getGameState() != GameState.INLOBBY && Infected.getGameState() != GameState.VOTING)
					{
						player.sendMessage(Methods.sendMessage("Vote_GameAlreadyStarted", null, null, null));
						return true;
					} else if (plugin.Voted4.containsKey(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Vote_AlreadyVoted", null, null, null));
						return true;
					} else if (!plugin.getConfig().getBoolean("Allow Votes"))
					{
						player.sendMessage(Methods.sendMessage("Vote_VotesNotAllowed", null, null, null));
						return true;
					} else
					{
						if (plugin.config.getBoolean("Vote with a GUI"))
						{
							Menus.openVotingMenu(player);
						} else
						{
							player.performCommand("Infected arenas");
							player.sendMessage(plugin.I + ChatColor.YELLOW + "Or you could just vote for \"/Infected Vote Random\"");
						}
					}
					return true;
					// ///////////////////////////////////////////////////////////////////////////////////////////////////
					// MANUAL START
				} else if (args[0].equalsIgnoreCase("Start"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null) + " to manual start Infected.");
						player.sendMessage(plugin.I + plugin.inGame.size() + "/" + plugin.getConfig().getInt("Automatic Start.Minimum Players") + " Players till an automatic start.");
						return true;
					}

					// If /start with only 1 player
					if (plugin.inGame.size() < 2)
					{
						player.sendMessage(plugin.I + plugin.inGame.size() + "/" + plugin.getConfig().getInt("Automatic Start.Minimum Players") + " Players till an automatic start.");
						player.sendMessage(plugin.I + ChatColor.RED + "You need more then 2 people to force start");
						return true;
					}
					// If the game already started
					if (Infected.getGameState() != GameState.INLOBBY)
					{
						player.sendMessage(Methods.sendMessage("Error_GameStarted", null, null, null));
						return true;
					} else
					{
						Game.START();
					}
					return true;
					// ///////////////////////////////////////////////////////////////////////////////////////////////////
					// END
				} else if (args[0].equalsIgnoreCase("End"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// End the game
					for (Player players : Bukkit.getServer().getOnlinePlayers())
					{
						if (plugin.inGame.contains(players.getName()))
						{
							if (plugin.config.getBoolean("Disguise Support.Enabled"))
								if (Disguises.isPlayerDisguised(players))
									Disguises.unDisguisePlayer(players);

							// Give player's all their stuff/stats back
							Reset.resetp(players);
							players.sendMessage(Methods.sendMessage("Game_ForcedToStop", null, null, null));
							for (PotionEffect reffect : players.getActivePotionEffects())
							{
								players.removePotionEffect(reffect.getType());
							}
						}
					}
					// reset lists, hashmaps and arena
					Reset.reset();
				}

				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// ARENAS
				else if (args[0].equalsIgnoreCase("Arenas"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Arenas"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					player.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>[" + ChatColor.YELLOW + ChatColor.BOLD + "Arenas" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<");
					// Check the arenas.yml for any paths
					// after the arean's path

					Infected.filesReloadArenas();
					plugin.possibleArenas.clear();
					if (Infected.filesGetArenas().getConfigurationSection("Arenas") == null)
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Missing an arena!");
						return true;
					}
					for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
					{
						// Check if the string matchs an arena

						if (plugin.possibleArenas.contains(parenas))
						{
							plugin.possibleArenas.remove(parenas);
						}
						if (!parenas.contains("."))
						{
							plugin.possibleArenas.add(parenas);
						}
						if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
						{
							plugin.possibleArenas.remove(parenas);
						}
						if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns") && !parenas.contains("."))
						{
							plugin.possibleArenasU.add(parenas);
						}
					}

					// send players the list
					StringBuilder possibleU = new StringBuilder();
					for (Object o : plugin.possibleArenasU)
					{
						possibleU.append(o.toString());
						if (plugin.possibleArenasU.size() > 1)
							possibleU.append(", ");
					}
					StringBuilder possible = new StringBuilder();
					for (Object o : plugin.possibleArenas)
					{
						possible.append(o.toString());
						if (plugin.possibleArenas.size() > 1)
							possible.append(", ");
					}
					player.sendMessage(plugin.I + ChatColor.GRAY + "Arenas: " + ChatColor.GREEN + possible.toString() + ChatColor.DARK_GRAY + possibleU.toString());
					plugin.possibleArenasU.clear();
					return true;
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// ADMIN
				else if (args[0].equalsIgnoreCase("Admin"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					player.sendMessage(plugin.I + ChatColor.YELLOW + "------= Admin Menu =------");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin Points <Player> <#>");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin Score <Player> <#>");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin KStats <Player> <#>");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin DStats <Player> <#>");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin Kick <Player>");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin Reset <Player>");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin Shutdown");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf Admin Reload");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// STATS
				else if (args[0].equalsIgnoreCase("Stats"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.Stats"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					String user = player.getName().toLowerCase();
					if (!Infected.filesGetPlayers().contains("Players." + user))
					{
						player.sendMessage(plugin.I + "You have never played infected!");
						return true;
					}
					if (Bukkit.getServer().getPlayer(user) != null)
					{
						user = Bukkit.getServer().getPlayer(user).getName();
					}
					if (Bukkit.getServer().getOfflinePlayer(user) != null)
					{
						user = Bukkit.getServer().getOfflinePlayer(user).getName();
					}
					player.sendMessage("");
					player.sendMessage(plugin.I + ChatColor.YELLOW + "------= " + user + " =------");
					player.sendMessage(plugin.I + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Infected.playerGetPoints(Bukkit.getPlayer(user)) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Score"));
					player.sendMessage(plugin.I + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Methods.getOnlineTime(user.toLowerCase()));
					player.sendMessage(plugin.I + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Kills") + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Deaths") + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + Methods.KD(Bukkit.getPlayer(user)));
					player.sendMessage(plugin.I + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".KillStreak"));
					player.sendMessage("");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// TPSPAWN(NO ARG)
				else if (args[0].equalsIgnoreCase("TpSpawn"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					sender.sendMessage(plugin.I + ChatColor.RED + "/Inf TpSpawn <ID>");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// TPLOBBY(NO ARG)
				else if (args[0].equalsIgnoreCase("TpLobby"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					String s = plugin.config.getString("Lobby");
					String[] floc = s.split(",");
					World world = Bukkit.getServer().getWorld(floc[0]);
					Location Loc = new Location(world,
							Integer.valueOf(floc[1]), Integer.valueOf(floc[2]),
							Integer.valueOf(floc[3]), Float.valueOf(floc[4]),
							Float.valueOf(floc[5]));
					player.teleport(Loc);
					sender.sendMessage(plugin.I + "You have teleported to the lobby.");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// DELSPAWN(NO ARG)
				else if (args[0].equalsIgnoreCase("DelSpawn"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					sender.sendMessage(plugin.I + ChatColor.RED + "/Inf DelSpawn <ID>");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// SPAWNS
				else if (args[0].equalsIgnoreCase("Spawns"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (!plugin.Creating.containsKey(sender.getName()))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "You don't have an arena selected!");
						return true;
					}
					List<String> list = Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns");
					sender.sendMessage(plugin.I + plugin.Creating.get(sender.getName()) + " has " + ChatColor.DARK_GRAY + list.size() + ChatColor.GRAY + " spawns.");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// SETSPAWN
				else if (args[0].equalsIgnoreCase("SetSpawn"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (!plugin.Creating.containsKey(player.getName()))
					{
						player.sendMessage(plugin.I + ChatColor.RED + "You don't have an arena selected!");
						return true;
					}
					Location l = player.getLocation();
					int ix = (int) l.getX();
					int iy = (int) l.getY();
					int iz = (int) l.getZ();
					World world = l.getWorld();
					float yaw = l.getYaw();
					float pitch = l.getPitch();
					String s = world.getName() + "," + ix + "," + iy + "," + iz + "," + yaw + "," + pitch;
					if (Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns") == null)
					{
						String[] list = { s };
						Infected.filesGetArenas().set("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns", list);
					} else
					{
						List<String> list = Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns");
						list.add(s);
						Infected.filesGetArenas().set("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns", list);
						Infected.filesSaveArenas();
					}
					Infected.filesSaveArenas();
					Infected.filesReloadArenas();
					player.sendMessage(plugin.I + creating + " spawn #" + ChatColor.YELLOW + Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns").size() + ChatColor.GRAY + " set at your location!");
					return true;
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// CREATE (No ARGS)
				else if (args[0].equalsIgnoreCase("Create"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + "/Infected Create <Arena Name>");
					}
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// TOP (No ARGS)
				else if (args[0].equalsIgnoreCase("Top"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Top"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + "/Infected Top <Kills/Deaths/Points/Score>");
					}
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// SETARENA (No ARGS)
				else if (args[0].equalsIgnoreCase("SetArena"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + "/Infected SetArena <Arena Name>");
						player.performCommand("infected arenas");
					}
				} else
				{
					CommandSender player = sender;
					player.sendMessage("");
					player.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected Help" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join" + ChatColor.WHITE + " - Join Infected");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave" + ChatColor.WHITE + " - Leave Infected");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote" + ChatColor.WHITE + " - Vote for a map");
					player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes" + ChatColor.WHITE + " - Choose a class");
					if (player.hasPermission("Infected.Shop"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Shop" + ChatColor.WHITE + " - See the purchasable grenades");
					if (player.hasPermission("Infected.Chat"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat" + ChatColor.WHITE + " - Chat in your team's chat");
					if (player.hasPermission("Infected.Stats"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats" + ChatColor.WHITE + " - Check a player's stats");
					if (player.hasPermission("Infected.Suicide"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide" + ChatColor.WHITE + " - Suicide if you're stuck");
					if (player.hasPermission("Infected.Info"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info" + ChatColor.WHITE + " - Check Infected's status");
					if (player.hasPermission("Infected.Top"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top" + ChatColor.WHITE + " - Check the top 5 players");
					if (player.hasPermission("Infected.Arenas"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas" + ChatColor.WHITE + " - See all possible arenas");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby" + ChatColor.WHITE + " - Set the main lobby");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn" + ChatColor.WHITE + " - Set the spawn for the selected arena");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns" + ChatColor.WHITE + " - List the number of spawns for a map");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn" + ChatColor.WHITE + " - Tp to a spawn ID");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn" + ChatColor.WHITE + " - Delete the spawn ID");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena" + ChatColor.WHITE + " - Select an arena");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create" + ChatColor.WHITE + " - Create an arena");
					if (player.hasPermission("Infected.SetUp"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove" + ChatColor.WHITE + " - Remove an Arena");
					if (player.hasPermission("Infected.Admin"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Start" + ChatColor.WHITE + " - Force the game to start");
					if (player.hasPermission("Infected.Admin"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "End" + ChatColor.WHITE + " - Force the game to end");
					if (player.hasPermission("Infected.Admin"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Admin" + ChatColor.WHITE + " - Show the admin menu");
					if (player.hasPermission("Infected.Admin"))
						player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Refresh" + ChatColor.WHITE + " - Refresh all the players");
					player.sendMessage("");
					return true;
				}
			} else if (args.length == 2)
			{

				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// TOP
				if (args[0].equalsIgnoreCase("Top"))
				{
					if (!sender.hasPermission("Infected.Top"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					String user = sender.getName().toLowerCase();
					if (!Infected.filesGetPlayers().contains("Players." + user))
					{
						sender.sendMessage(plugin.I + "In order to see the top you need to have played!");
						return true;
					}
					String Stat = args[1];
					char[] stringArray = Stat.toCharArray();
					stringArray[0] = Character.toUpperCase(stringArray[0]);
					Stat = new String(stringArray);
					if (!Infected.filesGetPlayers().contains("Players." + user + "." + Stat))
					{
						sender.sendMessage(Methods.sendMessage("Error_NotAStat", null, null, null));
						return true;
					}
					String[] top = Methods.getTop5(Stat);
					int stat1 = Infected.filesGetPlayers().getInt("Players." + top[0] + "." + Stat);
					int stat2 = Infected.filesGetPlayers().getInt("Players." + top[1] + "." + Stat);
					int stat3 = Infected.filesGetPlayers().getInt("Players." + top[2] + "." + Stat);
					int stat4 = Infected.filesGetPlayers().getInt("Players." + top[3] + "." + Stat);
					int stat5 = Infected.filesGetPlayers().getInt("Players." + top[4] + "." + Stat);
					sender.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>[" + ChatColor.YELLOW + ChatColor.BOLD + "Top 5 " + Stat + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<");

					if (!(top[0] == null) && !(stat1 == 0))
						sender.sendMessage(plugin.I + ChatColor.GOLD + ChatColor.BOLD + "1. " + ChatColor.GREEN + top[0] + ChatColor.AQUA + " - " + ChatColor.WHITE + stat1);
					if (!(top[1] == null) && !(stat2 == 0))
						sender.sendMessage(plugin.I + ChatColor.RED + ChatColor.BOLD + "2. " + ChatColor.GREEN + top[1] + ChatColor.AQUA + " - " + ChatColor.WHITE + stat2);
					if (!(top[2] == null) && !(stat3 == 0))
						sender.sendMessage(plugin.I + ChatColor.GREEN + ChatColor.BOLD + "3. " + ChatColor.GREEN + top[2] + ChatColor.AQUA + " - " + ChatColor.WHITE + stat3);
					if (!(top[3] == null) && !(stat4 == 0))
						sender.sendMessage(plugin.I + ChatColor.BOLD + "4. " + ChatColor.GREEN + top[3] + ChatColor.AQUA + " - " + ChatColor.WHITE + stat4);
					if (!(top[4] == null) && !(stat5 == 0))
						sender.sendMessage(plugin.I + ChatColor.BOLD + "5. " + ChatColor.GREEN + top[4] + ChatColor.AQUA + " - " + ChatColor.WHITE + stat5);
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// TPSPAWN
				else if (args[0].equalsIgnoreCase("TPSpawn"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					if (!plugin.Creating.containsKey(sender.getName()))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "You don't have an arena selected!");
						return true;
					}
					Player player = (Player) sender;
					List<String> list = Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns");
					int i = Integer.valueOf(args[1]) - 1;
					if (i < list.size())
					{
						// if (!(list.get(i) == null))
						// {
						String s = list.get(i);
						String[] floc = s.split(",");
						World world = Bukkit.getServer().getWorld(floc[0]);
						Location Loc = new Location(world,
								Integer.valueOf(floc[1]),
								Integer.valueOf(floc[2]),
								Integer.valueOf(floc[3]),
								Float.valueOf(floc[4]), Float.valueOf(floc[5]));
						player.teleport(Loc);
						sender.sendMessage(plugin.I + "You have teleported to spawn number " + (i + 1) + ".");
					} else
						sender.sendMessage(plugin.I + ChatColor.RED + "Infected doesn't know where your tying to go, check how many spawns this arena has again!");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// DELSPAWN
				else if (args[0].equalsIgnoreCase("DelSpawn"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (!plugin.Creating.containsKey(sender.getName()))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "You don't have an arena selected!");
						return true;
					}
					List<String> list = Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns");
					int i = Integer.valueOf(args[1]) - 1;
					if (!(list.get(i) == null))
					{
						list.remove(i);
						Infected.filesGetArenas().set("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns", list);
						Infected.filesSaveArenas();
						sender.sendMessage(plugin.I + ChatColor.RED + "You have removed spawn number " + i + 1 + ".");
					} else
						sender.sendMessage(plugin.I + ChatColor.RED + "Infected doesn't know where your tying to go, check how many spawns this arena has again!");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// CREATE
				else if (args[0].equalsIgnoreCase("Create"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// Set args[1] as the creating
					// string whilst setting the world
					// and making a new arena

					String arena = args[1].toLowerCase();
					Infected.filesReloadArenas();
					plugin.possibleArenas.clear();
					if (Infected.filesGetArenas().getConfigurationSection("Arenas") == null)
					{
						player.sendMessage(plugin.I + "Arena '" + arena + "' created.");
						Infected.filesGetArenas().set("Arenas." + arena + ".Plain Zombie Survival", false);
						String[] list = { "55", "20" };
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Global", list);
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Human", "[]");
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Zombie", "[]");
						Infected.filesSaveArenas();
						plugin.Creating.put(player.getName(), arena);
						return true;
					}
					for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
					{
						// Check if the string matchs an arena

						if (!plugin.possibleArenas.contains(parenas) && !parenas.contains("."))
						{
							plugin.possibleArenas.add(parenas);
						}
					}
					if (plugin.possibleArenas.isEmpty())
					{
						player.sendMessage(plugin.I + "Arena '" + arena + "' created.");
						Infected.filesGetArenas().set("Arenas." + arena + ".World", player.getWorld().getName());
						Infected.filesGetArenas().set("Arenas." + arena + ".Plain Zombie Survival", false);
						String[] list = { "55", "20" };
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Global", list);
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Human", "[]");
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Zombie", "[]");
						Infected.filesSaveArenas();
						plugin.Creating.put(player.getName(), arena);
						return true;
					}
					// Check if the arena already exits
					// by using the list of possible
					// arenas
					if (plugin.possibleArenas.contains(arena))
					{
						player.sendMessage(plugin.I + "That arena already exists!");
						return true;
					} else
					{
						player.sendMessage(plugin.I + "Arena '" + arena + "' created.");
						player.sendMessage(plugin.I + "Type " + ChatColor.YELLOW + "/Inf SetSpawn" + ChatColor.GRAY + " to finish the arena!");
						String[] list = { "55", "20" };
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Global", list);
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Human", "[]");
						Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Zombie", "[]");
						Infected.filesGetArenas().set("Arenas." + arena + ".Plain Zombie Survival", false);
						Infected.filesSaveArenas();
						plugin.Creating.put(player.getName(), arena);
						return true;
					}
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
				// SHOP
				else if (args[0].equalsIgnoreCase("Shop"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!Infected.filesGetGrenades().getBoolean("Use"))
					{
						player.sendMessage(Methods.sendMessage("Grenade_Disabled", null, null, null));
					} else if (!player.hasPermission("Infected.Grenades"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Grenade_OnlyBuyInGame", null, null, null));
						return true;
					} else if (plugin.getConfig().getBoolean("Grenades.Only Humans Can Use") && plugin.zombies.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Grenade_NoZombies", null, null, null));
						return true;
					} else
					{

						if (Infected.filesGetGrenades().getKeys(true) == null)
						{
							player.sendMessage(plugin.I + ChatColor.RED + " No Grenades were found...");
							return true;
						}
						plugin.Grenades.clear();
						for (String grenades : Infected.filesGetGrenades().getKeys(true))
						{
							// Check if the string matchs an arena
							if (!grenades.contains("."))
							{
								if (grenades.matches("[0-9]+"))
								{
									plugin.Grenades.add(Integer.valueOf(grenades));
								}
							}
						}
						if (args[1].matches("[0-9]+"))
						{
							int gi = Integer.parseInt(args[1]);
							if (Integer.valueOf(args[1]) <= (plugin.Grenades.size() - 1))
							{
								if (Infected.playerGetPoints(player) > Infected.filesGetGrenades().getInt(plugin.Grenades.get(gi) + ".Cost"))
								{
									ItemStack itemstack = new ItemStack(
											Material.getMaterial(plugin.Grenades.get(gi)),
											1);
									ItemMeta im = itemstack.getItemMeta();
									im.setDisplayName("�e" + Infected.filesGetGrenades().getString(plugin.Grenades.get(gi) + ".Name"));
									itemstack.setItemMeta(im);
									player.getInventory().addItem(itemstack);
									Infected.playerSetPoints(player, Infected.playerGetPoints(player), Infected.filesGetGrenades().getInt(plugin.Grenades.get(gi) + ".Cost"));
									player.sendMessage(plugin.I + ChatColor.DARK_AQUA + "You have just bought a " + ChatColor.AQUA + Infected.filesGetGrenades().getString(plugin.Grenades.get(gi) + ".Name"));
								} else
									player.sendMessage(plugin.I + ChatColor.RED + "You don't have enough points to make this purchase!");
							} else
								player.performCommand("inf shop");
						} else
							player.performCommand("inf shop");
					}
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// STATS
				else if (args[0].equalsIgnoreCase("Stats"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Stats.Other"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					String user = args[1].toLowerCase();
					if (!Infected.filesGetPlayers().contains(user))
					{
						player.sendMessage(plugin.I + "That user isn't online!");
						return true;
					}
					if (Bukkit.getServer().getPlayer(user) != null)
					{
						user = Bukkit.getServer().getPlayer(user).getName();
					}
					if (Bukkit.getServer().getOfflinePlayer(user) != null)
					{
						user = Bukkit.getServer().getOfflinePlayer(user).getName();
					}
					player.sendMessage("");
					player.sendMessage(plugin.I + ChatColor.YELLOW + "------= " + user + " =------");
					player.sendMessage(plugin.I + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Infected.playerGetPoints(Bukkit.getPlayer(user)) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Score"));
					player.sendMessage(plugin.I + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Methods.getOnlineTime(user.toLowerCase()));
					player.sendMessage(plugin.I + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Kills") + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Deaths") + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + Methods.KD(Bukkit.getPlayer(user)));
					player.sendMessage(plugin.I + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".KillStreak"));
					player.sendMessage("");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// REMOVE
				else if (args[0].equalsIgnoreCase("Remove"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// Set args[1] as the creating
					// string whilst setting the world
					// and making a new arena

					String arena = args[1].toLowerCase();
					for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
					{
						if (plugin.possibleArenas.contains(parenas))
						{
							plugin.possibleArenas.remove(parenas);
						}
						if (!parenas.contains("."))
						{
							plugin.possibleArenas.add(parenas);
						}
					}
					// Check if the arena already exits
					// by using the list of possible
					// arenas
					if (!plugin.possibleArenas.contains(arena))
					{
						player.sendMessage(plugin.I + "That arena doesn't exists!");
						return true;
					} else
					{
						player.sendMessage(plugin.I + "Arena deleted.");
						Infected.filesGetArenas().set("Arenas." + arena, null);
						Infected.filesSaveArenas();
						return true;
					}
				}

				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// VOTE
				else if (args[0].equalsIgnoreCase("Vote"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					// Make sure the player isn't
					// trying anything fancy
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					} else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Error_NotInGame", null, null, null));
						return true;
					} else if (Infected.getGameState() != GameState.INLOBBY && Infected.getGameState() != GameState.VOTING)
					{
						player.sendMessage(Methods.sendMessage("Vote_GameAlreadyStarted", null, null, null));
						return true;
					} else if (plugin.Voted4.containsKey(player.getName()))
					{
						player.sendMessage(Methods.sendMessage("Vote_AlreadyVoted", null, null, null));
						return true;
					} else if (!plugin.getConfig().getBoolean("Allow Votes"))
					{
						player.sendMessage(Methods.sendMessage("Vote_VotesNotAllowed", null, null, null));
						return true;
					} else
					{
						// Set his arg to a string
						String voted4 = args[1].toLowerCase();

						Infected.filesReloadArenas();
						plugin.possibleArenas.clear();
						for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
						{
							// Check if the string matchs an arena

							if (plugin.possibleArenas.contains(parenas))
							{
								plugin.possibleArenas.remove(parenas);
							}
							if (!parenas.contains("."))
							{
								plugin.possibleArenas.add(parenas);
							}
							if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
							{
								plugin.possibleArenas.remove(parenas);
							}
						}
						if (voted4.equalsIgnoreCase("Random"))
						{

							Random r = new Random();
							int i = r.nextInt(plugin.possibleArenas.size());
							voted4 = plugin.possibleArenas.get(i);
						}
						if (!voted4.equals("random") && !Infected.filesGetArenas().contains("Arenas." + voted4 + ".Spawns"))
						{
							player.sendMessage(plugin.I + ChatColor.RED + "Error: Something's wrong with that arena!");
							player.performCommand("infected arenas");
							return true;
						}
						if (!voted4.equals("random") && !plugin.possibleArenas.contains(voted4))
						{
							player.sendMessage(plugin.I + ChatColor.RED + "That's an invalid arena. Choose one of the following");
							player.performCommand("infected arenas");
						} else
						{
							if (plugin.Votes.containsKey(voted4))
							{
								plugin.Votes.put(voted4, plugin.Votes.get(voted4) + 1);
							} else
							{
								plugin.Votes.put(voted4, 1);
							}
							plugin.Voted4.put(player.getName(), voted4);
							for (Player players : Bukkit.getServer().getOnlinePlayers())
								if (plugin.inGame.contains(players.getName()))
								{
									players.sendMessage(plugin.I + ChatColor.GRAY + player.getName() + " has voted for: " + ChatColor.YELLOW + voted4);
								}
							if (plugin.config.getBoolean("ScoreBoard Support"))
							{
								ScoreBoard.updateScoreBoard();
							}
						}

					}
					return true;
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// SETARENA
				else if (args[0].equalsIgnoreCase("SetArena"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// Set args[1] as the creating
					// string
					String arena = args[1].toLowerCase();
					Infected.filesReloadArenas();
					plugin.possibleArenas.clear();
					if (Infected.filesGetArenas().getConfigurationSection("Arenas") == null)
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Missing an arena!");
						return true;
					}
					for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
					{
						if (plugin.possibleArenas.contains(parenas))
						{
							plugin.possibleArenas.remove(parenas);
						}
						if (!parenas.contains("."))
						{
							plugin.possibleArenas.add(parenas);
						}
					}
					if (plugin.possibleArenas.isEmpty())
					{
						player.sendMessage(plugin.I + ChatColor.RED + " You don't have any arenas created!");
						return true;
					}
					// Make sure they actually chose
					// an existing arena
					if (!plugin.possibleArenas.contains(arena))
					{
						player.sendMessage(plugin.I + ChatColor.RED + "Invalid Arena, please chooose one of the following");
						player.performCommand("Infected Arenas");
						return true;
					} else
					{

						plugin.Creating.put(player.getName(), arena);
						player.sendMessage(plugin.I + ChatColor.YELLOW + "Arena Set. Choosen Arena: " + ChatColor.GRAY + arena);
						return true;
					}
				} else if (args.length == 2 && args[0].equalsIgnoreCase("Admin") && args[1].equalsIgnoreCase("Shutdown"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					if (Infected.getGameState() != GameState.DISABLED)
					{
						Infected.setGameState(GameState.DISABLED);
						player.sendMessage(plugin.I + ChatColor.GRAY + "Joining Infected has been disabled.");
					} else
					{

						Infected.setGameState(GameState.INLOBBY);
						player.sendMessage(plugin.I + ChatColor.GRAY + "Joining Infected has been enabled.");
					}
				} else if (args.length == 2 && args[0].equalsIgnoreCase("Admin") && args[1].equalsIgnoreCase("Reload"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					System.out.println("===== Infected =====");
					Infected.filesReloadAllButConfig();
					plugin.reloadConfig();

					if (plugin.getConfig().getBoolean("Vault Support.Enable"))
					{
						if (!(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null))
						{
							System.out.println("Vault support has been enabled!");
						
							RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
							if (economyProvider != null)
							{
								plugin.economy = economyProvider.getProvider();
							}
							
						} 
						else
						{
							System.out.println(plugin.I + "Vault wasn't found on this server, Disabling Vault Support");
							plugin.getConfig().set("Vault Support.Enable", false);
							plugin.saveConfig();

						}
					}else
						System.out.println("Vault Support is Disabled");

					// Check if the plugin addons are there
					if (plugin.getConfig().getBoolean("CrackShot Support.Enable"))
					{
						if (plugin.getServer().getPluginManager().getPlugin("CrackShot") == null)
						{

							System.out.println(plugin.I + "CrackShot wasn't found on this server, disabling CrackShot Support");
							plugin.getConfig().set("CrackShot Support.Enable", false);
							plugin.saveConfig();
						} else
						{
						CrackShotApi CSApi = new CrackShotApi(plugin);
						Bukkit.getPluginManager().registerEvents(CSApi, plugin);
						System.out.println("CrackShot support has been enabled!");
						}
					}else
						System.out.println("CrackShot Support is Disabled");

					
					// Check if the plugin addons are there
					if (plugin.getConfig().getBoolean("TagAPI Support.Enable"))
					{
						if (plugin.getServer().getPluginManager().getPlugin("TagAPI") == null)
						{
							System.out.println(plugin.I + "TagApi wasn't found on this server, disabling TagApi Support");
							plugin.getConfig().set("TagAPI Support.Enable", false);
							plugin.saveConfig();
						} else
						{
							TagApi TagApi = new TagApi(plugin);
							Bukkit.getPluginManager().registerEvents(TagApi, plugin);
							System.out.println("TagApi support has been enabled!");
						}
					}else
						System.out.println("TagAPI Support is Disabled");
					
					Disguises.getDisguisePlugin();
					System.out.println("====================");

					player.sendMessage(plugin.I + "Infecteds Files have been reloaded");
				}
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// LIST
				else if (args[0].equalsIgnoreCase("List"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.List"))
					{
						player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
						return true;
					}
					// List everyone in the playing
					// hashmap
					if (args[1].equalsIgnoreCase("Playing"))
					{
						if (plugin.inGame.size() == 0)
						{
							player.sendMessage(plugin.I + ChatColor.GRAY + "No one's playing.");
							return true;
						}
						player.sendMessage(plugin.I + ChatColor.GOLD + "----Playing----");
						for (String all : plugin.inGame)
						{
							player.sendMessage(plugin.I + ChatColor.YELLOW + "> " + all);
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("Lobby"))
					{
						if (plugin.inLobby.size() == 0)
						{
							player.sendMessage(plugin.I + ChatColor.GRAY + "No one's playing.");
							return true;
						}
						player.sendMessage(plugin.I + ChatColor.GOLD + "----In Lobby----");
						for (String all : plugin.inLobby)
						{
							player.sendMessage(plugin.I + ChatColor.YELLOW + "> " + all);
						}
						return true;
					}
					if (Infected.getGameState() != GameState.STARTED)
					{
						player.sendMessage(plugin.I + ChatColor.GRAY + "The game hasn't started!");
						return true;
					}
					// List everyone in the humans
					// hashmap
					else if (args[1].equalsIgnoreCase("Humans"))
					{
						if (plugin.humans.size() == 0)
						{
							player.sendMessage(plugin.I + ChatColor.GRAY + "There are no humans!");
							return true;
						}
						player.sendMessage(plugin.I + ChatColor.GOLD + "----Humans----");
						for (String all : plugin.humans)
						{
							player.sendMessage(plugin.I + ChatColor.YELLOW + "> " + all);
						}
						return true;
					} else if (args[1].equalsIgnoreCase("Zombies"))
					{
						if (plugin.zombies.size() == 0)
						{
							player.sendMessage(plugin.I + ChatColor.GRAY + "There are no Zombies!");
							return true;
						}
						// List everyone in the zombies
						// hashmap
						player.sendMessage(plugin.I + ChatColor.GOLD + "----Zombies----");
						for (String all : plugin.zombies)
						{
							player.sendMessage(plugin.I + ChatColor.YELLOW + "> " + all);
						}
						return true;
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + "Unknown List, Possible Lists: Playing, Lobby, Humans, Zombies");
						return true;
					}
				} else
				{
					CommandSender player = sender;
					player.sendMessage(plugin.I + ChatColor.RED + "Unknown Command, Type /Infected Help");
					return true;
				}
				// //////////////////////////////////////////////////////////////////////////
				// ADMIN
			} else if (args.length > 2 && args[0].equalsIgnoreCase("Admin"))
			{
				CommandSender player = sender;
				if (!player.hasPermission("Infected.Admin"))
				{
					player.sendMessage(Methods.sendMessage("Error_NoPermission", null, null, null));
					return true;
				}
				if (args.length == 4)
				{
					Player user = Bukkit.getPlayer(args[2]);
					if (user == null)
					{
						player.sendMessage(plugin.I + ChatColor.RED + "The player must be online");
						return true;
					}
					int i = Integer.parseInt(args[3]);
					if (args[1].equalsIgnoreCase("Points"))
					{
						Infected.playerSetPoints(user, Infected.playerGetPoints(user) + i, 0);
						player.sendMessage(plugin.I + user.getName() + "'s new points is: " + Infected.playerGetPoints(user));
					} else if (args[1].equalsIgnoreCase("Score"))
					{
						Infected.playerSetScore(user, Infected.playerGetScore(user) + i);
						player.sendMessage(plugin.I + user.getName() + "'s new score is: " + Infected.playerGetScore(user));
					} else if (args[1].equalsIgnoreCase("kStats"))
					{
						Infected.playerSetKills(user, Infected.playerGetKills(user) + i);
						player.sendMessage(plugin.I + user.getName() + "'s new kill count is: " + Infected.playerGetKills(user));
					} else if (args[1].equalsIgnoreCase("DStats"))
					{
						Infected.playerSetDeaths(user, Infected.playerGetDeaths(user) + i);
						player.sendMessage(plugin.I + user.getName() + "'s new death count is: " + Infected.playerGetDeaths(user));
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + "Thats an invalid command");
					}
				} else if (args.length == 3)
				{
					if (args[1].equalsIgnoreCase("Kick"))
					{
						Player user = Bukkit.getPlayer(args[2]);
						if (user == null || !Infected.isPlayerInGame(user) || !Infected.isPlayerInLobby(user))
						{
							player.sendMessage(plugin.I + ChatColor.RED + "The player must be playing Infected");
						} else
						{
							user.performCommand("Infected Leave");
							user.sendMessage(Methods.sendMessage("Admin_YouAreKicked", null, null, null));
							player.sendMessage(plugin.I + "You have kicked " + user.getName() + " from Infected");
						}
					} else if (args[1].equalsIgnoreCase("Reset"))
					{
						Player user = Bukkit.getPlayer(args[2]);
						if (user == null)
						{
							player.sendMessage(plugin.I + ChatColor.RED + "The player must be online");
							return true;
						} else
						{
							Infected.filesGetPlayers().set("Players." + user.getName().toLowerCase(), null);
							player.sendMessage(plugin.I + user.getName() + "'s now reset!");
						}
					}
				} else
				{
					if (sender instanceof Player)
					{
						Player p = (Player) sender;
						p.performCommand("Infected Admin");
					} else
					{
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "Infected Admin");
					}

				}
			} else
			{
				CommandSender player = sender;
				// If the args isn't anything else identified
				// //////////////////////////////////////////////////////////////////////////////////////////////////////INFECTED
				// INFO
				player.sendMessage("");
				player.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
				if (plugin.update && player.isOp())
				{
					player.sendMessage(plugin.I + ChatColor.RED + "Update Available: " + ChatColor.WHITE + plugin.name);
				}
				player.sendMessage(plugin.I + ChatColor.GRAY + "Author:" + ChatColor.GREEN + " xXSniperzzXx_SD");
				player.sendMessage(plugin.I + ChatColor.GRAY + "Version: " + ChatColor.GREEN + plugin.v);
				player.sendMessage(plugin.I + ChatColor.GRAY + "BukkitDev:" + ChatColor.GREEN + " http://bit.ly/QN6Xg5");
				if (player.hasPermission("Infected.SetUp")){
					player.sendMessage(plugin.I + ChatColor.GRAY + "Disguise Support:" + ChatColor.GREEN + " " + (plugin.getConfig().getBoolean("Disguise Support.Enabled") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "CrackShot Support:" + ChatColor.GREEN + " " + (plugin.getConfig().getBoolean("CrackShot Support.Enable") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Zombie Abilities: " + ChatColor.GREEN + "" + (plugin.getConfig().getBoolean("Zombie Abilities") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "TagAPI Support:" + ChatColor.GREEN + " " + (plugin.getConfig().getBoolean("TagAPI Support.Enable") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Vault Support:" + ChatColor.GREEN + " " + (plugin.getConfig().getBoolean("Vault Support.Enable") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "ScoreBoard Support:" + ChatColor.GREEN + " " + (plugin.getConfig().getBoolean("ScoreBoard Support") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Grenades: " + ChatColor.GREEN + "" + (Infected.filesGetGrenades().getBoolean("Use") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Shop: " + ChatColor.GREEN + "" + (Infected.filesGetShop().getBoolean("Use") ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
				}
				player.sendMessage(plugin.I + ChatColor.YELLOW + "For Help type: /Infected Help");
				player.sendMessage("");
				return true;
			}
			plugin.saveConfig();
			Infected.filesSaveArenas();
			Infected.filesSaveGrenades();
			Infected.filesSavePlayers();
		}
		return true;
	}

}