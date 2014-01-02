
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

	public static void addRank(Rank rank) {
		ranks.add(rank);
	}

	public static Rank getRank(String rankName) {
		for (Rank rank : ranks)
		{
			if (rank.getName().equals(rankName))
				return rank;
		}
		return null;
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
		Rank nextrank = new Rank("", "", false, false,
				maxRank.getScoreNeeded() + 1, null, null, null);
		if (rank.isMaxRank())
			nextrank = null;
		else
		{
			for (Rank r : ranks)
			{
				// If r has a bigger score then the current rank and if r has a
				// smaller score then the other rank
				if (r.getScoreNeeded() > rank.getScoreNeeded() && r.getScoreNeeded() < nextrank.getScoreNeeded())
					nextrank = r;
			}
		}
		return nextrank;
	}

	public static Rank getLastRank(Player p) {
		Rank rank = getPlayersRank(p);
		Rank lastrank = new Rank("", "", false, false,
				defaultRank.getScoreNeeded() + 1, null, null, null);
		if (rank.isDefaultRank())
			lastrank = null;
		else
		{
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
		if (!getPlayersRank(p).isMaxRank() && InfPlayerManager.getInfPlayer(p).getScore() >= getNextRank(p).getScoreNeeded())
			return true;
		else
			return false;
	}

	public static boolean canRankDown(Player p) {
		if (!getPlayersRank(p).isDefaultRank() && InfPlayerManager.getInfPlayer(p).getScore() < getLastRank(p).getScoreNeeded())
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
}
