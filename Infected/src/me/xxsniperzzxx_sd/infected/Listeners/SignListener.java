
package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;
import java.util.List;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Enums.Teams;
import me.xxsniperzzxx_sd.infected.Events.InfectedClassSelectEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedShopPurchaseEvent;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.ItemHandler;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


@SuppressWarnings("static-access")
public class SignListener implements Listener {

	public Main Main = new Main();
	public ArrayList<String> item = new ArrayList<String>();

	public Main plugin;

	public SignListener(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickClassSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (player.getItemInHand().getType() == Material.BOW)
			{
				event.setUseItemInHand(Result.DEFAULT);
				event.setCancelled(false);
			} else
			{

				if (Infected.isPlayerInLobby(player) || Infected.isPlayerInGame(player))
				{
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						Block b = event.getClickedBlock();
						if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
						{
							Sign sign = ((Sign) b.getState());
							if (sign.getLine(0).contains("[Infected]") && sign.getLine(1).contains("Class"))
							{

								if (!plugin.getConfig().getBoolean("Class Support"))
								{
									player.sendMessage(Messages.sendMessage(Msgs.CLASSES_DISABLED, player, null));
								} else
								{
									String className = ChatColor.stripColor(sign.getLine(2));
									if (sign.getLine(3).contains("Human"))
									{
										if (className.equalsIgnoreCase("None"))
										{
											Main.humanClasses.remove(player.getName());
											player.sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, "None"));
										} else if (player.hasPermission("Infected.Classes.Human") || player.hasPermission("Infected.Classes.Human." + className))
										{
											InfectedClassSelectEvent classEvent = new InfectedClassSelectEvent(
													player, Teams.Human,
													className);
											Bukkit.getServer().getPluginManager().callEvent(classEvent);
											if (!classEvent.isCancelled())
											{
												Main.humanClasses.put(player.getName(), className);
												player.sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, className));
											}
										} else
										{
											player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
										}
									} else if (sign.getLine(3).contains("Zombie"))
									{
										if (className.equalsIgnoreCase("None"))
										{
											Main.zombieClasses.remove(player.getName());
											player.sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, "None"));
										} else if (player.hasPermission("Infected.Classes.Zombie") || player.hasPermission("Infected.Classes.Zombie." + className))
										{
											InfectedClassSelectEvent classEvent = new InfectedClassSelectEvent(
													player, Teams.Zombie,
													className);
											Bukkit.getServer().getPluginManager().callEvent(classEvent);
											if (!classEvent.isCancelled())
											{
												Main.zombieClasses.put(player.getName(), className);
												player.sendMessage(Messages.sendMessage(Msgs.CLASSES_CHOOSEN, player, className));
											}
										} else
										{
											player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickShopSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (player.getItemInHand().getType() == Material.BOW)
			{
				event.setUseItemInHand(Result.DEFAULT);
				event.setCancelled(false);
			} else
			{

				if (Infected.isPlayerInLobby(player) || Infected.isPlayerInGame(player))
				{
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						Block b = event.getClickedBlock();
						if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
						{
							if (Files.getShop().getBoolean("Use"))
							{
								Sign sign = ((Sign) b.getState());
								if (sign.getLine(0).contains("[Infected]") && !sign.getLine(1).contains("Playing") && !sign.getLine(1).contains("CmdSet") && !sign.getLine(1).toLowerCase().contains("click to use") && !sign.getLine(1).contains("Class"))
								{
									String prices = ChatColor.stripColor(sign.getLine(3).replaceAll("Cost: ", ""));
									int price;
									int points = Infected.playerGetPoints(player.getName());

									if (prices.contains(plugin.getConfig().getString("Vault Support.Symbol")) && plugin.getConfig().getBoolean("Vault Support.Enable"))
										price = Integer.valueOf(prices.replace(plugin.getConfig().getString("Vault Support.Symbol"), ""));
									else
										price = Integer.valueOf(prices);

									String itemname = ChatColor.stripColor(sign.getLine(1));

									Material im = null;
									for (Material item : Material.values())
										if (item.toString().equalsIgnoreCase(itemname))
										{
											im = item;
											break;
										}
									if (im != null)
									{
										Material item = Material.getMaterial(itemname);
										if (player.hasPermission("Infected.Shop") || player.hasPermission("Infected.Shop." + item.getId()))
										{
											if (price <= points)
											{

												ItemStack stack = ItemHandler.getItemStack(String.valueOf(Material.valueOf(itemname).getId()));
												InfectedShopPurchaseEvent shopEvent = new InfectedShopPurchaseEvent(
														player, sign, stack,
														price);
												Bukkit.getServer().getPluginManager().callEvent(shopEvent);
												if (!shopEvent.isCancelled())
												{
													Infected.playerSetPoints(player.getName(), points, price);
													if (!player.getInventory().contains(stack))
													{
														player.getInventory().addItem(stack);
														if (Files.getShop().getBoolean("Save Items"))
														{
															if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
															{
																Infected.playerAddToShopInventory(player, stack);
															}
														}
													} else
													{
														player.getInventory().addItem(new ItemStack(
																item, +1));
													}
													player.sendMessage(Messages.sendMessage(Msgs.SHOP_PURCHASE, player, itemname));
													if (Files.getShop().getBoolean("Save Items") || Files.getShop().getIntegerList("Save These Items No Matter What").contains(item.getId()))
													{
														if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
														{
															Infected.playerSaveShopInventory(player);
														}
													}
												}
											} else
											{
												player.sendMessage(Messages.sendMessage(Msgs.SHOP_INVALIDPOINTS, player, null));
												player.sendMessage(Messages.sendMessage(Msgs.SHOP_NEEDMOREPOINTS, player, String.valueOf(price - points)));
											}
											Files.savePlayers();
											player.updateInventory();
										} else
										{
											player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
										}
									} else
									{
										if (Files.getShop().contains(itemname))
										{
											ItemStack is = ItemHandler.getItemStack(Files.getShop().getString(itemname));
											for (Material items : Material.values())
											{
												if (items == is.getType())
												{
													im = items;
													break;
												}
											}
											if (price <= points)
											{
												if (player.hasPermission("Infected.Shop") || player.hasPermission("Infected.Shop." + itemname))
												{
													InfectedShopPurchaseEvent shopEvent = new InfectedShopPurchaseEvent(
															player, sign, is,
															price);
													Bukkit.getServer().getPluginManager().callEvent(shopEvent);
													if (!shopEvent.isCancelled())
													{
														Infected.playerSetPoints(player.getName(), points, price);
														ItemMeta i = is.getItemMeta();
														if (!player.getInventory().contains(is))
														{
															i.setDisplayName("§e" + itemname);
															is.setItemMeta(i);
															player.getInventory().addItem(is);
															if ((Files.getShop().getBoolean("Save Items") || Files.getShop().getIntegerList("Save These Items No Matter What").contains(is.getTypeId())) && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId()))))
															{
																if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
																{
																	Infected.playerSaveShopInventory(player);
																}
															}
														} else
														{
															i.setDisplayName("§e" + itemname);
															is.setItemMeta(i);
															player.getInventory().addItem(is);
														}

														player.sendMessage(Messages.sendMessage(Msgs.SHOP_PURCHASE, player, itemname));
														if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId()))))
														{
															if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
															{
																Infected.playerSaveShopInventory(player);
															}
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
											event.setCancelled(true);
										} else
										{
											Location loc = event.getClickedBlock().getLocation();
											String i = Files.getSigns().getString("Shop Signs." + LocationHandler.getLocationToString(loc));
											int itemi = ItemHandler.getItemID(i);
											Material item = Material.getMaterial(itemi);
											if (price < points + 1)
											{
												Infected.playerSetPoints(player.getName(), points, price);
												ItemStack stack = ItemHandler.getItemStack(i);
												if (!player.getInventory().contains(stack))
												{
													player.getInventory().addItem(stack);
													if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(item.getId()))))
													{
														if (Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion))
														{
															Infected.playerSaveShopInventory(player);
														}
													}
												} else
												{
													player.getInventory().addItem(new ItemStack(
															item, +1));
												}
												player.sendMessage(Messages.sendMessage(Msgs.SHOP_PURCHASE, player, item.name()));
											} else
											{
												player.sendMessage(Messages.sendMessage(Msgs.SHOP_INVALIDPOINTS, player, null));
												player.sendMessage(Messages.sendMessage(Msgs.SHOP_NEEDMOREPOINTS, player, String.valueOf(price - points)));
											}
											Files.savePlayers();
											player.updateInventory();
										}
									}
								} else if ((event.getClickedBlock().getTypeId() == 330 || event.getClickedBlock().getTypeId() == 96 || event.getClickedBlock().getTypeId() == 324 || event.getClickedBlock().getTypeId() == 69 || event.getClickedBlock().getTypeId() == 77 || event.getClickedBlock().getTypeId() == 143 || event.getClickedBlock().getTypeId() == 147 || event.getClickedBlock().getTypeId() == 148 || event.getClickedBlock().getTypeId() == 70 || event.getClickedBlock().getTypeId() == 72) && !Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())) && !plugin.getConfig().getBoolean("Allow Interacting"))
								{
									event.setCancelled(true);
								}
							} else if ((event.getClickedBlock().getTypeId() == 330 || event.getClickedBlock().getTypeId() == 96 || event.getClickedBlock().getTypeId() == 324 || event.getClickedBlock().getTypeId() == 69 || event.getClickedBlock().getTypeId() == 77 || event.getClickedBlock().getTypeId() == 143 || event.getClickedBlock().getTypeId() == 147 || event.getClickedBlock().getTypeId() == 148 || event.getClickedBlock().getTypeId() == 70 || event.getClickedBlock().getTypeId() == 72) && !Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())) && !plugin.getConfig().getBoolean("Allow Interacting"))
							{
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickCMDSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (player.getItemInHand().getType() == Material.BOW)
			{
				event.setUseItemInHand(Result.DEFAULT);
				event.setCancelled(false);
			} else
			{
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					Block b = event.getClickedBlock();
					if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
					{
						Sign sign = ((Sign) b.getState());
						if (sign.getLine(0).contains("[Infected]") && sign.getLine(1).toLowerCase().contains("click to use"))
						{
							String command = ChatColor.stripColor(sign.getLine(2));
							String commandarg = ChatColor.stripColor(sign.getLine(3));
							event.getPlayer().performCommand("Infected " + command + " " + commandarg);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerClickCmdSetSign(PlayerInteractEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();

			if (player.getItemInHand().getType() == Material.BOW)
			{
				event.setUseItemInHand(Result.DEFAULT);
				event.setCancelled(false);
			} else
			{

				if (Infected.isPlayerInLobby(player) || Infected.isPlayerInGame(player))
				{
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						Block b = event.getClickedBlock();
						if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
						{

							Sign sign = ((Sign) b.getState());
							if (sign.getLine(0).contains("[Infected]") && sign.getLine(1).contains("CmdSet"))
							{
								String prices = ChatColor.stripColor(sign.getLine(3).replaceAll("Cost: ", ""));
								int price;
								int points = Infected.playerGetPoints(player.getName());

								if (prices.contains(plugin.getConfig().getString("Vault Support.Symbol")) && plugin.getConfig().getBoolean("Vault Support.Enable"))
									price = Integer.valueOf(prices.replace(plugin.getConfig().getString("Vault Support.Symbol"), ""));
								else
									price = Integer.valueOf(prices);

								String cmdset = ChatColor.stripColor(sign.getLine(2));

								if (player.hasPermission("Infected.CmdSets") || player.hasPermission("Infected.CmdSets." + cmdset))
								{
									if (price <= points)
									{
										Infected.playerSetPoints(player.getName(), points, price);

										for (String row : Infected.filesGetSigns().getStringList("Command Sets." + cmdset))
										{
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), row.replaceAll("<player>", event.getPlayer().getName()));
										}

									} else
									{
										player.sendMessage(Main.I + "Not enough points!");
										player.sendMessage(Main.I + "You need " + (price - points) + " more points");
									}
									Files.savePlayers();
								} else
								{
									player.sendMessage(Main.I + ChatColor.RED + "You don't have permission to buy this set!");
								}
							}
						}
					}
				}
			}
		}
	}

	// ======================================================================================================================

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateShop(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			if (Files.getShop().getBoolean("Use"))
			{
				Player player = event.getPlayer();
				if (event.getLine(0).equalsIgnoreCase("[Infected]") && !event.getLine(1).equalsIgnoreCase("Info") && !event.getLine(1).equalsIgnoreCase("CmdSet") && !event.getLine(1).equalsIgnoreCase("cmd") && !event.getLine(1).equalsIgnoreCase("Class"))
				{
					if (!player.hasPermission("Infected.Setup"))
					{
						player.sendMessage(Main.I + "Invalid Permissions.");
						event.setCancelled(true);
					}

					if (event.getLine(1).isEmpty())
					{
						event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 2 is empty");
						event.getBlock().breakNaturally();
						event.setCancelled(true);
					} else if (event.getLine(2).isEmpty())
					{
						event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 3 is empty!");
						event.getBlock().breakNaturally();
						event.setCancelled(true);
					} else
					{
						if (Files.getShop().contains(event.getLine(1)))
						{
							String s = Files.getShop().getString(event.getLine(1));
							Material im = null;
							ItemStack is = new ItemStack(ItemHandler.getItem(s));
							for (Material items : Material.values())
							{
								if (items == is.getType())
								{
									im = items;
									break;
								}
							}
							if (im != null)
							{
								String price = event.getLine(2);
								boolean vault = false;
								if (plugin.getConfig().getBoolean("Vault Support.Enable") && price.contains("\\"+plugin.getConfig().getString("Vault Support.Symbol")))
									vault = true;
								price = price.replaceAll("\\"+plugin.getConfig().getString("Vault Support.Symbol"), "");
								try
								{
									Integer.valueOf(price);
								} catch (NumberFormatException nfe)
								{
									event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Cost must be a number!");
									event.getBlock().breakNaturally();
									event.setCancelled(true);
								}

								event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
								event.setLine(1, ChatColor.GRAY + event.getLine(1));
								event.setLine(2, ChatColor.GREEN + "Click To Buy");
								event.setLine(3, ChatColor.DARK_RED + "Cost: " +(vault ? ("\\"+plugin.getConfig().getString("Vault Support.Symbol")): "") + price);
								

							}
						} else
						{
							int itemid;
							
							try
							{
								itemid = ItemHandler.getItemID(event.getLine(1));
							} catch (NumberFormatException nfe)
							{
								event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Invalid Item");
								event.getBlock().breakNaturally();
								event.setCancelled(true);
								return;
							}
							Material im = null;
							itemid = ItemHandler.getItemID(event.getLine(1));
							short itemdata = ItemHandler.getItemData(event.getLine(1));
							System.out.println(itemdata);
							for (Material item : Material.values())
								if (item.getId() == Integer.valueOf(itemid))
								{
									im = item;
									break;
								}
							if (im != null)
							{
								boolean vault = false;
								String price = event.getLine(2);
							
								if (plugin.getConfig().getBoolean("Vault Support.Enable") && price.contains("\\"+plugin.getConfig().getString("Vault Support.Symbol")))
									vault = true;
								
								price = price.replaceAll("\\"+plugin.getConfig().getString("Vault Support.Symbol"), "");
								try
								{
									Integer.valueOf(price);
								} catch (NumberFormatException nfe)
								{
									event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Cost must be a number!");
									event.getBlock().breakNaturally();
									event.setCancelled(true);
								}
								Material item = Material.getMaterial(Integer.valueOf(itemid));
								event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
								event.setLine(1, ChatColor.GRAY + item.name().toUpperCase() + (itemdata == 0 ? "" :":" + itemdata));
								event.setLine(2, ChatColor.GREEN + "Click To Buy");
								event.setLine(3, ChatColor.DARK_RED + "Cost: " +(vault ? ("\\"+plugin.getConfig().getString("Vault Support.Symbol")): "") + price);
								
								Location loc = event.getBlock().getLocation();
								Infected.filesGetSigns().set("Shop Signs." + LocationHandler.getLocationToString(loc), itemid + ":" + Integer.valueOf(itemdata));
								Infected.filesSaveSigns();
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateInfoSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{

			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("Info"))
			{
				if (!player.hasPermission("Infected.Setup"))
				{
					player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
					event.setCancelled(true);
				}
				if (event.getLine(1).isEmpty())
				{
					event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 2 is empty");
					event.getBlock().breakNaturally();
					event.setCancelled(true);
				} else
				{
					if (plugin.getConfig().getBoolean("Info Signs.Enabled"))
					{
						if (!player.hasPermission("Infected.Setup"))
						{
							player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
							event.setCancelled(true);
						}
						event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");

						String status = "";
						if (Infected.getGameState() == GameState.VOTING)
							status = "Voting";
						if (Infected.getGameState() == GameState.BEFOREINFECTED)
							status = "B4 Infected";
						if (Infected.getGameState() == GameState.STARTED)
							status = "Started";
						else
							status = "In Lobby";
						int time = Main.currentTime;
						event.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Infected.listInGame().size()));
						event.setLine(2, ChatColor.GOLD + status);
						if (Infected.getGameState() == GameState.STARTED || Infected.getGameState() == GameState.VOTING)
							event.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));
						else
							event.setLine(3, "");

						if (Files.getSigns().getStringList("Info Signs") == null)
						{
							String[] list = { LocationHandler.getLocationToString(event.getBlock().getLocation()) };
							Files.getSigns().set("Info Signs", list);
							Files.saveSigns();
						} else
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

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateCMDSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{

			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("Cmd"))
			{
				if (!player.hasPermission("Infected.Setup"))
				{
					player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
					event.setCancelled(true);
				}
				if (event.getLine(1).isEmpty())
				{
					event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Line 2 is empty");
					event.getBlock().breakNaturally();
					event.setCancelled(true);
				} else
				{
					event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
					event.setLine(1, ChatColor.GREEN + "Click to use CMD");
					event.setLine(2, ChatColor.GOLD + event.getLine(2).toUpperCase());
					event.setLine(3, ChatColor.GOLD + event.getLine(3).toUpperCase());

				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateClassSign(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("Class"))
			{
				if (!player.hasPermission("Infected.Setup"))
				{
					player.sendMessage(Messages.sendMessage(Msgs.ERROR_NOPERMISSION, player, null));
					event.setCancelled(true);
				}
				if (!plugin.getConfig().getBoolean("Class Support"))
				{
					player.sendMessage(Messages.sendMessage(Msgs.CLASSES_DISABLED, player, null));
				} else
				{
					if (event.getLine(3).equalsIgnoreCase("Zombie"))
					{
						if (Infected.filesGetClasses().getConfigurationSection("Classes.Zombie") == null)
						{
							player.sendMessage(plugin.I + ChatColor.RED + " Missing classes... wtf?");
							event.setCancelled(true);
							event.getBlock().breakNaturally();
						}
						boolean classFound = false;
						String className = "None";
						for (String classes : Infected.filesGetClasses().getConfigurationSection("Classes.Zombie").getKeys(true))
						{
							if ((!classes.contains(".")) && event.getLine(2).equalsIgnoreCase(classes))
							{
								classFound = true;
								className = classes;
								break;
							}
						}
						if (classFound || event.getLine(2).equalsIgnoreCase("None"))
						{

							event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
							event.setLine(1, ChatColor.GRAY + "Class");
							event.setLine(2, ChatColor.GREEN + className);
							event.setLine(3, ChatColor.RED + "-> Zombie <-");
						}
					} else if (event.getLine(3).equalsIgnoreCase("Human"))
					{
						if (Infected.filesGetClasses().getConfigurationSection("Classes.Human") == null)
						{
							player.sendMessage(plugin.I + ChatColor.RED + " Missing classes... wtf?");
							event.setCancelled(true);
							event.getBlock().breakNaturally();
						}
						boolean classFound = false;
						String className = "None";
						for (String classes : Infected.filesGetClasses().getConfigurationSection("Classes.Human").getKeys(true))
						{

							if ((!classes.contains(".")) && event.getLine(2).equalsIgnoreCase(classes))
							{
								classFound = true;
								className = classes;
								break;
							}
						}
						if (classFound || event.getLine(2).equalsIgnoreCase("None"))
						{
							event.setLine(0, ChatColor.DARK_RED + "" + "[Infected]");
							event.setLine(1, ChatColor.GRAY + "Class");
							event.setLine(2, ChatColor.GREEN + className);
							event.setLine(3, ChatColor.DARK_GREEN + "-> Human <-");
						}
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Well we managed to see you attempt to make a class sign, but thats not a class...");
						event.setCancelled(true);
						event.getBlock().breakNaturally();
					}
				}
			}

		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCreateCmdset(SignChangeEvent event) {
		if (!event.isCancelled())
		{
			Player player = event.getPlayer();
			if (event.getLine(0).equalsIgnoreCase("[Infected]") && event.getLine(1).equalsIgnoreCase("CmdSet"))
			{
				if (!player.hasPermission("Infected.Setup"))
				{
					player.sendMessage(Main.I + "Invalid Permissions.");
					event.setCancelled(true);
				}

				if (Infected.filesGetSigns().getConfigurationSection("Command Sets") != null)
				{
					String commandSet = "None";
					for (String sets : Infected.filesGetSigns().getConfigurationSection("Command Sets").getKeys(true))
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
					} else
					{
						player.sendMessage(plugin.I + ChatColor.RED + " Well we managed to see you attempt to make a command set sign, but thats not a set...");
						event.setCancelled(true);
						event.getBlock().breakNaturally();
					}

				} else
				{
					player.sendMessage(plugin.I + ChatColor.RED + " Well we managed to see you attempt to make a command set sign, but theres no sets...");
					event.setCancelled(true);
					event.getBlock().breakNaturally();
				}
			}
		}
	}

}