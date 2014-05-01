
package me.sniperzciinema.infected.GameMechanics;

import java.util.UUID;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.MySQLManager;


public class Stats {
	
	public enum StatType
	{
		kills, deaths, points, score, killstreak, time;
	};
	
	// Get the deaths from the location required
	public static int getDeaths(UUID uuid) {
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(uuid, "Deaths"));
		else
			return Files.getPlayers().getInt("Players." + uuid + ".Deaths");
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @return HighestKillStreak
	 */
	public static int getHighestKillStreak(UUID uuid) {
		
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(uuid, "HighestKillStreak"));
		else
			return Files.getPlayers().getInt("Players." + uuid + ".HighestKillStreak");
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @return Kills
	 */
	public static int getKills(UUID uuid) {
		
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(uuid, "Kills"));
		else
			return Files.getPlayers().getInt("Players." + uuid + ".Kills");
	}
	
	/**
	 * Gets the value of the stat for the player's uuid
	 * 
	 * @param uuid
	 * @param stat
	 * @return value
	 */
	private static Integer getMySQLStats(UUID uuid, String stat) {
		return MySQLManager.getInt("Infected", stat, uuid);
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @return PlayingTime
	 */
	public static int getPlayingTime(UUID uuid) {
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(uuid, "PlayingTime"));
		else
			return Files.getPlayers().getInt("Players." + uuid.toString() + ".PlayingTime");
	}
	
	/**
	 * Checks if we're going MySQL or Player.yml
	 * 
	 * @param uuid
	 * @return the players Score
	 */
	public static int getPoints(UUID uuid, boolean useVault) {
		
		if (useVault)
			return (int) Infected.economy.getBalance(uuid.toString());
		else if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(uuid, "Points"));
		else
			return Files.getPlayers().getInt("Players." + uuid + ".Points");
	}
	
	/**
	 * Checks if we're going MySQL or Player.yml
	 * 
	 * @param uuid
	 * @return the players Score
	 */
	public static int getScore(UUID uuid) {
		
		if (Settings.MySQLEnabled())
			return Integer.valueOf(getMySQLStats(uuid, "Score"));
		else
			return Files.getPlayers().getInt("Players." + uuid + ".Score");
	}
	
	/**
	 * From a StatType get the value for the player
	 * 
	 * @param type
	 *          - The StatType
	 * @param user
	 *          - The player
	 * @return the value
	 */
	public static int getStat(StatType type, UUID uuid) {
		if (type == StatType.kills)
			return getKills(uuid);
		else if (type == StatType.deaths)
			return getDeaths(uuid);
		else if (type == StatType.points)
			return getPoints(uuid, Settings.VaultEnabled());
		else if (type == StatType.score)
			return getScore(uuid);
		else if (type == StatType.killstreak)
			return getHighestKillStreak(uuid);
		else if (type == StatType.time)
			return getPlayingTime(uuid);
		else
			return 0;
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @param deaths
	 */
	
	public static void setDeaths(UUID uuid, Integer deaths) {
		
		if (Settings.MySQLEnabled())
			setMySQLStats(uuid, "Deaths", deaths);
		else
		{
			Files.getPlayers().set("Players." + uuid + ".Deaths", deaths);
			Files.savePlayers();
		}
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @param highestKillStreak
	 */
	
	public static void setHighestKillStreak(UUID uuid, Integer highestKillStreak) {
		
		if (Settings.MySQLEnabled())
			setMySQLStats(uuid, "HighestKillStreak", highestKillStreak);
		else
		{
			Files.getPlayers().set("Players." + uuid + ".HighestKillStreak", highestKillStreak);
			Files.savePlayers();
		}
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @param kills
	 */
	
	public static void setKills(UUID uuid, Integer kills) {
		
		if (Settings.MySQLEnabled())
			setMySQLStats(uuid, "Kills", kills);
		else
		{
			Files.getPlayers().set("Players." + uuid + ".Kills", kills);
			Files.savePlayers();
		}
	}
	
	/**
	 * Sets the value of the stat to the player's uuid
	 * 
	 * @param uuid
	 * @param stat
	 * @param value
	 */
	private static void setMySQLStats(UUID uuid, String stat, int value) {
		
		MySQLManager.update("Infected", stat, value, uuid);
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @param PlayingTime
	 */
	
	public static void setPlayingTime(UUID uuid, long l) {
		
		if (Settings.MySQLEnabled())
			setMySQLStats(uuid, "PlayingTime", (int) l);
		else
		{
			Files.getPlayers().set("Players." + uuid + ".PlayingTime", l);
			Files.savePlayers();
		}
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @param points
	 */
	public static void setPoints(UUID uuid, Integer points, boolean useVault) {
		
		if (useVault)
		{
			int cPoints = Stats.getPoints(uuid, useVault);
			if (cPoints > points)
			{
				int price = cPoints - points;
				Infected.economy.withdrawPlayer(uuid.toString(), price);
			}
			else
			{
				int depo = points - cPoints;
				Infected.economy.depositPlayer(uuid.toString(), depo);
			}
		}
		else if (Settings.MySQLEnabled())
			setMySQLStats(uuid, "Points", points);
		else
		{
			Files.getPlayers().set("Players." + uuid + ".Points", points);
			Files.savePlayers();
		}
	}
	
	/**
	 * Checks if we're setting MySQL or Player.yml
	 * 
	 * @param uuid
	 * @param score
	 */
	public static void setScore(UUID uuid, Integer score) {
		
		if (Settings.MySQLEnabled())
			setMySQLStats(uuid, "Score", score);
		else
		{
			Files.getPlayers().set("Players." + uuid + ".Score", score);
			Files.savePlayers();
		}
	}
}
