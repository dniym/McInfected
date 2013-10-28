
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.ArrayList;
import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Enums.Teams;
import me.xxsniperzzxx_sd.infected.Events.InfectedClassSelectEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedShopPurchaseEvent;
import me.xxsniperzzxx_sd.infected.Handlers.ItemHandler;
import me.xxsniperzzxx_sd.infected.Messages.Messages;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.IconMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


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
				ChatColor.GREEN + player.getName() + "-Human Classes",
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
							InfectedClassSelectEvent classEvent = new InfectedClassSelectEvent(
									player, Teams.Human,
									classList.get((event.getPosition())));
							Bukkit.getServer().getPluginManager().callEvent(classEvent);
							if (!classEvent.isCancelled())
							{
								Main.humanClasses.put(player.getName(), classList.get(event.getPosition()));

								event.getPlayer().sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, classList.get(event.getPosition())));
							}
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
				ChatColor.DARK_RED + player.getName() + "-Zombie Classes",
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
							InfectedClassSelectEvent classEvent = new InfectedClassSelectEvent(
									player, Teams.Zombie,
									classList.get((event.getPosition())));
							Bukkit.getServer().getPluginManager().callEvent(classEvent);
							if (!classEvent.isCancelled())
							{
								Main.zombieClasses.put(player.getName(), classList.get(event.getPosition()));
								event.getPlayer().sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, classList.get(event.getPosition())));
							}
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

	public static void chooseClass(final Player player) {
		IconMenu menu = new IconMenu(
				ChatColor.DARK_RED + player.getName() + "- Classes", 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(final IconMenu.OptionClickEvent event) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{

							public void run() {
								event.getPlayer().performCommand("Infected Classes " + ChatColor.stripColor(event.getName()));
							}
						}, 2);
					}
				}, Main.me);
		ItemStack zombie = new ItemStack(Material.SKULL_ITEM);
		zombie.setDurability((short) 2);
		ItemStack human = new ItemStack(Material.SKULL_ITEM);
		human.setDurability((short) 3);
		menu.setOption(3, zombie, ChatColor.RED + "Zombie", ChatColor.DARK_PURPLE + "Choose a " + ChatColor.RED + "Zombie" + ChatColor.DARK_PURPLE + " Class!");
		menu.setOption(5, human, ChatColor.GREEN + "Human", ChatColor.DARK_PURPLE + "Choose a " + ChatColor.GREEN + "Human" + ChatColor.DARK_PURPLE + " Class!");
		menu.open(player);
	}

	// /////////////////////////////// VOTING
	// //////////////////////////////////////
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

						Vote.voteFor(player, event.getName());
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

	// ///////////////////////////////// SHOP
	// ///////////////////////////////////////////

	public static void openShopMenu(final Player player) {

		Infected.filesReloadShop();
		
		try
		{
			ArrayList<String> shop = new ArrayList<String>();
			for (String string : Infected.filesGetShop().getConfigurationSection("Custom Items").getKeys(true))
			{
				if (!string.contains(".") && ItemHandler.getItemID(Infected.filesGetShop().getString("Custom Items." + string + ".Item Code")) != null)
				{
					shop.add(string);
				}
			}

			IconMenu menu = new IconMenu(
					ChatColor.GREEN + player.getName() + " - Shop",
					((shop.size() / 9) * 9) + 9,
					new IconMenu.OptionClickEventHandler()
					{

						@SuppressWarnings("deprecation")
						@Override
						public void onOptionClick(IconMenu.OptionClickEvent event) {
							int price = Infected.filesGetShop().getInt("Custom Items." + event.getName() + ".GUI Price");
							int points = Infected.playerGetPoints(player.getName());
							ItemStack is = ItemHandler.getItemStack(Infected.filesGetShop().getString("Custom Items." + event.getName() + ".Item Code"));
							String itemname = event.getName();
							ItemMeta im = is.getItemMeta();
							im.setDisplayName(itemname);
							is.setItemMeta(im);
							if (price <= points)
							{
								InfectedShopPurchaseEvent shopEvent = new InfectedShopPurchaseEvent(
										player, null, is, price);
								Bukkit.getServer().getPluginManager().callEvent(shopEvent);
								if (!shopEvent.isCancelled())
								{
									Infected.playerSetPoints(player.getName(), points, price);
									
										player.getInventory().addItem(is);
									

									player.sendMessage(Messages.sendMessage(Msgs.SHOP_PURCHASE, player, itemname));
									if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId()))))
									{
										if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
										{
											Infected.playerSaveShopInventory(player);
										}
									}
								} else
									player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
							} else
							{
								player.sendMessage(Messages.sendMessage(Msgs.SHOP_INVALIDPOINTS, player, null));
								player.sendMessage(Messages.sendMessage(Msgs.SHOP_NEEDMOREPOINTS, player, String.valueOf(price - points)));
							}
							Files.savePlayers();
							player.updateInventory();

						}
					}, Main.me);
			int i = 0;
			for (String item : shop)
			{
				menu.setOption(i, ItemHandler.getItemStack(Infected.filesGetShop().getString("Custom Items." + item + ".Item Code")), item, ChatColor.YELLOW + "Click to purchase for: " + Infected.filesGetShop().getInt("Custom Items." + item + ".GUI Price"));
				i++;
			}
			menu.open(player);
		} catch (NullPointerException npe)
		{
			npe.printStackTrace();
			player.sendMessage(Main.I + ChatColor.RED + "It appears you're still using the old Custom Items, Please delete your current Shops.yml!");
			System.out.println("");
			System.out.println("!!!!! It appears you're still using the old Custom Items, Please delete your current Shops.yml !!!!!");
			System.out.println("");
		}
	}
}
