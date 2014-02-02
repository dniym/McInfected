
package me.sniperzciinema.infected.Tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * @author D4rKDeagle
 */
public class TabCompletionHelper {

	public static List<String> getPossibleCompletionsForGivenArgs(String[] args, String[] possibilitiesOfCompletion) {
		String argumentToFindCompletionFor = args[args.length - 1];

		List<String> listOfPossibleCompletions = new ArrayList<String>();

		for (int i = 0; i < possibilitiesOfCompletion.length; ++i)
		{
			String foundString = possibilitiesOfCompletion[i];

			if (foundString.regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length()))
			{
				listOfPossibleCompletions.add(foundString);
			}
		}

		return listOfPossibleCompletions;
	}

	public static String[] getOnlinePlayerNames() {
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		String[] onlinePlayerNames = new String[onlinePlayers.length];

		for (int i = 0; i < onlinePlayers.length; i++)
		{
			onlinePlayerNames[i] = onlinePlayers[i].getName();
		}

		return onlinePlayerNames;
	}
}