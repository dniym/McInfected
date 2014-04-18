
package me.sniperzciinema.infected.Events;

import me.sniperzciinema.infected.Handlers.Player.InfPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedCommandEvent extends Event implements Cancellable {

	public static HandlerList getHandlerList() {
		return InfectedCommandEvent.handlers;
	}

	private boolean						cancelled	= false;
	private String[]					args;
	private Player						p;

	private InfPlayer					ip;

	private static final HandlerList	handlers	= new HandlerList();

	public InfectedCommandEvent(String[] args, Player p, InfPlayer ip)
	{
		this.args = args;
		this.p = p;
		this.ip = ip;
	}

	/**
	 * @return args
	 */
	public String[] getArgs() {
		return this.args;
	}

	@Override
	public HandlerList getHandlers() {
		return InfectedCommandEvent.handlers;
	}

	/**
	 * @return the ip
	 */
	public InfPlayer getIp() {
		return this.ip;
	}

	/**
	 * @return the p
	 */
	public Player getP() {
		return this.p;
	}

	/**
	 * @return the cancelled
	 */
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * @param cancelled
	 *            the cancelled to set
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
