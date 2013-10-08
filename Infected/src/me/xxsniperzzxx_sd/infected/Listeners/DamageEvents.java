
package me.xxsniperzzxx_sd.infected.Listeners;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.GameMechanics.Abilities;
import me.xxsniperzzxx_sd.infected.GameMechanics.Deaths;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.Game;
import me.xxsniperzzxx_sd.infected.GameMechanics.Zombify;
import me.xxsniperzzxx_sd.infected.GameMechanics.Stats.Stats;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;


public class DamageEvents implements Listener {

	public Main plugin;

	public DamageEvents(Main instance)
	{
		this.plugin = instance;
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
					if (Infected.getGameState() == GameState.VOTING)
						e.setCancelled(true);

					// Before a zombie is chosen
					else if (Infected.getGameState() == GameState.BEFOREINFECTED)
					{
						if (victim.getHealth() - e.getDamage() <= 0)
						{
							e.setDamage(0);

							victim.sendMessage(Main.I + "You almost died before the game even started!");
							victim.setHealth(20);
							victim.setFoodLevel(20);
							Stats.handleKillStreaks(true, victim);
							victim.setFoodLevel(20);
							LocationHandler.respawn(victim);
							victim.setFallDistance(0F);
						}
					}
					// If the game has fully started
					else if (Infected.getGameState() == GameState.STARTED)
					{
						Player killer = null;
						if (Main.Lasthit.containsKey(victim.getName()))
							killer = Bukkit.getPlayer(Main.Lasthit.get(victim.getName()));

						if ((victim != null) && (killer != null))
						{

							// Make sure both are in the game(you never know :P)
							if (Infected.isPlayerInGame(killer) && Infected.isPlayerInGame(victim))
							{

								// Saves who hit the person last
								Main.Lasthit.put(victim.getName(), killer.getName());

								// If it was enough to kill the player
								if (victim.getHealth() - e.getDamage() <= 0)
								{
									Deaths.playerDies(killer, victim);
									e.setDamage(0);
								}
							}
						} else
						{
							if (victim.getHealth() - e.getDamage() <= 0)
							{
								if (Main.humans.contains(victim))
								{
									for (Player playing : Bukkit.getServer().getOnlinePlayers())
									{
										if (Infected.isPlayerInGame(playing))
										{
											playing.sendMessage(Messages.sendMessage(Msgs.GAME_GOTINFECTED, victim, null));
										}
									}
								}

								if (Infected.isPlayerHuman(victim))
								{
									for (Player playing : Bukkit.getServer().getOnlinePlayers())
									{
										if (Infected.isPlayerInGame(playing))
										{
											playing.sendMessage(Messages.sendMessage(Msgs.GAME_GOTINFECTED, victim, null));
										}
									}
								}
								victim.setHealth(20);
								victim.setFallDistance(0F);
								victim.setFoodLevel(20);
								LocationHandler.respawn(victim);
								victim.setFallDistance(0F);
								e.setDamage(0);
								Main.humans.remove(victim.getName());
								Main.Lasthit.remove(victim.getName());
								Main.Winners.remove(victim.getName());
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
						if (Infected.getGameState() == GameState.VOTING)
						{
							e.setDamage(0);
							e.setCancelled(true);
						}

						// Before a zombie is chosen
						else if (Infected.getGameState() == GameState.BEFOREINFECTED)
						{
							e.setDamage(0);
							e.setCancelled(true);
						}

						// If the game has fully started
						else if (Infected.getGameState() == GameState.STARTED)
						{
							// If it doesn't end up null (In case a mob hit
							// them)
							if ((victim != null) && (killer != null))
							{

								// Make sure both are in the game(you never know
								// :P)
								if (Infected.isPlayerInGame(killer) && Infected.isPlayerInGame(victim))
								{

									// Saves who hit the person last
									Main.Lasthit.put(victim.getName(), killer.getName());

									// If it was enough to kill the player
									if (victim.getHealth() - e.getDamage() <= 0)
									{
										e.setDamage(0);
										Deaths.playerDies(killer, victim);
									} else if (Infected.playerhasHumanClass(killer) || Infected.playerhasZombieClass(killer))
										Abilities.addEffectOnContact(killer, victim);

								}
							}
						} else
						{
							e.setDamage(0);
							e.setCancelled(true);
						}
					}
				}
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
