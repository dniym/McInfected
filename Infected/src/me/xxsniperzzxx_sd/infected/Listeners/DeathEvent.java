
package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.GameMechanics.Abilities;
import me.xxsniperzzxx_sd.infected.GameMechanics.Deaths;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.Game;
import me.xxsniperzzxx_sd.infected.GameMechanics.Zombify;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;


public class DeathEvent implements Listener {

	public Main plugin;

	public DeathEvent(Main instance)
	{
		this.plugin = instance;
	}

	@SuppressWarnings("static-access")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		// If they're in the game
		if (Infected.isPlayerInGame(player))
		{

			for (ItemStack is : event.getDrops())
			{
				is.setType(Material.AIR);
			}
			event.setDeathMessage("");
			event.getDrops().clear();
			event.setKeepLevel(true);
			event.getEntity().setHealth(20);

			Player victim = player;
			Player killer = null;

			// Get the attacker
			if (event.getEntity().getKiller() instanceof Player)
				killer = (Player) event.getEntity().getKiller();

			else if (event.getEntity().getKiller() instanceof Arrow)
			{
				Arrow arrow = (Arrow) event.getEntity().getKiller();

				if (arrow.getShooter() instanceof Player)
					killer = (Player) arrow.getShooter();
			}

			else if (event.getEntity().getKiller() instanceof Snowball)
			{
				Snowball ball = (Snowball) event.getEntity().getKiller();

				if (ball.getShooter() instanceof Player)
					killer = (Player) ball.getShooter();
			}

			else if (event.getEntity().getKiller() instanceof Egg)
			{
				Egg ball = (Egg) event.getEntity().getKiller();

				if (ball.getShooter() instanceof Player)
					killer = (Player) ball.getShooter();
			}
			if ((victim != null) && (killer != null))
			{
				final Player k = killer;
				final Player v = victim;
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{

					@Override
					public void run() {
						Deaths.playerDies(k, v);
					}
				}, 10L);

			} else
			{

				if (plugin.humans.contains(victim))
				{
					for (Player playing : Bukkit.getServer().getOnlinePlayers())
					{
						if (Infected.isPlayerInGame(playing))
						{
							playing.sendMessage(Messages.sendMessage("Game_GotInfected", victim, null));
						}
					}
				}
				victim.setHealth(20);
				victim.setFallDistance(0F);
				victim.setFoodLevel(20);
				LocationHandler.respawn(victim);
				victim.setFallDistance(0F);

				plugin.humans.remove(victim.getName());
				plugin.Lasthit.remove(victim.getName());
				plugin.Winners.remove(victim.getName());
				if (Infected.listHumans().size() == 0)
				{
					Game.endGame(false);
				} else
				{
					Equip.equipZombies(victim);
					Zombify.zombifyPlayer(victim);
				}
			}
		}
	}

	// Player is Damaged, User is Damager
	// When entity is damaged
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player)
		{
			Player victim = (Player) e.getEntity();

			if (Infected.isPlayerInGame(victim))
			{
				if (e.getCause() != DamageCause.ENTITY_ATTACK && e.getCause() != DamageCause.PROJECTILE)
				{

					// If the attack happened before the game started
					if (Infected.getGameState() != GameState.STARTED && Infected.getGameState() != GameState.BEFOREINFECTED)
					{
						e.setDamage(0);
						e.setCancelled(true);
					}

				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageByEntityEvent e) {

		// Is the victim a player?
		if (e.getEntity() instanceof Player)
		{
			Player victim = (Player) e.getEntity();
			Player killer = null;

			// If they're in the game
			if (Infected.isPlayerInGame(victim))
			{

				// Get the attacker
				if (e.getDamager() instanceof Player)
					killer = (Player) e.getDamager();

				else if (e.getDamager() instanceof Arrow)
				{
					victim = (Player) e.getEntity();
					Arrow arrow = (Arrow) e.getDamager();

					if (arrow.getShooter() instanceof Player)
						killer = (Player) arrow.getShooter();
				}

				else if (e.getDamager() instanceof Snowball)
				{
					victim = (Player) e.getEntity();
					Snowball ball = (Snowball) e.getDamager();

					if (ball.getShooter() instanceof Player)
						killer = (Player) ball.getShooter();
				}

				else if (e.getDamager() instanceof Egg)
				{
					victim = (Player) e.getEntity();
					Egg ball = (Egg) e.getDamager();

					if (ball.getShooter() instanceof Player)
						killer = (Player) ball.getShooter();
				}
				if (killer instanceof Player)
				{
					// Make sure they arn't on the same team
					if (Infected.isPlayerHuman(killer) && Infected.isPlayerHuman(victim))
					{
						e.setDamage(0);
						e.setCancelled(true);
					}

					if (Infected.isPlayerZombie(killer) && Infected.isPlayerZombie(victim))
					{
						e.setDamage(0);
						e.setCancelled(true);
					}

					else
					{

						// If the attack happened before the game started
						if (Infected.getGameState() == GameState.VOTING || Infected.getGameState() == GameState.BEFOREINFECTED || Infected.getGameState() == GameState.INLOBBY || Infected.getGameState() == GameState.GAMEOVER)
						{
							e.setDamage(0);
							e.setCancelled(true);
						} else
						{
							Main.Lasthit.put(victim.getName(), killer.getName());
							if (Infected.playerhasHumanClass(killer) || Infected.playerhasZombieClass(killer))
								Abilities.addEffectOnContact(killer, victim);
						}
					}
				}
			}

		}
	}

}
