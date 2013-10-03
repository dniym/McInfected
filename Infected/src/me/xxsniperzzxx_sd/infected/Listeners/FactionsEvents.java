package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FactionsEventPowerChange;
import com.massivecraft.factions.event.FactionsEventPvpDisallowed;
import com.massivecraft.factions.event.FactionsEventPowerChange.PowerChangeReason;


public class FactionsEvents implements Listener {


	public Main plugin;

	public FactionsEvents(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void factionPVP(FactionsEventPvpDisallowed e) {
		if(Infected.isPlayerInGame(e.getAttacker()) && Infected.isPlayerInGame(e.getDefender()))
			e.getEvent().setCancelled(false);
	}

	@EventHandler
	public void factionLoosePower(FactionsEventPowerChange e) {
		if(e.getReason() == PowerChangeReason.DEATH)
			if(Infected.isPlayerInGame(Bukkit.getPlayer(e.getUPlayer().getName())))
				e.setNewPower(e.getUPlayer().getPower());
		}
	
	
	
}
