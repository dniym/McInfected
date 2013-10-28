package me.xxsniperzzxx_sd.infected.Messages;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Handlers.MapHandler;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class Messages {

	public static String sendMessage(Msgs message, Player player, String string) {
		String msg = String.valueOf(Files.getMessages().getString(message.getStatus()));
		String msg1 = msg;
		if (msg.contains("<player>") && !(player == null)) 
			msg1 = msg1.replaceAll("<player>", player.getName());

		if (msg.contains("<timeleft>") && !(string == null)) 
			msg1 = msg1.replaceAll("<timeleft>", String.valueOf(string));

		if (msg.contains("<humans>")) 
			msg1 = msg1.replaceAll("<humans>", String.valueOf(Main.humans.size()));
			
		if(msg.contains("<zombies>"))
			msg1 = msg1.replaceAll("<zombies>", String.valueOf(Main.zombies.size()));

		if(msg.contains("<map>"))
			msg1 = msg1.replaceAll("<map>", Main.playingin);

		if(msg.contains("<class>"))
			msg1 = msg1.replaceAll("<class>", string);

		if(msg.contains("<item>"))
			msg1 = msg1.replaceAll("<item>", string);

		if(msg.contains("<pointsneeded>"))
			msg1 = msg1.replaceAll("<pointsneeded>", string);

		if(msg.contains("<votedfor>"))
			msg1 = msg1.replaceAll("<votedfor>", string);
		
		if(msg.contains("<creator>"))
			msg1 = msg1.replaceAll("<creator>", MapHandler.getMapCreator(Main.playingin));
		
		if (msg.contains("&")) 
			msg1 = ChatColor.translateAlternateColorCodes('&', msg1);

		String newMsg = msg1;
		
		if(!message.getStatus().contains("Format"))
			newMsg = Main.I + newMsg;
		
		return newMsg;
	}
}
