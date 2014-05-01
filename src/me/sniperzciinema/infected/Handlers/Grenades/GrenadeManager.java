
package me.sniperzciinema.infected.Handlers.Grenades;

import java.util.ArrayList;
import java.util.UUID;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Potions.PotionHandler;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class GrenadeManager {
	
	private static ArrayList<Grenade>	grenades				= new ArrayList<Grenade>();
	private static ArrayList<UUID>		thrownGrenades	= new ArrayList<UUID>();
	
	public static void addGrenade(Grenade grenade) {
		GrenadeManager.grenades.add(grenade);
	}
	
	public static void addGrenade(String id, String name, double damage, int delay, int range, int cost, boolean damageThrower, ArrayList<PotionEffect> effects) {
		Grenade grenade = new Grenade(id, name, damage, delay, range, cost, damageThrower, effects);
		GrenadeManager.grenades.add(grenade);
	}
	
	public static void addThrownGrenade(UUID uuid) {
		GrenadeManager.thrownGrenades.add(uuid);
	}
	
	public static void delThrownGrenade(UUID uuid) {
		GrenadeManager.thrownGrenades.remove(uuid);
	}
	
	public static Grenade getGrenade(ItemStack is) {
		for (Grenade grenade : GrenadeManager.grenades)
			if (grenade.getItem() == is)
				return grenade;
		
		return null;
	}
	
	public static Grenade getGrenade(String name) {
		for (Grenade grenade : GrenadeManager.grenades)
			if (grenade.getName().equalsIgnoreCase(ChatColor.stripColor(name)))
				return grenade;
		
		return null;
	}
	
	public static ArrayList<Grenade> getGrenades() {
		return GrenadeManager.grenades;
	}
	
	public static ArrayList<UUID> getThrownGrenade() {
		return GrenadeManager.thrownGrenades;
	}
	
	public static boolean isGrenade(Grenade g) {
		return GrenadeManager.grenades.contains(g);
	}
	
	public static boolean isGrenade(ItemStack is) {
		for (Grenade grenade : GrenadeManager.grenades)
			if (grenade.getName().equalsIgnoreCase(ChatColor.stripColor(is.getItemMeta().getDisplayName())))
				return true;
		return false;
	}
	
	public static boolean isThrownGrenade(UUID uuid) {
		return GrenadeManager.thrownGrenades.contains(uuid);
	}
	
	public static void loadConfigGrenades() {
		GrenadeManager.grenades = new ArrayList<Grenade>();
		for (String s : Files.getGrenades().getConfigurationSection("Grenades").getKeys(true))
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
				
				Grenade g = new Grenade(id, name, damage, delay, range, cost, damageThrower, potions);
				
				if (Settings.logGrenadesEnabled())
					System.out.println("Loaded Grenade " + g.getName());
				
				if (!isGrenade(g))
					addGrenade(g);
			}
	}
}
