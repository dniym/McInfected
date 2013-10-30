
package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RegisterAndUnRegister implements Listener{
	
	private InfPlayerManager IPM = Main.InfPlayerManager;
	
	
//When a player joins the server, create a InfPlayer for them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoinCreateCrankedPlayer(PlayerJoinEvent e){
		Player p = e.getPlayer();
		InfPlayer IP= new InfPlayer(p);
		IPM.createInfPlayer(IP);
	}
//When a player leaves the server willingly, delete the InfPlayer of them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLeaveDeleteCrankedPlayer(PlayerQuitEvent e){
		InfPlayer IP = IPM.getInfPlayer(e.getPlayer());
		IPM.removeInfPlayer(IP);
	}

	//When a player leaves the server by kick, delete the InfPlayer of them
	@EventHandler(priority = EventPriority.NORMAL)
	public void onKickedDeleteCrankedPlayer(PlayerKickEvent e){
		InfPlayer cp = IPM.getInfPlayer(e.getPlayer());
		IPM.removeInfPlayer(cp);
	}

}
