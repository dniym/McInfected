
package me.xxsniperzzxx_sd.infected.Events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InfectedPlayerVoteEvent extends Event implements Cancellable{

	boolean cancelled = false;
	Player player;
	String vote;
	HashMap<String, Integer> votes;
	HashMap<String, String> voted4;
	int voteCount;

	public InfectedPlayerVoteEvent(Player player, String vote, HashMap<String, Integer> votes, HashMap<String, String> voted4, int voteCount)
	{
		this.player = player;
		this.vote = vote;
		this.votes = votes;
		this.voted4 = voted4;
		this.voteCount = voteCount;
	}

	public Player getPlayer() {
		return player;
	}
	public String getVote(){
		return vote;
	}
	public HashMap<String, String> getAllPlayerVotes(){
		return voted4;
	}
	public HashMap<String, Integer> getmapVotes(){
		return votes;
	}
	public int getPlayersVoteCount(){
		return voteCount;
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