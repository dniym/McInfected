package me.xxsniperzzxx_sd.infected;

import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.MapHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Messages {

	public static String sendMessage(Msgs message, Player player, String string) {
		String msg = String.valueOf(Files.getMessages().getString(message.getStatus()));
		String msg1 = msg;
		if (msg1.contains("<player>") && !(player == null)) 
			msg1 = msg1.replaceAll("<player>", player.getName());

		if (msg1.contains("<timeleft>") && !(string == null)) 
			msg1 = msg1.replaceAll("<timeleft>", String.valueOf(string));

		if (msg.contains("<humans>")) 
			msg1 = msg1.replaceAll("<humans>", String.valueOf(Main.humans.size()));
			
		if(msg.contains("<zombies>"))
			msg1 = msg1.replaceAll("<zombies>", String.valueOf(Main.zombies.size()));

		if(msg.contains("<map>"))
			msg1 = msg1.replaceAll("<map>", Main.playingin);

		if(msg.contains("<class>"))
			msg1 = msg1.replaceAll("<class>", string);

		if(msg.contains("<votedfor>"))
			msg1 = msg1.replaceAll("<votedfor>", string);
		
		if(msg.contains("<creator>"))
			msg1 = msg1.replaceAll("<creator>", MapHandler.getMapCreator(Main.playingin));
		
		if (msg.contains("&")) 
			msg1 = ChatColor.translateAlternateColorCodes('&', msg1);

		String newMsg = Main.I + msg1;
		return newMsg;
	}
}
