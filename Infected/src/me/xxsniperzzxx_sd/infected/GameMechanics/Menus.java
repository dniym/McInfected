package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.ArrayList;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Tools.IconMenu;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.ItemHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Menus {


	@SuppressWarnings("deprecation")
	public static void openHumanMenu(final Player player) {
		final ArrayList<String> classList = new ArrayList<String>();
		for (String classes : Infected.filesGetClasses().getConfigurationSection("Classes.Human").getKeys(true))
		{
			if (!classes.contains("."))
			{
				classList.add(classes);
			}
		}
		IconMenu menu = new IconMenu(
				ChatColor.GREEN + player.getName() + " - Humans",
				((classList.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						if (event.getName().equalsIgnoreCase("None"))
						{
							Main.humanClasses.remove(player.getName());
							event.getPlayer().sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, "None"));
						} else if (player.hasPermission("Infected.Classes.Human") || player.hasPermission("Infected.Classes.Human." + event.getName()))
						{
							Main.humanClasses.put(player.getName(), classList.get(event.getPosition()));

							event.getPlayer().sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, classList.get(event.getPosition())));
						} else
						{
							player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						}
					}
				}, Main.me);
		int i = 0;
		for (String classes : classList)
		{
			if (!classes.contains("."))
			{
				ItemStack item = new ItemStack(
						Material.getMaterial(ItemHandler.getItemID(Infected.filesGetClasses().getStringList("Classes.Human." + classes + ".Items").get(0))));
				menu.setOption(i, item, classes, ChatColor.GREEN + "Click to choose this class");
				i++;
			}
		}
		menu.setOption(i, new ItemStack(Material.REDSTONE_WIRE), "None", "Choose to have no class");
		menu.open(player);
	}

	@SuppressWarnings("deprecation")
	public static void openZombieMenu(final Player player) {

		final ArrayList<String> classList = new ArrayList<String>();
		for (String classes : Infected.filesGetClasses().getConfigurationSection("Classes.Zombie").getKeys(true))
		{
			if (!classes.contains("."))
			{
				classList.add(classes);
			}
		}
		IconMenu menu = new IconMenu(
				ChatColor.DARK_RED + player.getName() + " - Zombies",
				((classList.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						if (event.getName().equalsIgnoreCase("None"))
						{
							Main.zombieClasses.remove(player.getName());
							event.getPlayer().sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, "None"));
						} else if (player.hasPermission("Infected.Classes.Zombie") || player.hasPermission("Infected.Classes.Zombie." + event.getName()))
						{
							Main.zombieClasses.put(player.getName(), classList.get(event.getPosition()));
							event.getPlayer().sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, classList.get(event.getPosition())));
						} else
						{
							player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, null, null));
						}
					}
				}, Main.me);
		int i = 0;
		for (String classes : classList)
		{
			if (!classes.contains("."))
			{
				ItemStack item = new ItemStack(
						Material.getMaterial(ItemHandler.getItemID(Infected.filesGetClasses().getStringList("Classes.Zombie." + classes + ".Items").get(0))));
				menu.setOption(i, item, classes, ChatColor.RED + "Click to choose this class");
				i++;
			}
		}
		menu.setOption(i, new ItemStack(Material.REDSTONE_WIRE), "None", "Choose to have no class");
		menu.open(player);
	}

	public static void openVotingMenu(final Player player) {

		Infected.filesReloadArenas();
		Main.possibleArenas.clear();
		for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
		{
			// Check if the string matchs an arena

			if (Main.possibleArenas.contains(parenas))
			{
				Main.possibleArenas.remove(parenas);
			}
			if (!parenas.contains("."))
			{
				Main.possibleArenas.add(parenas);
			}
			if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
			{
				Main.possibleArenas.remove(parenas);
			}
		}

		IconMenu menu = new IconMenu(
				ChatColor.DARK_BLUE + player.getName() + " - Map Vote",
				((Main.possibleArenas.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {

						if (event.getName().equalsIgnoreCase("Random"))
						{
							Random r = new Random();
							int i = r.nextInt(Main.possibleArenas.size());
							String voted4 = Main.possibleArenas.get(i);

							if (Main.Votes.containsKey(voted4))
							{
								Main.Votes.put(voted4, Main.Votes.get(voted4) + 1);
							} else
							{
								Main.Votes.put(voted4, 1);
							}
							Main.Voted4.put(player.getName(), voted4);
							for (Player players : Bukkit.getServer().getOnlinePlayers())
								if (Main.inGame.contains(players.getName()))
								{
									players.sendMessage(Messages.sendMessage(Msgs.VOTE_VOTEDFOR, player, voted4));
								}
							if (Main.config.getBoolean("ScoreBoard Support"))
							{
								ScoreBoard.updateScoreBoard();
							}
						} else
						{
							if (Main.Votes.containsKey(event.getName()))
							{
								Main.Votes.put(event.getName(), Main.Votes.get(event.getName()) + 1);
							} else
							{
								Main.Votes.put(event.getName(), 1);
							}
							Main.Voted4.put(player.getName(), event.getName());
							for (Player players : Bukkit.getServer().getOnlinePlayers())
								if (Main.inGame.contains(players.getName()))
								{
									players.sendMessage(Messages.sendMessage(Msgs.VOTE_VOTEDFOR, player, event.getName()));
								}
							if (Main.config.getBoolean("ScoreBoard Support"))
							{
								ScoreBoard.updateScoreBoard();
							}
						}
					}
				}, Main.me);
		int i = 0;
		for (String arenas : Main.possibleArenas)
		{
			menu.setOption(i, new ItemStack(Material.EMPTY_MAP), arenas, ChatColor.YELLOW + "Click to vote for this map");
			i++;
		}
		menu.setOption(i, new ItemStack(Material.MAP), "random", ChatColor.YELLOW + "Click to vote for Random");

		menu.open(player);
	}

	
}
