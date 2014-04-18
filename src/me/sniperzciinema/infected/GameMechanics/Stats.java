
package me.sniperzciinema.infected.GameMechanics;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.MySQLManager;


public class Stats {

	public enum StatType
	{
		kills, deaths, points, score, killstreak, time;
	};

	// Get the deaths from the location required
	public static int getDeaths(String name) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(name, "Deaths"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Deaths");
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @return HighestKillStreak
	 */
	public static int getHighestKillStreak(String name) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(name, "HighestKillStreak"));
		else
			return Files.getPlayers().getInt("Players." + name + ".HighestKillStreak");
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @return Kills
	 */
	public static int getKills(String name) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(name, "Kills"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Kills");
	}

	/**
	 * Gets the value of the stat for the player's name
	 * 
	 * @param name
	 * @param stat
	 * @return value
	 */
	private static Integer getMySQLStats(String name, String stat) {
		name = name.toLowerCase();
		return MySQLManager.getInt("Infected", stat, name);
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @return PlayingTime
	 */
	public static int getPlayingTime(String name) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(name, "PlayingTime"));
		else
			return Files.getPlayers().getInt("Players." + name + ".PlayingTime");
	}

	/**
	 * Checks if we're going MySQL or Player.yml
	 * 
	 * @param name
	 * @return the players Score
	 */
	public static int getPoints(String name, boolean useVault) {
		name = name.toLowerCase();
		if (useVault)
			return (int) Infected.economy.getBalance(name);
		else
			if (Settings.MySQLEnabled())
				return Integer.valueOf(getMySQLStats(name, "Points"));
			else
				return Files.getPlayers().getInt("Players." + name + ".Points");
	}

	/**
	 * Checks if we're going MySQL or Player.yml
	 * 
	 * @param name
	 * @return the players Score
	 */
	public static int getScore(String name) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(name, "Score"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Score");
	}

	/**
	 * From a StatType get the value for the player
	 * 
	 * @param type
	 *            - The StatType
	 * @param user
	 *            - The player
	 * @return the value
	 */
	public static int getStat(StatType type, String user) {
		if (type == StatType.kills)
			return getKills(user);
		else
			if (type == StatType.deaths)
				return getDeaths(user);
			else
				if (type == StatType.points)
					return getPoints(user, Settings.VaultEnabled());
				else
					if (type == StatType.score)
						return getScore(user);
					else
						if (type == StatType.killstreak)
							return getHighestKillStreak(user);
						else
							if (type == StatType.time)
								return getPlayingTime(user);
							else
								return 0;
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param deaths
	 */

	public static void setDeaths(String name, Integer deaths) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			setMySQLStats(name, "Deaths", deaths);
		else
		{
			Files.getPlayers().set("Players." + name + ".Deaths", deaths);
			Files.savePlayers();
		}
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param highestKillStreak
	 */

	public static void setHighestKillStreak(String name, Integer highestKillStreak) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			setMySQLStats(name, "HighestKillStreak", highestKillStreak);
		else
		{
			Files.getPlayers().set("Players." + name + ".HighestKillStreak", highestKillStreak);
			Files.savePlayers();
		}
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param kills
	 */

	public static void setKills(String name, Integer kills) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			setMySQLStats(name, "Kills", kills);
		else
		{
			Files.getPlayers().set("Players." + name + ".Kills", kills);
			Files.savePlayers();
		}
	}

	/**
	 * Sets the value of the stat to the player's name
	 * 
	 * @param name
	 * @param stat
	 * @param value
	 */
	private static void setMySQLStats(String name, String stat, int value) {
		name = name.toLowerCase();
		MySQLManager.update("Infected", stat, value, name);
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param PlayingTime
	 */

	public static void setPlayingTime(String name, long l) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			setMySQLStats(name, "PlayingTime", (int) l);
		else
		{
			Files.getPlayers().set("Players." + name + ".PlayingTime", l);
			Files.savePlayers();
		}
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param points
	 */
	public static void setPoints(String name, Integer points, boolean useVault) {
		name = name.toLowerCase();
		if (useVault)
		{
			int cPoints = Stats.getPoints(name, useVault);
			if (cPoints > points)
			{
				int price = cPoints - points;
				Infected.economy.withdrawPlayer(name, price);
			}
			else
			{
				int depo = points - cPoints;
				Infected.economy.depositPlayer(name, depo);
			}
		}
		else
			if (Settings.MySQLEnabled())
				setMySQLStats(name, "Points", points);
			else
			{
				Files.getPlayers().set("Players." + name + ".Points", points);
				Files.savePlayers();
			}
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param score
	 */
	public static void setScore(String name, Integer score) {
		name = name.toLowerCase();
		if (Settings.MySQLEnabled())
			setMySQLStats(name, "Score", score);
		else
		{
			Files.getPlayers().set("Players." + name + ".Score", score);
			Files.savePlayers();
		}
	}
}
