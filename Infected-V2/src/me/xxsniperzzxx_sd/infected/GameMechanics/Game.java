package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Events.InfectedGameEndEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedGameStartEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedVoteStartEvent;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.OldStats.MiscStats;
import me.xxsniperzzxx_sd.infected.GameMechanics.OldStats.PointsAndScores;
import me.xxsniperzzxx_sd.infected.Handlers.ItemHandler;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.LocationHandler;
import me.xxsniperzzxx_sd.infected.Handlers.MapHandler;
import me.xxsniperzzxx_sd.infected.Handlers.TimeHandler;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Game {

	public static void START() {

		Main.Winners.clear();
		Infected.filesReloadArenas();
		Infected.arenaReset();
		
		for (Player playing : Bukkit.getServer().getOnlinePlayers())
			if (Main.inGame.contains(playing.getName()))
			{

				if (Main.config.getBoolean("ScoreBoard Support"))
					ScoreBoard.updateScoreBoard();
				
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage("");
				playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
				playing.sendMessage("");
				playing.sendMessage(Messages.sendMessage(Msgs.VOTE_VOTETIME, null, TimeHandler.getTime(Long.valueOf(Main.voteTime))));
				playing.sendMessage(Messages.sendMessage(Msgs.VOTE_HOWTOVOTE, null, null));
				playing.sendMessage("");
				playing.sendMessage(Main.I + ChatColor.YELLOW + ChatColor.BOLD + "Arenas: " + ChatColor.WHITE + ChatColor.ITALIC + MapHandler.getPossibleMaps() + ", random");
				playing.sendMessage("");
				playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
			
				Main.Winners.add(playing.getName());
				ScoreBoard.updateScoreBoard();
			}

		// VOTEING TIME
		Bukkit.getServer().getPluginManager().callEvent(new InfectedVoteStartEvent(
				Main.inGame, Main.voteTime));

		Infected.setGameState(GameState.VOTING);
		Main.timeVote = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
		{
			int timeleft = Main.voteTime;

			boolean allVoted = false;
			boolean divided = false;
			@Override
			public void run() {
				if (timeleft != -1)
				{
					if(Main.Voted4.size() == Main.inGame.size())
						allVoted = true;
						if(allVoted && !divided){
							divided = true;
							timeleft /= 2 ;
							allVoted = false;
							for (Player playing : Bukkit.getServer().getOnlinePlayers())
								if (Main.inGame.contains(playing.getName()))
								{
									playing.playSound(playing.getLocation(), Sound.ORB_PICKUP, 1, 1);
									playing.sendMessage("");
									playing.sendMessage(Messages.sendMessage(Msgs.VOTE_TIMELEFT, null, TimeHandler.getTime(Long.valueOf(timeleft))));
								}
						}
						
					timeleft -= 1;
					Main.currentTime = timeleft;
					
					for (Player playing : Bukkit.getServer().getOnlinePlayers())
						if (Main.inGame.contains(playing.getName()))
							playing.setLevel(timeleft);

					if (timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2|| timeleft == 1)
					{
						for (Player playing : Bukkit.getServer().getOnlinePlayers())
						{
							if (Main.inGame.contains(playing.getName()))
							{
								playing.playSound(playing.getLocation(), Sound.NOTE_STICKS, 1, 1);
								playing.playSound(playing.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
								playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
							}
						}
					}
					if (timeleft == 0)
					{
						for (Player playing : Bukkit.getServer().getOnlinePlayers())
						{
							if (Main.inGame.contains(playing.getName()))
							{
								playing.playSound(playing.getLocation(), Sound.NOTE_STICKS, 1, 5);
								playing.playSound(playing.getLocation(), Sound.NOTE_BASS_DRUM, 1, 5);
								playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 5);
							}
						}
					}
					if (timeleft == 60 || timeleft == 50 || timeleft == 40 || timeleft == 30 || timeleft == 20 || timeleft == 10 || timeleft == 9 || timeleft == 8 || timeleft == 7 || timeleft == 6 || timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
					{
						for (Player playing : Bukkit.getServer().getOnlinePlayers())
							if (Main.inGame.contains(playing.getName()))
							{
								playing.sendMessage(Messages.sendMessage(Msgs.VOTE_TIMELEFT, null, TimeHandler.getTime(Long.valueOf(timeleft))));
							}

					} else if (timeleft == -1)
					{
						MapHandler.getPossibleMaps();
						if (!Main.config.getBoolean("Allow Votes"))
						{
							if (Main.arenaNumber >= Main.possibleArenas.size())
								Main.arenaNumber = 0;
							Main.playingin = Main.possibleArenas.get(Main.arenaNumber);
							Main.arenaNumber++;
						} else if (Main.Votes.isEmpty())
						{
							Random r = new Random();
							int i = r.nextInt(Main.possibleArenas.size());
							Main.playingin = Main.possibleArenas.get(i);
						} else
						{
							// Determine the most voted for map
							Main.playingin = MiscStats.countdown(Main.Votes);
						}
						if (Main.playingin.equalsIgnoreCase("random"))
						{
							Main.possibleArenas.remove("random");
							Random r = new Random();
							int i = r.nextInt(Main.possibleArenas.size());
							Main.playingin = Main.possibleArenas.get(i);
						}
						for (Player playing: Bukkit.getServer().getOnlinePlayers())
							if (Main.inGame.contains(playing.getName()))
							{
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage("");
								playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
								playing.sendMessage("");
								playing.sendMessage(Main.I + "Game Starting in 5 Seconds.");
								playing.sendMessage("");
								playing.sendMessage(Messages.sendMessage(Msgs.GAME_MAP, null, null));
								playing.sendMessage("");
								playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
							}
						if (Main.config.getBoolean("ScoreBoard Support"))
							ScoreBoard.updateScoreBoard();
						
						for (String loc : Infected.filesGetArenas().getStringList("Arenas." + Main.playingin + ".Spawns"))
						{
							String[] floc = loc.split(",");
							World world = Bukkit.getServer().getWorld(floc[0]);
							if (new Location(world, Double.valueOf(floc[1]),
									Double.valueOf(floc[2]),
									Double.valueOf(floc[3])) == null)
							{
								
								for (Player playing: Bukkit.getServer().getOnlinePlayers())
								{
									if (Main.inGame.contains(playing.getName()))
									{
										playing.sendMessage(Main.I + " The arena's location was unable to be found. Please get an admin to reset it.");
										Reset.tp2LobbyAfter(playing);
									}

								}
							} else
							{
								Location Loc = new Location(world,
										Double.valueOf(floc[1]),
										Double.valueOf(floc[2]),
										Double.valueOf(floc[3]));
								if (!Bukkit.getServer().getWorld(world.getName()).getChunkAt(Loc).isLoaded())
									Bukkit.getServer().getWorld(world.getName()).getChunkAt(Loc).load();
							}
						}
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
							{
								@Override
								public void run() {
									// Get Players in lobby setup
									Main.inLobby.clear();
									for (Player playing: Bukkit.getServer().getOnlinePlayers())
									{
										if (Main.inGame.contains(playing.getName()))
										{
											playing.setHealth(20);
											playing.setFoodLevel(20);
											playing.sendMessage(Messages.sendMessage(Msgs.GAME_FIRSTINFECTEDIN, null, TimeHandler.getTime(Long.valueOf(Main.Wait))));
										
											LocationHandler.respawn(playing);
											Equip.equipHumans(playing);
											if (Main.config.getBoolean("Allow Breaking Certain Blocks"))
												playing.setGameMode(GameMode.SURVIVAL);
											else
												playing.setGameMode(GameMode.ADVENTURE);
											if (!Main.KillStreaks.containsKey(playing.getName()))
												Main.KillStreaks.put(playing.getName(), Integer.valueOf("0"));
										}
									}
									Main.Voted4.clear();
									Main.Votes.clear();
									Infected.setGameState(GameState.BEFOREINFECTED);
									if (Main.config.getBoolean("ScoreBoard Support"))
									{
										ScoreBoard.updateScoreBoard();
									}
									Main.timestart = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
									{
										int timeleft = Main.Wait;

										@Override
										public void run() {
											if (timeleft != -1)
											{
												timeleft -= 1;
												for (Player playing : Bukkit.getServer().getOnlinePlayers())
													if (Main.inGame.contains(playing.getName()))
														playing.setLevel(timeleft);
												
												Main.currentTime = timeleft;
												if (timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
												{
													for (Player playing : Bukkit.getServer().getOnlinePlayers())
													{
														if (Main.inGame.contains(playing.getName()))
														{
															playing.playSound(playing.getLocation(), Sound.NOTE_STICKS, 1, 1);
															playing.playSound(playing.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
															playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
														}
													}
												}
												if (timeleft == 0)
												{
													for (Player playing : Bukkit.getServer().getOnlinePlayers())
													{
														if (Main.inGame.contains(playing.getName()))
														{
															playing.playSound(playing.getLocation(), Sound.ZOMBIE_INFECT, 1, 5);
															playing.playSound(playing.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
															playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
														}
													}
												}
												if (timeleft == 60 || timeleft == 50 || timeleft == 40 || timeleft == 30 || timeleft == 20 || timeleft == 10 || timeleft == 9 || timeleft == 8 || timeleft == 7 || timeleft == 6 || timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
												{
													for (Player playing : Bukkit.getServer().getOnlinePlayers())
													{
														if (Main.inGame.contains(playing.getName()))
														{
															playing.sendMessage(Messages.sendMessage(Msgs.GAME_INFECTIONTIMER, null, TimeHandler.getTime(Long.valueOf(timeleft))));
															}
													}
												} 
												
												
												// Game is ready to actually start
												else if (timeleft == -1)
												{
													// Choose the first infected

													Infected.setGameState(GameState.STARTED);
													for (Player playing : Bukkit.getServer().getOnlinePlayers())
													{
														if (Main.inGame.contains(playing.getName()))
														{
															if (!Main.Winners.contains(playing.getName()))
															{
																Main.Winners.add(playing.getName());
															}
															Main.Timein.put(playing.getName(), System.currentTimeMillis() / 1000);
															Main.inLobby.remove(playing.getName());
														}
													}
													Zombify.newZombieSetUpEveryOne();
													Bukkit.getServer().getPluginManager().callEvent(new InfectedGameStartEvent(
															Main.inGame,
															Main.Wait,
															Bukkit.getPlayer(Main.zombies.get(0))));
													
													// Set the game's time limit
													Main.timeLimit = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
													{
														int timeleft = Main.GtimeLimit;

														@Override
														public void run() {
															if (timeleft != -1)
															{
																if(Infected.getGameState() != GameState.GAMEOVER){
																timeleft -= 1;
																Main.currentTime = timeleft;
																}
																for (Player playing : Bukkit.getServer().getOnlinePlayers())
																	if (Main.inGame.contains(playing.getName()))
																		playing.setLevel(timeleft);
																
																if (Main.GtimeLimit - timeleft == 10)
																	for (final Player playing : Bukkit.getOnlinePlayers())
																	{
																		if (Main.inGame.contains(playing.getName()) || Main.inLobby.contains(playing.getName()))
																		{
																			playing.teleport(playing.getLocation());
																		}
																	}
																if (timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
																{
																	for (Player playing : Bukkit.getServer().getOnlinePlayers())
																	{
																		if (Main.inGame.contains(playing.getName()))
																		{
																			playing.playSound(playing.getLocation(), Sound.NOTE_STICKS, 1, 1);
																			playing.playSound(playing.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
																			playing.playSound(playing.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
																		}
																	}
																}
																if (timeleft == 0)
																{
																	for (Player playing : Bukkit.getServer().getOnlinePlayers())
																	{
																		if (Main.inGame.contains(playing.getName()))
																		{
																			playing.playSound(playing.getLocation(), Sound.ORB_PICKUP, 1, 5);
																			playing.playSound(playing.getLocation(), Sound.LEVEL_UP, 1, 5);
																		}
																	}
																}
																if (timeleft == (Main.GtimeLimit / 4) * 3 || timeleft == Main.GtimeLimit / 2 || timeleft == Main.GtimeLimit / 4 || timeleft == 60 || timeleft == 10 || timeleft == 9 || timeleft == 8 || timeleft == 7 || timeleft == 6 || timeleft == 5 || timeleft == 4 || timeleft == 3 || timeleft == 2 || timeleft == 1)
																{
																	for (Player playing : Bukkit.getServer().getOnlinePlayers())
																	{
																		if (Main.inGame.contains(playing.getName()))
																			if (timeleft > 61)
																			{
																				playing.sendMessage(Messages.sendMessage(Msgs.GAME_TIMELEFT, null, TimeHandler.getTime(Long.valueOf(timeleft))));
																				playing.sendMessage(Messages.sendMessage(Msgs.GAME_TEAMS, null, null));
																			} else
																			{
																				playing.sendMessage(Messages.sendMessage(Msgs.GAME_TIMELEFT, null, TimeHandler.getTime(Long.valueOf(timeleft))));
																			}
																	}
																} else if (timeleft == -1)
																{
																	if(Infected.getGameState() == GameState.STARTED){
																		endGame(true);
																	}
																}
															}
														}
													}, 0L, 20L);
												}
											}
										}
									}, 0L, 20L);
									
								}
							}, 100L);
					}
				}
			}
		}, 0L, 20L);
	}
	

	@SuppressWarnings("deprecation")
	public static void endGame(Boolean DidHumansWin) {
		if(Infected.getGameState() == GameState.STARTED){
			Infected.setGameState(GameState.GAMEOVER);

			ScoreBoard.updateScoreBoard();
			for (Player playing : Bukkit.getServer().getOnlinePlayers())
			{
				if (Infected.isPlayerInGame(playing))
				{
					if (Main.KillStreaks.containsKey(playing.getName()))
					{
						if (Main.KillStreaks.get(playing.getName()) > Files.getPlayers().getInt("Players." + playing.getName().toLowerCase() + ".KillStreak"))
						{
							Files.getPlayers().set("Players." + playing.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(playing.getName()));
							Files.savePlayers();
						}
					}
				}
			}
			Bukkit.getServer().getPluginManager().callEvent(new InfectedGameEndEvent(Main.inGame, Main.Winners, DidHumansWin));

			if (DidHumansWin)
			{
				if (Main.config.getBoolean("Vault Support.Enable"))
				{
					int rewardMoney = Main.config.getInt("Vault Support.Reward");
	
					for (Player playing : Bukkit.getOnlinePlayers())
						if (Main.Winners.contains(playing.getName()))
	
							Main.economy.depositPlayer(playing.getName(), rewardMoney);
				}
				if (!(Main.config.getString("Command Reward").equalsIgnoreCase(null) || Main.config.getString("Command Reward").equalsIgnoreCase("[]")))
				{
					for (Player playing : Bukkit.getOnlinePlayers())
					{
						if (Main.Winners.contains(playing.getName()))
						{
							String s = Main.config.getString("Command Reward").replaceAll("<player>", playing.getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
						}
					}
				}
				for (String s : Main.config.getStringList("Rewards"))
				{
					for (Player playing : Bukkit.getOnlinePlayers())
					{
						if (Main.Winners.contains(playing.getName()))
						{
							playing.getInventory().setContents(Main.Inventory.get(playing.getName()));
							playing.updateInventory();
							playing.getInventory().addItem(ItemHandler.getItemStack(s));
							playing.updateInventory();
							Main.Inventory.put(playing.getName(), playing.getInventory().getContents());
							Reset.resetPlayersInventory(playing);
							playing.updateInventory();
						}
					}
				}
				for (final Player playing : Bukkit.getServer().getOnlinePlayers())
				{
					if (Main.inGame.contains(playing.getName()))
					{
						PointsAndScores.rewardPointsAndScore(playing, "Game Over");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.AFTERGAME_HUMANSWIN, null, null));
						StringBuilder winners = new StringBuilder();
						for (Object o : Main.Winners)
						{
							winners.append(o.toString());
							winners.append(", ");
						}
						playing.sendMessage(Main.I + "Winners: " + winners.toString());
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.GAME_MAP, null, null));
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						Infected.playerSetTime(playing.getName());
						Files.savePlayers();
						if (Main.config.getBoolean("Disguise Support.Enabled"))
							if (Disguises.isPlayerDisguised(playing))
								Disguises.unDisguisePlayer(playing);

						for (PotionEffect reffect : playing.getActivePotionEffects())
						{
							playing.removePotionEffect(reffect.getType());
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{
							@Override
							public void run() {
								Reset.tp2LobbyAfter(playing);
							}
						}, 100L);
					}
				}
			} else
			{
				for (final Player playing : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(playing.getName()))
					{
						PointsAndScores.rewardPointsAndScore(playing, "Game Over");
						Infected.playerSetTime(playing.getName());
						
						Files.savePlayers();

						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						playing.sendMessage(Messages.sendMessage(Msgs.AFTERGAME_ZOMBESWIN, null, null));
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.GAME_MAP, null, null));
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						for (PotionEffect reffect : playing.getActivePotionEffects())
							playing.removePotionEffect(reffect.getType());

						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{
							@Override
							public void run() {
								Reset.tp2LobbyAfter(playing);
							}
						}, 100L);
					}
			}
			ScoreBoard.updateScoreBoard();
			Main.playingin = "";
			Main.Winners.clear();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
			{
				@Override
				public void run() {
	
					Infected.setGameState(GameState.INLOBBY);
					if (Main.inGame.size() >= Main.config.getInt("Automatic Start.Minimum Players") && Infected.getGameState() == GameState.INLOBBY && Main.config.getBoolean("Automatic Start.Use"))
					{
						Game.restartGame();
					}
				}
			}, 10 * 60);
		}
	}


	public static void restartGame() {
		Bukkit.getScheduler().cancelTask(Main.timeVote);
		START();
	}
	
	public static void endGame(){
		Lobby Lobby = Main.Lobby;
		Lobby.getHumans().clear();
		Lobby.getZombies().clear();
		Lobby.setGameState(GameState.InLobby);
		
		
	}
}