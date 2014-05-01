
package me.sniperzciinema.infected.Messages;

import org.bukkit.ChatColor;


public class StringUtil {
	
	public static String format(String string) {
		
		return ChatColor.translateAlternateColorCodes('&', string.replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar())));
	}
	
	/**
	 * @param string
	 * @return the string with a capital first letter and the rest lowercase
	 */
	public static String getWord(String string) {
		if (string != null)
		{
			string = string.replaceAll("_", " ");
			string = string.toLowerCase();
			string = string.replaceFirst(String.valueOf(string.charAt(0)), String.valueOf(string.charAt(0)).toUpperCase());
			
		}
		return string;
	}
}
