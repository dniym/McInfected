
package me.sniperzciinema.infected.Handlers;

import java.util.ArrayList;

import me.sniperzciinema.infected.Game;
import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Events.InfectedStartGame;
import me.sniperzciinema.infected.Events.InfectedStartInfecting;
import me.sniperzciinema.infected.Events.InfectedStartVote;
import me.sniperzciinema.infected.GameMechanics.Equip;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Arena.ArenaSettings;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
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

	// All the Arena Objects
	private static ArrayList<Arena> arenas = new ArrayList<Arena>();
	// All the players playing Infected
	private static ArrayList<Player> inGame = new ArrayList<Player>();
	// All the humans
	private static ArrayList<Player> humans = new ArrayList<Player>();
	// All the zombies
	private static ArrayList<Player> zombies = new ArrayList<Player>();
	// What ever arena we're playing on
	private static Arena activeArena;
	// The games state
	private static GameState state = GameState.InLobby;

	private static int VotingTime;
	private static int InfectingTime;
	private static int GameTime;
	private static int TimeLeft;
	private static int currentGameTimer;

	public enum GameState
	{
		InLobby("In Lobby"),
		Voting("Voting"),
		Loading("Loading Arena"),
		Infecting("Infecting"),
		Started("Started"),
		GameOver("Game Over"),
		Disabled("Disabled");

		private String s;

		private GameState(String string)
		{
			s = string;
		}

		public String toString() {
			return s;
		}
	};

	public Lobby()
	{
		loadArenas();
		InfClassManager.loadConfigClasses();
		GrenadeManager.loadConfigGrenades();
	}

	public static ArrayList<String> getValidArenas() {
		ArrayList<String> ga = new ArrayList<String>();
		for (Arena a : getArenas())
			if (isArenaValid(a))
				ga.add(a.getName());
		return ga;
	}

	public static ArrayList<String> getInValidArenas() {
		ArrayList<String> ba = new ArrayList<String>();
		for (Arena a : getArenas())
			if (!isArenaValid(a))
				ba.add(a.getName());
		return ba;
	}

	public static Location getLocation() {
		return LocationHandler.getPlayerLocation(Files.getConfig().getString("Lobby"));
	}

	public static void setLocation(Location loc) {
		Files.getConfig().set("Lobby", LocationHandler.getLocationToString(loc));
		Files.saveConfig();
	}

	public static ArrayList<Arena> getArenas() {
		return arenas;
	}

	public static boolean isInGame(Player p) {
		return inGame.contains(p);
	}

	public static void addPlayerInGame(Player p) {
		inGame.add(p);
	}

	public static void delPlayerInGame(Player p) {
		inGame.remove(p);
	}

	public static ArrayList<Player> getInGame() {
		return inGame;
	}

	public static void addHuman(Player p) {
		humans.add(p);
	}

	public static void delHuman(Player p) {
		humans.remove(p);
	}

	public static boolean isHuman(Player p) {
		return humans.contains(p);
	}

	public static void delZombie(Player p) {
		zombies.remove(p);
	}

	public static void addZombie(Player p) {
		zombies.add(p);
	}

	public static boolean isZombie(Player p) {
		return zombies.contains(p);
	}

	public static boolean oppositeTeams(Player p, Player u) {
		return !((isZombie(p) && isZombie(u)) || (isHuman(p) && isHuman(u))) || (isInGame(p) && !isInGame(u)) || (!isInGame(p) && isInGame(u));
	}

	public static ArrayList<Player> getHumans() {
		return humans;
	}

	public static ArrayList<Player> getZombies() {
		return zombies;
	}

	public static ArenaSettings getArenaSettings(Arena arena) {
		return arena.getSettings();
	}

	public static Arena getActiveArena() {
		if (activeArena == null)
			return new Arena("Sniperz Rocks");

		else
			return activeArena;
	}

	public static void setActiveArena(Arena arena) {
		activeArena = arena;
	}

	public static void setGameState(GameState gamestate) {
		state = gamestate;
	}

	public static GameState getGameState() {
		return state;
	}

	public static Arena addArena(Arena arena) {
		if (!arenas.contains(arena))
			arenas.add(arena);
		return arena;
	}

	public static Arena addArena(String arenaName) {
		Arena arena = new Arena(StringUtil.getWord(arenaName));
		if (arenas.contains(arena))
			arenas.add(arena);
		return arena;
	}

	public static Arena getArena(String arenaName) {
		for (Arena arena : arenas)
		{
			if (arena.getName().equalsIgnoreCase(arenaName))
				return arena;
		}
		return null;
	}

	public static void removeArena(Arena arena) {
		arenas.remove(arena);
		Files.getArenas().set("Arenas." + arena.getName(), null);
		Files.saveArenas();
	}

	public static void removeArena(String arenaName) {
		for (Arena arena : arenas)
		{
			if (arena.getName().equalsIgnoreCase(StringUtil.getWord(arenaName)))
				arenas.remove(arena);
		}
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

	// Check if the arena is avalid
	public static boolean isArenaValid(String name) {
		name = StringUtil.getWord(name);
		if (!Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty() || (!Files.getArenas().getStringList("Arenas." + name + ".Zombie Spawns").isEmpty() && !Files.getArenas().getStringList("Arenas." + name + ".Human Spawns").isEmpty()))
			return true;
		else
			return !Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty();
	}

	// Check if the arena is avalid
	public static boolean isArenaValid(Arena arena) {
		if (arena == null)
			return false;
		else
		{
			if (!arena.getSpawns(Team.Global).isEmpty() || (!arena.getSpawns(Team.Zombie).isEmpty() && !arena.getSpawns(Team.Human).isEmpty()))
				return true;
			else
				return !arena.getSpawns(Team.Global).isEmpty();
		}
	}

	public static void resetArena(Arena arena) {

		// Get the arena to fix any broken blocks
		arena.reset();

	}

	public static void reset() {
		stopTimer();
		Lobby.setGameState(GameState.InLobby);
		for (Arena a : Lobby.getArenas())
			a.setVotes(0);

		resetArena(getActiveArena());
		setActiveArena(null);
		getHumans().clear();
		getZombies().clear();
	}

	/**
	 * @return the currentTime
	 */
	public static int getTimeLeft() {
		return TimeLeft;
	}

	/**
	 * @param currentTime
	 *            the currentTime to set
	 */
	public static void setTimeLeft(int imeLeft) {
		TimeLeft = imeLeft;
	}

	/**
	 * @return the currentGameTimer
	 */
	public static int getCurrentGameTimer() {
		return currentGameTimer;
	}

	/**
	 * @param currentGameTimer
	 *            the currentGameTimer to set
	 */
	public static void setCurrentGameTimer(int currentGameTimer) {
		Lobby.currentGameTimer = currentGameTimer;
	}

	public static void stopTimer() {
		Bukkit.getScheduler().cancelTask(currentGameTimer);
		TimeLeft = 0;
	}

	public static void timerStartVote() {
		if (Lobby.getGameState() == GameState.InLobby)
		{
			stopTimer();
			VotingTime = Settings.getVotingTime();
			TimeLeft = VotingTime;

			setGameState(GameState.Voting);
			InfectedStartVote e = new InfectedStartVote();
			Bukkit.getPluginManager().callEvent(e);

			for (Player u : getInGame())
			{
				InfPlayer up = InfPlayerManager.getInfPlayer(u);
				up.getScoreBoard().showProperBoard();
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Header.getString("<title>", " Vote "));
				u.sendMessage("");
				u.sendMessage(Msgs.Game_Time_Left_Voting.getString("<time>", Time.getTime((long) getTimeLeft())));
				u.sendMessage(Msgs.Help_Vote.getString());
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Line.getString());
			}

			currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
			{

				@Override
				public void run() {
					if (getTimeLeft() != 0)
					{
						TimeLeft -= 1;

						for (Player u : getInGame())
							u.setLevel(getTimeLeft());

						if (TimeLeft == 60 || TimeLeft == 50 || TimeLeft == 40 || TimeLeft == 30 || TimeLeft == 20 || TimeLeft == 10 || TimeLeft == 9 || TimeLeft == 8 || TimeLeft == 7 || TimeLeft == 6 || TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2 || TimeLeft == 1)
						{
							for (Player u : getInGame())
								u.sendMessage(Msgs.Game_Time_Left_Voting.getString("<time>", Time.getTime((long) getTimeLeft())));
						}
						if (TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2)
						{
							for (Player u : getInGame())
							{
								u.playSound(u.getLocation(), Sound.NOTE_STICKS, 1, 1);
								u.playSound(u.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
								u.playSound(u.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
							}
						}
						if (TimeLeft == 1)

							for (Player u : getInGame())
							{
								u.playSound(u.getLocation(), Sound.NOTE_STICKS, 1, 5);
								u.playSound(u.getLocation(), Sound.NOTE_BASS_DRUM, 1, 5);
								u.playSound(u.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 5);
							}
						else if (TimeLeft == 0)
						{
							Arena arena = getArenas().get(0);
							for (Arena a : getArenas())
							{
								if (a.getVotes() > arena.getVotes())
									arena = a;
							}

							setActiveArena(arena);
							for (Player u : getInGame())
							{
								InfPlayer up = InfPlayerManager.getInfPlayer(u);
								up.getScoreBoard().showProperBoard();

								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage("");
								u.sendMessage(Msgs.Format_Line.getString());
								u.sendMessage("");
								u.sendMessage(Msgs.Game_Starting_In_5.getString());
								u.sendMessage("");
								u.sendMessage(Msgs.Game_Info_Arena.getString("<arena>", getActiveArena().getName(), "<creator>", getActiveArena().getCreator()));
								u.sendMessage("");
								u.sendMessage(Msgs.Format_Line.getString());
								Lobby.setGameState(GameState.Loading);
								stopTimer();
							}
							currentGameTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
							{

								@Override
								public void run() {

									for (Player u : getInGame())
									{
										u.setGameMode(GameMode.SURVIVAL);
										InfPlayer up = InfPlayerManager.getInfPlayer(u);
										up.respawn();
										Equip.equip(u);
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

	public static void timerStartInfecting() {

		stopTimer();
		setGameState(GameState.Infecting);

		InfectedStartInfecting e = new InfectedStartInfecting();
		Bukkit.getPluginManager().callEvent(e);

		for (Player u : getInGame())
		{
			InfPlayer ip = InfPlayerManager.getInfPlayer(u);
			ip.getScoreBoard().showProperBoard();
			ip.setTimeIn(System.currentTimeMillis() / 1000);
		}

		InfectingTime = getActiveArena().getSettings().getInfectingTime();
		TimeLeft = InfectingTime;
		currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
		{

			@Override
			public void run() {
				if (TimeLeft != 0)
				{
					TimeLeft -= 1;
					for (Player u : getInGame())
						u.setLevel(TimeLeft);

					if (TimeLeft == 30 || TimeLeft == 20 || TimeLeft == 10 || TimeLeft == 9 || TimeLeft == 8 || TimeLeft == 7 || TimeLeft == 6 || TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2 || TimeLeft == 1)
					{
						for (Player u : getInGame())
							u.sendMessage(Msgs.Game_Time_Left_Infecting.getString("<time>", Time.getTime((long) getTimeLeft())));
					}
					if (TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2)
					{

						for (Player u : getInGame())
						{
							u.playSound(u.getLocation(), Sound.NOTE_STICKS, 1, 1);
							u.playSound(u.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							u.playSound(u.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
						}
					}
					if (TimeLeft == 1)
					{
						for (Player u : getInGame())
						{
							u.playSound(u.getLocation(), Sound.ZOMBIE_INFECT, 1, 5);
							u.playSound(u.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							u.playSound(u.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
						}
					}

					else if (TimeLeft == 0)
					{
						Game.chooseAlphas();

						for (Player u : getInGame())
						{
							InfPlayer ip = InfPlayerManager.getInfPlayer(u);
							if (!Lobby.isHuman(u) && !Lobby.isZombie(u))
							{
								Lobby.addHuman(u);
								ip.setTeam(Team.Human);
							}
						}
						timerStartGame();
					}
				}
			}
		}, 0L, 20L);

	}

	public static void timerStartGame() {
		stopTimer();
		setGameState(GameState.Started);
		InfectedStartGame e = new InfectedStartGame();
		Bukkit.getPluginManager().callEvent(e);

		GameTime = getActiveArena().getSettings().getGameTime();
		TimeLeft = GameTime;

		for (Player u : Lobby.getInGame())
		{
			InfPlayer up = InfPlayerManager.getInfPlayer(u);
			up.getScoreBoard().showProperBoard();
		}
		currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
		{

			@Override
			public void run() {
				if (TimeLeft != 0)
				{
					TimeLeft -= 1;
					for (Player u : getInGame())
						u.setLevel(TimeLeft);

					if (GameTime - TimeLeft == 10)
						for (Player u : getInGame())
							u.teleport(u.getLocation());

					if (TimeLeft == (GameTime / 4) * 3 || TimeLeft == GameTime / 2 || TimeLeft == GameTime / 4 || TimeLeft == 60 || TimeLeft == 10 || TimeLeft == 9 || TimeLeft == 8 || TimeLeft == 7 || TimeLeft == 6 || TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2 || TimeLeft == 1)
					{
						if (TimeLeft > 61)
						{
							for (Player u : getInGame())
							{
								u.sendMessage(Msgs.Game_Time_Left_Game.getString("<time>", Time.getTime((long) TimeLeft)));
								u.sendMessage(Msgs.Game_Players_Left.getString("<humans>", String.valueOf(getHumans().size()), "<zombies>", String.valueOf(getZombies().size())));
							}
						} else
							for (Player u : getInGame())
								u.sendMessage(Msgs.Game_Time_Left_Game.getString("<time>", Time.getTime((long) TimeLeft)));

					}
					if (TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2)
					{

						for (Player u : getInGame())
						{
							u.playSound(u.getLocation(), Sound.NOTE_STICKS, 1, 1);
							u.playSound(u.getLocation(), Sound.NOTE_BASS_DRUM, 1, 1);
							u.playSound(u.getLocation(), Sound.NOTE_BASS_GUITAR, 1, 1);
						}
					}
					if (TimeLeft == 1)
					{
						for (Player u : getInGame())
						{
							u.playSound(u.getLocation(), Sound.ORB_PICKUP, 1, 5);
							u.playSound(u.getLocation(), Sound.LEVEL_UP, 1, 5);
						}
					} else if (TimeLeft == 0)
						Game.endGame(true);
				}
			}
		}, 0L, 20L);
	}

	public static void loadAllArenas() {
		arenas.clear();
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

	public static Location getLeave() {
		if (Files.getConfig().getString("Leave") == null || Files.getConfig().getString("Leave") == "")
			return null;
		else
			return LocationHandler.getPlayerLocation(Files.getConfig().getString("Leave"));
	}

	public static void setLeave(Location loc) {
		Files.getConfig().set("Leave", LocationHandler.getLocationToString(loc));
		Files.saveConfig();
	}

}
