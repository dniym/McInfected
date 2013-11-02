
package me.sniperzciinema.infectedv2.Disguise;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;

import org.bukkit.entity.Player;


public class DisguiseLibsDisguises {

	public static void disguisePlayer(Player p) {

		if (!DisguiseAPI.isDisguised(p))
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(p);

			if (DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise()) != null)
			{
				DisguiseAPI.disguiseToAll(p, new MobDisguise(
						DisguiseType.valueOf(IP.getInfClass(Team.Zombie).getDisguise().toUpperCase()),
						true));
			} else
				DisguiseAPI.disguiseToAll(p, new MobDisguise(
						DisguiseType.ZOMBIE, true));

		} else
		{
			DisguiseAPI.undisguiseToAll(p);
			disguisePlayer(p);
		}
	}

	public static void unDisguisePlayer(Player player) {

		DisguiseAPI.undisguiseToAll(player);
	}

	public static boolean isPlayerDisguised(Player player) {
		return DisguiseAPI.isDisguised(player);
	}

}
