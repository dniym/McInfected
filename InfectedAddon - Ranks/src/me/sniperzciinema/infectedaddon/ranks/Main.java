
package me.sniperzciinema.infectedaddon.ranks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;

import me.sniperzciinema.infected.Events.InfectedCommandEvent;
import me.sniperzciinema.infected.Events.InfectedEndGame;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Tools.Files;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import code.husky.mysql.MySQL;


public class Main extends JavaPlugin implements Listener {

	// Set up all the needed things for files
	public YamlConfiguration ranks = null;
	public File ranksFile = null;
	public Plugin me;
	public Permission perms;
	public static MySQL MySQL = null;
	public static Connection connection = null;

	public boolean update;
	public String updateName;

	public void onEnable() {
		me = this;
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null)
		{
			perms = permissionProvider.getProvider();
		}

		getRanks().options().copyDefaults(true);
		saveRanks();

		for (String s : getRanks().getConfigurationSection("Ranks").getKeys(true))
		{
			if (!s.contains("."))
			{
				RankManager.addRank(new Rank(
						s,
						getRanks().getString("Ranks." + s + ".Prefix"),
						getRanks().getBoolean("Ranks." + s + ".Default"),
						getRanks().getBoolean("Ranks." + s + ".Max"),
						getRanks().getInt("Ranks." + s + ".Score"),
						InfClassManager.getClass(Team.Human, getRanks().getString("Ranks." + s + ".Class.Human")),
						InfClassManager.getClass(Team.Zombie, getRanks().getString("Ranks." + s + ".Class.Zombie")),
						getRanks().getStringList("Ranks." + s + ".Permissions")));
			}
		}
		RankManager.getPresets();

