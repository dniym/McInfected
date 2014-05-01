
package me.sniperzciinema.infected.Events;

import java.util.ArrayList;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedStartVote extends Event {
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return InfectedStartVote.handlers;
	}
	
	/**
	 * @return all arenas
	 */
	public ArrayList<Arena> getArenas() {
		return Lobby.getArenas();
	}
	
	@Override
	public HandlerList getHandlers() {
		return InfectedStartVote.handlers;
	}
	
	/**
	 * @return the players
	 */
	public ArrayList<Player> getPlayers() {
		return Lobby.getPlayersInGame();
	}
	
	/**
	 * @return the voting time
	 */
	public int getTimeLimit() {
		return Settings.getVotingTime();
	}
	
}
