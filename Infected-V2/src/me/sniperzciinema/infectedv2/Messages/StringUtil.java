
package me.sniperzciinema.infectedv2.Messages;
/**
 * If anyone wants to use this class for their own plugin go ahead, this is a class you can also find in my gists(On Github).
 * If you have any suggestions/additions that you think would help Infected feel free to message me or fork the gist
 * 
 * @author Sniperz
 *
 */
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

