
package me.sniperzciinema.infected.Listeners;

import me.sniperzciinema.infected.Handlers.Lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.fake.FakeEntityDamageEvent;


public class mcMMOEvents implements Listener {

	@EventHandler
	public void mcMMOExtraDamage(FakeEntityDamageEvent e) {
		if (e.getEntity() instanceof Player)
		{
			Player player = (Player) e.getEntity();
			if (Lobby.isInGame(player))
			{
				e.setDamage(0);
				e.setCancelled(true);
			}
		}

	}

}
