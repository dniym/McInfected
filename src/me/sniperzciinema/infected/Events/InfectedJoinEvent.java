
package me.sniperzciinema.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedJoinEvent extends Event {

	private Player p;

	public InfectedJoinEvent(Player p)
	{
		this.p = p;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the p
	 */
	public Player getP() {
		return p;
	}

}