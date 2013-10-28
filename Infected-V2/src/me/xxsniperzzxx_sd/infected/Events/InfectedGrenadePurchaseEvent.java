
package me.xxsniperzzxx_sd.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;


public class InfectedGrenadePurchaseEvent extends Event implements Cancellable{

	boolean cancelled = false;
	Player player;
	ItemStack item;
	int price;

	public InfectedGrenadePurchaseEvent(Player player, ItemStack item, int price)
	{
		this.player = player;
		this.item = item;
		this.price = price;
	}

	public Player getPlayer() {
		return player;
	}
	public int getPrice(){
		return price;
	}
	public ItemStack getItemStack(){
		return item;
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