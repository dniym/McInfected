
package me.sniperzciinema.infected.Tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.sniperzciinema.infected.Infected;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Files {

	// Set up all the needed things for files
	private static YamlConfiguration	classes			= null;
	private static File					classesFile		= null;
	private static YamlConfiguration	arenas			= null;
	private static File					arenasFile		= null;
	private static YamlConfiguration	playerF			= null;
	private static File					playerFile		= null;
	private static YamlConfiguration	messages		= null;
	private static File					messagesFile	= null;
	private static YamlConfiguration	shop			= null;
	private static File					shopFile		= null;
	private static YamlConfiguration	grenades		= null;
	private static File					grenadesFile	= null;
	private static YamlConfiguration	signs			= null;
	private static File					signsFile		= null;

	public static FileConfiguration getArenas() {
		if (Files.arenas == null)
			reloadArenas();
		return Files.arenas;
	}

	public static FileConfiguration getClasses() {
		if (Files.classes == null)
			reloadClasses();
		return Files.classes;
	}

	public static FileConfiguration getConfig() {
		return Infected.me.getConfig();
	}

	public static FileConfiguration getGrenades() {
		if (Files.grenades == null)
			reloadGrenades();
		return Files.grenades;
	}

	public static FileConfiguration getMessages() {
		if (Files.messages == null)
			reloadMessages();
		return Files.messages;
	}

	public static FileConfiguration getPlayers() {
		if (Files.playerF == null)
		{
			reloadPlayers();
			savePlayers();
		}
		return Files.playerF;
	}

	public static FileConfiguration getShop() {
		if (Files.shop == null)
			reloadShop();
		return Files.shop;
	}

	public static FileConfiguration getSigns() {
		if (Files.signs == null)
		{
			reloadSigns();
			saveSigns();
		}
		return Files.signs;
	}

	public static void reloadAll() {
		reloadConfig();
		reloadClasses();
		reloadArenas();
		reloadPlayers();
		reloadMessages();
		reloadShop();
		reloadGrenades();
		reloadSigns();
	}

	public static void reloadArenas() {
		if (Files.arenasFile == null)
			Files.arenasFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Arenas.yml");
		Files.arenas = YamlConfiguration.loadConfiguration(Files.arenasFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Arenas.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!Files.arenasFile.exists() || (Files.arenasFile.length() == 0))
				Files.arenas.setDefaults(defConfig);
		}
	}

	public static void reloadClasses() {
		if (Files.classesFile == null)
			Files.classesFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Classes.yml");
		Files.classes = YamlConfiguration.loadConfiguration(Files.classesFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Classes.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!Files.classesFile.exists() || (Files.classesFile.length() == 0))
				Files.classes.setDefaults(defConfig);
		}
	}

	public static void reloadConfig() {
		Infected.me.reloadConfig();
	}

	public static void reloadGrenades() {
		if (Files.grenades == null)
			Files.grenadesFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Grenades.yml");
		Files.grenades = YamlConfiguration.loadConfiguration(Files.grenadesFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Grenades.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!Files.grenadesFile.exists() || (Files.grenadesFile.length() == 0))
				Files.grenades.setDefaults(defConfig);
		}
	}

	public static void reloadMessages() {
		if (Files.messages == null)
			Files.messagesFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Messages.yml");
		Files.messages = YamlConfiguration.loadConfiguration(Files.messagesFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Messages.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			Files.messages.setDefaults(defConfig);
		}
	}

	public static void reloadPlayers() {
		if (Files.playerFile == null)
			Files.playerFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Players.yml");
		Files.playerF = YamlConfiguration.loadConfiguration(Files.playerFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Players.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!Files.playerFile.exists() || (Files.playerFile.length() == 0))
				Files.playerF.setDefaults(defConfig);
		}
	}

	public static void reloadShop() {
		if (Files.shop == null)
			Files.shopFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Shop.yml");
		Files.shop = YamlConfiguration.loadConfiguration(Files.shopFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Shop.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!Files.shopFile.exists() || (Files.shopFile.length() == 0))
				Files.shop.setDefaults(defConfig);
		}
	}

	public static void reloadSigns() {
		if (Files.signsFile == null)
			Files.signsFile = new File(
					Bukkit.getPluginManager().getPlugin("Infected").getDataFolder(), "Signs.yml");
		Files.signs = YamlConfiguration.loadConfiguration(Files.signsFile);
		// Look for defaults in the jar
		InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("Infected").getResource("Signs.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			if (!Files.signsFile.exists() || (Files.signsFile.length() == 0))
				Files.signs.setDefaults(defConfig);
		}
	}

	public static void saveAll() {
		saveConfig();
		saveClasses();
		saveArenas();
		savePlayers();
		saveMessages();
		saveShop();
		saveGrenades();
		saveSigns();
	}

	public static void saveArenas() {
		if ((Files.arenas == null) || (Files.arenasFile == null))
			return;
		try
		{
			getArenas().save(Files.arenasFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.arenasFile, ex);
		}
	}

	public static void saveClasses() {
		if ((Files.classes == null) || (Files.classesFile == null))
			return;
		try
		{
			getClasses().save(Files.classesFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.classesFile, ex);
		}
	}

	public static void saveConfig() {
		Infected.me.saveConfig();
	}

	// Safe Arenas File
	public static void saveGrenades() {
		if ((Files.grenades == null) || (Files.grenadesFile == null))
			return;
		try
		{
			getGrenades().save(Files.grenadesFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.grenadesFile, ex);
		}
	}

	public static void saveMessages() {
		if ((Files.messages == null) || (Files.messagesFile == null))
			return;
		try
		{
			getMessages().save(Files.messagesFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.messagesFile, ex);
		}
	}

	public static void savePlayers() {
		if ((Files.playerF == null) || (Files.playerFile == null))
			return;
		try
		{
			getPlayers().save(Files.playerFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.playerFile, ex);
		}
	}

	public static void saveShop() {
		if ((Files.shop == null) || (Files.shopFile == null))
			return;
		try
		{
			getShop().save(Files.shopFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.shopFile, ex);
		}
	}

	public static void saveSigns() {
		if ((Files.signs == null) || (Files.signsFile == null))
			return;
		try
		{
			getSigns().save(Files.signsFile);
		}
		catch (IOException ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Could not save config " + Files.signsFile, ex);
		}
	}

	public static void updateAll() {
		getArenas().options().copyDefaults(true);
		getShop().options().copyDefaults(true);
		getPlayers().options().copyDefaults(true);
		getMessages().options().copyDefaults(true);
		getGrenades().options().copyDefaults(true);
		getClasses().options().copyDefaults(true);
		getSigns().options().copyDefaults(true);
		getMessages().options().copyDefaults(true);
		getConfig().options().copyDefaults(true);
	}

	public Files()
	{
	}
}
