
package me.sniperzciinema.infected.Tools;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Disguise.Disguises;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Listeners.CrackShotApi;
import me.sniperzciinema.infected.Listeners.FactionsEvents;
import me.sniperzciinema.infected.Listeners.mcMMOEvents;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


public class AddonManager {

	/**
	 * Go through all the plugins Infected supports, and if any are there and it
	 * has been enabled in the config, enable the support for that plugin
	 * (Vault, Crackshot, Factions, mcMMO, LibsDisguises, DisguiseCraft,
	 * iDisguise)
	 */
	public static void getAddons() {

		// ------------------------------------------------Vault------------------------------------------------\\
		if (Settings.VaultEnabled())
		{
			if (!(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null))
			{
				if (Settings.logAddonsEnabled())
					System.out.println("Vault support has been enabled!");

				RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

				if (economyProvider != null)
					Infected.economy = economyProvider.getProvider();

				else
				{
					if (Settings.logAddonsEnabled())
						System.out.println("Vault wasn't found on plugin server, Disabling Vault Support");

					Files.getConfig().set("Addons.Vault Support.Enabled", false);
					Files.saveConfig();
				}
			}
			else
			{
				if (Settings.logAddonsEnabled())
					System.out.println("Vault wasn't found on plugin server, Disabling Vault Support");

				Files.getConfig().set("Addons.Vault Support.Enabled", false);
				Files.saveConfig();

			}
		}
		else
			if (Settings.logAddonsEnabled())
				System.out.println("Vault Support is Disabled");

		// ------------------------------------------------Crackshot------------------------------------------------\\
		if (Settings.CrackShotEnabled())
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("CrackShot") == null)
			{
				if (Settings.logAddonsEnabled())
					System.out.println("CrackShot wasn't found on plugin server, disabling CrackShot Support");

				Files.getConfig().set("Addons.CrackShot Support.Enabled", false);
				Files.saveConfig();
			}
			else
			{
				CrackShotApi CSApi = new CrackShotApi();
				Bukkit.getPluginManager().registerEvents(CSApi, Infected.me);

				if (Settings.logAddonsEnabled())
					System.out.println("CrackShot support has been enabled!");
			}
		}
		else
			if (Settings.logAddonsEnabled())
				System.out.println("CrackShot Support is Disabled");

		// ------------------------------------------------Factions------------------------------------------------\\
		if (Settings.FactionsEnabled())
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("Factions") == null)
			{
				if (Settings.logAddonsEnabled())
					System.out.println("Factions wasn't found on plugin server, disabling Factions Support");

				Files.getConfig().set("Addons.Factions Support.Enabled", false);
				Files.saveConfig();
			}
			else
			{
				FactionsEvents FactionsEvents = new FactionsEvents();
				Bukkit.getPluginManager().registerEvents(FactionsEvents, Infected.me);

				if (Settings.logAddonsEnabled())
					System.out.println("Factions support has been enabled!");
			}
		}
		else
			if (Settings.logAddonsEnabled())
				System.out.println("Factions Support is Disabled");

		// --------------------------------------------------mcMMO--------------------------------------------------\\
		if (Settings.mcMMOEnabled())
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("mcMMO") == null)
			{
				if (Settings.logAddonsEnabled())
					System.out.println("mcMMO wasn't found on plugin server, disabling mcMMO Support");

				Files.getConfig().set("Addons.mcMMO Support.Enabled", false);
				Files.saveConfig();
			}
			else
			{
				mcMMOEvents mcMMOEvents = new mcMMOEvents();
				Bukkit.getPluginManager().registerEvents(mcMMOEvents, Infected.me);

				if (Settings.logAddonsEnabled())
					System.out.println("mcMMO support has been enabled!");
			}
		}
		else
			if (Settings.logAddonsEnabled())
				System.out.println("mcMMO Support is Disabled");

		// ------------------------------------------------Disguises------------------------------------------------\\
		/* Because theres multiple disguise plugin supports in Infected, i have
		 * it in it's own class with all the disguise features */
		if (Settings.DisguisesEnabled())
			Disguises.getDisguisePlugin();
		else
			if (Settings.logAddonsEnabled())
				System.out.println("Disguise Support is Disabled");

	}
}
