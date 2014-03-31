
package me.sniperzciinema.infected.Events;

import java.util.ArrayList;

import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedStartGame extends Event {

	public InfectedStartGame()
	{
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the players
	 */
	public ArrayList<String> getPlayers() {
		return Lobby.getInGame();
	}

	/**
	 * @return the zombies
	 */
	public ArrayList<String> getZombies() {
		return Lobby.getZombies();
	}

	/**
	 * @return the Humans
	 */
	public ArrayList<String> getHumans() {
		return Lobby.getHumans();
	}

	/**
	 * @return the infecting time
	 */
	public int getTimeLimit() {
		return Lobby.getActiveArena().getSettings().getGameTime();
	}

	/**
	 * @return active arena
	 */
	public Arena getArena() {
		return Lobby.getActiveArena();
	}

}