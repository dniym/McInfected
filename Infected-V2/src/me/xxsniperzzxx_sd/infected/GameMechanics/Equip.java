
package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClass;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Equip {


	@SuppressWarnings("deprecation")
	public static void equip(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		Team team = IP.getTeam();
		InfClass Class = IP.getInfClass(team);
		p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);

		// Reset their inventory by: Going through and removing any old items
		// from the class
		// and add the new ones, this way we don't remove purchased/grenades
		for (ItemStack is : Class.getItems())
		{
			if (p.getInventory().contains(is))
				p.getInventory().remove(is);
			if (!p.getInventory().contains(is))
				p.getInventory().addItem(is);
		}

		// Only replace the armor if the player hasn't changed it(So if it there
		// is none, or if it is the same as default)
		if (p.getInventory().getHelmet() == Class.getHelmet() || p.getInventory().getHelmet() == null)
			p.getInventory().setHelmet(Class.getHelmet());
		if (p.getInventory().getChestplate() == Class.getChestplate() || p.getInventory().getChestplate() == null)
			p.getInventory().setChestplate(Class.getChestplate());
		if (p.getInventory().getLeggings() == Class.getLeggings() || p.getInventory().getLeggings() == null)
			p.getInventory().setLeggings(Class.getLeggings());
		if (p.getInventory().getBoots() == Class.getBoots() || p.getInventory().getBoots() == null)
			p.getInventory().setBoots(Class.getBoots());

		p.updateInventory();

	}

	@SuppressWarnings("deprecation")
	public static void equipToZombie(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);

		InfClass humanClass = IP.getInfClass(Team.Human);
		InfClass zombieClass = IP.getInfClass(Team.Zombie);

		p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);

		// Reset their inventory by: Going through and removing any old items
		// from the class
		// and add the new ones, this way we don't remove purchased/grenades
		for (ItemStack is : humanClass.getItems())
		{
			p.getInventory().remove(is);
			// TODO: Also see if their human armor is in their inventory
		}
		for (ItemStack is : zombieClass.getItems())
		{
			p.getInventory().addItem(is);
		}

		// Only replace the armor if the player hasn't changed it(So if it there
		// is none, or if it is the same as default)
		if (p.getInventory().getHelmet() == humanClass.getHelmet() || p.getInventory().getHelmet() == null)
			p.getInventory().setHelmet(zombieClass.getHelmet());
		if (p.getInventory().getChestplate() == humanClass.getChestplate() || p.getInventory().getChestplate() == null)
			p.getInventory().setChestplate(zombieClass.getChestplate());
		if (p.getInventory().getLeggings() == humanClass.getLeggings() || p.getInventory().getLeggings() == null)
			p.getInventory().setLeggings(zombieClass.getLeggings());
		if (p.getInventory().getBoots() == humanClass.getBoots() || p.getInventory().getBoots() == null)
			p.getInventory().setBoots(zombieClass.getBoots());

		p.updateInventory();

	}
}
