
package me.xxsniperzzxx_sd.infected.Disguise;

import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;

import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;


public class DisguiseDisguiseCraft {

	private static DisguiseCraftAPI dcAPI = DisguiseCraft.getAPI();

	public static void disguisePlayer(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);

		if (!dcAPI.isDisguised(p))
		{
			if (DisguiseType.fromString(IP.getInfClass(Team.Zombie).getDisguise()) != null)
			{
				dcAPI.disguisePlayer(p, new Disguise(
						dcAPI.newEntityID(),
						DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise())).addSingleData("noarmor"));
			} else
				dcAPI.disguisePlayer(p, new Disguise(dcAPI.newEntityID(),
						DisguiseType.Zombie).addSingleData("noarmor"));
		} else
		{
			dcAPI.undisguisePlayer(p);
			disguisePlayer(p);
		}
	}

	public static void unDisguisePlayer(Player player) {

		dcAPI.undisguisePlayer(player);
	}

	public static boolean isPlayerDisguised(Player player) {
		return dcAPI.isDisguised(player);
	}
}
