package me.xxsniperzzxx_sd.infected.Tools;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Misc.LocationHandler;

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
						Lobby Lobby = Main.Lobby;
						String status = Lobby.getGameState().toString();
						
						int time = Lobby.getCurrentTime();

						Location location = LocationHandler.getObjectLocation(loc);
						if (location.getBlock().getType() == Material.SIGN_POST || location.getBlock().getType() == Material.WALL_SIGN)
						{
							Sign sign = (Sign) location.getBlock().getState();
							sign.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Main.Lobby.getInGame().size()));
							sign.setLine(2, ChatColor.GOLD + status);
							if (Lobby.getGameState() == GameState.Started ||Lobby.getGameState() == GameState.Infecting || Lobby.getGameState() == GameState.Voting)
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
