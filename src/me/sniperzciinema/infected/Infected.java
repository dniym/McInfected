
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
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Infected extends JavaPlugin {

	// Initialize all the variables
	public static String		version		= null;
	public static Plugin		me;

	public static boolean		update		= false;
	public static String		updateName	= "";
	public static String		updateLink	= "";
	public static File			file;

	// Plugin Addons
	public static Plugin		Disguiser;
	public static Economy		economy		= null;

	public static MySQL			MySQL		= null;
	public static Connection	connection	= null;

	// Create the menus
	public static Menus			Menus;

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm = getServer().getPluginManager();
		me = this;
		System.out.println(Msgs.Format_Header.getString("<title>", " Infected "));

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

		if (Settings.checkForUpdates())
		{

			file = this.getFile();
			Updater updater = new Updater(this, 44622, this.getFile(),
					Updater.UpdateType.NO_DOWNLOAD, true);

			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			updateName = updater.getLatestName();
			updateLink = updater.getLatestFileLink();

			if (update)
			{
				for (Player player : Bukkit.getOnlinePlayers())
					if (player.hasPermission("Infected.Admin"))
						player.sendMessage(RandomChatColor.getColor() + "Update for Infected Availble: " + updateName);
			}
		}

		// Get the Commands class and the Listener
		getCommand("Infected").setExecutor(new CHandler());

		pm.registerEvents(new ScoreBoardToggle(), this);
		pm.registerEvents(new DamageEvents(this), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new RegisterAndUnRegister(), this);
		pm.registerEvents(new GrenadeListener(), this);
		pm.registerEvents(new SignListener(), this);
		pm.registerEvents(new TeleportFix(this), this);

		AddonManager.getAddons();

		// Do the info signs (Updating the info)
		if (Settings.InfoSignsEnabled())
			UpdateInfoSigns.update();

		if (Settings.MySQLEnabled())
		{
			System.out.println("Attempting to connect to MySQL");
			MySQL = new MySQL(this, Files.getConfig().getString("MySQL.Host"),
					Files.getConfig().getString("MySQL.Port"),
					Files.getConfig().getString("MySQL.Database"),
					Files.getConfig().getString("MySQL.Username"),
					Files.getConfig().getString("MySQL.Password"));

			try
			{
				connection = MySQL.openConnection();
				Statement statement = connection.createStatement();

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Infected" + " (Player VARCHAR(20), Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10));");
				System.out.println("MySQL Table has been loaded");
			}
			catch (Exception e)
			{
				Files.getConfig().set("MySQL.Enabled", false);
				Files.saveConfig();
				System.out.println("Unable to connect to MySQL");
			}
		}

		for (Player u : Bukkit.getOnlinePlayers())
			InfPlayerManager.createInfPlayer(u);

		Lobby.loadAllArenas();

		InfClassManager.loadConfigClasses();
		GrenadeManager.loadConfigGrenades();

		Menus = new Menus();

		System.out.println(Msgs.Format_Line.getString());

	}

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
			// If theres no one in Infected it seems to not be able to find the
			// Lobby class when checking if the game is empty
		}
		if (Settings.MySQLEnabled())
		{
			MySQL.closeConnection();
		}
	}

}
