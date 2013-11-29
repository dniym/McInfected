
package me.sniperzciinema.infectedv2.Messages;

import java.util.Random;

import org.bukkit.ChatColor;
/**
 * If anyone wants to use this class for their own plugin go ahead, this is a class you can also find in my gists(On Github).
 * If you have any suggestions/additions that you think would help Infected feel free to message me or fork the gist
 * 
 * @author Sniperz
 *
 */

public class RandomChatColor {

	public static ChatColor getColor() {
		Random r = new Random();
		int i = r.nextInt(ChatColor.values().length);
		while (!ChatColor.values()[i].isColor())
			i = r.nextInt(ChatColor.values().length);
		ChatColor rc = ChatColor.values()[i];
		return rc;
	}
	public static ChatColor getFormat() {
		Random r = new Random();
		int i = r.nextInt(ChatColor.values().length);
		while (!ChatColor.values()[i].isFormat())
			i = r.nextInt(ChatColor.values().length);
		ChatColor rc = ChatColor.values()[i];
		return rc;
	}
}