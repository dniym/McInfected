
package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.GameMechanics.DeathType;
import me.xxsniperzzxx_sd.infected.GameMechanics.Deaths;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Grenades.Grenade;
import me.xxsniperzzxx_sd.infected.Handlers.Grenades.GrenadeManager;
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


public class GrenadeListener implements Listener {

	public Main Main = new Main();
	public ArrayList<String> item = new ArrayList<String>();

	public Main plugin;

	public GrenadeListener(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerReThrowEvent(PlayerPickupItemEvent event) {
		if (!event.isCancelled())
			if (GrenadeManager.isThrownGrenade(event.getItem().getUniqueId()))
				if (event.getPlayer().hasPermission("Infected.Grenades"))
					if (event.getPlayer().getItemInHand().getType() == Material.AIR)
					{
						event.getItem().setVelocity(event.getPlayer().getEyeLocation().getDirection());
						event.setCancelled(true);
					}
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowEvent(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && GrenadeManager.isGrenade(p.getItemInHand().getTypeId()))
		{
			final Lobby Lobby = Main.Lobby;
			if (Lobby.getGameState() == GameState.Started && Lobby.isInGame(p))
			{
				final Grenade grenade = GrenadeManager.getGrenade(p.getItemInHand().getTypeId());

				if (p.hasPermission("Infected.Grenades") || p.hasPermission("Infected.Grenades." + grenade.getName()))
				{

					final Item grenadeItem = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(
							Material.getMaterial(grenade.getId())));

					grenadeItem.setVelocity(event.getPlayer().getEyeLocation().getDirection());

					p.updateInventory();
					GrenadeManager.addThrownGrenade(grenadeItem.getUniqueId());

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{

						@Override
						public void run() {
							grenadeItem.getWorld().playEffect(grenadeItem.getLocation(), Effect.SMOKE, 5);
							for (Player u : Lobby.getInGame())
							{
								if (grenadeItem.getLocation().distance(u.getLocation()) <= grenade.getRange())
								{

									u.playEffect(EntityEffect.HURT);
									if (u.getHealth() - grenade.getDamage() <= 0)
									{
										if (u == p)
											Deaths.playerDies(DeathType.Other, p, u);
										else
											Deaths.playerDies(DeathType.Grenade, p, u);
									} else
									{
										u.damage(grenade.getDamage(), p);
									}
								}
							}
							Location loc = grenadeItem.getLocation();
							p.getWorld().createExplosion(loc, 0.0F, false);

							GrenadeManager.delThrownGrenade(grenadeItem.getUniqueId());
							grenadeItem.remove();
						}
					}, grenade.getDelay() * 20);
				}
			}
			event.setCancelled(true);
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
		}
	}

}