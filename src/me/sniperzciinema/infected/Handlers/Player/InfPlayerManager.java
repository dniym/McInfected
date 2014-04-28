
package me.sniperzciinema.infected.Handlers.Player;

import java.util.ArrayList;
import java.util.UUID;

import me.sniperzciinema.infected.Handlers.UUID.UUIDManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class InfPlayerManager {

	private static ArrayList<InfPlayer>	players	= new ArrayList<InfPlayer>();

	/**
	 * Create InfPlayer
	 * 
	 * @param Player
	 * @return The new InfPlayer
	 */
	public static InfPlayer createInfPlayer(InfPlayer IP) {
		if (!InfPlayerManager.players.contains(IP))
			InfPlayerManager.players.add(IP);
		return IP;
	}

	/**
	 * Create InfPlayer
	 * 
	 * @param Player
	 * @return The new InfPlayer
	 */
	public static InfPlayer createInfPlayer(Player p) {
		InfPlayer IP = new InfPlayer(p);
		if (!InfPlayerManager.players.contains(IP))
			InfPlayerManager.players.add(IP);
		return IP;
	}

	/**
	 * Create InfPlayer
	 * 
	 * @param Player
	 */
	public static InfPlayer getInfPlayer(Player p) {
		for (InfPlayer IP : InfPlayerManager.players)
			if (IP.getPlayer() == p)
				return IP;
		return createInfPlayer(p);
	}

	/**
	 * Get InfPlayer
	 * 
	 * @param playername
	 */
	@SuppressWarnings("deprecation")
	public static InfPlayer getInfPlayer(String playerName) {
		for (InfPlayer IP : InfPlayerManager.players)
			if (IP.getName().equalsIgnoreCase(playerName))
				return IP;
		return createInfPlayer(Bukkit.getPlayer(playerName));
	}

	/**
	 * Get InfPlayer
	 * 
	 * @param uuid
	 */
	@SuppressWarnings("deprecation")
	public static InfPlayer getInfPlayer(UUID id) {
		for (InfPlayer IP : InfPlayerManager.players)
			if (IP.getUuid() == id)
				return IP;
		return createInfPlayer(Bukkit.getPlayer(UUIDManager.getPlayerName(id)));
	}

	/**
	 * Remove InfPlayer
	 * 
	 * @param IP
	 */
	public static void removeInfPlayer(InfPlayer IP) {
		InfPlayerManager.players.remove(IP);
	}

	/**
	 * Remove InfPlayer
	 * 
	 * @param Playername
	 */
	public static void removeInfPlayer(String playerName) {
		for (InfPlayer player : InfPlayerManager.players)
			if (player.getName().equalsIgnoreCase(playerName))
				InfPlayerManager.players.remove(player);
	}

	public InfPlayerManager()
	{
	}
}
