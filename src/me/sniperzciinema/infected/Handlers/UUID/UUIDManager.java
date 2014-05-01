
package me.sniperzciinema.infected.Handlers.UUID;

import java.util.UUID;

import org.bukkit.Bukkit;


public class UUIDManager {
	
	public static String getPlayerName(UUID id) {
		
		if (Bukkit.getPlayer(id) != null)
			return Bukkit.getPlayer(id).getName();
		
		else if (Bukkit.getOfflinePlayer(id) != null)
			return Bukkit.getOfflinePlayer(id).getName();
		
		else
			return "Unkown";
	}
	
	@SuppressWarnings("deprecation")
	public static UUID getPlayerUUID(String name) {
		
		if (Bukkit.getPlayer(name) != null)
			return Bukkit.getPlayer(name).getUniqueId();
		else if (Bukkit.getOfflinePlayer(name) != null)
			return Bukkit.getOfflinePlayer(name).getUniqueId();
		else
			return null;
	}
}
