
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Events.InfectedGameEndEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedGameStartEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedVoteStartEvent;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.OldStats.MiscStats;
import me.xxsniperzzxx_sd.infected.GameMechanics.OldStats.PointsAndScores;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Handlers.Misc.ItemHandler;
import me.xxsniperzzxx_sd.infected.Handlers.Misc.LocationHandler;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class Game {

	private static Lobby Lobby = Main.Lobby;

	@SuppressWarnings("deprecation")
	public static void endGame(Boolean DidHumansWin) {
		if (Lobby.getGameState() == GameState.Started)
		{
			Lobby.setGameState(GameState.InLobby);

			ScoreBoard.updateScoreBoard();
			for (Player playing : Bukkit.getServer().getOnlinePlayers())
			{
				if (Lobby.isPlayerInGame(playing))
				{
					if (Main.KillStreaks.containsKey(playing.getName()))
					{
						if (Main.KillStreaks.get(playing.getName()) > Files.getPlayers().getInt("Players." + playing.getName().toLowerCase() + ".KillStreak"))
						{
							Files.getPlayers().set("Players." + playing.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(playing.getName()));
							Files.savePlayers();
						}
					}
				}
			}
			Bukkit.getServer().getPluginManager().callEvent(new InfectedGameEndEvent(
					Main.inGame, Main.Winners, DidHumansWin));

			if (DidHumansWin)
			{
				if (Main.config.getBoolean("Vault Support.Enable"))
				{
					int rewardMoney = Main.config.getInt("Vault Support.Reward");

					for (Player playing : Bukkit.getOnlinePlayers())
						if (Main.Winners.contains(playing.getName()))

							Main.economy.depositPlayer(playing.getName(), rewardMoney);
				}
				if (!(Main.config.getString("Command Reward").equalsIgnoreCase(null) || Main.config.getString("Command Reward").equalsIgnoreCase("[]")))
				{
					for (Player playing : Bukkit.getOnlinePlayers())
					{
						if (Main.Winners.contains(playing.getName()))
						{
							String s = Main.config.getString("Command Reward").replaceAll("<player>", playing.getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
						}
					}
				}
				for (String s : Main.config.getStringList("Rewards"))
				{
					for (Player playing : Bukkit.getOnlinePlayers())
					{
						if (Main.Winners.contains(playing.getName()))
						{
							playing.getInventory().setContents(Main.Inventory.get(playing.getName()));
							playing.updateInventory();
							playing.getInventory().addItem(ItemHandler.getItemStack(s));
							playing.updateInventory();
							Main.Inventory.put(playing.getName(), playing.getInventory().getContents());
							Reset.resetPlayersInventory(playing);
							playing.updateInventory();
						}
					}
				}
				for (final Player playing : Bukkit.getServer().getOnlinePlayers())
				{
					if (Main.inGame.contains(playing.getName()))
					{
						PointsAndScores.rewardPointsAndScore(playing, "Game Over");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.AFTERGAME_HUMANSWIN, null, null));
						StringBuilder winners = new StringBuilder();
						for (Object o : Main.Winners)
						{
							winners.append(o.toString());
							winners.append(", ");
						}
						playing.sendMessage(Main.I + "Winners: " + winners.toString());
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.GAME_MAP, null, null));
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						Lobby.playerSetTime(playing.getName());
						Files.savePlayers();
						if (Main.config.getBoolean("Disguise Support.Enabled"))
							if (Disguises.isPlayerDisguised(playing))
								Disguises.unDisguisePlayer(playing);

						for (PotionEffect reffect : playing.getActivePotionEffects())
						{
							playing.removePotionEffect(reffect.getType());
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{

							@Override
							public void run() {
								Reset.tp2LobbyAfter(playing);
							}
						}, 100L);
					}
				}
			} else
			{
				for (final Player playing : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(playing.getName()))
					{
						PointsAndScores.rewardPointsAndScore(playing, "Game Over");
						Lobby.playerSetTime(playing.getName());

						Files.savePlayers();

						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						playing.sendMessage(Messages.sendMessage(Msgs.AFTERGAME_ZOMBESWIN, null, null));
						playing.sendMessage("");
						playing.sendMessage(Messages.sendMessage(Msgs.GAME_MAP, null, null));
						playing.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
						for (PotionEffect reffect : playing.getActivePotionEffects())
							playing.removePotionEffect(reffect.getType());

						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{

							@Override
							public void run() {
								Reset.tp2LobbyAfter(playing);
							}
						}, 100L);
					}
			}
			ScoreBoard.updateScoreBoard();
			Main.playingin = "";
			Main.Winners.clear();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
			{

				@Override
				public void run() {

					Lobby.setGameState(GameState.INLOBBY);
					if (Main.inGame.size() >= Main.config.getInt("Automatic Start.Minimum Players") && Lobby.getGameState() == GameState.INLOBBY && Main.config.getBoolean("Automatic Start.Use"))
					{
						Lobby.timerStartVote();
					}
				}
			}, 10 * 60);
		}
	}


	public static void endGame() {
		Lobby Lobby = Main.Lobby;
		Lobby.getHumans().clear();
		Lobby.getZombies().clear();
		Lobby.setGameState(GameState.InLobby);
	}

	public static void leaveGame(Player p) {

	}
}