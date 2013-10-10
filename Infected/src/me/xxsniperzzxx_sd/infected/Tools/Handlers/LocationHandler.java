
package me.xxsniperzzxx_sd.infected.Tools.Handlers;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.GameMechanics.ScoreBoard;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class LocationHandler {

	
	public static Location getObjectLocation(String loc) {
		String[] floc = loc.split(",");
		World world = Bukkit.getServer().getWorld(floc[0]);
		Location Loc = new Location(world, Double.valueOf(floc[1]),
				Double.valueOf(floc[2]), Double.valueOf(floc[3]));
		return Loc;
	}
	public static Location getPlayerLocation(String loc) {
		String[] floc = loc.split(",");
		World world = Bukkit.getServer().getWorld(floc[0]);
		Location Loc = new Location(world, Double.valueOf(floc[1]),
				Double.valueOf(floc[2])+.5, Double.valueOf(floc[3]),
				Float.valueOf(floc[4]), Float.valueOf(floc[5]));
		return Loc;
	}
	

	public static void respawn(Player player) {
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		Random r = new Random();
		int i = r.nextInt(Files.getArenas().getStringList("Arenas." + Main.playingin + ".Spawns").size());
		String loc = Files.getArenas().getStringList("Arenas." + Main.playingin + ".Spawns").get(i);
		
		player.teleport(getPlayerLocation(loc));
		Main.Lasthit.remove(player.getName());
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			ScoreBoard.updateScoreBoard();
		}
	}

	public static String getLocationToString(Location loc) {
		double ix = loc.getX();
		double iy = loc.getY();
		double iz = loc.getZ();
		World world = loc.getWorld();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		String s = world.getName() + "," + ix + "," + iy + "," + iz+ "," + yaw+ "," + pitch;
		return s;
	}


	public static void saveLocation(Location loc, String saveto) {
		double ix = loc.getX();
		double iy = loc.getY();
		double iz = loc.getZ();
		World world = loc.getWorld();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		String s = world.getName() + "," + ix + "," + iy + "," + iz + "," + yaw + "," + pitch;
		Main.config.set(saveto, s);
		Main.me.saveConfig();
	}

}
