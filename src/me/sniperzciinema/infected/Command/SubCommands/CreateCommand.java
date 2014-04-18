
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;

import org.bukkit.block.Block;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CreateCommand extends SubCommand {

	public CreateCommand()
	{
		super("create");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			if (!p.hasPermission("Infected.Create"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
			else
				if (args.length != 1)
				{

					String arena = StringUtil.getWord(args[1]);

					if (Lobby.getArena(arena) != null)
						p.sendMessage(Msgs.Error_Arena_Already_Exists.getString());

					else
					{
						p.sendMessage(Msgs.Help_SetSpawn.getString());

						Arena a = new Arena(arena);
						Lobby.addArena(a);

						Infected.Menus.destroyMenu(Infected.Menus.voteMenu);
						Infected.Menus.voteMenu = Infected.Menus.getVoteMenu();

						if (args.length == 3)
							a.setCreator(args[2]);
						else
							a.setCreator("Unkown");

						Block b = p.getLocation().clone().add(0, -1, 0).getBlock();
						a.setBlock(b.getState().getData().toItemStack());

						ip.setCreating(arena);
						p.sendMessage(Msgs.Command_Arena_Created.getString("<arena>", arena));
					}
				}
				else
					p.sendMessage(Msgs.Help_Create.getString());
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "addmap", "addarena" });
	}
}
