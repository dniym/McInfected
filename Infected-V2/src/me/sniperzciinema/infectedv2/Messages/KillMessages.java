
package me.sniperzciinema.infectedv2.Messages;

import me.sniperzciinema.infectedv2.GameMechanics.DeathType;
import me.sniperzciinema.infectedv2.GameMechanics.Stats;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class KillMessages {

	public static String getKillMessage(Player killer, Player killed, DeathType death) {

		InfPlayer IP = null;
		Team team = Team.Zombie;
		if (killer != null)
		{
			IP = InfPlayerManager.getInfPlayer(killer);
			team = IP.getTeam();
		} else
		{
			if (InfPlayerManager.getInfPlayer(killed).getTeam() == Team.Human)
				team = Team.Zombie;
			else
				team = Team.Human;
		}
		
		String msg = Msgs.Kill.getKill(team, death);

		// Replace color codes, and killer and killed names
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if (killer != null)
			msg = msg.replaceAll("<killer>", killer.getName() + "(" + Stats.getScore(killer.getName()) + ")");
		if (killed != null)
			msg = msg.replaceAll("<killed>", killed.getName() + "(" + Stats.getScore(killed.getName()) + ")");
		return Msgs.Format_Prefix.getString() + msg;
	}
}
