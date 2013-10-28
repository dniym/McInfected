package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerVoteEvent;
import me.xxsniperzzxx_sd.infected.Messages.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Vote {

	public static void voteFor(Player player, String map){
		int votes = 1;
		for(String node : Main.config.getConfigurationSection("Votes.Extra Votes").getKeys(true)){
			if(!node.contains(".")){
				if(player.hasPermission("Infected.vote."+node))
					votes = Main.config.getInt("Votes.Extra Votes."+node);
			}
		}
		if (map.equalsIgnoreCase("Random"))
		{
			Random r = new Random();
			int i = r.nextInt(Main.possibleArenas.size());
			String voted4 = Main.possibleArenas.get(i);
			
			InfectedPlayerVoteEvent voteEvent = new InfectedPlayerVoteEvent(player, voted4, Main.Votes, Main.Voted4, votes);
			Bukkit.getServer().getPluginManager().callEvent(voteEvent);
			if (!voteEvent.isCancelled())
			{
				if (Main.Votes.containsKey(voted4))
				{
					Main.Votes.put(voted4, Main.Votes.get(voted4) + votes);
				} else
				{
					Main.Votes.put(voted4, votes);
				}
				Main.Voted4.put(player.getName(), voted4);
				for (Player players : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(players.getName()))
					{
						int v;
						for(v = 0; v != votes; v++)
							players.sendMessage(Messages.sendMessage(Msgs.VOTE_VOTEDFOR, player, voted4));
					}
				if (Main.config.getBoolean("ScoreBoard Support"))
				{
					ScoreBoard.updateScoreBoard();
				}
			}
		} else
		{
			InfectedPlayerVoteEvent voteEvent = new InfectedPlayerVoteEvent(player, map, Main.Votes, Main.Voted4, votes);
			Bukkit.getServer().getPluginManager().callEvent(voteEvent);
			if (!voteEvent.isCancelled())
			{
				if (Main.Votes.containsKey(map))
				{
					Main.Votes.put(map, Main.Votes.get(map) + votes);
				} else
				{
					Main.Votes.put(map, votes);
				}
				Main.Voted4.put(player.getName(), map);
				for (Player players : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(players.getName()))
					{
						int v;
						for(v = 0; v != votes; v++)
							players.sendMessage(Messages.sendMessage(Msgs.VOTE_VOTEDFOR, player, map));
					}
				if (Main.config.getBoolean("ScoreBoard Support"))
				{
					ScoreBoard.updateScoreBoard();
				}
			}
		}
	}
}
