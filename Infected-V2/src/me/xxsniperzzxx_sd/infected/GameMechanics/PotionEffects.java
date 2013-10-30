
package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class PotionEffects {

	private static InfPlayerManager IPM = Main.InfPlayerManager;

	public static void applyClassEffects(Player p) {
		InfPlayer IP = IPM.getInfPlayer(p);

		for (PotionEffect PE : IP.getInfClass(IP.getTeam()).getEffects())
		{
			p.addPotionEffect(PE);
		}
	}

	public static void addEffectOnContact(Player p, Player u) {
		InfPlayer IP = IPM.getInfPlayer(p);

		for (PotionEffect PE : IP.getInfClass(IP.getTeam()).getContacteffects())
		{

			u.addPotionEffect(PE);
		}

	}

}
