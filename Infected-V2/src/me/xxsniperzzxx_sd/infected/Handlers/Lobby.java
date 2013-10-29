
package me.xxsniperzzxx_sd.infected.Handlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Handlers.Arena.Arena;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClassManager;
import me.xxsniperzzxx_sd.infected.Handlers.Grenades.GrenadeManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Messages.StringUtil;
import me.xxsniperzzxx_sd.infected.Tools.Files;


public class Lobby {

	//All the InfPlayer Objects
	private ArrayList<InfPlayer> InfPlayers = new ArrayList<InfPlayer>();
	//All the Arena Objects
	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	//All the players playing Infected
	private ArrayList<Player> inGame = new ArrayList<Player>();
	//All the humans 
	private ArrayList<Player> humans = new ArrayList<Player>();
	//All the zombies
	private ArrayList<Player> zombies = new ArrayList<Player>();
	//What ever arena we're playing on
	private Arena activeArena;
	//The games state
	private GameState state = GameState.InLobby;

	public enum GameState {InLobby, Voting, Infecting, Started, Disabled;};
	
	private int voting, beforeInfected, game;

	public Lobby()
	{
		loadArenas();
		InfClassManager.loadConfigClasses();
		GrenadeManager.loadConfigGrenades();
	}

	public void createInfPlayer(Player p){
		InfPlayer IP = new InfPlayer(p);
		InfPlayers.add(IP);
	}
	public void removeInfPlayer(InfPlayer IP){
		InfPlayers.remove(IP);
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

	public boolean isInGame(Player p){
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

	public void delZombie(Player p) {
		zombies.remove(p);
	}

	public void addZombie(Player p) {
		zombies.add(p);
		}

	public ArrayList<Player> getHumans() {
		return humans;
	}

	public ArrayList<InfPlayer> getZombies() {
		return zombies;
	}

	public Arena getActiveArena() {
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

	public void addArena(Arena arena) {
		arenas.add(arena);
	}

	public void addArena(String arenaName) {
		Arena arena = new Arena(this, StringUtil.getWord(arenaName));
		arenas.add(arena);
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
			if (arena.getName().equalsIgnoreCase( StringUtil.getWord(arenaName)))
				arenas.remove(arena);
		}
	}

	public void loadArenas() {
		// TODO: Load arenas from config and create new Arena
	}

	// Check if the arena is avalid
	public boolean isArenaValid(String name) {
		name = StringUtil.getWord(name);
		return !Files.getArenas().getStringList("Arenas." + name + ".Spawns").isEmpty();
	}

	public void resetArena(Arena arena) {

		// Get the arena to fix any broken blocks
		arena.reset();
		// Remove the arena to reset everything
		removeArena(arena);
		// Re-add it in case they feel like playing it again.
		addArena(arena);
	}

	public void stopTimerVote() {
		Bukkit.getScheduler().cancelTask(voting);
	}

	public void stopTimerBeforeInfected() {
		Bukkit.getScheduler().cancelTask(beforeInfected);
	}

	public void stopTimerGame() {
		Bukkit.getScheduler().cancelTask(game);
	}

	public void timerStartVote() {

	}

	public void timerStartBeforeInfected() {

	}

	public void timerStartGame() {

	}

}
