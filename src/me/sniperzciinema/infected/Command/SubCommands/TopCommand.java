
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.GameMechanics.Sort;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.GameMechanics.Stats.StatType;
import me.sniperzciinema.infected.Handlers.UUID.UUIDManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.Time;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;


public class TopCommand extends SubCommand {

	public TopCommand()
	{
		super("top");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission("Infected.Top"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
		else
			if (args.length == 2)
			{
				String stat = args[1].toLowerCase();
				System.out.println(stat);
				if (stat.equals("kills") || stat.equals("deaths") || stat.equals("score") || stat.equals("time") || stat.equals("points") || stat.equals("killstreak"))
				{
					StatType type = StatType.valueOf(stat);

					int i = 1;
					sender.sendMessage(Msgs.Format_Header.getString("<title>", "Top " + stat.toString()));
					for (UUID id : Sort.topStats(type, 5))
					{
						if (id != null)
						{

							String name = UUIDManager.getPlayerName(id);
							if (i == 1)
								sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + i + ". " + ChatColor.GOLD + ChatColor.BOLD + (name.length() == 16 ? name : (name + "                 ").substring(0, 16)) + ChatColor.GREEN + " =-= " + ChatColor.GRAY + (type == StatType.time ? Time.getOnlineTime((long) Stats.getStat(type, id)) : Stats.getStat(type, id)));
							else
								if ((i == 2) || (i == 3))
									sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + i + ". " + ChatColor.GRAY + ChatColor.BOLD + (name.length() == 16 ? name : (name + "                ").substring(0, 16)) + ChatColor.GREEN + " =-= " + ChatColor.GRAY + (type == StatType.time ? Time.getOnlineTime((long) Stats.getStat(type, id)) : Stats.getStat(type, id)));
								else
									sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + i + ". " + ChatColor.WHITE + ChatColor.BOLD + (name.length() == 16 ? name : (name + "                 ").substring(0, 16)) + ChatColor.GREEN + " =-= " + ChatColor.DARK_GRAY + (type == StatType.time ? Time.getOnlineTime((long) Stats.getStat(type, id)) : Stats.getStat(type, id)));

						}
						i++;

						if (i == 6)
							break;
					}

				}
				else
					sender.sendMessage(Msgs.Error_Top_Not_Stat.getString());
			}
			else
				sender.sendMessage(Msgs.Help_Top.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "leaderboard", "leaderboards" });
	}

	@Override
	public List<String> getTabs() {
		List<String> stats = Arrays.asList(new String[] { "Kills", "Deaths", "Points", "Score", "Time", "KillStreak" });
		return stats;
	}
}
