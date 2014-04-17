
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.GameMechanics.Deaths;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SuicideCommand extends SubCommand {

	public SuicideCommand()
	{
		super("suicide");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			if (!p.hasPermission("Infected.Suicide"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (!Lobby.isInGame(p))
					p.sendMessage(Msgs.Error_Game_Not_In.getString());

				else
					if (Lobby.getGameState() != GameState.Started)
						p.sendMessage(Msgs.Error_Game_Not_Started.getString());

					else
					{
						if (ip.getTeam() == Team.Human)
							ip.Infect();

						Deaths.playerDies(DeathType.Other, null, p);
						ip.respawn();
					}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "kill", "die", "stuck" });
	}
}
