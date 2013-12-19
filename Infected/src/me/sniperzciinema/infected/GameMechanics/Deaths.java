
package me.sniperzciinema.infected.GameMechanics;

import me.sniperzciinema.infected.Game;
import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.Events.InfectedDeathEvent;
import me.sniperzciinema.infected.Extras.Pictures;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.KillMessages;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class Deaths {

	public static void playerDies(DeathType death, Player killer, Player killed) {

		InfectedDeathEvent e = new InfectedDeathEvent(killer, killed, death);
		Bukkit.getPluginManager().callEvent(e);

		if (death == DeathType.Other && InfPlayerManager.getInfPlayer(killed).getTeam() == Team.Zombie)
		{
		} else
		{
			String killMessage = KillMessages.getKillMessage(killer, killed, death, true);

			if (Settings.PictureEnabled() && Lobby.isHuman(killed) && Lobby.getHumans().size() > 1)
			{
				killMessage = KillMessages.getKillMessage(killer, killed, death, false);
				String[] face = Pictures.getZombie();
				face[2] = face[2] + "     " + Msgs.Picture_Infected_You.getString();
				face[3] = face[3] + "     " + Msgs.Picture_Infected_To_Win.getString();

				killed.sendMessage(face);

				for (Player u : Lobby.getInGame())
					if (u != killed)
						u.sendMessage(Msgs.Format_Prefix.getString() + killMessage);
			} else
				for (Player u : Lobby.getInGame())
					u.sendMessage(killMessage);

		}
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

		for (Player u : Lobby.getInGame())
		{
			InfPlayer up = InfPlayerManager.getInfPlayer(u);
			up.getScoreBoard().showProperBoard();
		}

	}

}
