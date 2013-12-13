
package me.sniperzciinema.infected;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	// Initialize all the variables
	public static String version = null;
	public static Plugin me;

	public static boolean update = false;
	public static String updateName = "";
	public static File file;


	// Plugin Addons
	public static Plugin Disguiser;
	public static Economy economy = null;

	public static MySQL MySQL = null;
	public static Connection connection = null;

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm = getServer().getPluginManager();
		me = this;
		
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
		version = pdf.getVersion();

		if (getConfig().getBoolean("Check For Updates.Enable"))
		{

			file = this.getFile();
			Updater updater = new Updater(this, 44622, this.getFile(),
					Updater.UpdateType.NO_DOWNLOAD, true);

			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			updateName = updater.getLatestName();

			if (update)
				System.out.println("You need to update Infected to: " + updater.getLatestFileVersion());
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
		AddonManager.getAddons();
		
		// Do the info signs (Updating the info)
		if (Settings.InfoSignsEnabled())
			UpdateInfoSigns.update();

		if (Settings.MySQLEnabled())
		{
			System.out.println("Attempting to connect to MySQL");
			MySQL = new MySQL(this, getConfig().getString("MySQL.Host"),
					getConfig().getString("MySQL.Port"),
					getConfig().getString("MySQL.Database"),
					getConfig().getString("MySQL.Username"),
					getConfig().getString("MySQL.Password"));

			try
			{
				connection = MySQL.openConnection();
				Statement statement = connection.createStatement();

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Infected" + " (Player VARCHAR(20), Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10));");
				System.out.println("MySQL Table has been loaded");
			} catch (Exception e)
			{
				Files.getConfig().set("MySQL.Enabled", false);
				System.out.println("Unable to connect to MySQL");
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