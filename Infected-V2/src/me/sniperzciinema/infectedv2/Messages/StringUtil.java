
package me.sniperzciinema.infectedv2.Messages;

public class StringUtil {

	// Method to capitalize the first letter of the arena name, and nothing
	// else(This way when you join caps don't matter)
	public static String getWord(String string) {
		String s = string;
		if (string != null)
		{
			s= s.replaceAll("_", " ");
			s = s.toLowerCase();
			s = s.replaceFirst(String.valueOf(s.charAt(0)), String.valueOf(s.charAt(0)).toUpperCase());
		}
		return s;
	}
}

