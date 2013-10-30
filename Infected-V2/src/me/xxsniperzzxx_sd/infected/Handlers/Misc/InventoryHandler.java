package me.xxsniperzzxx_sd.infected.Handlers.Misc;


import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.ItemSerialization;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;




public class InventoryHandler {

	public static void saveInventory(Player player, String loc) {
		String data = ItemSerialization.toBase64(player.getInventory());
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + "." + loc, data);
		Files.savePlayers();
	}

	public static void addItemToInventory(Player player, String loc, ItemStack items) {
		if (Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc) == null)
		{
			saveInventory(player, loc);
		}
		Inventory copy = ItemSerialization.fromBase64(Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc));
		copy.addItem(items);
		String done = ItemSerialization.toBase64(copy);
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + "." + loc, done);
		Files.savePlayers();
	}

	public static ItemStack[] getInventory(Player player, String loc) {
		if (Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc) == null)
		{
			saveInventory(player, loc);
		}
		String data = Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc);
		Inventory copy = ItemSerialization.fromBase64(data);
		return copy.getContents();
	}



}
