
package me.sniperzciinema.infected.Disguise;

import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;


public class DisguiseIDisguise {

	public static DisguiseAPI	idAPI	= Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();

	/**
	 * Disguise the player depending on what their class's disguise is
	 * 
	 * @param p
	 */
	public static void disguisePlayer(Player p) {

		if (!DisguiseIDisguise.idAPI.isDisguised(p))
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(p);

			if (DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()) != null)
				DisguiseIDisguise.idAPI.disguiseToAll(p, new MobDisguise(
						DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()),
						true));
			else
				DisguiseIDisguise.idAPI.disguiseToAll(p, new MobDisguise(DisguiseType.ZOMBIE, true));

		}
		else
		{
			DisguiseIDisguise.idAPI.undisguiseToAll(p);
			disguisePlayer(p);
		}
	}

	/**
	 * @param p
	 * @return the player's disguise
	 */
	public static Disguise getDisguise(Player p) {
		return DisguiseIDisguise.idAPI.getDisguise(p);
	}

	/**
	 * @param p
	 * @return if the player is disguised
	 */
	public static boolean isPlayerDisguised(Player p) {
		return DisguiseIDisguise.idAPI.isDisguised(p);
	}

	/**
	 * unDisguise the player
	 * 
	 * @param p
	 */
	public static void unDisguisePlayer(Player p) {

		DisguiseIDisguise.idAPI.undisguiseToAll(p);
	}

}
