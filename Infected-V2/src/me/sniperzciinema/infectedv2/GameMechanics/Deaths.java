
package me.sniperzciinema.infectedv2.GameMechanics;

import me.sniperzciinema.infectedv2.Game;
import me.sniperzciinema.infectedv2.Extras.ScoreBoard;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;
import me.sniperzciinema.infectedv2.Messages.DeathMessages;
import me.sniperzciinema.infectedv2.Tools.Events;
import me.sniperzciinema.infectedv2.Tools.Files;
import me.sniperzciinema.infectedv2.Tools.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Deaths {

	// TODO: Readd death event

	public static void playerDies(DeathType death, Player killer, Player killed) {
		ScoreBoard.updateScoreBoard();

		String deathMessage = DeathMessages.getDeathMessage(killer, killed, death);

		InfPlayer InfKiller = null;
		InfPlayer InfKilled = null;
		if (killer != null)
		{
			InfKiller = InfPlayerManager.getInfPlayer(killer);
			InfKiller.updateStats(1, 0);
			handleKillStreaks(false, killer);

		}

		if (killed != null)
		{
			InfKilled = InfPlayerManager.getInfPlayer(killed);
			InfKilled.updateStats(0, 1);

			handleKillStreaks(true, killed);

			for (Player u : Lobby.getInGame())
				u.getPlayer().sendMessage(deathMessage);

			if (InfKilled.getTeam() == Team.Human)
			{
				InfKilled.respawn();
				InfKilled.Infect();

				if (Lobby.getHumans().size() == 0 && Lobby.getGameState() == GameState.Started)
					Game.endGame(false);

			} else
			{
				killed.playSound(killed.getLocation(), Sound.ZOMBIE_PIG_DEATH, 1, 1);
				InfKilled.respawn();
				Equip.equip(killed);
			}

		}
	}

	public static void handleKillStreaks(boolean killed, Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		if (killed)
		{
			if (IP.getKillstreak() > Stats.getHighestKillStreak(p.getName()))
				Stats.setHighestKillStreak(p.getName(), IP.getKillstreak());

		} else
		{
			IP.setKillstreak(IP.getKillstreak() + 1);
			int KillStreak = IP.getKillstreak();
			if (KillStreak >= 3)
				for (Player u : Lobby.getInGame())
					u.sendMessage("<player> has a killstreak of " + KillStreak);

			if (Files.getKills().contains("Kill Streaks." + String.valueOf(KillStreak)))
			{
				String command = String.valueOf(Files.getKills().getString("Kill Streaks." + KillStreak)).replaceAll("<player>", p.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			IP.setPoints(IP.getPoints() + Lobby.getActiveArena().getSettings().getPointsPer(Events.Kill), Settings.VaultEnabled());
			IP.setScore(IP.getScore() + Lobby.getActiveArena().getSettings().getScorePer(Events.Kill));

			for (Player u : Lobby.getInGame())
			{
				if (Lobby.isHuman(u))
				{
					IP.setPoints(IP.getPoints() + Lobby.getActiveArena().getSettings().getPointsPer(Events.Survive), Settings.VaultEnabled());
					IP.setScore(IP.getScore() + Lobby.getActiveArena().getSettings().getScorePer(Events.Survive));
				} else
				{
					IP.setPoints(IP.getPoints() + Lobby.getActiveArena().getSettings().getPointsPer(Events.Infected), Settings.VaultEnabled());
					IP.setScore(IP.getScore() + Lobby.getActiveArena().getSettings().getScorePer(Events.Infected));
				}

			}
		}
	}

}
