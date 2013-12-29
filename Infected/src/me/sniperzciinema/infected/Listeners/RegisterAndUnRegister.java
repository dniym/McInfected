
package me.sniperzciinema.infected.Listeners;

import me.sniperzciinema.infected.Main;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * Creating/Removing the InfPlayers
 * 
 */
public class RegisterAndUnRegister implements Listener {

	// When a player joins the server, create a InfPlayer for them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoinCreateInfPlayer(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		InfPlayer IP = new InfPlayer(p);
		InfPlayerManager.createInfPlayer(IP);
	}

	// When a player leaves the server willingly, delete the InfPlayer of them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLeaveDeleteInfPlayer(final PlayerQuitEvent e) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
		{

			@Override
			public void run() {

				InfPlayer IP = InfPlayerManager.getInfPlayer(e.getPlayer());
				InfPlayerManager.removeInfPlayer(IP);
			}
		}, 2L);
	}

	// When a player leaves the server by kick, delete the InfPlayer of them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onKickedDeleteInfPlayer(final PlayerKickEvent e) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
		{

			@Override
			public void run() {

				InfPlayer IP = InfPlayerManager.getInfPlayer(e.getPlayer());
				InfPlayerManager.removeInfPlayer(IP);
			}
		}, 2L);
	}

}
