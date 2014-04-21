
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Disguise.Disguises;
import me.sniperzciinema.infected.Extras.Menus;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Handlers.Potions.PotionHandler;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Material;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class SetClassCommand extends SubCommand {

	public SetClassCommand()
	{
		super("setclass");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			if (!p.hasPermission("Infected.SetClass"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if ((args.length == 3) && (args[2].equalsIgnoreCase("Zombie") || args[2].equalsIgnoreCase("Human")))
				{

					String className = args[1];
					Team team = args[2].equalsIgnoreCase("Human") ? Team.Human : Team.Zombie;

					String helmet = p.getInventory().getHelmet() != null ? ItemHandler.getItemStackToString(p.getInventory().getHelmet()) : "id:0";
					String chestplate = p.getInventory().getChestplate() != null ? ItemHandler.getItemStackToString(p.getInventory().getChestplate()) : "id:0";
					String leggings = p.getInventory().getLeggings() != null ? ItemHandler.getItemStackToString(p.getInventory().getLeggings()) : "id:0";
					String boots = p.getInventory().getBoots() != null ? ItemHandler.getItemStackToString(p.getInventory().getBoots()) : "id:0";

					ArrayList<String> items = new ArrayList<String>();
					if (p.getInventory().getContents().length != 0)
						for (ItemStack im : p.getInventory().getContents())
							if (im != null)
								items.add(ItemHandler.getItemStackToString(im));

					String icon = p.getItemInHand().getType() != Material.AIR ? ItemHandler.getItemStackToString(p.getItemInHand()) : "id:276";

					ArrayList<String> potions = new ArrayList<String>();
					if (!p.getActivePotionEffects().isEmpty())
						for (PotionEffect pe : p.getActivePotionEffects())
							potions.add(PotionHandler.getPotionToString(pe));

					Files.getClasses().set("Classes." + team.toString() + "." + className + ".Icon", icon);

					if (Settings.DisguisesEnabled())
						if (Disguises.isPlayerDisguised(p))
							Files.getClasses().set("Classes." + team.toString() + "." + className + ".Disguise", Disguises.getDisguise(p));

					Files.getClasses().set("Classes." + team.toString() + "." + className + ".Helmet", helmet);
					Files.getClasses().set("Classes." + team.toString() + "." + className + ".Chestplate", chestplate);
					Files.getClasses().set("Classes." + team.toString() + "." + className + ".Leggings", leggings);
					Files.getClasses().set("Classes." + team.toString() + "." + className + ".Boots", boots);
					if (!items.isEmpty())
						Files.getClasses().set("Classes." + team.toString() + "." + className + ".Items", items);
					if (!potions.isEmpty())
						Files.getClasses().set("Classes." + team.toString() + "." + className + ".Potion Effects", potions);
					Files.saveClasses();
					InfClassManager.loadConfigClasses();
					Infected.Menus = new Menus();

					sender.sendMessage(Msgs.Command_Classes_SetClass.getString("<class>", className, "<team>", team.toString()));

				}
				else
					sender.sendMessage(Msgs.Help_SetClass.getString());
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "createclass", "makeclass" });
	}
}
