
package me.sniperzciinema.infected.Tools;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;


public class UpdateInfoSigns {

	/**
	 * Update all signs every few seconds(Set in the config)
	 */
	public static void update() {

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Infected.me, new Runnable()
		{

			@Override
			public void run() {
				if (!Files.getSigns().getStringList("Info Signs").isEmpty())
				{
					for (String loc : Files.getSigns().getStringList("Info Signs"))
					{
						String status = Lobby.getGameState().toString();

						int time = Lobby.getTimeLeft();

						Location location = LocationHandler.getObjectLocation(loc);
						if (location.getBlock().getType() == Material.SIGN_POST || location.getBlock().getType() == Material.WALL_SIGN)
						{
							Sign sign = (Sign) location.getBlock().getState();
							sign.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Lobby.getInGame().size()));
							sign.setLine(2, ChatColor.GOLD + status);
							if (Lobby.getGameState() == GameState.Started || Lobby.getGameState() == GameState.Infecting || Lobby.getGameState() == GameState.Voting)
								sign.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));
							else
								sign.setLine(3, "");
							sign.update();
						}
					}

				}
			}
		}, 100L, Settings.InfoSignsUpdateTime() * 20);
	}
}
