
package me.sniperzciinema.infected.GameMechanics;

import me.sniperzciinema.infected.Game;
import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.KillMessages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Deaths {

	// TODO: Readd death event

	public static void playerDies(DeathType death, Player killer, Player killed) {
		
		for(Player u : Lobby.getInGame()){
			InfPlayer up = InfPlayerManager.getInfPlayer(u);
			up.getScoreBoard().showProperBoard();
		}

		String killMessage = KillMessages.getKillMessage(killer, killed, death);
		
		for (Player u : Lobby.getInGame())
			u.sendMessage(killMessage);

		InfPlayer InfKiller = null;
		InfPlayer InfKilled = null;
		if (killer != null)
		{
			InfKiller = InfPlayerManager.getInfPlayer(killer);
			InfKiller.updateStats(1, 0);
			KillStreaks.handle(false, killer);

		}

		if (killed != null)
		{
			InfKilled = InfPlayerManager.getInfPlayer(killed);
			InfKilled.updateStats(0, 1);

			KillStreaks.handle(true, killed);

			if (InfKilled.getTeam() == Team.Human)
			{
				
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

}
