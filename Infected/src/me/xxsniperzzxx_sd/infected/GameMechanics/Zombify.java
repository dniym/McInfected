
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.TimeHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Zombify {


	public static void zombifyPlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()))
		{
			Abilities.applyClassAbility(player);
		} else if (Main.config.getBoolean("Zombie Abilities") == true)
		{
			Abilities.applyAbilities(player);
		}
		if (Main.config.getBoolean("Disguise Support.Enabled"))
			Disguises.disguisePlayer(player);
	}
	@SuppressWarnings("deprecation")
	public static void newZombieSetUpEveryOne() {
		Random r = new Random();
		if (Main.inGame.size() <= 0)
		{
			Infected.resetPlugin();
		} else
		{
			Main.zombies.clear();
			Main.humans.clear();
			for (Player online : Bukkit.getServer().getOnlinePlayers())
				if (Infected.isPlayerInGame(online) && Main.config.getBoolean("Disguise Support.Enabled"))
					if (Disguises.isPlayerDisguised(online))
						Disguises.unDisguisePlayer(online);
			int alphazombies = 1;
			if (Main.config.getBoolean("Percent to Infected.Enable"))
				alphazombies = Main.inGame.size() / Main.config.getInt("Percent to Infect");
			int temp;
			for (temp = 0; temp != alphazombies && Main.zombies.size() != alphazombies; temp++)
			{
				int alpha = r.nextInt(Main.inGame.size());
				String name = Main.inGame.get(alpha);
				Player zombie = Bukkit.getServer().getPlayer(name);
				zombie.sendMessage(Messages.sendMessage(Msgs.GAME_YOURAREFIRSTINFECTED, null, null));
				Main.zombies.add(zombie.getName());
				Main.Winners.remove(zombie.getName());
				if (Main.config.getBoolean("New Zombie Tp"))
					LocationHandler.respawn(zombie);

				zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
				if (Main.config.getBoolean("Zombie Abilities") == true)
				{
					zombie.addPotionEffect(new PotionEffect(
							PotionEffectType.SPEED, 2000, 2), true);
					zombie.addPotionEffect(new PotionEffect(
							PotionEffectType.JUMP, 2000, 1), true);
				}
				zombie.setHealth(20);
				Equip.equipZombies(zombie);
				zombifyPlayer(zombie);
				for (Player online : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
						online.sendMessage(Messages.sendMessage(Msgs.GAME_FIRSTINFECTED, zombie, null));

			}
			// Inform humans of infected, prepare them
			for (Player online : Bukkit.getServer().getOnlinePlayers())
			{
				if (Infected.isPlayerInGame(online))
				{
					if (Main.config.getBoolean("ScoreBoard Support"))
						if (!Main.KillStreaks.containsKey(online.getName()))
							Main.KillStreaks.put(online.getName(), Integer.valueOf("0"));
					int timeleft = Main.GtimeLimit;
					online.sendMessage(Main.I + ChatColor.WHITE + "You have " + ChatColor.YELLOW + TimeHandler.getTime(Long.valueOf(timeleft)) + ChatColor.WHITE + ". Good luck!");
					if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
					{
						// if(Main.humans.contains(online)) {
						Main.humans.add(online.getName());
						if (!Main.Winners.contains(online.getName()))
						{
							Main.Winners.add(online.getName());
						}
						online.setHealth(20);
						online.playEffect(online.getLocation(), Effect.SMOKE, 2);

					}
				}
			}
		}
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			ScoreBoard.updateScoreBoard();
		}
	}

	@SuppressWarnings("deprecation")
	public static void joinInfectHuman(Player player) {
		Player newzombie = player;
		if (!Main.Timein.containsKey(newzombie.getName()))
			Main.Timein.put(newzombie.getName(), System.currentTimeMillis() / 1000);
		Main.humans.remove(newzombie.getName());
		if (!Main.KillStreaks.containsKey(newzombie.getName()))
			Main.KillStreaks.put(newzombie.getName(), Integer.valueOf("0"));
		newzombie.sendMessage(Main.I + "You have became infected!");
		Equip.equipZombies(newzombie);
		newzombie.setHealth(20);
		newzombie.setFoodLevel(20);
		Main.KillStreaks.remove(newzombie.getName());
		for (Player playing : Bukkit.getServer().getOnlinePlayers())
		{
			if ((!(playing == newzombie)) && Main.inGame.contains(playing.getName()))
				playing.sendMessage(Messages.sendMessage(Msgs.GAME_GOTINFECTED, newzombie, null));
		}
		newzombie.setFallDistance(0F);
		LocationHandler.respawn(newzombie);
		newzombie.setFallDistance(0F);

		Main.zombies.add(newzombie.getName());
		Main.Winners.remove(newzombie.getName());
		Main.inLobby.remove(newzombie.getName());
		newzombie.playEffect(newzombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 2);
		zombifyPlayer(newzombie);
		newzombie.setHealth(20);
		Equip.equipZombies(newzombie);
	}

}
