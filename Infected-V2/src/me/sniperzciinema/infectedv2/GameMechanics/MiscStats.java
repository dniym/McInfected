
package me.sniperzciinema.infectedv2.GameMechanics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import me.sniperzciinema.infectedv2.GameMechanics.Stats.StatType;
import me.sniperzciinema.infectedv2.Tools.Files;


public class MiscStats {

	private static HashMap<String, Integer> Top = new HashMap<String, Integer>();

	public static Double KD(String user) {
		int kills = Stats.getKills(user);
		int deaths = Stats.getDeaths(user);
		double ratio = Math.round(((double) kills / (double) deaths) * 100.0D) / 100.0D;
		if (deaths == 0)
			ratio = kills;
		else if (kills == 0)
			ratio = 0.00;
		return ratio;
	}

	public static String countdown(HashMap<String, Integer> map) {
		String top = null;
		int maxValueInMap = (Collections.max(map.values())); // This will return
																// max value in
																// the Hashmap
		for (Entry<String, Integer> entry : map.entrySet())
		{ // Itrate through hashmap
			if (entry.getValue() == maxValueInMap)
			{ // Print the key with max value
				top = entry.getKey();
			}
		}

		return top;
	}

	public static HashMap<String, Integer> getTop5(StatType type) {
		
		for (String user : Files.getPlayers().getConfigurationSection("Players").getKeys(true))
		{
			if (!user.contains("."))
			{
				Top.put(user, Stats.getStat(type, user));
			}
		}
		if (Top.size() < 6)
		{
			Top.put(" ", 0);
			Top.put("  ", 0);
			Top.put("   ", 0);
			Top.put("    ", 0);
			Top.put("     ", 0);
		}
		String name1 = countdown(Top);
		Top.remove(name1);
		String name2 = countdown(Top);
		Top.remove(name2);
		String name3 = countdown(Top);
		Top.remove(name3);
		String name4 = countdown(Top);
		Top.remove(name4);
		String name5 = countdown(Top);
		Top.remove(name5);
		
		return Top;
	}

}
