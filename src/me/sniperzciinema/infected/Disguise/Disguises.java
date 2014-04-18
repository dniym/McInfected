
package me.sniperzciinema.infected.Disguise;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Disguises {

	/**
	 * Check what disguise plugin we're using then disguise the player
	 * 
	 * @param player
	 */
	public static void disguisePlayer(Player player) {

		if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
			DisguiseDisguiseCraft.disguisePlayer(player);

		else
			if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
				DisguiseIDisguise.disguisePlayer(player);

			else
				if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
					DisguiseLibsDisguises.disguisePlayer(player);
	}

	public static String getDisguise(Player player) {
		if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
			return DisguiseDisguiseCraft.getDisguise(player).type.name();

		else
			if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
				return DisguiseIDisguise.getDisguise(player).getType().name();

			else
				if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
					return DisguiseLibsDisguises.getDisguise(player).getType().name();

				else
					return null;
	}

	/**
	 * Go through the disguise plugins that I've added support for, if one of
	 * them are on there, set that as disguiser(Plugin)
	 */
	public static void getDisguisePlugin() {

		if (Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft") != null)
			Infected.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft");

		else
			if (Bukkit.getServer().getPluginManager().getPlugin("iDisguise") != null)
				Infected.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("iDisguise");

			else
				if (Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises") != null)
					Infected.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises");

				else
				{
					if (Settings.logAddonsEnabled())
						System.out.println("No Valid Disguise Plugins found... disabling Disguise Support");

					Files.getConfig().set("Addons.Disguise Support.Enabled", false);
					Files.saveConfig();
				}
		if (Infected.Disguiser != null)
			if (Settings.logAddonsEnabled())
				System.out.println("For Disguise Support we're using " + Infected.Disguiser);

	}

	/**
	 * Check what disguise plugin we're using then check if the player is
	 * disguised
	 * 
	 * @param player
	 */
	public static boolean isPlayerDisguised(Player player) {

		boolean disguised = false;

		if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
			disguised = DisguiseDisguiseCraft.isPlayerDisguised(player);

		else
			if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
				disguised = DisguiseIDisguise.isPlayerDisguised(player);

			else
				if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
					disguised = DisguiseLibsDisguises.isPlayerDisguised(player);

		return disguised;
	}

	/**
	 * Check what disguise plugin we're using then undisguise the player
	 * 
	 * @param player
	 */
	public static void unDisguisePlayer(Player player) {

		if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
			DisguiseDisguiseCraft.unDisguisePlayer(player);

		else
			if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
				DisguiseIDisguise.unDisguisePlayer(player);

			else
				if (Infected.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
					DisguiseLibsDisguises.unDisguisePlayer(player);
	}
}
