
package me.xxsniperzzxx_sd.infected.Extras;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Events.InfectedClassSelectEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedShopPurchaseEvent;
import me.xxsniperzzxx_sd.infected.Handlers.ItemHandler;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Arena.Arena;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClass;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClassManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;
import me.xxsniperzzxx_sd.infected.Messages.Msgs;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.IconMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Menus {

	public static void chooseClass(final Team team, final Player p) {
		final InfPlayer IP = InfPlayerManager.getPlayer(p);
		IconMenu menu = new IconMenu(
				ChatColor.GREEN + p.getName() + "-"+team.toString()+" Classes", ((InfClassManager.getHumanClasses().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{

					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						if (event.getName().equalsIgnoreCase("None"))
						{
							
						} else if (p.hasPermission("Infected.Classes."+team.toString()) || p.hasPermission("Infected.Classes."+team.toString()+"." + event.getName()))
						{
							InfectedClassSelectEvent classEvent = new InfectedClassSelectEvent(p, team, InfClassManager.getClass(Team.Human, ChatColor.stripColor(event.getName())));
							Bukkit.getServer().getPluginManager().callEvent(classEvent);
							if (!classEvent.isCancelled())
							{
								p.sendMessage("You have chosen <Class>");
								IP.setInfClass(team, InfClassManager.getClass(team, ChatColor.stripColor(event.getName())));
							}
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
				menu.setOption(i, item, Class.getName(), (team == Team.Human ? ChatColor.GREEN : ChatColor.RED )+ "Click to choose this class");
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
		final Lobby Lobby = Main.Lobby;
		final InfPlayer IP = InfPlayerManager.getPlayer(p);
		IconMenu menu = new IconMenu(ChatColor.DARK_BLUE + p.getName() + " - Map Vote", ((Lobby.getArenas().size() / 9) * 9) + 9,
				new IconMenu.OptionClickEventHandler()
				{
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						Arena arena = Lobby.getArena(ChatColor.stripColor(event.getName()));
						arena.setVotes(arena.getVotes() + 1);
						IP.setVote(arena);
						for(InfPlayer u : Lobby.getInGame()){
							u.getPlayer().sendMessage("<Player> voted for <Arena>");
						}
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
				menu.setOption(place, new ItemStack(m), "" + ChatColor.getByChar(String.valueOf(place + 1)) + ChatColor.BOLD + ChatColor.UNDERLINE + arena.getName(), 
						"", 
						ChatColor.GREEN + "Click Here Vote For This Arena", 
						"", 
						ChatColor.GRAY+"--------------------------",
						ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
			else
				menu.setOption(place, new ItemStack(Material.REDSTONE_BLOCK), ChatColor.DARK_RED + arena.getName(), 
						"",
						ChatColor.RED + "This arena isn't playable!",
						ChatColor.RED + "      It's Missing Spawns!",
						ChatColor.GRAY+"--------------------------", 
						"", 
						"" + ChatColor.GREEN +ChatColor.STRIKETHROUGH + "Click Here Vote For This Arena", 
						"", 
						ChatColor.GRAY+"--------------------------",
						ChatColor.AQUA + "Creator: " + ChatColor.WHITE + arena.getCreator());
			
			place++;
		}
		menu.setOption(place, new ItemStack(Material.MAP), "random", ChatColor.YELLOW + "Click to vote for Random");
		menu.open(p);
	}

	// ///////////////////////////////// SHOP
	// ///////////////////////////////////////////

	public static void openShopMenu(final Player player) {

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
									

									player.sendMessage("<Item> Bought");
									if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId()))))
									{
										if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
										{
											Infected.playerSaveShopInventory(player);
										}
									}
								} else
									player.sendMessage(Msgs.Error_No_Permission.getString());
							} else
							{
								player.sendMessage("Not enough points");
								player.sendMessage("You need <Points> more");
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
	}
}
