
package me.sniperzciinema.infected.Handlers.Potions;

import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class PotionEffects {
	
	public static void addEffectOnContact(Player p, Player u) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		if (!IP.getInfClass(IP.getTeam()).getContactEffects().isEmpty())
			for (PotionEffect PE : IP.getInfClass(IP.getTeam()).getContactEffects())
				u.addPotionEffect(PE);
	}
	
	public static void applyClassEffects(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		
		if (!IP.getInfClass(IP.getTeam()).getEffects().isEmpty())
			for (PotionEffect PE : IP.getInfClass(IP.getTeam()).getEffects())
				p.addPotionEffect(PE);
	}
}
