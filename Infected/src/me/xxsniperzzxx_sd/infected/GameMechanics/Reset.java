package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;


public class Reset {

	@SuppressWarnings("deprecation")
	public static void resetPlayersInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	public static void tp2LobbyAfter(Player player) {
		player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
		Infected.arenaReset();

		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			ScoreBoard.updateScoreBoard();
		}
		resetPlayersInventory(player);
		player.teleport(LocationHandler.getPlayerLocation(Main.config.getString("Lobby")));

		if(Main.bVersion.equalsIgnoreCase(Main.updateBukkitVersion)){
			if (Infected.filesGetShop().getBoolean("Save Items"))
				player.getInventory().setContents(Infected.playerGetShopInventory(player));
		}
		Main.Lasthit.remove(player.getName());
		
		if(Main.humanClasses.containsKey(player.getName()))
			Infected.playersetLastHumanClass(player, Main.humanClasses.get(player.getName()));
		if(Main.zombieClasses.containsKey(player.getName()))
			Infected.playersetLastZombieClass(player, Main.zombieClasses.get(player.getName()));
		
		Main.humanClasses.remove(player.getName());
		Main.zombieClasses.remove(player.getName());
		
		player.setGameMode(GameMode.ADVENTURE);
		player.updateInventory();
		player.setLevel(0);
		player.setLevel(0);
		Main.Votes.clear();
		Main.inLobby.add(player.getName());
		Main.zombies.clear();
		Main.humans.clear();
		player.setFireTicks(0);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		Main.KillStreaks.remove(player.getName());
		Infected.setGameState(GameState.INLOBBY);
		Main.Voted4.clear();
		Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
		Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
		if (Main.config.getBoolean("Disguise Support.Enabled"))
			if (Disguises.isPlayerDisguised(player))
				Disguises.unDisguisePlayer(player);

		if (Main.inGame.size() == 0)
			Main.Winners.clear();

	}

	public static void resetInf() {
		Infected.setGameState(GameState.INLOBBY);
		Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
		Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
		Main.Winners.clear();
		Main.humans.clear();
		Main.zombies.clear();
		Main.KillStreaks.clear();
		Infected.arenaReset();
	}

	@SuppressWarnings("deprecation")
	public static void resetp(Player player) {

		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
		player.setHealth(20.0);
		player.setFoodLevel(20);
		for (PotionEffect reffect : player.getActivePotionEffects())
		{
			player.removePotionEffect(reffect.getType());
		}
		resetPlayersInventory(player);
		player.updateInventory();
		if (GameMode.valueOf(Main.gamemode.get(player.getName())) == null)
			player.setGameMode(GameMode.SURVIVAL);
		else
			player.setGameMode(GameMode.valueOf(Main.gamemode.get(player.getName())));
		Main.Lasthit.remove(player.getName());
		if (Main.Inventory.containsKey(player.getName()))
			player.getInventory().setContents(Main.Inventory.get(player.getName()));
		if (Main.Armor.containsKey(player.getName()))
			player.getInventory().setArmorContents(Main.Armor.get(player.getName()));
		player.updateInventory();
		player.setExp(Main.Exp.get(player.getName()));
		player.setLevel(Main.Levels.get(player.getName()));
		if (Main.Spot.containsKey(player.getName()))
			player.teleport(LocationHandler.getPlayerLocation(Main.Spot.get(player.getName())));
		if (Main.Food.containsKey(player.getName()))
			player.setFoodLevel(Main.Food.get(player.getName()));
		if (Main.Health.containsKey(player.getName()))
			player.setHealth(Main.Health.get(player.getName()));
		Main.humanClasses.remove(player.getName());
		Main.zombieClasses.remove(player.getName());
		Main.inLobby.remove(player.getName());
		Main.zombies.remove(player.getName());
		Main.humans.remove(player.getName());
		Main.inGame.remove(player.getName());
		Main.Creating.remove(player.getName());
		Main.Health.remove(player.getName());
		Main.Food.remove(player.getName());
		Main.Armor.remove(player.getName());
		Main.Inventory.remove(player.getName());
		Main.Spot.remove(player.getName());
		Main.Winners.remove(player.getName());
		
		if (Main.config.getBoolean("Disguise Support.Enabled"))
			if (Disguises.isPlayerDisguised(player))
				Disguises.unDisguisePlayer(player);

		if (Main.Voted4.containsKey(player.getName()))
		{
			if (Main.Votes.containsKey(Main.Voted4.get(player.getName())))
				Main.Votes.put(Main.Voted4.get(player.getName()).toString(), Main.Votes.get(Main.Voted4.get(player.getName())) - 1);
			Main.Voted4.remove(player.getName());

		}
	}

	// Reset the game(Method)
	public static void reset() {
		for (Player players : Bukkit.getOnlinePlayers())
		{
			if (Infected.isPlayerInGame(players))
			{
				Infected.resetPlayer(players);
				if (Main.config.getBoolean("ScoreBoard Support"))
				{
					ScoreBoard.updateScoreBoard();
				}
			}
		}
		Infected.arenaReset();
		Main.playingin = "";
		Main.KillStreaks.clear();
		Main.possibleArenas.clear();
		Main.inLobby.clear();
		Main.zombieClasses.clear();
		Main.humanClasses.clear();
		Main.Winners.clear();
		Main.zombies.clear();
		Main.humans.clear();
		Main.inGame.clear();
		Main.Voted4.clear();
		Main.Votes.clear();
		Main.Health.clear();
		Main.Food.clear();
		Main.Inventory.clear();
		Main.Spot.clear();
		Main.Armor.clear();
		Infected.setGameState(GameState.INLOBBY);
		Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
		Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
		resetInf();
	}
}
