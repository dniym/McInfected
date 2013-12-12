package me.sniperzciinema.infected.GameMechanics;

import me.sniperzciinema.infected.Enums.Events;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class KillStreaks {

	public static void handle(boolean killed, Player p) {
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
				ItemStack is = IP.getInfClass(IP.getTeam()).getKillstreaks().get(IP.getKillstreak());
				p.sendMessage(Msgs.Game_KillStreak_Reward.getString("<item>", is.getItemMeta().getDisplayName() != null ? is.getItemMeta().getDisplayName() : StringUtil.getWord(is.getType().name())));
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
