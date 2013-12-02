
package me.sniperzciinema.infected.Events;

import me.sniperzciinema.infected.Handlers.Player.InfPlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@SuppressWarnings("unused")
public class InfectedCommandEvent extends Event implements Cancellable{

	
	private boolean cancelled = false;
	private String[] args;
	private Player p;
	private InfPlayer ip;

	public InfectedCommandEvent(String[] args, Player p, InfPlayer ip)
	{
		this.args = args;
		this.p = p;
		this.ip = ip;
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

	/**
	 * @return the ip
	 */
	public InfPlayer getIp() {
		return ip;
	}
	
	
	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @param cancelled the cancelled to set
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


	/**
	 * @param args the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	/**
	 * 
	 * @return args
	 */
	public String[] getArgs(){
		return args;
	}

}