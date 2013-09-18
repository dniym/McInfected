package me.xxsniperzzxx_sd.infected.Tools.Disguise;

import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class DisguisePlayer {

	public static void disguisePlayer(Player player){
		if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			DisguiseDisguiseCraft.disguisePlayer(player);
		}
		
		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			DisguiseIDisguise.disguisePlayer(player);
		}
	}
	
	

	public static void unDisguisePlayer(Player player){
		if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			DisguiseDisguiseCraft.unDisguisePlayer(player);
		}
		
		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			DisguiseIDisguise.unDisguisePlayer(player);
		}
	}
	
	

	public static boolean isPlayerDisguised(Player player){
		boolean disguised = false;
		if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			disguised = DisguiseDisguiseCraft.isPlayerDisguised(player);
		}
		
		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			disguised = DisguiseIDisguise.isPlayerDisguised(player);
		}
		return disguised;
	}
}
