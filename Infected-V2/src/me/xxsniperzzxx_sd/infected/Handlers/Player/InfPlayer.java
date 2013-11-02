
package me.xxsniperzzxx_sd.infected.Handlers.Player;

import java.util.Random;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.Extras.ScoreBoard;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.PotionEffects;
import me.xxsniperzzxx_sd.infected.GameMechanics.Stats;
import me.xxsniperzzxx_sd.infected.Handlers.Arena.Arena;
import me.xxsniperzzxx_sd.infected.Handlers.Classes.InfClass;
import me.xxsniperzzxx_sd.infected.Handlers.Misc.LocationHandler;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kitteh.tag.TagAPI;


public class InfPlayer {

	private Player player;
	private Arena vote;
	String name;
	private int killstreak = 0;
	private long timeIn;
	private GameMode gamemode;
	private int level;
	private float exp;
	private double health;
	private int food;
	private ItemStack[] armor;
	private ItemStack[] inventory;
	private Location location;
	private String creating;
	private Player lastDamager;
	private InfClass humanClass;
	private InfClass zombieClass;
	private Team team = Team.None;
	private boolean isWinner = true;
	private boolean isInfChatting = false;

	public InfPlayer(Player p)
	{
		location = p.getLocation();
		name = p.getName();
		gamemode = p.getGameMode();
		level = p.getLevel();
		exp = p.getExp();
		health = p.getHealth();
		food = p.getFoodLevel();
		inventory = p.getInventory().getContents();
		armor = p.getInventory().getArmorContents();
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		player = p;
	}

	/**
	 * Saves: <li>Location 
	 * - Name 
	 * - Gamemode 
	 * - Level 
	 * - Exp - Health - Food -
	 * Inventory - Armor </li>
	 * Clears: <li>Armor - Inventory Sets: <li>Gamemode to
	 * adventure - Sets level to 0 - Sets the players exp to 0 - Sets the
	 * players health to 20 - Sets the food to 20
	 */
	public void setInfo() {
		location = player.getLocation();
		name = player.getName();
		gamemode = player.getGameMode();
		level = player.getLevel();
		exp = player.getExp();
		health = player.getHealth();
		food = player.getFoodLevel();
		inventory = player.getInventory().getContents();
		armor = player.getInventory().getArmorContents();
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);

