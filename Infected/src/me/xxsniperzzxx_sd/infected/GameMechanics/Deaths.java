
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Enums.DeathTypes;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.GameMechanics.Stats.Stats;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Deaths {

	public static void playerDies(DeathTypes death, Player killer, Player killed) {

		String kill;
		if (getKillType(Infected.playerGetGroup(killer) + "s", Infected.playerGetGroup(killer) + "s." +death.toString(), killer.getName(), killed.getName()) == null)
		{
			killer.sendMessage(Main.I + ChatColor.DARK_RED + "Please tell your server Admins to delete the Kills.yml for Infected! It now does kill messages differently.");
			killed.sendMessage(Main.I + ChatColor.DARK_RED + "Please tell your server Admins to delete the Kills.yml for Infected! It now does kill messages differently.");
			kill = getKillType(Infected.playerGetGroup(killer) + "s", Infected.playerGetGroup(killer) + "s." +death.toString(), killer.getName(), killed.getName());
		} else
			kill = getKillType(Infected.playerGetGroup(killer) + "s", Infected.playerGetGroup(killer) + "s." +death.toString(), killer.getName(), killed.getName());

		InfectedPlayerDieEvent dieEvent = new InfectedPlayerDieEvent(killer,
				killed, Infected.isPlayerHuman(killed) ? true : false, death,
				kill);
		Bukkit.getServer().getPluginManager().callEvent(dieEvent);
		if (!dieEvent.isCancelled())
		{

			Stats.setStats(killer, 1, 0);
			Stats.setStats(killed, 0, 1);

			Stats.handleKillStreaks(true, killed);
			Stats.handleKillStreaks(false, killer);
			for (Player playing : Bukkit.getServer().getOnlinePlayers())
				if (Main.inGame.contains(playing.getName()))
					playing.sendMessage(dieEvent.getDeathMsg());

			if (Infected.isPlayerHuman(killed))
			{
				killed.playSound(killed.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
				if (Main.config.getBoolean("New Zombies Tp"))
					LocationHandler.respawn(killed);

				Zombify.zombifyPlayer(killed);

				killed.sendMessage(Main.I + "You have become infected!");
				killed.addPotionEffect(new PotionEffect(
						PotionEffectType.CONFUSION, 20, 2));

				Equip.equipZombies(killed);
				killed.setHealth(20);
				killed.setFoodLevel(20);
			} else
			{
				killed.playSound(killed.getLocation(), Sound.ZOMBIE_PIG_DEATH, 1, 1);
				LocationHandler.respawn(killed);
				Equip.equipZombies(killed);
			}

			killed.setFallDistance(0F);

			if (Infected.isPlayerHuman(killed))
				Infected.delPlayerHuman(killed);

			if (!Infected.isPlayerZombie(killed))
				Infected.addPlayerZombie(killed);

			if (Main.Lasthit.containsKey(killed.getName()))
				Main.Lasthit.remove(killed.getName());

			if (Main.Winners.contains(killed.getName()))
				Main.Winners.remove(killed.getName());

			if (Main.humans.size() == 0 && Infected.getGameState() == GameState.STARTED)
				Game.endGame(false);

			else
			{
				Equip.equipZombies(killed);
				Zombify.zombifyPlayer(killed);
			}
		}
	}

	public static String getKillType(String group, String path, String killer, String killed) {
		Random r = new Random();
		int i = r.nextInt(Files.getKills().getStringList(path).size());
		String killtype = ChatColor.GRAY + Files.getKills().getStringList(path).get(i);
		String msg = null;

		if (group.equalsIgnoreCase("Zombies"))
			msg = killtype.replaceAll("<zombie>", ChatColor.RED + killer).replaceAll("<human>", ChatColor.GREEN + killed);
		else
			msg = killtype.replaceAll("<zombie>", ChatColor.RED + killed).replaceAll("<human>", ChatColor.GREEN + killer);

		String cmsg = ChatColor.translateAlternateColorCodes('&', msg);
		return cmsg;
	}

}
