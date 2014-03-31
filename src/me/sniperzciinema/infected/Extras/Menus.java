
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

	// Create all the menus
	public IconMenu teamMenu;
	public IconMenu classHumanMenu;
	public IconMenu classZombieMenu;
	public IconMenu voteMenu;
	public IconMenu shopMenu;
	public IconMenu grenadeMenu;

	// When the class gets loaded create all the IconMenus and store them for
	// later use
	public Menus()
	{
		teamMenu = getTeamMenu();
		classHumanMenu = getClassHumanMenu();
		classZombieMenu = getClassZombieMenu();
		voteMenu = getVoteMenu();
		shopMenu = getShopMenu();
		grenadeMenu = getGrenadeMenu();
	}

	public void destroyMenu(IconMenu menu) {
		menu.destroy();
	}

	public void destroyAllMenus() {
		teamMenu.destroy();
		classHumanMenu.destroy();
		classZombieMenu.destroy();
		voteMenu.destroy();
		shopMenu.destroy();
		grenadeMenu.destroy();
	}

	public IconMenu getTeamMenu() {
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Choose a Team",
				9, new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(final IconMenu.OptionClickEvent event) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
						{

							public void run() {
								Player player = event.getPlayer();
								if (Team.valueOf(ChatColor.stripColor(event.getName())) == Team.Human)
									classHumanMenu.open(player);
								else if (Team.valueOf(ChatColor.stripColor(event.getName())) == Team.Zombie)
									classZombieMenu.open(player);
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

		return menu;
	}

	public IconMenu getClassHumanMenu() {
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Human Classes",
				((InfClassManager.getClasses(Team.Human).size() / 9) * 9) + 18,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						final Player player = event.getPlayer();
						final InfPlayer infPlayer = InfPlayerManager.getInfPlayer(player);
						if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("None"))
						{
							player.sendMessage(Msgs.Classes_None.getString("<team>", "Human"));

							if (event.getClickType().isRightClick())
							{
								Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
								{

									public void run() {
										infPlayer.openMenu(player, classHumanMenu);
									}
								}, 2);
							}
						} else if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("Return"))
						{
							Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
							{

								public void run() {
									infPlayer.openMenu(player, teamMenu);

								}
							}, 2);
						} else if (player.hasPermission("Infected.Classes.Human") || player.hasPermission("Infected.Classes.Human." + event.getName()))
						{
							if (InfClassManager.isRegistered(Team.Human, InfClassManager.getClass(Team.Human, event.getName())))
							{
								player.sendMessage(Msgs.Classes_Chosen.getString("<class>", event.getName(), "<team>", "Human"));
								infPlayer.setInfClass(Team.Human, InfClassManager.getClass(Team.Human, ChatColor.stripColor(event.getName())));

								if (event.getClickType().isRightClick())
								{
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{

										public void run() {
											infPlayer.openMenu(player, classHumanMenu);
										}
									}, 2);
								}
							}

						} else
							player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					}
				}, Infected.me);
		int i = 0;
		for (InfClass Class : InfClassManager.getClasses(Team.Human))
		{
			ItemStack item = Class.getIcon();
			menu.setOption(i, item, Class.getName(),

			ChatColor.GREEN + Msgs.Menu_Classes_Click_To_Choose.getString(), "", ChatColor.GRAY + "Helmet: " + ChatColor.GREEN + StringUtil.getWord(Class.getHelmet().getType().name()), ChatColor.GRAY + "Chestplate: " + ChatColor.GREEN + StringUtil.getWord(Class.getChestplate().getType().name()), ChatColor.GRAY + "Leggings: " + ChatColor.GREEN + StringUtil.getWord(Class.getLeggings().getType().name()), ChatColor.GRAY + "Boots: " + ChatColor.GREEN + StringUtil.getWord(Class.getBoots().getType().name()), "", ChatColor.AQUA + "Potion Effect:", ChatColor.WHITE + (Class.getEffects().isEmpty() ? "" : StringUtil.getWord(Class.getEffects().get(0).getType().getName().toString())), "", ChatColor.AQUA + "Transfer Effect:", ChatColor.WHITE + (Class.getContactEffects().isEmpty() ? "" : StringUtil.getWord(Class.getContactEffects().get(0).getType().getName().toString())), "", Settings.DisguisesEnabled() && Class.getDisguise() != null ? ChatColor.YELLOW + "Disguise: " + ChatColor.WHITE + StringUtil.getWord(Class.getDisguise()) : "");
			i++;
		}
		menu.setOption(i, new ItemStack(Material.CAKE), "None", Msgs.Menu_Classes_Click_For_None.getString());
		menu.setOption(i + (9 - i) + 4, new ItemStack(Material.NETHER_STAR), "Return", Msgs.Menu_Classes_Click_To_Return.getString());

		return menu;
	}

	public IconMenu getClassZombieMenu() {
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Zombie Classes",
				((InfClassManager.getClasses(Team.Zombie).size() / 9) * 9) + 18,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						final Player player = event.getPlayer();
						final InfPlayer infPlayer = InfPlayerManager.getInfPlayer(player);
						if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("None"))
						{
							player.sendMessage(Msgs.Classes_None.getString("<team>", "Zombie"));

							if (event.getClickType().isRightClick())
							{
								Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
								{

									public void run() {
										infPlayer.openMenu(player, classZombieMenu);
									}
								}, 2);
							}
						} else if (ChatColor.stripColor(event.getName()).equalsIgnoreCase("Return"))
						{
							Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
							{

								public void run() {
									infPlayer.openMenu(player, teamMenu);

								}
							}, 2);
						} else if (player.hasPermission("Infected.Classes.Zombie") || player.hasPermission("Infected.Classes.Zombie." + event.getName()))
						{
							if (InfClassManager.isRegistered(Team.Zombie, InfClassManager.getClass(Team.Zombie, event.getName())))
							{
								player.sendMessage(Msgs.Classes_Chosen.getString("<class>", event.getName(), "<team>", "Zombie"));
								infPlayer.setInfClass(Team.Zombie, InfClassManager.getClass(Team.Zombie, ChatColor.stripColor(event.getName())));

								if (event.getClickType().isRightClick())
								{
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{

										public void run() {
											infPlayer.openMenu(player, classZombieMenu);
										}
									}, 2);
								}
							}

						} else
							player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					}
				}, Infected.me);
		int i = 0;
		for (InfClass Class : InfClassManager.getClasses(Team.Zombie))
		{
			ItemStack item = Class.getIcon();
			menu.setOption(i, item, Class.getName(),

			ChatColor.RED + Msgs.Menu_Classes_Click_To_Choose.getString(), "", ChatColor.GRAY + "Helmet: " + ChatColor.GREEN + StringUtil.getWord(Class.getHelmet().getType().name()), ChatColor.GRAY + "Chestplate: " + ChatColor.GREEN + StringUtil.getWord(Class.getChestplate().getType().name()), ChatColor.GRAY + "Leggings: " + ChatColor.GREEN + StringUtil.getWord(Class.getLeggings().getType().name()), ChatColor.GRAY + "Boots: " + ChatColor.GREEN + StringUtil.getWord(Class.getBoots().getType().name()), "", ChatColor.AQUA + "Potion Effect:", ChatColor.WHITE + (Class.getEffects().isEmpty() ? "" : StringUtil.getWord(Class.getEffects().get(0).getType().getName().toString())), "", ChatColor.AQUA + "Transfer Effect:", ChatColor.WHITE + (Class.getContactEffects().isEmpty() ? "" : StringUtil.getWord(Class.getContactEffects().get(0).getType().getName().toString())), "", Settings.DisguisesEnabled() && Class.getDisguise() != null ? ChatColor.YELLOW + "Disguise: " + ChatColor.WHITE + StringUtil.getWord(Class.getDisguise()) : "");
			i++;
		}
		menu.setOption(i, new ItemStack(Material.CAKE), "None", Msgs.Menu_Classes_Click_For_None.getString());
		menu.setOption(i + (9 - i) + 4, new ItemStack(Material.NETHER_STAR), "Return", Msgs.Menu_Classes_Click_To_Return.getString());

		return menu;
	}

	@SuppressWarnings("deprecation")
	public IconMenu getVoteMenu() {
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Vote for an Arena",
				((Lobby.getArenas().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						Arena arena;
						final Player player = event.getPlayer();
						final InfPlayer infPlayer = InfPlayerManager.getInfPlayer(event.getPlayer());
						int votes = infPlayer.getAllowedVotes();
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
							infPlayer.setVote(arena);

							for (String name : Lobby.getInGame())
							{
								Player u = Bukkit.getPlayer(name);
								u.sendMessage(Msgs.Command_Vote.getString("<player>", player.getName(), "<arena>", arena.getName()) + ChatColor.GRAY + (votes != 0 ? " (x" + votes + ")" : ""));
								InfPlayer up = InfPlayerManager.getInfPlayer(u);
								up.getScoreBoard().showProperBoard();
							}

						} else
						{
							event.getPlayer().sendMessage(Msgs.Error_Arena_Not_Valid.getString());
							event.setWillClose(false);
						}
					}
				}, Infected.me);
		int place = 0;
		for (Arena arena : Lobby.getArenas())
		{
			if (Lobby.isArenaValid(arena))
			{
				menu.setOption(place, arena.getBlock() != null && arena.getBlock().getTypeId() != 0 ? arena.getBlock() : new ItemStack(
						Material.EMPTY_MAP), "" + RandomChatColor.getColor() + ChatColor.BOLD + ChatColor.UNDERLINE + arena.getName(), "", Msgs.Menu_Vote_Choose.getString(), "", ChatColor.GREEN + "Creator: " + arena.getCreator(), "", ChatColor.AQUA + "--------------------------", ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
				place++;
			}
		}
		menu.setOption(place, new ItemStack(Material.MAP), "§aR§ba§cn§dd§eo§fm", "", Msgs.Menu_Vote_Random.getString());

		return menu;
	}

	@SuppressWarnings("deprecation")
	public IconMenu getShopMenu() {
		ArrayList<String> shop = new ArrayList<String>();
		for (String string : Files.getShop().getConfigurationSection("Custom Items").getKeys(true))
		{
			if (!string.contains(".") && ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + string + ".Item Code")).getType() != Material.AIR)
				shop.add(string);
		}

		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Item Shop",
				((shop.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {

						final Player player = event.getPlayer();
						final InfPlayer infPlayer = InfPlayerManager.getInfPlayer(player);
						int price = Files.getShop().getInt("Custom Items." + event.getName() + ".GUI Price");
						int points = infPlayer.getPoints(Settings.VaultEnabled());
						ItemStack is = ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + event.getName() + ".Item Code"));
						String itemname = event.getName();
						ItemMeta im = is.getItemMeta();
						im.setDisplayName(itemname);
						is.setItemMeta(im);
						if (price <= points)
						{
							if (player.hasPermission("Infected.Shop") || player.hasPermission("Infected.Shoplayer." + is.getTypeId()))
							{
								player.sendMessage(Msgs.Shop_Bought_Item.getString("<item>", is.getItemMeta().getDisplayName() == null ? StringUtil.getWord(is.getType().name()) : is.getItemMeta().getDisplayName()));
								infPlayer.setPoints(points - price, Settings.VaultEnabled());

								player.getInventory().addItem(is);

								if (Lobby.isInGame(player))
									if (!GrenadeManager.isGrenade(is) && Settings.saveItem(is))
										SaveItemHandler.saveItem(player, is);

								if (event.getClickType().isRightClick())
								{
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{

										public void run() {
											infPlayer.openMenu(player, shopMenu);
										}
									}, 2);
								}

							} else
								player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
						} else
						{
							player.sendMessage(Msgs.Shop_Cost_Not_Enough.getString());
							player.sendMessage(Msgs.Shop_Cost_Needed.getString("<needed>", String.valueOf(price - points)));
						}
						Files.savePlayers();
						player.updateInventory();

					}
				}, Infected.me);
		int i = 0;
		for (String item : shop)
		{
			menu.setOption(i, ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + item + ".Item Code")), item, Msgs.Menu_Shop_Click_To_Buy.getString("<price>", String.valueOf(Files.getShop().getInt("Custom Items." + item + ".GUI Price"))));
			i++;
		}
		return menu;
	}

	public IconMenu getGrenadeMenu() {
		ArrayList<String> shop = new ArrayList<String>();
		IconMenu menu = new IconMenu(
				RandomChatColor.getColor(ChatColor.GOLD, ChatColor.GREEN, ChatColor.BLUE, ChatColor.RED, ChatColor.DARK_AQUA, ChatColor.YELLOW) + "Grenade Shop",
				((shop.size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@SuppressWarnings("deprecation")
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {

						final Player player = event.getPlayer();
						final InfPlayer infPlayer = InfPlayerManager.getInfPlayer(player);
						Grenade grenade = GrenadeManager.getGrenade(ChatColor.stripColor(event.getName()));
						int price = grenade.getCost();
						int points = infPlayer.getPoints(Settings.VaultEnabled());
						ItemStack is = grenade.getItem();

						if (price <= points)
						{
							if (player.hasPermission("Infected.Grenades"))
							{
								player.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));
								infPlayer.setPoints(points - price, Settings.VaultEnabled());

								player.getInventory().addItem(is);

								if (event.getClickType().isRightClick())
								{
									Bukkit.getScheduler().scheduleSyncDelayedTask(Infected.me, new Runnable()
									{

										public void run() {
											infPlayer.openMenu(player, grenadeMenu);
										}
									}, 2);
								}

							} else
								player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
						} else
						{
							player.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
						}
						player.updateInventory();

					}
				}, Infected.me);
		int i = 0;
		for (Grenade grenade : GrenadeManager.getGrenades())
		{
			menu.setOption(i, grenade.getItem(), grenade.getName(), "§7§lCost: §f§o" + String.valueOf(grenade.getCost()), "", "§4§lDamage: §c§o" + grenade.getDamage(), "§9§lRange: §b§o" + grenade.getRange(), "§2§lDelay: §a§o" + grenade.getDelay());
			i++;
		}

		return menu;
	}

}
