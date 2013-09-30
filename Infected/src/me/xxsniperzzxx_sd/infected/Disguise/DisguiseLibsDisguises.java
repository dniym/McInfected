
package me.xxsniperzzxx_sd.infected.Disguise;

import java.util.Random;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class DisguiseLibsDisguises {

	public static DisguiseAPI ldAPI;
	public static void disguisePlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()) && !Main.zombieClasses.get(player.getName()).equalsIgnoreCase("None")){
			if (!DisguiseAPI.isDisguised(player))
			{
				if (DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise").toUpperCase()) != null)
				{
					DisguiseAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise").toUpperCase())));
				} else{
					DisguiseAPI.disguiseToAll(player, new MobDisguise(DisguiseType.ZOMBIE));
				}

			} else
			{
				DisguiseAPI.undisguiseToAll(player);
				disguisePlayer(player);
			}
		} else
		{
			Random ra = new Random();
			int chance = ra.nextInt(100);
			if (!DisguiseAPI.isDisguised(player))
			{
				if (chance <= 20)
				{
					DisguiseAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.PIG_ZOMBIE, true));

					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = pigzombie");
					}
				} else
					DisguiseAPI.disguiseToAll(player, new MobDisguise(
							DisguiseType.ZOMBIE));
			} else
			{
				DisguiseAPI.undisguiseToAll(player);
				disguisePlayer(player);
			}
		}
	}
	
	public static void unDisguisePlayer(Player player){
		
		DisguiseAPI.undisguiseToAll(player);
	}


	public static boolean isPlayerDisguised(Player player){
		return	DisguiseAPI.isDisguised(player);
	}
	
	public static void setup(){
		Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("iDisguise");
	}
	
}
