package me.xxsniperzzxx_sd.infected.Listeners;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Enums.DeathType;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.GameMechanics.Deaths;
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
		if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && Main.inGame.contains(player.getName()) && Files.getGrenades().contains(String.valueOf(player.getItemInHand().getTypeId())))
		{
			if (Infected.getGameState() == GameState.STARTED && Files.getGrenades().getBoolean("Use"))
			{
				if (!Main.inLobby.contains(player.getName()) && (player.hasPermission("Infected.Grenades") || player.hasPermission("Infected.Grenades."+Files.getGrenades().getString(event.getPlayer().getItemInHand().getTypeId() + ".Name"))))
				{
					final String ItemId = String.valueOf(player.getItemInHand().getTypeId());
					final int delay = Files.getGrenades().getInt(Integer.valueOf(ItemId) + ".Delay");
					final Item grenade = event.getPlayer().getWorld().dropItem(player.getEyeLocation(), new ItemStack(
							Material.getMaterial(player.getItemInHand().getTypeId())));
					grenade.setVelocity(event.getPlayer().getEyeLocation().getDirection());
					if (Files.getGrenades().getBoolean(Integer.valueOf(ItemId) + ".Take After Thrown"))
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
								if (Infected.isPlayerInGame(ppl) && ((!(Infected.playerGetGroup(ppl) == Infected.playerGetGroup(player))) || (Files.getGrenades().getBoolean(Integer.valueOf(ItemId) + ".Damage Self") && ppl == player)) && ppl.getLocation().distance(grenade.getLocation()) < Files.getGrenades().getInt(Integer.valueOf(ItemId) + ".Range"))
								{
									grenadeAddPotion(ppl, Integer.valueOf(ItemId));
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


	@SuppressWarnings("deprecation")
	public static void grenadeAddPotion(Player player, Integer Itemid) {
		Integer id = 0;
		Integer time = 0;
		Integer power = 0;
		int max = Files.getGrenades().getStringList(Itemid + ".Effects").size();
		for (int x = 0; x < max; x = x + 1)
		{
			String path = Files.getGrenades().getStringList(Itemid + ".Effects").get(x);
			String[] strings = path.split(":");
			id = Integer.valueOf(strings[0]);
			time = Integer.valueOf(strings[1]) * 20;
			power = Integer.valueOf(strings[2]);
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.getById(id), time, power));
		}

	}
	
}