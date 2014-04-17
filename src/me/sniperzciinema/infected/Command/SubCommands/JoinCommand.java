
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Events.InfectedJoinEvent;
import me.sniperzciinema.infected.GameMechanics.Deaths;
import me.sniperzciinema.infected.GameMechanics.Equip;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class JoinCommand extends SubCommand {

	public JoinCommand()
	{
		super("join");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);

			if (!p.hasPermission("Infected.Join"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (Lobby.getGameState() == GameState.Disabled)
					p.sendMessage(Msgs.Error_Misc_Plugin_Disabled.getString());

				else
					if (Lobby.getLocation() == null)
						p.sendMessage(Msgs.Error_Lobby_Doesnt_Exist.getString());

					else
						if (Lobby.getValidArenas().isEmpty())
							p.sendMessage(Msgs.Error_Arena_No_Valid.getString());

						else
							if (Settings.isJoiningDuringGamePrevented() && (Lobby.getGameState() == GameState.Started || Lobby.getGameState() == GameState.Infecting || Lobby.getGameState() == GameState.GameOver))
								p.sendMessage(Msgs.Error_Misc_Joining_While_Game_Started.getString());

							else
								if (Lobby.isInGame(p))
									p.sendMessage(Msgs.Error_Game_In.getString());

								else
								{
									InfectedJoinEvent je = new InfectedJoinEvent(p);
									Bukkit.getPluginManager().callEvent(je);

									for (Player player : Lobby.getPlayersInGame())
										player.sendMessage(Msgs.Game_Joined_They.getString("<player>", p.getName()));

									ip.setInfo();
									Lobby.addPlayerInGame(p);

									// If the game isn't started and isn't
									// infecting then
									// the players are all still in the lobby
									if (Lobby.getGameState() != GameState.Started && Lobby.getGameState() != GameState.Infecting)
										ip.tpToLobby();

									p.sendMessage(Msgs.Game_Joined_You.getString());

									// If the game hasn't started and there's
									// enough players
									// for an autostart, start the timer
									if (Lobby.getGameState() == GameState.InLobby && Lobby.getPlayersInGame().size() >= Settings.getRequiredPlayers())
									{
										Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
										{

											@Override
											public void run() {

												Lobby.timerStartVote();
											}
										}, 100L);
									}
									// If voting has started, tell the new
									// player how to
									// vote
									else
										if (Lobby.getGameState() == GameState.Voting)
											p.sendMessage(Msgs.Help_Vote.getString());

										// If it's already looking for the first
										// infected,
										// respawn them as a human and equip
										// them
										else
											if (Lobby.getGameState() == GameState.Infecting)
											{
												ip.setTimeIn(System.currentTimeMillis() / 1000);
												ip.respawn();
												Equip.equip(p);
											}
											// If the game has started already
											// make the player a
											// zombie without calling any
											// deaths(Event and stats)
											else
												if (Lobby.getGameState() == GameState.Started)
												{
													ip.setTimeIn(System.currentTimeMillis() / 1000);
													Deaths.playerDiesWithoutDeathStat(p);
												}
								}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "j" });
	}
}
