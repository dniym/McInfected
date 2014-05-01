
package me.sniperzciinema.infected.Events;

import java.util.ArrayList;

import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedStartGame extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return InfectedStartGame.handlers;
	}
	
	public InfectedStartGame()
	{
	}
	
	/**
	 * @return active arena
	 */
	public Arena getArena() {
		return Lobby.getActiveArena();
	}
	
	@Override
	public HandlerList getHandlers() {
		return InfectedStartGame.handlers;
	}
	
	/**
	 * @return the Humans
	 */
	public ArrayList<Player> getHumans() {
		return Lobby.getHumans();
	}
	
	/**
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return Lobby.getPlayersInGame();
	}
	
	/**
	 * @return the infecting time
	 */
	public int getTimeLimit() {
		return Lobby.getActiveArena().getSettings().getGameTime();
	}
	
	/**
	 * @return the zombies
	 */
	public ArrayList<Player> getZombies() {
		return Lobby.getZombies();
	}
	
}