		getServer().getPluginManager().registerEvents(this, this);

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

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Infected_Ranks" + " (Player VARCHAR(20), Rank VARCHAR(20));");
				System.out.println("MySQL Table has been loaded");
			} catch (Exception e)
			{
				System.out.println("Unable to connect to MySQL");
			}
		}

		if (Settings.checkForUpdates())
		{

			Updater updater = new Updater(this, 70530, this.getFile(),
					Updater.UpdateType.NO_DOWNLOAD, true);

			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			updateName = updater.getLatestName();

			if (update)
			{
				System.out.println("You need to update InfectedAddon-Dedicated Server to: " + updater.getLatestFileVersion());
				for (Player player : Bukkit.getOnlinePlayers())
					player.sendMessage(RandomChatColor.getColor() + "Update for Infected Availble: " + updateName);
			}
		}

	}

	public void onDisable() {
		if (Settings.MySQLEnabled())
			MySQL.closeConnection();
	}

	@EventHandler
	public void onInfectedCommand(final InfectedCommandEvent event) {
		if (event.getArgs().length >= 1)
		{
			if (event.getArgs()[0].equalsIgnoreCase("Rank") || event.getArgs()[0].equalsIgnoreCase("Rank") || event.getArgs()[0].equalsIgnoreCase("Ranks") && event.getP() != null)
			{
				event.setCancelled(true);

				Player p = event.getP();

				if (RankManager.canRankUp(p))
					RankManager.setPlayersRank(p, RankManager.getNextRank(p));

				Rank rank = RankManager.getPlayersRank(p);
				Rank nextRank = RankManager.getNextRank(p);
				p.sendMessage(Msgs.Format_Header.getString("<title>", "Ranks"));
				if (rank.isMaxRank())
					p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "                      MAX RANK");
				p.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Your Current Rank: " + ChatColor.GRAY + rank.getPrefix());
				p.sendMessage(ChatColor.GRAY + "Your Score: " + ChatColor.RED + InfPlayerManager.getInfPlayer(p).getScore());
				if (!rank.isMaxRank())
					p.sendMessage(ChatColor.GRAY + "Score to next rank: " + ChatColor.RED + (nextRank.getScoreNeeded() - InfPlayerManager.getInfPlayer(p).getScore()));
				if (!rank.isMaxRank())
					p.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Next Rank: " + nextRank.getPrefix() + ChatColor.RED + " - " + ChatColor.GRAY + " Unlocks at " + ChatColor.RED + nextRank.getScoreNeeded());
				if (rank.isMaxRank())
					p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "                      MAX RANK");
				p.sendMessage(Msgs.Format_Line.getString());
			} else if (event.getArgs()[0].equalsIgnoreCase("Stats"))
			{
				if (event.getArgs().length == 1)
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
					{

						@Override
						public void run() {
							event.getP().sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + ChatColor.BOLD + "Rank: " + ChatColor.GRAY + RankManager.getPlayersRank(event.getP()).getPrefix());
						}
					}, 1L);
				}
				if (event.getArgs().length == 2 && event.getP().hasPermission("Infected.Stats.Other"))
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
					{

						@Override
						public void run() {
							event.getP().sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GREEN + ChatColor.BOLD + "Rank: " + ChatColor.GRAY + RankManager.getPlayersRank(event.getArgs()[1]).getPrefix());
						}
					}, 1L);
				}
			}
		}
	}

	@EventHandler
	public void onInfectedGameEnd(InfectedEndGame event) {
		for (Player u : event.getPlayers())
		{
			if (RankManager.canRankUp(u))
				RankManager.setPlayersRank(u, RankManager.getNextRank(u));

			else if (RankManager.canRankDown(u))
				RankManager.setPlayersRank(u, RankManager.getLastRank(u));
		}
	}

	@EventHandler
	public void onInfectedJoinOrLeave(InfectedCommandEvent e) {
		if (e.getArgs().length >= 1 && !e.isCancelled() && e.getArgs()[0].toLowerCase().equals("join"))
		{
			Player p = e.getP();

			if (RankManager.canRankUp(p))
				RankManager.setPlayersRank(p, RankManager.getNextRank(p));

			else if (RankManager.canRankDown(p))
				RankManager.setPlayersRank(p, RankManager.getLastRank(p));

			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			ip.setInfClass(Team.Human, RankManager.getPlayersRank(p).getHumanClass());
			ip.setInfClass(Team.Zombie, RankManager.getPlayersRank(p).getZombieClass());

			addPermissions(p);

		} else if (e.getArgs().length >= 1 && !e.isCancelled() && e.getArgs()[0].toLowerCase().equals("leave"))
		{
			Player p = e.getP();

			if (RankManager.canRankUp(p))
				RankManager.setPlayersRank(p, RankManager.getNextRank(p));

			else if (RankManager.canRankDown(p))
				RankManager.setPlayersRank(p, RankManager.getLastRank(p));

			removePermissions(p);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (update && player.hasPermission("Infected.Admin"))
		{
			player.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "An update is available: " + updateName);
			player.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "Download it at: http://dev.bukkit.org/server-mods/infectedaddon-ranks/");
		}
	}

	public void addPermissions(Player p) {
		for (String s : RankManager.getPlayersRank(p).getPermissions())
			perms.playerAdd(p, s);
	}

	public void removePermissions(Player p) {
		for (String s : RankManager.getPlayersRank(p).getPermissions())
			perms.playerRemove(p, s);
	}

	public void reloadRanks() {
		if (ranksFile == null)
			ranksFile = new File(
					Bukkit.getPluginManager().getPlugin("InfectedAddon-Ranks").getDataFolder(),
					"Ranks.yml");
		ranks = YamlConfiguration.loadConfiguration(ranksFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("InfectedAddon-Ranks").getResource("Ranks.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!ranksFile.exists())
				ranks.setDefaults(defConfig);
		}
	}

	// Get Ranks file
	public FileConfiguration getRanks() {
		if (ranks == null)
			reloadRanks();
		return ranks;
	}

	// Safe Ranks File
	public void saveRanks() {
		if (ranks == null || ranksFile == null)
			return;
		try
		{
			getRanks().save(ranksFile);
		} catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + ranksFile, ex);
		}
	}

}
