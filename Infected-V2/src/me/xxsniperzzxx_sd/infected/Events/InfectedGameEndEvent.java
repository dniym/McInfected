
package me.xxsniperzzxx_sd.infected.Events;

import java.util.ArrayList;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedGameEndEvent extends Event {

	ArrayList<String> players;
	ArrayList<String> winners;
	boolean humansWin = false;
	
	public InfectedGameEndEvent(ArrayList<String> inGame, ArrayList<String> winners, boolean humansWin)
	{
		this.players = inGame;
		this.winners = winners;
		this.humansWin = humansWin;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}
	public ArrayList<String> getWinners() {
		return winners;
	}
	public boolean didHumansWin() {
		return humansWin;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}