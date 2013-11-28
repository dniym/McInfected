
package me.sniperzciinema.infectedv2.Extras;

import java.util.List;

import me.sniperzciinema.infectedv2.Messages.ScoreBoardVariables;
import me.sniperzciinema.infectedv2.Tools.Settings;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Arena.Arena;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class ScoreBoard {

	InfPlayer ip;

	public ScoreBoard(InfPlayer ip)
	{
		this.ip = ip;
	}

	public enum ScoreBoards{Regular, Stats};
	private ScoreBoards showing = ScoreBoards.Regular;
	
	/**
	 * 
	 * @return the scoreboard theyre seeing
	 */
	public ScoreBoards getShowing(){
		return showing;
	}
	/**
	 * Toggles the scoreboard they're seeing
	 */
	public void switchShowing(){
		if(getShowing() == ScoreBoards.Regular)
			showing = ScoreBoards.Stats;
		else
			showing = ScoreBoards.Regular;
	}
	/**
	 * Shows proper scoreboard for what they're SUPPOSED to see
	 */
	public void showProperBoard(){
		if(showing == ScoreBoards.Regular)
			showRegular();
		else
			showStats();
	}
	
	/**
	 * Force seeing regular
	 */
	public void showRegular(){
		showing = ScoreBoards.Regular;
		Player player = ip.getPlayer();

		// Make sure the player is in an arena before setting
		// If they aren't clear their scoreboard, because they just left
		if (!ip.isInGame())
		{
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		} else
		{
			// Create a new scoreboard
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sb = manager.getNewScoreboard();
			Objective ob = sb.registerNewObjective("Infected", "dummy");
			ob.setDisplaySlot(DisplaySlot.SIDEBAR);

			// Now set all the scores and the title
			ob.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Rankings");


			if (Lobby.getGameState() == GameState.Started || Lobby.getGameState() == GameState.Infecting)
			{
				ob.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Teams");
				Score score = ob.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + ChatColor.ITALIC + "Humans:"));
				if (Lobby.getHumans().size() != 0)
					score.setScore(Lobby.getHumans().size());
				else
				{
					score.setScore(1);
					score.setScore(0);
				}
				Score score2 = ob.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "" + ChatColor.ITALIC + "Zombies:"));
				if (Lobby.getZombies().size() != 0)
					score2.setScore(Lobby.getZombies().size());
				else
				{
					score2.setScore(1);
					score2.setScore(0);
				}

			} else if (Lobby.getGameState() == GameState.InLobby || Lobby.getGameState() == GameState.Voting)
			{
				ob.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Vote for your map!");
				for (Arena arena : Lobby.getArenas())
				{
					if (Lobby.isArenaValid(arena))
					{
						Score score;
						if (arena == Lobby.getActiveArena())
							score = ob.getScore(Bukkit.getOfflinePlayer(ChatColor.BOLD + ">" + arena.getName()));
						else
							score = ob.getScore(Bukkit.getOfflinePlayer("" + ChatColor.YELLOW + ChatColor.ITALIC + arena.getName()));

						score.setScore(1);
						score.setScore(arena.getVotes());

					}
				}
			}
			
			player.setScoreboard(sb);
		}
	}
	
	/**
	 * Force showing stats
	 */
	public void showStats() {
		showing = ScoreBoards.Stats;
		Player player = ip.getPlayer();

		// Make sure the player is in an arena before setting
		// If they aren't clear their scoreboard, because they just left
		if (!ip.isInGame())
		{
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		} else
		{
			// Create a new scoreboard
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard sb = manager.getNewScoreboard();
			Objective ob = sb.registerNewObjective("Infected", "dummy");
			ob.setDisplaySlot(DisplaySlot.SIDEBAR);

			// Now set all the scores and the title
			ob.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Stats");


			int row = 0;
			int spaces = 0;

			List<String> list = Settings.getScoreBoardRows();

			for (@SuppressWarnings("unused")
			// loop through all the list
			String s : list)
			{
				Score score = null;

				// Get the string my using the row
				String line = list.get(row);

				// If the line is just a space, set the offline player to a
				// color code
				// This way it'll show as a blank line, and not be merged with
				// similar color codes
				if (ScoreBoardVariables.getLine(line, player).equalsIgnoreCase(" "))
				{
					String space = "&" + spaces;
					spaces++;
					score = ob.getScore(Bukkit.getOfflinePlayer(ScoreBoardVariables.getLine(space, player)));
				} else
				{
					// If its just a regular message, just set it
					score = ob.getScore(Bukkit.getOfflinePlayer(ScoreBoardVariables.getLine(line, player)));

				}
				score.setScore(list.size() - 1 - row);
				row++;
			}
			
			player.setScoreboard(sb);
		}
	}
}
