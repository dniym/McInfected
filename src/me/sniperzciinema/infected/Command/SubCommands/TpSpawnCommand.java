
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TpSpawnCommand extends SubCommand {

	public TpSpawnCommand()
	{
		super("tpspawn");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			if (!p.hasPermission("Infected.TpSpawn"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (ip.getCreating() == null)
					p.sendMessage(Msgs.Error_Arena_None_Set.getString());
				else
					if ((args.length == 3) && (args[1].equalsIgnoreCase("Global") || args[1].equalsIgnoreCase("Zombie") || args[1].equalsIgnoreCase("Human")))
					{
						Team team = args[1].equalsIgnoreCase("Human") ? Team.Human : args[1].equalsIgnoreCase("Zombie") ? Team.Zombie : Team.Global;
						Arena a = Lobby.getArena(ip.getCreating());
						int i = Integer.valueOf(args[2]) - 1;
						if (i < a.getSpawns(team).size())
						{
							p.teleport(LocationHandler.getPlayerLocation(a.getSpawns(team).get(i)));
							sender.sendMessage(Msgs.Command_Spawn_Tp.getString("<team>", team.toString(), "<spawn>", String.valueOf(i + 1)));
						}

						else
							sender.sendMessage(Msgs.Error_Arena_Not_A_Spawn.getString());

					}
					else
						sender.sendMessage(Msgs.Help_TpSpawn.getString());
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "visitspawn", "seespawn" });
	}
}
