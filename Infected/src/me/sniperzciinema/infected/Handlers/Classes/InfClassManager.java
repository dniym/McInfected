
package me.sniperzciinema.infected.Handlers.Classes;

import java.util.ArrayList;
import java.util.HashMap;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Handlers.Potions.PotionHandler;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class InfClassManager {

	private static ArrayList<InfClass> humanClasses = new ArrayList<InfClass>();
	private static ArrayList<InfClass> zombieClasses = new ArrayList<InfClass>();
	private static InfClass defaultHuman;
	private static InfClass defaultZombie;

	public static void addClass(String name, Team team, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ArrayList<ItemStack> items, ArrayList<PotionEffect> effects, ArrayList<PotionEffect> transfereffects, HashMap<Integer, ItemStack> killstreaks, String disguise) {
		InfClass IC = new InfClass(name, team, helmet, chestplate, leggings,
				boots, items, effects, transfereffects, killstreaks, disguise);
		if (team == Team.Human)
			humanClasses.add(IC);
		else
			zombieClasses.add(IC);
	}

	public static ArrayList<InfClass> getClasses(Team team) {
		if (team == Team.Human)
			return humanClasses;
		else
			return zombieClasses;
	}

	public static ArrayList<InfClass> getHumanClasses() {
		return humanClasses;
	}

	public static ArrayList<InfClass> getZombieClasses() {
		return zombieClasses;
	}

	public static boolean isRegistered(Team team, InfClass IC) {
		if (team == Team.Human)
			return humanClasses.contains(IC);
		else
			return zombieClasses.contains(IC);
	}

	public static void addClass(InfClass IC) {
		if (IC.getTeam() == Team.Human)
			humanClasses.add(IC);
		else
			zombieClasses.add(IC);
	}

	public static InfClass getClass(Team team, String className) {
		if (team == Team.Human)
			for (InfClass IC : humanClasses)
			{
				if (IC.getName().equalsIgnoreCase(className))
					return IC;
			}
		else
			for (InfClass IC : zombieClasses)
			{
				if (IC.getName().equalsIgnoreCase(className))
					return IC;
			}

		return null;
	}

	public static void removeClass(InfClass IC) {
		if (IC.getTeam() == Team.Human)
			humanClasses.remove(IC);
		else
			zombieClasses.remove(IC);
	}

	public static void removeClass(Team team, String className) {
		if (team == Team.Human)
			for (InfClass IC : humanClasses)
			{
				if (IC.getName().equalsIgnoreCase(className))
					humanClasses.remove(IC);
			}
		else
			for (InfClass IC : zombieClasses)
			{
				if (IC.getName().equalsIgnoreCase(className))
					zombieClasses.remove(IC);
			}
	}

	public static InfClass getDefaultClass(Team team) {
		if (team == Team.Human)
			return defaultHuman;
		else
			return defaultZombie;
	}

	public static void loadDefaultClasses() {
		defaultHuman = getClass(Team.Human, Files.getConfig().getString("Settings.Global.Default Classes.Human"));
		if(defaultHuman == null){
			defaultHuman = getClasses(Team.Human).get(0);
			Files.getConfig().set("Settings.Global.Default Classes.Human", defaultHuman.getName());
			Files.saveConfig();
			if (Settings.logClassesEnabled())
				System.out.println("Invalid default human class. Changed to: " + defaultHuman.getName());
		}
		defaultZombie = getClass(Team.Zombie, Files.getConfig().getString("Settings.Global.Default Classes.Zombie"));
		if(defaultZombie == null){
			defaultZombie = getClasses(Team.Zombie).get(0);
			Files.getConfig().set("Settings.Global.Default Classes.Zombie", defaultZombie.getName());
			Files.saveConfig();
			if (Settings.logClassesEnabled())
				System.out.println("Invalid default zombie class. Changed to: " + defaultZombie.getName());
		}
	}

	public static void loadConfigClasses() {
		humanClasses = new ArrayList<InfClass>();
		zombieClasses = new ArrayList<InfClass>();
		for (String s : Files.getClasses().getConfigurationSection("Classes.Human").getKeys(true))
		{
			String name = "0";
			String helmet = "0";
			String chestplate = "0";
			String leggings = "0";
			String boots = "0";
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			String disguise = null;
			ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
			ArrayList<PotionEffect> transfereffects = new ArrayList<PotionEffect>();
			HashMap<Integer, ItemStack> killstreaks = new HashMap<Integer, ItemStack>();
			if (!s.contains("."))
			{
				name = s;
				helmet = Files.getClasses().getString("Classes.Human." + s + ".Helmet");
				chestplate = Files.getClasses().getString("Classes.Human." + s + ".Chestplate");
				leggings = Files.getClasses().getString("Classes.Human." + s + ".Leggings");
				boots = Files.getClasses().getString("Classes.Human." + s + ".Boots");
				items = ItemHandler.getItemStackList(Files.getClasses().getStringList("Classes.Human." + s + ".Items"));
				disguise = Files.getClasses().getString("Classes.Human." + s + ".Disguise");
				effects = PotionHandler.getPotions(Files.getClasses().getStringList("Classes.Human." + s + ".Potion Effects"));
				transfereffects = PotionHandler.getPotions(Files.getClasses().getStringList("Classes.Human." + s + ".Transfer Potion Effects"));
				killstreaks = ItemHandler.getItemHashMap(Files.getClasses(), "Classes.Human." + s + ".KillStreaks");
				InfClass IC = new InfClass(name, Team.Human,
						ItemHandler.getItemStack(helmet),
						ItemHandler.getItemStack(chestplate),
						ItemHandler.getItemStack(leggings),
						ItemHandler.getItemStack(boots), items, effects,
						transfereffects, killstreaks, disguise);

				if (Settings.logClassesEnabled())
					System.out.println("Loaded Human Class: " + IC.getName());
				if (!isRegistered(Team.Human, IC))
					addClass(IC);
			}
		}
		for (String s : Files.getClasses().getConfigurationSection("Classes.Zombie").getKeys(true))
		{
			String name = "0";
			String helmet = "0";
			String chestplate = "0";
			String leggings = "0";
			String boots = "0";
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			String disguise = null;
			ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
			ArrayList<PotionEffect> transfereffects = new ArrayList<PotionEffect>();
			HashMap<Integer, ItemStack> killstreaks = new HashMap<Integer, ItemStack>();
			if (!s.contains("."))
			{
				name = s;
				helmet = Files.getClasses().getString("Classes.Zombie." + s + ".Helmet");
				chestplate = Files.getClasses().getString("Classes.Zombie." + s + ".Chestplate");
				leggings = Files.getClasses().getString("Classes.Zombie." + s + ".Leggings");
				boots = Files.getClasses().getString("Classes.Zombie." + s + ".Boots");
				items = ItemHandler.getItemStackList(Files.getClasses().getStringList("Classes.Zombie." + s + ".Items"));
				disguise = Files.getClasses().getString("Classes.Zombie." + s + ".Disguise");
				effects = PotionHandler.getPotions(Files.getClasses().getStringList("Classes.Zombie." + s + ".Potion Effects"));
				transfereffects = PotionHandler.getPotions(Files.getClasses().getStringList("Classes.Zombie." + s + ".Transfer Potion Effects"));
				killstreaks = ItemHandler.getItemHashMap(Files.getClasses(), "Classes.Zombie." + s + ".KillStreaks");

				InfClass IC = new InfClass(name, Team.Zombie,
						ItemHandler.getItemStack(helmet),
						ItemHandler.getItemStack(chestplate),
						ItemHandler.getItemStack(leggings),
						ItemHandler.getItemStack(boots), items, effects,
						transfereffects, killstreaks, disguise);

				if (Settings.logClassesEnabled())
					System.out.println("Loaded Zombie Class: " + IC.getName());
				if (!isRegistered(Team.Zombie, IC))
					addClass(IC);
			}
		}
		loadDefaultClasses();
	}

}
