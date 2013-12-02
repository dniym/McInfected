
package me.sniperzciinema.infectedv2.GameMechanics;

import me.sniperzciinema.infectedv2.Game;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;
import me.sniperzciinema.infectedv2.Messages.KillMessages;
import me.sniperzciinema.infectedv2.Messages.Msgs;
import me.sniperzciinema.infectedv2.Tools.Events;
import me.sniperzciinema.infectedv2.Tools.Settings;

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
			handleKillStreaks(false, killer);

		}

		if (killed != null)
		{
			InfKilled = InfPlayerManager.getInfPlayer(killed);
			InfKilled.updateStats(0, 1);

			handleKillStreaks(true, killed);

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
					u.sendMessage(Msgs.Game_KillStreak_Value.getString("<player>", p.getName(), "<killstreak>", String.valueOf(KillStreak)));

			if (IP.getInfClass(IP.getTeam()).getKillstreaks().containsKey(IP.getKillstreak())){
				p.getInventory().addItem(IP.getInfClass(IP.getTeam()).getKillstreaks().get(IP.getKillstreak()));
				p.sendMessage(Msgs.Game_KillStreak_Reward.getString("<item>", IP.getInfClass(IP.getTeam()).getKillstreaks().get(IP.getKillstreak()).getItemMeta().getDisplayName()));
			}
			IP.setPoints(IP.getPoints(Settings.VaultEnabled()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.Kill), Settings.VaultEnabled());
			IP.setScore(IP.getScore() + Lobby.getActiveArena().getSettings().getScorePer(Events.Kill));

			for (Player u : Lobby.getInGame())
			{
				if (Lobby.isHuman(u))
				{
					IP.setPoints(IP.getPoints(Settings.VaultEnabled()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.Survive), Settings.VaultEnabled());
					IP.setScore(IP.getScore() + Lobby.getActiveArena().getSettings().getScorePer(Events.Survive));
				} else
				{
					IP.setPoints(IP.getPoints(Settings.VaultEnabled()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.Infected), Settings.VaultEnabled());
					IP.setScore(IP.getScore() + Lobby.getActiveArena().getSettings().getScorePer(Events.Infected));
				}

			}
		}
	}

}
