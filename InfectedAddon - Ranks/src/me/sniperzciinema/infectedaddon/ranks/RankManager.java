
package me.sniperzciinema.infectedaddon.ranks;

import java.util.ArrayList;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.entity.Player;


public class RankManager {

	private static ArrayList<Rank> ranks = new ArrayList<Rank>();
	public static Rank defaultRank;
	public static Rank maxRank;

	public static ArrayList<Rank> getRanks(){
		return ranks;
	}
	public static void addRank(Rank rank) {
		ranks.add(rank);
	}

	public static boolean isRank(String rankName){
		for (Rank rank : ranks)
		{
			if (rank.getName().equals(rankName))
				return true;
		}
		return false;
	}
	
	public static Rank getRank(String rankName) {
		for (Rank rank : ranks)
		{
			if (rank.getName().equals(rankName))
				return rank;
		}
		return defaultRank;
	}

	public static Rank getPlayersRank(Player p) {
		if (Settings.MySQLEnabled())
		{
			return getRank(MySQLManager.getRank(p.getName()));
		} else
		{
			if (Files.getPlayers().getString("Players." + p.getName().toLowerCase() + ".Rank") != null)
			{
				return getRank(Files.getPlayers().getString("Players." + p.getName().toLowerCase() + ".Rank"));
			} else
				return defaultRank;
		}
	}

	public static Rank getPlayersRank(String name) {
		if (Settings.MySQLEnabled())
		{
			return getRank(MySQLManager.getRank(name));
		} else
		{
			if (Files.getPlayers().getString("Players." + name.toLowerCase() + ".Rank") != null)
			{
				return getRank(Files.getPlayers().getString("Players." + name.toLowerCase() + ".Rank"));
			} else
				return defaultRank;
		}

	}

	public static void getPresets() {
		for (Rank rank : ranks)
		{
			if (rank.isDefaultRank())
				defaultRank = rank;
			if (rank.isMaxRank())
				maxRank = rank;
		}
	}

	public static Rank getNextRank(Player p) {
		Rank rank = getPlayersRank(p);
		Rank nextrank = maxRank;
		if (!rank.isMaxRank())
		{
			if(rank.getScoreNeeded() == -1)
				return rank;
			
			for (Rank r : ranks)
			{
				// If r has a bigger score then the current rank and if r has a
				// smaller score then the other rank
				if (r.getScoreNeeded() > rank.getScoreNeeded() && r.getScoreNeeded() < nextrank.getScoreNeeded())
				{
					nextrank = r;
				}
			}
		}
		return nextrank;
	}

	public static Rank getLastRank(Player p) {
		Rank rank = getPlayersRank(p);
		Rank lastrank = defaultRank;
		if (!rank.isDefaultRank())
		{
			if(rank.getScoreNeeded() == -1)
				return rank;
			
			for (Rank r : ranks)
			{
				// If r has a smaller score then the current rank and if r has a
				// bigger score then the other rank
				if (r.getScoreNeeded() < rank.getScoreNeeded() && r.getScoreNeeded() > lastrank.getScoreNeeded())
					lastrank = r;
			}
		}
		return lastrank;
	}

	public static boolean canRankUp(Player p) {
		//If the player isn't max rank and the player score is greater or equal to the next rank
		if (!getPlayersRank(p).isMaxRank() && getPlayersRank(p).getScoreNeeded()!= -1 && InfPlayerManager.getInfPlayer(p).getScore() >= getNextRank(p).getScoreNeeded())
			return true;
		else
			return false;
	}

	public static boolean canRankDown(Player p) {
		//If the players rank isn't default and the players score is less then their current ranks score
		if (!getPlayersRank(p).isDefaultRank() && getPlayersRank(p).getScoreNeeded()!= -1 && InfPlayerManager.getInfPlayer(p).getScore() < getPlayersRank(p).getScoreNeeded())
			return true;
		else
			return false;
	}

	public static void setPlayersRank(Player p, Rank rank) {
		if (Settings.MySQLEnabled())
		{
			MySQLManager.update(rank.getName(), p.getName());
		} else
		{
			Files.getPlayers().set("Players." + p.getName().toLowerCase() + ".Rank", rank.getName());
			Files.savePlayers();
		}
	}
	public static void setNamesRank(String name, Rank rank) {
		if (Settings.MySQLEnabled())
		{
			MySQLManager.update(rank.getName(), name);
		} else
		{
			Files.getPlayers().set("Players." + name.toLowerCase() + ".Rank", rank.getName());
			Files.savePlayers();
		}
	}
}
