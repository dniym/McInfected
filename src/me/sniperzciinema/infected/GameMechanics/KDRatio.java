
package me.sniperzciinema.infected.GameMechanics;

import java.util.UUID;

import me.sniperzciinema.infected.Handlers.UUID.UUIDManager;

import org.bukkit.Bukkit;


public class KDRatio {
	
	/**
	 * @param user
	 *          - The players name
	 * @return Their kill/death Ratio
	 */
	@SuppressWarnings("deprecation")
	public static Double KD(String user) {
		UUID id = null;
		if (Bukkit.getPlayer(user) != null)
			id = Bukkit.getPlayer(user).getUniqueId();
		else
			id = UUIDManager.getPlayerUUID(user);
		int kills = Stats.getKills(id);
		int deaths = Stats.getDeaths(id);
		double ratio = Math.round(((double) kills / (double) deaths) * 100.0D) / 100.0D;
		if (deaths == 0)
			ratio = kills;
		else if (kills == 0)
			ratio = 0.00;
		return ratio;
	}
	
}
