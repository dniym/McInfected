
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.FancyMessages.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AddonsCommand extends SubCommand {

	public AddonsCommand()
	{
		super("addons");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {

		sender.sendMessage("");
		sender.sendMessage(Msgs.Format_Header.getString("<title>", " Addons "));
		if (!(sender instanceof Player))
		{
			sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.DisguisesEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			if (Settings.DisguisesEnabled())
				sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Plugin:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + Infected.Disguiser.getName());
			sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "CrackShot Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.CrackShotEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Factions Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.FactionsEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "mcMMO Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.mcMMOEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Vault Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Settings.VaultEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Infected-Ranks Support:" + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + (Bukkit.getPluginManager().getPlugin("InfectedAddon-Ranks") != null ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled")));
			sender.sendMessage(Msgs.Format_Line.getString());
		}
		else
		{
			Player p = (Player) sender;
			new FancyMessage(Msgs.Format_Prefix.getString()).then("§7Disguise Support: " + (Settings.DisguisesEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled"))).tooltip("§aIf enabled, zombies can be actual zombies!").send(p);

			if (Settings.DisguisesEnabled())
				sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "Disguise Plugin: " + "" + ChatColor.GREEN + ChatColor.ITALIC + " " + Infected.Disguiser.getName());
			new FancyMessage(Msgs.Format_Prefix.getString()).then("§7CrackShot Support: " + (Settings.CrackShotEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled"))).tooltip("§aIf enabled, you can use guns!").send(p);
			new FancyMessage(Msgs.Format_Prefix.getString()).then("§7Factions Support: " + (Settings.FactionsEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled"))).tooltip("§aIf enabled, pvp ignores factions relations!").send(p);
			new FancyMessage(Msgs.Format_Prefix.getString()).then("§7mcMMO Support: " + (Settings.mcMMOEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled"))).tooltip("§aIf enabled, pvp ignores mcMMO's levels!").send(p);
			new FancyMessage(Msgs.Format_Prefix.getString()).then("§7Vault Support: " + (Settings.VaultEnabled() ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled"))).tooltip("§aIf enabled, money can be given as a reward!").send(p);
			new FancyMessage(Msgs.Format_Prefix.getString()).then("§7Infected-Ranks Support: " + (Bukkit.getPluginManager().getPlugin("InfectedAddon-Ranks") != null ? ("" + ChatColor.GREEN + ChatColor.ITALIC + "Enabled") : ("" + ChatColor.RED + ChatColor.ITALIC + "Disabled"))).tooltip("§aIf enabled, Infected has ranks!").send(p);
		}

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "dependancies" });
	}

	@Override
	public List<String> getTabs() {
		return Arrays.asList(new String[] { "" });
	}
}
