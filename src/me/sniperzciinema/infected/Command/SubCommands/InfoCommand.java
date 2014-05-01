
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.Time;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;


public class InfoCommand extends SubCommand {
	
	public InfoCommand()
	{
		super("info");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		
		if (!sender.hasPermission("Infected.Info"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
		
		else if (Lobby.getGameState() == GameState.Disabled)
			sender.sendMessage(Msgs.Error_Misc_Plugin_Disabled.getString());
		
		else
		{
			sender.sendMessage("");
			sender.sendMessage(Msgs.Format_Header.getString("<title>", "Status"));
			sender.sendMessage(Msgs.Command_Info_Players.getString("<players>", String.valueOf(Lobby.getPlayersInGame().size())));
			sender.sendMessage(Msgs.Command_Info_State.getString("<state>", Lobby.getGameState().toString()));
			sender.sendMessage(Msgs.Command_Info_Time_Left.getString("<time>", Time.getTime((long) Lobby.getTimeLeft())));
		}
		
	}
	
	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "status" });
	}
	
	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "" });
	}
}
