
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;


public class FilesCommand extends SubCommand {

	public FilesCommand()
	{
		super("file");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission("Infected.Files"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
		else
		{
			Configuration config = null;

			if (args.length > 1)
				if (args[1].equalsIgnoreCase("Config"))
					config = Files.getConfig();
				else
					if (args[1].equalsIgnoreCase("Arenas"))
						config = Files.getArenas();
					else
						if (args[1].equalsIgnoreCase("Classes"))
							config = Files.getClasses();
						else
							if (args[1].equalsIgnoreCase("Grenades"))
								config = Files.getGrenades();
							else
								if (args[1].equalsIgnoreCase("Messages"))
									config = Files.getMessages();
								else
									if (args[1].equalsIgnoreCase("Players"))
										config = Files.getPlayers();
									else
										if (args[1].equalsIgnoreCase("Shop"))
											config = Files.getShop();
										else
											if (args[1].equalsIgnoreCase("Signs"))
												config = Files.getSigns();
											else
												sender.sendMessage(Msgs.Error_Misc_Not_A_File.getString("<files>", "Config, Arenas, Classes, Grenades, Messages, Players, Shop, Signs"));
			if (args.length == 2)
			{

				if (config != null)
					for (String path : config.getConfigurationSection("").getKeys(true))
						if (!config.getString(path).startsWith("MemorySection"))
							sender.sendMessage(ChatColor.YELLOW + path.replaceAll(" ", "_") + ChatColor.WHITE + ": " + ChatColor.GRAY + config.getString(path).replaceAll(" ", "_"));
			}
			else
				if (args.length == 3)
				{
					String path = args[2].replaceAll("_", " ");

					if (config != null)
						sender.sendMessage(Msgs.Command_Files_Value.getString("<path>", path, "<value>", config.getString(path).replaceAll("_", " ")));
				}
				else
					if (args.length == 4)
					{
						String path = args[2].replaceAll("_", " ");
						String newvalue = args[3].replaceAll("_", " ");

						if (config != null)
						{
							if (config.get(path) != null)
							{
								sender.sendMessage(Msgs.Command_Files_Changed.getString("<path>", path, "<value>", config.getString(path), "<newvalue>", newvalue));
								if (newvalue.equalsIgnoreCase("True") || newvalue.equalsIgnoreCase("False"))
									config.set(path.replaceAll("_", " "), Boolean.valueOf(newvalue.toUpperCase()));
								else
									if (newvalue.startsWith(String.valueOf('[')) && newvalue.endsWith("]"))
									{
										String[] list = (newvalue.replaceAll("\\[", "").replaceAll("]", "")).split(",");
										config.set(path, list);
									}
									else
										try
										{
											int i = Integer.valueOf(newvalue);
											config.set(path, i);
										}
										catch (Exception ex)
										{
											config.set(path, newvalue);
										}
								Files.saveAll();
							}
							else
								sender.sendMessage(Msgs.Error_Misc_Not_A_Path.getString());

						}
						else
							sender.sendMessage(Msgs.Help_Files.getString("<files>", "Config, Arenas, Classes, Grenades, Messages, Players, Shop, Signs"));

					}
					else
						sender.sendMessage(Msgs.Help_Files.getString("<files>", "Config, Arenas, Classes, Grenades, Messages, Players, Shop, Signs"));
		}

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "files" });
	}

	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "Config", "Arenas", "Classes", "Grenades", "Messages", "Players", "Shop", "Signs" });
	}
}
