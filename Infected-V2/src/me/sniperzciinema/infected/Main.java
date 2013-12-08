
package me.sniperzciinema.infected;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Listeners.DamageEvents;
import me.sniperzciinema.infected.Listeners.GrenadeListener;
import me.sniperzciinema.infected.Listeners.PlayerListener;
import me.sniperzciinema.infected.Listeners.RegisterAndUnRegister;
import me.sniperzciinema.infected.Listeners.ScoreBoardToggle;
import me.sniperzciinema.infected.Listeners.SignListener;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Tools.AddonManager;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.Metrics;
import me.sniperzciinema.infected.Tools.TeleportFix;
import me.sniperzciinema.infected.Tools.UpdateInfoSigns;
import me.sniperzciinema.infected.Tools.Updater;
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
	public static boolean update = false;
	public static String name = "";
	public static Plugin me;
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
		Files.getShop().options().copyDefaults(true);
		Files.getPlayers().options().copyDefaults(true);
		Files.getMessages().options().copyDefaults(true);
		Files.getGrenades().options().copyDefaults(true);
		Files.getClasses().options().copyDefaults(true);
		Files.getSigns().options().copyDefaults(true);
		Files.saveAll();

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

		// Get the Commands class and the Listener
		getCommand("Infected").setExecutor(new Commands(this));
		PlayerListener PlayerListener = new PlayerListener();
		SignListener SignListener = new SignListener();
		GrenadeListener GrenadeListener = new GrenadeListener();
		TeleportFix TeleportFix = new TeleportFix(this);
		DamageEvents DamageEvents = new DamageEvents(this);
		ScoreBoardToggle ScoreBoardToggle = new ScoreBoardToggle();
		RegisterAndUnRegister RegisterAndUnRegister = new RegisterAndUnRegister();
		pm.registerEvents(ScoreBoardToggle, this);
		pm.registerEvents(DamageEvents, this);
		pm.registerEvents(PlayerListener, this);
		pm.registerEvents(RegisterAndUnRegister, this);
		pm.registerEvents(GrenadeListener, this);
		pm.registerEvents(SignListener, this);
		pm.registerEvents(TeleportFix, this);

		// Do the info signs (Updating the info)
		if (Settings.InfoSignsEnabled())
			UpdateInfoSigns.update();

		// Make sure the Infected's CB is the same as the server's CB
		if (!currentBukkitVersion.equalsIgnoreCase(Main.bVersion))
		{
			System.out.println("Your Bukkit Version: |" + currentBukkitVersion + "|");
			System.out.println("Versions do not match! \n There is no promise this plugin will work!");
		}
		if (Settings.MySQLEnabled())
		{
			System.out.println("Attempting to connect to MySQL");
			MySQL = new MySQL(this, getConfig().getString("MySQL.Host"),
					getConfig().getString("MySQL.Port"),
					getConfig().getString("MySQL.Database"),
					getConfig().getString("MySQL.Username"),
					getConfig().getString("MySQL.Password"));
			c = MySQL.openConnection();

			try
			{
				Statement statement = c.createStatement();

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Infected" + " (Player VARCHAR(20), Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10));");
				System.out.println("MySQL Table has been loaded");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		for (Player u : Bukkit.getOnlinePlayers())
		{
			InfPlayerManager.createInfPlayer(u);
		}
		if (Files.getArenas().getConfigurationSection("Arenas") != null)
			for (String a : Files.getArenas().getConfigurationSection("Arenas").getKeys(false))
			{
				Arena arena = new Arena(StringUtil.getWord(a));
				Lobby.addArena(arena);
				if (Settings.logAreansEnabled())
					System.out.println("Loaded Arena: " + arena.getName());
			}
		else if (Settings.logAreansEnabled())
			System.out.println("Couldn't Find Any Arenas");

		InfClassManager.loadConfigClasses();
		GrenadeManager.loadConfigGrenades();

		System.out.println("====================");
	}

	@Override
	public void onDisable() {

		// On disable reset players with everything from before
		if (!Lobby.getInGame().isEmpty())
			for (Player p : Bukkit.getOnlinePlayers())
				if (Lobby.isInGame(p))
				{
					p.sendMessage(Msgs.Error_Misc_Plugin_Unloaded.getString());
					InfPlayerManager.getInfPlayer(p).leaveInfected();
				}
		if (getConfig().getBoolean("MySQL.Enable"))
		{
			MySQL.closeConnection();
		}
	}

}