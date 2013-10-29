
package me.xxsniperzzxx_sd.infected.Messages;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.GameMechanics.DeathType;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class DeathMessages {

	// Get the death message
	public static String getDeathMessage(Player killer, Player killed, DeathType death) {

		InfPlayer IP = InfPlayerManager.getPlayer(killer);
		
		// Get a random message from the death type's messages
		Random r = new Random();
		int i = r.nextInt(IP.getTeam().getKillMessages().size());
		String msg = IP.getTeam().getKillMessages().get(i);

		// Replace color codes, and killer and killed names
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if (killer != null)
			msg = msg.replaceAll("<killer>", killer.getName()+"(Put score here)");
		if (killed != null)
			msg = msg.replaceAll("<killed>", killed.getName() +"(Put score here)");
		return msg;
	}
}
