
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ListCommand extends SubCommand {
	
	public ListCommand()
	{
		super("list");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (!sender.hasPermission("Infected.List"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
		
		if (args.length != 1)
		{
			if (args[1].equalsIgnoreCase("Playing"))
			{
				sender.sendMessage(Msgs.Format_Header.getString("<title>", "Playing"));
				for (Player u : Lobby.getPlayersInGame())
					sender.sendMessage(Msgs.Format_List.getString("<player>", u.getDisplayName()));
			}
			else if (args[1].equalsIgnoreCase("Humans"))
			{
				sender.sendMessage(Msgs.Format_Header.getString("<title>", "Humans"));
				for (Player u : Lobby.getTeam(Team.Human))
					sender.sendMessage(Msgs.Format_List.getString("<player>", u.getDisplayName()));
				
			}
			else if (args[1].equalsIgnoreCase("Zombies"))
			{
				sender.sendMessage(Msgs.Format_Header.getString("<title>", "Zombies"));
				for (Player u : Lobby.getTeam(Team.Zombie))
					sender.sendMessage(Msgs.Format_List.getString("<player>", u.getDisplayName()));
				
			}
			else
				sender.sendMessage(Msgs.Help_Lists.getString("<lists>", "Playing, Humans, Zombies"));
			
		}
		else
			sender.sendMessage(Msgs.Help_Lists.getString("<lists>", "Playing, Humans, Zombies"));
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "teams", "show" });
	}
	
	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "Playing", "Humans", "Zombies" });
	}
}
