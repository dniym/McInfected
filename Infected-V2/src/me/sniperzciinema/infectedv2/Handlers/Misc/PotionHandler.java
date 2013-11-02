
package me.sniperzciinema.infectedv2.Handlers.Misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


@SuppressWarnings("deprecation")
public class PotionHandler {

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

	public static ArrayList<PotionEffect> getPotions(List<String> list) {
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		for (String path : list)
		{
			Integer id = 0;
			Integer time = 0;
			Integer power = 0;
			String[] strings = path.split(":");
			id = Integer.valueOf(strings[0]);
			time = Integer.valueOf(strings[1]) * 20;
			power = Integer.valueOf(strings[2]);
			effects.add(new PotionEffect(PotionEffectType.getById(id), time,
					power));
		}
		return effects;
	}

}
