
package me.sniperzciinema.infectedv2.Extras;

import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Arena.Arena;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class ScoreBoard {

	public static void updateScoreBoard() {

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard infectedBoard = manager.getNewScoreboard();

		Objective infectedList = infectedBoard.registerNewObjective("InfectedBoard", "dummy");
		infectedList.setDisplaySlot(DisplaySlot.SIDEBAR);

		if (Lobby.getGameState() == GameState.Started || Lobby.getGameState() == GameState.Infecting)
		{
			infectedList.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Teams");
			Score score = infectedList.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + ChatColor.ITALIC + "Humans:"));
			if (Lobby.getHumans().size() != 0)
				score.setScore(Lobby.getHumans().size());
			else
			{
				score.setScore(1);
				score.setScore(0);
			}
			Score score2 = infectedList.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + ChatColor.ITALIC + "Zombies:"));
			if (Lobby.getZombies().size() != 0)
				score2.setScore(Lobby.getZombies().size());
			else
			{
				score2.setScore(1);
				score2.setScore(0);
			}

		} else if (Lobby.getGameState() == GameState.InLobby || Lobby.getGameState() == GameState.Voting)
		{
			infectedList.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Vote for your map!");
			for (Arena arena : Lobby.getArenas())
			{
				if (Lobby.isArenaValid(arena))
				{
					Score score;
					if (arena == Lobby.getActiveArena())
						score = infectedList.getScore(Bukkit.getOfflinePlayer(ChatColor.BOLD + ">" + arena.getName()));
					else
						score = infectedList.getScore(Bukkit.getOfflinePlayer("" + ChatColor.YELLOW + ChatColor.ITALIC + arena.getName()));

					score.setScore(1);
					score.setScore(arena.getVotes());

				}
			}
		}
		for (Player u : Lobby.getInGame())
		{
			u.setScoreboard(infectedBoard);
		}

	}

}