
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.Material;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SetBlockCommand extends SubCommand {

	public SetBlockCommand()
	{
		super("setblock");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);

			if (!p.hasPermission("Infected.SetBlock"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (ip.getCreating() == null)
					p.sendMessage(Msgs.Error_Arena_None_Set.getString());
				else
					if (args.length == 2)
					{
						Arena arena = Lobby.getArena(ip.getCreating());
						if (Material.getMaterial(args[1]) != null)
						{
							arena.setBlock(new ItemStack(Material.getMaterial(args[1])));
							p.sendMessage(Msgs.Command_Arena_SetBlock.getString());
						}
						else
							if (Material.getMaterial(Integer.parseInt(args[1])) != null)
							{
								arena.setBlock(new ItemStack(Integer.parseInt(args[1])));
								p.sendMessage(Msgs.Command_Arena_SetBlock.getString());
							}
							else
								p.sendMessage(Msgs.Error_Misc_Not_A_Block.getString());
					}
					else
						p.sendMessage(Msgs.Help_Arena_SetBlock.getString());
		}

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "seticon" });
	}
}
