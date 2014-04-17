
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.GameMechanics.Game;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;


public class EndCommand extends SubCommand {

	public EndCommand()
	{
		super("end");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission("Infected.End"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

		else
			if (Lobby.getGameState() == GameState.InLobby)
				sender.sendMessage(Msgs.Error_Game_Started.getString());

			else
				Game.endGame(true);
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "finish", "stop" });
	}
}
