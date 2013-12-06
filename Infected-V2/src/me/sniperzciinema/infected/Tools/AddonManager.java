
package me.sniperzciinema.infected.Tools;

import me.sniperzciinema.infected.Main;
import me.sniperzciinema.infected.Disguise.Disguises;
import me.sniperzciinema.infected.Listeners.CrackShotApi;
import me.sniperzciinema.infected.Listeners.FactionsEvents;
import me.sniperzciinema.infected.Listeners.mcMMOEvents;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


public class AddonManager {

	public Main plugin;

	public AddonManager(Main instance)
	{
		plugin = instance;
	}

	public void getAddons() {

		// Check if the plugin addons are there
		if (plugin.getConfig().getBoolean("Addons.Vault Support.Enabled"))
		{
			if (!(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null))
			{
				if (Settings.logAddonsEnabled())
					System.out.println("Vault support has been enabled!");

				RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
				if (economyProvider != null)
				{
					Main.economy = economyProvider.getProvider();
				} else
				{
					if (Settings.logAddonsEnabled())
						System.out.println("Vault wasn't found on plugin server, Disabling Vault Support");
					plugin.getConfig().set("Addons.Vault Support.Enabled", false);
					plugin.saveConfig();
				}
			} else
			{
				if (Settings.logAddonsEnabled())
					System.out.println("Vault wasn't found on plugin server, Disabling Vault Support");
				plugin.getConfig().set("Addons.Vault Support.Enabled", false);
				plugin.saveConfig();

			}
		} else if (Settings.logAddonsEnabled())
			System.out.println("Vault Support is Disabled");

		if (plugin.getConfig().getBoolean("Addons.CrackShot Support.Enabled"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") == null)
			{

				if (Settings.logAddonsEnabled())
					System.out.println("CrackShot wasn't found on plugin server, disabling CrackShot Support");
				plugin.getConfig().set("Addons.CrackShot Support.Enabled", false);
				plugin.saveConfig();
			} else
			{
				CrackShotApi CSApi = new CrackShotApi();
				Bukkit.getPluginManager().registerEvents(CSApi, plugin);
				if (Settings.logAddonsEnabled())
					System.out.println("CrackShot support has been enabled!");
			}
		} else if (Settings.logAddonsEnabled())
			System.out.println("CrackShot Support is Disabled");

		if (plugin.getConfig().getBoolean("Addons.Factions Support.Enabled"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("Factions") == null)
			{
				if (Settings.logAddonsEnabled())
					System.out.println("Factions wasn't found on plugin server, disabling Factions Support");
				plugin.getConfig().set("Addons.Factions Support.Enabled", false);
				plugin.saveConfig();
			} else
			{
				FactionsEvents FactionsEvents = new FactionsEvents();
				Bukkit.getPluginManager().registerEvents(FactionsEvents, plugin);
				if (Settings.logAddonsEnabled())
					System.out.println("Factions support has been enabled!");
			}
		} else if (Settings.logAddonsEnabled())
			System.out.println("Factions Support is Disabled");

		if (plugin.getConfig().getBoolean("Addons.mcMMO Support.Enabled"))
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("mcMMO") == null)
			{
				if (Settings.logAddonsEnabled())
					System.out.println("mcMMO wasn't found on plugin server, disabling mcMMO Support");
				plugin.getConfig().set("Addons.mcMMO Support.Enabled", false);
				plugin.saveConfig();
			} else
			{
				mcMMOEvents mcMMOEvents = new mcMMOEvents();
				Bukkit.getPluginManager().registerEvents(mcMMOEvents, plugin);
				if (Settings.logAddonsEnabled())
					System.out.println("mcMMO support has been enabled!");
			}
		} else if (Settings.logAddonsEnabled())
			System.out.println("mcMMO Support is Disabled");

		Disguises.getDisguisePlugin();
	}
}
