
package me.sniperzciinema.infected.Command;

import java.util.ArrayList;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommands.AddonsCommand;
import me.sniperzciinema.infected.Command.SubCommands.AdminCommand;
import me.sniperzciinema.infected.Command.SubCommands.ArenasCommand;
import me.sniperzciinema.infected.Command.SubCommands.ChatCommand;
import me.sniperzciinema.infected.Command.SubCommands.ClassesCommand;
import me.sniperzciinema.infected.Command.SubCommands.CreateCommand;
import me.sniperzciinema.infected.Command.SubCommands.DelSpawnCommand;
import me.sniperzciinema.infected.Command.SubCommands.EndCommand;
import me.sniperzciinema.infected.Command.SubCommands.FilesCommand;
import me.sniperzciinema.infected.Command.SubCommands.GrenadesCommand;
import me.sniperzciinema.infected.Command.SubCommands.HelpCommand;
import me.sniperzciinema.infected.Command.SubCommands.InfoCommand;
import me.sniperzciinema.infected.Command.SubCommands.JoinCommand;
import me.sniperzciinema.infected.Command.SubCommands.LeaveCommand;
import me.sniperzciinema.infected.Command.SubCommands.ListCommand;
import me.sniperzciinema.infected.Command.SubCommands.RemoveCommand;
import me.sniperzciinema.infected.Command.SubCommands.SetArenaCommand;
import me.sniperzciinema.infected.Command.SubCommands.SetClassCommand;
import me.sniperzciinema.infected.Command.SubCommands.SetLeaveCommand;
import me.sniperzciinema.infected.Command.SubCommands.SetLobbyCommand;
import me.sniperzciinema.infected.Command.SubCommands.SetSpawnCommand;
import me.sniperzciinema.infected.Command.SubCommands.SetupCommand;
import me.sniperzciinema.infected.Command.SubCommands.ShopCommand;
import me.sniperzciinema.infected.Command.SubCommands.SpawnsCommand;
import me.sniperzciinema.infected.Command.SubCommands.StartCommand;
import me.sniperzciinema.infected.Command.SubCommands.SuicideCommand;
import me.sniperzciinema.infected.Command.SubCommands.TopCommand;
import me.sniperzciinema.infected.Command.SubCommands.TpLeaveCommand;
import me.sniperzciinema.infected.Command.SubCommands.TpLobbyCommand;
import me.sniperzciinema.infected.Command.SubCommands.TpSpawnCommand;
import me.sniperzciinema.infected.Command.SubCommands.VoteCommand;
import me.sniperzciinema.infected.Events.InfectedCommandEvent;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.FancyMessages.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CHandler implements CommandExecutor {

	private ArrayList<SubCommand>	commands;

	public CHandler()
	{
		this.commands = new ArrayList<SubCommand>();

		registerSubCommand(new AddonsCommand());
		registerSubCommand(new AdminCommand());
		registerSubCommand(new ArenasCommand());
		registerSubCommand(new ChatCommand());
		registerSubCommand(new ClassesCommand());
		registerSubCommand(new CreateCommand());
		registerSubCommand(new DelSpawnCommand());
		registerSubCommand(new EndCommand());
		registerSubCommand(new FilesCommand());
		registerSubCommand(new GrenadesCommand());
		registerSubCommand(new HelpCommand());
		registerSubCommand(new InfoCommand());
		registerSubCommand(new JoinCommand());
		registerSubCommand(new LeaveCommand());
		registerSubCommand(new ListCommand());
		registerSubCommand(new RemoveCommand());
		registerSubCommand(new SetArenaCommand());
		registerSubCommand(new SetClassCommand());
		registerSubCommand(new SetLeaveCommand());
		registerSubCommand(new SetLobbyCommand());
		registerSubCommand(new SetSpawnCommand());
		registerSubCommand(new SetupCommand());
		registerSubCommand(new ShopCommand());
		registerSubCommand(new SpawnsCommand());
		registerSubCommand(new StartCommand());
		registerSubCommand(new SuicideCommand());
		registerSubCommand(new TopCommand());
		registerSubCommand(new TpLeaveCommand());
		registerSubCommand(new TpLobbyCommand());
		registerSubCommand(new TpSpawnCommand());
		registerSubCommand(new VoteCommand());

	}

	public void registerSubCommand(SubCommand subCommand) {
		this.commands.add(subCommand);
	}

	public ArrayList<SubCommand> getSubCommands() {
		return this.commands;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("Infected"))
		{
			Player p = null;
			InfPlayer ip = null;
			if (sender instanceof Player)
			{
				p = (Player) sender;
				ip = InfPlayerManager.getInfPlayer(p);
			}
			InfectedCommandEvent ce = new InfectedCommandEvent(args, p, ip);
			Bukkit.getPluginManager().callEvent(ce);
			if (!ce.isCancelled())
			{
				if (args.length == 0)
				{
					sender.sendMessage("");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + ">>>>>>[" + ChatColor.GOLD + ChatColor.BOLD + "Infected" + ChatColor.DARK_AQUA + ChatColor.STRIKETHROUGH + "]<<<<<<");
					if (Infected.update)
						if (p == null)
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + ChatColor.BOLD + "Update Available: " + ChatColor.WHITE + ChatColor.BOLD + Infected.updateName);
						else
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§c§lUpdate Available: §f§l" + Infected.updateName).tooltip("§aClick to open page").link(Infected.updateLink).send(p);

					sender.sendMessage("");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Author: " + ChatColor.GREEN + ChatColor.BOLD + "SniperzCiinema" + ChatColor.WHITE + ChatColor.ITALIC + "(" + ChatColor.DARK_AQUA + "xXSniperzXx_SD" + ChatColor.WHITE + ")");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Version: " + ChatColor.GREEN + ChatColor.BOLD + Infected.version);
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.WHITE + "BukkitDev: " + ChatColor.GREEN + ChatColor.BOLD + "http://bit.ly/McInfected");
					if (p == null)
					{
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Help type: /Infected Help");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.YELLOW + "For Addons type: /Infected Addons");
					}
					else
					{
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§eFor Help type: Infected Help").tooltip("§aClick to autotype").suggest("/Infected Help 1").send(p);
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§eFor Help type: Infected Addons").tooltip("§aClick to autotype").suggest("/Infected Addons").send(p);
					}
					sender.sendMessage(Msgs.Format_Line.getString());
				}
				else
				{
					for (SubCommand subCommand : this.commands)
						if (subCommand.getName().equalsIgnoreCase(args[0]) || subCommand.getAliases().contains(args[0].toLowerCase()))
						{
							subCommand.execute(sender, args);
							return true;
						}
					sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());
				}
			}
		}
		return true;
	}
}
