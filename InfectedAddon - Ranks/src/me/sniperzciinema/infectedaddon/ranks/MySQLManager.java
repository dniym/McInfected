
package me.sniperzciinema.infectedaddon.ranks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.sniperzciinema.infected.Main;


public class MySQLManager {

	public static String getRank(String playerName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			ResultSet set = statement.executeQuery("SELECT " + "Rank" + " FROM " + "Infected_Ranks" + " WHERE Player = '" + playerName + "';");
			
			set.next();
			String rank = set.getString("Rank");
			set.close();
			return rank;
		} catch (SQLException e)
		{
			setRank(RankManager.defaultRank.getName(), playerName);
			return RankManager.defaultRank.getName();
		}
	}

	public static void update(String rank, String playerName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			statement.execute("UPDATE " + "Infected_Ranks" + " SET " + "Rank" + "=" + rank + " WHERE Player ='" + playerName + "';");
			statement.close();
		} catch (SQLException e)
		{
			setRank(rank, playerName);
		}
	}

	private static void setRank(String rank, String playerName) {
		try
		{
			Statement statement = Main.connection.createStatement();
			statement.execute("INSERT INTO " + "Infected_Ranks" + " (`Player`, `" + "Rank" + "`) VALUES ('" + playerName + "', '" + rank + "');");

			statement.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
