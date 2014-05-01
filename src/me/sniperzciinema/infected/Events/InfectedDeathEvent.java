
package me.sniperzciinema.infected.Events;

import me.sniperzciinema.infected.Enums.DeathType;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedDeathEvent extends Event {
	
	private Player										killer;
	private Player										killed;
	private DeathType									death;
	
	private static final HandlerList	handlers	= new HandlerList();
	
	public static HandlerList getHandlerList() {
		return InfectedDeathEvent.handlers;
	}
	
	public InfectedDeathEvent(Player killer, Player killed, DeathType death)
	{
		this.killer = killer;
		this.killed = killed;
		this.death = death;
	}
	
	/**
	 * @return the death
	 */
	public DeathType getDeath() {
		return this.death;
	}
	
	@Override
	public HandlerList getHandlers() {
		return InfectedDeathEvent.handlers;
	}
	
	/**
	 * Returns before anything is done(So if the killed was a human, getting
	 * their team will return a human)
	 * 
	 * @return the killed
	 */
	public Player getKilled() {
		return this.killed;
	}
	
	/**
	 * @return the killer
	 */
	public Player getKiller() {
		return this.killer;
	}
	
}
