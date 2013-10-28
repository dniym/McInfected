
package me.xxsniperzzxx_sd.infected.Handlers.Player;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class InfPlayerManager {

	Plugin plugin;
	private static ArrayList<InfPlayer> players = new ArrayList<InfPlayer>();

	public InfPlayerManager(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public static void addPlayer(Player player) {
		InfPlayer IP = new InfPlayer(player);
		players.add(IP);
	}

	public static void addPlayer(InfPlayer IP) {
		players.add(IP);
	}

	public static InfPlayer getPlayer(String playerName) {
		for (InfPlayer IP : players)
		{
			if (IP.getName().equalsIgnoreCase(playerName))
				return IP;
		}
		return null;
	}

	public static InfPlayer getPlayer(Player p) {
		for (InfPlayer IP : players)
		{
			if (IP.getPlayer() == p)
				return IP;
		}
		return null;
	}

	public static void removeInfPlayer(InfPlayer IP) {
		players.remove(IP);
	}

	public static void removeInfPlayer(String playerName) {
		for (InfPlayer player : players)
		{
			if (player.getName().equalsIgnoreCase(playerName))
				players.remove(player);
		}
	}
}
