
package me.sniperzciinema.infected.Messages;

import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Tools.Settings;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ScoreBoardVariables {

	public static String getLine(String string, Player user) {

		InfPlayer ip = InfPlayerManager.getInfPlayer(user);
		String newString = string;
		// Replace all variables we need

		newString = newString.replaceAll("<kills>", String.valueOf(ip.getKills()));
		newString = newString.replaceAll("<deaths>", String.valueOf(ip.getDeaths()));
		newString = newString.replaceAll("<highestkillstreak>", String.valueOf(ip.getHighestKillStreak()));
		newString = newString.replaceAll("<points>", String.valueOf(ip.getPoints(Settings.VaultEnabled())));
		newString = newString.replaceAll("<score>", String.valueOf(ip.getScore()));
		newString = newString.replaceAll("<players>", String.valueOf(ip.getScore()));
		
		// Replace color codes
		newString = ChatColor.translateAlternateColorCodes('&', ChatColor.stripColor(newString));

		// Make sure string isnt to long
		if (newString.length() > 16)
			newString = newString.substring(0, Math.min(newString.length(), 16));

		return newString;
	}
}
