
package me.xxsniperzzxx_sd.infected.Messages;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.GameMechanics.DeathType;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class DeathMessages {

	private static InfPlayerManager IPM = Main.InfPlayerManager;
	
	public static String getDeathMessage(Player killer, Player killed, DeathType death) {

		InfPlayer IP = null;
		Team team = null;
		if(killer != null){
			IP = IPM.getInfPlayer(killer);
			team = IP.getTeam();
		}else{
			if(IPM.getInfPlayer(killed).getTeam() == Team.Human)
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
