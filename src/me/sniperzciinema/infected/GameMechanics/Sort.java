
package me.sniperzciinema.infected.GameMechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.UUID;

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
	private static UUID getHighest(HashMap<UUID, Integer> map) {
		UUID highest = null;

		if (!map.isEmpty())
		{
			int top = (Collections.max(map.values()));

			for (Entry<UUID, Integer> e : map.entrySet())
				if (e.getValue() == top)
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
	public static ArrayList<UUID> topStats(StatType type, Integer howMany) {

		HashMap<UUID, Integer> uuids = new HashMap<UUID, Integer>();
		// Get all the players and put them and their score in a new hashmap for
		// Player, Score
		ArrayList<UUID> top = new ArrayList<UUID>();

		for (Player p : Bukkit.getOnlinePlayers())
			uuids.put(p.getUniqueId(), Stats.getStat(type, p.getUniqueId()));

		if (Settings.MySQLEnabled())
		{
			for (UUID uuid : MySQLManager.getPlayers("Infected"))
				if (!uuids.containsKey(uuid.toString()))
					uuids.put(uuid, Stats.getStat(type, uuid));
		}
		else
			if (!Files.getPlayers().getString("Players").isEmpty())
				for (String uuidString : Files.getPlayers().getConfigurationSection("Players").getKeys(false))
					try
					{

						UUID uuid = UUID.fromString(uuidString);
						if (!uuids.containsKey(uuid))
							uuids.put(uuid, Stats.getStat(type, uuid));
					}
					catch (IllegalArgumentException e)
					{
					}
		for (int place = 0; place != howMany; place++)
			// If the list still has players in it, find the top player
			try
			{
				top.add(getHighest(uuids));
				uuids.remove(top.get(place));
			}
			catch (NoSuchElementException e)
			{
				break;
			}
		return top;
	}
}
