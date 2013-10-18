
package me.sniperzciinema.infectedaddon.ranks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.xxsniperzzxx_sd.infected.Infected;
import me.xxsniperzzxx_sd.infected.Messages;
import me.xxsniperzzxx_sd.infected.Enums.Msgs;
import me.xxsniperzzxx_sd.infected.Events.InfectedCommandEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedGameEndEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerJoinEvent;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerLeaveEvent;
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

		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
	}

	@EventHandler
	public void onInfectedCommand(InfectedCommandEvent event) {
		if (event.getArgs().length >= 1)
		{
			if (event.getArgs()[0].equalsIgnoreCase("Rank") && event.getSender() instanceof Player)
			{
				event.setCancelled(true);
				Player p = (Player) event.getSender();
				p.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
				if (!canRankUp(p))
					p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "                      MAX RANK");
				p.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Your Current Rank: " + ChatColor.GRAY + rankPrefix(getRank(p)));
				p.sendMessage(ChatColor.GRAY + "Your Score: " + ChatColor.RED + getStat(p));
				if (canRankUp(p))
					p.sendMessage(ChatColor.GRAY + "Score to next rank: " + ChatColor.RED + (getRanks().getInt("Ranks." + nextRank(p) + ".Needed Score") - getStat(p)));
				if (canRankUp(p))
					p.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Next Rank: " + ChatColor.GRAY + rankPrefix(nextRank(p)) + ChatColor.RED + " - " + ChatColor.GRAY + " Unlocks at " + ChatColor.RED + getRanks().getInt("Ranks." + nextRank(p) + ".Needed Score"));
				if (!canRankUp(p))
					p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "                      MAX RANK");
				p.sendMessage(Messages.sendMessage(Msgs.FORMAT_LINE, null, null));
			}
		}
	}

	@EventHandler
	public void onInfectedGameEnd(InfectedGameEndEvent event) {
		for (String s : event.getPlayers())
		{
			if (canRankUp(Bukkit.getPlayer(s)))
				rankUp(Bukkit.getPlayer(s));
		}
	}

	@EventHandler
	public void onInfectedJoin(InfectedPlayerJoinEvent event) {
		if (!event.isCancelled())
		{
			setClasses(event.getPlayer());
			addPermissions(event.getPlayer());

			if (canRankUp(event.getPlayer()))
				rankUp(event.getPlayer());
		}
	}

	@EventHandler
	public void onInfectedLeave(InfectedPlayerLeaveEvent event) {
		if (!event.isCancelled())
			removePermissions(event.getPlayer());
		if (canRankUp(event.getPlayer()))
			rankUp(event.getPlayer());
	}

	public void setClasses(Player p) {
		if (getConfig().getString("Ranks." + getRank(p) + ".Default Class.Humans") != null)
			Infected.playersetLastHumanClass(p, getConfig().getString("Ranks." + getRank(p) + ".Default Class.Human"));
		if (getConfig().getString("Ranks." + getRank(p) + ".Default Class.Zombie") != null)
			Infected.playersetLastHumanClass(p, getConfig().getString("Ranks." + getRank(p) + ".Default Class.Zombie"));
	}

	public void addPermissions(Player p) {
		for (String s : getRanks().getStringList("Ranks." + getRank(p) + ".Permissions"))
			perms.playerAdd(p, s);
	}

	public void removePermissions(Player p) {
		for (String s : getRanks().getStringList("Ranks." + getRank(p) + ".Permissions"))
			perms.playerRemove(p, s);
	}

	public String getRank(Player p) {
		if (!getConfig().contains("Players." + p.getName()))
		{
			String rank = "";
			for (String s : getRanks().getConfigurationSection("Ranks").getKeys(true))
			{
				if (!s.contains("."))
				{
					if (getRanks().getInt("Ranks." + s + ".Needed Score") == 0)
					{
						rank = s;
					}
				}
			}

			getConfig().set("Players." + p.getName(), rank);
			saveConfig();
		}
		return getConfig().getString("Players." + p.getName());
	}

	public String rankPrefix(String rank) {
		return ChatColor.translateAlternateColorCodes('&', getRanks().getString("Ranks." + rank + ".Prefix"));
	}

	public boolean canRankUp(Player p) {

		boolean b = false;
		String rank = getRank(p);

		int pointsNeed = 0;

		for (String s : getRanks().getConfigurationSection("Ranks").getKeys(true))
			if (!s.contains("."))
			{
				if (!s.equalsIgnoreCase(rank))
				{
					if (getRanks().getInt("Ranks." + s + ".Needed Score") > getRanks().getInt("Ranks." + rank + ".Needed Score"))
					{
						if (getRanks().getInt("Ranks." + s + ".Needed Score") <= getStat(p))
						{
							if (getRanks().getInt("Ranks." + s + ".Needed Score") > pointsNeed)
							{
								pointsNeed = getRanks().getInt("Ranks." + s + ".Needed Score");
								b = true;
							}
						}
					}
				}
			}
		return b;
	}

	public String nextRank(Player p) {
		String rank = getRank(p);

		// Set the next rank to null
		String rankto = null;
		int pointsNeed = 0;

		for (String s : getRanks().getConfigurationSection("Ranks").getKeys(true))
			if (!s.contains("."))
			{

				// If the current rank and the rank isnt the same
				if (!s.equalsIgnoreCase(rank))
				{

					// If the score for the rank is less or equal to that of the
					// players
					if (getRanks().getInt("Ranks." + s + ".Needed Score") <= getStat(p))
					{

						// If the score of the rank is more then the previously
						// discovered rank's
						if (getRanks().getInt("Ranks." + s + ".Needed Score") > pointsNeed)
						{
							rankto = s;
							pointsNeed = getRanks().getInt("Ranks." + s + ".Needed Score");
						}
					}
				}
			}
		return rankto;

	}

	public void rankUp(Player p) {

		String rankto = nextRank(p);
		getConfig().set("Players." + p.getName(), rankto);
		saveConfig();
	}

	public int getStat(Player p) {
		if (getConfig().getBoolean("Use Score for ranks"))
		{
			return Infected.playerGetScore(p.getName());
		} else
			return Infected.playerGetPoints(p.getName());
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
