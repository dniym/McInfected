package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.LocationHandler;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;


public class UpdateInfoSigns {
	
	public static void update(){

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.me, new Runnable()
		{

			@Override
			public void run() {
				if (!Files.getSigns().getStringList("Info Signs").isEmpty())
				{
					for (String loc : Files.getSigns().getStringList("Info Signs"))
					{
						String status;

						if (Infected.getGameState() == GameState.VOTING)
						{
							status = "Voting";
						}
						if (Infected.getGameState() == GameState.BEFOREINFECTED)
						{
							status = "B4 Infected";
						}
						if (Infected.getGameState() == GameState.STARTED)
						{
							status = "Started";
						} else
						{
							status = "In Lobby";
						}

						int time = Main.currentTime;

						Location location = LocationHandler.getObjectLocation(loc);
						if (location.getBlock().getType() == Material.SIGN_POST || location.getBlock().getType() == Material.WALL_SIGN)
						{
							Sign sign = (Sign) location.getBlock().getState();
							sign.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Infected.listInGame().size()));
							sign.setLine(2, ChatColor.GOLD + status);
							if (Infected.getGameState() == GameState.STARTED || Infected.getGameState() == GameState.VOTING)
								sign.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));
							else
								sign.setLine(3, "");
							sign.update();
						}
					}

				}
			}
		}, 100L, Main.config.getInt("Info Signs.Refresh Time") * 20);
	}
}
