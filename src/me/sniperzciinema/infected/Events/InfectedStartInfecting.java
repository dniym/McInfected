
package me.sniperzciinema.infected.Events;

import java.util.ArrayList;

import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedStartInfecting extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return InfectedStartInfecting.handlers;
	}
	
	/**
	 * @return active arena
	 */
	public Arena getArena() {
		return Lobby.getActiveArena();
	}
	
	@Override
	public HandlerList getHandlers() {
		return InfectedStartInfecting.handlers;
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
		return Lobby.getActiveArena().getSettings().getInfectingTime();
	}
	
}
