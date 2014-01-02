
package me.sniperzciinema.infected.Listeners;

import java.util.ArrayList;

import me.sniperzciinema.infected.Main;
import me.sniperzciinema.infected.Enums.DeathType;
import me.sniperzciinema.infected.GameMechanics.Deaths;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Grenades.Grenade;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


/**
 * The Listener for Infected's grenades
 * 
 */
public class GrenadeListener implements Listener {

	public Main Main = new Main();
	public ArrayList<String> item = new ArrayList<String>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerReThrowEvent(PlayerPickupItemEvent event) {
		if (!event.isCancelled())
			// If this item is a thrown grenade
			if (GrenadeManager.isThrownGrenade(event.getItem().getUniqueId()))
			{
				if (event.getPlayer().hasPermission("Infected.Grenades"))
				{
					// Make sure they have nothing in their hands
					if (event.getPlayer().getItemInHand().getType() == Material.AIR)
					{
						// Throw the item in the direction they're looking
						event.getItem().setVelocity(event.getPlayer().getEyeLocation().getDirection());
					}
				}
				event.setCancelled(true);
			}
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowEvent(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		// Make sure they clicked with the mouse, and that the item in their
		// hand is in fact a grenade
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && GrenadeManager.isGrenade(p.getItemInHand().getTypeId()))
		{
			if (Lobby.getGameState() == GameState.Started && Lobby.isInGame(p))
			{
				final Grenade grenade = GrenadeManager.getGrenade(p.getItemInHand().getTypeId());

				if (p.hasPermission("Infected.Grenades"))
				{

					// Create a new Item and throw it in the direction the
					// player is looking
					final Item grenadeItem = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(
							Material.getMaterial(grenade.getId())));
					grenadeItem.setVelocity(event.getPlayer().getEyeLocation().getDirection());

					if (p.getItemInHand().getAmount() != 1)
						p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
					else
						p.getInventory().remove(p.getItemInHand());

					p.updateInventory();
					GrenadeManager.addThrownGrenade(grenadeItem.getUniqueId());

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
					{

						@Override
						public void run() {
							if (Lobby.getGameState() == GameState.Started)
							{
								// Play an effect then get every player in it's
								// range and do the damage
								grenadeItem.getWorld().playEffect(grenadeItem.getLocation(), Effect.SMOKE, 5);
								for (Player u : Lobby.getInGame())
								{
									if (grenadeItem.getLocation().distance(u.getLocation()) <= grenade.getRange())
									{
										if (InfPlayerManager.getInfPlayer(p).getTeam() != InfPlayerManager.getInfPlayer(u).getTeam() || (grenade.isDamageThrower() && p == u))
										{
											u.playEffect(EntityEffect.HURT);
											if (u.getHealth() - grenade.getDamage() <= 0D)
											{
												if (u == p)
													Deaths.playerDies(DeathType.Other, p, u);
												else
													Deaths.playerDies(DeathType.Grenade, p, u);
											} else
											{
												u.damage(grenade.getDamage());
												for (PotionEffect pe : grenade.getEffects())
													u.addPotionEffect(pe);
											}
										}
									}
								}
							}
							// Create an explosion effect and remove the grenade
							Location loc = grenadeItem.getLocation();
							p.getWorld().createExplosion(loc, 0.0F, false);

							GrenadeManager.delThrownGrenade(grenadeItem.getUniqueId());
							grenadeItem.remove();
						}
					}, grenade.getDelay() * 20);
				}

			}// Prevent anything else they may have done
			event.setCancelled(true);
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
			p.updateInventory();
		}
	}

}