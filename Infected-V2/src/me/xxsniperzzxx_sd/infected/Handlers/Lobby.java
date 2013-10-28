
package me.xxsniperzzxx_sd.infected.Handlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Arena.Arena;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClassManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;


public class Lobby {

	private ArrayList<Arena> arenas = new ArrayList<Arena>();
	private ArrayList<InfPlayer> players = new ArrayList<InfPlayer>();
	private ArrayList<InfPlayer> humans = new ArrayList<InfPlayer>();
	private ArrayList<InfPlayer> zombies = new ArrayList<InfPlayer>();
	private Arena activeArena;
	private GameState state = GameState.INLOBBY;

	private int voting, beforeInfected, game;

	public Lobby()
	{
		InfClassManager.loadConfigClasses();
		loadArenas();
	}

	public void addPlayer(InfPlayer IP) {
		players.add(IP);
	}

	public void delPlayer(InfPlayer IP) {
		players.remove(IP);
	}

	public ArrayList<InfPlayer> getPlayers() {
		return players;
	}

	public void addHuman(InfPlayer IP) {
		humans.add(IP);
	}

	public void delHuman(InfPlayer IP) {
		humans.remove(IP);
	}

	public void delZombie(InfPlayer IP) {
		zombies.remove(IP);
	}

	public void addZombie(InfPlayer IP) {
		zombies.add(IP);
	}

	public ArrayList<InfPlayer> getHumans() {
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
		Arena arena = new Arena(this, arenaName);
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
			if (arena.getName().equalsIgnoreCase(arenaName))
				arenas.remove(arena);
		}
	}

	public void loadArenas() {
		// TODO: Load arenas from config and create new Arena
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
