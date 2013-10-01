
package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;
import java.util.List;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Methods;
import me.xxsniperzzxx_sd.infected.Main.GameState;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.ItemHandler;
import me.xxsniperzzxx_sd.infected.Tools.Updater;

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
									player.sendMessage(Methods.sendMessage("Error_NoClassSupport", player, null, null));
								} else
								{
									String className = ChatColor.stripColor(sign.getLine(2));
									if (sign.getLine(3).contains("Human"))
									{
										if (className.equalsIgnoreCase("None"))
										{
											Main.humanClasses.remove(player.getName());
											player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You no longer have a selected human class");
										} else if (player.hasPermission("Infected.Classes.Human") || player.hasPermission("Infected.Classes.Human." + className))
										{
											Main.humanClasses.put(player.getName(), className);
											player.sendMessage(Main.I + ChatColor.DARK_AQUA + "Your current human class is: " + sign.getLine(2));

										} else
										{
											player.sendMessage(Main.I + ChatColor.RED + "You don't have permission to buy this item!");
										}
									} else if (sign.getLine(3).contains("Zombie"))
									{
										if (className.equalsIgnoreCase("None"))
										{
											Main.zombieClasses.remove(player.getName());
											player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You no longer have a selected zombie class");
										} else if (player.hasPermission("Infected.Classes.Zombie") || player.hasPermission("Infected.Classes.Zombie." + className))
										{
											Main.zombieClasses.put(player.getName(), className);
											player.sendMessage(Main.I + ChatColor.DARK_AQUA + "Your current zombie class is: " + sign.getLine(2));

										} else
										{
											player.sendMessage(Main.I + ChatColor.RED + "You don't have permission to buy this item!");
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
									int points = Infected.playerGetPoints(player);

									if (prices.contains(plugin.getConfig().getString("Vault Support.Symbol")) && plugin.getConfig().getBoolean("Vault Support.Enable"))
										price = Integer.valueOf(prices.replace(plugin.getConfig().getString("Vault Support.Symbol"), ""));
									else
										price = Integer.valueOf(prices);

									String itemstring = ChatColor.stripColor(sign.getLine(1));
									String itemname = null;
									short itemdata = 0;
									String s = itemstring;
									if (s.contains(":"))
									{
										String[] s1 = s.split(":");
										itemname = s1[0];
										itemdata = Short.valueOf(s1[1]);
									} else
									{
										itemname = s;
										itemdata = 0;
									}
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
												Infected.playerSetPoints(player, points, price);
												ItemStack stack = new ItemStack(
														item);
												stack.setDurability(itemdata);
												if (!player.getInventory().contains(stack))
												{
													player.getInventory().addItem(stack);
													if (Files.getShop().getBoolean("Save Items"))
													{
														Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
														if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
															Infected.playerAddToShopInventory(player, stack);
														}
													}
												} else
												{
													player.getInventory().addItem(new ItemStack(
															item, +1));
												}
												player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You have bought a " + item);
												if (Files.getShop().getBoolean("Save Items") || Files.getShop().getIntegerList("Save These Items No Matter What").contains(item.getId()))
												{
													Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
													if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
														Infected.playerSaveShopInventory(player);
													}
												}
											} else
											{
												player.sendMessage(Main.I + "Not enough points!");
												player.sendMessage(Main.I + "You need " + (price - points) + " more points");
											}
											Files.savePlayers();
											player.updateInventory();
										} else
										{
											player.sendMessage(Main.I + ChatColor.RED + "You don't have permission to buy this item!");
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
													Infected.playerSetPoints(player, points, price);
													ItemMeta i = is.getItemMeta();
													if (!player.getInventory().contains(is))
													{
														i.setDisplayName("§e" + itemname);
														is.setItemMeta(i);
														player.getInventory().addItem(is);
														if ((Files.getShop().getBoolean("Save Items") || Files.getShop().getIntegerList("Save These Items No Matter What").contains(is.getTypeId())) && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId()))))
														{
															Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
															if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
																Infected.playerSaveShopInventory(player);
															}
														}
													} else
													{
														i.setDisplayName("§e" + itemname);
														is.setItemMeta(i);
														player.getInventory().addItem(is);
													}
													player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You have bought a " + itemname);
													if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(is.getTypeId()))))
													{
														Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
														if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
															Infected.playerSaveShopInventory(player);
														}
													}
												}
											} else
											{
												player.sendMessage(Main.I + "Not enough points!");
												player.sendMessage(Main.I + "You need " + (price - points) + " more points");
											}
											Files.savePlayers();
											player.updateInventory();
											event.setCancelled(true);
										}
										Location loc = event.getClickedBlock().getLocation();
										if (loc.getBlock().getType() == Material.SIGN_POST || loc.getBlock().getType() == Material.WALL_SIGN)
										{

											String i = Files.getSigns().getString("Shop Signs." + Methods.getLocationToString(loc));
											String itemi = null;
											short itemd = 0;
											if (i.contains(":"))
											{
												String[] i1 = i.split(":");
												itemi = i1[0];
												itemdata = Short.valueOf(i1[1]);
											} else
											{
												itemi = i;
												itemd = 0;
											}
											Material item = Material.getMaterial(Integer.valueOf(itemi));
											if (price < points + 1)
											{
												Infected.playerSetPoints(player, points, price);
												ItemStack stack = new ItemStack(
														Material.getMaterial(Integer.valueOf(itemi)));
												stack.setDurability(itemd);
												if (!player.getInventory().contains(stack))
												{
													player.getInventory().addItem(stack);
													if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(item.getId()))))
													{
														Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
														if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
															Infected.playerSaveShopInventory(player);
														}
													}
												} else
												{
													player.getInventory().addItem(new ItemStack(
															item, +1));
												}
												player.sendMessage(Main.I + ChatColor.DARK_AQUA + "You have bought a " + item);
												if (Files.getShop().getBoolean("Save Items") && (!Infected.filesGetGrenades().contains(String.valueOf(item.getId()))))
												{
													Updater updater = new Updater(Main.me, "Infected-Core", Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
													if(Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion)){
														Infected.playerSaveShopInventory(player);
													}
												}

											} else
											{
												player.sendMessage(Main.I + "Not enough points!");
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
								int points = Infected.playerGetPoints(player);

								if (prices.contains(plugin.getConfig().getString("Vault Support.Symbol")) && plugin.getConfig().getBoolean("Vault Support.Enable"))
									price = Integer.valueOf(prices.replace(plugin.getConfig().getString("Vault Support.Symbol"), ""));
								else
									price = Integer.valueOf(prices);

								String cmdset = ChatColor.stripColor(sign.getLine(2));

								if (player.hasPermission("Infected.CmdSets") || player.hasPermission("Infected.CmdSets." + cmdset))
								{
									if (price <= points)
									{
										Infected.playerSetPoints(player, points, price);

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
								if (plugin.getConfig().getBoolean("Vault Support.Enable") && price.contains(plugin.getConfig().getString("Vault Support.Symbol")))
									vault = true;
								price = price.replaceAll(plugin.getConfig().getString("Vault Support.Symbol"), "");
								System.out.println(vault);
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
								if (vault)
									event.setLine(3, ChatColor.DARK_RED + "Cost: " + plugin.getConfig().getString("Vault Support.Symbol") + price);
								else
									event.setLine(3, ChatColor.DARK_RED + "Cost: " + price);

							}
						} else
						{
							try
							{
								String s = event.getLine(1);
								String[] s1 = s.split(":");
								@SuppressWarnings("unused")
								int itemid = Integer.valueOf(s1[0]);
							} catch (NumberFormatException nfe)
							{
								event.getPlayer().sendMessage(Main.I + ChatColor.RED + "Invalid Item");
								event.getBlock().breakNaturally();
								event.setCancelled(true);
								return;
							}
							Material im = null;
							String itemid = null;
							String itemdata = null;
							String s = event.getLine(1);

							if (s.contains(":"))
							{
								String[] s1 = s.split(":");
								itemid = s1[0];
								itemdata = s1[1];
							} else
							{
								itemid = s;
								itemdata = "";
							}
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
								if (plugin.getConfig().getBoolean("Vault Support.Enable") && event.getLine(2).startsWith(plugin.getConfig().getString("Vault Support.Symbol")))
									vault = true;
								price = price.replaceAll(plugin.getConfig().getString("Vault Support.Symbol"), "");
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
								event.setLine(1, ChatColor.GRAY + item.name().toUpperCase() + ":" + itemdata);
								if (itemdata == "")
								{
									event.setLine(1, ChatColor.GRAY + item.name().toUpperCase());
								}
								event.setLine(2, ChatColor.GREEN + "Click To Buy");
								if (vault)
									event.setLine(3, ChatColor.DARK_RED + "Cost: " + plugin.getConfig().getString("Vault Support.Symbol") + price);
								else
									event.setLine(3, ChatColor.DARK_RED + "Cost: " + price);
								if (itemdata == "")
								{
									itemdata = "0";
								}

								Location loc = event.getBlock().getLocation();

								Infected.filesGetSigns().set("Shop Signs." + Methods.getLocationToString(loc), itemid + ":" + Integer.valueOf(itemdata));
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
					player.sendMessage(Main.I + "Invalid Permissions.");
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
							player.sendMessage(Main.I + "Invalid Permissions.");
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
						event.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));

						if (Files.getSigns().getStringList("Info Signs") == null)
						{
							String[] list = { Methods.getLocationToString(event.getBlock().getLocation()) };
							Files.getSigns().set("Info Signs", list);
							Files.saveSigns();
						} else
						{
							List<String> list = Files.getSigns().getStringList("Info Signs");
							list.add(Methods.getLocationToString(event.getBlock().getLocation()));
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
					player.sendMessage(Main.I + "Invalid Permissions.");
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
					player.sendMessage(Main.I + "Invalid Permissions.");
					event.setCancelled(true);
				}
				if (!plugin.getConfig().getBoolean("Class Support"))
				{
					player.sendMessage(Methods.sendMessage("Error_NoClassSupport", player, null, null));
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