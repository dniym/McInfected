
package me.sniperzciinema.infected.Listeners;

import java.util.List;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Items.SaveItemHandler;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * The Signs Listener
 */

public class SignListener implements Listener {

	// ////////////////////////////////////////////////-CHOOSE_A_CLASS-//////////////////////////////////////////////////////
	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerChoosesClassFromSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player p = event.getPlayer();

			// Make sure the player is in the game
			if (Lobby.isInGame(p) && event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Block b = event.getClickedBlock();
				if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
				{
					Sign sign = ((Sign) b.getState());

					// Lets make sure it's an Infected Class sign
					if (sign.getLine(0).contains("[Infected]") && (sign.getLine(1).contains("Class") || sign.getLine(1).contains("Classes")))
					{
						// Make sure classes are enabled

						InfPlayer IP = InfPlayerManager.getInfPlayer(p);
						String className = ChatColor.stripColor(sign.getLine(2));
						Team team = null;
						if (sign.getLine(3).contains("Zombie"))
							team = Team.Zombie;
						else
							team = Team.Human;

						// If className is "None" well set the back to the
						// default class
						if (className.equalsIgnoreCase("None"))
						{
							IP.setInfClass(Team.Human, InfClassManager.getDefaultClass(team));
							p.sendMessage(Msgs.Classes_Chosen.getString("<class>", "Default", "<team>", team.toString()));
						}
						// If its anything other then "None" Make sure they
						// have the permissions needed
						else
							if (p.hasPermission("Infected.Classes." + team.toString()) || p.hasPermission("Infected.Classes." + team.toString() + "." + className))
							{
								IP.setInfClass(team, InfClassManager.getClass(team, className));
								p.sendMessage(Msgs.Classes_Chosen.getString("<class>", className, "<team>", team.toString()));
							}
							else
							{
								p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

							}
					}
				}
			}

		}
	}

	// ///////////////////////////////////////////////-CLICK_COMMAND_SIGN-//////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerClicksCMDSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Block b = event.getClickedBlock();
				if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
				{
					Sign sign = ((Sign) b.getState());
					// If the sign seems like its a Command sign
					if (sign.getLine(0).contains("[Infected]") && sign.getLine(1).toLowerCase().contains("click to use"))
					{
						// Not much to this, just get whats on the signs and
						// make the player use the command
						String command = ChatColor.stripColor(sign.getLine(2));
						String commandarg = ChatColor.stripColor(sign.getLine(3));
						event.getPlayer().performCommand("Infected " + command + " " + commandarg);
					}
				}
			}
		}
	}

	// //////////////////////////////////////////////////////-CLICK_COMMAND_SET_SIGN-//////////////////////////////////////////////////
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickCmdSetSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player p = event.getPlayer();

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Block b = event.getClickedBlock();
				if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
				{

					Sign sign = ((Sign) b.getState());

					// If the sign seems like a Command Set's sign
					if (sign.getLine(0).contains("[Infected]") && sign.getLine(1).contains("CmdSet"))
					{
						// Set all the stuff needed
						String cmdset = ChatColor.stripColor(sign.getLine(2));
						boolean useVault = false;

						InfPlayer IP = InfPlayerManager.getInfPlayer(p);

						int iPrice = 0;
						String price = ChatColor.stripColor(sign.getLine(3).replaceAll("Cost: ", ""));

						// If the price starts with a $, then we'll set vault
						// support to true, and remove the $ for now
						if (price.startsWith(String.valueOf('$')))
						{
							useVault = true;
							price = price.replaceAll("\\$", "");
						}
						iPrice = Integer.valueOf(price);
						int points = IP.getPoints(useVault);

						// Make sure they have permissions for this command set
						if (p.hasPermission("Infected.CmdSets") || p.hasPermission("Infected.CmdSets." + cmdset))
						{
							if (iPrice <= points)
							{
								IP.setPoints(points - iPrice, useVault);

								// Lets just loop through all the commands in
								// the set.
								for (String row : Files.getSigns().getStringList("Command Sets." + cmdset))
									Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), row.replaceAll("<player>", event.getPlayer().getName()));

							}
							else
							{
								p.sendMessage(Msgs.Sign_CmdSet_Not_Enough.getString());
								p.sendMessage(Msgs.Sign_CmdSet_Cost_Needed.getString("<needed>", String.valueOf(iPrice - points)));
							}
						}
						else
							p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					}
				}
			}
		}
	}

	// ///////////////////////////////////////////////-CLCIK_SHOP_SIGN_/////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickShopSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player p = event.getPlayer();

			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Block b = event.getClickedBlock();
				if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
				{
					if (Settings.shopsEnabled())
					{
						Sign sign = ((Sign) b.getState());
						if (sign.getLine(0).contains("[Infected]") && !sign.getLine(1).contains("Playing") && !sign.getLine(1).contains("CmdSet") && !sign.getLine(1).toLowerCase().contains("click to use") && !sign.getLine(1).contains("Class") && !sign.getLine(1).contains("Classes"))
						{
							boolean useVault = false;

							InfPlayer IP = InfPlayerManager.getInfPlayer(p);

							int iPrice = 0;
							String price = ChatColor.stripColor(sign.getLine(3).replaceAll("Cost: ", ""));

							// If the price starts with a $, then we'll set
							// vault
							// support to true, and remove the $ for now
							if (price.startsWith(String.valueOf('$')))
							{
								useVault = true;
								price = price.replaceAll("\\$", "");
							}
							int points = IP.getPoints(useVault);
							iPrice = Integer.valueOf(price);

							String materialName = ChatColor.stripColor(sign.getLine(1));
							ItemStack stack = null;
							Material material = null;
							// Loop through all materials in minecraft, if the
							// item name matches the itemname variable, break
							// out of the loop
							for (Material materials : Material.values())
							{
								if (materials.toString().equalsIgnoreCase(materialName))
								{
									material = materials;
									break;
								}
							}
							// If the material was found
							if (material != null)
							{
								stack = new ItemStack(material);
							}
							// If the item is in the custom items
							else
								if (Files.getShop().contains("Custom Items." + materialName + ".Item Code"))
								{
									stack = ItemHandler.getItemStack(Files.getShop().getString("Custom Items." + materialName + ".Item Code"));
									ItemMeta meta = stack.getItemMeta();
									meta.setDisplayName(materialName);
									stack.setItemMeta(meta);
								}
								// It's gotta be the start of an item name
								// then...
								else
								{
									for (Material materials : Material.values())
									{
										if (materials.toString().startsWith(materialName))
										{
											stack = new ItemStack(materials);
											break;
										}
									}
								}
							if (p.hasPermission("Infected.Shop") || p.hasPermission("Infected.Shop." + stack.getTypeId()))
							{
								if (iPrice <= points)
								{
									IP.setPoints(IP.getPoints(useVault) - iPrice, useVault);
									p.getInventory().addItem(stack);
									p.sendMessage(Msgs.Shop_Bought_Item.getString("<item>", stack.getItemMeta().getDisplayName() == null ? StringUtil.getWord(stack.getType().name()) : stack.getItemMeta().getDisplayName()));
									if (Lobby.isInGame(p))
										if (!GrenadeManager.isGrenade(stack) && Settings.saveItem(stack))
											SaveItemHandler.saveItem(p, stack);
								}
								else
								{
									p.sendMessage(Msgs.Shop_Cost_Not_Enough.getString());
									p.sendMessage(Msgs.Shop_Cost_Needed.getString("<needed>", String.valueOf(iPrice - points)));
								}
								p.updateInventory();
							}
							else
								p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

						}
					}
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////-CREATE_SHOP_SIGN-///////////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateShop(SignChangeEvent event) {
		if (!event.isCancelled())
		{

			Player p = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && !event.getLine(1).equalsIgnoreCase("Info") && !event.getLine(1).equalsIgnoreCase("CmdSet") && !event.getLine(1).equalsIgnoreCase("cmd") && !event.getLine(1).equalsIgnoreCase("Class") && !event.getLine(1).contains("Classes"))
			{
				// Make sure everything checks out as good
				if (!p.hasPermission("Infected.Signs.Shop"))
				{
					p.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					event.setCancelled(true);
				}

				if (event.getLine(1).isEmpty() || event.getLine(2).isEmpty())
				{
					p.sendMessage(Msgs.Error_Sign_Not_Valid.getString());
					event.setCancelled(true);
				}
				else
				{
					try
					{
						// Check if it's a custom item
						if (Files.getShop().contains("Custom Items." + event.getLine(1) + ".Item Code"))
						{
							// Lets get the Item Code and the price
							String price = event.getLine(2);

							// Set the sign
							event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
							event.setLine(1, ChatColor.GRAY + event.getLine(1));
							event.setLine(2, ChatColor.GREEN + "Click To Buy");
							event.setLine(3, ChatColor.DARK_RED + price);

						}
						// If its not in the custom items, it's either an
						// item
						// id or invalid
						else
						{
							p.sendMessage(Msgs.Error_Sign_Not_Valid.getString());
							event.setCancelled(true);

						}
					}
					catch (Exception e)
					{
						p.sendMessage(Msgs.Error_Sign_Not_Valid.getString());
						event.setCancelled(true);
					}

				}
			}
		}

	}

	// /////////////////////////////////////////////////////////-CREATE_INFO_SIGN-/////////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateInfoSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{

			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("Info"))
			{
				// Make thing everythings in check
				if (!player.hasPermission("Infected.Signs.Info"))
				{
					player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					event.setCancelled(true);
				}
				if (event.getLine(1).isEmpty())
				{
					event.getPlayer().sendMessage(Msgs.Error_Sign_Not_Valid.getString());
					event.getBlock().breakNaturally();
					event.setCancelled(true);
				}
				else
				{
					// Is Info Signs enabled
					if (Settings.InfoSignsEnabled())
					{

						event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");

						String status = Lobby.getGameState().toString();

						int time = Lobby.getTimeLeft();

						event.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Lobby.getPlayersInGame().size()));
						event.setLine(2, ChatColor.GOLD + status);
						if (Lobby.getGameState() == GameState.Started || Lobby.getGameState() == GameState.Infecting || Lobby.getGameState() == GameState.Voting)
							event.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));
						else
							event.setLine(3, "");

						// Now lets save these signs
						if (Files.getSigns().getStringList("Info Signs") == null)
						{
							String[] list = { LocationHandler.getLocationToString(event.getBlock().getLocation()) };
							Files.getSigns().set("Info Signs", list);
							Files.saveSigns();
						}
						else
						{
							List<String> list = Files.getSigns().getStringList("Info Signs");
							list.add(LocationHandler.getLocationToString(event.getBlock().getLocation()));
							Files.getSigns().set("Info Signs", list);
							Files.saveSigns();
						}

					}
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////-CREATE_COMMAND_SIGN-///////////////////////////////////////////////////
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateCMDSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("Cmd"))
			{
				if (!player.hasPermission("Infected.Signs.Command"))
				{
					player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					event.setCancelled(true);
				}
				if (event.getLine(1).isEmpty())
				{
					event.getPlayer().sendMessage(Msgs.Error_Sign_Not_Valid.getString());
					event.getBlock().breakNaturally();
					event.setCancelled(true);
				}
				else
				{
					event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
					event.setLine(1, ChatColor.GREEN + "Click to use CMD");
					event.setLine(2, ChatColor.GOLD + event.getLine(2).toUpperCase());
					event.setLine(3, ChatColor.GOLD + event.getLine(3).toUpperCase());

				}
			}
		}
	}

	// /////////////////////////////////////////////////-CREATE_CMD_SET-////////////////////////////////////////
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateCmdset(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("CmdSet"))
			{
				if (!player.hasPermission("Infected.Signs.CmdSets"))
				{
					player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					event.setCancelled(true);
				}
				String commandSet = "None";

				if (Files.getSigns().getConfigurationSection("Command Sets") != null)
				{
					for (String sets : Files.getSigns().getConfigurationSection("Command Sets").getKeys(true))
					{
						if ((!sets.contains(".")) && event.getLine(2).equalsIgnoreCase(sets))
						{
							commandSet = sets;
							break;
						}
					}

					if (!commandSet.equalsIgnoreCase("None"))
					{
						event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
						event.setLine(1, ChatColor.GRAY + "CmdSet");
						event.setLine(2, ChatColor.GOLD + commandSet);
						event.setLine(3, ChatColor.YELLOW + "Cost: " + event.getLine(3));
					}
					else
					{
						player.sendMessage(Msgs.Error_Sign_Not_Valid.getString());
						event.setCancelled(true);
					}

				}
				else
				{
					player.sendMessage(Msgs.Error_Sign_Not_Valid.getString());
					event.setCancelled(true);
				}
			}
		}
	}

	// //////////////////////////////////////////////////////-CREATE_CLASS_SIGN-//////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateClassSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && (event.getLine(1).equalsIgnoreCase("Class") || event.getLine(1).equalsIgnoreCase("Classes")))
			{
				if (!player.hasPermission("Infected.Signs.Classes"))
				{
					player.sendMessage(Msgs.Error_Misc_No_Permission.getString());
					event.setCancelled(true);
				}
				String line = event.getLine(3) != null ? event.getLine(3) : "";
				Team team = line.equalsIgnoreCase("Human") ? Team.Human : line.equalsIgnoreCase("Zombie") ? Team.Zombie : null;
				String className = event.getLine(2);

				if (team == null || className.equalsIgnoreCase("None") || InfClassManager.isRegistered(team, InfClassManager.getClass(team, className)))
				{

					if (team == null)
					{
						if (InfClassManager.isClass(className))
						{
							team = InfClassManager.getClass(className).getTeam();
						}
					}
					event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
					event.setLine(1, ChatColor.GRAY + "Class");
					event.setLine(2, ChatColor.GREEN + className);
					event.setLine(3, (team == Team.Human ? ChatColor.GREEN : ChatColor.RED) + "-> " + team.toString() + " <-");
				}
				else
				{
					player.sendMessage(Msgs.Error_Sign_Not_Valid.getString());
				}
			}

		}
	}
}
