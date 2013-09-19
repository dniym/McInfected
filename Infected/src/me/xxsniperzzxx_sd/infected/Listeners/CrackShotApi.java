
package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Methods;
import me.xxsniperzzxx_sd.infected.Main.GameState;

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
					//If the attack happened before the game started
					if(Infected.getGameState() == GameState.VOTING)
						e.setCancelled(true);

					//Before a zombie is chosen	
					else if(Infected.getGameState() == GameState.BEFOREINFECTED){
						e.setDamage(0);
						e.setCancelled(true);
					}
				
					//If the game has fully started
					else{
						
						//Saves who hit the person last
						Main.Lasthit.put(victim.getName(), killer.getName());
						
						//If it was enough to kill the player
						if(victim.getHealth() - e.getDamage() <= 0){
							e.setDamage(0);
							Methods.playerDies(killer, victim);
						}
						
					}
				}
			}
		}
	}
}