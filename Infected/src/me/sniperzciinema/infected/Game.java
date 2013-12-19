
package me.sniperzciinema.infected;

import java.util.ArrayList;
import java.util.Random;

import me.sniperzciinema.infected.Enums.Events;
import me.sniperzciinema.infected.Events.InfectedEndGame;
import me.sniperzciinema.infected.Extras.Pictures;
import me.sniperzciinema.infected.GameMechanics.KillStreaks;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;

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

		for (Player u : Lobby.getInGame())
		{
			InfPlayer IP = InfPlayerManager.getInfPlayer(u);
			IP.getScoreBoard().showProperBoard();

			KillStreaks.handle(true, u);
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
				int i = 0;
				for (String s : winners)
				{
					i++;
					winnersS.append(s);
					if(i == winners.size())
						winnersS.append(".");
					else
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
				u.sendMessage(Msgs.Game_Info_Arena.getString("<arena>", Lobby.getActiveArena().getName(), "<creator>", Lobby.getActiveArena().getCreator()));
				u.sendMessage("");
				u.sendMessage(Msgs.Format_Line.getString());
				Stats.setPlayingTime(u.getName(), Stats.getPlayingTime(u.getName()) + InfPlayerManager.getInfPlayer(u).getPlayingTime());
			}
		}
		Lobby.reset();
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
		String[] face = null;

		while (Lobby.getZombies().size() != toInfect)
		{
			Player alpha = Lobby.getInGame().get(new Random().nextInt(Lobby.getInGame().size()));

			if (Settings.PictureEnabled())
			{
				face = Pictures.getZombie();
				face[2] = face[2] + "     " + Msgs.Game_Alpha_You.getString();
				face[3] = face[3] + "     " + Msgs.Picture_Infected_To_Win.getString();

				alpha.sendMessage(face);
			} else
				alpha.sendMessage(Msgs.Game_Alpha_You.getString());

			InfPlayerManager.getInfPlayer(alpha).Infect();

			if (Settings.PictureEnabled())
			{
				face = Pictures.getHuman();
				face[2] = face[2] + "     " + Msgs.Picture_Survivor_You.getString();
				face[3] = face[3] + "     " + Msgs.Picture_Survivor_To_Win.getString();
			}
			for (Player u : Lobby.getInGame())
				if (u != alpha)
				{
					if (Settings.PictureEnabled())
						u.sendMessage(face);
					else
						u.sendMessage(Msgs.Game_Alpha_They.getString("<player>", alpha.getName()));
				}

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