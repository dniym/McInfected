
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;


public class ArenasCommand extends SubCommand {

	public ArenasCommand()
	{
		super("arenas");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission("Infected.Arenas"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

		else
		{
			sender.sendMessage(Msgs.Format_Header.getString("<title>", "Arenas"));

			StringBuilder valid = new StringBuilder();
			for (Object o : Lobby.getValidArenas())
			{
				valid.append(o.toString());
				if (Lobby.getValidArenas().size() > 1)
					valid.append(", ");
			}

			StringBuilder inValid = new StringBuilder();
			for (Object o : Lobby.getInValidArenas())
			{
				inValid.append(o.toString());
				if (Lobby.getInValidArenas().size() > 1)
					inValid.append(", ");
			}

			sender.sendMessage(Msgs.Command_Arena_List.getString("<valid>", valid.toString(), "<invalid>", inValid.toString()));
		}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "maps", "" });
	}
}
