
package me.sniperzciinema.infected.Handlers;

import java.util.ArrayList;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Events.InfectedStartGame;
import me.sniperzciinema.infected.Events.InfectedStartInfecting;
import me.sniperzciinema.infected.Events.InfectedStartVote;
import me.sniperzciinema.infected.GameMechanics.Equip;
import me.sniperzciinema.infected.GameMechanics.Game;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Arena.ArenaSettings;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Messages.Time;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Lobby {
	
	public static enum GameState
	{
		InLobby("In Lobby"),
		Voting("Voting"),
		Loading("Loading Arena"),
		Infecting("Infecting"),
		Started("Started"),
		GameOver("Game Over"),
		Disabled("Disabled");
		
		private String	s;
		
		private GameState(String string)
		{
			this.s = string;
		}
		
		@Override
		public String toString() {
			return this.s;
		}
	}
	
	private static ArrayList<Arena>		arenas	= new ArrayList<Arena>();
	private static ArrayList<Player>	inGame	= new ArrayList<Player>();
	
	private static Arena							activeArena;
	private static GameState					state		= GameState.InLobby;
	private static int								VotingTime, InfectingTime, GameTime, TimeLeft;
	
	private static int								currentGameTimer;
	
	public static Arena addArena(Arena arena) {
		if (!Lobby.arenas.contains(arena))
			Lobby.arenas.add(arena);
		return arena;
	};
	
	public static Arena addArena(String arenaName) {
		Arena arena = new Arena(StringUtil.getWord(arenaName));
		if (Lobby.arenas.contains(arena))
			Lobby.arenas.add(arena);
		return arena;
	}
	
	public static void addPlayerInGame(Player p) {
		Lobby.inGame.add(p);
	}
	
	public static void delPlayerInGame(Player p) {
		Lobby.inGame.remove(p);
	}
	
	public static Arena getActiveArena() {
		if (Lobby.activeArena == null)
			return new Arena("Sniperz Rocks");
		
		else
			return Lobby.activeArena;
	}
	
	public static Arena getArena(String arenaName) {
		for (Arena arena : Lobby.arenas)
			if (arena.getName().equalsIgnoreCase(arenaName))
				return arena;
		return null;
	}
	
	public static ArrayList<Arena> getArenas() {
		return Lobby.arenas;
	}
	
	public static ArenaSettings getArenaSettings(Arena arena) {
		return arena.getSettings();
	}
	
	/**
	 * @return the currentGameTimer
	 */
	public static int getCurrentGameTimer() {
		return Lobby.currentGameTimer;
	}
	
	public static GameState getGameState() {
		return Lobby.state;
	}
	
	public static ArrayList<Player> getHumans() {
		return getTeam(Team.Human);
	}
	
	@Deprecated
	public static ArrayList<Player> getInGame() {
		return Lobby.inGame;
	}
	
	public static ArrayList<String> getInValidArenas() {
		ArrayList<String> ba = new ArrayList<String>();
		for (Arena a : getArenas())
			if (!isArenaValid(a))
				ba.add(a.getName());
		return ba;
	}
	
	public static Location getLeave() {
		if ((Files.getConfig().getString("Leave") == null) || (Files.getConfig().getString("Leave") == ""))
			return null;
		else
			return LocationHandler.getPlayerLocation(Files.getConfig().getString("Leave"));
	}
	
	public static Location getLocation() {
		return LocationHandler.getPlayerLocation(Files.getConfig().getString("Lobby"));
	}
	
	public static ArrayList<Player> getPlayersInGame() {
		return Lobby.inGame;
	}
	
	public static Team getPlayersTeam(Player p) {
		return InfPlayerManager.getInfPlayer(p).getTeam();
	}
	
	/**
	 * @return the teams list
	 */
	public static ArrayList<Player> getTeam(Team team) {
		ArrayList<Player> teamList = new ArrayList<Player>();
		for (Player player : getPlayersInGame())
		{
			InfPlayer ip = InfPlayerManager.getInfPlayer(player);
			if (ip.getTeam() == team)
				teamList.add(player);
		}
		return teamList;
	}
	
	/**
	 * @return the currentTime
	 */
	public static int getTimeLeft() {
		return Lobby.TimeLeft;
	}
	
	public static ArrayList<String> getValidArenas() {
		ArrayList<String> ga = new ArrayList<String>();
		for (Arena a : getArenas())
			if (isArenaValid(a))
				ga.add(a.getName());
		return ga;
	}
	
	public static ArrayList<Player> getZombies() {
		return getTeam(Team.Zombie);
	}
	
	// Check if the arena is avalid
	public static boolean isArenaValid(Arena arena) {
		if (arena == null)
			return false;
		else if (!arena.getSpawns(Team.Global).isEmpty() || (!arena.getSpawns(Team.Zombie).isEmpty() && !arena.getSpawns(Team.Human).isEmpty()))
			return true;
		else
			return !arena.getSpawns(Team.Global).isEmpty();
	}
	
	// Check if the arena is avalid
	public static boolean isArenaValid(String name) {
		name = StringUtil.getWord(name);
		if (!Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty() || (!Files.getArenas().getStringList("Arenas." + name + ".Zombie Spawns").isEmpty() && !Files.getArenas().getStringList("Arenas." + name + ".Human Spawns").isEmpty()))
			return true;
		else
			return !Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty();
	}
	
	public static boolean isHuman(Player p) {
		return InfPlayerManager.getInfPlayer(p).getTeam() == Team.Human;
	}
	
	public static boolean isInGame(Player p) {
		return Lobby.inGame.contains(p);
	}
	
	public static boolean isZombie(Player p) {
		return InfPlayerManager.getInfPlayer(p).getTeam() == Team.Zombie;
	}
	
	public static void loadAllArenas() {
		Lobby.arenas.clear();
		if (Files.getArenas().getConfigurationSection("Arenas") != null)
			for (String a : Files.getArenas().getConfigurationSection("Arenas").getKeys(false))
			{
				Arena arena = new Arena(StringUtil.getWord(a));
				addArena(arena);
				
				if (Settings.logAreansEnabled())
					System.out.println("Loaded Arena: " + arena.getName());
			}
		else if (Settings.logAreansEnabled())
			System.out.println("Couldn't Find Any Arenas");
		
	}
	
	public static void loadArenas() {
		if (Files.getArenas().getConfigurationSection("Arenas") != null)
			for (String s : Files.getArenas().getConfigurationSection("Arenas").getKeys(false))
			{
				Arena arena = new Arena(StringUtil.getWord(s));
				addArena(arena);
				System.out.println("Loaded Arena: " + arena.getName());
			}
	}
	
	public static boolean oppositeTeams(Player p, Player u) {
		InfPlayer ip = InfPlayerManager.getInfPlayer(p);
		InfPlayer iu = InfPlayerManager.getInfPlayer(u);
		return (ip.getTeam() != iu.getTeam());
	}
	
	public static void removeArena(Arena arena) {
		Lobby.arenas.remove(arena);
		Files.getArenas().set("Arenas." + arena.getName(), null);
		Files.saveArenas();
	}
	
	public static void removeArena(String arenaName) {
		for (Arena arena : Lobby.arenas)
			if (arena.getName().equalsIgnoreCase(StringUtil.getWord(arenaName)))
				Lobby.arenas.remove(arena);
	}
	
	public static void reset() {
		stopTimer();
		setGameState(GameState.InLobby);
		for (Arena a : getArenas())
			a.setVotes(0);
		
		resetArena(getActiveArena());
		setActiveArena(null);
		for (Player player : getPlayersInGame())
			InfPlayerManager.getInfPlayer(player).setTeam(Team.Human);
		
	}
	
	public static void resetArena(Arena arena) {
		
		// Get the arena to fix any broken blocks
		arena.reset();
		
	}
	
	public static void setActiveArena(Arena arena) {
		Lobby.activeArena = arena;
	}
	
	/**
	 * @param currentGameTimer
	 *          the currentGameTimer to set
	 */
	public static void setCurrentGameTimer(int currentGameTime) {
		Lobby.currentGameTimer = currentGameTime;
	}
	
	public static void setGameState(GameState gamestate) {
		Lobby.state = gamestate;
	}
	
	public static void setLeave(Location loc) {
		Files.getConfig().set("Leave", LocationHandler.getLocationToString(loc));
		Files.saveConfig();
	}
	
	public static void setLocation(Location loc) {
		Files.getConfig().set("Lobby", LocationHandler.getLocationToString(loc));
		Files.saveConfig();
	}
	
	/**
	 * Set the InfPlayer's team
	 * 
	 * @param ip
	 * @param team
	 */
	public static void setTeam(InfPlayer ip, Team team) {
		ip.setTeam(team);
	}
	
	/**
	 * @param currentTime
	 *          the currentTime to set
	 */
	public static void setTimeLeft(int imeLeft) {
		Lobby.TimeLeft = imeLeft;
	}
	
	public static void stopTimer() {
		Bukkit.getScheduler().cancelTask(Lobby.currentGameTimer);
		Lobby.TimeLeft = 0;
	}
	
	public static void timerStartGame() {
		stopTimer();
		setGameState(GameState.Started);
		InfectedStartGame e = new InfectedStartGame();
		Bukkit.getPluginManager().callEvent(e);
		
		Lobby.GameTime = getActiveArena().getSettings().getGameTime();
		Lobby.TimeLeft = Lobby.GameTime;
		
		for (Player player : getPlayersInGame())
		{
			InfPlayer ip = InfPlayerManager.getInfPlayer(player);
			ip.getScoreBoard().showProperBoard();
		}
		Lobby.currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
		{
			
			@Override
			public void run() {
				if (Lobby.TimeLeft != 0)
				{
					Lobby.TimeLeft -= 1;
					for (Player player : getPlayersInGame())
						player.setLevel(Lobby.TimeLeft);
					
					if ((Lobby.GameTime - Lobby.TimeLeft) == 10)
						for (Player player : getPlayersInGame())
							player.teleport(player.getLocation());
					
					if ((Lobby.TimeLeft == ((Lobby.GameTime / 4) * 3)) || (Lobby.TimeLeft == (Lobby.GameTime / 2)) || (Lobby.TimeLeft == (Lobby.GameTime / 4)) || (Lobby.TimeLeft == 60) || (Lobby.TimeLeft == 10) || (Lobby.TimeLeft == 9) || (Lobby.TimeLeft == 8) || (Lobby.TimeLeft == 7) || (Lobby.TimeLeft == 6) || (Lobby.TimeLeft == 5) || (Lobby.TimeLeft == 4) || (Lobby.TimeLeft == 3) || (Lobby.TimeLeft == 2) || (Lobby.TimeLeft == 1))
						if (Lobby.TimeLeft > 61)
							for (Player player : getPlayersInGame())
							{
								player.sendMessage(Msgs.Game_Time_Left_Game.getString("<time>", Time.getTime((long) Lobby.TimeLeft)));
								player.sendMessage(Msgs.Game_Players_Left.getString("<humans>", String.valueOf(getTeam(Team.Human).size()), "<zombies>", String.valueOf(getTeam(Team.Zombie).size())));
							}
						else
							for (Player player : getPlayersInGame())
								player.sendMessage(Msgs.Game_Time_Left_Game.getString("<time>", Time.getTime((long) Lobby.TimeLeft)));
					if ((Lobby.TimeLeft == 5) || (Lobby.TimeLeft == 4) || (Lobby.TimeLeft == 3) || (Lobby.TimeLeft == 2))
						for (Player player : getPlayersInGame())
						{
							player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
							player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
						}
					if (Lobby.TimeLeft == 1)
						for (Player player : getPlayersInGame())
						{
							player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 5);
							player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 5);
						}
					else if (Lobby.TimeLeft == 0)
						Game.endGame(true);
				}
			}
		}, 0L, 20L);
	}
	
	public static void timerStartInfecting() {
		
		stopTimer();
		setGameState(GameState.Infecting);
		
		InfectedStartInfecting e = new InfectedStartInfecting();
		Bukkit.getPluginManager().callEvent(e);
		
		for (Player player : getPlayersInGame())
		{
			InfPlayer ip = InfPlayerManager.getInfPlayer(player);
			ip.getScoreBoard().showProperBoard();
			ip.setTimeIn(System.currentTimeMillis() / 1000);
		}
		
		Lobby.InfectingTime = getActiveArena().getSettings().getInfectingTime();
		Lobby.TimeLeft = Lobby.InfectingTime;
		Lobby.currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
		{
			
			@Override
			public void run() {
				if (Lobby.TimeLeft != 0)
				{
					Lobby.TimeLeft -= 1;
					for (Player player : getPlayersInGame())
						player.setLevel(Lobby.TimeLeft);
					
					if ((Lobby.TimeLeft == 30) || (Lobby.TimeLeft == 20) || (Lobby.TimeLeft == 10) || (Lobby.TimeLeft == 9) || (Lobby.TimeLeft == 8) || (Lobby.TimeLeft == 7) || (Lobby.TimeLeft == 6) || (Lobby.TimeLeft == 5) || (Lobby.TimeLeft == 4) || (Lobby.TimeLeft == 3) || (Lobby.TimeLeft == 2) || (Lobby.TimeLeft == 1))
						for (Player player : getPlayersInGame())
							player.sendMessage(Msgs.Game_Time_Left_Infecting.getString("<time>", Time.getTime((long) getTimeLeft())));
					if ((Lobby.TimeLeft == 5) || (Lobby.TimeLeft == 4) || (Lobby.TimeLeft == 3) || (Lobby.TimeLeft == 2))
						for (Player player : getPlayersInGame())
						{
							player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
							player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
						}
					if (Lobby.TimeLeft == 1)
						for (Player player : getPlayersInGame())
						{
							player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 1, 5);
							player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
						}
					else if (Lobby.TimeLeft == 0)
					{
						Game.chooseAlphas();
						
						for (Player player : getPlayersInGame())
						{
							InfPlayer ip = InfPlayerManager.getInfPlayer(player);
							if ((ip.getTeam() != Team.Human) && (ip.getTeam() != Team.Zombie))
								ip.setTeam(Team.Human);
						}
						timerStartGame();
					}
				}
			}
		}, 0L, 20L);
		
	}
	
	public static void timerStartVote() {
		if (getGameState() == GameState.InLobby)
		{
			stopTimer();
			Lobby.VotingTime = Settings.getVotingTime();
			Lobby.TimeLeft = Lobby.VotingTime;
			
			setGameState(GameState.Voting);
			InfectedStartVote e = new InfectedStartVote();
			Bukkit.getPluginManager().callEvent(e);
			
			for (Player player : getPlayersInGame())
			{
				InfPlayer ip = InfPlayerManager.getInfPlayer(player);
				ip.getScoreBoard().showProperBoard();
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage("");
				player.sendMessage(Msgs.Format_Header.getString("<title>", " Vote "));
				player.sendMessage("");
				player.sendMessage(Msgs.Game_Time_Left_Voting.getString("<time>", Time.getTime((long) getTimeLeft())));
				player.sendMessage(Msgs.Help_Vote.getString());
				player.sendMessage("");
				player.sendMessage(Msgs.Format_Line.getString());
			}
			
			Lobby.currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
			{
				
				@Override
				public void run() {
					if (getTimeLeft() != 0)
					{
						Lobby.TimeLeft -= 1;
						
						for (Player player : getPlayersInGame())
							player.setLevel(getTimeLeft());
						
						if ((Lobby.TimeLeft == 60) || (Lobby.TimeLeft == 50) || (Lobby.TimeLeft == 40) || (Lobby.TimeLeft == 30) || (Lobby.TimeLeft == 20) || (Lobby.TimeLeft == 10) || (Lobby.TimeLeft == 9) || (Lobby.TimeLeft == 8) || (Lobby.TimeLeft == 7) || (Lobby.TimeLeft == 6) || (Lobby.TimeLeft == 5) || (Lobby.TimeLeft == 4) || (Lobby.TimeLeft == 3) || (Lobby.TimeLeft == 2) || (Lobby.TimeLeft == 1))
							for (Player player : getPlayersInGame())
								player.sendMessage(Msgs.Game_Time_Left_Voting.getString("<time>", Time.getTime((long) getTimeLeft())));
						if ((Lobby.TimeLeft == 5) || (Lobby.TimeLeft == 4) || (Lobby.TimeLeft == 3) || (Lobby.TimeLeft == 2))
							for (Player player : getPlayersInGame())
							{
								player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
								player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
								player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
							}
						if (Lobby.TimeLeft == 1)
							
							for (Player player : getPlayersInGame())
							{
								player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 5);
								player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 1, 5);
								player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 5);
							}
						else if (Lobby.TimeLeft == 0)
						{
							Arena arena = getArenas().get(0);
							for (Arena a : getArenas())
								if (a.getVotes() > arena.getVotes())
									arena = a;
							
							setActiveArena(arena);
							for (Player player : getPlayersInGame())
							{
								InfPlayer ip = InfPlayerManager.getInfPlayer(player);
								ip.getScoreBoard().showProperBoard();
								
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage("");
								player.sendMessage(Msgs.Format_Line.getString());
								player.sendMessage("");
								player.sendMessage(Msgs.Game_Starting_In_5.getString());
								player.sendMessage("");
								player.sendMessage(Msgs.Game_Info_Arena.getString("<arena>", getActiveArena().getName(), "<creator>", getActiveArena().getCreator()));
								player.sendMessage("");
								player.sendMessage(Msgs.Format_Line.getString());
								setGameState(GameState.Loading);
								stopTimer();
							}
							Lobby.currentGameTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
							{
								
								@Override
								public void run() {
									
									for (Player player : getPlayersInGame())
									{
										player.setGameMode(GameMode.SURVIVAL);
										InfPlayer up = InfPlayerManager.getInfPlayer(player);
										up.respawn();
										Equip.equip(player);
									}
									timerStartInfecting();
								}
							}, 100L);
						}
					}
				}
			}, 0L, 20L);
		}
	}
	
	public Lobby()
	{
		loadAllArenas();
	}
	
}
