package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Methods;
import me.xxsniperzzxx_sd.infected.Tools.ItemHandler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.TagAPI;


public class Equip {

	@SuppressWarnings("deprecation")
	public static void equipHumans(Player human) {
		if (Main.config.getBoolean("Default Classes.Use"))
			Main.humanClasses.put(human.getName(), Main.config.getString("Default Classes.Human"));

		if (Main.config.getBoolean("TagAPI Support.Enable"))
			TagAPI.refreshPlayer(human);
		
		if (Main.humanClasses.containsKey(human.getName()))
			if (Main.humanClasses.get(human.getName()).equalsIgnoreCase("None"))
				Main.humanClasses.remove(human.getName());
		
		if (Main.humanClasses.containsKey(human.getName()))
		{
			for (String s : Infected.filesGetClasses().getStringList("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Items"))
			{
				human.getInventory().addItem(ItemHandler.getItemStack(s));
				human.updateInventory();
			}
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Head") != null)
				human.getInventory().setHelmet(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Head")));
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Chest") != null)
				human.getInventory().setChestplate(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Chest")));
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Legs") != null)
				human.getInventory().setLeggings(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Legs")));
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Feet") != null)
				human.getInventory().setBoots(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Feet")));
			
		} else
		{
			for (String s : Main.config.getStringList("Armor.Human.Items"))
			{
				human.getInventory().addItem(ItemHandler.getItemStack(s));
				human.updateInventory();
			}
			if (Main.config.getString("Armor.Human.Head") != null)
				human.getInventory().setHelmet(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Head")));
			if (Main.config.getString("Armor.Human.Chest") != null)
				human.getInventory().setChestplate(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Chest")));
			if (Main.config.getString("Armor.Human.Legs") != null)
				human.getInventory().setLeggings(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Legs")));
			if (Main.config.getString("Armor.Human.Feet") != null)
				human.getInventory().setBoots(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Feet")));
		}
		human.updateInventory();
		Methods.applyClassAbility(human);
	}

	@SuppressWarnings("deprecation")
	public static void equipZombies(Player zombie) {

		if (Main.config.getBoolean("Default Classes.Use"))
			Main.zombieClasses.put(zombie.getName(), Main.config.getString("Default Classes.Zombie"));

		ScoreBoard.updateScoreBoard();
		if (Main.config.getBoolean("TagAPI Support.Enable"))
			TagAPI.refreshPlayer(zombie);
		// Give infected their armor

		// Take away humans items
		for (String s : Main.config.getStringList("Armor.Human.Items"))
		{
			if (zombie.getInventory().contains(ItemHandler.getItem(s).getType()))
			{
				zombie.getInventory().remove(ItemHandler.getItem(s).getType());
			}
		}
		// Take away any items from their human class
		if (Main.humanClasses.containsKey(zombie.getName()))
		{
			for (String s : Infected.filesGetClasses().getStringList("Classes.Human." + Main.humanClasses.get(zombie.getName()) + ".Items"))
			{
				if (zombie.getInventory().contains(ItemHandler.getItem(s).getType()))
				{
					zombie.getInventory().remove(ItemHandler.getItem(s).getType());
				}
			}
		}
		for (ItemStack armor : zombie.getInventory().getArmorContents())
		{
			if (!(armor == null || armor.getType() == Material.AIR) && (armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Head")) || armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Chest")) || armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Legs")) || armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Feet"))))
			{
				zombie.getInventory().addItem(armor);
			}
		}

		// Add armor from the zombie class

		if (Main.zombieClasses.containsKey(zombie.getName()))
			if (!Main.zombieClasses.get(zombie.getName()).equalsIgnoreCase("None"))
				Main.zombieClasses.remove(zombie.getName());
		
		if (Main.zombieClasses.containsKey(zombie.getName()))
		{
			for (String s : Infected.filesGetClasses().getStringList("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Items"))
			{
				if (!zombie.getInventory().contains(ItemHandler.getItem(s)))
					zombie.getInventory().addItem(ItemHandler.getItemStack(s));
				zombie.updateInventory();
			}
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Head") != null)
				zombie.getInventory().setHelmet(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Head")));
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Chest") != null)
				zombie.getInventory().setChestplate(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Chest")));
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Legs") != null)
				zombie.getInventory().setLeggings(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Legs")));
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Feet") != null)
				zombie.getInventory().setBoots(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Feet")));
				
		} else
		{
			for (String s : Main.config.getStringList("Armor.Zombie.Items"))
			{
				if (!zombie.getInventory().contains(ItemHandler.getItem(s)))
					zombie.getInventory().addItem(ItemHandler.getItemStack(s));
			}
			if (Main.config.getString("Armor.Zombie.Head") != null)
				zombie.getInventory().setHelmet(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Head")));
			if (Main.config.getString("Armor.Zombie.Chest") != null)
				zombie.getInventory().setChestplate(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Chest")));
			if (Main.config.getString("Armor.Zombie.Legs") != null)
				zombie.getInventory().setLeggings(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Legs")));
			if (Main.config.getString("Armor.Zombie.Feet") != null)
				zombie.getInventory().setBoots(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Feet")));
		}
		zombie.updateInventory();
	}
}
