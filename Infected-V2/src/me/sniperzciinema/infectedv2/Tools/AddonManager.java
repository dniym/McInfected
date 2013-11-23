
package me.sniperzciinema.infectedv2.Tools;

import me.sniperzciinema.infectedv2.Main;
import me.sniperzciinema.infectedv2.Disguise.Disguises;
import me.sniperzciinema.infectedv2.Listeners.CrackShotApi;
import me.sniperzciinema.infectedv2.Listeners.FactionsEvents;
import me.sniperzciinema.infectedv2.Listeners.TagApi;
import me.sniperzciinema.infectedv2.Listeners.mcMMOEvents;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


public class AddonManager {

	public Main plugin;

	public AddonManager(Main instance)
	{
		plugin = instance;
	}

	public static boolean useVault = false;
	public static boolean useDisguises = false;
	public static boolean useCrackShot = false;
	public static boolean useFactions = false;
	public static boolean useEssentials = false;
	public static boolean useMcmmo = false;
	public static boolean useTagApi = false;
	
	public void getAddons() {

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
					useVault = true;
				}

			} else
			{
				System.out.println("Vault wasn't found on plugin server, Disabling Vault Support");
				plugin.getConfig().set("Vault Support.Enable", false);
				plugin.saveConfig();

			}
		} else
			System.out.println("Vault Support is Disabled");

		if (plugin.getConfig().getBoolean("CrackShot Support.Enable"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") == null)
			{

				System.out.println("CrackShot wasn't found on plugin server, disabling CrackShot Support");
				plugin.getConfig().set("CrackShot Support.Enable", false);
				plugin.saveConfig();
			} else
			{
				CrackShotApi CSApi = new CrackShotApi();
				Bukkit.getPluginManager().registerEvents(CSApi, plugin);
				System.out.println("CrackShot support has been enabled!");
				useCrackShot = true;
			}
		} else
			System.out.println("CrackShot Support is Disabled");

		if (plugin.getConfig().getBoolean("Factions Support.Enable"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("Factions") == null)
			{
				System.out.println("Factions wasn't found on plugin server, disabling Factions Support");
				plugin.getConfig().set("Factions Support.Enable", false);
				plugin.saveConfig();
			} else
			{
				FactionsEvents FactionsEvents = new FactionsEvents();
				Bukkit.getPluginManager().registerEvents(FactionsEvents, plugin);
				System.out.println("Factions support has been enabled!");
				useFactions = true;
			}
		} else
			System.out.println("Factions Support is Disabled");

		if (plugin.getConfig().getBoolean("mcMMO Support.Enable"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("mcMMO") == null)
			{
				System.out.println("mcMMO wasn't found on plugin server, disabling mcMMO Support");
				plugin.getConfig().set("mcMMO Support.Enable", false);
				plugin.saveConfig();
			} else
			{
				mcMMOEvents mcMMOEvents = new mcMMOEvents();
				Bukkit.getPluginManager().registerEvents(mcMMOEvents, plugin);
				System.out.println("mcMMO support has been enabled!");
				useMcmmo = true;
			}
		} else
			System.out.println("mcMMO Support is Disabled");

		if (plugin.getConfig().getBoolean("TagAPI Support.Enable"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("TagAPI") == null)
			{
				System.out.println("TagApi wasn't found on plugin server, disabling TagApi Support");
				plugin.getConfig().set("TagAPI Support.Enable", false);
				plugin.saveConfig();
			} else
			{
				TagApi TagApi = new TagApi(plugin);
				Bukkit.getPluginManager().registerEvents(TagApi, plugin);
				System.out.println("TagApi support has been enabled!");
				useTagApi = true;
			}
		} else
			System.out.println("TagAPI Support is Disabled");

	
		Disguises.getDisguisePlugin();
	}
}
