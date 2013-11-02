
package me.xxsniperzzxx_sd.infected.Handlers;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.Game;
import me.xxsniperzzxx_sd.infected.Handlers.Arena.Arena;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClassManager;
import me.xxsniperzzxx_sd.infected.Handlers.Grenades.GrenadeManager;
import me.xxsniperzzxx_sd.infected.Handlers.Misc.LocationHandler;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Messages.Msgs;
import me.xxsniperzzxx_sd.infected.Messages.StringUtil;
import me.xxsniperzzxx_sd.infected.Messages.Time;
import me.xxsniperzzxx_sd.infected.Tools.Files;


public class Lobby {

	// All the Arena Objects
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	// All the players playing Infected
	private ArrayList<Player> inGame = new ArrayList<Player>();
	// All the humans
	private ArrayList<Player> humans = new ArrayList<Player>();
	// All the zombies
	private ArrayList<Player> zombies = new ArrayList<Player>();
	// What ever arena we're playing on
	private Arena activeArena;
	// The games state
	private GameState state = GameState.InLobby;

	private int VotingTime;
	private int InfectingTime;
	private int GameTime;
	private int TimeLeft;

	private int currentGameTimer;

	public enum GameState
	{
		InLobby, Voting, Infecting, Started, Disabled;
	};

	public Lobby()
	{
		loadArenas();
		InfClassManager.loadConfigClasses();
		GrenadeManager.loadConfigGrenades();
	}

	public Location getLocation() {
		return LocationHandler.getPlayerLocation(Main.config.getString("Lobby"));
	}

	public void setLocation(Location loc) {
		Files.getConfig().set("Lobby", LocationHandler.getLocationToString(loc));
	}

	public ArrayList<Arena> getArenas() {
		return arenas;
	}

	public boolean isInGame(Player p) {
		return inGame.contains(p);
	}

	public void addPlayerInGame(Player p) {
		inGame.add(p);
	}

	public void delPlayerInGame(Player p) {
		inGame.remove(p);
	}

	public ArrayList<Player> getInGame() {
		return inGame;
	}

	public void addHuman(Player p) {
		humans.add(p);
	}

	public void delHuman(Player p) {
		humans.remove(p);
	}

	public boolean isHuman(Player p) {
		return humans.contains(p);
	}

	public void delZombie(Player p) {
		zombies.remove(p);
	}

	public void addZombie(Player p) {
		zombies.add(p);
	}

	public boolean isZombie(Player p) {
		return zombies.contains(p);
	}

	public boolean oppositeTeams(Player p, Player u) {
		return !(isZombie(p) && isZombie(u)) || (isHuman(p) && isHuman(u));
	}

	public ArrayList<Player> getHumans() {
		return humans;
	}

	public ArrayList<Player> getZombies() {
		return zombies;
	}

	public Arena getActiveArena() {
		if (activeArena == null)
		{
			setActiveArena(addArena("Not a real Arena"));
		}
		return activeArena;
	}

	public void setActiveArena(Arena arena) {
		activeArena = arena;
	}

	public void setGameState(GameState state) {
		this.state = state;
	}

	public GameState getGameState() {
		return state;
	}

	public Arena addArena(Arena arena) {
		arenas.add(arena);
		return arena;
	}

	public Arena addArena(String arenaName) {
		Arena arena = new Arena(this, StringUtil.getWord(arenaName));
		arenas.add(arena);
		return arena;
	}

	public Arena getArena(String arenaName) {
		for (Arena arena : arenas)
		{
			if (arena.getName().equalsIgnoreCase(arenaName))
				return arena;
		}
		return null;
	}

	public void removeArena(Arena arena) {
		arenas.remove(arena);
	}

	public void removeArena(String arenaName) {
		for (Arena arena : arenas)
		{
			if (arena.getName().equalsIgnoreCase(StringUtil.getWord(arenaName)))
				arenas.remove(arena);
		}
	}

	public void loadArenas() {
		if (Files.getArenas().getConfigurationSection("Arenas") != null)
			for (String s : Files.getArenas().getConfigurationSection("Arenas").getKeys(false))
			{
				Arena arena = new Arena(this, StringUtil.getWord(s));
				addArena(arena);
				System.out.println("Loaded Arena: " + arena.getName());
			}
	}

	// Check if the arena is avalid
	public boolean isArenaValid(String name) {
		name = StringUtil.getWord(name);
		return !Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty();
	}

	// Check if the arena is avalid
	public boolean isArenaValid(Arena arena) {
		return !arena.getSpawns().isEmpty();
	}

	public void resetArena(Arena arena) {

		// Get the arena to fix any broken blocks
		arena.reset();
		// Remove the arena to reset everything
		removeArena(arena);
		// Re-add it in case they feel like playing it again.
		addArena(arena);
	}

	/**
	 * @return the currentTime
	 */
	public int getTimeLeft() {
		return TimeLeft;
	}

