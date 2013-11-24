
package me.sniperzciinema.infectedv2.Disguise;

import me.sniperzciinema.infectedv2.Main;
import me.sniperzciinema.infectedv2.Tools.Files;
import me.sniperzciinema.infectedv2.Tools.Settings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Disguises {

	public static void disguisePlayer(Player player) {
		if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			DisguiseDisguiseCraft.disguisePlayer(player);
		}

		else if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			DisguiseIDisguise.disguisePlayer(player);
		}

		else if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
		{
			DisguiseLibsDisguises.disguisePlayer(player);
		}
	}

	public static void unDisguisePlayer(Player player) {
		if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			DisguiseDisguiseCraft.unDisguisePlayer(player);
		}

		else if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			DisguiseIDisguise.unDisguisePlayer(player);
		}

		else if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
		{
			DisguiseLibsDisguises.unDisguisePlayer(player);
		}
	}

	public static boolean isPlayerDisguised(Player player) {
		boolean disguised = false;
		if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft"))
		{
			disguised = DisguiseDisguiseCraft.isPlayerDisguised(player);
		}

		else if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("iDisguise"))
		{
			disguised = DisguiseIDisguise.isPlayerDisguised(player);
		}

		else if (Main.Disguiser == Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"))
		{
			disguised = DisguiseLibsDisguises.isPlayerDisguised(player);
		}

		return disguised;
	}

	public static void getDisguisePlugin() {
		if (Settings.DisguisesEnabled())
		{
			if (Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft") != null)
				Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("DisguiseCraft");

			else if (Bukkit.getServer().getPluginManager().getPlugin("iDisguise") != null)
				Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("iDisguise");

			else if (Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises") != null)
				Main.Disguiser = Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises");

			else
			{
				System.out.println("No Valid Disguise Plugins found... disabling Disguise Support");
				Files.getConfig().set("Addons.Disguise Support.Enabled", false);
				Files.saveConfig();
			}
			if (Main.Disguiser != null)
			{
				System.out.println("For Disguise Support we're using " + Main.Disguiser);
			}
		} else
		{
			System.out.println("Disguise Support is Disabled");
		}
	}
}
