
package me.xxsniperzzxx_sd.infected.Tools.Handlers;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;


public class MapHandler {
	

	public static String getMapCreator(String map){
		return Infected.filesGetArenas().getString("Arenas." + map + ".Creator");
	}

	
	public static String getPossibleMaps(){

		Infected.filesReloadArenas();
		Main.possibleArenas.clear();

		for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
		{
			// Check if the string matchs an arena

			if (Main.possibleArenas.contains(parenas))
			{
				Main.possibleArenas.remove(parenas);
			}
			if (!parenas.contains("."))
			{
				Main.possibleArenas.add(parenas);
			}
			if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
			{
				Main.possibleArenas.remove(parenas);
			}
			if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns") && !parenas.contains("."))
			{
				Main.possibleArenasU.add(parenas);
			}
		}

		StringBuilder possible = new StringBuilder();
		for (Object o : Main.possibleArenas)
		{
			possible.append(o.toString());
			if (Main.possibleArenas.size() > 1)
				possible.append(", ");
		}
		return possible.toString();
	}

}