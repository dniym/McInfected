
package me.sniperzciinema.infectedv2.Listeners;

import me.sniperzciinema.infectedv2.Main;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Messages.RandomChatColor;

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
		Player p = e.getPlayer();
		if (Lobby.isInGame(p))
		{
			if (Lobby.isHuman(e.getNamedPlayer()))
			{
				String string = ChatColor.DARK_GREEN + e.getNamedPlayer().getName();
				String s = string.substring(0, Math.min(string.length(), 16));
				e.setTag(s);
			} else if (Lobby.isZombie(e.getNamedPlayer()))
			{
				String string = ChatColor.DARK_RED + e.getNamedPlayer().getName();
				String s = string.substring(0, Math.min(string.length(), 16));
				e.setTag(s);
			} else
			{
				String string = RandomChatColor.getColor() + e.getNamedPlayer().getName();
				String s = string.substring(0, Math.min(string.length(), 16));
				e.setTag(s);
			}
		}
	}
}