
package me.xxsniperzzxx_sd.infected.Handlers;

import me.xxsniperzzxx_sd.infected.Infected;


public class MapHandler {
	

	public static String getMapCreator(String map){
		if(Infected.filesGetArenas().getString("Arenas." + map + ".Creator") != null)
			return Infected.filesGetArenas().getString("Arenas." + map + ".Creator");
		else 
			return "Unkown";
	}
}