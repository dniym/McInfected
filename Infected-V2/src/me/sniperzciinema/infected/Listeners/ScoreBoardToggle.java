
package me.sniperzciinema.infected.Listeners;

import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;


//TODO: Set up breaking/placing blocks to work with listeners

public class ScoreBoardToggle implements Listener {

	// Show rankings when a player sneaks
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
		if(Lobby.isInGame(e.getPlayer())){
			InfPlayer ip = InfPlayerManager.getInfPlayer(e.getPlayer());
			ip.getScoreBoard().switchShowing();
			ip.getScoreBoard().showProperBoard();
		}
	}
}