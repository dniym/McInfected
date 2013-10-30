
package me.xxsniperzzxx_sd.infected.Messages;

import java.util.Random;

import org.bukkit.ChatColor;


public class RandomChatColor {

	public static ChatColor getColor() {
		Random r = new Random();
		int i = r.nextInt(ChatColor.values().length);
		ChatColor rc = ChatColor.values()[i];
		return rc;
	}
}