		player.setGameMode(GameMode.ADVENTURE);
		player.setLevel(0);
		player.setExp(0.0F);
		player.setHealth(20);
		player.setFoodLevel(20);
	}

	/**
	 * Restores: - Inventory - Armor - Gamemode - Level - Exp - Health - Food -
	 * Location Resets: - Potion Effects - timeIn
	 */
	@SuppressWarnings("deprecation")
	public void leaveInfected() {
		Player p = Bukkit.getPlayerExact(name);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setGameMode(gamemode);
		p.setLevel(level);
		p.setExp(exp);
		p.setHealth(health);
		p.setFoodLevel(food);
		p.getInventory().setContents(inventory);
		p.getInventory().setArmorContents(armor);
		p.updateInventory();
		p.setFallDistance(0);
		p.teleport(location);
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		killstreak = 0;
		location = null;
		gamemode = null;
		level = 0;
		exp = 0;
		health = 20;
		food = 20;
		inventory = null;
		armor = null;
		vote = null;
		timeIn = 0;
	}

	/**
	 * Disguises the player
	 */
	public void disguise() {

		Disguises.disguisePlayer(player);
	}

	/**
	 * Undisguises the player
	 */
	public void unDisguise() {
		if (Main.config.getBoolean("Disguise Support.Enabled"))
			if (Disguises.isPlayerDisguised(player))
				Disguises.unDisguisePlayer(player);
	}

	/**
	 * - Updates Scoreboard - Play a sound - Set level to 0 - Set exp to 0 -
	 * Teleport to the lobby - Set gamemode - Remove Fire - Set health to 20 -
	 * Set Food to 20 - Undisguise the player
	 */
	public void tpToLobby() {

		ScoreBoard.updateScoreBoard();
		player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
		player.setLevel(0);
		player.setExp(0.0F);
		player.teleport(Main.Lobby.getLocation());
		player.setGameMode(GameMode.ADVENTURE);
		player.setFireTicks(0);
		player.setHealth(20.0);
		player.setFoodLevel(20);

		unDisguise();
		killstreak = 0;
		vote = null;
		lastDamager = null;
		isWinner = true;

	}

	/**
	 * - Set health and food to 20 - Remove Fire Ticks - Teleport to new spawn -
	 * Clear potion effects
	 */
	public void respawn() {
		Player p = player;
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		Random r = new Random();
		int i = r.nextInt(Main.Lobby.getActiveArena().getSpawns().size());
		String loc = Main.Lobby.getActiveArena().getSpawns().get(i);

		p.teleport(LocationHandler.getPlayerLocation(loc));

		if (Main.config.getBoolean("TagAPI Support.Enable"))
			TagAPI.refreshPlayer(player);

		for (PotionEffect reffect : player.getActivePotionEffects())
			player.removePotionEffect(reffect.getType());

		p.setFallDistance(0F);
		lastDamager = null;
	}

	/**
	 * Change team to Zombie Set winner to false Change their equipment to
	 * zombie apply potion effects disguise update scoreboard apply confussion
	 */
	public void Infect() {
		player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
		player.sendMessage(Main.I + "You have become infected!");
		team = Team.Zombie;
		isWinner = false;
		Equip.equipToZombie(player);
		PotionEffects.applyClassEffects(player);
		disguise();
		ScoreBoard.updateScoreBoard();
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20,
				2));
	}

	public long getPlayingTime() {
		return (System.currentTimeMillis() / 1000) - getTimeIn();
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the points
	 */
	public int getPoints() {
		return Stats.getPoints(name);
	}

	/**
	 * @param points
	 *            the points to set
	 */
	public void setPoints(int points, boolean useVault) {
		Stats.setPoints(name, points, useVault);
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return Stats.getScore(name);
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		Stats.setScore(name, score);
	}

	/**
	 * @return the killstreak
	 */
	public int getKillstreak() {
		return killstreak;
	}

	/**
	 * @param killstreak
	 *            the killstreak to set
	 */
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}

	/**
	 * @return the timeJoined
	 */
	public long getTimeIn() {
		return timeIn;
	}

	/**
	 * @param timeIn
	 *            the timeIn to set
	 */
	public void setTimeIn(long timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * @return the gamemode
	 */
	public GameMode getGamemode() {
		return gamemode;
	}

	/**
	 * @param gamemode
	 *            the gamemode to set
	 */
	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the exp
	 */
	public float getExp() {
		return exp;
	}

	/**
	 * @param exp
	 *            the exp to set
	 */
	public void setExp(float exp) {
		this.exp = exp;
	}

	/**
	 * @return the health
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * @param health
	 *            the health to set
	 */
	public void setHealth(double health) {
		this.health = health;
	}

	/**
	 * @return the food
	 */
	public int getFood() {
		return food;
	}

	/**
	 * @param food
	 *            the food to set
	 */
	public void setFood(int food) {
		this.food = food;
	}

	/**
	 * @return the armor
	 */
	public ItemStack[] getArmor() {
		return armor;
	}

	/**
	 * @param armor
	 *            the armor to set
	 */
	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	/**
	 * @return the inventory
	 */
	public ItemStack[] getInventory() {
		return inventory;
	}

	/**
	 * @param inventory
	 *            the inventory to set
	 */
	public void setInventory(ItemStack[] inventory) {
		this.inventory = inventory;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the creating
	 */
	public String getCreating() {
		return creating;
	}

	/**
	 * @param creating
	 *            the creating to set
	 */
	public void setCreating(String creating) {
		this.creating = creating;
	}

	/**
	 * @return the lastDamager
	 */
	public Player getLastDamager() {
		return lastDamager;
	}

	/**
	 * @param lastDamager
	 *            the lastDamager to set
	 */
	public void setLastDamager(Player lastDamager) {
		this.lastDamager = lastDamager;
	}

	/**
	 * @return the kills
	 */
	public int getKills() {
		return Stats.getKills(name);
	}

	/**
	 * @param kills
	 *            the kills to set
	 */
	public void setKills(int kills) {
		Stats.setKills(name, kills);
	}

	/**
	 * @return the deaths
	 */
	public int getDeaths() {
		return Stats.getDeaths(name);
	}

	/**
	 * @param deaths
	 *            the deaths to set
	 */
	public void setDeaths(int deaths) {
		this.setDeaths(deaths);
	}

	/**
	 * @return the infClass
	 */
	public InfClass getInfClass(Team team) {
		if (team == Team.Human)
			return humanClass;
		else
			return zombieClass;
	}

	/**
	 * @param infClass
	 *            the infClass to set
	 */
	public void setInfClass(Team team, InfClass Class) {
		if (team == Team.Human)
			humanClass = Class;
		else
			zombieClass = Class;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return the vote
	 */
	public Arena getVote() {
		return vote;
	}

	/**
	 * @param vote
	 *            the vote to set
	 */
	public void setVote(Arena vote) {
		this.vote = vote;
	}

	public void updateStats(int kills, int deaths) {
		if (kills != 0)
			Stats.setKills(name, Stats.getKills(name) + kills);
		if (deaths != 0)
			Stats.setDeaths(name, Stats.getDeaths(name) + deaths);
	}

	/**
	 * @return the isWinner
	 */
	public boolean isWinner() {
		return isWinner;
	}

	/**
	 * @param isWinner
	 *            the isWinner to set
	 */
	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	/**
	 * @return the isInfChatting
	 */
	public boolean isInfChatting() {
		return isInfChatting;
	}

	/**
	 * @param isInfChatting
	 *            the isInfChatting to set
	 */
	public void setInfChatting(boolean isInfChatting) {
		this.isInfChatting = isInfChatting;
	}

}
