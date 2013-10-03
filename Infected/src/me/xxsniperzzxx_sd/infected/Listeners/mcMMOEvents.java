package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.fake.FakeEntityDamageByEntityEvent;


public class mcMMOEvents implements Listener {


	public Main plugin;

	public mcMMOEvents(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void mcMMOExtraDamage(FakeEntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player user = (Player)e.getDamager();
			Player player = (Player)e.getEntity();
			if(Infected.isPlayerInGame(player) && Infected.isPlayerInGame(user)){
				e.setDamage(0);
				e.setCancelled(true);
			}	
		}
		
	}
	
	
	
}
