
package me.sniperzciinema.infected.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.sniperzciinema.infected.Handlers.Lobby;

import com.massivecraft.factions.event.FactionsEventPowerChange;
import com.massivecraft.factions.event.FactionsEventPvpDisallowed;
import com.massivecraft.factions.event.FactionsEventPowerChange.PowerChangeReason;


public class FactionsEvents implements Listener {

	@EventHandler
	public void factionPVP(FactionsEventPvpDisallowed e) {
		if (Lobby.isInGame(e.getAttacker()) && Lobby.isInGame(e.getDefender())){
			e.setCancelled(true);	
		}
	}

	@EventHandler
	public void factionLoosePower(FactionsEventPowerChange e) {
		if (e.getReason() == PowerChangeReason.DEATH)
			if (Lobby.isInGame(Bukkit.getPlayer(e.getUPlayer().getName())))
				e.setNewPower(e.getUPlayer().getPower());
	}

}
