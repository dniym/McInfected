
package me.xxsniperzzxx_sd.infected.Handlers.Player;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class InfPlayerManager {

	Plugin plugin;
	private ArrayList<InfPlayer> players = new ArrayList<InfPlayer>();

	public InfPlayerManager(Plugin plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * Create InfPlayer
	 * 
	 * @param IP
	 */
	public void createInfPlayer(InfPlayer IP) {
		players.add(IP);
	}
	/**
	 * Create InfPlayer
	 * @param Player
	 */
	public void createInfPlayer(Player p) {
		InfPlayer IP = new InfPlayer(p);
		players.add(IP);
	}

	/**
	 * Remove InfPlayer
	 * @param Playername
	 */
	public void removeInfPlayer(String playerName) {
		for (InfPlayer player : players)
		{
			if (player.getName().equalsIgnoreCase(playerName))
				players.remove(player);
		}
	}
	/**
	 * Remove InfPlayer
	 * @param IP
	 */
	public void removeInfPlayer(InfPlayer IP) {
		players.remove(IP);
	}
	/**
	 * Get InfPlayer
	 * @param playername
	 */
	public InfPlayer getInfPlayer(String playerName) {
		for (InfPlayer IP : players)
		{
			if (IP.getName().equalsIgnoreCase(playerName))
				return IP;
		}
		return null;
	}
	/**
	 * Create InfPlayer
	 * @param Player
	 */
	public InfPlayer getInfPlayer(Player p) {
		for (InfPlayer IP : players)
		{
			if (IP.getPlayer() == p)
				return IP;
		}
		return null;
	}
}
