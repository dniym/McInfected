package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.fake.FakeEntityDamageEvent;


public class mcMMOEvents implements Listener {


	public Main plugin;

	public mcMMOEvents(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void mcMMOExtraDamage(FakeEntityDamageEvent e) {
		if(e.getEntity() instanceof Player){
			Player player = (Player)e.getEntity();
			if(Infected.isPlayerInGame(player)){
				e.setDamage(0);
				e.setCancelled(true);
			}	
		}
		
	}
	
	
	
}
