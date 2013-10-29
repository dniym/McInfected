package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.GameMechanics.DeathType;
import me.xxsniperzzxx_sd.infected.GameMechanics.Deaths;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Grenades.Grenade;
import me.xxsniperzzxx_sd.infected.Handlers.Grenades.GrenadeManager;
import me.xxsniperzzxx_sd.infected.Tools.Files;

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
import org.bukkit.potion.PotionEffectType;

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
		{
			if (Files.getGrenades().getBoolean("Use"))
				if (event.getPlayer().hasPermission("Infected.Grenades"))
					if (item.contains(event.getItem().getUniqueId().toString()))
					{
						if (event.getPlayer().getItemInHand().getType() == Material.AIR)
						{
							event.getItem().setVelocity(event.getPlayer().getEyeLocation().getDirection());
						}
						event.setCancelled(true);
					}
		}
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerThrowEvent(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && GrenadeManager.isGrenade(player.getItemInHand().getTypeId()))
		{
			final Lobby Lobby = Main.Lobby;
			if (Lobby.getGameState() == GameState.Started && Lobby.isInGame(player))
			{
				Grenade grenade = GrenadeManager.getGrenade(player.getItemInHand().getTypeId());
				
				if (player.hasPermission("Infected.Grenades") || player.hasPermission("Infected.Grenades."+grenade.getName()))
				{
			
					final Item grenadeItem = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(
							Material.getMaterial(grenade.getId())));
					
					grenadeItem.setVelocity(event.getPlayer().getEyeLocation().getDirection());
					
					player.updateInventory();
					GrenadeManager.addThrownGrenade(grenadeItem.getUniqueId());
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						@Override
						public void run() {
							grenadeItem.getWorld().playEffect(grenadeItem.getLocation(), Effect.SMOKE, 5);
							for(Player u: Lobby.getInGame()))
							{
								{
									
									ppl.playEffect(EntityEffect.HURT);
									if (ppl.getHealth() - Files.getGrenades().getInt(Integer.valueOf(ItemId) + ".Damage") <= 0)
									{
										if(ppl == player)
											Deaths.playerDies(DeathType.Other, player, ppl);
										else	
											Deaths.playerDies(DeathType.Grenade, player, ppl);
									} else
									{
										ppl.setHealth(ppl.getHealth() - Files.getGrenades().getInt(Integer.valueOf(ItemId) + ".Damage"));
									}
								}
							}
							grenadeItem.remove();
							Location loc = grenadeItem.getLocation();
							player.getWorld().createExplosion(loc, 0.0F, false);
							item.remove(grenade.getUniqueId().toString());
						}
					}, delay);
				}
			}
			event.setCancelled(true);
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
		}
	}
	
}