
package me.xxsniperzzxx_sd.infected.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedGrenadeDeathEvent extends Event {

	Player player;
	Player killer;
	int grenade;

	public InfectedGrenadeDeathEvent(Player player, Player killer, int grenade)
	{
		this.player = player;
		this.grenade = grenade;
		this.killer = killer;
	}

	public Player getPlayer() {
		return player;
	}

	public Player getKiller() {
		return killer;
	}

	public int getGrenadeID() {
		return grenade;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}