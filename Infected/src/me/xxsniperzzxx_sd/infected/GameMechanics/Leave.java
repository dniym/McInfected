
package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerLeaveEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Leave {

	public static void leaveGame(Player player, boolean isCommand) {


		InfectedPlayerLeaveEvent leaveEvent = new InfectedPlayerLeaveEvent(player, Main.inGame, Infected.playerGetGroup(player), isCommand);
		Bukkit.getServer().getPluginManager().callEvent(leaveEvent);
		
			if(!leaveEvent.isCancelled()){
			// Reset the player to before they joined Infected
			if (Main.inGame.contains(player.getName()))
				Main.inGame.remove(player.getName());
			if (Main.inLobby.contains(player.getName()))
				Main.inLobby.remove(player.getName());
	
			player.sendMessage(Messages.sendMessage(Msgs.LEAVE_YOUHAVELEFT, null, null));
			Reset.resetp(player);
	
			//Is there anyone left in the lobby?
			if(Infected.listInGame().size() == 0){

				//Reset all the timers, lists, etc(Not including the ones for people in Infected)
				Reset.resetInf();
			}
			
			// If nothing has started yet, just inform players they left
			else if (Infected.getGameState() == GameState.INLOBBY)
			{
				for (String name : Infected.listInGame())
				{
					Player user = Bukkit.getPlayer(name);
					user.sendMessage(Messages.sendMessage(Msgs.LEAVE_NOEFFECT, player, null));
				}
			}
	
			// If the game isn't fully started yet, this includes Voting, and before and Infecteds chosen
			else if (Infected.getGameState() == GameState.VOTING || Infected.getGameState() == GameState.BEFOREINFECTED)
			{
				// If theres only one person left in the lobby, end the game
				if (Main.inGame.size() == 1)
				{
					for (String name : Infected.listInGame())
					{
						Player user = Bukkit.getPlayer(name);
						user.sendMessage(Messages.sendMessage(Msgs.LEAVE_NOTENOUGHPLAYERS, player, null));
						Reset.tp2LobbyAfter(user);
					}

					//Reset all the timers, lists, etc(Not including the ones for people in Infected)
					Reset.resetInf();
				}
				
				//More then one person left in Infected, just inform players they left
				else
				{
					for (String name : Infected.listInGame())
					{
						Player user = Bukkit.getPlayer(name);
						user.sendMessage(Messages.sendMessage(Msgs.LEAVE_NOEFFECT, player, null));
					}
				}
			}
			
		
			//If the game has fully started
			else if (Infected.getGameState() == GameState.STARTED)
			{
				//If theres only one person left in the game, end the game
				if (Main.inGame.size() == 1)
				{
					for (String name : Infected.listInGame())
					{
						Player user = Bukkit.getPlayer(name);
						user.sendMessage(Messages.sendMessage(Msgs.LEAVE_NOTENOUGHPLAYERS, player, null));
						Reset.tp2LobbyAfter(user);
					}
	
					//Reset all the timers, lists, etc(Not including the ones for people in Infected)
					Reset.resetInf();
				}
				
				// If theres no zombies left
				else if (Main.zombies.size() == 0)
				{
					
					//Choose someone new to be the zombie
					Zombify.newZombieSetUpEveryOne();
				}
				
				// If theres no humans left(Player who left was human, or the new zombie was the only human)
				if (Main.humans.size() == 0)
				{
					//Tell whos ever in Infected that theres not enough humans left and the game is ending
				for (String name : Infected.listInGame())
					{
						Player user = Bukkit.getPlayer(name);
						user.sendMessage(Messages.sendMessage(Msgs.LEAVE_NOTENOUGHPLAYERS, player, null));
						Reset.tp2LobbyAfter(user);
					}
				
				//Reset all the timers, lists, etc(Not including the ones for people in Infected)
				Reset.resetInf();
				}
				
				//If it is anything else(Game is over, etc) just tell others they left
				else
				{
				for (String name : Infected.listInGame())
					{
						Player user = Bukkit.getPlayer(name);
						user.sendMessage(Messages.sendMessage(Msgs.LEAVE_NOEFFECT, player, null));
					}
				}
			}
	
			//Update the scoreboard stats
			ScoreBoard.updateScoreBoard();
		}
	}
}
