
package me.sniperzciinema.infectedv2.Extras;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import me.sniperzciinema.infectedv2.Main;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Arena.Arena;
import me.sniperzciinema.infectedv2.Handlers.Classes.InfClass;
import me.sniperzciinema.infectedv2.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infectedv2.Handlers.Misc.ItemHandler;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Handlers.Player.Team;
import me.sniperzciinema.infectedv2.Messages.Msgs;
import me.sniperzciinema.infectedv2.Tools.Files;
import me.sniperzciinema.infectedv2.Tools.IconMenu;
import me.sniperzciinema.infectedv2.Tools.Settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Menus {

	public static void chooseClass(final Team team, final Player p) {
		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		IconMenu menu = new IconMenu(
				ChatColor.GREEN + p.getName() + "-" + team.toString() + " Classes",
				((InfClassManager.getHumanClasses().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						if (event.getName().equalsIgnoreCase("None"))
						{

						} else if (p.hasPermission("Infected.Classes." + team.toString()) || p.hasPermission("Infected.Classes." + team.toString() + "." + event.getName()))
						{

							p.sendMessage("You have chosen <Class>");
							IP.setInfClass(team, InfClassManager.getClass(team, ChatColor.stripColor(event.getName())));

						} else
						{
							p.sendMessage(Msgs.Error_No_Permission.getString());
						}
					}
				}, Main.me);
		int i = 0;
		for (InfClass Class : (team == Team.Human ? InfClassManager.getHumanClasses() : InfClassManager.getZombieClasses()))
		{
			ItemStack item = Class.getItems().get(0);
			menu.setOption(i, item, Class.getName(), (team == Team.Human ? ChatColor.GREEN : ChatColor.RED) + "Click to choose this class");
			i++;
		}
		menu.setOption(i, new ItemStack(Material.REDSTONE_WIRE), "None", "Choose to have no class");
		menu.open(p);
	}

	public static void chooseClassTeam(final Player p) {
		IconMenu menu = new IconMenu(
				ChatColor.DARK_RED + p.getName() + "- Classes", 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(final IconMenu.OptionClickEvent event) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{

							public void run() {
								chooseClass(Team.valueOf(ChatColor.stripColor(event.getName())), p);
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
		menu.open(p);
	}

	// /////////////////////////////// VOTING
	// //////////////////////////////////////
	public static void openVotingMenu(final Player p) {
		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		IconMenu menu = new IconMenu(
				ChatColor.DARK_BLUE + p.getName() + " - Vote",
				((Lobby.getArenas().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						Arena arena = Lobby.getArena(ChatColor.stripColor(event.getName()));
						int votes = 1;

						for (Entry<String, Integer> node : Settings.getExtraVoteNodes().entrySet())
							if (p.hasPermission("Infected.vote." + node.getKey()) && node.getValue() > votes)
								votes = node.getValue();

						arena.setVotes(arena.getVotes() + votes);
						IP.setVote(arena);

						for (Player u : Lobby.getInGame())
							u.sendMessage("<Player> voted for <Arena>" + (votes != 0 ? " (x" + votes + ")" : ""));

						ScoreBoard.updateScoreBoard();

					}
				}, Main.me);
		int place = 0;
		List<String> list = Files.getConfig().getStringList("Blocks to use for GUI voting");
		for (Arena arena : Lobby.getArenas())
		{
			Random r = new Random();
			int i = r.nextInt(list.size() - 1) + 1;
			Material m = Material.valueOf(list.get(i));

			// Set the message depending on if the arena is valid(Has spawns)
			if (Lobby.isArenaValid(arena.getName()))
				menu.setOption(place, new ItemStack(m), "" + ChatColor.getByChar(String.valueOf(place + 1)) + ChatColor.BOLD + ChatColor.UNDERLINE + arena.getName(), "", ChatColor.GREEN + "Click Here Vote For This Arena", "", ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
			else
				menu.setOption(place, new ItemStack(Material.REDSTONE_BLOCK), ChatColor.DARK_RED + arena.getName(), "", ChatColor.RED + "This arena isn't playable!", ChatColor.RED + "      It's Missing Spawns!", ChatColor.GRAY + "--------------------------", "", "" + ChatColor.GREEN + ChatColor.STRIKETHROUGH + "Click Here Vote For This Arena", "", ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());

			place++;
		}
		menu.setOption(place, new ItemStack(Material.MAP), "random", ChatColor.YELLOW + "Click to vote for Random");
		menu.open(p);
	}

	// ///////////////////////////////// SHOP
	// ///////////////////////////////////////////

	public static void openShopMenu(final Player p) {

		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		ArrayList<String> shop = new ArrayList<String>();
		for (String string : Files.getShop().getConfigurationSection("Custom Items").getKeys(true))
		{
			if (!string.contains(".") && ItemHandler.getItemID(Files.getShop().getString("Custom Items." + string + ".Item Code")) != null)
			{
				shop.add(string);
			}
		}

		IconMenu menu = new IconMenu(ChatColor.GREEN + p.getName() + " - Shop",
				((shop.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@SuppressWarnings("deprecation")
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						int price = Files.getShop().getInt("Custom Items." + event.getName() + ".GUI Price");
						int points = IP.getPoints();
						ItemStack is = ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + event.getName() + ".Item Code"));
						String itemname = event.getName();
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(itemname);
						is.setItemMeta(im);
						if (price <= points)
						{

							IP.setPoints(points - price, Main.config.getBoolean("Vault Support.Enabled"));

							p.getInventory().addItem(is);

							p.sendMessage("<Item> Bought");
							if (Files.getShop().getBoolean("Save Items") && (!Files.getGrenades().contains(String.valueOf(is.getTypeId()))))
							{
								if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
								{
									/**
									 * TODO: Allow saving Items
									 * saveShopInventory(p);
									 */

								}
							} else
								p.sendMessage(Msgs.Error_No_Permission.getString());
						} else
						{
							p.sendMessage("Not enough points");
							p.sendMessage("You need <Points> more");
						}
						Files.savePlayers();
						p.updateInventory();

					}
				}, Main.me);
		int i = 0;
		for (String item : shop)
		{
			menu.setOption(i, ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + item + ".Item Code")), item, ChatColor.YELLOW + "Click to purchase for: " + Files.getShop().getInt("Custom Items." + item + ".GUI Price"));
			i++;
		}
		menu.open(p);
	}
}
