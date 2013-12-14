
package me.sniperzciinema.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedDeathEvent extends Event {

	private Player killer;
	private Player killed;

	public InfectedDeathEvent(Player killer, Player killed)
	{
		this.killer = killer;
		this.killed = killed;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the killer
	 */
	public Player getKiller() {
		return killer;
	}

	/**
	 * Returns before anything is done(So if the killed was a human, getting
	 * their team will return a human)
	 * 
	 * @return the killed
	 */
	public Player getKilled() {
		return killed;
	}

}