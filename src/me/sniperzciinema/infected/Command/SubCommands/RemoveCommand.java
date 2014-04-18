
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;


public class RemoveCommand extends SubCommand {

	public RemoveCommand()
	{
		super("remove");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission("Infected.Remove"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
		else
			if (args.length != 1)
			{

				String arena = args[1];

				if (Lobby.getArena(arena) == null)
					sender.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString());

				else
				{
					Lobby.removeArena(Lobby.getArena(arena));

					Infected.Menus.destroyMenu(Infected.Menus.voteMenu);
					Infected.Menus.voteMenu = Infected.Menus.getVoteMenu();

					sender.sendMessage(Msgs.Command_Arena_Removed.getString("<arena>", arena));
				}
			}
			else
				sender.sendMessage(Msgs.Help_Remove.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "delete" });
	}
}
