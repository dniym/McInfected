
package me.sniperzciinema.infected.Extras;

import org.bukkit.ChatColor;


public class Pictures {

	public static String[] getZombie() {
		String[] face = { "", "", "", "", "", "", "", "", "", "" };
		String block = "▓";
		face[0] = "";
		face[1] = ChatColor.GREEN + block + block + block + block + block + block + block + block;
		face[2] = ChatColor.GREEN + block + block + block + block + block + block + block + block;
		face[3] = ChatColor.GREEN + block + block + block + block + block + block + block + block;
		face[4] = ChatColor.GREEN + block + block + block + block + block + block + block + block;
		face[5] = ChatColor.GREEN + block + ChatColor.BLACK + block + block + ChatColor.GREEN + block + block + ChatColor.BLACK + block + block + ChatColor.GREEN + block;
		face[6] = ChatColor.GREEN + block + block + block + ChatColor.DARK_GREEN + block + block + ChatColor.GREEN + block + block + block;
		face[7] = ChatColor.GREEN + block + block + block + block + block + block + block + block;
		face[8] = ChatColor.GREEN + block + block + block + block + block + block + block + block;
		face[9] = "";
		return face;
	}

	public static String[] getHuman() {
		String[] face = { "", "", "", "", "", "", "", "", "", "" };
		String block = "▓";
		face[0] = "";
		face[1] = ChatColor.GOLD + block + block + block + block + block + block + block + block;
		face[2] = ChatColor.GOLD + block + block + block + block + block + block + block + block;
		face[3] = ChatColor.GOLD + block + ChatColor.BLACK + block + ChatColor.GOLD + block + block + block + block + ChatColor.BLACK + block + ChatColor.GOLD + block;
		face[4] = ChatColor.GOLD + block + block + block + block + block + block + block + block;
		face[5] = ChatColor.GOLD + block + block + ChatColor.BLACK + block + block + block + block + ChatColor.GOLD + block + block;
		face[6] = ChatColor.GOLD + block + ChatColor.BLACK + block + ChatColor.WHITE + block + block + block + block + ChatColor.BLACK + block + ChatColor.GOLD + block;
		face[7] = ChatColor.GOLD + block + ChatColor.BLACK + block + block + block + block + block + block + ChatColor.GOLD + block;
		face[8] = ChatColor.GOLD + block + ChatColor.BLACK + block + block + block + block + block + block + ChatColor.GOLD + block;
		face[9] = "";
		return face;
	}
}
