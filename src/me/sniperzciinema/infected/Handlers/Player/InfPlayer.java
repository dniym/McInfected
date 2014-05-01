
package me.sniperzciinema.infected.Handlers.Player;

import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import me.sniperzciinema.infected.Disguise.Disguises;
import me.sniperzciinema.infected.Events.InfectedLeaveEvent;
import me.sniperzciinema.infected.Extras.ScoreBoard;
import me.sniperzciinema.infected.GameMechanics.Equip;
import me.sniperzciinema.infected.GameMechanics.Game;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Classes.InfClass;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Items.SaveItemHandler;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Potions.PotionEffects;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.IconMenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;


public class InfPlayer {
	
	private Player			player;
	private UUID				uuid;
	private Arena				vote;
	private String			name;
	private int					killstreak		= 0;
	private long				timeIn;
	private GameMode		gamemode;
	private int					level;
	private float				exp;
	private double			health;
	private int					food;
	private ItemStack[]	armor;
	private ItemStack[]	inventory;
	private Location		location;
	private String			creating;
	private Player			lastDamager;
	private InfClass		humanClass;
	private InfClass		zombieClass;
	private Team				team					= Team.Human;
	private boolean			isWinner			= true;
	private boolean			isInfChatting	= false;
	private ScoreBoard	ScoreBoard		= new ScoreBoard(this);
	
	public InfPlayer(Player p)
	{
		setPlayer(p);
		setName(p.getName());
		setUuid(p.getUniqueId());
		
	}
	
	/**
	 * Clears the players armor and inventory
	 */
	@SuppressWarnings("deprecation")
	public void clearEquipment() {
		this.player.getInventory().clear();
		this.player.getInventory().setArmorContents(null);
		this.player.updateInventory();
	}
	
	/**
	 * Disguises the player
	 */
	public void disguise() {
		if (Settings.DisguisesEnabled())
			if (!Disguises.isPlayerDisguised(this.player))
				Disguises.disguisePlayer(this.player);
	}
	
	/**
	 * @return how many votes they have
	 */
	public int getAllowedVotes() {
		int votes = 1;
		
		for (Entry<String, Integer> node : Settings.getExtraVoteNodes().entrySet())
			if (this.player.hasPermission("Infected.vote." + node.getKey()) && (node.getValue() > votes))
				votes = node.getValue();
		return votes;
	}
	
	/**
	 * @return the armor
	 */
	public ItemStack[] getArmor() {
		return this.armor;
	}
	
	/**
	 * @return the creating
	 */
	public String getCreating() {
		return this.creating;
	}
	
	/**
	 * @return the deaths
	 */
	public int getDeaths() {
		return Stats.getDeaths(this.uuid);
	}
	
	/**
	 * @return the exp
	 */
	public float getExp() {
		return this.exp;
	}
	
	/**
	 * @return the food
	 */
	public int getFood() {
		return this.food;
	}
	
	/**
	 * @return the gamemode
	 */
	public GameMode getGamemode() {
		return this.gamemode;
	}
	
	/**
	 * @return the health
	 */
	public double getHealth() {
		return this.health;
	}
	
	public int getHighestKillStreak() {
		return Stats.getHighestKillStreak(this.uuid);
	}
	
	/**
	 * @return the infClass
	 */
	public InfClass getInfClass(Team team) {
		if (team == Team.Human)
			return this.humanClass;
		else
			return this.zombieClass;
	}
	
	/**
	 * @return the inventory
	 */
	public ItemStack[] getInventory() {
		return this.inventory;
	}
	
	/**
	 * @return the kills
	 */
	public int getKills() {
		return Stats.getKills(this.uuid);
	}
	
	/**
	 * @return the killstreak
	 */
	public int getKillstreak() {
		return this.killstreak;
	}
	
	/**
	 * @return the lastDamager
	 */
	public Player getLastDamager() {
		return this.lastDamager;
	}
	
	/**
	 * @return the level
	 */
	public int getLevel() {
		return this.level;
	}
	
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return this.location;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	public long getPlayingTime() {
		return (System.currentTimeMillis() / 1000) - getTimeIn();
	}
	
	/**
	 * @return the points
	 */
	public int getPoints(boolean useVault) {
		return Stats.getPoints(this.uuid, useVault);
	}
	
	/**
	 * @return how many votes they have
	 */
	public int getPointsModifier() {
		int points = 1;
		
		for (Entry<String, Integer> node : Settings.getPointsModifiers().entrySet())
			if (this.player.hasPermission("Infected.points." + node.getKey()) && (node.getValue() > points))
				points = node.getValue();
		return points;
	}
	
	/**
	 * @return the score
	 */
	public int getScore() {
		return Stats.getScore(this.uuid);
	}
	
