
package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;

import me.xxsniperzzxx_sd.infected.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;


public class TagApi implements Listener {

	public Main plugin;

	public TagApi(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onNameTag(PlayerReceiveNameTagEvent e) {
		Player player = e.getPlayer();
		if (Infected.isPlayerInGame(player))
		{
			if (Infected.isPlayerHuman(e.getNamedPlayer()))
			{
				String string = ChatColor.GREEN + e.getNamedPlayer().getName();
				String s = string.substring(0, Math.min(string.length(), 16));
				e.setTag(s);
			}
			else if(Infected.isPlayerZombie(e.getNamedPlayer()))
			{
					String string = ChatColor.DARK_RED + e.getNamedPlayer().getName();
					String s = string.substring(0, Math.min(string.length(), 16));
					e.setTag(s);
			} else
				if (!plugin.getConfig().getBoolean("DisguiseCraft Support")){
					String string = ChatColor.GOLD + e.getNamedPlayer().getName();
					String s = string.substring(0, Math.min(string.length(), 16));
					e.setTag(s);
			}
		}
	}
}