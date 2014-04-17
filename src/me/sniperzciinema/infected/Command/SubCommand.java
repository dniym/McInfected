
package me.sniperzciinema.infected.Command;

import java.util.List;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;


public abstract class SubCommand {

	private String	name;

	public SubCommand(String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getPermission() {
		return "Infected." + name;
	}

	public final boolean hasPermission(CommandSender sender) {

		return sender.hasPermission(getPermission());
	}

	public abstract List<String> getAliases();

	public abstract void execute(CommandSender sender, String[] args) throws CommandException;

}