	public ScoreBoard getScoreBoard() {
		return this.ScoreBoard;
	}
	
	/**
	 * @return how many votes they have
	 */
	public int getScoreModifier() {
		int score = 1;
		
		for (Entry<String, Integer> node : Settings.getScoreModifiers().entrySet())
			if (this.player.hasPermission("Infected.points." + node.getKey()) && (node.getValue() > score))
				score = node.getValue();
		return score;
	}
	
	/**
	 * @return the team
	 */
	public Team getTeam() {
		return this.team;
	}
	
	/**
	 * @return the timeJoined
	 */
	public long getTimeIn() {
		return this.timeIn;
	}
	
	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return this.uuid;
	}
	
	/**
	 * @return the vote
	 */
	public Arena getVote() {
		return this.vote;
	}
	
	/**
	 * Change team to Zombie Set winner to false Change their equipment to
	 * zombie apply potion effects disguise update scoreboard apply confussion
	 */
	public void Infect() {
		this.player.setHealth(20.0);
		this.player.setFoodLevel(20);
		this.player.setFireTicks(0);
		this.player.playSound(this.player.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
		this.team = Team.Zombie;
		this.isWinner = false;
		Equip.equipToZombie(this.player);
		for (PotionEffect reffect : this.player.getActivePotionEffects())
			this.player.removePotionEffect(reffect.getType());
		
		PotionEffects.applyClassEffects(this.player);
		disguise();
		
		getScoreBoard().showProperBoard();
		this.player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 2));
	}
	
	/**
	 * @return the isInfChatting
	 */
	public boolean isInfChatting() {
		return this.isInfChatting;
	}
	
	public boolean isInGame() {
		return Lobby.isInGame(this.player);
	}
	
	/**
	 * @return the isWinner
	 */
	public boolean isWinner() {
		return this.isWinner;
	}
	
	/**
	 * Restores: - Inventory - Armor - Gamemode - Level - Exp - Health - Food -
	 * Location Resets: - Potion Effects - timeIn
	 */
	@SuppressWarnings("deprecation")
	public void leaveInfected() {
		
		InfectedLeaveEvent le = new InfectedLeaveEvent(this.player);
		Bukkit.getPluginManager().callEvent(le);
		if (!le.isCancelled())
		{
			unDisguise();
			clearEquipment();
			
			Player p = getPlayer();
			p.setGameMode(this.gamemode);
			p.setLevel(this.level);
			p.setExp(this.exp);
			p.setHealth(this.health);
			p.setFoodLevel(this.food);
			p.setFireTicks(0);
			p.getInventory().setContents(this.inventory);
			p.getInventory().setArmorContents(this.armor);
			p.updateInventory();
			
			for (PotionEffect effect : this.player.getActivePotionEffects())
				this.player.removePotionEffect(effect.getType());
			
			this.player.setFallDistance(0F);
			if (Lobby.getLeave() != null)
				p.teleport(Lobby.getLeave());
			else
				p.teleport(this.location);
			p.setFallDistance(0F);
			setTeam(Team.Human);
			Lobby.delPlayerInGame(this.player);
			
			if (Lobby.getGameState() == GameState.Started)
				Stats.setPlayingTime(this.uuid, Stats.getPlayingTime(this.uuid) + getPlayingTime());
			
			if ((getVote() != null) && ((Lobby.getGameState() == GameState.InLobby) || (Lobby.getGameState() == GameState.Voting)))
				getVote().setVotes(getVote().getVotes() - getAllowedVotes());
			
			this.killstreak = 0;
			this.location = null;
			this.gamemode = null;
			this.level = 0;
			this.exp = 0;
			this.health = 20;
			this.food = 20;
			this.inventory = null;
			this.armor = null;
			this.vote = null;
			this.timeIn = 0;
			setInfChatting(false);
			
			this.player.sendMessage(Msgs.Game_Left_You.getString());
			
			manageLeaving();
			
			for (Player u : Lobby.getPlayersInGame())
				if (u.getUniqueId() != this.uuid)
					InfPlayerManager.getInfPlayer(u).getScoreBoard().showProperBoard();
			
			this.player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
	}
	
	public void manageLeaving() {
		
		// Is there anyone left in the lobby?
		if (Lobby.getPlayersInGame().size() == 0)
			Lobby.reset();
		
		// If nothing has started yet, just inform players they left
		else if (Lobby.getGameState() == GameState.InLobby)
			for (Player u : Lobby.getPlayersInGame())
				u.sendMessage(Msgs.Game_Left_They.getString("<player>", this.player.getName()));
		
		// If the game isn't fully started yet, this includes Voting, and
		// before
		// and Infecteds chosen
		else if ((Lobby.getGameState() == GameState.Voting) || (Lobby.getGameState() == GameState.Infecting))
		{
			
			for (Player u : Lobby.getPlayersInGame())
				u.sendMessage(Msgs.Game_Left_They.getString("<player>", this.player.getName()));
			
			// If theres only one person left in the lobby, end the game
			if (Lobby.getPlayersInGame().size() <= 1)
			{
				Lobby.setGameState(GameState.InLobby);
				for (Player u : Lobby.getPlayersInGame())
				{
					u.sendMessage(Msgs.Game_End_Not_Enough_Players.getString());
					InfPlayerManager.getInfPlayer(u).tpToLobby();
				}
				
				// Reset all the timers, lists, etc(Not including the
				// ones for
				// people in Infected)
				Lobby.reset();
			}
		}
		
		// If the game has fully started
		else if (Lobby.getGameState() == GameState.Started)
		{
			for (Player u : Lobby.getPlayersInGame())
				u.sendMessage(Msgs.Game_Left_They.getString("<player>", this.player.getName()));
			
			// If theres only one person left in the lobby, end the
			// game
			if (Lobby.getPlayersInGame().size() <= 1)
			{
				Lobby.setGameState(GameState.InLobby);
				for (Player u : Lobby.getPlayersInGame())
				{
					u.sendMessage(Msgs.Game_End_Not_Enough_Players.getString());
					InfPlayerManager.getInfPlayer(u).tpToLobby();
				}
				
				// Reset all the timers, lists, etc(Not including
				// the ones for
				// people in Infected)
				Lobby.reset();
			}
			
			// If theres no zombies left
			else if (Lobby.getTeam(Team.Zombie).size() == 0)
				// Choose someone new to be the zombie
				Game.chooseAlphas();
			
			// If theres no humans left(Player who left was
			// human, or the new
			// zombie was the only human)
			else if (Lobby.getTeam(Team.Human).size() == 0)
			{
				Lobby.setGameState(GameState.InLobby);
				for (Player u : Lobby.getPlayersInGame())
				{
					u.sendMessage(Msgs.Game_End_Not_Enough_Players.getString());
					InfPlayerManager.getInfPlayer(u).tpToLobby();
				}
				
				// Reset all the timers, lists, etc(Not
				// including the ones for
				// people in Infected)
				Lobby.reset();
			}
			
		}
	}
	
	public void openMenu(Player player, IconMenu menu) {
		menu.open(player);
	}
	
	/**
	 * - Set health and food to 20 - Remove Fire Ticks - Teleport to new spawn -
	 * Clear potion effects
	 */
	public void respawn() {
		getScoreBoard().showProperBoard();
		Player p = this.player;
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		Random r = new Random();
		int i = r.nextInt(Lobby.getActiveArena().getSpawns(this.team).size());
		String loc = Lobby.getActiveArena().getSpawns(this.team).get(i);
		
		p.setFallDistance(0F);
		p.teleport(LocationHandler.getPlayerLocation(loc));
		p.setFallDistance(0F);
		
		for (PotionEffect reffect : this.player.getActivePotionEffects())
			this.player.removePotionEffect(reffect.getType());
		
		PotionEffects.applyClassEffects(this.player);
		
		p.setFallDistance(0F);
		this.lastDamager = null;
	}
	
	/**
	 * @param armor
	 *          the armor to set
	 */
	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}
	
	/**
	 * @param creating
	 *          the creating to set
	 */
	public void setCreating(String creating) {
		this.creating = creating;
	}
	
	/**
	 * @param deaths
	 *          the deaths to set
	 */
	public void setDeaths(int deaths) {
		setDeaths(deaths);
	}
	
	/**
	 * @param exp
	 *          the exp to set
	 */
	public void setExp(float exp) {
		this.exp = exp;
	}
	
	/**
	 * @param food
	 *          the food to set
	 */
	public void setFood(int food) {
		this.food = food;
	}
	
	/**
	 * @param gamemode
	 *          the gamemode to set
	 */
	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}
	
	/**
	 * @param health
	 *          the health to set
	 */
	public void setHealth(double health) {
		this.health = health;
	}
	
	/**
	 * @param isInfChatting
	 *          the isInfChatting to set
	 */
	public void setInfChatting(boolean isInfChatting) {
		this.isInfChatting = isInfChatting;
	}
	
	/**
	 * @param infClass
	 *          the infClass to set
	 */
	public void setInfClass(Team team, InfClass Class) {
		if (team == Team.Human)
			this.humanClass = Class;
		else
			this.zombieClass = Class;
	}
	
	/**
	 * Saves: <li>Location - Name - Gamemode - Level - Exp - Health - Food -
	 * Inventory - Armor</li> Clears: <li>Armor - Inventory Sets: <li>Gamemode to
	 * adventure - Sets level to 0 - Sets the players exp to 0 - Sets the players
	 * health to 20 - Sets the food to 20
	 */
	public void setInfo() {
		this.location = this.player.getLocation();
		this.name = this.player.getName();
		this.gamemode = this.player.getGameMode();
		this.level = this.player.getLevel();
		this.exp = this.player.getExp();
		this.health = this.player.getHealth();
		this.food = this.player.getFoodLevel();
		this.inventory = this.player.getInventory().getContents();
		this.armor = this.player.getInventory().getArmorContents();
		clearEquipment();
		setInfClass(Team.Human, InfClassManager.getDefaultClass(Team.Human));
		setInfClass(Team.Zombie, InfClassManager.getDefaultClass(Team.Zombie));
		
		this.player.setGameMode(GameMode.ADVENTURE);
		this.player.setLevel(0);
		this.player.setExp(0.0F);
		this.player.setHealth(20);
		this.player.setFoodLevel(20);
	}
	
	/**
	 * @param inventory
	 *          the inventory to set
	 */
	public void setInventory(ItemStack[] inventory) {
		this.inventory = inventory;
	}
	
	/**
	 * @param kills
	 *          the kills to set
	 */
	public void setKills(int kills) {
		Stats.setKills(this.uuid, kills);
	}
	
	/**
	 * @param killstreak
	 *          the killstreak to set
	 */
	public void setKillstreak(int killstreak) {
		this.killstreak = killstreak;
	}
	
	/**
	 * @param lastDamager
	 *          the lastDamager to set
	 */
	public void setLastDamager(Player lastDamager) {
		this.lastDamager = lastDamager;
	}
	
	/**
	 * @param level
	 *          the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * @param location
	 *          the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param player
	 *          the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * @param points
	 *          the points to set
	 */
	public void setPoints(int points, boolean useVault) {
		Stats.setPoints(this.uuid, points, useVault);
	}
	
	/**
	 * @param score
	 *          the score to set
	 */
	public void setScore(int score) {
		Stats.setScore(this.uuid, score);
	}
	
	/**
	 * @param team
	 *          the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}
	
	/**
	 * @param timeIn
	 *          the timeIn to set
	 */
	public void setTimeIn(long timeIn) {
		this.timeIn = timeIn;
	}
	
	/**
	 * @param uuid
	 *          the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * @param vote
	 *          the vote to set
	 */
	public void setVote(Arena vote) {
		this.vote = vote;
	}
	
	/**
	 * @param isWinner
	 *          the isWinner to set
	 */
	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
	
	/**
	 * - Updates Scoreboard - Play a sound - Set level to 0 - Set exp to 0 -
	 * Teleport to the lobby - Set gamemode - Remove Fire - Set health to 20 -
	 * Set Food to 20 - Undisguise the player
	 */
	public void tpToLobby() {
		clearEquipment();
		setTeam(Team.Human);
		
		this.player.setFlying(false);
		
		this.player.playSound(this.player.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
		this.player.setLevel(0);
		this.player.setExp(0.0F);
		this.player.setFallDistance(0F);
		this.player.teleport(Lobby.getLocation());
		this.player.setFallDistance(0F);
		this.player.setGameMode(GameMode.ADVENTURE);
		this.player.setFireTicks(0);
		this.player.setHealth(20.0);
		this.player.setFoodLevel(20);
		
		for (PotionEffect effect : this.player.getActivePotionEffects())
			this.player.removePotionEffect(effect.getType());
		
		if (!SaveItemHandler.getSavedItems(this.player).isEmpty())
			try
			{
				for (String string : SaveItemHandler.getSavedItems(this.player))
					this.player.getInventory().addItem(ItemHandler.getItemStack(string));
				
			}
			catch (Exception e)
			{
				this.player.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + ChatColor.BOLD + "Tell an Admin that your saved inventory is invalid!");
			}
		
		unDisguise();
		this.killstreak = 0;
		this.vote = null;
		this.lastDamager = null;
		this.isWinner = true;
		
		getScoreBoard().showProperBoard();
	}
	
	/**
	 * Undisguises the player
	 */
	public void unDisguise() {
		if (Settings.DisguisesEnabled())
			if (Disguises.isPlayerDisguised(this.player))
				Disguises.unDisguisePlayer(this.player);
	}
	
	public void updateStats(int kills, int deaths) {
		if (kills != 0)
			Stats.setKills(this.uuid, Stats.getKills(this.uuid) + kills);
		if (deaths != 0)
			Stats.setDeaths(this.uuid, Stats.getDeaths(this.uuid) + deaths);
	}
	
}
