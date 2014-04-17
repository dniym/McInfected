
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetArenaCommand extends SubCommand {

	public SetArenaCommand()
	{
		super("setarena");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			if (!p.hasPermission("Infected.SetArena"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
			else
			{
				if (args.length != 1)
				{
					String arena = StringUtil.getWord(args[1]);

					if (Lobby.getArenas().isEmpty())
						p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString("<arena>", "Default"));

					else if (Lobby.getArena(arena) == null)
						p.sendMessage(Msgs.Error_Arena_Doesnt_Exist.getString("<arena>", arena));

					else
					{
						ip.setCreating(arena);
						p.sendMessage(Msgs.Command_Arena_Set.getString("<arena>", arena));
					}
				} else
					p.sendMessage(Msgs.Help_SetArena.getString());

			}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "choosearena", "select", "selectarena" });
	}
}
