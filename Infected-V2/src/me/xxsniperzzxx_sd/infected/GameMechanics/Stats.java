
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Tools.Files;


public class Stats {

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @return HighestKillStreak
	 */
	public static int getHighestKillStreak(String name) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "HighestKillStreak"));
		else
			return Files.getPlayers().getInt("Players." + name + ".HighestKillStreak");
	}


	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param highestKillStreak
	 */

	public static void setHighestKillStreak(String name, Integer highestKillStreak) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
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
	 * @return Kills
	 */
	public static int getKills(String name) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Kills"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Kills");
	}


	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param kills
	 */

	public static void setKills(String name, Integer kills) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Kills", kills);
		else
		{
			Files.getPlayers().set("Players." + name + ".Kills", kills);
			Files.savePlayers();
		}
	}

	// Get the deaths from the location required
	public static int getDeaths(String name) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Deaths"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Deaths");
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param deaths
	 */

	public static void setDeaths(String name, Integer deaths) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Deaths", deaths);
		else
		{
			Files.getPlayers().set("Players." + name + ".Deaths", deaths);
			Files.savePlayers();
		}
	}

	/**
	 * Checks if we're going MySQL or Player.yml
	 * 
	 * @param name
	 * @return the players Score
	 */
	public static int getScore(String name) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Score"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Score");
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param score
	 */
	public static void setScore(String name, Integer score) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Score", score);
		else
		{
			Files.getPlayers().set("Players." + name + ".Score", score);
			Files.savePlayers();
		}
	}
	/**
	 * Checks if we're going MySQL or Player.yml
	 * 
	 * @param name
	 * @return the players Score
	 */
	public static int getPoints(String name) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "Points"));
		else
			return Files.getPlayers().getInt("Players." + name + ".Points");
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param points
	 */
	public static void setPoints(String name, Integer points) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			setMySQLStats(name, "Points", points);
		else
		{
			Files.getPlayers().set("Players." + name + ".Points", points);
			Files.savePlayers();
		}
	}
	/**
	 * Attempts to get Stats from MySQL (Untested)
	 * 
	 * @param name
	 * @param stat
	 * @return value
	 */
	private static String getMySQLStats(String name, String stat) {
		String value = "0";

		Statement statement;
		try
		{
			statement = Main.c.createStatement();

			ResultSet res;

			res = statement.executeQuery("SELECT * FROM " + stat + " WHERE PlayerName = '" + name + "';");

			res.next();

			if (res.getString("PlayerName") == null)
			{
				value = "0";
			} else
			{
				value = res.getString("stat");
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			value = "0";
		}
		return value;
	}

	/**
	 * Attempts to set Stats to MySQL (Untested)
	 * 
	 * @param name
	 * @param stat
	 * @param value
	 */
	private static void setMySQLStats(String name, String stat, int value) {
		Statement statement;
		try
		{
			statement = Main.c.createStatement();

			statement.executeUpdate("INSERT INTO Cranked (`PlayerName`, `" + stat + "`) VALUES ('" + name + "', " + value + ");");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
