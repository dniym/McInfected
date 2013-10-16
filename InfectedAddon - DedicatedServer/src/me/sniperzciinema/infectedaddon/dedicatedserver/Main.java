package me.sniperzciinema.infectedaddon.dedicatedserver;

import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerLeaveEvent;

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
	public void onPlayerLeave(InfectedPlayerLeaveEvent event) {
		if(event.isCommand())
			event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage().toLowerCase();
		msg = msg.replaceAll("/join", "/inf join");
		msg = msg.replaceAll("/leave", "/inf leave");
		msg = msg.replaceAll("/vote", "/inf vote");
		msg = msg.replaceAll("/chat", "/inf chat");
		msg = msg.replaceAll("/grenade", "/inf grenade");
		msg = msg.replaceAll("/classes", "/inf classes");
		msg = msg.replaceAll("/refresh", "/inf refresh");
		msg = msg.replaceAll("/info", "/inf info");
		msg = msg.replaceAll("/suicide", "/inf suicide");
		msg = msg.replaceAll("/setlobby", "/inf setlobby");
		msg = msg.replaceAll("/list", "/inf list");
		msg = msg.replaceAll("/help", "/inf help");
		msg = msg.replaceAll("/vote", "/inf vote");
		msg = msg.replaceAll("/start", "/inf start");
		msg = msg.replaceAll("/end", "/inf end");
		msg = msg.replaceAll("/arenas", "/inf arenas");
		msg = msg.replaceAll("/admin", "/inf admin");
		msg = msg.replaceAll("/stats", "/inf stats");
		msg = msg.replaceAll("/tpspawn", "/inf tpspawn");
		msg = msg.replaceAll("/tplobby", "/inf tplobby");
		msg = msg.replaceAll("/delspawn", "/inf delspawn");
		msg = msg.replaceAll("/spawns", "/inf spawns");
		msg = msg.replaceAll("/setspawn", "/inf setspawn");
		msg = msg.replaceAll("/create", "/inf create");
		msg = msg.replaceAll("/remove", "/inf remove");
		msg = msg.replaceAll("/top", "/inf top");
		msg = msg.replaceAll("/setarena", "/inf setarena");
		msg = msg.replaceAll("/rank", "/inf rank");
		event.setMessage(msg);
	}
}
