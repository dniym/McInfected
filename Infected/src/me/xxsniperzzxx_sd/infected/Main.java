package me.xxsniperzzxx_sd.infected;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.xxsniperzzxx_sd.infected.Listeners.CrackShotApi;
import me.xxsniperzzxx_sd.infected.Listeners.GrenadeListener;
import me.xxsniperzzxx_sd.infected.Listeners.PlayerListener;
import me.xxsniperzzxx_sd.infected.Listeners.SignListener;
import me.xxsniperzzxx_sd.infected.Listeners.TagApi;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.ItemSerialization;
import me.xxsniperzzxx_sd.infected.Tools.Metrics;
import me.xxsniperzzxx_sd.infected.Tools.TeleportFix;
import me.xxsniperzzxx_sd.infected.Tools.Updater;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class Main extends JavaPlugin {

	// Initialize all the variables
	public static String bVersion = "1.6.2";
	public static int currentTime = 0;
	public static String v = null;
	public static enum GameState {INLOBBY, VOTING, BEFOREINFECTED, STARTED, GAMEOVER, DISABLED}; 
	public static GameState gameState = GameState.INLOBBY;

	// Lists, Strings and Integers Infected needs
	public static int arenaNumber = 0;
	public static ArrayList<String> infectedChat = new ArrayList<String>();
	public static ArrayList<String> possibleArenas = new ArrayList<String>();
	public static ArrayList<String> possibleArenasU = new ArrayList<String>();
	public static ArrayList<String> Winners = new ArrayList<String>();
	public static ArrayList<Integer> Score = new ArrayList<Integer>();
	public static ArrayList<Integer> Grenades = new ArrayList<Integer>();
	public static ArrayList<String> inLobby = new ArrayList<String>();
	public static ArrayList<String> zombies = new ArrayList<String>();
	public static ArrayList<String> humans = new ArrayList<String>();
	public static ArrayList<String> inGame = new ArrayList<String>();
	public static HashMap<String, String> humanClasses = new HashMap<String, String>();
	public static HashMap<String, String> zombieClasses = new HashMap<String, String>();
	public static HashMap<String, Integer> Leaders = new HashMap<String, Integer>();
	public static HashMap<String, Long> Timein = new HashMap<String, Long>();
	public static HashMap<String, String> Lasthit = new HashMap<String, String>();
	public static HashMap<String, String> Voted4 = new HashMap<String, String>();
	public static HashMap<String, String> Creating = new HashMap<String, String>();
	public static HashMap<String, Integer> Votes = new HashMap<String, Integer>();
	public static HashMap<String, Integer> KillStreaks = new HashMap<String, Integer>();
	
	public static HashMap<String, String> gamemode = new HashMap<String, String>();
	public static HashMap<String, Integer> Levels = new HashMap<String, Integer>();
	public static HashMap<String, Float> Exp = new HashMap<String, Float>();
	public static HashMap<String, Double> Health = new HashMap<String, Double>();
	public static HashMap<String, Integer> Food = new HashMap<String, Integer>();
	public static HashMap<String, ItemStack[]> Armor = new HashMap<String, ItemStack[]>();
	public static HashMap<String, ItemStack[]> Inventory = new HashMap<String, ItemStack[]>();
	

	public static HashMap<Location, Material> Blocks = new HashMap<Location, Material>();
	public static HashMap<Location, ItemStack[]> Chests = new HashMap<Location, ItemStack[]>();
	
	public static String I = ChatColor.DARK_RED + "" + "«†" + ChatColor.RESET + ChatColor.DARK_RED + "Infected" + ChatColor.DARK_RED + "†»" + ChatColor.RESET + ChatColor.GRAY + " ";
	public static HashMap<String, Location> Spot = new HashMap<String, Location>();
	public static String playingin = null;
	public static int timestart;
	public static int queuedtpback;
	public static int queuedtp;
	public static int voteTime;
	public static int Wait;
	public static int GtimeLimit;
	public static int timedCycle;
	public static int timeLimit;
	public static int timeVote;
	public static int timest;
	public static int lastscore;
	public static boolean update = false;
	public static String name = "";
	public static String leader = "";
	public static Plugin me;
	public static File file;
	public static String BV = null;

	public static Configuration config = null;
	public String currentBukkitVersion = null;
	public String updateBukkitVersion = null;

	// Item Serializer class
	public static ItemSerialization is = null;

	// Scoreboard
	private ScoreboardManager manager;
	public static Scoreboard voteBoard;
	public static Scoreboard playingBoard;
	public static Objective voteList;
	public static Objective playingList;

	// Plugin Addons
	public static DisguiseCraftAPI dcAPI;
	public static Economy economy = null;

	// public NamedItemStack NIS;

	@Override
	public void onEnable() {

		// Setup the scoreboard
		manager = Bukkit.getScoreboardManager();
		Main.voteBoard = manager.getNewScoreboard();
		Main.playingBoard = manager.getNewScoreboard();
		Main.playingList = Main.playingBoard.registerNewObjective("playing", "dummy");
		Main.voteList = Main.voteBoard.registerNewObjective("votes", "dummy");

		// Item Serialization
		Main.is = new ItemSerialization();

		// Create Configs and files
		Infected.filesGetArenas().options().copyDefaults(true);
		Infected.filesGetKillTypes().options().copyDefaults(true);
		getConfig().options().copyDefaults(true);
		Infected.filesGetShop().options().copyDefaults(true);
		Infected.filesGetPlayers().options().copyDefaults(true);
		Infected.filesGetMessages().options().copyDefaults(true);
		Infected.filesGetGrenades().options().copyDefaults(true);
		Infected.filesGetAbilities().options().copyDefaults(true);
		Infected.filesGetClasses().options().copyDefaults(true);
		Infected.filesGetSigns().options().copyDefaults(true);
		Infected.filesSafeAllButConfig();
		saveConfig();

		
		
		
		
		
		PluginManager pm = getServer().getPluginManager();
		pm = getServer().getPluginManager();
		Main.me = this;
		Main.file = getFile();
		Configuration getconfig = getConfig();
		Main.config = getconfig;

		// Check for an update
		PluginDescriptionFile pdf = getDescription();
		Main.v = pdf.getVersion();
		String[] s = Bukkit.getBukkitVersion().split("-");
		currentBukkitVersion = s[0];
		if (getConfig().getBoolean("Check For Updates.Enable"))
		{

			Updater updater = new Updater(this, "Infected-Core", getFile(),
					Updater.UpdateType.NO_DOWNLOAD, false);

			Main.update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			Main.name = updater.getLatestVersionString();
			updateBukkitVersion = updater.updateBukkitVersion;

			if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(0))) <= Integer.valueOf(String.valueOf(Main.v.charAt(0))))
				if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(2))) <= Integer.valueOf(String.valueOf(Main.v.charAt(2))))
					if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(4))) <= Integer.valueOf(String.valueOf(Main.v.charAt(4))))
					{
						Main.update = false;
					}
		}

		// Check if the plugin addons are there
		if (getConfig().getBoolean("DisguiseCraft Support"))
		{
			if (getServer().getPluginManager().getPlugin("ProtocolLib") == null)
			{
				System.out.println(Main.I + "DisguiseCraft was enabled but ProtocolLib wasn't found on the server");
				getConfig().set("DisguiseCraft Support", false);
			} else
			{
				if (!(getServer().getPluginManager().getPlugin("DisguiseCraft") == null))
				{
					setupDisguiseCraft();
				} else
				{
					System.out.println(Main.I + "DisguiseCraft wasn't found on this server, disabling DisguiseCraft Support");
					getConfig().set("DisguiseCraft Support", false);
					saveConfig();
				}
			}
		}
		// Check if the plugin addons are there
		if (getConfig().getBoolean("Vault Support.Enable"))
		{
			if (!(getServer().getPluginManager().getPlugin("Vault") == null))
			{
				setupEconomy();
			} else
			{
				System.out.println(Main.I + "Vault wasn't found on this server, Disabling Vault Support");
				getConfig().set("Vault Support.Enable", false);
				saveConfig();

			}
		}

		// Check if the plugin addons are there
		if (getConfig().getBoolean("CrackShot Support.Enable"))
		{
			if (getServer().getPluginManager().getPlugin("CrackShot") == null)
			{

				System.out.println(Main.I + "CrackShot wasn't found on this server, disabling CrackShot Support");
				getConfig().set("CrackShot Support.Enable", false);
				saveConfig();
			} else
			{
			CrackShotApi CSApi = new CrackShotApi(this);
			pm.registerEvents(CSApi, this);
			}
		}

		// Check if the plugin addons are there
		if (getConfig().getBoolean("TagAPI Support.Enable"))
		{
			if (getServer().getPluginManager().getPlugin("TagAPI") == null)
			{

				System.out.println(Main.I + "TagApi wasn't found on this server, disabling TagApi Support");
				getConfig().set("TagApi Support.Enable", false);
				saveConfig();
			} else
			{
				TagApi TagApi = new TagApi(this);
				pm.registerEvents(TagApi, this);
			}
		}

		// On enable set the times form the config
		Main.voteTime = getConfig().getInt("Time.Voting Time");
		Main.Wait = getConfig().getInt("Time.Alpha Zombie Infection");
		Main.GtimeLimit = getConfig().getInt("Time.Game Time Limit");

		// Setup metrics
		try
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e)
		{
			System.out.println("Error getting metrics!");
		}

		// Get the Commands class and the Listener
		getCommand("Infected").setExecutor(new Commands(this));
		PlayerListener PlayerListener = new PlayerListener(this);
		SignListener SignListener = new SignListener(this);
		TeleportFix TeleportFix = new TeleportFix(this);
		GrenadeListener GrenadeListener = new GrenadeListener(this);
		pm.registerEvents(PlayerListener, this);
		pm.registerEvents(GrenadeListener, this);
		pm.registerEvents(SignListener, this);
		pm.registerEvents(TeleportFix, this);

		// Clear the blocks DB because a game couldnt have started before the
		// plugins up
		Main.Chests.clear();
		Main.Blocks.clear();

		// Do the info signs (Updating the info)
		if (getConfig().getBoolean("Info Signs.Enabled"))
		{
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
			{
				@Override
				public void run() {
					if (!Files.getSigns().getStringList("Info Signs").isEmpty())
					{
						for (String loc : Files.getSigns().getStringList("Info Signs"))
						{
							String status;

							if (Infected.getGameState() == GameState.VOTING)
							{
								status = "Voting";
							}
							if (Infected.getGameState() == GameState.BEFOREINFECTED)
							{
								status = "B4 Infected";
							}
							if (Infected.getGameState() == GameState.STARTED)
							{
								status = "Started";
							} else
							{
								status = "In Lobby";
							}

							int time = Main.currentTime;
							if (Files.getSigns().getStringList("Info Signs").contains(loc))
							{
								Location location = Methods.getLocationFromString(loc);
								if (location.getBlock().getType() == Material.SIGN_POST || location.getBlock().getType() == Material.WALL_SIGN)
								{
									Sign sign = (Sign) location.getBlock().getState();
									sign.setLine(1, ChatColor.GREEN + "Playing: " + ChatColor.DARK_GREEN + String.valueOf(Infected.listInGame().size()));
									sign.setLine(2, ChatColor.GOLD + status);
									sign.setLine(3, ChatColor.GRAY + "Time: " + ChatColor.YELLOW + String.valueOf(time));
									sign.update();
								}
							}
						}
					}
				}
			}, 100L, getConfig().getInt("Info Signs.Refresh Time") * 20);
		}

		// Make sure the Infected's CB is the same as the server's CB
		if (!currentBukkitVersion.equalsIgnoreCase(Main.bVersion))
		{
			System.out.println("==========================================================");
			System.out.println("");
			System.out.println("Your Bukkit Version: |" + currentBukkitVersion + "|");
			System.out.println("Versions do not match so I am not responsible for any errors on your server!");
			System.out.println("");
			System.out.println("==========================================================");
		}

		// Setup the scoreboards
		if (Main.config.getBoolean("ScoreBoard Support"))
		{

			// Votes
			Main.voteList.setDisplaySlot(DisplaySlot.SIDEBAR);
			Main.voteList.setDisplayName("Votes");

			// Playing
			Main.playingList.setDisplaySlot(DisplaySlot.SIDEBAR);
			Main.playingList.setDisplayName("Playing");

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {

		// On disable reset players with everything from before
		for (Player player : Bukkit.getServer().getOnlinePlayers())
			if (player != null)
				if (Main.inGame.contains(player.getName()))
				{
					player.sendMessage(Main.I + "Server was reloaded!");
					player.setHealth(20.0);
					player.setFoodLevel(20);
					for (PotionEffect reffect : player.getActivePotionEffects())
					{
						player.removePotionEffect(reffect.getType());
					}
					player.setGameMode(GameMode.valueOf(Main.gamemode.get(player.getName())));
					Main.Lasthit.remove(player.getName());
					if (Main.Inventory.containsKey(player.getName()))
					{
						player.getInventory().setContents(Main.Inventory.get(player.getName()));
					}
					if (Main.Armor.containsKey(player.getName()))
					{
						player.getInventory().setArmorContents(Main.Armor.get(player.getName()));
					}
					player.updateInventory();
					player.setExp(Main.Exp.get(player.getName()));
					player.setLevel(Main.Levels.get(player.getName()));
					if (Main.Spot.containsKey(player.getName()))
					{
						player.teleport(Main.Spot.get(player.getName()));
					}
					if (Main.Food.containsKey(player.getName()))
					{
						player.setFoodLevel(Main.Food.get(player.getName()));
					}
					if (Main.Health.containsKey(player.getName()))
					{
						player.setHealth(Main.Health.get(player.getName()));
					}
				}
		
	}

	// Setup DisguiseCraft
	public void setupDisguiseCraft() {
		Main.dcAPI = DisguiseCraft.getAPI();
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null)
		{
			Main.economy = economyProvider.getProvider();
		}

		return (Main.economy != null);
	}

}