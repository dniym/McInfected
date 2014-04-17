
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TpLobbyCommand extends SubCommand {

	public TpLobbyCommand()
	{
		super("tplobby");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			 if (!p.hasPermission("Infected.TpLobby"))
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

				else
				{
					p.teleport(Lobby.getLocation());
					p.sendMessage(Msgs.Command_Lobby_Tp.getString());
				}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "visitlobby", "seelobby" });
	}
}
