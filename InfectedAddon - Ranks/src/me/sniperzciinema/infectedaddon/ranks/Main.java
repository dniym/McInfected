
package me.sniperzciinema.infectedaddon.ranks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.sniperzciinema.infected.Events.InfectedCommandEvent;
import me.sniperzciinema.infected.Events.InfectedEndGame;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

	// Set up all the needed things for files
	public YamlConfiguration ranks = null;
	public File ranksFile = null;
	public Plugin me;
	public Permission perms;

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
				RanksManager.addRank(new Rank(
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
		RanksManager.getPresets();

		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
	}

	@EventHandler
	public void onInfectedCommand(InfectedCommandEvent event) {
		if (event.getArgs().length >= 1)
		{
			if (event.getArgs()[0].equalsIgnoreCase("Rank") || event.getArgs()[0].equalsIgnoreCase("Ranks") && event.getP() != null)
			{
				event.setCancelled(true);
				Player p = event.getP();
				Rank rank = RanksManager.getPlayersRank(p);
				Rank nextRank = RanksManager.getNextRank(p);
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
			}
		}
	}

	@EventHandler
	public void onInfectedGameEnd(InfectedEndGame event) {
		for (Player u : event.getPlayers())
			if (RanksManager.canRankUp(u))
				RanksManager.setPlayersRank(u, RanksManager.getNextRank(u));
	}

	@EventHandler
	public void onInfectedJoin(InfectedCommandEvent e) {
		if (!e.isCancelled() && e.getArgs()[0].toLowerCase().equals("join"))
		{
			Player p = e.getP();

			if (RanksManager.canRankUp(p))
				RanksManager.setPlayersRank(p, RanksManager.getNextRank(p));

			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			ip.setInfClass(Team.Human, RanksManager.getPlayersRank(p).getHumanClass());
			ip.setInfClass(Team.Zombie, RanksManager.getPlayersRank(p).getZombieClass());
			addPermissions(p);
		}
	}

	@EventHandler
	public void onInfectedLeave(InfectedCommandEvent e) {
		if (!e.isCancelled() && e.getArgs()[0].toLowerCase().equals("leave"))
		{
			Player p = e.getP();

			if (RanksManager.canRankUp(p))
				RanksManager.setPlayersRank(p, RanksManager.getNextRank(p));

			removePermissions(p);
		}
	}

	public void addPermissions(Player p) {
		for (String s : RanksManager.getPlayersRank(p).getPermissions())
			perms.playerAdd(p, s);
	}

	public void removePermissions(Player p) {
		for (String s : RanksManager.getPlayersRank(p).getPermissions())
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
