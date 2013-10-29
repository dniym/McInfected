
package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.OldStats.MiscStats;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;
import me.xxsniperzzxx_sd.infected.Messages.DeathMessages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Deaths {

	public static void playerDies(DeathType death, Player killer, Player killed) {
		ScoreBoard.updateScoreBoard();

		String deathMessage = DeathMessages.getDeathMessage(killer, killed, death);

		InfectedPlayerDieEvent dieEvent = new InfectedPlayerDieEvent(killer,
				killed, Infected.isPlayerHuman(killed) ? true : false, death,
				deathMessage);
		Bukkit.getServer().getPluginManager().callEvent(dieEvent);
		if (!dieEvent.isCancelled())
		{
			InfPlayer InfKiller = InfPlayerManager.getPlayer(killer);
			InfPlayer InfKilled = InfPlayerManager.getPlayer(killed);

			InfKiller.updateStats(1, 0);
			InfKilled.updateStats(0, 1);
			
			MiscStats.handleKillStreaks(true, killed);
			MiscStats.handleKillStreaks(false, killer);
			
			for(InfPlayer IP : Main.Lobby.getPlayers())
					IP.getPlayer().sendMessage(dieEvent.getDeathMsg());

			if (InfKilled.getTeam() == Team.Human)
			{	
				Zombify.zombifyPlayer(killed);

				InfKilled.respawn();
				InfKilled.Infect();
			} else
			{
				killed.playSound(killed.getLocation(), Sound.ZOMBIE_PIG_DEATH, 1, 1);
				InfKilled.respawn();
				Equip.equip(killed);
			}
			if (Main.humans.size() == 0 && Main.Lobby.getGameState() == GameState.Started)
				Game.endGame(false);

			else
			{
				InfKilled.respawn();
				Equip.equip(killed);
				Zombify.zombifyPlayer(killed);
			}
		}
	}

}
