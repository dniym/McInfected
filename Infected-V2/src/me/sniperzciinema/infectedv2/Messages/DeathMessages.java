
package me.sniperzciinema.infectedv2.Messages;

import java.util.Random;

import me.sniperzciinema.infectedv2.GameMechanics.DeathType;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class DeathMessages {

	
	public static String getDeathMessage(Player killer, Player killed, DeathType death) {

		InfPlayer IP = null;
		Team team = null;
		if(killer != null){
			IP = InfPlayerManager.getInfPlayer(killer);
			team = IP.getTeam();
		}else{
			if(InfPlayerManager.getInfPlayer(killed).getTeam() == Team.Human)
				team = Team.Zombie;
			else
				team = Team.Human;
		}
		// Get a random message from the death type's messages
		Random r = new Random();
		int i = r.nextInt(team.getKillMessages(death).size());
		String msg = team.getKillMessages(death).get(i);

		// Replace color codes, and killer and killed names
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if (killer != null)
			msg = msg.replaceAll("<killer>", killer.getName()+"(Put score here)");
		if (killed != null)
			msg = msg.replaceAll("<killed>", killed.getName() +"(Put score here)");
		return msg;
	}
}
