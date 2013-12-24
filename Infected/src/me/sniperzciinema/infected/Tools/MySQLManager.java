
package me.sniperzciinema.infected.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.sniperzciinema.infected.Main;


public class MySQLManager {

	public static int getInt(String tableName, String columnName, String playerName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT " + columnName + " FROM " + tableName + " WHERE Player = '" + playerName + "';");
			int i = 0;
			set.next();
			i = set.getInt(columnName);
			set.close();
			return i;
		} catch (SQLException e)
		{
			setInt(tableName, columnName, 0, playerName);
			return 0;
		}
	}

	public static void update(String tableName, String columnName, int value, String playerName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			statement.execute("UPDATE " + tableName + " SET " + columnName + "=" + value + " WHERE Player ='" + playerName + "';");
			statement.close();
		} catch (SQLException e)
		{
			setInt(tableName, columnName, value, playerName);
		}
	}

	private static void setInt(String tableName, String columnName, int value, String playerName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			statement.execute("INSERT INTO " + tableName + " (`Player`, `" + columnName + "`) VALUES ('" + playerName + "', '" + value + "');");

			statement.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getPlayers(String tableName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT * FROM `infected` ");
			ArrayList<String> players = new ArrayList<String>();
			while (true)
			{
				set.next();
				players.add(set.getString("Player"));
				if (set.isLast())
					break;
			}
			set.close();
			return players;
		} catch (SQLException e)
		{
			ArrayList<String> nope = new ArrayList<String>();
			return nope;
		}
	}
}
