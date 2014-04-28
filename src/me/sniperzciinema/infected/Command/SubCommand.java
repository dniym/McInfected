
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

	public abstract List<String> getTabs();

	public abstract void execute(CommandSender sender, String[] args) throws CommandException;

	public abstract List<String> getAliases();

	public String getName() {
		return this.name;
	}

	public String getPermission() {
		return "Infected." + this.name;
	}

	public final boolean hasPermission(CommandSender sender) {

		return sender.hasPermission(getPermission());
	}

}
