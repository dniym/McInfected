
package me.sniperzciinema.infected.GameMechanics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

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
	 * @return A String[] of the <howMany> top players
	 */
	public static String[] topStats(StatType type, Integer howMany) {

		HashMap<String, Integer> players = new HashMap<String, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		String[] top = { " ", " ", " ", " ", " " };

		for (Player p : Bukkit.getOnlinePlayers())
			players.put(p.getName(), Stats.getStat(type, p.getName()));

		if (Settings.MySQLEnabled())
			for (String playerName : MySQLManager.getPlayers("Infected"))
				players.put(playerName, Stats.getStat(type, playerName));

		else
			for (String playerName : Files.getPlayers().getConfigurationSection("Players").getKeys(false))
			{
				players.put(playerName, Stats.getStat(type, playerName));

			}
		int place = 0;
		while (place != howMany)
		{
			// If the list still has players in it, find the top player

			try
			{
				top[place] = getHighest(players);

				players.remove(top[place]);
			} catch (NoSuchElementException ne)
			{

			}
			place++;
		}

		return top;
	}
}
