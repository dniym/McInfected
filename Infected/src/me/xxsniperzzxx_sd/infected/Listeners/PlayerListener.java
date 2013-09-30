
package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.Reset;
import me.xxsniperzzxx_sd.infected.Main.GameState;
import me.xxsniperzzxx_sd.infected.Methods;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
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
		if (plugin.getConfig().getBoolean("Force Join.Enable"))
		{
			player.performCommand("Infected Join");

			if (plugin.getConfig().getBoolean("Force Join.Hide Join Messages"))
				event.setJoinMessage("");
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
			if (!(Infected.getGameState() == GameState.STARTED) && (!e.getInventory().getTitle().contains("Class") && !e.getInventory().getTitle().contains("Vote")))
			{
				Player player = (Player) e.getViewers().get(0);
				player.sendMessage(Methods.sendMessage("Error_CantEditInventoryYet", null, null, null));
				e.setCancelled(true);
			}
	}

	// Check if the player is in game and the situation around them breaking a
	// block
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		if (!e.isCancelled())
		{
			if (Files.getSigns().getStringList("Info Signs").contains(Methods.getLocationToString(e.getBlock().getLocation()))){
				Files.getSigns().getStringList("Info Signs").remove(Methods.getLocationToString(e.getBlock().getLocation()));
				Files.saveSigns();
			}

			if (Files.getSigns().getStringList("Shop Signs").contains(Methods.getLocationToString(e.getBlock().getLocation()))){
				Files.getSigns().getStringList("Shop Signs").remove(Methods.getLocationToString(e.getBlock().getLocation()));
				Files.saveSigns();
			}

			if (Main.inLobby.contains(e.getPlayer().getName()))
				e.setCancelled(true);
			
			else if (Main.inGame.contains(e.getPlayer().getName()))
			{
				e.getBlock().getDrops().clear();
				if (Files.getArenas().getIntegerList("Arenas." + Main.playingin + ".Allow Breaking Of.Global").contains(e.getBlock().getTypeId()) || Files.getArenas().getIntegerList("Arenas." + Main.playingin + ".Allow Breaking Of." + Infected.playerGetGroup(e.getPlayer())).contains(e.getBlock().getTypeId()))
				{
					if (!Main.Blocks.containsKey(e.getBlock().getLocation()))
					{
						Location loc = e.getBlock().getLocation();
						Main.Blocks.put(loc, e.getBlock().getType());
						e.getBlock().getDrops().clear();
					}
				} else
				{
					if (Main.Blocks.containsKey(e.getBlock().getLocation()))
					{
						Location loc = e.getBlock().getLocation();
						Main.Blocks.remove(loc);
						e.getBlock().getDrops().clear();
					}else
						e.setCancelled(true);
				}
			}
		}
	}
	
	  @EventHandler(priority = EventPriority.HIGHEST)
	    public void onPlayerChat(AsyncPlayerChatEvent e) {
	    	if(plugin.infectedChat.contains(e.getPlayer().getName())){
	    		String msg = e.getMessage();
	    		e.getPlayer().performCommand("Inf Chat " + msg);
	    		e.setCancelled(true);
	    	}
	    }

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.isCancelled())
			if (Infected.getGameState() == GameState.STARTED && Infected.isPlayerInGame(e.getPlayer()))
			{
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					Block b = e.getClickedBlock();
					if (e.getClickedBlock().getType() == Material.CHEST)
					{
						Chest chest = (Chest) b.getState();
						ItemStack[] z = chest.getBlockInventory().getContents();
						if (!Main.Chests.containsKey(b.getLocation()))
							Main.Chests.put(b.getLocation(), z);
					}
				}
			}
	}

	// Check to make sure they arn't trying to place a block in game
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerBlockPlace(BlockPlaceEvent e) {
		
		if (Main.inLobby.contains(e.getPlayer().getName()))
			e.setCancelled(true);
		
		else if (Main.inGame.contains(e.getPlayer().getName()))
		{
			Location loc = e.getBlock().getLocation();
			Main.Blocks.put(loc, Material.AIR);
		}
	}

	// See if a player got kicked and if it effected the game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerGetKicked(final PlayerKickEvent e) {
		plugin.Creating.remove(e.getPlayer().getName());

		// If a player logs out well playing, make sure it doesn't effect the
		// game, end the game if it does
		if (plugin.inGame.contains(e.getPlayer().getName()))
		{
			plugin.inGame.remove(e.getPlayer().getName());

			// Leave well everyones in the lobby
			if (Infected.getGameState() == GameState.INLOBBY)
			{
				if (plugin.getConfig().getBoolean("Debug"))
					System.out.println("Leave: Leaving, wellin lobby, no timers active");

				e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
				Reset.resetp(e.getPlayer());
				for (Player players : Bukkit.getOnlinePlayers())
				{
					if (Infected.isPlayerInGame(players))
						players.sendMessage(Methods.sendMessage("Leave_InLobby", e.getPlayer(), null, null));
				}
			}

			// Voting has started, less then 2 people left
			else if (Infected.getGameState() == GameState.VOTING)
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				} else
				{
					Reset.resetp(e.getPlayer());
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
			else if (Infected.getGameState() == GameState.BEFOREINFECTED)
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				} else
				{
					Reset.resetp(e.getPlayer());
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
			else if (Infected.getGameState() == GameState.STARTED)
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				}

				// If Not Enough zombies remain
				else if (Main.zombies.size() == 1 && Infected.isPlayerZombie(e.getPlayer()))
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Reset.resetp(e.getPlayer());
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
					zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, BlockFace.UP);
					if (plugin.getConfig().getBoolean("Zombie Abilties") == true)
					{
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 2000, 2), true);
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.JUMP, 2000, 1), true);
					}
					Methods.zombifyPlayer(zombie);
					zombie.setHealth(20);
					Equip.equipZombies(zombie);

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
								online.playEffect(online.getLocation(), Effect.SMOKE, BlockFace.UP);
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				} else
				{
					Reset.resetp(e.getPlayer());
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
		plugin.Creating.remove(e.getPlayer().getName());

		// If a player logs out well playing, make sure it doesn't effect the
		// game, end the game if it does
		if (plugin.inGame.contains(e.getPlayer().getName()))
		{
			plugin.inGame.remove(e.getPlayer().getName());

			// Leave well everyones in the lobby
			if (Infected.getGameState() == GameState.INLOBBY)
			{
				if (plugin.getConfig().getBoolean("Debug"))
					System.out.println("Leave: Leaving, wellin lobby, no timers active");

				e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
				Reset.resetp(e.getPlayer());
				for (Player players : Bukkit.getOnlinePlayers())
				{
					if (Infected.isPlayerInGame(players))
						players.sendMessage(Methods.sendMessage("Leave_InLobby", e.getPlayer(), null, null));
				}
			}

			// Voting has started, less then 2 people left
			else if (Infected.getGameState() == GameState.VOTING)
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				} else
				{
					Reset.resetp(e.getPlayer());
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
			else if (Infected.getGameState() == GameState.BEFOREINFECTED)
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				} else
				{
					Reset.resetp(e.getPlayer());
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
			else if (Infected.getGameState() == GameState.STARTED)
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				}

				// If Not Enough zombies remain
				else if (Main.zombies.size() == 1 && Infected.isPlayerZombie(e.getPlayer()))
				{
					if (plugin.getConfig().getBoolean("Debug"))
					{
						System.out.println("Leave: In Arena, Game Has Started (Not Enough Zombies)");
					}
					e.getPlayer().sendMessage(Methods.sendMessage("Leave_YouHaveLeft", null, null, null));
					Reset.resetp(e.getPlayer());
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
					zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, BlockFace.UP);
					if (plugin.getConfig().getBoolean("Zombie Abilties") == true)
					{
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 2000, 2), true);
						zombie.addPotionEffect(new PotionEffect(
								PotionEffectType.JUMP, 2000, 1), true);
					}
					Methods.zombifyPlayer(zombie);
					zombie.setHealth(20);
					Equip.equipZombies(zombie);

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
								online.playEffect(online.getLocation(), Effect.SMOKE, BlockFace.UP);
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
					Reset.resetp(e.getPlayer());
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(players))
						{
							players.sendMessage(Methods.sendMessage("Leave_NotEnoughPlayers", e.getPlayer(), null, null));
							Reset.tp2LobbyAfter(players);
						}
					}
					Reset.resetInf();
				} else
				{
					Reset.resetp(e.getPlayer());
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
		if (!e.isCancelled() && Infected.getGameState() != GameState.STARTED)
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

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowPotion(PlayerInteractEvent e) {
		if (!e.isCancelled())
			if ((Infected.isPlayerInGame(e.getPlayer()) || Infected.isPlayerInLobby(e.getPlayer())) && Infected.getGameState() != GameState.STARTED)
			{
				if (e.getPlayer().getItemInHand().getType() == Material.POTION)
				{
					e.setUseItemInHand(Result.DENY);
					e.setCancelled(true);
				}
			}
	}


}