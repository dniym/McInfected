package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Methods;
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
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && Main.inGame.contains(player.getName()) && Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())))
		{
			if (Infected.booleanIsStarted() && Files.getGrenades().getBoolean("Use"))
			{
				if (!Main.inLobby.contains(player.getName()) && player.hasPermission("Infected.Grenades"))
				{
					final int delay = Methods.grenadeGetDelay(player.getItemInHand().getTypeId());
					final String ItemId = String.valueOf(player.getItemInHand().getTypeId());
					final Item grenade = event.getPlayer().getWorld().dropItem(player.getEyeLocation(), new ItemStack(
							Material.getMaterial(player.getItemInHand().getTypeId())));
					grenade.setVelocity(event.getPlayer().getEyeLocation().getDirection());
					if (Methods.grenadeTakeAfter(player.getItemInHand().getTypeId()))
					{
						if (player.getInventory().getItemInHand().getAmount() >= 2)
						{
							player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
						} else
						{
							player.getInventory().remove(player.getItemInHand());
						}
					}
					player.updateInventory();
					item.add(grenade.getUniqueId().toString());
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						@Override
						public void run() {
							grenade.getWorld().playEffect(grenade.getLocation(), Effect.SMOKE, 5);
							for (Player ppl : Bukkit.getServer().getOnlinePlayers())
							{
								if (Infected.isPlayerInGame(ppl) && ((!(Infected.playerGetGroup(ppl) == Infected.playerGetGroup(player))) || (Main.grenades.getBoolean("Damage Self") && ppl == player)) && ppl.getLocation().distance(grenade.getLocation()) < Methods.grenadeGetRange(Integer.valueOf(ItemId)))
								{
									Methods.grenadeAddPotion(ppl, Integer.valueOf(ItemId));
									ppl.playEffect(EntityEffect.HURT);
									if (ppl.getHealth() - Methods.grenadeGetDamage(Integer.valueOf(ItemId)) <= 0)
									{
										Methods.grenadeKill(player, ppl);
									} else
									{
										ppl.setHealth(ppl.getHealth() - Methods.grenadeGetDamage(Integer.valueOf(ItemId)));
									}
								}
							}
							grenade.remove();
							Location loc = grenade.getLocation();
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