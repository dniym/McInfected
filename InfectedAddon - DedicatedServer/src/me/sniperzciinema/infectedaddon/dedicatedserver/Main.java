package me.sniperzciinema.infectedaddon.dedicatedserver;

import me.sniperzciinema.infected.Events.InfectedCommandEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
		
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable(){
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().performCommand("infected join");
		event.setJoinMessage("");
	}
	
	@EventHandler
	public void onPlayerTryToLeave(InfectedCommandEvent event) {
		if(event.getArgs()[0].equalsIgnoreCase("Leave"))
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage();
		msg = msg.toLowerCase().replaceAll("/join", "/inf join");
		msg = msg.toLowerCase().replaceAll("/leave", "/inf leave");
		msg = msg.toLowerCase().replaceAll("/vote", "/inf vote");
		msg = msg.toLowerCase().replaceAll("/files", "/inf files");
		msg = msg.toLowerCase().replaceAll("/chat", "/inf chat");
		msg = msg.toLowerCase().replaceAll("/grenade", "/inf grenade");
		msg = msg.toLowerCase().replaceAll("/grenades", "/inf grenade");
		msg = msg.toLowerCase().replaceAll("/shop", "/inf shop");
		msg = msg.toLowerCase().replaceAll("/store", "/inf shop");
		msg = msg.toLowerCase().replaceAll("/classes", "/inf classes");
		msg = msg.toLowerCase().replaceAll("/info", "/inf info");
		msg = msg.toLowerCase().replaceAll("/suicide", "/inf suicide");
		msg = msg.toLowerCase().replaceAll("/setlobby", "/inf setlobby");
		msg = msg.toLowerCase().replaceAll("/list", "/inf list");
		msg = msg.toLowerCase().replaceAll("/help", "/inf help");
		msg = msg.toLowerCase().replaceAll("/vote", "/inf vote");
		msg = msg.toLowerCase().replaceAll("/start", "/inf start");
		msg = msg.toLowerCase().replaceAll("/end", "/inf end");
		msg = msg.toLowerCase().replaceAll("/arenas", "/inf arenas");
		msg = msg.toLowerCase().replaceAll("/admin", "/inf admin");
		msg = msg.toLowerCase().replaceAll("/stats", "/inf stats");
		msg = msg.toLowerCase().replaceAll("/tpspawn", "/inf tpspawn");
		msg = msg.toLowerCase().replaceAll("/tplobby", "/inf tplobby");
		msg = msg.toLowerCase().replaceAll("/delspawn", "/inf delspawn");
		msg = msg.toLowerCase().replaceAll("/spawns", "/inf spawns");
		msg = msg.toLowerCase().replaceAll("/setspawn", "/inf setspawn");
		msg = msg.toLowerCase().replaceAll("/create", "/inf create");
		msg = msg.toLowerCase().replaceAll("/remove", "/inf remove");
		msg = msg.toLowerCase().replaceAll("/top", "/inf top");
		msg = msg.toLowerCase().replaceAll("/setarena", "/inf setarena");
		event.setMessage(msg);
	}
}
