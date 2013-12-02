
package me.sniperzciinema.infected.Extras;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import me.sniperzciinema.infected.Main;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Classes.InfClass;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Misc.ItemHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.IconMenu;
import me.sniperzciinema.infected.Tools.Settings;

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
							p.sendMessage(Msgs.Menu_Classes_None.getString("<team>", team.toString()));
						} else if (p.hasPermission("Infected.Classes." + team.toString()) || p.hasPermission("Infected.Classes." + team.toString() + "." + event.getName()))
						{
							p.sendMessage(Msgs.Classes_Chosen.getString("<class>", event.getName(), "<team>", team.toString()));
							IP.setInfClass(team, InfClassManager.getClass(team, ChatColor.stripColor(event.getName())));

						} else
							p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					}
				}, Main.me);
		int i = 0;
		for (InfClass Class : (team == Team.Human ? InfClassManager.getHumanClasses() : InfClassManager.getZombieClasses()))
		{
			ItemStack item = Class.getItems().get(0);
			menu.setOption(i, item, Class.getName(), (team == Team.Human ? ChatColor.GREEN : ChatColor.RED) + Msgs.Menu_Classes_Click_To_Choose.getString(), "", ChatColor.GRAY + "Helmet: " + ChatColor.GREEN + StringUtil.getWord(Class.getHelmet().getType().name()), ChatColor.GRAY + "Chestplate: " + ChatColor.GREEN + StringUtil.getWord(Class.getChestplate().getType().name()), ChatColor.GRAY + "Leggings: " + ChatColor.GREEN + StringUtil.getWord(Class.getLeggings().getType().name()), ChatColor.GRAY + "Boots: " + ChatColor.GREEN + StringUtil.getWord(Class.getBoots().getType().name()), team == Team.Zombie && Settings.DisguisesEnabled() ? ChatColor.YELLOW + "Disguise: " + ChatColor.WHITE + StringUtil.getWord(Class.getDisguise()) : "");
			i++;
		}
		menu.setOption(i, new ItemStack(Material.REDSTONE_WIRE), "None", Msgs.Menu_Classes_None.getString());
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
		menu.setOption(3, zombie, ChatColor.RED + "Zombie", Msgs.Menu_Team_Choose.getString("<team>", ChatColor.RED + "Zombie"));
		menu.setOption(5, human, ChatColor.GREEN + "Human", Msgs.Menu_Team_Choose.getString("<team>", ChatColor.GREEN + "Human"));
		menu.open(p);
	}

	// ///////////////////////////////-VOTING-//////////////////////////////////////
	public static void openVotingMenu(final Player p) {
		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		IconMenu menu = new IconMenu(
				ChatColor.DARK_BLUE + p.getName() + " - Vote",
				((Lobby.getArenas().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						Arena arena;
						int votes = 1;

						for (Entry<String, Integer> node : Settings.getExtraVoteNodes().entrySet())
						{
							if (p.hasPermission("Infected.vote." + node.getKey()) && node.getValue() > votes)
								votes = node.getValue();
						}
						if(ChatColor.stripColor(event.getName()).equalsIgnoreCase("Random")){
							int i;
							Random r = new Random();
							i = r.nextInt(Lobby.getArenas().size());
							arena = Lobby.getArenas().get(i);
						}
						else{
							arena = Lobby.getArena(ChatColor.stripColor(event.getName()));
						}
						arena.setVotes(arena.getVotes() + votes);
						IP.setVote(arena);

						for (Player u : Lobby.getInGame())
						{
							u.sendMessage(Msgs.Command_Vote.getString("<player>", p.getName(), "<arena>", arena.getName()) + ChatColor.GRAY+(votes != 0 ? " (x" + votes + ")" : ""));
							InfPlayer up = InfPlayerManager.getInfPlayer(u);
							up.getScoreBoard().showProperBoard();
						}

					}
				}, Main.me);
		int place = 0;
		for (Arena arena : Lobby.getArenas())
		{

			if (Lobby.isArenaValid(arena.getName()))
				menu.setOption(place, arena.getBlock(), "" + ChatColor.getByChar(String.valueOf(place + 1)) + ChatColor.BOLD + ChatColor.UNDERLINE + arena.getName(), "", Msgs.Menu_Vote_Choose.getString(), "", ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
			else
				menu.setOption(place, new ItemStack(Material.REDSTONE_BLOCK), ChatColor.DARK_RED + arena.getName(), "", ChatColor.RED + "This arena isn't playable!", ChatColor.RED + "      It's Missing Spawns!", ChatColor.GRAY + "--------------------------", "", "" + ChatColor.GREEN + ChatColor.STRIKETHROUGH + Msgs.Menu_Vote_Choose.getString(), "", ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());

			place++;
		}
		menu.setOption(place, new ItemStack(Material.MAP), "random", Msgs.Menu_Vote_Random.getString());
		menu.open(p);
	}

	// /////////////////////////////////-SHOP-///////////////////////////////////////////

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
						int points = IP.getPoints(Settings.VaultEnabled());
						ItemStack is = ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + event.getName() + ".Item Code"));
						String itemname = event.getName();
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(itemname);
						is.setItemMeta(im);
						if (price <= points)
						{
							if (p.hasPermission("Infected.Shop") || p.hasPermission("Infected.Shop." + is.getTypeId()))
							{
								p.sendMessage(Msgs.Shop_Bought_Item.getString("<item>", is.getItemMeta().getDisplayName()));
								IP.setPoints(points - price, Settings.VaultEnabled());

								p.getInventory().addItem(is);

								if (Files.getShop().getBoolean("Save Items") && !GrenadeManager.isGrenade(is.getTypeId()))
								{
									if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
									{
										/**
										 * TODO: Allow saving Items
										 * saveShopInventory(p);
										 */

									}
								}
							} else
								p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
						} else
						{
							p.sendMessage(Msgs.Shop_Cost_Not_Enough.getString());
							p.sendMessage(Msgs.Shop_Cost_Needed.getString("<needed>", String.valueOf(price - points)));
						}
						Files.savePlayers();
						p.updateInventory();

					}
				}, Main.me);
		int i = 0;
		for (String item : shop)
		{
			menu.setOption(i, ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + item + ".Item Code")), item, Msgs.Menu_Shop_Click_To_Buy.getString("<price>", String.valueOf(Files.getShop().getInt("Custom Items." + item + ".GUI Price"))));
			i++;
		}
		menu.open(p);
	}
}