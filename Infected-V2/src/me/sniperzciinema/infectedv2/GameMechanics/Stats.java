
package me.sniperzciinema.infectedv2.GameMechanics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.sniperzciinema.infectedv2.Main;
import me.sniperzciinema.infectedv2.Tools.Files;


public class Stats {

	public enum StatType{
		kills, deaths, points, score, killstreak, playingtime;
	};
	public static int getStat(StatType type, String user){
		if(type == StatType.kills)
			return getKills(user);
		else if(type == StatType.deaths)
			return getDeaths(user);
		else if(type == StatType.points)
			return getPoints(user);
		else if(type == StatType.score)
			return getScore(user);
		else if(type == StatType.killstreak)
			return getHighestKillStreak(user);
		else if(type == StatType.playingtime)
			return getPlayingTime(user);
		else return 0;
	}
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
	 * @return PlayingTime
	 */
	public static int getPlayingTime(String name) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
			return Integer.valueOf(getMySQLStats(name, "PlayingTime"));
		else
			return Files.getPlayers().getInt("Players." + name + ".PlayingTime");
	}

	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param name
	 * @param PlayingTime
	 */

	public static void setPlayingTime(String name, long l) {
		if (Files.getConfig().getBoolean("MySQL.Enable"))
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
	public static void setPoints(String name, Integer points, boolean useVault) {
		if (useVault)
		{
			int cPoints = Stats.getPoints(name);
			if (cPoints > points)
			{
				int price = cPoints - points;
				Main.economy.withdrawPlayer(name, price);
			}
			else{
				int depo = points;
				Main.economy.depositPlayer(name, depo);
			}
		}
		else if (Files.getConfig().getBoolean("MySQL.Enable"))
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
