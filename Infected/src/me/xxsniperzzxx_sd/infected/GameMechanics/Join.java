package me.xxsniperzzxx_sd.infected.GameMechanics;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerJoinEvent;
import me.xxsniperzzxx_sd.infected.Tools.Updater;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Join
{
     public static void joinGame(Player player)
     {
    
		for (Player all : Bukkit.getServer().getOnlinePlayers())
		{
			if (Main.inGame.contains(all.getName()))
			{
				all.sendMessage(Messages.sendMessage(Msgs.LOBBY_OTHERJOINEDLOBBY, player, null));
			}
		}

		// Safe player's stats/stuff

		InfectedPlayerJoinEvent joinEvent = new InfectedPlayerJoinEvent(player, Main.inGame, Main.me.getConfig().getInt("Automatic Start.Minimum Players"));
		Bukkit.getServer().getPluginManager().callEvent(joinEvent);
		
		if(!joinEvent.isCancelled()){
			Main.inGame.add(player.getName());
			Main.Spot.put(player.getName(), LocationHandler.getLocationToString(player.getLocation()));
			Main.Health.put(player.getName(), player.getHealth());
			Main.Food.put(player.getName(), player.getFoodLevel());
			player.teleport(LocationHandler.getPlayerLocation(Main.me.getConfig().getString("Lobby")));
			Main.Levels.put(player.getName(), player.getLevel());
			Main.Exp.put(player.getName(), player.getExp());
			Main.Inventory.put(player.getName(), player.getInventory().getContents());
			Main.Armor.put(player.getName(), player.getInventory().getArmorContents());
			Main.Winners.clear();
			Main.inLobby.add(player.getName());
			Main.gamemode.put(player.getName(), player.getGameMode().toString());
	
			if (Main.config.getBoolean("ScoreBoard Support"))
			{
				ScoreBoard.updateScoreBoard();
			}
	
			if (Main.config.getBoolean("Disguise Support.Enabled"))
				if (Disguises.isPlayerDisguised(player))
					Disguises.unDisguisePlayer(player);
	
			// Prepare player
			player.setMaxHealth(20.0);
			player.setHealth(20.0);
			player.setFoodLevel(20);
			for (PotionEffect reffect : player.getActivePotionEffects())
			{
				player.removePotionEffect(reffect.getType());
			}
			Reset.resetPlayersInventory(player);
			Updater updater = new Updater(Main.me, "Infected-Core",
					Main.file, Updater.UpdateType.NO_DOWNLOAD, false);
			if (Main.bVersion.equalsIgnoreCase(updater.updateBukkitVersion))
			{
				if (Infected.filesGetShop().getBoolean("Save Items") && Infected.playerGetShopInventory(player) != null)
					player.getInventory().setContents(Infected.playerGetShopInventory(player));
			}
			player.sendMessage(Messages.sendMessage(Msgs.LOBBY_JOINLOBBY, null, null));
	
			player.setGameMode(GameMode.ADVENTURE);
			player.setFlying(false);
	
			if (!Infected.playergetLastHumanClass(player).equalsIgnoreCase("None"))
				Main.humanClasses.put(player.getName(), Infected.playergetLastHumanClass(player));
	
			if (!Infected.playergetLastZombieClass(player).equalsIgnoreCase("None"))
				Main.zombieClasses.put(player.getName(), Infected.playergetLastZombieClass(player));
	
			if (!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), Integer.valueOf("0"));
	
			if (Main.inGame.size() >= Main.me.getConfig().getInt("Automatic Start.Minimum Players") && Infected.getGameState() == GameState.INLOBBY && Main.me.getConfig().getBoolean("Automatic Start.Use"))
			{
				Game.START();
			}
			else if (Infected.getGameState() == GameState.VOTING)
			{
				player.sendMessage(Messages.sendMessage(Msgs.VOTE_HOWTOVOTE, null, null));
				player.performCommand("Infected Arenas");
				
			}
			else if (Infected.getGameState() == GameState.STARTED)
			{
				player.setGameMode(GameMode.SURVIVAL);
				Zombify.joinInfectHuman(player);
				Infected.delPlayerInLobby(player);
				
			}
			else if (Infected.getGameState() == GameState.BEFOREINFECTED)
			{
				if (Main.config.getBoolean("ScoreBoard Support"))
				{
					player.setGameMode(GameMode.SURVIVAL);
					ScoreBoard.updateScoreBoard();
				}
				LocationHandler.respawn(player);
				if (!Main.Winners.contains(player.getName()))
				{
					Main.Winners.add(player.getName());
				}
				Main.Timein.put(player.getName(), System.currentTimeMillis() / 1000);
				if (!Main.KillStreaks.containsKey(player.getName()))
					Main.KillStreaks.put(player.getName(), Integer.valueOf("0"));
				player.setHealth(20.0);
				player.setFoodLevel(20);
				player.playEffect(player.getLocation(), Effect.SMOKE, BlockFace.UP);
				Equip.equipHumans(player);
				Infected.delPlayerInLobby(player);
			}
		}
	}

}
