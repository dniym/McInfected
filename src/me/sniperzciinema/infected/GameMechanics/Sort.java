
package me.sniperzciinema.infected.GameMechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import me.sniperzciinema.infected.GameMechanics.Stats.StatType;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.MySQLManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Sort {

	/**
	 * Take the hashmap and from it get the highest value's key
	 * 
	 * @param map
	 *            - The hashmap in questioning
	 * @return The highest value's key
	 */
	private static String getHighest(HashMap<String, Integer> map) {
		String highest = null;

		int top = (Collections.max(map.values()));

		for (Entry<String, Integer> e : map.entrySet())
			if (e.getValue() == top)
			{
				highest = e.getKey();
			}

		return highest;
	}

	/**
	 * Loops the the values of that stat and returns the top players
	 * 
	 * @param type
	 *            - The type of stat
	 * @param howMany
	 *            - How many players to return
	 * @return A ArrayList<String> of the <howMany> top players
	 */
	public static ArrayList<String> topStats(StatType type, Integer howMany) {

		HashMap<String, Integer> players = new HashMap<String, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		ArrayList<String> top = new ArrayList<String>();

		for (Player p : Bukkit.getOnlinePlayers())
			players.put(p.getName().toLowerCase(), Stats.getStat(type, p.getName()));

		if (Settings.MySQLEnabled())
		{
			for (String playerName : MySQLManager.getPlayers("Infected"))
				if (!players.containsKey(playerName.toLowerCase()))
					players.put(playerName, Stats.getStat(type, playerName));
		} else
		{
			if (!Files.getPlayers().getString("Players").isEmpty())
				for (String playerName : Files.getPlayers().getConfigurationSection("Players").getKeys(false))
					if (!players.containsKey(playerName.toLowerCase()))
						players.put(playerName, Stats.getStat(type, playerName));
		}
		for (int place = 0; place != howMany; place++)
		{
			// If the list still has players in it, find the top player
			top.add(getHighest(players));
			players.remove(top.get(place));
		}

		return top;
	}
}
