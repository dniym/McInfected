
package me.xxsniperzzxx_sd.infected.Events;

import me.xxsniperzzxx_sd.infected.Enums.DeathTypes;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedPlayerDieEvent extends Event implements Cancellable{

	boolean cancelled = false;
	
	Player killer;
	Player killed;
	boolean becomeInfected;
	DeathTypes death;
	String deathMsg;

	public InfectedPlayerDieEvent(Player killer, Player killed, boolean becameInfected, DeathTypes death, String deathMsg)
	{
		this.killer = killer;
		this.killed = killed;
		this.becomeInfected = becameInfected;
		this.death = death;
		this.deathMsg = deathMsg;
	}

	public Player getKilled() {
		return this.killed;
	}

	public Player getKiller() {
		return this.killer;
	}

	public boolean didKilledBecameInfected() {
		return becomeInfected;
	}
	
	public DeathTypes getDeathType(){
		return death;
	}
	
	public String getDeathMsg(){
		return deathMsg;
	}

	public void setDeathMsg(String msg){
		deathMsg = msg;
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