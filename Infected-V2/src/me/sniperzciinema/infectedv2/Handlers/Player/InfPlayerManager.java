
package me.sniperzciinema.infectedv2.Handlers.Player;

import java.util.ArrayList;

import org.bukkit.entity.Player;


public class InfPlayerManager {

	private static ArrayList<InfPlayer> players = new ArrayList<InfPlayer>();

	

	/**
	 * Create InfPlayer
	 * 
	 * @param IP
	 */
	public static void createInfPlayer(InfPlayer IP) {
		if(!players.contains(IP))
			players.add(IP);
	}
	/**
	 * Create InfPlayer
	 * @param Player
	 */
	public static void createInfPlayer(Player p) {
		InfPlayer IP = new InfPlayer(p);
		if(!players.contains(IP))
			players.add(IP);
	}

	/**
	 * Remove InfPlayer
	 * @param Playername
	 */
	public static void removeInfPlayer(String playerName) {
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
	public static void removeInfPlayer(InfPlayer IP) {
		players.remove(IP);
	}
	/**
	 * Get InfPlayer
	 * @param playername
	 */
	public static InfPlayer getInfPlayer(String playerName) {
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
	public static InfPlayer getInfPlayer(Player p) {
		for (InfPlayer IP : players)
		{
			if (IP.getPlayer() == p)
				return IP;
		}
		return null;
	}
}
