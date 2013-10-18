
package me.xxsniperzzxx_sd.infected;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.GameMechanics.Reset;
import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.Enums.Teams;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.InventoryHandler;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.TimeHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Infected {

	public static void filesSafeAll() {
		filesSaveArenas();
		filesSavePlayers();
		filesSaveKillTypes();
		filesSaveShop();
		filesSaveGrenades();
		filesSaveMessages();
		filesSaveAbilities();
		filesSaveClasses();
		filesSaveSigns();
		Main.me.saveConfig();
	}

	public static void filesReloadAll() {
		filesReloadArenas();
		filesReloadPlayers();
		filesReloadKillTypes();
		filesReloadShop();
		filesReloadGrenades();
		filesReloadMessages();
		filesReloadAbilities();
		filesReloadClasses();
		filesReloadSigns();
		Main.me.reloadConfig();
	}

	public static void playerSaveShopInventory(Player player) {
		InventoryHandler.saveInventory(player, "Shop Inventory");
	}

	public static void playerAddToShopInventory(Player player, ItemStack items) {
		InventoryHandler.addItemToInventory(player, "Shop Inventory", items);
	}

	public static ItemStack[] playerGetShopInventory(Player player) {
		return InventoryHandler.getInventory(player, "Shop Inventory");
	}

	public static Teams playerGetGroup(Player player) {
		Teams teams = Teams.Other;
		if (isPlayerHuman(player))
			teams = Teams.Human;
		else if (isPlayerZombie(player))
			teams = Teams.Zombie;
		return teams;
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

	public static boolean playerHasPlayed(String name) {
		return Files.getPlayers().contains(name.toLowerCase());
	}

	public static void playerSetTime(String name) {
		TimeHandler.SetOnlineTime(name);
	}

	public static String playerGetTime(String name) {
		return TimeHandler.getOnlineTime(name.toLowerCase());
	}

	public static void playerDelTime(String name) {
		Files.getPlayers().set("Players." + name.toLowerCase() + ".Time", null);
	}

	public static void playerSetKills(String name, Integer kills) {
		Files.getPlayers().set("Players." + "Players." + name.toLowerCase() + ".Kills", kills);
		Files.savePlayers();
	}

	public static int playerGetKills(String name) {
		return Files.getPlayers().getInt("Players." + name.toLowerCase() + ".Kills");
	}

	public static void playerDelKills(String name) {
		Files.getPlayers().set("Players." + name.toLowerCase() + ".Kills", null);
	}

	public static void playerSetDeaths(String name, Integer deaths) {
		Files.getPlayers().set("Players." + "Players." + name.toLowerCase() + ".Deaths", deaths);
		Files.savePlayers();
	}

	public static int playerGetDeaths(String name) {
		return Files.getPlayers().getInt("Players." + name.toLowerCase() + ".Deaths");
	}

	public static void playerDelDeaths(String name) {
		Files.getPlayers().set("Players." + name.toLowerCase() + ".Deaths", null);
	}

	public static void playerSetPoints(String name, Integer points, Integer price) {
		if (Main.config.getBoolean("Vault Support.Enable"))
		{
			Main.economy.withdrawPlayer(name, price);
		} else
		{
			Files.getPlayers().set("Players." + name.toLowerCase() + ".Points", points - price);
			Files.savePlayers();
		}
	}

	public static int playerGetPoints(String name) {
		if (Main.config.getBoolean("Vault Support.Enable"))
			return (int) Main.economy.getBalance(name);
		else
			return Files.getPlayers().getInt("Players." + name.toLowerCase() + ".Points");
	}

	public static void playerDelPoints(String name) {
		Files.getPlayers().set("Players." + name.toLowerCase() + ".Points", null);
		Files.savePlayers();
	}

	public static void playerSetScore(String name, Integer score) {
		Files.getPlayers().set("Players." + name.toLowerCase() + ".Score", score);
		Files.savePlayers();
	}

	public static int playerGetScore(String name) {
		return Files.getPlayers().getInt("Players." + name.toLowerCase() + ".Score");
	}

	public static void playerDelScore(String name) {
		Files.getPlayers().set("Players." + name.toLowerCase() + ".Score", null);
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
		Main.Spot.put(player.getName(), LocationHandler.getLocationToString(player.getLocation()));
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

	public static void playersetLastHumanClass(Player player, String classname) {
		filesGetPlayers().set("Players." + player.getName().toLowerCase() + ".Last Class.Human", classname);
		filesSavePlayers();
	}

	public static String playergetLastHumanClass(Player player) {
		if (filesGetPlayers().getString("Players." + player.getName().toLowerCase() + ".Last Class.Human") == null)
			return "None";
		else
			return filesGetPlayers().getString("Players." + player.getName().toLowerCase() + ".Last Class.Human");
	}

	public static void playersetLastZombieClass(Player player , String classname) {
		filesGetPlayers().set("Players." + player.getName().toLowerCase() + ".Last Class.Zombie", classname);
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

	public static String playergetHumanClass(Player player){
		return Main.humanClasses.get(player.getName());
	}
	public static String playergetZombieClass(Player player){
		return Main.zombieClasses.get(player.getName());
	}
	public static boolean playerhasHumanClass(Player player){
		return Main.humanClasses.containsKey(player.getName());
	}
	public static boolean playerhasZombieClass(Player player){
		return Main.zombieClasses.containsKey(player.getName());
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