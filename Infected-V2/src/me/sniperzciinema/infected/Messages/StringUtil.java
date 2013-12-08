
package me.sniperzciinema.infected.Messages;

public class StringUtil {

	// Method to capitalize the first letter of the arena name, and nothing
	// else(This way when you join caps don't matter)
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
