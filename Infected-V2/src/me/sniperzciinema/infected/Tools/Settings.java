
package me.sniperzciinema.infected.Tools;

import java.util.HashMap;
import java.util.List;


public class Settings {

	public static boolean shopsEnabled() {
		return Files.getShop().getBoolean("Enabled");
	}

	public static boolean DisguisesEnabled() {
		return Files.getConfig().getBoolean("Addons.Disguise Support.Enabled");
	}

	public static boolean VaultEnabled() {
		return Files.getConfig().getBoolean("Addons.Vault Support.Enabled");
	}

	public static boolean mcMMOEnabled() {
		return Files.getConfig().getBoolean("Addons.mcMMO Support.Enabled");
	}

	public static boolean FactionsEnabled() {
		return Files.getConfig().getBoolean("Addons.Factions Support.Enabled");
	}

	public static boolean TagAPIEnabled() {
		return Files.getConfig().getBoolean("Addons.TagAPI Support.Enabled");
	}

	public static boolean CrackShotEnabled() {
		return Files.getConfig().getBoolean("Addons.CrackShot Support.Enabled");
	}

	public static int getRequiredPlayers() {
		return Files.getConfig().getInt("Settings.Misc.Automatic Start.Minimum Players");
	}

	public static boolean MySQLEnabled() {
		return Files.getConfig().getBoolean("MySQL.Enabled");
	}

	public static int InfoSignsUpdateTime() {
		return Files.getConfig().getInt("Settings.Misc.Info Signs.Refresh Time");
	}

	public static boolean InfoSignsEnabled() {
		return Files.getConfig().getBoolean("Settings.Misc.Info Signs.Enabled");
	}

	public static List<String> AllowedCommands() {
		return Files.getConfig().getStringList("Settings.Misc.Allowed Commands");
	}

	public static boolean useBookForHelp() {
		return Files.getConfig().getBoolean("Settings.Misc.Use Book For Help");
	}

	public static HashMap<String, Integer> getExtraVoteNodes() {
		HashMap<String, Integer> nodes = new HashMap<String, Integer>();

		for (String node : Files.getConfig().getConfigurationSection("Settings.Misc.Votes.Extra Votes").getKeys(true))
			if (!node.contains("."))
			{
				nodes.put(node, Files.getConfig().getInt("Settings.Misc.Votes.Extra Votes." + node));
			}
		return nodes;
	}

	public static List<String> getScoreBoardRows() {
		return Files.getConfig().getStringList("Settings.Misc.ScoreBoard Stats");
	}

	public static int getVotingTime() {
		return Files.getConfig().getInt("Settings.Global.Time.Voting");
	}

	public static boolean logAddonsEnabled() {
		return Files.getConfig().getBoolean("Logs.Addons");
	}

	public static boolean logAreansEnabled() {
		return Files.getConfig().getBoolean("Logs.Arenas");
	}

	public static boolean logGrenadesEnabled() {
		return Files.getConfig().getBoolean("Logs.Grenades");
	}

	public static boolean logClassesEnabled() {
		return Files.getConfig().getBoolean("Logs.Classes");
	}
}
