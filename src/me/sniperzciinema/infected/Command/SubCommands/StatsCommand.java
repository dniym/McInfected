
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.GameMechanics.KDRatio;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.Handlers.UUID.UUIDManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.Time;
import me.sniperzciinema.infected.Tools.TabCompletionHelper;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StatsCommand extends SubCommand {

	public StatsCommand()
	{
		super("stats");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;

			if (!p.hasPermission("Infected.Stats"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
			else
				if (args.length != 1)
				{

					if (!p.hasPermission("Infected.Stats.Other"))
						p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

					else
					{
						String user = args[1];
						UUID id = UUIDManager.getPlayerUUID(user);

						p.sendMessage("");
						p.sendMessage(Msgs.Format_Header.getString("<title>", user));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(id, Settings.VaultEnabled()) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(id));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getOnlineTime((long) Stats.getPlayingTime(id)));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(id) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(id) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + KDRatio.KD(user));
						p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(id));
					}
				}
				else
				{
					String user = p.getName();
					UUID id = p.getUniqueId();
					p.sendMessage("");
					p.sendMessage(Msgs.Format_Header.getString("<title>", user));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Points: " + ChatColor.GOLD + Stats.getPoints(id, Settings.VaultEnabled()) + ChatColor.GREEN + "     Score: " + ChatColor.GOLD + Stats.getScore(id));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Playing Time: " + ChatColor.GOLD + Time.getOnlineTime((long) Stats.getPlayingTime(id)));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Kills: " + ChatColor.GOLD + Stats.getKills(id) + ChatColor.GREEN + "     Deaths: " + ChatColor.GOLD + Stats.getDeaths(id) + ChatColor.GREEN + "    KDR: " + ChatColor.GOLD + KDRatio.KD(user));
					p.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + "Highest KillStreak: " + ChatColor.GOLD + Stats.getHighestKillStreak(id));
				}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] {});
	}

	@Override
	public List<String> getTabs() {
		List<String> players = new ArrayList<String>();
		players.add("");
		players.addAll(TabCompletionHelper.getOnlinePlayers());
		return players;
	}
}
