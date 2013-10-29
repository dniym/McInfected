package me.xxsniperzzxx_sd.infected.GameMechanics.OldStats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class MiscStats {


	private static HashMap<String, Integer> Stats = new HashMap<String, Integer>();

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
				Stats.put(user, Files.getPlayers().getInt("Players." + user + "." + Stat));
			}
		}
		if (Stats.size() < 6)
		{
			Stats.put(" ", 0);
			Stats.put("  ", 0);
			Stats.put("   ", 0);
			Stats.put("    ", 0);
			Stats.put("     ", 0);
		}
		String name1 = countdown(Stats);
		Stats.remove(name1);
		String name2 = countdown(Stats);
		Stats.remove(name2);
		String name3 = countdown(Stats);
		Stats.remove(name3);
		String name4 = countdown(Stats);
		Stats.remove(name4);
		String name5 = countdown(Stats);
		Stats.remove(name5);
		String[] top = { name1, name2, name3, name4, name5 };
		Stats.clear();
		return top;
	}
	

	public static void handleKillStreaks(boolean killed, Player player) {
		if (killed)
		{
			if (!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), 0);

			if (Main.KillStreaks.get(player.getName()) > Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".KillStreak"))
			{
				Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(player.getName()));
				Files.savePlayers();
			}

			Main.KillStreaks.put(player.getName(), 0);
		} else
		{
			if (!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), 0);
			Main.KillStreaks.put(player.getName(), Main.KillStreaks.get(player.getName()) + 1);

			if (Main.KillStreaks.get(player.getName()) >= 3)
				for (Player playing : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(playing.getName()))
						playing.sendMessage(Main.I + (Infected.isPlayerHuman(player) ? ChatColor.RED + player.getName() : ChatColor.GREEN + player.getName()) + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + Main.KillStreaks.get(player.getName()));

			if ((Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(Main.KillStreaks.get(player.getName())))))
			{
				String command = null;
				command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + Main.KillStreaks.get(player.getName()))).replaceAll("<player>", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			if (Infected.isPlayerHuman(player))
				PointsAndScores.rewardPointsAndScore(player, "Kill");

			else
			{
				PointsAndScores.rewardPointsAndScore(player, "Kill");

				for (String playing : Infected.listInGame())
				{
					if (Infected.isPlayerHuman(Bukkit.getPlayer(playing)))
						PointsAndScores.rewardPointsAndScore(Bukkit.getPlayer(playing), "Survive");

					else if (Infected.isPlayerZombie(Bukkit.getPlayer(playing)))
						PointsAndScores.rewardPointsAndScore(Bukkit.getPlayer(playing), "Zombies Infected");
				}

			}

		}
	}

}
