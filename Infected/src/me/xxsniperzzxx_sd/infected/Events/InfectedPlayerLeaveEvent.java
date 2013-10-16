
package me.xxsniperzzxx_sd.infected.Events;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Enums.Teams;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedPlayerLeaveEvent extends Event implements Cancellable{

	boolean cancelled = false;
	
	Player player;
	ArrayList<String> playersInLobby;
	Teams team;
	boolean command = false;

	public InfectedPlayerLeaveEvent(Player player, ArrayList<String> inLobby, Teams team, boolean command)
	{
		this.command = command;
		this.player = player;
		this.playersInLobby = inLobby;
		this.team = team;
	}

	public Player getPlayer() {
		return player;
	}
	public boolean isCommand() {
		return command;
	}

	public Teams getTeam() {
		return Infected.playerGetGroup(player);
	}
	
	public ArrayList<String> getPlayersInLobby() {
		return playersInLobby;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	public boolean isCancelled() {
		return cancelled;
	}

}