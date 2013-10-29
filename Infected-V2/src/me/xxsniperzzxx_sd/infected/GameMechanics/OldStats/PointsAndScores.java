package me.xxsniperzzxx_sd.infected.GameMechanics.OldStats;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class PointsAndScores {

	public static void rewardPointsAndScore(Player player, String PointsCause) {
		if (Main.config.getBoolean("Points.Use"))
		{
			if (Main.config.getBoolean("Debug"))
				System.out.print(Main.KillStreaks.toString());
			if (Main.KillStreaks.containsKey(player.getName()))
			{
				if (!(Main.KillStreaks.get(player.getName()) == 0))
				{
					int times = Main.KillStreaks.get(player.getName()) / 2;
					Main.timest = times;
				} else
				{
					int times = 1;
					Main.timest = times;
				}
			} else
				Main.timest = 1;
			int score = Main.config.getInt("Score." + PointsCause) * Main.timest;
			int reward = Main.config.getInt("Points." + PointsCause);

			reward = Main.config.getInt("Points." + PointsCause);
			if (Infected.playerGetPoints(player.getName()) > Main.config.getInt("Points.Max Points"))
				player.sendMessage(Main.I + ChatColor.RED + "You have exceded the max points!");
			if (Infected.playerGetScore(player.getName()) > Main.config.getInt("Score.Max Score"))
				player.sendMessage(Main.I + ChatColor.RED + "You have exceded the max score!");
			else
			{
				Infected.playerSetPoints(player.getName(), Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Points") + reward, 0);
				Infected.playerSetScore(player.getName(), Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Score") + score);
				Files.savePlayers();
				player.sendMessage(Main.I + ChatColor.AQUA + "Points +" + reward);
				Files.savePlayers();
			}
		}
	}

}
