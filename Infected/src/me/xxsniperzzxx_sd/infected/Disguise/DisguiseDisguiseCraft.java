
package me.xxsniperzzxx_sd.infected.Disguise;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;


public class DisguiseDisguiseCraft {

	public static DisguiseCraftAPI dcAPI;
	
	public static void disguisePlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()))
		{
			if (!dcAPI.isDisguised(player))
			{
				if (DisguiseType.fromString(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise")) != null)
				{
					dcAPI.disguisePlayer(player, new Disguise(
							dcAPI.newEntityID(),
							DisguiseType.valueOf(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Disguise"))).addSingleData("noarmor"));
				} else
					dcAPI.disguisePlayer(player, new Disguise(
							dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));

			} else
			{
				dcAPI.undisguisePlayer(player);
				disguisePlayer(player);
			}
		} else
		{
			// https://gitorious.org/disguisecraft/disguisecraft/blobs/master/src/pgDev/bukkit/DisguiseCraft/disguise/Disguise.java#line234
			Random ra = new Random();
			int chance = ra.nextInt(100);
			if (!dcAPI.isDisguised(player))
			{
				if (chance <=20)
				{
					dcAPI.disguisePlayer(player, new Disguise(
							dcAPI.newEntityID(), DisguiseType.PigZombie).addSingleData("noarmor"));
					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = pigzombie");
					}
				}  else
				{
					dcAPI.disguisePlayer(player, new Disguise(
							dcAPI.newEntityID(), DisguiseType.Zombie).addSingleData("noarmor"));
					if (Main.config.getBoolean("Debug"))
					{
						System.out.println("Choosing new zombie: " + player.getName() + " = zombie");
					}
				}
			} else
			{
				dcAPI.undisguisePlayer(player);
				disguisePlayer(player);
			}
		}
	}
	
	public static void unDisguisePlayer(Player player){

		dcAPI.undisguisePlayer(player);
	}

	public static boolean isPlayerDisguised(Player player){
		return dcAPI.isDisguised(player);
	}
	public static void setup(){
		dcAPI = DisguiseCraft.getAPI();
		Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft");
	}
}
