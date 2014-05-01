
package me.sniperzciinema.infected.Events;

import java.util.ArrayList;

import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedEndGame extends Event {
	
	private boolean										didHumansWin;
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return InfectedEndGame.handlers;
	}
	
	public InfectedEndGame(boolean didHumansWin)
	{
		this.didHumansWin = didHumansWin;
	}
	
	public boolean didHumansWin() {
		return this.didHumansWin;
	}
	
	/**
	 * @return active arena
	 */
	public Arena getArena() {
		return Lobby.getActiveArena();
	}
	
	@Override
	public HandlerList getHandlers() {
		return InfectedEndGame.handlers;
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
	 * @return the zombies
	 */
	public ArrayList<Player> getZombies() {
		return Lobby.getZombies();
	}
	
}
