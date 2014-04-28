
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ChatCommand extends SubCommand {

	public ChatCommand()
	{
		super("chat");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);

			if (!p.hasPermission("Infected.Chat"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (!Lobby.isInGame(p))
					p.sendMessage(Msgs.Error_Game_Not_In.getString());

				else
					if (args.length == 1)
					{
						if (!ip.isInfChatting())
						{
							ip.setInfChatting(true);
							p.sendMessage(Msgs.Command_InfChat.getString("<state>", "in to"));
						}
						else
						{
							ip.setInfChatting(false);
							p.sendMessage(Msgs.Command_InfChat.getString("<state>", "out of"));
						}
					}
					else
					{
						StringBuilder message = new StringBuilder(args[1]);
						for (int arg = 2; arg < args.length; arg++)
							message.append(" ").append(args[arg]);

						for (Player u : Bukkit.getOnlinePlayers())
							if ((ip.getTeam() == InfPlayerManager.getInfPlayer(p).getTeam()) || u.hasPermission("Infected.Chat.Spy"))
								u.sendMessage(Msgs.Format_InfChat.getString("<team>", ip.getTeam().toString(), "<player>", p.getName(), "<score>", String.valueOf(ip.getScore()), "<message>", message.toString()));
					}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "talk", "message", "msg" });
	}

	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "" });
	}
}
