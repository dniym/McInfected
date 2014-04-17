
package me.sniperzciinema.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedLeaveEvent extends Event implements Cancellable {

	private Player	p;
	private boolean	b	= false;

	public InfectedLeaveEvent(Player p)
	{
		this.p = p;
	}

	private static final HandlerList	handlers	= new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	/**
	 * @return the player
	 */
	public Player getP() {
		return p;
	}

	@Override
	public boolean isCancelled() {
		return b;
	}

	@Override
	public void setCancelled(boolean b) {
		this.b = b;
	}

}
