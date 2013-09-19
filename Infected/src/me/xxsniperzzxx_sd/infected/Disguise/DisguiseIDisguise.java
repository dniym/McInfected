
package me.xxsniperzzxx_sd.infected.Disguise;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.entity.Player;

import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;


public class DisguiseIDisguise {

	public static void disguisePlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()))
		{
			if (!Main.idAPI.isDisguised(player))
			{
				if (!(DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise")) == null))
				{
					Main.idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise")),
							true));
				} else
					Main.idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.ZOMBIE, true));

			} else
			{
				Main.idAPI.undisguiseToAll(player);
				disguisePlayer(player);
			}
		} else
		{
			Random ra = new Random();
			int chance = ra.nextInt(100);
			if (!Main.idAPI.isDisguised(player))
			{
				if (chance <= 20)
				{
					Main.idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.PIG_ZOMBIE, true));

					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = pigzombie");
					}
				} else
					Main.idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.ZOMBIE, true));
			} else
			{
				Main.idAPI.undisguiseToAll(player);
				disguisePlayer(player);
			}
		}
	}
	
	public static void unDisguisePlayer(Player player){
		
		Main.idAPI.undisguiseToAll(player);
		disguisePlayer(player);
	}


	public static boolean isPlayerDisguised(Player player){
		return	Main.idAPI.isDisguised(player);
	}
	
}
