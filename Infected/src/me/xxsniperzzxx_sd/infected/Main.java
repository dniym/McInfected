
package me.xxsniperzzxx_sd.infected;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.xxsniperzzxx_sd.infected.Enums.GameState;
import me.xxsniperzzxx_sd.infected.GameMechanics.UpdateInfoSigns;
import me.xxsniperzzxx_sd.infected.Listeners.DamageEvents;
import me.xxsniperzzxx_sd.infected.Listeners.DeathEvent;
import me.xxsniperzzxx_sd.infected.Listeners.GrenadeListener;
import me.xxsniperzzxx_sd.infected.Listeners.PlayerListener;
import me.xxsniperzzxx_sd.infected.Listeners.SignListener;
import me.xxsniperzzxx_sd.infected.Tools.AddonManager;
import me.xxsniperzzxx_sd.infected.Tools.Metrics;
import me.xxsniperzzxx_sd.infected.Tools.TeleportFix;
import me.xxsniperzzxx_sd.infected.Tools.Updater;
import me.xxsniperzzxx_sd.infected.Tools.Handlers.LocationHandler;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;

public class Main extends JavaPlugin {

	// Initialize all the variables
	public static String bVersion = "1.6.4";
	public static int currentTime = 0;
	public static String v = null;
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
	public static HashMap<String, String> Spot = new HashMap<String, String>();

	public static HashMap<Location, Material> Blocks = new HashMap<Location, Material>();
	public static HashMap<Location, ItemStack[]> Chests = new HashMap<Location, ItemStack[]>();

	public static String I = "" + ChatColor.DARK_RED + ChatColor.BOLD + "«" + ChatColor.RESET + ChatColor.DARK_GRAY + ChatColor.BOLD +"Infected" + ChatColor.DARK_RED + ChatColor.BOLD + "»" + ChatColor.RESET + ChatColor.GRAY + " ";

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
	public static String updateBukkitVersion = "0.0.0";

	public static AddonManager addon;

	// Plugin Addons
	public static Plugin Disguiser;
	public static Economy economy = null;

	// public NamedItemStack NIS;

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm = getServer().getPluginManager();
		Main.me = this;
		Main.file = getFile();
		Configuration getconfig = getConfig();
		Main.config = getconfig;

		System.out.println("===== Infected =====");
		try
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e)
		{
			System.out.println("Metrics was unable to start...");
		}

		// Create Configs and files
		getConfig().options().copyDefaults(true);
		Infected.filesGetArenas().options().copyDefaults(true);
		Infected.filesGetKillTypes().options().copyDefaults(true);
		Infected.filesGetShop().options().copyDefaults(true);
		Infected.filesGetPlayers().options().copyDefaults(true);
		Infected.filesGetMessages().options().copyDefaults(true);
		Infected.filesGetGrenades().options().copyDefaults(true);
		Infected.filesGetAbilities().options().copyDefaults(true);
		Infected.filesGetClasses().options().copyDefaults(true);
		Infected.filesGetSigns().options().copyDefaults(true);
		Infected.filesSafeAll();

		// Check for an update
		PluginDescriptionFile pdf = getDescription();
		Main.v = pdf.getVersion();
		String[] s = Bukkit.getBukkitVersion().split("-");
		currentBukkitVersion = s[0];
		if (getConfig().getBoolean("Check For Updates.Enable"))
		{
			try
			{
				Updater updater = new Updater(this, "Infected-Core", getFile(),
						Updater.UpdateType.NO_DOWNLOAD, false);

				Main.update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				Main.name = updater.getLatestVersionString();

				if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(0))) <= Integer.valueOf(String.valueOf(Main.v.charAt(0))))
					if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(2))) <= Integer.valueOf(String.valueOf(Main.v.charAt(2))))
						if (Integer.valueOf(String.valueOf(updater.getVersion().charAt(4))) <= Integer.valueOf(String.valueOf(Main.v.charAt(4))))
							Main.update = false;
			} catch (Exception ex)
			{
				System.out.println("The auto-updater tried to contact dev.bukkit.org, but was unsuccessful.");
			}
		}
		// Get Plugin addons
		addon = new AddonManager(this);
		addon.getAddons();

		// On enable set the times form the config
		Main.voteTime = getConfig().getInt("Time.Voting Time");
		Main.Wait = getConfig().getInt("Time.Alpha Zombie Infection");
		Main.GtimeLimit = getConfig().getInt("Time.Game Time Limit");

		// Get the Commands class and the Listener
		getCommand("Infected").setExecutor(new Commands(this));
		PlayerListener PlayerListener = new PlayerListener(this);
		SignListener SignListener = new SignListener(this);
		TeleportFix TeleportFix = new TeleportFix(this);
		if (getConfig().getBoolean("Use Death Event Instead of Damage Event"))
		{
			DeathEvent DeathEvents = new DeathEvent(this);
			pm.registerEvents(DeathEvents, this);
		} else
		{
			DamageEvents DamageEvents = new DamageEvents(this);
			pm.registerEvents(DamageEvents, this);
		}
		GrenadeListener GrenadeListener = new GrenadeListener(this);
		pm.registerEvents(PlayerListener, this);
		pm.registerEvents(GrenadeListener, this);
		pm.registerEvents(SignListener, this);
		pm.registerEvents(TeleportFix, this);

		// Do the info signs (Updating the info)
		if (getConfig().getBoolean("Info Signs.Enabled"))
		{
			UpdateInfoSigns.update();
		}

		// Make sure the Infected's CB is the same as the server's CB
		if (!currentBukkitVersion.equalsIgnoreCase(Main.bVersion))
		{
			System.out.println("Your Bukkit Version: |" + currentBukkitVersion + "|");
			System.out.println("Versions do not match so I am not responsible for any errors on your server!");
		}

		System.out.println("====================");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {

		// On disable reset players with everything from before
		for (Player player : Bukkit.getServer().getOnlinePlayers())
			if (player != null)
				if (Main.inGame.contains(player.getName()))
				{
					player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
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
						player.teleport(LocationHandler.getPlayerLocation(Main.Spot.get(player.getName())));
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

}