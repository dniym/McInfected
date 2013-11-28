
package me.sniperzciinema.infectedv2.Disguise;

import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;
import me.sniperzciinema.infectedv2.Messages.StringUtil;

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
			if (DisguiseType.fromString(StringUtil.getWord(IP.getInfClass(Team.Zombie).getDisguise())) != null)
			{
				dcAPI.disguisePlayer(p, new Disguise(
						dcAPI.newEntityID(),
						DisguiseType.valueOf(StringUtil.getWord(IP.getInfClass(Team.Zombie).getDisguise()))).addSingleData("noarmor"));
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
