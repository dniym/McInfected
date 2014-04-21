
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Extras.Menus;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class VoteCommand extends SubCommand {

	public VoteCommand()
	{
		super("vote");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);

			if (!p.hasPermission("Infected.Vote"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (!Lobby.isInGame(p))
					p.sendMessage(Msgs.Error_Game_Not_In.getString());

				else
					if ((Lobby.getGameState() != GameState.Voting) && (Lobby.getGameState() != GameState.InLobby))
						p.sendMessage(Msgs.Error_Game_Started.getString());

					else
						if (ip.getVote() != null)
							p.sendMessage(Msgs.Error_Already_Voted.getString());
						else
							// If the user didn't specify an arena, open the
							// voting
							// GUI
							if (args.length == 1)
								Infected.Menus.voteMenu.open(p);
							else
							{
								// Check if the user voted for Random
								Arena arena;
								if (args[1].equalsIgnoreCase("Random"))
								{
									int i;
									Random r = new Random();
									i = r.nextInt(Lobby.getArenas().size());
									arena = Lobby.getArenas().get(i);
									while (!Lobby.isArenaValid(arena))
									{
										i = r.nextInt(Lobby.getArenas().size());
										arena = Lobby.getArenas().get(i);
									}
								}
								else
									// Assign arena to what ever the user said
									arena = Lobby.getArena(args[1]);
								// If its a valid arena, let the user vote and
								// set
								// everything
								if (Lobby.isArenaValid(arena))
								{
									arena.setVotes(arena.getVotes() + ip.getAllowedVotes());
									ip.setVote(arena);

									Infected.Menus = new Menus();

									for (Player u : Lobby.getPlayersInGame())
									{
										u.sendMessage(Msgs.Command_Vote.getString("<player>", p.getName(), "<arena>", arena.getName()) + ChatColor.GRAY + (ip.getAllowedVotes() != 0 ? " (x" + ip.getAllowedVotes() + ")" : ""));
										InfPlayer up = InfPlayerManager.getInfPlayer(u);
										up.getScoreBoard().showProperBoard();
									}
								}
								// If its not a valid arena tell them that
								else
									p.sendMessage(Msgs.Error_Arena_Not_Valid.getString());
							}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "choose" });
	}
}
