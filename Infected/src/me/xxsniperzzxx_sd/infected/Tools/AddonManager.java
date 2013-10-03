package me.xxsniperzzxx_sd.infected.Tools;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Listeners.CrackShotApi;
import me.xxsniperzzxx_sd.infected.Listeners.FactionsEvents;
import me.xxsniperzzxx_sd.infected.Listeners.TagApi;
import me.xxsniperzzxx_sd.infected.Listeners.mcMMOEvents;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


public class AddonManager {

	public Main plugin;

	public AddonManager(Main instance){
		plugin = instance;
	}
	public void getAddons(){

	// Check if the plugin addons are there
	if (plugin.getConfig().getBoolean("Vault Support.Enable"))
	{
		if (!(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null))
		{
			System.out.println("Vault support has been enabled!");
		
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null)
			{
				Main.economy = economyProvider.getProvider();
			}
			
		} 
		else
		{
			System.out.println(Main.I + "Vault wasn't found on plugin server, Disabling Vault Support");
			plugin.getConfig().set("Vault Support.Enable", false);
			plugin.saveConfig();

		}
	}else
		System.out.println("Vault Support is Disabled");


	if (plugin.getConfig().getBoolean("CrackShot Support.Enable"))
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") == null)
		{

			System.out.println(Main.I + "CrackShot wasn't found on plugin server, disabling CrackShot Support");
			plugin.getConfig().set("CrackShot Support.Enable", false);
			plugin.saveConfig();
		} else
		{
		CrackShotApi CSApi = new CrackShotApi(plugin);
		Bukkit.getPluginManager().registerEvents(CSApi, plugin);
		System.out.println("CrackShot support has been enabled!");
		}
	}else
		System.out.println("CrackShot Support is Disabled");


	if (plugin.getConfig().getBoolean("Factions Support.Enable"))
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("Factions") == null)
		{
			System.out.println(Main.I + "Factions wasn't found on plugin server, disabling Factions Support");
			plugin.getConfig().set("Factions Support.Enable", false);
			plugin.saveConfig();
		} else
		{
			FactionsEvents FactionsEvents = new FactionsEvents(plugin);
			Bukkit.getPluginManager().registerEvents(FactionsEvents, plugin);
			System.out.println("Factions support has been enabled!");
		}
	}else
		System.out.println("Factions Support is Disabled");
	

	if (plugin.getConfig().getBoolean("mcMMO Support.Enable"))
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("mcMMO") == null)
		{
			System.out.println(Main.I + "mcMMO wasn't found on plugin server, disabling mcMMO Support");
			plugin.getConfig().set("mcMMO Support.Enable", false);
			plugin.saveConfig();
		} else
		{
			mcMMOEvents mcMMOEvents = new mcMMOEvents(plugin);
			Bukkit.getPluginManager().registerEvents(mcMMOEvents, plugin);
			System.out.println("mcMMO support has been enabled!");
		}
	}else
		System.out.println("mcMMO Support is Disabled");
	
	
	if (plugin.getConfig().getBoolean("TagAPI Support.Enable"))
	{
		if (Bukkit.getServer().getPluginManager().getPlugin("TagAPI") == null)
		{
			System.out.println(Main.I + "TagApi wasn't found on plugin server, disabling TagApi Support");
			plugin.getConfig().set("TagAPI Support.Enable", false);
			plugin.saveConfig();
		} else
		{
			TagApi TagApi = new TagApi(plugin);
			Bukkit.getPluginManager().registerEvents(TagApi, plugin);
			System.out.println("TagApi support has been enabled!");
		}
	}else
		System.out.println("TagAPI Support is Disabled");
	
	Disguises.getDisguisePlugin();
	}
}
