
package me.sniperzciinema.infected.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import me.sniperzciinema.infected.Infected;


public class MySQLManager {
	
	/**
	 * @param tableName
	 *          - The tables name
	 * @param columnName
	 *          - The stats name
	 * @param uuid
	 * @return the players stats
	 */
	public static int getInt(String tableName, String columnName, UUID uuid) {
		try
		{
			Statement statement = Infected.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE UUID = '" + uuid.toString() + "';");
			int i = 0;
			set.next();
			i = set.getInt(columnName);
			set.close();
			return i;
		}
		catch (SQLException e)
		{
			setInt(tableName, columnName, 0, uuid);
			return 0;
		}
	}
	
	/**
	 * @param tableName
	 *          - The tables name
	 * @return All the players in the Infected table
	 */
	public static ArrayList<UUID> getPlayers(String tableName) {
		try
		{
			Statement statement = Infected.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT * FROM `" + tableName + "` ");
			ArrayList<UUID> players = new ArrayList<UUID>();
			while (true)
			{
				set.next();
				players.add(UUID.fromString(set.getString("UUID")));
				if (set.isLast())
					break;
			}
			set.close();
			return players;
		}
		catch (SQLException e)
		{
			ArrayList<UUID> nope = new ArrayList<UUID>();
			return nope;
		}
	}
	
	/**
	 * Force the setting of the players value
	 * 
	 * @param tableName
	 *          - The tables name
	 * @param columnName
	 *          - The stats name
	 * @param value
	 * @param playerName
	 */
	private static void setInt(String tableName, String columnName, int value, UUID uuid) {
		try
		{
			Statement statement = Infected.connection.createStatement();
			statement.execute("INSERT INTO " + tableName + " (`UUID`, `" + columnName + "`) VALUES ('" + uuid + "', '" + value + "');");
			
			statement.close();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Safely update/set the value
	 * Will set the value only if the table doesn't have the player already,
	 * otherwise it'll just update the players values
	 * 
	 * @param tableName
	 *          - The tables name
	 * @param columnName
	 *          - The stats name
	 * @param value
	 * @param playerName
	 */
	public static void update(String tableName, String columnName, int value, UUID uuid) {
		try
		{
			Statement statement = Infected.connection.createStatement();
			statement.execute("UPDATE " + tableName + " SET " + columnName + "=" + value + " WHERE UUID ='" + uuid + "';");
			statement.close();
		}
		catch (SQLException e)
		{
			setInt(tableName, columnName, value, uuid);
		}
	}
}
