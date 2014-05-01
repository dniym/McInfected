
package me.sniperzciinema.infected.Disguise;

import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.StringUtil;

import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;


public class DisguiseDisguiseCraft {
	
	private static DisguiseCraftAPI	dcAPI	= DisguiseCraft.getAPI();
	
	/**
	 * Disguise the player depending on what their class's disguise is
	 * 
	 * @param p
	 */
	public static void disguisePlayer(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		
		if (!DisguiseDisguiseCraft.dcAPI.isDisguised(p))
		{
			if (DisguiseType.fromString(StringUtil.getWord(IP.getInfClass(Team.Zombie).getDisguise())) != null)
				DisguiseDisguiseCraft.dcAPI.disguisePlayer(p, new Disguise(
						DisguiseDisguiseCraft.dcAPI.newEntityID(),
						DisguiseType.valueOf(StringUtil.getWord(IP.getInfClass(Team.Zombie).getDisguise()))).addSingleData("noarmor"));
			else
				DisguiseDisguiseCraft.dcAPI.disguisePlayer(p, new Disguise(
						DisguiseDisguiseCraft.dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));
		}
		else
		{
			DisguiseDisguiseCraft.dcAPI.undisguisePlayer(p);
			disguisePlayer(p);
		}
	}
	
	/**
	 * @param p
	 * @return the player's disguise
	 */
	public static Disguise getDisguise(Player p) {
		return DisguiseDisguiseCraft.dcAPI.getDisguise(p);
	}
	
	/**
	 * @param p
	 * @return if the player is disguised
	 */
	public static boolean isPlayerDisguised(Player p) {
		return DisguiseDisguiseCraft.dcAPI.isDisguised(p);
	}
	
	/**
	 * unDisguise the player
	 * 
	 * @param p
	 */
	public static void unDisguisePlayer(Player p) {
		
		DisguiseDisguiseCraft.dcAPI.undisguisePlayer(p);
	}
}
