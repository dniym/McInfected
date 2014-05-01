
package me.sniperzciinema.infected;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import me.sniperzciinema.infected.Command.CHandler;
import me.sniperzciinema.infected.Extras.Menus;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
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
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Tools.AddonManager;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.Metrics;
import me.sniperzciinema.infected.Tools.TeleportFix;
import me.sniperzciinema.infected.Tools.UpdateInfoSigns;
import me.sniperzciinema.infected.Tools.Updater;
import me.sniperzciinema.infected.Tools.MySQL.MySQL;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Infected extends JavaPlugin {
	
	// Initialize all the variables
	public static Plugin			me;
	
	public static boolean			update			= false;
	public static String			updateName	= "";
	public static String			updateLink	= "";
	public static File				file;
	
	// Plugin Addons
	public static Plugin			Disguiser;
	public static Economy			economy			= null;
	
	public static MySQL				MySQL				= null;
	public static Connection	connection	= null;
	
	public static Menus				Menus;
	public static CHandler		commandsHandler;
	
	@Override
	public void onDisable() {
		
		Infected.Menus.destroyAllMenus();
		
		try
		{
			// On disable reset players with everything from before
			if (!Lobby.getPlayersInGame().isEmpty())
				for (Player p : Bukkit.getOnlinePlayers())
					if (Lobby.isInGame(p))
					{
						p.sendMessage(Msgs.Error_Misc_Plugin_Unloaded.getString());
						InfPlayerManager.getInfPlayer(p).leaveInfected();
					}
		}
		catch (Exception e)
		{
			
		}
		if (Settings.MySQLEnabled())
			Infected.MySQL.closeConnection();
	}
	
	@Override
	public void onEnable() {
		
		System.out.println(Msgs.Format_Header.getString("<title>", " Infected "));
		
		// Initialize an instance
		Infected.me = this;
		
		// Files
		Files.updateAll();
		Files.saveAll();
		Files.reloadAll();
		
		// Addons
		Lobby.loadAllArenas();
		GrenadeManager.loadConfigGrenades();
		InfClassManager.loadConfigClasses();
		AddonManager.getAddons();
		Infected.Menus = new Menus();
		
		// Extras
		checkMySQL();
		checkForUpdates();
		startMetrics();
		
		// Commands And Events
		registerCommands();
		registerEvents();
		
		// Create Players for Infected
		for (Player u : Bukkit.getOnlinePlayers())
			InfPlayerManager.createInfPlayer(u);
		
		// Do the info signs (Updating the info)
		if (Settings.InfoSignsEnabled())
			UpdateInfoSigns.update();
		
		System.out.println(Msgs.Format_Line.getString());
	}
	
	void registerCommands() {
		
		Infected.commandsHandler = new CHandler();
		getCommand("Infected").setExecutor(Infected.commandsHandler);
		getCommand("Infected").setTabCompleter(Infected.commandsHandler);
		
	}
	
	void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm = getServer().getPluginManager();
		pm.registerEvents(new ScoreBoardToggle(), this);
		pm.registerEvents(new DamageEvents(this), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new RegisterAndUnRegister(), this);
		pm.registerEvents(new GrenadeListener(), this);
		pm.registerEvents(new SignListener(), this);
		pm.registerEvents(new TeleportFix(this), this);
	}
	
	void checkMySQL() {
		if (Settings.MySQLEnabled())
		{
			System.out.println("Attempting to connect to MySQL");
			Infected.MySQL = new MySQL(this, Files.getConfig().getString("MySQL.Host"),
					Files.getConfig().getString("MySQL.Port"), Files.getConfig().getString("MySQL.Database"),
					Files.getConfig().getString("MySQL.Username"),
					Files.getConfig().getString("MySQL.Password"));
			
			try
			{
				Infected.connection = Infected.MySQL.openConnection();
				Statement statement = Infected.connection.createStatement();
				
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Infected" + " (UUID VARCHAR(40), Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10));");
				System.out.println("MySQL Table has been loaded");
			}
			catch (Exception e)
			{
				Files.getConfig().set("MySQL.Enabled", false);
				Files.saveConfig();
				System.out.println("Unable to connect to MySQL");
			}
		}
	}
	
	void checkForUpdates() {
		if (Settings.checkForUpdates())
		{
			
			Infected.file = getFile();
			Updater updater = new Updater(this, 44622, getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
			
			Infected.update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			Infected.updateName = updater.getLatestName();
			Infected.updateLink = updater.getLatestFileLink();
			
			if (Infected.update)
				for (Player player : Bukkit.getOnlinePlayers())
					if (player.hasPermission("Infected.Admin"))
						player.sendMessage(RandomChatColor.getColor() + "Update for Infected Availble: " + Infected.updateName);
		}
	}
	
	void startMetrics() {
		try
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
			System.out.println("Metrics was started!");
		}
		catch (IOException e)
		{
			System.out.println("Metrics was unable to start...");
		}
	}
}
