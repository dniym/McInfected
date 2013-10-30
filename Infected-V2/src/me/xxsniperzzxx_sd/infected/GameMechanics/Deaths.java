
package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.OldStats.MiscStats;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;
import me.xxsniperzzxx_sd.infected.Messages.DeathMessages;

import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Deaths {

	// TODO: Readd death event
	private static InfPlayerManager IPM = Main.InfPlayerManager;

	public static void playerDies(DeathType death, Player killer, Player killed) {
		ScoreBoard.updateScoreBoard();

		String deathMessage = DeathMessages.getDeathMessage(killer, killed, death);

		InfPlayer InfKiller = null;
		InfPlayer InfKilled = null;
		if (killer != null)
		{
			InfKiller = IPM.getInfPlayer(killer);
			InfKiller.updateStats(1, 0);
			MiscStats.handleKillStreaks(false, killer);

		}

		if (killed != null)
		{
			InfKilled = IPM.getInfPlayer(killed);
			InfKilled.updateStats(0, 1);

			MiscStats.handleKillStreaks(true, killed);

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

}
