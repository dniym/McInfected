
package me.xxsniperzzxx_sd.infected;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Handlers.Player.InfPlayerManager;
import me.xxsniperzzxx_sd.infected.Listeners.DamageEvents;
import me.xxsniperzzxx_sd.infected.Listeners.GrenadeListener;
import me.xxsniperzzxx_sd.infected.Listeners.PlayerListener;
import me.xxsniperzzxx_sd.infected.Listeners.SignListener;
import me.xxsniperzzxx_sd.infected.Tools.AddonManager;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.Metrics;
import me.xxsniperzzxx_sd.infected.Tools.TeleportFix;
import me.xxsniperzzxx_sd.infected.Tools.UpdateInfoSigns;
import me.xxsniperzzxx_sd.infected.Tools.Updater;
import net.milkbowl.vault.economy.Economy;

import code.husky.mysql.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	// Initialize all the variables
	public static String bVersion = "1.6.4";
	public static String v = null;

	public static String I = "" + ChatColor.DARK_RED + ChatColor.BOLD + "«" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Infected" + ChatColor.DARK_RED + ChatColor.BOLD + "»" + ChatColor.LIGHT_PURPLE + " ";

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

	public static MySQL MySQL = null;
	public static Connection c = null;

	public static Lobby Lobby;
	public static InfPlayerManager InfPlayerManager;

	@Override
	public void onEnable() {
		Lobby = new Lobby();
		InfPlayerManager = new InfPlayerManager(this);
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
			System.out.println("Metrics was started!");
		} catch (IOException e)
		{
			System.out.println("Metrics was unable to start...");
		}

		// Create Configs and files
		getConfig().options().copyDefaults(true);
		Files.getArenas().options().copyDefaults(true);
		Files.getKills().options().copyDefaults(true);
		Files.getShop().options().copyDefaults(true);
		Files.getPlayers().options().copyDefaults(true);
		Files.getMessages().options().copyDefaults(true);
		Files.getGrenades().options().copyDefaults(true);
		Files.getAbilities().options().copyDefaults(true);
		Files.getClasses().options().copyDefaults(true);
		Files.getSigns().options().copyDefaults(true);
		Files.saveAbilities();
		Files.saveArenas();
		Files.saveClasses();
		Files.saveConfig();
		Files.saveGrenades();
		Files.saveKills();
		Files.saveMessages();
		Files.savePlayers();
		Files.saveShop();
		Files.saveSigns();

		// Check for an update
		PluginDescriptionFile pdf = getDescription();
		Main.v = pdf.getVersion();
		String[] s = Bukkit.getBukkitVersion().split("-");
		currentBukkitVersion = s[0];
		if (getConfig().getBoolean("Check For Updates.Enable"))
		{
			try
			{
				Updater updater = new Updater(this, 44622, getFile(),
						Updater.UpdateType.NO_DOWNLOAD, false);

				Main.update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				Main.name = updater.getLatestName();

				System.out.println(v.replaceAll(".", ""));
				System.out.println(updater.getLatestFileVersion().replaceAll(".", ""));

				int currentVersion = Integer.valueOf(v.replaceAll("\\.", ""));
				int newVersion = Integer.valueOf(updater.getLatestFileVersion().replaceAll("\\.", ""));

				if (currentVersion >= newVersion)
				{
					System.out.println("You are running a beta version of Infected!");
					update = false;
				}

				else
					System.out.println("You need to update Infected to: " + updater.getLatestFileVersion());

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
		PlayerListener PlayerListener = new PlayerListener();
		SignListener SignListener = new SignListener();
		TeleportFix TeleportFix = new TeleportFix(this);
		// if
		// (getConfig().getBoolean("Use Death Event Instead of Damage Event"))
		// {
		// DeathEvent DeathEvents = new DeathEvent(this);
		// pm.registerEvents(DeathEvents, this);
		// } else
		// {
		DamageEvents DamageEvents = new DamageEvents(this);
		pm.registerEvents(DamageEvents, this);
		// }
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
		if (getConfig().getBoolean("MySQL.Enable"))
		{

			System.out.println("Attempting to connect to MySQL");
			MySQL = new MySQL(this, getConfig().getString("MySQL.Host"),
					getConfig().getString("MySQL.Port"),
					getConfig().getString("MySQL.Database"),
					getConfig().getString("MySQL.User"),
					getConfig().getString("MySQL.Pass"));
			c = MySQL.openConnection();
			try
			{
				Statement state = c.createStatement();

				state.executeUpdate("CREATE TABLE IF NOT EXISTS Infected (Player CHAR(16), Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10));");
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("====================");
	}

	@Override
	public void onDisable() {

		// On disable reset players with everything from before
		for (Player p : Lobby.getInGame())
			InfPlayerManager.getInfPlayer(p).leaveInfected();

		if (getConfig().getBoolean("MySQL.Enable"))
		{
			MySQL.closeConnection();
		}
	}

}