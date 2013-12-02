
package me.sniperzciinema.infectedv2;

import java.util.ArrayList;
import java.util.Random;

import me.sniperzciinema.infectedv2.Events.InfectedEndGame;
import me.sniperzciinema.infectedv2.GameMechanics.Deaths;
import me.sniperzciinema.infectedv2.GameMechanics.Stats;
import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Lobby.GameState;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayer;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Messages.Msgs;
import me.sniperzciinema.infectedv2.Tools.Events;
import me.sniperzciinema.infectedv2.Tools.Settings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class Game {

	public static void endGame(Boolean DidHumansWin) {
		Lobby.setGameState(GameState.InLobby);

		InfectedEndGame e = new InfectedEndGame(DidHumansWin);
		Bukkit.getPluginManager().callEvent(e);

		Lobby.reset();

		for (Player u : Lobby.getInGame())
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(u);
			IP.getScoreBoard().showProperBoard();

			Deaths.handleKillStreaks(true, u);
		}
		if (DidHumansWin)
		{
			for (Player u : Lobby.getInGame())
			{
				InfPlayer IP = InfPlayerManager.getInfPlayer(u);
				Inventory IV = Bukkit.getServer().createInventory(null, InventoryType.PLAYER);
				for (ItemStack stack : IP.getInventory())
					if (stack != null)
						IV.addItem(stack);
				for (ItemStack is : Lobby.getActiveArena().getSettings().getRewordItems())
					IV.addItem(is);
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
				Stats.setPoints(u.getName(), Stats.getPoints(u.getName(), Settings.VaultEnabled()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.GameEnds), Settings.VaultEnabled());
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Header.getString("<title>", "Game Over"));
				u.sendMessage("");
				u.sendMessage(Msgs.Game_Over_Humans_Win.getString());
				StringBuilder winnersS = new StringBuilder();
				for (String s : winners)
				{
					winnersS.append(s);
					winnersS.append(", ");
				}
				u.sendMessage(Msgs.Game_Over_Winners.getString("<winners>", winnersS.toString()));
				u.sendMessage("");
				u.sendMessage(Msgs.Game_Info_Arena.getString("<arena>", Lobby.getActiveArena().getName(), "<creator>", Lobby.getActiveArena().getCreator()));
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Line.getString());
				Stats.setPlayingTime(u.getName(), Stats.getPlayingTime(u.getName()) + InfPlayerManager.getInfPlayer(u).getPlayingTime());
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
			{

				@Override
				public void run() {
					for (Player u : Lobby.getInGame())
						InfPlayerManager.getInfPlayer(u).tpToLobby();
				}
			}, 100L);

		} else
		{
			for (final Player u : Lobby.getInGame())
			{
				Stats.setScore(u.getName(), Stats.getScore(u.getName()) + Lobby.getActiveArena().getSettings().getScorePer(Events.GameEnds));
				Stats.setPoints(u.getName(), Stats.getPoints(u.getName(), Settings.VaultEnabled()) + Lobby.getActiveArena().getSettings().getPointsPer(Events.GameEnds), Settings.VaultEnabled());
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Header.getString("<title>", "Game Over"));
				u.sendMessage("");
				u.sendMessage(Msgs.Game_Over_Zombies_Win.getString());
				u.sendMessage("");
				u.sendMessage(Msgs.Game_Info_Arena.getString("<map>", Lobby.getActiveArena().getName(), "<creator>", Lobby.getActiveArena().getCreator()));
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Line.getString());
				Stats.setPlayingTime(u.getName(), Stats.getPlayingTime(u.getName()) + InfPlayerManager.getInfPlayer(u).getPlayingTime());
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
		{

			@Override
			public void run() {
				for (Player u : Lobby.getInGame())
					InfPlayerManager.getInfPlayer(u).tpToLobby();

				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.me, new Runnable()
				{

					@Override
					public void run() {

						if (Lobby.getInGame().size() >= Settings.getRequiredPlayers() && Lobby.getGameState() == GameState.InLobby)
							Lobby.timerStartVote();
					}
				}, 10 * 60);
			}
		}, 100L);

	}

	public static void leaveGame(Player p) {
		InfPlayerManager.getInfPlayer(p).leaveInfected();
	}

	public static void chooseAlphas() {

		int toInfect = 1;
		float percent = Lobby.getActiveArena().getSettings().getAlphaPercent() / 10;
		float inGame = Lobby.getInGame().size();
		toInfect = (int) (inGame * percent);
		if (toInfect == 0)
		{
			toInfect = 1;
		}
		while (Lobby.getZombies().size() != toInfect)
		{
			Player alpha = Lobby.getInGame().get(new Random(50).nextInt(Lobby.getInGame().size()));
			alpha.sendMessage(Msgs.Game_Alpha_You.getString());
			InfPlayerManager.getInfPlayer(alpha).Infect();
			for (Player u : Lobby.getInGame())
				if (u != alpha)
					u.sendMessage(Msgs.Game_Alpha_They.getString("<player>", alpha.getName()));

			for (PotionEffect PE : Lobby.getActiveArena().getSettings().getAlphaPotionEffects())
				alpha.addPotionEffect(PE);
		}
		for (Player u : Lobby.getInGame())
		{
			InfPlayer up = InfPlayerManager.getInfPlayer(u);
			up.getScoreBoard().showProperBoard();
		}
	}
}