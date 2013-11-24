
package me.sniperzciinema.infectedv2.GameMechanics;

import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class PotionEffects {


	public static void applyClassEffects(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);

		for (PotionEffect PE : IP.getInfClass(IP.getTeam()).getEffects())
			p.addPotionEffect(PE);
	}

	public static void addEffectOnContact(Player p, Player u) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);

		for (PotionEffect PE : IP.getInfClass(IP.getTeam()).getContacteffects())
			u.addPotionEffect(PE);
	}

}
