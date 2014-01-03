
package me.sniperzciinema.infected.Extras;

import java.util.ArrayList;
import java.util.Random;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Classes.InfClass;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.Grenade;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Items.SaveItemHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.IconMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Menus {

	/**
	 * Opens a Inventory GUI that allows the player to choose a team to view
	 * their classes
	 * 
	 * @param team
	 *            - The teams classes will be shown
	 * @param p
	 *            - the player who will see the menu
	 */
	public static void chooseClass(final Team team, final Player p) {
		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + p.getName().substring(0, Math.min(13, p.getName().length())) + " - Choose a Class",
				((InfClassManager.getClasses(team).size() / 9) * 9) + 18,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("None"))
						{
							p.sendMessage(Msgs.Classes_None.getString("<team>", team.toString()));

							if(event.getClick().isRightClick()){
								Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
								{
									public void run() {
										chooseClass(team, p);
									}
								}, 2);
							}
						} else if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("Return"))
						{
							Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
							{

								public void run() {
									chooseClassTeam(p);

								}
							}, 2);
						} else if (p.hasPermission("Infected.Classes." + team.toString()) || p.hasPermission("Infected.Classes." + team.toString() + "." + event.getName()))
						{
							if (InfClassManager.isRegistered(team, InfClassManager.getClass(team, event.getName())))
							{
								p.sendMessage(Msgs.Classes_Chosen.getString("<class>", event.getName(), "<team>", team.toString()));
								IP.setInfClass(team, InfClassManager.getClass(team, ChatColor.stripColor(event.getName())));

								if(event.getClick().isRightClick()){
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{
										public void run() {
											chooseClass(team, p);
										}
									}, 2);
								}
							}

						} else
							p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					}
				}, Infected.me);
		int i = 0;
		for (InfClass Class : InfClassManager.getClasses(team))
		{
			ItemStack item = Class.getIcon();
			menu.setOption(i, item, Class.getName(),

			(team == Team.Human ? ChatColor.GREEN : ChatColor.RED) + Msgs.Menu_Classes_Click_To_Choose.getString(), "", ChatColor.GRAY + "Helmet: " + ChatColor.GREEN + StringUtil.getWord(Class.getHelmet().getType().name()), ChatColor.GRAY + "Chestplate: " + ChatColor.GREEN + StringUtil.getWord(Class.getChestplate().getType().name()), ChatColor.GRAY + "Leggings: " + ChatColor.GREEN + StringUtil.getWord(Class.getLeggings().getType().name()), ChatColor.GRAY + "Boots: " + ChatColor.GREEN + StringUtil.getWord(Class.getBoots().getType().name()), "", ChatColor.AQUA + "Potion Effect:", ChatColor.WHITE + (Class.getEffects().isEmpty() ? "" : StringUtil.getWord(Class.getEffects().get(0).getType().getName().toString())), "", ChatColor.AQUA + "Transfer Effect:", ChatColor.WHITE + (Class.getContactEffects().isEmpty() ? "" : StringUtil.getWord(Class.getContactEffects().get(0).getType().getName().toString())), "", team == Team.Zombie && Settings.DisguisesEnabled() ? ChatColor.YELLOW + "Disguise: " + ChatColor.WHITE + StringUtil.getWord(Class.getDisguise()) : "");
			i++;
		}
		menu.setOption(i, new ItemStack(Material.CAKE), "None", Msgs.Menu_Classes_Click_For_None.getString());
		menu.setOption(i + (9 - i) + 4, new ItemStack(Material.NETHER_STAR), "Return", Msgs.Menu_Classes_Click_To_Return.getString());
		menu.open(p);
	}

	/**
	 * Opens a Inventory GUI that lets the player pick their class
	 * 
	 * @param p
	 *            - the player who will see the menu
	 */
	public static void chooseClassTeam(final Player p) {
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + p.getName().substring(0, Math.min(14, p.getName().length())) + " - Choose a Team",
				9, new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(final IconMenu.OptionClickEvent event) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
						{

							public void run() {
								chooseClass(Team.valueOf(ChatColor.stripColor(event.getName())), p);

							}
						}, 2);
					}
				}, Infected.me);

		ItemStack zombie = new ItemStack(Material.SKULL_ITEM);
		zombie.setDurability((short) 2);
		ItemStack human = new ItemStack(Material.SKULL_ITEM);
		human.setDurability((short) 3);
		menu.setOption(3, zombie, ChatColor.RED + "Zombie", Msgs.Menu_Team_Choose.getString("<team>", ChatColor.RED + "Zombie"));
		menu.setOption(5, human, ChatColor.GREEN + "Human", Msgs.Menu_Team_Choose.getString("<team>", ChatColor.GREEN + "Human"));
		menu.open(p);
	}

	/**
	 * Opens a Inventory GUI that lets the player vote for an arena of their
	 * choice
	 * 
	 * @param p
	 *            - the player who will see the menu
	 */
	// ///////////////////////////////-VOTING-//////////////////////////////////////
	public static void openVotingMenu(final Player p) {
		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + p.getName().substring(0, Math.min(10, p.getName().length())) + " - Vote for an Arena",
				((Lobby.getArenas().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						Arena arena;
						int votes = IP.getAllowedVotes();
						if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("Random"))
						{
							int i;
							Random r = new Random();
							i = r.nextInt(Lobby.getArenas().size());
							arena = Lobby.getArenas().get(i);
							while (!Lobby.isArenaValid(arena))
							{
								i = r.nextInt(Lobby.getArenas().size());
								arena = Lobby.getArenas().get(i);
							}
						} else
						{
							arena = Lobby.getArena(ChatColor.stripColor(event.getName()));
						}
						if (Lobby.isArenaValid(arena))
						{
							arena.setVotes(arena.getVotes() + votes);
							IP.setVote(arena);

							for (Player u : Lobby.getInGame())
							{
								u.sendMessage(Msgs.Command_Vote.getString("<player>", p.getName(), "<arena>", arena.getName()) + ChatColor.GRAY + (votes != 0 ? " (x" + votes + ")" : ""));
								InfPlayer up = InfPlayerManager.getInfPlayer(u);
								up.getScoreBoard().showProperBoard();
							}

						} else
						{
							event.setWillClose(false);
						}
					}
				}, Infected.me);
		int place = 0;
		for (Arena arena : Lobby.getArenas())
		{

			if (Lobby.isArenaValid(arena.getName()))
				menu.setOption(place, arena.getBlock() != null ? arena.getBlock() : new ItemStack(
						Material.EMPTY_MAP), "" + RandomChatColor.getColor() + ChatColor.BOLD + ChatColor.UNDERLINE + arena.getName(), "", Msgs.Menu_Vote_Choose.getString(), "", ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
			else
				menu.setOption(place, new ItemStack(Material.REDSTONE_BLOCK), ChatColor.DARK_RED + arena.getName(), "", ChatColor.RED + "This arena isn't playable!", ChatColor.RED + "      It's Missing Spawns!", ChatColor.GRAY + "--------------------------", "", "" + ChatColor.GREEN + ChatColor.STRIKETHROUGH + Msgs.Menu_Vote_Choose.getString(), "", ChatColor.GRAY + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());

			place++;
		}
		menu.setOption(place, new ItemStack(Material.MAP), "§aR§ba§cn§dd§eo§fm", Msgs.Menu_Vote_Random.getString());
		menu.open(p);
	}

	/**
	 * Opens a Inventory GUI that allows the player to purchase unique items
	 * from the shops.yml
	 * 
	 * @param p
	 *            - the player who will see the menu
	 */
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

		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + p.getName().substring(0, Math.min(18, p.getName().length())) + " - Item Shop",
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
								p.sendMessage(Msgs.Shop_Bought_Item.getString("<item>", is.getItemMeta().getDisplayName() == null ? StringUtil.getWord(is.getType().name()) : is.getItemMeta().getDisplayName()));
								IP.setPoints(points - price, Settings.VaultEnabled());

								p.getInventory().addItem(is);

								if (Lobby.isInGame(p))
									if (!GrenadeManager.isGrenade(is.getTypeId()) && Settings.saveItem(is.getTypeId()))
										SaveItemHandler.saveItems(p, is);

								if(event.getClick().isRightClick()){
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{
										public void run() {
											openShopMenu(p);
										}
									}, 2);
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
				}, Infected.me);
		int i = 0;
		for (String item : shop)
		{
			menu.setOption(i, ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + item + ".Item Code")), item, Msgs.Menu_Shop_Click_To_Buy.getString("<price>", String.valueOf(Files.getShop().getInt("Custom Items." + item + ".GUI Price"))));
			i++;
		}
		menu.open(p);
	}

	/**
	 * Opens a Inventory GUI that allows the player to purchase unique grenades
	 * from the Grenades.yml
	 * 
	 * @param p
	 *            - the player who will see the menu
	 */
	// /////////////////////////////////-SHOP-///////////////////////////////////////////

	public static void openGrenadesMenu(final Player p) {

		final InfPlayer IP = InfPlayerManager.getInfPlayer(p);
		ArrayList<String> shop = new ArrayList<String>();
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + p.getName().substring(0, Math.min(15, p.getName().length())) + " - Grenade Shop",
				((shop.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@SuppressWarnings("deprecation")
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						Grenade grenade = GrenadeManager.getGrenade(ChatColor.stripColor(event.getName()));
						int price = grenade.getCost();
						int points = IP.getPoints(Settings.VaultEnabled());
						ItemStack is = grenade.getItemStack();

						if (price <= points)
						{
							if (p.hasPermission("Infected.Grenades"))
							{
								p.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));
								IP.setPoints(points - price, Settings.VaultEnabled());

								p.getInventory().addItem(is);

								if(event.getClick().isRightClick()){
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{
										public void run() {
											openGrenadesMenu(p);
										}
									}, 2);
								}

							} else
								p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
						} else
						{
							p.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
						}
						p.updateInventory();

					}
				}, Infected.me);
		int i = 0;
		for (Grenade grenade : GrenadeManager.getGrenades())
		{
			menu.setOption(i, grenade.getItemStack(), grenade.getName(), "§7§lCost: §f§o" + String.valueOf(grenade.getCost()), "", "§4§lDamage: §c§o" + grenade.getDamage(), "§9§lRange: §b§o" + grenade.getRange(), "§2§lDelay: §a§o" + grenade.getDelay());
			i++;
		}
		menu.open(p);
	}
}
