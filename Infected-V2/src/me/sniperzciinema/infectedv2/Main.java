
package me.sniperzciinema.infectedv2;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.sniperzciinema.infectedv2.Handlers.Lobby;
import me.sniperzciinema.infectedv2.Handlers.Arena.Arena;
import me.sniperzciinema.infectedv2.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infectedv2.Listeners.DamageEvents;
import me.sniperzciinema.infectedv2.Listeners.GrenadeListener;
import me.sniperzciinema.infectedv2.Listeners.PlayerListener;
import me.sniperzciinema.infectedv2.Listeners.RegisterAndUnRegister;
import me.sniperzciinema.infectedv2.Listeners.SignListener;
import me.sniperzciinema.infectedv2.Messages.StringUtil;
import me.sniperzciinema.infectedv2.Tools.AddonManager;
import me.sniperzciinema.infectedv2.Tools.Files;
import me.sniperzciinema.infectedv2.Tools.Metrics;
import me.sniperzciinema.infectedv2.Tools.TeleportFix;
import me.sniperzciinema.infectedv2.Tools.UpdateInfoSigns;
import me.sniperzciinema.infectedv2.Tools.Updater;
import net.milkbowl.vault.economy.Economy;

import code.husky.mysql.MySQL;

import org.bukkit.Bukkit;
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
		GrenadeListener GrenadeListener = new GrenadeListener();
		TeleportFix TeleportFix = new TeleportFix(this);
		DamageEvents DamageEvents = new DamageEvents(this);
		RegisterAndUnRegister RegisterAndUnRegister = new RegisterAndUnRegister();
		pm.registerEvents(DamageEvents, this);
		pm.registerEvents(PlayerListener, this);
		pm.registerEvents(RegisterAndUnRegister, this);
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
			System.out.println("Versions do not match! \n There is no promise this plugin will work!");
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
				e.printStackTrace();
			}

		}
		for(Player u : Bukkit.getOnlinePlayers()){
			InfPlayerManager.createInfPlayer(u);
		}
		if (Files.getArenas().getConfigurationSection("Arenas") != null)
			for (String a : Files.getArenas().getConfigurationSection("Arenas").getKeys(false))
			{
				Arena arena = new Arena(StringUtil.getWord(a));
				Lobby.addArena(arena);
				System.out.println("Loaded Arena: " + arena.getName());
			}
		else
			System.out.println("Couldn't Find Any Arenas");

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