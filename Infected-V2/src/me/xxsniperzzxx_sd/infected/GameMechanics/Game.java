
package me.xxsniperzzxx_sd.infected.GameMechanics;

import java.util.ArrayList;
import java.util.Random;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayer;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby.GameState;
import me.xxsniperzzxx_sd.infected.Messages.Msgs;
import me.xxsniperzzxx_sd.infected.Tools.Events;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;


public class Game {

	private static Lobby Lobby = Main.Lobby;

	public static void endGame(Boolean DidHumansWin) {
		Lobby.setGameState(GameState.InLobby);
		ScoreBoard.updateScoreBoard();

		for (Player u : Lobby.getInGame())
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(u);
			if (IP.getKillstreak() > Stats.getHighestKillStreak(u.getName()))
				Stats.setHighestKillStreak(u.getName(), IP.getKillstreak());
		}
		if (DidHumansWin)
		{
			for (Player u : Lobby.getInGame())
			{
				InfPlayer IP = InfPlayerManager.getInfPlayer(u);
				Inventory IV = Bukkit.getServer().createInventory(null, InventoryType.PLAYER);
				IV.addItem(IP.getInventory());
				IV.addItem(Lobby.getActiveArena().getSettings().getRewordItems());
			}
			// TODO: If winner add money
			// Main.economy.depositPlayer(playing.getName(), rewardMoney);

			ArrayList<String> winners = new ArrayList<String>();
			for (final Player u : Lobby.getInGame())
				if (InfPlayerManager.getInfPlayer(u).isWinner())
					winners.add(u.getName());

			for (final Player u : Lobby.getInGame())
			{
				Stats.setScore(u.getName(), Stats.getScore(u.getName()) + Lobby.getActiveArena().getSettings().getScorePer(Events.GameEnds));
				Stats.setPoints(u.getName(), Stats.getPoints(u.getName()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.GameEnds), Main.config.getBoolean("Vault Support.Enabled"));
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Header.getString("<title>", "Game Over"));
				u.sendMessage("");
				u.sendMessage("Humans Won");
				StringBuilder winnersS = new StringBuilder();
				for (String s : winners)
				{
					winnersS.append(s);
					winnersS.append(", ");
				}
				u.sendMessage(Main.I + "Winners: " + winners.toString());
				u.sendMessage("");
				u.sendMessage(Msgs.Arena_Information.getString("<map>", Lobby.getActiveArena().getName(), "<creator>", Lobby.getActiveArena().getCreator()));
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Line.getString());
				Stats.setPlayingTime(u.getName(), InfPlayerManager.getInfPlayer(u).getPlayingTime());

				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
				{

					@Override
					public void run() {
						InfPlayerManager.getInfPlayer(u).tpToLobby();
					}
				}, 100L);
			}
		} else
		{
			for (final Player u : Lobby.getInGame())
			{
				Stats.setScore(u.getName(), Stats.getScore(u.getName()) + Lobby.getActiveArena().getSettings().getScorePer(Events.GameEnds));
				Stats.setPoints(u.getName(), Stats.getPoints(u.getName()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.GameEnds), Main.config.getBoolean("Vault Support.Enabled"));
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Header.getString("<title>", "Game Over"));
				u.sendMessage("");
				u.sendMessage("Zombies Won");
				u.sendMessage("");
				u.sendMessage(Msgs.Arena_Information.getString("<map>", Lobby.getActiveArena().getName(), "<creator>", Lobby.getActiveArena().getCreator()));
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Line.getString());
				Stats.setPlayingTime(u.getName(), InfPlayerManager.getInfPlayer(u).getPlayingTime());

				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
				{

					@Override
					public void run() {
						InfPlayerManager.getInfPlayer(u).tpToLobby();

						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
						{

							@Override
							public void run() {

								if (Lobby.getInGame().size() >= Main.config.getInt("Automatic Start.Minimum Players") && Lobby.getGameState() == GameState.InLobby)
								{
									Lobby.timerStartVote();
								}
							}
						}, 10 * 60);
					}
				}, 100L);
			}
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

	public static void chooseAlphas() {

		int toInfect = Lobby.getInGame().size() / (Lobby.getActiveArena().getSettings().getAlphaPercent() / 10);

		do
		{
			Player alpha = Lobby.getInGame().get(new Random(50).nextInt(Lobby.getInGame().size()));
			alpha.sendMessage("You're an alpha");
			InfPlayerManager.getInfPlayer(alpha).Infect();
			for (Player u : Lobby.getInGame())
				if (u != alpha)
					u.sendMessage("Alpha is an alpha");

			for (PotionEffect PE : Lobby.getActiveArena().getSettings().getAlphaPotionEffects())
				alpha.addPotionEffect(PE);

		} while (Lobby.getZombies().size() != toInfect);

		ScoreBoard.updateScoreBoard();
	}
}