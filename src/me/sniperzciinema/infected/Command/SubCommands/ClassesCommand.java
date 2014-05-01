
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ClassesCommand extends SubCommand {
	
	public ClassesCommand()
	{
		super("classes");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if (!p.hasPermission("Infected.Classes"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
			
			else if (!Lobby.isInGame(p))
				p.sendMessage(Msgs.Error_Game_Not_In.getString());
			
			else if ((Lobby.getGameState() == GameState.Infecting) || (Lobby.getGameState() == GameState.Started))
				p.sendMessage(Msgs.Error_Game_Started.getString());
			
			else
				Infected.Menus.teamMenu.open(p);
			
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "kits", "class", "kit" });
	}
	
	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "" });
	}
}
