package me.xxsniperzzxx_sd.infected.Handlers;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PotionHandler {
	
	@SuppressWarnings("deprecation")
	public static PotionEffect getPotion(String path) {
		Integer id = 0;
		Integer time = 0;
		Integer power = 0;
		String[] strings = path.split(":");
		id = Integer.valueOf(strings[0]);
		time = Integer.valueOf(strings[1]) * 20;
		power = Integer.valueOf(strings[2]);
		return new PotionEffect(PotionEffectType.getById(id), time, power);
	}

}
