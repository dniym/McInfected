
package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.GameMechanics.DeathType;
import me.xxsniperzzxx_sd.infected.GameMechanics.Deaths;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;


public class CrackShotApi implements Listener {

	public Main plugin;

	public CrackShotApi(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerGetShot(WeaponDamageEntityEvent e) {

		//Is the victim a player?
		if (e.getVictim() instanceof Player)
		{
			Player victim = (Player) e.getVictim();	
			Player killer = null;
			
			//If they're in the game
			if(Infected.isPlayerInGame(victim)){

				//Get the attacker
				if(e.getPlayer() instanceof Player)
					killer = e.getPlayer();

				//Make sure they arn't on the same team
				if (Infected.isPlayerHuman(killer) && Infected.isPlayerHuman(victim))
					e.setCancelled(true);
				
				if (Infected.isPlayerZombie(killer) && Infected.isPlayerZombie(victim))
					e.setCancelled(true);
				
				else{
					Lobby Lobby = Main.Lobby;
					 if(Lobby.getGameState() != GameState.Started){
						e.setDamage(0);
						e.setCancelled(true);
					}
				
					//If the game has fully started
					else{
						InfPlayer IPV = InfPlayerManager.getPlayer(victim);
						
						//Saves who hit the person last
						IPV.setLastDamager(killer);
						
						//If it was enough to kill the player
						if(victim.getHealth() - e.getDamage() <= 0){
							e.setDamage(0);
							Deaths.playerDies(DeathType.Gun, killer, victim);
						}
						
					}
				}
			}
		}
	}
}