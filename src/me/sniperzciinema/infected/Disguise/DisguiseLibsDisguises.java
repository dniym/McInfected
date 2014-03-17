
package me.sniperzciinema.infected.Disguise;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;

import org.bukkit.entity.Player;



public class DisguiseLibsDisguises {

	/**
	 * Disguise the player depending on what their class's disguise is
	 * 
	 * @param p
	 */
	@SuppressWarnings("deprecation")
	public static void disguisePlayer(Player p) {

		if (!DisguiseAPI.isDisguised(p))
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(p);

			if (DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()) != null)
			{
				MobDisguise md = new MobDisguise(
						DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()),
						true);
				// TODO: Figure our why when i try and show the disguise to the
				// player it crashes the console...
				DisguiseAPI.disguiseToAll(p, md);
			} else
				DisguiseAPI.disguiseToAll(p, new MobDisguise(
						DisguiseType.ZOMBIE, true));

		} else
		{
			DisguiseAPI.undisguiseToAll(p);
			disguisePlayer(p);
		}
	}

	/**
	 * 
	 * @param p
	 * @return the player's disguise
	 */
	@SuppressWarnings("deprecation")
	public static Disguise getDisguise(Player p){
		return DisguiseAPI.getDisguise(p);
	}
	/**
	 * unDisguise the player
	 * 
	 * @param p
	 */
	public static void unDisguisePlayer(Player p) {

		DisguiseAPI.undisguiseToAll(p);
	}

	/**
	 * 
	 * @param p
	 * @return if the player is disguised
	 */
	@SuppressWarnings("deprecation")
	public static boolean isPlayerDisguised(Player p) {
		return DisguiseAPI.isDisguised(p);
	}

}
