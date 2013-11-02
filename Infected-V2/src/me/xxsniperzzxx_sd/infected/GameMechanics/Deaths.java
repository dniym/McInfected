
package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;
import me.xxsniperzzxx_sd.infected.Messages.DeathMessages;
import me.xxsniperzzxx_sd.infected.Tools.Events;
import me.xxsniperzzxx_sd.infected.Tools.Files;

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

			for (Player u : Main.Lobby.getInGame())
				u.getPlayer().sendMessage(deathMessage);

			if (InfKilled.getTeam() == Team.Human)
			{
				InfKilled.respawn();
				InfKilled.Infect();

				if (Main.Lobby.getHumans().size() == 0 && Main.Lobby.getGameState() == GameState.Started)
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
				for (Player u : Main.Lobby.getInGame())
					u.sendMessage("<player> has a killstreak of " + KillStreak);

			if (Files.getKills().contains("Kill Streaks." + String.valueOf(KillStreak)))
			{
				String command = String.valueOf(Files.getKills().getString("Kill Streaks." + KillStreak)).replaceAll("<player>", p.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			IP.setPoints(IP.getPoints() + Main.Lobby.getActiveArena().getSettings().getPointsPer(Events.Kill), Main.config.getBoolean("Vault Support.Enabled"));
			IP.setScore(IP.getScore() + Main.Lobby.getActiveArena().getSettings().getScorePer(Events.Kill));

			for (Player u : Main.Lobby.getInGame())
			{
				if (Main.Lobby.isHuman(u))
				{
					IP.setPoints(IP.getPoints() + Main.Lobby.getActiveArena().getSettings().getPointsPer(Events.Survive), Main.config.getBoolean("Vault Support.Enabled"));
					IP.setScore(IP.getScore() + Main.Lobby.getActiveArena().getSettings().getScorePer(Events.Survive));
				} else
				{
					IP.setPoints(IP.getPoints() + Main.Lobby.getActiveArena().getSettings().getPointsPer(Events.Infected), Main.config.getBoolean("Vault Support.Enabled"));
					IP.setScore(IP.getScore() + Main.Lobby.getActiveArena().getSettings().getScorePer(Events.Infected));
				}

			}
		}
	}

}
