
package me.sniperzciinema.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedJoinEvent extends Event {
	
	private Player										p;
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return InfectedJoinEvent.handlers;
	}
	
	public InfectedJoinEvent(Player p)
	{
		this.p = p;
	}
	
	@Override
	public HandlerList getHandlers() {
		return InfectedJoinEvent.handlers;
	}
	
	/**
	 * @return the p
	 */
	public Player getP() {
		return this.p;
	}
	
}
