
package me.sniperzciinema.infected.Listeners;

import me.sniperzciinema.infected.Handlers.Lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.fake.FakeEntityDamageByEntityEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.unarmed.McMMOPlayerDisarmEvent;


/**
 * The mcMMO Api Listener
 * 
 */
public class mcMMOEvents implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void mcMMOExtraDamage(FakeEntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			Player attacker = (Player) e.getDamager();
			if (Lobby.isInGame(player) || Lobby.isInGame(attacker))
			{
				e.setDamage(0);
				e.setCancelled(true);
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void mcMMODisarm(McMMOPlayerDisarmEvent e) {
		if (Lobby.isInGame(e.getPlayer()) || Lobby.isInGame(e.getDefender()))
			e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void mcMMOAbilityActivate(McMMOPlayerAbilityActivateEvent e) {
		if (Lobby.isInGame(e.getPlayer()))
			e.setCancelled(true);
	}

}
