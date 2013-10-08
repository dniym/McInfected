
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
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

	public static void playerDies(Player Killer, Player Killed) {

		Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(
				Killer, Killed, Infected.playerGetGroup(Killed),
				Infected.isPlayerHuman(Killed) ? true : false));

		Stats.setStats(Killer, 1, 0);
		Stats.setStats(Killed, 0, 1);

		Stats.handleKillStreaks(true, Killed);
		Stats.handleKillStreaks(false, Killer);

		String kill = getKillType(Infected.playerGetGroup(Killer) + "s", Killer.getName(), Killed.getName());
		for (Player playing : Bukkit.getServer().getOnlinePlayers())
			if (Main.inGame.contains(playing.getName()))
				playing.sendMessage(kill);

		if (Infected.isPlayerHuman(Killed))
		{
			Killed.playSound(Killed.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
			if (Main.config.getBoolean("New Zombies Tp"))
				LocationHandler.respawn(Killed);

			Zombify.zombifyPlayer(Killed);

			Killed.sendMessage(Main.I + "You have become infected!");
			Killed.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					20, 2));

			Equip.equipZombies(Killed);
			Killed.setHealth(20);
			Killed.setFoodLevel(20);
		} else
		{
			Killed.playSound(Killed.getLocation(), Sound.ZOMBIE_PIG_DEATH, 1, 1);
			LocationHandler.respawn(Killed);
			Equip.equipZombies(Killed);
		}

		Killed.setFallDistance(0F);

		if (Infected.isPlayerHuman(Killed))
			Infected.delPlayerHuman(Killed);

		if (!Infected.isPlayerZombie(Killed))
			Infected.addPlayerZombie(Killed);

		if (Main.Lasthit.containsKey(Killed.getName()))
			Main.Lasthit.remove(Killed.getName());

		if (Main.Winners.contains(Killed.getName()))
			Main.Winners.remove(Killed.getName());

		if (Main.humans.size() == 0 && Infected.getGameState() == GameState.STARTED)
			Game.endGame(false);

		else
		{
			Equip.equipZombies(Killed);
			Zombify.zombifyPlayer(Killed);
		}
	}

	public static String getKillType(String group, String killer, String killed) {
		Random r = new Random();
		int i = r.nextInt(Files.getKills().getStringList(group).size());
		String killtype = ChatColor.GRAY + Files.getKills().getStringList(group).get(i);
		String msg = null;

		if (group.equalsIgnoreCase("Zombies"))
			msg = killtype.replaceAll("<zombie>", ChatColor.RED + killer).replaceAll("<human>", ChatColor.GREEN + killed);
		else
			msg = killtype.replaceAll("<zombie>", ChatColor.RED + killed).replaceAll("<human>", ChatColor.GREEN + killer);

		String cmsg = ChatColor.translateAlternateColorCodes('&', msg);
		return cmsg;
	}

}
