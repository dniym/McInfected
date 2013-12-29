
package me.sniperzciinema.infected.Listeners;

import me.sniperzciinema.infected.Game;
import me.sniperzciinema.infected.Main;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * The Miscellaneous Listeners for Infected
 * 
 */
public class PlayerListener implements Listener {

	// Check for updates when a player joins, making sure they are OP
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Main.update && player.hasPermission("Infected.Admin"))
		{
			player.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "An update is available: " + Main.updateName);
			player.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "Download it at: http://dev.bukkit.org/server-mods/infected-core/");
		}
	}

	// If a player attempts to drop an item in game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (Lobby.isInGame(e.getPlayer()))
			if (!Lobby.getActiveArena().getSettings().canDropBlocks())
				e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		if (!e.isCancelled())
		{
			// Remove any signs if it is one
			if (Files.getSigns().getStringList("Info Signs").contains(LocationHandler.getLocationToString(e.getBlock().getLocation())))
			{
				Files.getSigns().getStringList("Info Signs").remove(LocationHandler.getLocationToString(e.getBlock().getLocation()));
				Files.saveSigns();
			}

			if (Files.getSigns().getStringList("Shop Signs").contains(LocationHandler.getLocationToString(e.getBlock().getLocation())))
			{
				Files.getSigns().getStringList("Shop Signs").remove(LocationHandler.getLocationToString(e.getBlock().getLocation()));
				Files.saveSigns();
			}
			// When players break blocks, before we do anything, are they in the
			// started game?
			else if (Lobby.isInGame(e.getPlayer()) && Lobby.getGameState() == GameState.Started)
			{
				e.getBlock().getDrops().clear();
				// Does the config say they can break it?
				if (Lobby.getActiveArena().getSettings().canBreakBlock(InfPlayerManager.getInfPlayer(e.getPlayer()).getTeam(), e.getBlock().getTypeId()))
				{
					Location loc = e.getBlock().getLocation();
					// Lets make sure this block wasn't a placed one, if so
					// we'll just remove it
					if (!Lobby.getActiveArena().getBlocks().containsKey(loc))
						Lobby.getActiveArena().setBlock(loc, e.getBlock().getType());
					else
						Lobby.getActiveArena().removeBlock(loc);
				} else
					e.setCancelled(true);
			}
		}

	}

	// If the player has toggled on InfectedChat, lets make it so they only have
	// to type
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (InfPlayerManager.getInfPlayer(e.getPlayer()).isInfChatting())
		{
			String msg = e.getMessage();
			e.getPlayer().performCommand("Inf Chat " + msg);
			e.setCancelled(true);
		}
	}

	// When a player interacts with an object well the game is started
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!e.isCancelled())
			// If they are playing and the game has started
			if (Lobby.isInGame(e.getPlayer()) && Lobby.getGameState() == GameState.Started)
			{
				if (Lobby.getActiveArena().getSettings().canInteract())
				{

					if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						Block b = e.getClickedBlock();
						// Make sure the chest isn't already saved, if it isn't
						// save
						// it
						if (e.getClickedBlock().getType() == Material.CHEST)
						{
							Chest chest = (Chest) b.getState();
							if (Lobby.getActiveArena().getChests().containsKey(chest.getLocation()))
								Lobby.getActiveArena().setChest(chest.getLocation(), chest.getBlockInventory());
						}
					}
				} else
				{
					e.setUseInteractedBlock(Result.DENY);
					e.setUseItemInHand(Result.DENY);
					e.setCancelled(true);

				}
			}
	}

	// If a player is attempting to place a block
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBlockPlace(BlockPlaceEvent e) {

		if (Lobby.isInGame(e.getPlayer()))
		{
			if (Lobby.getGameState() != GameState.Started)
				e.setCancelled(true);
			else
				Lobby.getActiveArena().setBlock(e.getBlock().getLocation(), Material.AIR);
		}
	}

	// If the player gets kicked out of the server, do we need to remove them?
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKick(final PlayerKickEvent e) {
		if (Lobby.isInGame(e.getPlayer()))
			Game.leaveGame(e.getPlayer());
	}

	// If the player quits on the server, do we need to remove them?
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(final PlayerQuitEvent e) {
		if (Lobby.isInGame(e.getPlayer()))
			Game.leaveGame(e.getPlayer());
	}

	// If a player uses a command and they aren't op, the cmds not on the
	// allowed list, and it doesn't contain "inf". We'll block the cmd
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent e) {
		if (Lobby.isInGame(e.getPlayer()) && !e.getPlayer().isOp())
		{
			String msg = null;
			if (e.getMessage().contains(" "))
			{
				String[] ss = e.getMessage().split(" ");
				msg = ss[0];
			} else
			{
				msg = e.getMessage();
			}
			if (!Settings.AllowedCommands().contains(msg.toLowerCase()) && !e.getMessage().toLowerCase().contains("inf"))
			{
				e.getPlayer().sendMessage(Msgs.Error_Misc_Use_Command.getString());
				e.setCancelled(true);
			}
		}

	}

	// Stop "Living Entities" from targetting the players in Infectedhj
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityAtkPlayerLivingEntity(EntityTargetLivingEntityEvent e) {
		if (e.getTarget() instanceof Player)
		{
			Player p = (Player) e.getTarget();
			if (Lobby.isInGame(p))
				e.setCancelled(true);
		}
	}

	// When a Entity targets another, block it if it's in a game
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityAtk(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player)
		{
			Player p = (Player) e.getTarget();
			if (Lobby.isInGame(p))
				e.setCancelled(true);
		}
	}

	// Should we allow for food levels to drop
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerHunger(FoodLevelChangeEvent e) {
		if (!e.isCancelled())
		{
			Player p = (Player) e.getEntity();
			if (Lobby.isInGame(p) && Lobby.getActiveArena().getSettings().canLooseHunger())
				e.setCancelled(true);
		}
	}

	// Block enchantment tables as levels are used as a timer
	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerTryEnchant(PrepareItemEnchantEvent e) {
		Player p = e.getEnchanter();
		if (Lobby.isInGame(p) && !Lobby.getActiveArena().getSettings().canEnchant())
			e.setCancelled(true);
	}

	// If a player shoots a bow, before the game has started, lets return the
	// arrow and cancel the shot
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShootBow(EntityShootBowEvent e) {
		if (!e.isCancelled() && Lobby.getGameState() != GameState.Started)
			if (e.getEntity() instanceof Player)
			{
				Player player = (Player) e.getEntity();
				if (Lobby.isInGame(player))
				{
					e.getProjectile().remove();
					e.setCancelled(true);
					player.updateInventory();
				}
			}
	}

	// Prevent throwing potions well in the lobby of Infected, as it just gets
	// annoying
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowPotion(PlayerInteractEvent e) {
		if (!e.isCancelled())
			if (Lobby.isInGame(e.getPlayer()) && Lobby.getGameState() != GameState.Started)
			{
				if (e.getPlayer().getItemInHand().getType() == Material.POTION)
				{
					e.setUseItemInHand(Result.DENY);
					e.setCancelled(true);
				}
			}
	}

}