package me.xxsniperzzxx_sd.infected.Disguise;

import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;




public class Disguises {
	

	public static void disguisePlayer(Player player){
		if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			DisguiseDisguiseCraft.disguisePlayer(player);
		}
		
		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			DisguiseIDisguise.disguisePlayer(player);
		}
		
		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
		{
			DisguiseLibsDisguises.disguisePlayer(player);
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
		
		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
		{
			DisguiseLibsDisguises.unDisguisePlayer(player);
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

		else if(Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
		{
			disguised = DisguiseLibsDisguises.isPlayerDisguised(player);
		}
		
		return disguised;
	}
	
	
	
	
	
	public static void getDisguisePlugin(){
		//If disguises are a go
		if (Main.config.getBoolean("Disguise Support.Enabled"))
		{
			//If we're looking for disguisecraft
			if(Main.config.getBoolean("Disguise Support.DisguiseCraft")){
				if (Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft") != null)
				{
					
					Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft");
				}
				else
				{
					System.out.println("DisguiseCraft wasn't found on this server, disabling DisguiseCraft Support");
					Main.config.set("Disguise Support.DisguiseCraft", false);
					Main.me.saveConfig();
				}
			
			}
			//If were looking for iDisguise
			if(Main.config.getBoolean("Disguise Support.iDisguise")){

				if (Bukkit.getServer().getPluginManager().getPlugin("iDisguise") != null)
				{
					
					Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("iDisguise");
				}
				else
				{
					System.out.println("iDisguise wasn't found on this server, disabling iDisguise Support");
					Main.config.set("Disguise Support.iDisguise", false);
					Main.me.saveConfig();
				}
			}
			//If were looking for LibsDisguises
			if(Main.config.getBoolean("Disguise Support.LibsDisguises")){

				if (Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises") != null)
				{
					
					Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises");
				}
				else
				{
					System.out.println("LibsDisguises wasn't found on this server, disabling LibsDisguises Support");
					Main.config.set("Disguise Support.LibsDisguises", false);
					Main.me.saveConfig();
				}
			}
			if(Main.Disguiser == null){
				System.out.println("No Valid Disguise Plugins found... disabling Disguise Support");
				Main.config.set("Disguise Support.Enabled", false);
				Main.me.saveConfig();
			}
			if(Main.Disguiser != null){
				System.out.println("For Disguise Support we're using " + Main.Disguiser);
			}
		}else{
			System.out.println("Disguise Support is Disabled");
		}
	}
	
}
