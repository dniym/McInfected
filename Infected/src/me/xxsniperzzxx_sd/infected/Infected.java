
package me.xxsniperzzxx_sd.infected;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.GameMechanics.Reset;
import me.xxsniperzzxx_sd.infected.Main.GameState;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Infected {

	public static void filesSafeAllButConfig() {
		filesSaveArenas();
		filesSavePlayers();
		filesSaveKillTypes();
		filesSaveShop();
		filesSaveGrenades();
		filesSaveMessages();
		filesSaveAbilities();
		filesSaveClasses();
		filesSaveSigns();
	}

	public static void filesReloadAllButConfig() {
		filesReloadArenas();
		filesReloadPlayers();
		filesReloadKillTypes();
		filesReloadShop();
		filesReloadGrenades();
		filesReloadMessages();
		filesReloadAbilities();
		filesReloadClasses();
		filesReloadSigns();
	}

	public static void playerSaveShopInventory(Player player) {
		Methods.saveInventory(player, "Shop Inventory");
	}

	public static void playerAddToShopInventory(Player player, ItemStack items) {
		Methods.addItemToInventory(player, "Shop Inventory", items);
	}

	public static ItemStack[] playerGetShopInventory(Player player) {
		return Methods.getInventory(player, "Shop Inventory");
	}

	public static String playerGetGroup(Player player) {
		String group = null;
		if (isPlayerHuman(player))
			group = "Human";
		else if (isPlayerZombie(player))
			group = "Zombie";
		return group;
	}

	public static void clearZombie() {
		Main.zombies.clear();
	}

	public static ArrayList<String> listZombies() {
		return Main.zombies;
	}

	public static void clearHuman() {
		Main.humans.clear();
	}

	public static ArrayList<String> listHumans() {
		return Main.humans;
	}

	public static void clearInGame() {
		Main.inGame.clear();
	}

	public static ArrayList<String> listInGame() {
		return Main.inGame;
	}

	public static void clearInLobby() {
		Main.inLobby.clear();
	}

	public static ArrayList<String> listInLobby() {
		return Main.inLobby;
	}

	public static void addPlayerZombie(Player player) {
		Main.zombies.add(player.getName());
	}

	public static void addPlayerHuman(Player player) {
		Main.humans.add(player.getName());
	}

	public static void addPlayerInGame(Player player) {
		Main.inGame.add(player.getName());
	}

	public static void addPlayerInLobby(Player player) {
		Main.inLobby.add(player.getName());
	}

	public static void delPlayerZombie(Player player) {
		Main.zombies.remove(player.getName());
	}

	public static void delPlayerHuman(Player player) {
		Main.humans.remove(player.getName());
	}

	public static void delPlayerInGame(Player player) {
		Main.inGame.remove(player.getName());
	}

	public static void delPlayerInLobby(Player player) {
		Main.inLobby.remove(player.getName());
	}

	public static boolean isPlayerZombie(Player player) {
		return Main.zombies.contains(player.getName());
	}

	public static boolean isPlayerHuman(Player player) {
		return Main.humans.contains(player.getName());
	}

	public static boolean isPlayerInGame(Player player) {
		return Main.inGame.contains(player.getName());
	}

	public static boolean isPlayerInLobby(Player player) {
		return Main.inLobby.contains(player.getName());
	}

	public static GameState getGameState() {
		return Main.gameState;
	}

	public static void setGameState(GameState gs) {
		Main.gameState = gs;
	}

	public static String playerCreatingArena(Player player) {
		return Main.Creating.get(player);
	}

	public static String gameArenaPlayingIn() {
		return Main.playingin;
	}

	public static ArrayList<String> gameWinners() {
		return Main.Winners;
	}

	public static boolean gameisWinner(Player player) {
		return Main.Winners.contains(player.getName());
	}

	public static void gameDelWinners(Player player) {
		Main.Winners.remove(player.getName());
	}

	public static void gameClearWinners(Player player) {
		Main.Winners.clear();
	}

	public static void gameAddWinners(Player player) {
		Main.Winners.add(player.getName());
	}

	public static boolean playerHasPlayed(Player player) {
		return Files.getPlayers().contains(player.getName().toLowerCase());
	}

	public static void playerSetTime(Player player) {
		Methods.SetOnlineTime(player);
	}

	public static String playerGetTime(Player player) {
		return Methods.getOnlineTime(player.getName().toLowerCase());
	}

	public static void playerDelTime(Player player) {
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Time", null);
	}

	public static void playerSetKills(Player player, Integer kills) {
		Files.getPlayers().set("Players." + "Players." + player.getName().toLowerCase() + ".Kills", kills);
		Files.savePlayers();
	}

	public static int playerGetKills(Player player) {
		return Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
	}

	public static void playerDelKills(Player player) {
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Kills", null);
	}

	public static void playerSetDeaths(Player player, Integer deaths) {
		Files.getPlayers().set("Players." + "Players." + player.getName().toLowerCase() + ".Deaths", deaths);
		Files.savePlayers();
	}

	public static int playerGetDeaths(Player player) {
		return Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
	}

	public static void playerDelDeaths(Player player) {
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Deaths", null);
	}

	public static void playerSetPoints(Player player, Integer points, Integer price) {
		if (Main.config.getBoolean("Vault Support.Enable"))
		{
			Main.economy.withdrawPlayer(player.getName(), price);
		} else
		{
			Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Points", points - price);
			Files.savePlayers();
		}
	}

	public static int playerGetPoints(Player player) {
		if (Main.config.getBoolean("Vault Support.Enable"))
			return (int) Main.economy.getBalance(player.getName());
		else
			return Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Points");
	}

	public static void playerDelPoints(Player player) {
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Points", null);
		Files.savePlayers();
	}

	public static void playerSetScore(Player player, Integer score) {
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Score", score);
		Files.savePlayers();
	}

	public static int playerGetScore(Player player) {
		return Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Score");
	}

	public static void playerDelScore(Player player) {
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Score", null);
		Files.savePlayers();
	}

	public static void inventoryClearItems() {
		Main.Inventory.clear();
	}

	public static void inventorySetItems(Player player) {
		Main.Inventory.put(player.getName(), player.getInventory().getContents());
	}

	public static ItemStack[] inventoryDelItems(Player player) {
		return Main.Inventory.remove(player.getName());
	}

	public static ItemStack[] inventoryGetItems(Player player) {
		return Main.Inventory.get(player.getName());
	}

	public static void inventoryClearArmor() {
		Main.Armor.clear();
	}

	public static void inventorySetArmor(Player player) {
		Main.Armor.put(player.getName(), player.getInventory().getArmorContents());
	}

	public static ItemStack[] inventoryDelArmor(Player player) {
		return Main.Armor.remove(player.getName());
	}

	public static ItemStack[] inventoryGetArmor(Player player) {
		return Main.Armor.get(player.getName());
	}

	public static void playerClearHealth() {
		Main.Health.clear();
	}

	public static void playerSetHealth(Player player) {
		Main.Health.put(player.getName(), player.getHealth());
	}

	public static void playerDelHealth(Player player) {
		Main.Health.remove(player.getName());
	}

	public static double playergetHealth(Player player) {
		return Main.Health.get(player.getName());
	}

	public static void playerClearFoodlevel() {
		Main.Food.clear();
	}

	public static void playerSetFoodLevel(Player player) {
		Main.Food.put(player.getName(), player.getFoodLevel());
	}

	public static void playerDelFoodLevel(Player player) {
		Main.Food.remove(player.getName());
	}

	public static int playergetFoodLevel(Player player) {
		return Main.Food.get(player.getName());
	}

	public static void playerClearLastLocation() {
		Main.Spot.clear();
	}

	public static void playerSetLastLocation(Player player) {
		Main.Spot.put(player.getName(), player.getLocation());
	}

	public static void playerDelLastLocation(Player player) {
		Main.Spot.remove(player.getName());
	}

	public static int playergetLastLocation(Player player) {
		return Main.Food.get(player.getName());
	}

	public static Configuration filesGetPlayers() {
		return Files.getPlayers();
	}

	public static Configuration filesGetArenas() {
		return Files.getArenas();
	}

	public static Configuration filesGetKillTypes() {
		return Files.getKills();
	}

	public static Configuration filesGetShop() {
		return Files.getShop();
	}

	public static Configuration filesGetGrenades() {
		return Files.getGrenades();
	}

	public static Configuration filesGetClasses() {
		return Files.getClasses();
	}

	public static Configuration filesGetMessages() {
		return Files.getMessages();
	}

	public static Configuration filesGetAbilities() {
		return Files.getAbilities();
	}

	public static Configuration filesGetSigns() {
		return Files.getSigns();
	}

	public static void filesSaveAbilities() {
		Files.saveAbilities();
	}

	public static void filesSaveClasses() {
		Files.saveClasses();
	}

	public static void filesSaveMessages() {
		Files.saveMessages();
	}

	public static void filesSavePlayers() {
		Files.savePlayers();
	}

	public static void filesSaveArenas() {
		Files.saveArenas();
	}

	public static void filesSaveKillTypes() {
		Files.saveKills();
	}

	public static void filesSaveShop() {
		Files.saveShop();
	}

	public static void filesSaveGrenades() {
		Files.saveGrenades();
	}

	public static void filesSaveSigns() {
		Files.saveSigns();
	}

	public static void filesReloadClasses() {
		Files.reloadClasses();
	}

	public static void filesReloadSigns() {
		Files.reloadSigns();
	}

	public static void filesReloadAbilities() {
		Files.reloadPlayers();
	}

	public static void filesReloadPlayers() {
		Files.reloadPlayers();
	}

	public static void filesReloadShop() {
		Files.reloadShop();
	}

	public static void filesReloadGrenades() {
		Files.reloadGrenades();
	}

	public static void filesReloadMessages() {
		Files.reloadMessages();
	}

	public static void filesReloadArenas() {
		Files.reloadArenas();
	}

	public static void filesReloadKillTypes() {
		Files.reloadKills();
	}

	public static void resetPlayer(Player player) {
		Reset.resetp(player);
	}

	public static void resetPlugin() {
		Reset.reset();
	}

	public static void playersetLastHumanClass(Player player) {
		if (Main.humanClasses.get(player.getName()) == null || Main.humanClasses.get(player.getName()).equalsIgnoreCase("None"))
			filesGetPlayers().set("Players." + player.getName().toLowerCase() + ".Last Class.Human", "None");
		else
			filesGetPlayers().set("Players." + player.getName().toLowerCase() + ".Last Class.Human", Main.humanClasses.get(player.getName()));
		filesSavePlayers();
	}

	public static String playergetLastHumanClass(Player player) {
		if (filesGetPlayers().getString("Players." + player.getName().toLowerCase() + ".Last Class.Human") == null)
			return "None";
		else
			return filesGetPlayers().getString("Players." + player.getName().toLowerCase() + ".Last Class.Human");
	}

	public static void playersetLastZombieClass(Player player) {
		if (Main.zombieClasses.get(player.getName()) == null || Main.zombieClasses.get(player.getName()).equalsIgnoreCase("None"))
			filesGetPlayers().set("Players." + player.getName().toLowerCase() + ".Last Class.Zombie", "None");
		else
			filesGetPlayers().set("Players." + player.getName().toLowerCase() + ".Last Class.Zombie", Main.zombieClasses.get(player.getName()));
		filesSavePlayers();
	}

	public static String playergetLastZombieClass(Player player) {
		if (filesGetPlayers().getString("Players." + player.getName().toLowerCase() + ".Last Class.Zombie") == null)
			return "None";
		else
			return filesGetPlayers().getString("Players." + player.getName().toLowerCase() + ".Last Class.Zombie");
	}

	public static String playerGetLastDamage(Player player) {
		return Main.Lasthit.get(player.getName());
	}

	public static String playerDelLastDamage(Player player) {
		return Main.Lasthit.remove(player.getName());
	}

	public static boolean playerHasLastDamage(Player player) {
		return Main.Lasthit.containsKey(player.getName());
	}

	public static void arenaReset() {
		if (!Main.Blocks.isEmpty())
		{
			for (Location loc : Main.Blocks.keySet())
			{
				loc.getBlock().setType(Main.Blocks.get(loc));
			}
		}
		Main.Blocks.clear();

		// Clear Chests too
		if (!Main.Chests.isEmpty())
		{
			for (Location loc : Main.Chests.keySet())
			{
				if (loc.getBlock().getType() == Material.CHEST)
				{
					Chest chest = (Chest) loc.getBlock().getState();
					chest.getBlockInventory().setContents(Main.Chests.get(loc));
				}
			}
		}
	}
}