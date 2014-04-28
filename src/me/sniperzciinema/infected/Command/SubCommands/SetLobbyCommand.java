
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetLobbyCommand extends SubCommand {

	public SetLobbyCommand()
	{
		super("setlobby");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			if (!p.hasPermission("Infected.SetLobby"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
			{
				Lobby.setLocation(p.getLocation());
				p.sendMessage(Msgs.Command_Lobby_Set.getString());
			}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "sl" });
	}

	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "" });
	}
}
