package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.AttackingManager;
import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Methods;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("static-access")
public class PlayerListener implements Listener {

	public Main Main = new Main();
	public ArrayList<String> item = new ArrayList<String>();

	public Main plugin;

	public PlayerListener(Main instance)
	{
		this.plugin = instance;
	}

	// Create global players (Bows and melee)
	Player playeruser = null;
	Player useruser = null;

	// Settings for effects
	int effect = 0;
	boolean effectb = false;

	// Check for updates when a player joins, making sure they are OP
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.getConfig().getBoolean("Check For Updates"))
			if (plugin.update && player.isOp())
				if (plugin.getConfig().getBoolean("Update Notification"))
				{
					player.sendMessage(Main.I + ChatColor.RED + "An update is available: " + Main.name);
					player.sendMessage(Main.I + ChatColor.RED + "Download it at: http://dev.bukkit.org/server-mods/infected-core/");
				}
	}

	// Disable dropping items if the player is in game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (Main.inGame.contains(e.getPlayer().getName()))
			e.setCancelled(true);
	}

	// If the game hasn't started yet
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerUseInventory(InventoryClickEvent e) {
		if (Main.inGame.contains(e.getWhoClicked().getName()))
			if (!Infected.booleanIsStarted())
			{
				Player player = (Player) e.getViewers().get(0);
				player.sendMessage(Methods.sendMessage("Error_CantEditInventoryYet", null, null, null));
				e.setCancelled(true);
			}
	}

	// Check if the player is in game and the situation around them breaking a
	// block
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		if (!e.isCancelled())
		{
			if (Main.db.isInfoSign(e.getBlock().getLocation()))
			{
				Main.db.removeInfoSignLoc(e.getBlock().getLocation());
			}
			if (Main.db.isSign(e.getBlock().getLocation()))
			{
				Main.db.removeSign(e.getBlock().getLocation());
			}
			if (Main.inLobby.contains(e.getPlayer().getName()))
			{
				e.setCancelled(true);
			} else if (Main.inGame.contains(e.getPlayer().getName()))
			{
				e.getBlock().getDrops().clear();
				if (Files.getArenas().getIntegerList("Arenas." + Main.playingin + ".Allow Breaking Of.Global").contains(e.getBlock().getTypeId()) || Files.getArenas().getIntegerList("Arenas." + Main.playingin + ".Allow Breaking Of." + Infected.playerGetGroup(e.getPlayer())).contains(e.getBlock().getTypeId()))
				{
					if (!plugin.db.getBlocks().containsKey(e.getBlock().getLocation()))
					{
						Location loc = e.getBlock().getLocation();
						plugin.db.getBlocks().put(loc, e.getBlock().getType());
						e.getBlock().getDrops().clear();
					}
				} else
				{
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.isCancelled())
			if (Infected.booleanIsStarted() && Infected.isPlayerInGame(e.getPlayer()))
			{
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					Block b = e.getClickedBlock();
					if (e.getClickedBlock().getTypeId() == 54)
					{
						Chest chest = (Chest) b.getState();
						Inventory z;
						z = chest.getBlockInventory();
						if (!Main.db.getChests().containsKey(b.getLocation()))
							Main.db.saveChest(b.getLocation(), z);
					}
				}
			}
	}

	// Check to make sure they arn't trying to place a block in game
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerBlockPlace(BlockPlaceEvent e) {
		if (Main.inLobby.contains(e.getPlayer().getName()))
		{
			e.setCancelled(true);
		} else if (Main.inGame.contains(e.getPlayer().getName()))
		{
			Location loc = e.getBlock().getLocation();
			plugin.db.getBlocks().put(loc, Material.AIR);
		}
	}

	// See if a player got kicked and if it effected the game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerGetKicked(final PlayerKickEvent e) {
		boolean Started = plugin.Booleans.get("Started");
		boolean BeforeGame = plugin.Booleans.get("BeforeGame");
		boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
		plugin.Creating.remove(e.getPlayer().getName());

		// If a player logs out well playing, make sure it doesn't effect the
		// game, end the game if it does
		if (plugin.inGame.contains(e.getPlayer().getName()))
		{
			plugin.inGame.remove(e.getPlayer().getName());

			// Leave well everyones in the lobby
			if (!Started && !BeforeGame && !BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
					System.out.println("Leave: Leaving, wellin lobby, no timers active");

				e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
				Methods.resetp(e.getPlayer());
				for (Player players : Bukkit.getOnlinePlayers())
				{
					if (Infected.isPlayerInGame(players))
						players.sendMessage(Methods.sendMessage("Leave_InLobby", e.getPlayer(), null, null));
				}
			}

			// Voting has started, less then 2 people left
			else if (!Started && BeforeGame && !BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
					System.out.println("Leave: Before Voting");

				if (Main.inGame.size() == 1)
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: Before Voting(Triggered)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				} else
				{
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
						}
					}
				}
			}

			// In Arena, before first Infected
			else if (!Started && !BeforeGame && BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
				{
					System.out.println("Leave: In Arena Before Infected");
				}

				if (Main.inGame.size() == 1)
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena Before Infected(Triggered)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				} else
				{
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
						}
					}
				}
			}

			// In Arena, Game has started
			else if (Started && !BeforeGame && !BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
				{
					System.out.println("Leave: In Arena, Game Has Started");
				}
				if (Main.inGame.size() == 1)
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Players)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				}

				// If Not Enough zombies remain
				else if (Main.zombies.size() == 1 && Infected.isPlayerZombie(e.getPlayer()))
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					Random r = new Random();
					int alpha = r.nextInt(Main.inGame.size());
					String name = Main.inGame.get(alpha);
					Player zombie = Bukkit.getServer().getPlayer(name);
					zombie.sendMessage(Main.I + ChatColor.RED + "You are the new Infected! Good luck!");
					Main.zombies.clear();
					Main.zombies.add(zombie.getName());
					Main.Winners.remove(zombie.getName());
					if (plugin.getConfig().getBoolean("New Zombie Tp"))
					{
						Methods.respawn(zombie);
					}
					zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
					if (plugin.getConfig().getBoolean("Zombie Abilties") == true)
					{
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 2000, 2), true);
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.JUMP, 2000, 1), true);
					}
					Methods.zombifyPlayer(zombie);
					zombie.setHealth(20);
					Methods.equipZombies(zombie);

					// Inform humans of infected, prepare them
					for (Player online : Bukkit.getServer().getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(online))
						{
							if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
							{

								// if(Main.humans.contains(online)) {
								Main.humans.add(online.getName());
								online.sendMessage(Main.I + ChatColor.RED + zombie.getName() + " has became the new Infected!");
								online.setHealth(20);
								online.playEffect(online.getLocation(), Effect.SMOKE, 1);
							}
						}
					}
				}

				// Not enough humans left
				else if (Main.humans.size() == 1 && plugin.humans.contains(e.getPlayer().getName()))
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Humans)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				} else
				{
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
						}
					}
				}
			}
		}
	}

	// When players leave the server willingly, make sure it doesn't effect the
	// game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(final PlayerQuitEvent e) {
		boolean Started = plugin.Booleans.get("Started");
		boolean BeforeGame = plugin.Booleans.get("BeforeGame");
		boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
		plugin.Creating.remove(e.getPlayer().getName());

		// If a player logs out well playing, make sure it doesn't effect the
		// game, end the game if it does
		if (plugin.inGame.contains(e.getPlayer().getName()))
		{
			plugin.inGame.remove(e.getPlayer().getName());

			// Leave well everyones in the lobby
			if (!Started && !BeforeGame && !BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
					System.out.println("Leave: Leaving, wellin lobby, no timers active");

				e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
				Methods.resetp(e.getPlayer());
				for (Player players : Bukkit.getOnlinePlayers())
				{
					if (Infected.isPlayerInGame(players))
						players.sendMessage(Methods.sendMessage("Leave_InLobby", e.getPlayer(), null, null));
				}
			}

			// Voting has started, less then 2 people left
			else if (!Started && BeforeGame && !BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
					System.out.println("Leave: Before Voting");

				if (Main.inGame.size() == 1)
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: Before Voting(Triggered)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				} else
				{
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
						}
					}
				}
			}

			// In Arena, before first Infected
			else if (!Started && !BeforeGame && BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
				{
					System.out.println("Leave: In Arena Before Infected");
				}

				if (Main.inGame.size() == 1)
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena Before Infected(Triggered)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				} else
				{
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
						}
					}
				}
			}

			// In Arena, Game has started
			else if (Started && !BeforeGame && !BeforeFirstInf)
			{
				if (plugin.getConfig().getBoolean("Debug"))
				{
					System.out.println("Leave: In Arena, Game Has Started");
				}
				if (Main.inGame.size() == 1)
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Players)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				}

				// If Not Enough zombies remain
				else if (Main.zombies.size() == 1 && Infected.isPlayerZombie(e.getPlayer()))
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					Random r = new Random();
					int alpha = r.nextInt(Main.inGame.size());
					String name = Main.inGame.get(alpha);
					Player zombie = Bukkit.getServer().getPlayer(name);
					zombie.sendMessage(Main.I + ChatColor.RED + "You are the new Infected! Good luck!");
					Main.zombies.clear();
					Main.zombies.add(zombie.getName());
					Main.Winners.remove(zombie.getName());
					if (plugin.getConfig().getBoolean("New Zombie Tp"))
					{
						Methods.respawn(zombie);
					}
					zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
					if (plugin.getConfig().getBoolean("Zombie Abilties") == true)
					{
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 2000, 2), true);
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.JUMP, 2000, 1), true);
					}
					Methods.zombifyPlayer(zombie);
					zombie.setHealth(20);
					Methods.equipZombies(zombie);

					// Inform humans of infected, prepare them
					for (Player online : Bukkit.getServer().getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(online))
						{
							if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
							{

								// if(Main.humans.contains(online)) {
								Main.humans.add(online.getName());
								online.sendMessage(Main.I + ChatColor.RED + zombie.getName() + " has became the new Infected!");
								online.setHealth(20);
								online.playEffect(online.getLocation(), Effect.SMOKE, 1);
							}
						}
					}
				}

				// Not enough humans left
				else if (Main.humans.size() == 1 && plugin.humans.contains(e.getPlayer().getName()))
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Humans)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Methods.tp2LobbyAfter(players);
						}
					}
					Methods.resetInf();
				} else
				{
					Methods.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NoEffect", e.getPlayer(), null, null));
						}
					}
				}
			}
		}
	}

	// When a player uses a command make sure it does, what its supposed to
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent e) {
		String msg = null;
		if (e.getMessage().contains(" "))
		{
			String[] ss = e.getMessage().split(" ");
			msg = ss[0];
		} else
		{
			msg = e.getMessage();
		}
		if (Main.inGame.contains(e.getPlayer().getName()) || Main.inLobby.contains(e.getPlayer().getName()))

			if (!e.getPlayer().isOp())
			{

				// If a player tries a command but is in infected, block all
				// that aren't /inf
				if (plugin.getConfig().getStringList("Blocked Commands").contains(msg.toLowerCase()))
				{
					e.getPlayer().sendMessage(Methods.sendMessage("Error_CantUseCommand", null, null, null));
					e.setCancelled(true);
				} else if (!(plugin.getConfig().getStringList("Allowed Commands").contains(msg.toLowerCase()) || e.getMessage().toLowerCase().contains("inf")))
				{
					e.getPlayer().sendMessage(Methods.sendMessage("Error_CantUseCommand", null, null, null));
					e.setCancelled(true);
				}
			}
	}

	// When a Living Entitie targets another block it if its in a game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityAtkPlayerLivingEntity(EntityTargetLivingEntityEvent e) {
		if (e.getTarget() instanceof Player)
		{
			Player player = (Player) e.getTarget();
			if (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName()))
				e.setCancelled(true);
		}
	}

	// When a Entity targets another, block it if it's in a game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityAtk(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player)
		{
			Player player = (Player) e.getTarget();
			if (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName()))
				e.setCancelled(true);
		}
	}

	// If theres no other plugin that interfers with Food level, do "stuff"
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerHunger(FoodLevelChangeEvent e) {
		if (!e.isCancelled())
		{
			Player player = (Player) e.getEntity();
			if (plugin.getConfig().getBoolean("Disable Hunger"))
				if (Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName()))
					e.setCancelled(true);
		}
	}

	// Block enchantment tables if they're just for show
	@EventHandler(priority = EventPriority.LOW)
	public void PlayerTryEnchant(PrepareItemEnchantEvent e) {
		Player player = e.getEnchanter();
		if ((Main.inGame.contains(player.getName()) || Main.inLobby.contains(player.getName())) && plugin.getConfig().getBoolean("Disable Enchanting"))
			e.setCancelled(true);
	}

	// If an entity shoots a bow do "Stuff"
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShootBow(EntityShootBowEvent e) {
		if (!e.isCancelled() && !Infected.booleanIsStarted())
			if (e.getEntity() instanceof Player)
			{
				Player player = (Player) e.getEntity();
				if (Main.inGame.contains(player.getName()))
				{
					e.getProjectile().remove();
					e.setCancelled(true);
					player.updateInventory();
				} else
				{
					e.setCancelled(false);
				}
			}
	}

	// When a player moves trigger effects
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(final PlayerMoveEvent e) {
		if (plugin.getConfig().getBoolean("Use Zombie Movement Effects") && Infected.isPlayerZombie(e.getPlayer()))
		{
			if (!Bukkit.getScheduler().isCurrentlyRunning(effect))
			{
				if (!effectb)
				{
					effectb = true;
					effect = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
					{
						@Override
						public void run() {
							e.getPlayer().getWorld().playEffect(e.getPlayer().getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
							effectb = false;
						}
					}, 200L);
				}
			}
		}
	}

	// Player is Damaged, User is Damager
	// When entity is damaged
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent e) {
		boolean Started = plugin.Booleans.get("Started");
		boolean BeforeGame = plugin.Booleans.get("BeforeGame");
		boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
		if (e.getEntity() instanceof Player)
		{
			Player p = (Player) e.getEntity();
			if (plugin.inLobby.contains(p.getName()))
				e.setCancelled(true);

			else if (plugin.inGame.contains(p.getName()))
				if (!(p.getLastDamageCause() instanceof Player))
					if (plugin.inGame.contains(p.getName()) && Started == true)
						if (e.getCause() == DamageCause.PROJECTILE)
						{
							EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent) e;
							if (ee.getDamager() instanceof Arrow)
							{
								if (e.getEntity() instanceof Player)
								{
									playeruser = (Player) e.getEntity();
									Arrow arrow = (Arrow) ee.getDamager();
									useruser = (Player) arrow.getShooter();
									Main.Lasthit.put(playeruser.getName(), useruser.getName());
								}
							}
						}

			if (p.getHealth() - e.getDamage() <= 0)
			{
				if (plugin.inGame.contains(p.getName()) && BeforeGame == true)
				{
					e.setCancelled(true);
				} else if (plugin.inGame.contains(p.getName()) && BeforeFirstInf == true)
				{
					p.sendMessage(plugin.I + "You almost died before the game even started!");
					p.setHealth(20);
					p.setFoodLevel(20);
					plugin.KillStreaks.put(p.getName(), 0);
					p.setFallDistance(0F);
					p.setFoodLevel(20);
					Methods.respawn(p);
					p.setFallDistance(0F);
					e.setCancelled(true);
				} else if (!(e.getCause() == DamageCause.ENTITY_ATTACK) && plugin.inGame.contains(p.getName()) && Started == true)
				{
					e.setDamage(0);
					Methods.equipZombies(p);
					if (Main.Lasthit.containsKey(p.getName()))
					{
						Player human = Bukkit.getPlayer(Main.Lasthit.get(p.getName()));
						Player Killed = p;
						Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(
								human, Killed, Infected.playerGetGroup(Killed),
								Infected.isPlayerHuman(Killed) ? true : false));
						Methods.stats(human, 1, 0);
						Methods.rewardPoints(human, "Kill");
						String kill = Methods.getKillType(Infected.playerGetGroup(human) + "s", human.getName(), Killed.getName());
						for (Player playing : Bukkit.getServer().getOnlinePlayers())
							if (plugin.inGame.contains(playing.getName()))
							{
								playing.sendMessage(kill);
							}
						if (!plugin.KillStreaks.containsKey(human.getName()))
							plugin.KillStreaks.put(human.getName(), 1);
						else
							plugin.KillStreaks.put(human.getName(), plugin.KillStreaks.get(human.getName()) + 1);
						Files.getPlayers().set("Players." + human.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(human.getName()));
						if (plugin.KillStreaks.get(human.getName()) > 2)
							for (Player playing : Bukkit.getServer().getOnlinePlayers())
								if (plugin.inGame.contains(playing.getName()))
								{
									playing.sendMessage(plugin.I + ChatColor.GREEN + human.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + plugin.KillStreaks.get(human.getName()));
								}
						if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(plugin.KillStreaks.get(human.getName())))))
						{
							String command = null;
							command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + plugin.KillStreaks.get(human.getName()))).replaceAll("<player>", human.getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
						}
						Methods.stats(Killed, 0, 1);
						if (plugin.KillStreaks.containsKey(Killed.getName()))
						{
							if (plugin.KillStreaks.get(Killed.getName()) > Files.getPlayers().getInt("Players." + Killed.getName().toLowerCase() + ".KillStreak"))
							{
								Files.getPlayers().set("Players." + Killed.getName().toLowerCase() + ".KillStreak", plugin.KillStreaks.get(Killed.getName()));
								Files.savePlayers();
							}
							plugin.KillStreaks.put(Killed.getName(), 0);
						}
						Main.Lasthit.remove(Killed.getName());
					} else
					{
						if (Main.humans.contains(p))
						{
							for (Player playing : Bukkit.getServer().getOnlinePlayers())
							{
								if (Infected.isPlayerInGame(playing))
								{
									playing.sendMessage(Methods.sendMessage("Game_GotInfected", p, null, null));
								}
							}
						}
					}
					p.setHealth(20);
					p.setFallDistance(0F);
					p.setFoodLevel(20);
					Methods.respawn(p);
					p.setFallDistance(0F);
					e.setDamage(0);
					Main.humans.remove(p.getName());
					Main.Lasthit.remove(p.getName());
					if (plugin.humans.size() == 0)
					{
						Methods.endGame(false);
					} else
					{
						Methods.equipZombies(p);
						Methods.zombifyPlayer(p);
					}

					if (plugin.Winners.contains(p.getName()))
						plugin.Winners.remove(p.getName());
					if (plugin.humans.contains(p.getName()))
						plugin.humans.remove(p.getName());
					if (!plugin.zombies.contains(p.getName()))
						plugin.zombies.add(p.getName());
				}
			}
		}
	}

	// Player is Damaged, User is Damager
	// When entity is damaged by another entity(As some times the first one
	// doesn't work...
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if (plugin.getConfig().getBoolean("Debug"))
		{
			System.out.println("PVPhumans: " + plugin.humans.toString());
			System.out.println("PVPzombies: " + plugin.zombies.toString());
			System.out.println("PVPInGame " + plugin.inGame.toString());
		}
		boolean Started = plugin.Booleans.get("Started");
		boolean BeforeGame = plugin.Booleans.get("BeforeGame");
		boolean BeforeFirstInf = plugin.Booleans.get("BeforeFirstInf");
		if (e.getEntity() instanceof Player)
		{
			if (e.getDamager() instanceof Arrow)
			{
				playeruser = (Player) e.getEntity();
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player)
				{
					useruser = (Player) arrow.getShooter();
				}
			}
			if (e.getDamager() instanceof Snowball)
			{
				playeruser = (Player) e.getEntity();
				Snowball ball = (Snowball) e.getDamager();
				if (ball.getShooter() instanceof Player)
				{
					useruser = (Player) ball.getShooter();
				}
			}
			if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
			{
				playeruser = (Player) e.getEntity();
				useruser = (Player) e.getDamager();
			}
			if (e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow))
				if (plugin.inGame.contains(playeruser.getName()) || plugin.inLobby.contains(playeruser.getName()))
				{
					if (!plugin.inGame.contains(useruser.getName()))
						e.setCancelled(true);
					if (BeforeGame)
						e.setCancelled(true);
					else if (BeforeFirstInf)
						e.setCancelled(true);
					else if (Started)
					{
						if (plugin.humans.contains(playeruser.getName()) && plugin.humans.contains(useruser.getName()))
							e.setCancelled(true);
						if (plugin.zombies.contains(playeruser.getName()) && plugin.zombies.contains(useruser.getName()))
							e.setCancelled(true);
						// /////////////////////////////////////////////////////////////////////////////////
						// HUMAN KILLING ZOMBIE
						// If the attacker is a human, and the attacky is a
						// zombie
						if (plugin.humans.contains(useruser.getName()) && plugin.zombies.contains(playeruser.getName()))
						{
							Player human = useruser;
							Player zombie = playeruser;

							Main.Lasthit.put(zombie.getName(), human.getName());

							// If the damage done will kill the zombie
							if (zombie.getHealth() - e.getDamage() <= 0)
							{
								Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(
										human, zombie,
										Infected.playerGetGroup(zombie), false));
								e.setDamage(0);
								AttackingManager.HumanAttackZombie(human, zombie);
							}
							// ////////////////////////////////////////////////////////////////////////
							// ZOMBIE KILLING HUMAN
						} else if (plugin.humans.contains(playeruser.getName()) && plugin.zombies.contains(useruser.getName()))
						{
							// If the attacker is a zombie and the attacky is a
							// human
							Player newzombie = playeruser;
							Player zombie = useruser;

							Main.Lasthit.put(newzombie.getName(), zombie.getName());
							if (newzombie.getHealth() - e.getDamage() <= 0)
							{

								Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(
										zombie, newzombie,
										Infected.playerGetGroup(newzombie),
										true));
								e.setDamage(0);
								AttackingManager.ZombieAttackHuman(zombie, newzombie);
							}
						} else
						{
							// If the attackers arn't from different groups
							// act like nothing happened
							e.setDamage(0);
							e.setCancelled(true);
						}
					}
				}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowPotion(PlayerInteractEvent e) {
		if (!e.isCancelled())
			if ((Infected.isPlayerInGame(e.getPlayer()) || Infected.isPlayerInLobby(e.getPlayer())) && !Infected.booleanIsStarted())
			{
				if (e.getPlayer().getItemInHand().getTypeId() == 373)
				{
					e.setUseItemInHand(Result.DENY);
					e.setCancelled(true);
				}
			}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		if (Main.inGame.contains(player.getName()))
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run() {
					player.sendMessage(Main.I + "Apperently i missed a way to die...");
					player.sendMessage(Main.I + "Inform the author on how you died!");
					player.sendMessage(Main.I + "http://www.dev.Bukkit.org/Server_Mods/Infected-Core");
					player.performCommand("Infected Leave");
					player.performCommand("Infected Join");
				}

			}, 10L);
	}

}