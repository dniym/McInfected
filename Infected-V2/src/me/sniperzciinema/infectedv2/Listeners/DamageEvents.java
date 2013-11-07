
package me.sniperzciinema.infectedv2.Listeners;

import me.sniperzciinema.infectedv2.Game;
import me.sniperzciinema.infectedv2.Main;
import me.sniperzciinema.infectedv2.GameMechanics.DeathType;
import me.sniperzciinema.infectedv2.GameMechanics.Deaths;
import me.sniperzciinema.infectedv2.GameMechanics.PotionEffects;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;

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


public class DamageEvents implements Listener {

	public Main plugin;

	public DamageEvents(Main instance)
	{
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player)
		{
			Player victim = (Player) e.getEntity();

			// Make sure the victim is in Infected
			if (Lobby.isInGame(victim))
			{
				// Make sure the damage caused isn't from another entity as that
				// is handled in a different event listener
				if (e.getCause() != DamageCause.ENTITY_ATTACK && e.getCause() != DamageCause.PROJECTILE)
				{
					InfPlayer IPV = InfPlayerManager.getInfPlayer(victim);

					// If the Player got hurt during Voting, just cancel it.
					if (Lobby.getGameState() == GameState.Voting)
					{
						e.setDamage(0);
						e.setCancelled(true);
					}

					// If the player gets hurt well the game is choosing an
					// Infected, kill the player, but respawn them without doing
					// anything
					else if (Lobby.getGameState() == GameState.Infecting && victim.getHealth() - e.getDamage() <= 0)
					{
						e.setDamage(0);
						victim.sendMessage(Main.I + "You almost died before the game even started!");
						// Because we're not counting this, we'll just respawn
						// them without saying they died
						IPV.respawn();
					}

					// If the game has started, lets do some stuff
					else if (Lobby.getGameState() == GameState.Started)
					{
						// Is the damage enough to kill the player?
						if (victim.getHealth() - e.getDamage() <= 0)
						{
							// If the stored last damager isn't null, we'll say
							// they're the killer
							if (IPV.getLastDamager() != null)
							{
								Player killer = IPV.getLastDamager();
								// Not really sure how they died? Just say melee
								Deaths.playerDies(DeathType.Melee, killer, victim);
								e.setDamage(0);
							}
							// Other wise, they just died by "Other"
							else
								Deaths.playerDies(DeathType.Other, null, victim);

							// Was that the last human?
							if (Lobby.getHumans().size() == 0)
								Game.endGame(false);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamage(EntityDamageByEntityEvent e) {

		// Hmm so an Entity hit them, but which entity?

		// Is the victim a player?
		if (e.getEntity() instanceof Player)
		{
			Player victim = (Player) e.getEntity();
			Player killer = null;

			// Before we go any farther lets make sure they're even in the game
			if (Lobby.isInGame(victim))
			{
				// By default we'll say it was melee
				DeathType death = DeathType.Melee;

				// Was the entity that did the damage a player?
				if (e.getDamager() instanceof Player)
					killer = (Player) e.getDamager();

				// Was the entity that did the damage a arrow?
				else if (e.getDamager() instanceof Arrow)
				{
					victim = (Player) e.getEntity();
					Arrow arrow = (Arrow) e.getDamager();

					// Was the shooter of the arrow a player?
					if (arrow.getShooter() instanceof Player)
					{
						killer = (Player) arrow.getShooter();
						death = DeathType.Arrow;
					}
				}
				// Was the entity that did the damage a Snowball?
				else if (e.getDamager() instanceof Snowball)
				{
					victim = (Player) e.getEntity();
					Snowball ball = (Snowball) e.getDamager();

					// Was the shooter of the snowball a player?
					if (ball.getShooter() instanceof Player)
					{
						killer = (Player) ball.getShooter();
						death = DeathType.Snowball;
					}
				}

				// Was the entity that did the damage a Snowball?
				else if (e.getDamager() instanceof Egg)
				{
					victim = (Player) e.getEntity();
					Egg ball = (Egg) e.getDamager();

					// Was the shooter of the egg a player??
					if (ball.getShooter() instanceof Player)
					{
						killer = (Player) ball.getShooter();
						death = DeathType.Egg;
					}
				}
				// Lets make sure the final killer is a Player
				if (killer instanceof Player && Lobby.isInGame(killer))
				{
					// If the Player got hurt by a player before the game
					// started, just cancel it.
					if (Lobby.getGameState() != GameState.Started)
					{
						e.setDamage(0);
						e.setCancelled(true);
					}

					// If the game has started lets start watching
					else
					{
						// Make sure they arn't on the same team
						if (!Lobby.oppositeTeams(killer, victim))
						{
							e.setDamage(0);
							e.setCancelled(true);
						} else
						{
							InfPlayer IPV = InfPlayerManager.getInfPlayer(victim);
							InfPlayer IPK = InfPlayerManager.getInfPlayer(killer);
							IPV.setLastDamager(killer);

							// If it was enough to kill the player
							if (victim.getHealth() - e.getDamage() <= 0)
							{
								e.setDamage(0);
								Deaths.playerDies(death, killer, victim);

							}
							// If the damage wasn't enough to kill them, lets
							// see what contact effects we need to apply
							else if (!IPK.getInfClass(IPK.getTeam()).getContacteffects().isEmpty())
								PotionEffects.addEffectOnContact(killer, victim);
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