	/**
	 * @param currentTime
	 *            the currentTime to set
	 */
	public void setTimeLeft(int TimeLeft) {
		this.TimeLeft = TimeLeft;
	}

	/**
	 * @return the currentGameTimer
	 */
	public int getCurrentGameTimer() {
		return currentGameTimer;
	}

	/**
	 * @param currentGameTimer
	 *            the currentGameTimer to set
	 */
	public void setCurrentGameTimer(int currentGameTimer) {
		this.currentGameTimer = currentGameTimer;
	}

	public void stopTimer() {
		Bukkit.getScheduler().cancelTask(currentGameTimer);
	}

	public void timerStartVote() {
		stopTimer();
		VotingTime = getActiveArena().getSettings().getVotingTime();
		TimeLeft = VotingTime;

		setGameState(GameState.Voting);

		ScoreBoard.updateScoreBoard();

		for (Player u : getInGame())
		{
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
			u.sendMessage(Msgs.Game_Time_Left.getString("<time>", Time.getTime((long) getTimeLeft())));
			u.sendMessage("How to vote");
			u.sendMessage("");
			u.sendMessage(Msgs.Format_Line.getString());
		}

		currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
		{

			@Override
			public void run() {
				if (getTimeLeft() != 0)
				{
					// TODO: Re-add: When all players vote, time gets cut in
					// half

					TimeLeft -= 1;

					for (Player u : getInGame())
						u.setLevel(getTimeLeft());

					if (TimeLeft == 60 || TimeLeft == 50 || TimeLeft == 40 || TimeLeft == 30 || TimeLeft == 20 || TimeLeft == 10 || TimeLeft == 9 || TimeLeft == 8 || TimeLeft == 7 || TimeLeft == 6 || TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2 || TimeLeft == 1)
					{
						for (Player u : getInGame())
							u.sendMessage(Msgs.Game_Time_Left.getString("<time>", Time.getTime((long) getTimeLeft())));
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

						ScoreBoard.updateScoreBoard();
						for (Player u : getInGame())
						{
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
							u.sendMessage(Main.I + "Game Starting in 5 Seconds.");
							u.sendMessage("");
							u.sendMessage("Map - <MapName>");
							u.sendMessage("");
							u.sendMessage(Msgs.Format_Line.getString());
						}
						currentGameTimer = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{

							@Override
							public void run() {

								for (Player u : getInGame())
								{
									u.setGameMode(GameMode.SURVIVAL);
									InfPlayer IP = InfPlayerManager.getInfPlayer(u);
									IP.respawn();
									Equip.equip(u);
									u.sendMessage("Infection going viral in : <time>");

								}
								ScoreBoard.updateScoreBoard();
								timerStartInfecting();
							}
						}, 100L);
					}
				}
			}
		}, 0L, 20L);
	}

	public void timerStartInfecting() {

		stopTimer();
		setGameState(GameState.Infecting);
		InfectingTime = getActiveArena().getSettings().getInfectingTime();
		TimeLeft = InfectingTime;
		currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
		{

			@Override
			public void run() {
				if (TimeLeft != 0)
				{
					TimeLeft -= 1;
					for (Player u : getInGame())
						u.setLevel(TimeLeft);

					if (TimeLeft == 60 || TimeLeft == 50 || TimeLeft == 40 || TimeLeft == 30 || TimeLeft == 20 || TimeLeft == 10 || TimeLeft == 9 || TimeLeft == 8 || TimeLeft == 7 || TimeLeft == 6 || TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2 || TimeLeft == 1)
					{
						for (Player u : getInGame())
							u.sendMessage("Infection in: <time>");
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
							InfPlayerManager.getInfPlayer(u).setTimeIn(System.currentTimeMillis()/1000);
						
						timerStartGame();
					}
				}
			}
		}, 0L, 20L);

	}

	public void timerStartGame() {
		stopTimer();
		setGameState(GameState.Started);
		GameTime = getActiveArena().getSettings().getGameTime();
		TimeLeft = GameTime;

		ScoreBoard.updateScoreBoard();
		currentGameTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
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

					if (TimeLeft == (Main.GtimeLimit / 4) * 3 || TimeLeft == Main.GtimeLimit / 2 || TimeLeft == Main.GtimeLimit / 4 || TimeLeft == 60 || TimeLeft == 10 || TimeLeft == 9 || TimeLeft == 8 || TimeLeft == 7 || TimeLeft == 6 || TimeLeft == 5 || TimeLeft == 4 || TimeLeft == 3 || TimeLeft == 2)
					{
						for (Player u : getInGame())
						{
							if (TimeLeft > 61)
							{
								u.sendMessage(Msgs.Game_Time_Left.getString("<time>", Time.getTime((long) TimeLeft)));
								u.sendMessage("humans left: <humans>|| Zombies left: <zombies>");
							} else
								u.sendMessage(Msgs.Game_Time_Left.getString("<time>", Time.getTime((long) TimeLeft)));
						}

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

}
