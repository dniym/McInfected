
package me.sniperzciinema.infected.Messages;

import java.util.List;
import java.util.Random;

import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Tools.Files;

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
		
		String msg = getKill(team, death);

		// Replace color codes, and killer and killed names
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if (killer != null)
			msg = msg.replaceAll("<killer>", killer.getName() + "(" + Stats.getScore(killer.getName()) + ")");
		if (killed != null)
			msg = msg.replaceAll("<killed>", killed.getName() + "(" + Stats.getScore(killed.getName()) + ")");
		return Msgs.Format_Prefix.getString() + msg;
	}

	private static String getKill(Team team, DeathType death){
		String message;
		List<String> list;
		if(death == DeathType.Other)
		list = Files.getMessages().getStringList("Suicides." + team.toString());
			else
		list =  Files.getMessages().getStringList("Kills." + team.toString() + "." + death.toString());
		Random r = new Random();
		int i = r.nextInt(list.size());
		message = list.get(i);
		return message;
	}
}
