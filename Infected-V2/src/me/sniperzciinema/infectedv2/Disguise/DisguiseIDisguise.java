
package me.sniperzciinema.infectedv2.Disguise;

import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;


public class DisguiseIDisguise {

	public static DisguiseAPI idAPI = Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();;

	public static void disguisePlayer(Player p) {

		if (!idAPI.isDisguised(p))
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(p);

			if (DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()) != null)
			{
				idAPI.disguiseToAll(p, new MobDisguise(
						DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()),
						true));
			} else
				idAPI.disguiseToAll(p, new MobDisguise(DisguiseType.ZOMBIE,
						true));

		} else
		{
			idAPI.undisguiseToAll(p);
			disguisePlayer(p);
		}
	}

	public static void unDisguisePlayer(Player player) {

		idAPI.undisguiseToAll(player);
	}

	public static boolean isPlayerDisguised(Player player) {
		return idAPI.isDisguised(player);
	}

}
