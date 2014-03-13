
package me.sniperzciinema.infected.Tools;

import java.util.List;

import me.sniperzciinema.infected.Disguise.Disguises;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;


public class TeleportFix implements Listener {

	private Plugin plugin;
	private Server server;

	private final int TELEPORT_FIX_DELAY = 15; // ticks

	public TeleportFix(Plugin plugin)
	{
		this.plugin = plugin;
		this.server = plugin.getServer();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		final Player player = event.getPlayer();
		if (Lobby.isInGame(player))
		{
			// Fix the visibility issue one tick later
			server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{

				@Override
				public void run() {
					// Refresh nearby clients
					final List<Player> nearby = Lobby.getInGame();

					// Hide every player
					updateEntities(player, nearby);

					// Then show them again
					server.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{

						@Override
						public void run() {
							updateEntities(player, nearby);
						}
					}, 1);
				}
			}, TELEPORT_FIX_DELAY);
		}
	}

	public void updateEntities(Player tpedPlayer, List<Player> players) {
		// Hide or show every player to tpedPlayer
		// and hide or show tpedPlayer to every player.
		for (Player player : players)
		{
			// If disguises are enabled only toggle the player if they're
			// disguised
			if (Settings.DisguisesEnabled())
			{
				if (!Disguises.isPlayerDisguised(player))
					if (!tpedPlayer.canSee(player))
						tpedPlayer.showPlayer(player);
					else
						tpedPlayer.hidePlayer(player);
				if (!Disguises.isPlayerDisguised(tpedPlayer))
					if (!player.canSee(tpedPlayer))
						player.showPlayer(tpedPlayer);
					else
						player.hidePlayer(tpedPlayer);

			} else
			{
				if (!tpedPlayer.canSee(player))
					tpedPlayer.showPlayer(player);
				else
					tpedPlayer.hidePlayer(player);
				if (!player.canSee(tpedPlayer))
					player.showPlayer(tpedPlayer);
				else
					player.hidePlayer(tpedPlayer);

			}
		}
	}

}