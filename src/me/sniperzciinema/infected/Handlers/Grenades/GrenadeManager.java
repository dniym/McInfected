
package me.sniperzciinema.infected.Handlers.Grenades;

import java.util.ArrayList;
import java.util.UUID;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Potions.PotionHandler;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class GrenadeManager {

	private static ArrayList<Grenade>	grenades		= new ArrayList<Grenade>();
	private static ArrayList<UUID>		thrownGrenades	= new ArrayList<UUID>();

	public static void addGrenade(String id, String name, double damage, int delay, int range, int cost, boolean damageThrower, ArrayList<PotionEffect> effects) {
		Grenade grenade = new Grenade(id, name, damage, delay, range, cost, damageThrower, effects);
		grenades.add(grenade);
	}

	public static void addGrenade(Grenade grenade) {
		grenades.add(grenade);
	}

	public static ArrayList<Grenade> getGrenades() {
		return grenades;
	}

	public static ArrayList<UUID> getThrownGrenade() {
		return thrownGrenades;
	}

	public static void addThrownGrenade(UUID uuid) {
		thrownGrenades.add(uuid);
	}

	public static void delThrownGrenade(UUID uuid) {
		thrownGrenades.remove(uuid);
	}

	public static boolean isThrownGrenade(UUID uuid) {
		return thrownGrenades.contains(uuid);
	}

	public static boolean isGrenade(ItemStack is) {
		for (Grenade grenade : grenades)
		{
			if (grenade.getItem() == is)
				return true;
		}
		return false;
	}

	public static boolean isGrenade(Grenade g) {
		return grenades.contains(g);
	}

	public static Grenade getGrenade(ItemStack is) {
		for (Grenade grenade : grenades)
			if (grenade.getItem() == is)
				return grenade;

		return null;
	}

	public static Grenade getGrenade(String name) {
		for (Grenade grenade : grenades)
			if (grenade.getName().equals(name))
				return grenade;

		return null;
	}

	public static void loadConfigGrenades() {
		grenades = new ArrayList<Grenade>();
		for (String s : Files.getGrenades().getConfigurationSection("Grenades").getKeys(true))
		{
			if (!s.contains("."))
			{

				String name = s;
				String id = Files.getGrenades().getString("Grenades." + s + ".Item Id");
				double damage = Files.getGrenades().getDouble("Grenades." + s + ".Damage");
				int delay = Files.getGrenades().getInt("Grenades." + s + ".Delay");
				int range = Files.getGrenades().getInt("Grenades." + s + ".Range");
				int cost = Files.getGrenades().getInt("Grenades." + s + ".Cost");
				boolean damageThrower = Files.getGrenades().getBoolean("Grenades." + s + ".Damage Thrower");
				ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
				for (String string : Files.getGrenades().getStringList("Grenades." + s + ".Potion Effects"))
					potions.add(PotionHandler.getPotion(string));

				Grenade g = new Grenade(id, name, damage, delay, range, cost, damageThrower,
						potions);

				if (Settings.logGrenadesEnabled())
					System.out.println("Loaded Grenade " + g.getName());

				if (!isGrenade(g))
					addGrenade(g);
			}
		}
	}
}
