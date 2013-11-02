
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.entity.Player;


public class MiscStats {

	private static HashMap<String, Integer> Top = new HashMap<String, Integer>();

	public static Double KD(Player player) {
		int kills = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
		int deaths = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
		double ratio = Math.round(((double) kills / (double) deaths) * 100.0D) / 100.0D;
		if (deaths == 0)
			ratio = kills;
		else if (kills == 0)
			ratio = 0.00;
		return ratio;
	}

	public static void setStats(Player player, Integer Kills, Integer Deaths) {
		int kills = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
		int deaths = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
		if (kills == 0)
			kills = 0;
		if (deaths == 0)
			deaths = 0;
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Kills", kills + Kills);
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Deaths", deaths + Deaths);
		Files.savePlayers();
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

	public static String[] getTop5(String stat) {
		String Stat = stat;
		char[] stringArray = Stat.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		Stat = new String(stringArray);
		for (String user : Files.getPlayers().getConfigurationSection("Players").getKeys(true))
		{
			if (!user.contains("."))
			{
				Top.put(user, Files.getPlayers().getInt("Players." + user + "." + Stat));
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
		String[] top = { name1, name2, name3, name4, name5 };
		Top.clear();
		return top;
	}

}
