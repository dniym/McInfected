
package me.sniperzciinema.infectedv2.Handlers.Classes;

import java.util.ArrayList;

import me.sniperzciinema.infectedv2.Handlers.Misc.ItemHandler;
import me.sniperzciinema.infectedv2.Handlers.Misc.PotionHandler;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;
import me.sniperzciinema.infectedv2.Tools.Files;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class InfClassManager {

	private static ArrayList<InfClass> humanClasses = new ArrayList<InfClass>();
	private static ArrayList<InfClass> zombieClasses = new ArrayList<InfClass>();
	private static InfClass defaultHuman;
	private static InfClass defaultZombie;

	public static void addClass(String name, Team team, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ArrayList<ItemStack> items, ArrayList<PotionEffect> effects, ArrayList<PotionEffect> transfereffects, String disguise) {
		InfClass IC = new InfClass(name, team, helmet, chestplate, leggings,
				boots, items, effects, transfereffects, disguise);
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
		defaultHuman = getClass(Team.Human, Files.getConfig().getString("Classes.Default.Human"));
		defaultZombie = getClass(Team.Zombie, Files.getConfig().getString("Classes.Default.Zombie"));
	}

	public static void loadConfigClasses() {
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
			if (!s.contains("."))
				name = s;
			if (s.contains("Helmet"))
				helmet = Files.getClasses().getString("Classes.Human") + s;
			if (s.contains("ChestPlate"))
				chestplate = Files.getClasses().getString("Classes.Human") + s;
			if (s.contains("Leggings"))
				leggings = Files.getClasses().getString("Classes.Human") + s;
			if (s.contains("Boots"))
				boots = Files.getClasses().getString("Classes.Human") + s;
			if (s.contains("Items"))
				items.add(ItemHandler.getItemStack(Files.getClasses().getString("Classes.Human") + s));
			if (s.contains("Disguise"))
				disguise = Files.getClasses().getString("Classes.Human") + s;
			if (s.contains("Potion Effects"))
				effects.add(PotionHandler.getPotion(Files.getClasses().getString("Classes.Human") + s));
			if (s.contains("Transfer Potion Effects"))
				effects.add(PotionHandler.getPotion(Files.getClasses().getString("Classes.Human") + s));
			InfClass IC = new InfClass(name, Team.Human,
					ItemHandler.getItemStack(helmet),
					ItemHandler.getItemStack(chestplate),
					ItemHandler.getItemStack(leggings),
					ItemHandler.getItemStack(boots), items, effects,
					transfereffects, disguise);

			if (!isRegistered(Team.Human, IC))
				addClass(IC);
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
			if (!s.contains("."))
				name = Files.getClasses().getString("Classes.Zombie") + s;
			if (s.contains("Helmet"))
				helmet = Files.getClasses().getString("Classes.Zombie") + s;
			if (s.contains("ChestPlate"))
				chestplate = Files.getClasses().getString("Classes.Zombie") + s;
			if (s.contains("Leggings"))
				leggings = Files.getClasses().getString("Classes.Zombie") + s;
			if (s.contains("Boots"))
				boots = Files.getClasses().getString("Classes.Zombie") + s;
			if (s.contains("Items"))
				items.add(ItemHandler.getItemStack(Files.getClasses().getString("Classes.Zombie") + s));
			if (s.contains("Disguise"))
				disguise = Files.getClasses().getString("Classes.Zombie") + s;
			if (s.contains("Potion Effects"))
				effects.add(PotionHandler.getPotion(Files.getClasses().getString("Classes.Zombie") + s));
			if (s.contains("Transfer Potion Effects"))
				effects.add(PotionHandler.getPotion(Files.getClasses().getString("Classes.Zombie") + s));
			InfClass IC = new InfClass(name, Team.Zombie,
					ItemHandler.getItemStack(helmet),
					ItemHandler.getItemStack(chestplate),
					ItemHandler.getItemStack(leggings),
					ItemHandler.getItemStack(boots), items, effects,
					transfereffects, disguise);

			if (!isRegistered(Team.Zombie, IC))
				addClass(IC);

			loadDefaultClasses();
		}
	}
}