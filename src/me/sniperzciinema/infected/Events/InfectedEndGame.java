
package me.sniperzciinema.infected.Events;

import java.util.ArrayList;

import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedEndGame extends Event {

	private boolean	didHumansWin;

	public InfectedEndGame(boolean didHumansWin)
	{
		this.didHumansWin = didHumansWin;
	}

	private static final HandlerList	handlers	= new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return Lobby.getPlayersInGame();
	}

	/**
	 * @return the zombies
	 */
	public ArrayList<Player> getZombies() {
		return Lobby.getZombies();
	}

	/**
	 * @return the Humans
	 */
	public ArrayList<Player> getHumans() {
		return Lobby.getHumans();
	}

	public boolean didHumansWin() {
		return didHumansWin;
	}

	/**
	 * @return active arena
	 */
	public Arena getArena() {
		return Lobby.getActiveArena();
	}

}
