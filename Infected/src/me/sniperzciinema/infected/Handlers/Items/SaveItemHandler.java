
package me.sniperzciinema.infected.Handlers.Items;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SaveItemHandler {

	public static void saveItems(Player p, ItemStack is) {
		List<ItemStack> list = getItems(p);
		list.add(is);
		Files.getPlayers().set("Players." + p.getName().toLowerCase() + ".Saved Inventory", list);
	}

	@SuppressWarnings("unchecked")
	public static List<ItemStack> getItems(Player p) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		if (Files.getPlayers().get("Players." + p.getName().toLowerCase() + ".Saved Inventory") != null)
			try
			{
				list = (ArrayList<ItemStack>) Files.getPlayers().get("Players." + p.getName().toLowerCase() + ".Saved Inventory");
			} catch (Exception e)
			{
				p.sendMessage("Tell an Admin that your saved inventory is invalid!");
			}
		return list;
	}
}
