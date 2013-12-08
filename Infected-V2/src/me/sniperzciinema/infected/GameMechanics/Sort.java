
package me.sniperzciinema.infected.GameMechanics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import me.sniperzciinema.infected.GameMechanics.Stats.StatType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;



public class Sort {

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

	public static String[] topStats(StatType type, Integer howMany) {

		HashMap<String, Integer> players = new HashMap<String, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		String[] top = { " ", " ", " ", " ", " " };
		for (Player p : Bukkit.getOnlinePlayers())
			players.put(p.getName(), Stats.getStat(type, p.getName()));

		int place = 0;
		while (place != howMany - 1)
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
