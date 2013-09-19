
package me.xxsniperzzxx_sd.infected.Disguise;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;


public class DisguiseDisguiseCraft {

	public static void disguisePlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()))
		{
			if (!Main.dcAPI.isDisguised(player))
			{
				if (!(DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise")) == null))
				{
					Main.dcAPI.disguisePlayer(player, new Disguise(
							Main.dcAPI.newEntityID(),
							DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise"))).addSingleData("noarmor"));
				} else
					Main.dcAPI.disguisePlayer(player, new Disguise(
							Main.dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));

			} else
			{
				Main.dcAPI.undisguisePlayer(player);
				disguisePlayer(player);
			}
		} else
		{
			// https://gitorious.org/disguisecraft/disguisecraft/blobs/master/src/pgDev/bukkit/DisguiseCraft/disguise/Disguise.java#line234
			Random ra = new Random();
			int chance = ra.nextInt(100);
			if (!Main.dcAPI.isDisguised(player))
			{
				if (chance <=20)
				{
					Main.dcAPI.disguisePlayer(player, new Disguise(
							Main.dcAPI.newEntityID(), DisguiseType.PigZombie).addSingleData("noarmor"));
					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = pigzombie");
					}
				}  else
				{
					Main.dcAPI.disguisePlayer(player, new Disguise(
							Main.dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));
					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = zombie");
					}
				}
			} else
			{
				Main.dcAPI.undisguisePlayer(player);
				disguisePlayer(player);
			}
		}
	}
	
	public static void unDisguisePlayer(Player player){

		Main.dcAPI.undisguisePlayer(player);
		disguisePlayer(player);
	}

	public static boolean isPlayerDisguised(Player player){
		return Main.dcAPI.isDisguised(player);
	}
}
