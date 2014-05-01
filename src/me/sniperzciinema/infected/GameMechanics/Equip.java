
package me.sniperzciinema.infected.GameMechanics;

import me.sniperzciinema.infected.Handlers.Classes.InfClass;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Equip {
	
	/**
	 * Equip the player to match with what their class says they should have,
	 * any items in the inventory that aren't from that class will be ignored.
	 * Also does their armor
	 * 
	 * @param p
	 *          - The player we're equipping
	 */
	@SuppressWarnings("deprecation")
	public static void equip(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		Team team = IP.getTeam();
		InfClass Class = IP.getInfClass(team);
		p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
		
		// Reset their inventory by: Going through and removing any old items
		// from the class
		// and add the new ones, this way we don't remove purchased/grenades
		if (!Class.getItems().isEmpty())
			for (ItemStack is : Class.getItems())
			{
				if (p.getInventory().contains(ItemHandler.getItemStackIgnoreDamage(is)))
					p.getInventory().remove(is);
				if (!p.getInventory().contains(ItemHandler.getItemStackIgnoreDamage(is)))
					p.getInventory().addItem(is);
			}
		
		// Only replace the armor if the player hasn't changed it(So if
		// its none, or if it is the same as default)
		if ((ItemHandler.getItemStackIgnoreDamage(p.getInventory().getHelmet()) == ItemHandler.getItemStackIgnoreDamage(Class.getHelmet())) || (p.getInventory().getHelmet() == null))
			p.getInventory().setHelmet(Class.getHelmet());
		if ((ItemHandler.getItemStackIgnoreDamage(p.getInventory().getChestplate()) == ItemHandler.getItemStackIgnoreDamage(Class.getChestplate())) || (p.getInventory().getChestplate() == null))
			p.getInventory().setChestplate(Class.getChestplate());
		if ((ItemHandler.getItemStackIgnoreDamage(p.getInventory().getLeggings()) == ItemHandler.getItemStackIgnoreDamage(Class.getLeggings())) || (p.getInventory().getLeggings() == null))
			p.getInventory().setLeggings(Class.getLeggings());
		if ((ItemHandler.getItemStackIgnoreDamage(p.getInventory().getBoots()) == ItemHandler.getItemStackIgnoreDamage(Class.getBoots())) || (p.getInventory().getBoots() == null))
			p.getInventory().setBoots(Class.getBoots());
		
		p.updateInventory();
		
	}
	
	/**
	 * Set up their new inventory as a zombie
	 * 
	 * @param p
	 *          - The player who just became a Zombie
	 */
	@SuppressWarnings("deprecation")
	public static void equipToZombie(Player p) {
		InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		
		InfClass zombieClass = IP.getInfClass(Team.Zombie);
		
		p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
		
		IP.clearEquipment();
		if (!zombieClass.getItems().isEmpty())
			for (ItemStack is : zombieClass.getItems())
				p.getInventory().addItem(is);
		
		p.getInventory().setHelmet(zombieClass.getHelmet());
		p.getInventory().setChestplate(zombieClass.getChestplate());
		p.getInventory().setLeggings(zombieClass.getLeggings());
		p.getInventory().setBoots(zombieClass.getBoots());
		
		p.updateInventory();
		
	}
	
}
