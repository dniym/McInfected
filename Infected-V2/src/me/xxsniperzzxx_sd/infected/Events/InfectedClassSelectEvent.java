
package me.xxsniperzzxx_sd.infected.Events;

import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClass;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedClassSelectEvent extends Event implements Cancellable{

	boolean cancelled = false;
	Player player;
	Team team;
	InfClass InfClass;

	public InfectedClassSelectEvent(Player player, Team team, InfClass Class)
	{
		this.player = player;
		this.team = team;
		this.InfClass = Class;
	}

	public Player getPlayer() {
		return player;
	}
	public InfClass getInfClass(){
		return InfClass;
	}
	public Team getTeam(){
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