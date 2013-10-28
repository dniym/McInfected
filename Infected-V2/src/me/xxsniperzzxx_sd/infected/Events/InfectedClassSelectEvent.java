
package me.xxsniperzzxx_sd.infected.Events;

import me.xxsniperzzxx_sd.infected.Enums.Teams;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedClassSelectEvent extends Event implements Cancellable{

	boolean cancelled = false;
	Player player;
	Teams team;
	String classname;

	public InfectedClassSelectEvent(Player player, Teams team, String classname)
	{
		this.player = player;
		this.team = team;
		this.classname = classname;
	}

	public Player getPlayer() {
		return player;
	}
	public String getClassName(){
		return classname;
	}
	public Teams getTeam(){
		return team;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}