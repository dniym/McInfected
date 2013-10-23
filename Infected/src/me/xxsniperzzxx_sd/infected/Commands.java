
package me.xxsniperzzxx_sd.infected;

import java.util.ArrayList;
import java.util.List;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Events.InfectedCommandEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedGrenadePurchaseEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerJoinEvent;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.Game;
import me.xxsniperzzxx_sd.infected.GameMechanics.Leave;
import me.xxsniperzzxx_sd.infected.GameMechanics.Menus;
import me.xxsniperzzxx_sd.infected.GameMechanics.Reset;
import me.xxsniperzzxx_sd.infected.GameMechanics.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.Vote;
import me.xxsniperzzxx_sd.infected.GameMechanics.Zombify;
import me.xxsniperzzxx_sd.infected.GameMechanics.Stats.Stats;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.ItemHandler;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
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
			InfectedCommandEvent cmdEvent = new InfectedCommandEvent(sender,
					args);
			Bukkit.getServer().getPluginManager().callEvent(cmdEvent);
			if (!cmdEvent.isCancelled())
			{
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
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else if (!Infected.isPlayerInGame(player) || Infected.isPlayerInLobby(player) || Infected.getGameState() != GameState.STARTED)
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTINGAME, player, null));
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
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_GAMESTARTED, player, null));
						return true;
					}
					if (!Infected.isPlayerInLobby(player))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTINGAME, player, null));
						return true;
					}
					if (!plugin.getConfig().getBoolean("Class Support"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.CLASSES_DISABLED, player, null));
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
						Menus.chooseClass(player);
				}

				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// JOIN

				else if (args.length > 0 && args[0].equalsIgnoreCase("Join"))
				{

					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (Infected.getGameState() == GameState.DISABLED)
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_INFECTEDDISABLED, null, null));
						return true;
					} else if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else if (plugin.inLobby.contains(player.getName()) || plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_ALREADYINGAME, null, null));
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
					} else if (plugin.getConfig().getBoolean("Prevent Joining During Game"))
					{
						if (Infected.getGameState() == GameState.STARTED)
						{
							player.sendMessage(Messages.sendMessage(Msgs.ERROR_JOINWELLSTARTEDBLOCKED, null, null));
						}
					} else
					{
						for (Player all : Bukkit.getServer().getOnlinePlayers())
						{
							if (Main.inGame.contains(all.getName()))
							{
								all.sendMessage(Messages.sendMessage(Msgs.LOBBY_OTHERJOINEDLOBBY, player, null));
							}
						}

						// Safe player's stats/stuff

						InfectedPlayerJoinEvent joinEvent = new InfectedPlayerJoinEvent(
								player,
								Main.inGame,
								Main.me.getConfig().getInt("Automatic Start.Minimum Players"));
						Bukkit.getServer().getPluginManager().callEvent(joinEvent);

						if (!joinEvent.isCancelled())
						{
							Main.inGame.add(player.getName());
							Main.Spot.put(player.getName(), LocationHandler.getLocationToString(player.getLocation()));
							Main.Health.put(player.getName(), player.getHealth());
							Main.Food.put(player.getName(), player.getFoodLevel());
							player.teleport(LocationHandler.getPlayerLocation(Main.me.getConfig().getString("Lobby")));
							Main.Levels.put(player.getName(), player.getLevel());
							Main.Exp.put(player.getName(), player.getExp());
							Main.Inventory.put(player.getName(), player.getInventory().getContents());
							Main.Armor.put(player.getName(), player.getInventory().getArmorContents());
							Main.Winners.clear();
							Main.inLobby.add(player.getName());
							Main.gamemode.put(player.getName(), player.getGameMode().toString());

							if (Main.config.getBoolean("ScoreBoard Support"))
							{
								ScoreBoard.updateScoreBoard();
							}

							if (Main.config.getBoolean("Disguise Support.Enabled"))
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
							Reset.resetPlayersInventory(player);

							if (Main.bVersion.equalsIgnoreCase(plugin.updateBukkitVersion))
							{
								if (Infected.filesGetShop().getBoolean("Save Items") && Infected.playerGetShopInventory(player) != null)
									player.getInventory().setContents(Infected.playerGetShopInventory(player));
							}
							player.sendMessage(Messages.sendMessage(Msgs.LOBBY_JOINLOBBY, null, null));

							player.setGameMode(GameMode.ADVENTURE);
							player.setFlying(false);

							if (!Infected.playergetLastHumanClass(player).equalsIgnoreCase("None"))
								Main.humanClasses.put(player.getName(), Infected.playergetLastHumanClass(player));

							if (!Infected.playergetLastZombieClass(player).equalsIgnoreCase("None"))
								Main.zombieClasses.put(player.getName(), Infected.playergetLastZombieClass(player));

							if (!Main.KillStreaks.containsKey(player.getName()))
								Main.KillStreaks.put(player.getName(), Integer.valueOf("0"));

							if (Main.inGame.size() >= Main.me.getConfig().getInt("Automatic Start.Minimum Players") && Infected.getGameState() == GameState.INLOBBY && Main.me.getConfig().getBoolean("Automatic Start.Use"))
							{
								Game.START();
							} else if (Infected.getGameState() == GameState.VOTING)
							{
								player.sendMessage(Messages.sendMessage(Msgs.VOTE_HOWTOVOTE, null, null));
								player.performCommand("Infected Arenas");

							} else if (Infected.getGameState() == GameState.STARTED)
							{
								player.setGameMode(GameMode.SURVIVAL);
								Zombify.joinInfectHuman(player);
								Infected.delPlayerInLobby(player);

							} else if (Infected.getGameState() == GameState.BEFOREINFECTED)
							{
								if (Main.config.getBoolean("ScoreBoard Support"))
								{
									player.setGameMode(GameMode.SURVIVAL);
									ScoreBoard.updateScoreBoard();
								}
								LocationHandler.respawn(player);
								if (!Main.Winners.contains(player.getName()))
								{
									Main.Winners.add(player.getName());
								}
								Main.Timein.put(player.getName(), System.currentTimeMillis() / 1000);
								if (!Main.KillStreaks.containsKey(player.getName()))
									Main.KillStreaks.put(player.getName(), Integer.valueOf("0"));
								player.setHealth(20.0);
								player.setFoodLevel(20);
								player.playEffect(player.getLocation(), Effect.SMOKE, BlockFace.UP);
								Equip.equipHumans(player);
								Infected.delPlayerInLobby(player);
							}
						}
					}

				}
				// ////////////////////////////////////////
				// REFRESH
				else if (args.length > 0 && args[0].equalsIgnoreCase("Refresh"))
				{
					if (!sender.hasPermission("Infected.Refresh"))
					{
						sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					for (Player playing : Bukkit.getOnlinePlayers())
					{
						if (plugin.inGame.contains(playing.getName()) || plugin.inLobby.contains(playing.getName()))
						{
							playing.teleport(playing.getLocation());
						}
					}
					sender.sendMessage("Refreshed all the players everyone playing should see each other.");
				}

				// //////////////////////////////////////
				// INFO
				else if (args.length > 0 && args[0].equalsIgnoreCase("Info"))
				{

					if (!sender.hasPermission("Infected.Info"))
					{
						sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
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

				// /////////////////////////////////////////////////////////////
				// SUICIDE
				else if (args.length > 0 && args[0].equalsIgnoreCase("Suicide"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					} else if (!sender.hasPermission("Infected.Suicide"))
					{
						sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					Player player = (Player) sender;

					if (plugin.inGame.contains(player.getName()) && Infected.getGameState() == GameState.STARTED)
					{
						if (plugin.humans.contains(player))
							for (Player playing : Bukkit.getServer().getOnlinePlayers())
							{
								if ((!(playing == player)) && plugin.inGame.contains(playing.getName()))
									playing.sendMessage(Messages.sendMessage(Msgs.GAME_GOTINFECTED, player, null));
							}
						plugin.humans.remove(player.getName());
						if (!plugin.zombies.contains(player.getName()))
							plugin.zombies.add(player.getName());
						plugin.Winners.remove(player.getName());
						plugin.Lasthit.remove(player.getName());
						player.sendMessage(plugin.I + "You have become Infected!");
						player.addPotionEffect(new PotionEffect(
								PotionEffectType.CONFUSION, 20, 2));
						Equip.equipZombies(player);
						player.setHealth(20.0);
						player.setFoodLevel(20);
						Stats.setStats(player, 0, 1);
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
							LocationHandler.respawn(player);
							player.setFallDistance(0F);
						}
						Zombify.zombifyPlayer(player);
					} else
					{
						// If the player tries to suicide and they arnt in the
						// lobby
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTINGAME, player, null));
						return true;
					}
				}
				// //////////////////////////////////////////////////////////////////////////////
				// SHOP / STORE
				else if (args.length > 0 && (args[0].equalsIgnoreCase("Shop") || args[0].equalsIgnoreCase("Store")))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					
					if (!player.hasPermission("Infected.Shop"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTINGAME, null, null));
						return true;
					}
					else if (Infected.getGameState() == GameState.STARTED)
					{
						player.sendMessage(Messages.sendMessage(Msgs.SHOP_GUIONLYINLOBBY, null, null));
						return true;
					}
					else
					{
						if (!Infected.filesGetShop().getBoolean("Use GUI"))
						{
							Menus.openShopMenu(player);
						}
					}
				}
				// //////////////////////////////////////////////////////////////////////////////
				// GRENADES
				else if (args.length > 0 && (args[0].equalsIgnoreCase("Grenades") || args[0].equalsIgnoreCase("Grenade")))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!Infected.filesGetGrenades().getBoolean("Use"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.GRENADE_DISABLED, null, null));
						return true;
					} else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Messages.sendMessage(Msgs.GRENADE_ONLYBUYINGAME, null, null));
						return true;
					} else if (!player.hasPermission("Infected.Grenades"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else if (plugin.getConfig().getBoolean("Grenades.Only Humans Can Use") && plugin.zombies.contains(player.getName()))
					{
						player.sendMessage(Messages.sendMessage(Msgs.GRENADE_NOZOMBIES, null, null));
						return true;
					} else if (Infected.filesGetGrenades().getKeys(true) == null)
					{
						player.sendMessage(plugin.I + ChatColor.RED + " No Grenades were found...");
						return true;
					} else
					{
						if (args.length != 1)
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
								int gi = Integer.parseInt(args[1]) - 1;
								if (gi <= (plugin.Grenades.size() - 1))
								{

									if (Infected.playerGetPoints(player.getName()) > Infected.filesGetGrenades().getInt(plugin.Grenades.get(gi) + ".Cost"))
									{
										ItemStack itemstack = new ItemStack(
												Material.getMaterial(plugin.Grenades.get(gi)),
												1);
										ItemMeta im = itemstack.getItemMeta();
										im.setDisplayName("§e" + Infected.filesGetGrenades().getString(plugin.Grenades.get(gi) + ".Name"));
										itemstack.setItemMeta(im);

										InfectedGrenadePurchaseEvent grenadeEvent = new InfectedGrenadePurchaseEvent(
												player,
												itemstack,
												Infected.filesGetGrenades().getInt(plugin.Grenades.get(gi) + ".Cost"));
										Bukkit.getServer().getPluginManager().callEvent(grenadeEvent);
										if (!grenadeEvent.isCancelled())
										{
											player.getInventory().addItem(itemstack);
											Infected.playerSetPoints(player.getName(), Infected.playerGetPoints(player.getName()), Infected.filesGetGrenades().getInt(plugin.Grenades.get(gi) + ".Cost"));
											player.sendMessage(plugin.I + ChatColor.DARK_AQUA + "You have just bought a " + ChatColor.AQUA + Infected.filesGetGrenades().getString(plugin.Grenades.get(gi) + ".Name"));
										}
									} else
										player.sendMessage(plugin.I + ChatColor.RED + "You don't have enough points to make this purchase!");
								} else
									player.performCommand("Infected Grenades");
							} else
								player.performCommand("Infected Grenades");
						} else
						{
							player.sendMessage(plugin.I + ChatColor.GREEN + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "======" + ChatColor.GOLD + " Infected's Grenades Shop " + ChatColor.GREEN + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "======");

							String gname = null;
							int gcost = 0;
							int i = 1;
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
											player.sendMessage(plugin.I + ChatColor.GREEN + ">  " + ChatColor.GRAY + String.valueOf(i) + ". " + ChatColor.DARK_AQUA + ChatColor.BOLD + gname + ChatColor.DARK_GRAY + " - " + ChatColor.AQUA + ChatColor.ITALIC + gcost);
											i++;
										}
									}
								}
							}
							player.sendMessage(plugin.I + "To Buy Type: " + ChatColor.YELLOW + ChatColor.BOLD + "/Infected Grenades <ID>");
						}
					}
				}

				// ///////////////////////////////////////////////////////////////////
				// SETLOBBY
				else if (args.length > 0 && args[0].equalsIgnoreCase("SetLobby"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.setup"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					// Set Lobby's Warp
					Location loc = player.getLocation();
					LocationHandler.saveLocation(loc, "Lobby");

					player.sendMessage(plugin.I + "Lobby set!");
					return true;
				}

				// ///////////////////////////////////////////////////////////////////////
				// LIST
				else if (args.length > 0 && args[0].equalsIgnoreCase("List"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}

					if (args.length != 1)
					{

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
						// List possible lists
						player.sendMessage(plugin.I + ChatColor.RED + "Unknown List, Possible Lists: Playing, Humans, Zombies");
						return true;
					}
				}
				// //////////////////////////////////////////////////////////////
				// LEAVE
				else if (args.length > 0 && args[0].equalsIgnoreCase("Leave"))
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
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else if (plugin.inGame.contains(player.getName()) || plugin.inLobby.contains(player.getName()))
					{
						Leave.leaveGame(player, false);
					} else
					{
						// If the player tries to leave
						// without joining first
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTINGAME, player, null));
						return true;
					}
				}

				// /////////////////////////////////////////////////
				// HELP
				else if (args.length > 0 && args[0].equalsIgnoreCase("Help"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else
					{
						if (args.length != 1)
						{
							player.sendMessage("");
							player.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected Help" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
							player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join" + ChatColor.WHITE + " - Join Infected");
							player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave" + ChatColor.WHITE + " - Leave Infected");
							player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote" + ChatColor.WHITE + " - Vote for a map");
							player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes" + ChatColor.WHITE + " - Choose a class");
							if (player.hasPermission("Infected.Grenades"))
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades" + ChatColor.WHITE + " - See the purchasable grenades");
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
							{
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby" + ChatColor.WHITE + " - Set the main lobby");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn" + ChatColor.WHITE + " - Set the spawn for the selected arena");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns" + ChatColor.WHITE + " - List the number of spawns for a map");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn" + ChatColor.WHITE + " - Tp to a spawn ID");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn" + ChatColor.WHITE + " - Delete the spawn ID");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena" + ChatColor.WHITE + " - Select an arena");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create" + ChatColor.WHITE + " - Create an arena");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove" + ChatColor.WHITE + " - Remove an Arena");

							}
							if (player.hasPermission("Infected.Admin"))
							{
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Start" + ChatColor.WHITE + " - Force the game to start");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "End" + ChatColor.WHITE + " - Force the game to end");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Admin" + ChatColor.WHITE + " - Show the admin menu");
								player.sendMessage(plugin.I + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Refresh" + ChatColor.WHITE + " - Refresh all the players");
							}
						} else
						{
							if (sender instanceof Player && plugin.getConfig().getBoolean("Book For Help"))
							{
								Player p = (Player) sender;
								ItemStack is = new ItemStack(
										Material.WRITTEN_BOOK);
								BookMeta b = (BookMeta) is.getItemMeta();
								ArrayList<String> pages = new ArrayList<String>();
								pages.add(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Infected Player\n" + "     Commands" + "\n\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join\n" + ChatColor.GOLD + " - Join Infected\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave\n" + ChatColor.GOLD + " - Leave Infected\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote <Arena>\n" + ChatColor.GOLD + " - Vote for a map\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades [Id]\n" + ChatColor.GOLD + " - See the purchasable grenades\n" + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide \n" + ChatColor.GOLD + " - Suicide if your stuck");
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
									pages.add(ChatColor.GREEN + "" + ChatColor.BOLD + " Infected Admin\n" + "     Menu (2)" + "\n\n" + ChatColor.DARK_PURPLE + "/Inf Admin Reset <Player>\n" + ChatColor.GOLD + "/Inf Admin Shutdown\n" + ChatColor.LIGHT_PURPLE + "/Inf Admin Code\n");
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
								player.sendMessage(plugin.I + ChatColor.YELLOW + "For Help type: /Infected Help 1");

							}
						}
						return true;
					}
				}

				// ///////////////////////////////////////////////////////
				// VOTE
				else if (args.length > 0 && args[0].equalsIgnoreCase("Vote"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					if (!player.hasPermission("Infected.Join"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else if (!plugin.inGame.contains(player.getName()))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTINGAME, null, null));
						return true;
					} else if (Infected.getGameState() != GameState.INLOBBY && Infected.getGameState() != GameState.VOTING)
					{
						player.sendMessage(Messages.sendMessage(Msgs.VOTE_GAMEALREADYSTARTED, null, null));
						return true;
					} else if (plugin.Voted4.containsKey(player.getName()))
					{
						player.sendMessage(Messages.sendMessage(Msgs.VOTE_ALLREADYVOTED, null, null));
						return true;
					} else if (!plugin.getConfig().getBoolean("Votes.Allow"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.VOTE_NOTENABLED, null, null));
						return true;
					} else
					{
						if (args.length != 1)
						{
							if (plugin.config.getBoolean("Votes.Use a GUI"))
							{
								Menus.openVotingMenu(player);
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
									Vote.voteFor(player, voted4);
								}
							}
						} else
						{
							if (plugin.config.getBoolean("Votes.Use a GUI"))
							{
								Menus.openVotingMenu(player);
							} else
							{
								player.sendMessage(Messages.sendMessage(Msgs.VOTE_HOWTOVOTE, null, null));
								player.performCommand("Infected arenas");
							}
						}
						return true;
					}
				}

				// //////////////////////////////////////////////////
				// START
				else if (args.length > 0 && args[0].equalsIgnoreCase("Start"))
				{

					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
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
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_GAMESTARTED, null, null));
						return true;
					} else
					{
						Game.START();
					}
					return true;

				}

				// //////////////////////////////////////////////////
				// END
				else if (args.length > 0 && args[0].equalsIgnoreCase("End"))
				{

					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
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
							players.sendMessage(Messages.sendMessage(Msgs.GAME_FORCEDTOSTOP, null, null));
							for (PotionEffect reffect : players.getActivePotionEffects())
							{
								players.removePotionEffect(reffect.getType());
							}
						}
					}
					// reset lists, hashmaps and arena
					Reset.reset();
				}

				// //////////////////////////////////////////////////
				// ARENAS
				else if (args.length > 0 && args[0].equalsIgnoreCase("Arenas"))
				{

					CommandSender player = sender;
					if (!player.hasPermission("Infected.Arenas"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
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
					player.sendMessage(plugin.I + ChatColor.GRAY + "Arenas: " + ChatColor.GREEN + possible.toString() + " - " + ChatColor.DARK_GRAY + possibleU.toString());
					plugin.possibleArenasU.clear();
					return true;
				}
				// /////////////////////////////////////////////////////////////
				// ADMIN
				else if (args.length > 0 && args[0].equalsIgnoreCase("Admin"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Admin"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}

					if (args.length == 2)
					{
						if (args[1].equalsIgnoreCase("Shutdown"))
						{
							if (Infected.getGameState() != GameState.DISABLED)
							{
								Infected.setGameState(GameState.DISABLED);
								player.sendMessage(plugin.I + ChatColor.GRAY + "Joining Infected has been disabled.");
							} else
							{
								Infected.setGameState(GameState.INLOBBY);
								player.sendMessage(plugin.I + ChatColor.GRAY + "Joining Infected has been enabled.");
							}
						} else if (args[1].equalsIgnoreCase("Reload"))
						{
							System.out.println("===== Infected =====");
							Infected.filesReloadAll();
							plugin.addon.getAddons();
							System.out.println("====================");

							player.sendMessage(plugin.I + "Infecteds Files have been reloaded");
						} else if (args[1].equalsIgnoreCase("Code"))
						{
							player.sendMessage(plugin.I + "Code: " + ChatColor.WHITE + ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
							player.sendMessage(plugin.I + "This code has also been sent to your console to allow for copy and paste!");
							System.out.println(ItemHandler.getItemStackToString(((Player) sender).getItemInHand()));
						} else
						{
							player.sendMessage(plugin.I + ChatColor.RED + "Unknown Admin Command, Type /Infected Admin");
							return true;
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
								user.sendMessage(Messages.sendMessage(Msgs.ADMIN_YOUAREKICKED, null, null));
								player.sendMessage(plugin.I + "You have kicked " + user.getName() + " from Infected");
							}
						} else if (args[1].equalsIgnoreCase("Reset"))
						{
							String name = args[2];
							Infected.filesGetPlayers().set("Players." + name.toLowerCase(), null);
							player.sendMessage(plugin.I + name + "'s now reset!");
						}
					} else if (args.length == 4)
					{
						String user = args[2];

						int i = Integer.parseInt(args[3]);
						if (args[1].equalsIgnoreCase("Points"))
						{
							Infected.playerSetPoints(user, Infected.playerGetPoints(user) + i, 0);
							player.sendMessage(plugin.I + user + "'s new points is: " + Infected.playerGetPoints(user));
						} else if (args[1].equalsIgnoreCase("Score"))
						{
							Infected.playerSetScore(user, Infected.playerGetScore(user) + i);
							player.sendMessage(plugin.I + user + "'s new score is: " + Infected.playerGetScore(user));
						} else if (args[1].equalsIgnoreCase("kStats"))
						{
							Infected.playerSetKills(user, Infected.playerGetKills(user) + i);
							player.sendMessage(plugin.I + user + "'s new kill count is: " + Infected.playerGetKills(user));
						} else if (args[1].equalsIgnoreCase("DStats"))
						{
							Infected.playerSetDeaths(user, Infected.playerGetDeaths(user) + i);
							player.sendMessage(plugin.I + user + "'s new death count is: " + Infected.playerGetDeaths(user));
						} else
						{
							player.sendMessage(plugin.I + ChatColor.RED + "Thats an invalid command");
						}
					} else
					{
						player.sendMessage(plugin.I + ChatColor.GREEN + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "======" + ChatColor.GOLD + " Admin Menu " + ChatColor.GREEN + ChatColor.STRIKETHROUGH + ChatColor.BOLD + "======");
						player.sendMessage(plugin.I + ChatColor.AQUA + "/Inf Admin Points <Player> <#>");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Add points to a player(Also goes negative)");
						player.sendMessage(plugin.I + ChatColor.BLUE + "/Inf Admin Score <Player> <#>");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Add score to a player(Also goes negative)");
						player.sendMessage(plugin.I + ChatColor.DARK_AQUA + "/Inf Admin KStats <Player> <#>");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Add kills to a player(Also goes negative)");
						player.sendMessage(plugin.I + ChatColor.DARK_BLUE + "/Inf Admin DStats <Player> <#>");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Add deaths to a player(Also goes negative)");
						player.sendMessage(plugin.I + ChatColor.DARK_GRAY + "/Inf Admin Kick <Player>");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Kick a player out of Infected");
						player.sendMessage(plugin.I + ChatColor.DARK_GREEN + "/Inf Admin Reset <Player>");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Reset a player's stats");
						player.sendMessage(plugin.I + ChatColor.DARK_PURPLE + "/Inf Admin Shutdown");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Prevent joining Infected");
						player.sendMessage(plugin.I + ChatColor.DARK_RED + "/Inf Admin Reload");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"Reload the config");
						player.sendMessage(plugin.I + ChatColor.GOLD + "/Inf Admin Code");
						player.sendMessage(plugin.I + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC +"See Infected's item code for the item in hand");
					}
				}
				// ////////////////////////////////////////////
				// STATS
				else if (args.length > 0 && args[0].equalsIgnoreCase("Stats"))
				{

					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.Stats"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					if (args.length != 1)
					{

						if (!player.hasPermission("Infected.Stats.Other"))
						{
							player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
							return true;
						}
						String user = args[1].toLowerCase();
						if (!Infected.filesGetPlayers().contains(user))
						{
							player.sendMessage(plugin.I + "That users never played!");
							return true;
						}
						player.sendMessage("");
						player.sendMessage(plugin.I + ChatColor.YELLOW + "------= " + user + " =------");
						player.sendMessage(plugin.I + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Infected.playerGetPoints(user) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Score"));
						player.sendMessage(plugin.I + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Infected.playerGetTime(user));
						player.sendMessage(plugin.I + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Kills") + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Deaths") + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + Stats.KD(Bukkit.getPlayer(user)));
						player.sendMessage(plugin.I + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".KillStreak"));

					} else
					{
						String user = player.getName().toLowerCase();
						if (!Infected.filesGetPlayers().contains("Players." + user))
						{
							player.sendMessage(plugin.I + "You have never played Infected!");
							return true;
						}
						player.sendMessage("");
						player.sendMessage(plugin.I + ChatColor.YELLOW + "------= " + user + " =------");
						player.sendMessage(plugin.I + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Infected.playerGetPoints(user) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Score"));
						player.sendMessage(plugin.I + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Infected.playerGetTime(user));
						player.sendMessage(plugin.I + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Kills") + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".Deaths") + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + Stats.KD(Bukkit.getPlayer(user)));
						player.sendMessage(plugin.I + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Infected.filesGetPlayers().getInt("Players." + user.toLowerCase() + ".KillStreak"));

					}
				}

				// ////////////////////////////////////////////////
				// TPSPAWN
				else if (args.length > 0 && args[0].equalsIgnoreCase("TpSpawn"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;

					if (args.length != 1)
					{

						List<String> list = Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns");
						int i = Integer.valueOf(args[1]) - 1;
						if (i < list.size())
						{
							String s = list.get(i);
							player.teleport(LocationHandler.getPlayerLocation(s));
							sender.sendMessage(plugin.I + "You have teleported to spawn number " + (i + 1) + ".");
						} else
							sender.sendMessage(plugin.I + ChatColor.RED + "Infected doesn't know where your tying to go, check how many spawns this arena has again!");
					} else
					{
						if (!sender.hasPermission("Infected.SetUp"))
						{
							sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
							return true;
						}
						sender.sendMessage(plugin.I + ChatColor.RED + "/Inf TpSpawn <ID>");

					}
				}
				// //////////////////////////////////////////////////
				// TPLOBBY
				else if (args.length > 0 && args[0].equalsIgnoreCase("TpLobby"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					String s = plugin.config.getString("Lobby");
					player.teleport(LocationHandler.getPlayerLocation(s));
					sender.sendMessage(plugin.I + "You have teleported to the lobby.");

				}

				// /////////////////////////////////////////
				// DELSPAWN
				else if (args.length > 0 && args[0].equalsIgnoreCase("DelSpawn"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					if (args.length != 1)
					{

						List<String> list = Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns");
						int i = Integer.valueOf(args[1]) - 1;
						if (!(list.get(i) == null))
						{
							list.remove(i);
							Infected.filesGetArenas().set("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns", list);
							Infected.filesSaveArenas();
							sender.sendMessage(plugin.I + ChatColor.RED + "You have removed spawn number " + (i + 1) + ".");
						} else
							sender.sendMessage(plugin.I + ChatColor.RED + "Check how many spawns this arena has again!");
					} else
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "/Inf DelSpawn <ID>");
					}
				}

				// ///////////////////////////////////
				// SPAWNS
				else if (args.length > 0 && args[0].equalsIgnoreCase("Spawns"))
				{
					if (!sender.hasPermission("Infected.SetUp"))
					{
						sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
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

				// //////////////////////////////////////////////
				// SETSPAWN
				else if (args.length > 0 && args[0].equalsIgnoreCase("SetSpawn"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					}
					if (!plugin.Creating.containsKey(player.getName()))
					{
						player.sendMessage(plugin.I + ChatColor.RED + "You don't have an arena selected!");
						return true;
					}
					Location l = player.getLocation();
					String s = LocationHandler.getLocationToString(l);
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
					player.sendMessage(plugin.I + plugin.Creating.get(player.getName()) + " spawn #" + ChatColor.YELLOW + Infected.filesGetArenas().getStringList("Arenas." + plugin.Creating.get(sender.getName()) + ".Spawns").size() + ChatColor.LIGHT_PURPLE + " set at your location!");
					return true;
				}

				// /////////////////////////////////////////////
				// CREATE
				else if (args.length > 0 && args[0].equalsIgnoreCase("Create"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else
					{
						if (args.length != 1)
						{

							String arena = args[1].toLowerCase();
							Infected.filesReloadArenas();
							plugin.possibleArenas.clear();
							if (Infected.filesGetArenas().getConfigurationSection("Arenas") == null)
							{

								player.sendMessage(plugin.I + "Arena " + ChatColor.WHITE + ChatColor.BOLD + arena + ChatColor.LIGHT_PURPLE + " created.");
								player.sendMessage(plugin.I + ChatColor.DARK_AQUA + "Type " + ChatColor.YELLOW + "/Inf SetSpawn" + ChatColor.DARK_AQUA + " to finish the arena!");
								Infected.filesGetArenas().set("Arenas." + arena + ".Plain Zombie Survival", false);
								String[] list = { "55", "20" };
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Global", list);
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Human", "[]");
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Zombie", "[]");
								if (args.length == 3)
									Infected.filesGetArenas().set("Arenas." + arena + ".Creator", args[2]);
								else
									Infected.filesGetArenas().set("Arenas." + arena + ".Creator", "Unkown");

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

								player.sendMessage(plugin.I + "Arena " + ChatColor.WHITE + ChatColor.BOLD + arena + ChatColor.GRAY + " created.");
								player.sendMessage(plugin.I + ChatColor.DARK_AQUA + "Type " + ChatColor.YELLOW + "/Inf SetSpawn" + ChatColor.DARK_AQUA + " to finish the arena!");
								Infected.filesGetArenas().set("Arenas." + arena + ".World", player.getWorld().getName());
								String[] list = { "55", "20" };
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Global", list);
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Human", "[]");
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Zombie", "[]");
								Infected.filesGetArenas().set("Arenas." + arena + ".Plain Zombie Survival", false);
								if (args.length == 3)
									Infected.filesGetArenas().set("Arenas." + arena + ".Creator", args[2]);
								else
									Infected.filesGetArenas().set("Arenas." + arena + ".Creator", "Unkown");

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
								player.sendMessage(plugin.I + "Arena " + ChatColor.WHITE + ChatColor.BOLD + arena + ChatColor.GRAY + " created.");
								player.sendMessage(plugin.I + ChatColor.DARK_AQUA + "Type " + ChatColor.YELLOW + "/Inf SetSpawn" + ChatColor.DARK_AQUA + " to finish the arena!");
								String[] list = { "55", "20" };
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Global", list);
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Human", "[]");
								Infected.filesGetArenas().set("Arenas." + arena + ".Allow Breaking Of.Zombie", "[]");
								Infected.filesGetArenas().set("Arenas." + arena + ".Plain Zombie Survival", false);
								if (args.length == 3)
									Infected.filesGetArenas().set("Arenas." + arena + ".Creator", args[2]);
								else
									Infected.filesGetArenas().set("Arenas." + arena + ".Creator", "Unkown");

								Infected.filesSaveArenas();
								plugin.Creating.put(player.getName(), arena);
								return true;
							}
						} else
						{
							player.sendMessage(plugin.I + ChatColor.RED + "/Infected Create <Arena Name> [Creator]");
						}
					}
				}

				// /////////////////////////////////////////////
				// REMOVE
				else if (args.length > 0 && args[0].equalsIgnoreCase("Remove"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else
					{
						if (args.length != 1)
						{
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
						} else
						{
							player.sendMessage(plugin.I + ChatColor.RED + "/Infected Remove <Arena Name>");
						}
					}
				}

				// /////////////////////////////////////////////////////////////
				// TOP
				else if (args.length > 0 && args[0].equalsIgnoreCase("Top"))
				{
					CommandSender player = sender;
					if (!player.hasPermission("Infected.Top"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else
					{
						if (args.length != 1)
						{

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
								sender.sendMessage(Messages.sendMessage(Msgs.ERROR_NOTASTAT, null, null));
								return true;
							}
							String[] top = Stats.getTop5(Stat);
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
						} else
						{
							player.sendMessage(plugin.I + ChatColor.RED + "/Infected Top <Kills/Deaths/Points/Score>");
						}
					}
				}

				// ////////////////////////////////////////////////////////
				// SETARENA
				else if (args.length > 0 && args[0].equalsIgnoreCase("SetArena"))
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage(plugin.I + ChatColor.RED + "Expected a player!");
						return true;
					}
					Player player = (Player) sender;
					if (!player.hasPermission("Infected.SetUp"))
					{
						player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						return true;
					} else
					{
						if (args.length != 1)
						{

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
						} else
						{
							player.sendMessage(plugin.I + ChatColor.RED + "/Infected SetArena <Arena Name>");
							player.performCommand("Infected arenas");
						}
					}
				} else if (args.length > 0 && args[0].equalsIgnoreCase("Addons"))
				{
					CommandSender player = sender;
					player.sendMessage("");
					player.sendMessage(plugin.I + ChatColor.GRAY + "Disguise Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("Disguise Support.Enabled") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Disguise Plugin:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("Disguise Support.DisguiseCraft") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "DisguiseCraft") : "") + (plugin.getConfig().getBoolean("Disguise Support.iDisguise") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "iDisguise") : "") + (plugin.getConfig().getBoolean("Disguise Support.LibsDisguises") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "LibsDisguises") : ""));
					player.sendMessage(plugin.I + ChatColor.GRAY + "CrackShot Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("CrackShot Support.Enable") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Zombie Abilities: " + "" + ChatColor.GREEN + ChatColor.ITALIC + "" + (plugin.getConfig().getBoolean("Zombie Abilities") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "TagAPI Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("TagAPI Support.Enable") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Factions Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("Factions Support.Enable") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "mcMMO Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("mcMMO Support.Enable") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Vault Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("Vault Support.Enable") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "ScoreBoard Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (plugin.getConfig().getBoolean("ScoreBoard Support") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Grenades: " + "" + ChatColor.GREEN + ChatColor.ITALIC + "" + (Infected.filesGetGrenades().getBoolean("Use") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage(plugin.I + ChatColor.GRAY + "Shop: " + "" + ChatColor.GREEN + ChatColor.ITALIC + "" + (Infected.filesGetShop().getBoolean("Use") ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
					player.sendMessage("");
				} else
				{
					CommandSender player = sender;
					player.sendMessage("");
					player.sendMessage(plugin.I + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
					if (plugin.update)
						player.sendMessage(plugin.I + ChatColor.RED + ChatColor.BOLD + "Update Available: " + ChatColor.WHITE + ChatColor.BOLD + plugin.name);
					player.sendMessage("");
					player.sendMessage(plugin.I + ChatColor.GRAY + "Author: " + ChatColor.GREEN + ChatColor.BOLD + "xXSniperzzXx_SD");
					player.sendMessage(plugin.I + ChatColor.GRAY + "Version: " + ChatColor.GREEN + ChatColor.BOLD + plugin.v);
					player.sendMessage(plugin.I + ChatColor.GRAY + "BukkitDev: " + ChatColor.GREEN + ChatColor.BOLD + "http://bit.ly/QN6Xg5");

					player.sendMessage(plugin.I + ChatColor.YELLOW + "For Help type: /Infected Help");
					player.sendMessage(plugin.I + ChatColor.YELLOW + "For Addons type: /Infected Addons");

					return true;
				}

			}
			Infected.filesSafeAll();

		}
		return true;
	}
}