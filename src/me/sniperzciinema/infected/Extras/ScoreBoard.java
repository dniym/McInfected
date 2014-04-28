
package me.sniperzciinema.infected.Extras;

import java.util.List;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Messages.RandomChatColor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


public class ScoreBoard {

	public enum ScoreBoards
	{
		Regular, Stats
	}

	InfPlayer			ip;

	private ScoreBoards	showing	= ScoreBoards.Regular;	;

	public ScoreBoard(InfPlayer ip)
	{
		this.ip = ip;
	}

	/**
	 * @return the scoreboard theyre seeing
	 */
	public ScoreBoards getShowing() {
		return this.showing;
	}

	/**
	 * Shows proper scoreboard for what they're SUPPOSED to see(Also used to
	 * update a scoreboard)
	 */
	public void showProperBoard() {
		if (this.showing == ScoreBoards.Regular)
			showRegular();
		else
			showStats();
	}

	/**
	 * Force seeing the regular scoreboard(Votes, Players)
	 */
	@SuppressWarnings("deprecation")
	public void showRegular() {
		this.showing = ScoreBoards.Regular;
		Player player = this.ip.getPlayer();

		// Create a new scoreboard
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sb = manager.getNewScoreboard();
		Objective ob = sb.registerNewObjective("Infected", "dummy");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);

		// Now set all the scores and the title
		ob.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Rankings");

		if ((Lobby.getGameState() == GameState.Started) || (Lobby.getGameState() == GameState.Infecting))
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

		}
		else
			if ((Lobby.getGameState() == GameState.InLobby) || (Lobby.getGameState() == GameState.Voting))
			{
				ob.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Vote For An Arena!");
				int i = 1;
				for (Arena arena : Lobby.getArenas())
					if (Lobby.isArenaValid(arena))
					{
						Score score;
						if (arena == Lobby.getActiveArena())
							score = ob.getScore(Bukkit.getOfflinePlayer("" + RandomChatColor.getColor(ChatColor.AQUA, ChatColor.GOLD, ChatColor.RED, ChatColor.LIGHT_PURPLE) + ChatColor.BOLD + ">" + arena.getName().substring(0, Math.min(11, arena.getName().length()))));
						else
							score = ob.getScore(Bukkit.getOfflinePlayer("" + ChatColor.YELLOW + ChatColor.ITALIC + arena.getName().substring(0, Math.min(12, arena.getName().length()))));
						if (i > 15)
							for (OfflinePlayer op : sb.getPlayers())
								if (ob.getScore(op).getScore() == 0)
								{
									sb.resetScores(op);
									break;

								}
						score.setScore(1);
						score.setScore(arena.getVotes());
						i++;
					}
			}

		player.setScoreboard(sb);
	}

	/**
	 * Force showing the stats scoreboard (Layout is set in the config (Mainly
	 * because i was to lazy to make it myself...))
	 */
	@SuppressWarnings("deprecation")
	public void showStats() {
		this.showing = ScoreBoards.Stats;
		Player player = this.ip.getPlayer();

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
			if (ScoreBoardVariables.getLine(line, this.ip).equalsIgnoreCase(" "))
			{
				String space = "&" + spaces;
				spaces++;
				score = ob.getScore(Bukkit.getOfflinePlayer(ScoreBoardVariables.getLine(space, this.ip)));
			}
			else
				// If its just a regular message, just set it
				score = ob.getScore(Bukkit.getOfflinePlayer(ScoreBoardVariables.getLine(line, this.ip)));
			score.setScore(list.size() - 1 - row);
			row++;
		}

		player.setScoreboard(sb);
	}

	/**
	 * Toggles the scoreboard they're seeing
	 */
	public void switchShowing() {
		if (getShowing() == ScoreBoards.Regular)
			this.showing = ScoreBoards.Stats;
		else
			this.showing = ScoreBoards.Regular;
	}
}


class ScoreBoardVariables {

	/**
	 * Replace the regular line of text with the one that is all fancy
	 * 
	 * @param string
	 *            - The line of text
	 * @param user
	 *            - the player who will see this line
	 * @return The new line with the variables replaced and color added
	 */
	public static String getLine(String string, InfPlayer ip) {

		String newString = string;
		// Replace all variables we need

		newString = newString.replaceAll("<kills>", String.valueOf(ip.getKills()));
		newString = newString.replaceAll("<deaths>", String.valueOf(ip.getDeaths()));
		newString = newString.replaceAll("<highestkillstreak>", String.valueOf(ip.getHighestKillStreak()));
		newString = newString.replaceAll("<points>", String.valueOf(ip.getPoints(Settings.VaultEnabled())));
		newString = newString.replaceAll("<score>", String.valueOf(ip.getScore()));
		newString = newString.replaceAll("<players>", String.valueOf(ip.getScore()));

		// Replace color codes

		newString = newString.replaceAll("&x", RandomChatColor.getColor().toString());
		newString = newString.replaceAll("&y", RandomChatColor.getFormat().toString());
		newString = ChatColor.translateAlternateColorCodes('&', newString);

		// Make sure string isnt to long
		if (newString.length() > 16)
			newString = newString.substring(0, Math.min(newString.length(), 16));

		return newString;
	}
}
