
package me.xxsniperzzxx_sd.infected.Disguise;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.robingrether.idisguise.api.DisguiseAPI;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.disguise.MobDisguise;


public class DisguiseIDisguise {

	public static DisguiseAPI idAPI = Bukkit.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();;

	public static void disguisePlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()) && !Main.zombieClasses.get(player.getName()).equalsIgnoreCase("None")){
			if (!idAPI.isDisguised(player))
			{
				if (DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise").toUpperCase()) != null)
				{
					idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise").toUpperCase()),
							true));
				} else
					idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.ZOMBIE, true));

			} else
			{
				idAPI.undisguiseToAll(player);
				disguisePlayer(player);
			}
		} else
		{
			Random ra = new Random();
			int chance = ra.nextInt(100);
			if (!idAPI.isDisguised(player))
			{
				if (chance <= 20)
				{
					idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.PIG_ZOMBIE, true));

					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = pigzombie");
					}
				} else
					idAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.ZOMBIE, true));
			} else
			{
				idAPI.undisguiseToAll(player);
				disguisePlayer(player);
			}
		}
	}
	
	public static void unDisguisePlayer(Player player){
		
		idAPI.undisguiseToAll(player);
	}


	public static boolean isPlayerDisguised(Player player){
		return	idAPI.isDisguised(player);
	}
	
	
}
