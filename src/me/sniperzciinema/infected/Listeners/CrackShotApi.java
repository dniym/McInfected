
package me.sniperzciinema.infected.Listeners;

import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.GameMechanics.Deaths;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;


/**
 * The CrackShot Api Listener
 */
public class CrackShotApi implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerGetShot(WeaponDamageEntityEvent e) {
		
		// Is the victim a player?
		if (e.getVictim() instanceof Player)
		{
			Player victim = (Player) e.getVictim();
			Player killer = null;
			// If they're in the game
			if (Lobby.isInGame(victim))
			{
				
				// Get the attacker
				if (e.getPlayer() instanceof Player)
					killer = e.getPlayer();
				
				// Make sure they arn't on the same team
				if (!Lobby.oppositeTeams(killer, victim))
				{
					e.setDamage(0);
					e.setCancelled(true);
				}
				else if (Lobby.getGameState() != GameState.Started)
				{
					e.setDamage(0);
					e.setCancelled(true);
				}
				
				// If the game has fully started
				else
				{
					InfPlayer IPV = InfPlayerManager.getInfPlayer(victim);
					
					// Saves who hit the person last
					IPV.setLastDamager(killer);
					
					// If it was enough to kill the player
					if ((victim.getHealth() - e.getDamage()) <= 0)
					{
						e.setDamage(0);
						Deaths.playerDies(DeathType.Gun, killer, victim);
					}
					
				}
			}
		}
	}
}
