
package me.sniperzciinema.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedLeaveEvent extends Event implements Cancellable {

	private Player						p;
	private boolean						b			= false;

	private static final HandlerList	handlers	= new HandlerList();

	public static HandlerList getHandlerList() {
		return InfectedLeaveEvent.handlers;
	}

	public InfectedLeaveEvent(Player p)
	{
		this.p = p;
	}

	@Override
	public HandlerList getHandlers() {
		return InfectedLeaveEvent.handlers;
	}

	/**
	 * @return the player
	 */
	public Player getP() {
		return this.p;
	}

	@Override
	public boolean isCancelled() {
		return this.b;
	}

	@Override
	public void setCancelled(boolean b) {
		this.b = b;
	}

}
