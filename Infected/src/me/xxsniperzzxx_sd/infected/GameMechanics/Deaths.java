package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Methods;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Main.GameState;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Deaths {

	public static void playerDies(Player Killer, Player Killed) {

		Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(
				Killer, Killed, Infected.playerGetGroup(Killed),
				Infected.isPlayerHuman(Killed) ? true : false));

		Methods.stats(Killer, 1, 0);
		Methods.stats(Killed, 0, 1);

		Methods.handleKillStreaks(true, Killed);
		Methods.handleKillStreaks(false, Killer);

		String kill = getKillType(Infected.playerGetGroup(Killer) + "s", Killer.getName(), Killed.getName());
		for (Player playing : Bukkit.getServer().getOnlinePlayers())
			if (Main.inGame.contains(playing.getName()))
				playing.sendMessage(kill);

		if (Infected.isPlayerHuman(Killed))
		{
			if (Main.config.getBoolean("New Zombies Tp"))
			{
				Methods.zombifyPlayer(Killed);
				Methods.respawn(Killed);

			} else
				Methods.zombifyPlayer(Killed);

			Killed.sendMessage(Main.I + "You have become infected!");
			Killed.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,
					20, 2));

			Equip.equipZombies(Killed);
			Killed.setHealth(20);
			Killed.setFoodLevel(20);
		} else
		{
			Methods.respawn(Killed);
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
			Methods.zombifyPlayer(Killed);
		}
	}
	
	
	
	
	
	

	public static String getKillType(String group, String human, String zombie) {
		Random r = new Random();
		int i = r.nextInt(Files.getKills().getStringList(group).size());
		String killtype = ChatColor.GRAY + Files.getKills().getStringList(group).get(i);
		String msg = null;
		msg = killtype.replaceAll("<zombie>", ChatColor.RED + zombie).replaceAll("<human>", ChatColor.GREEN + human);
		String cmsg = ChatColor.translateAlternateColorCodes('&', msg);
		return cmsg;
	}

}
