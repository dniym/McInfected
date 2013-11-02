
package me.sniperzciinema.infectedv2.Tools;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.infectedv2.Handlers.Arena.Arena;
import me.sniperzciinema.infectedv2.Handlers.Misc.ItemHandler;
import me.sniperzciinema.infectedv2.Handlers.Misc.PotionHandler;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class Settings {

	private Arena arena;

	public Settings(Arena arena)
	{
		this.arena = arena;
	}

	// /////////////////////////////////////////////-Integers-//////////////////////////////////////////////////////
	public int getGameTime() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Time.Game"))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Time.Game");
		else
			return Files.getConfig().getInt("Settings.Global.Time.Game");
	}

	public int getInfectingTime() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Time.Infecting"))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Time.Infecting");
		else
			return Files.getConfig().getInt("Settings.Global.Time.Infecting");
	}

	public int getVotingTime() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Time.Voting"))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Time.Voting");
		else
			return Files.getConfig().getInt("Settings.Global.Time.Voting");
	}

	public int getRequiredPlayers() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Auto Start.Required Players"))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Auto Start.Required Players");
		else
			return Files.getConfig().getInt("Settings.Global.Auto Start.Required Players");
	}

	public int getScorePer(Events e) {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Score." + e.toString()))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Score." + e.toString());
		else
			return Files.getConfig().getInt("Settings.Global.Score." + e.toString());
	}

	public int getPointsPer(Events e) {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Points." + e.toString()))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Points." + e.toString());
		else
			return Files.getConfig().getInt("Settings.Global.Points." + e.toString());
	}

	public int getAlphaPercent() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Infecting.Percent"))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Infecting.Percent");
		else
			return Files.getConfig().getInt("Settings.Global.Infecting.Percent");
	}

	// ////////////////////////////////////////////////-BOOLEANS-////////////////////////////////////////////////////

	public boolean canDropBlocks() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Misc.Can Drop Blocks"))
			return Files.getArenas().getBoolean("Arenas." + arena.getName() + ".Misc.Can Drop Blocks");
		else
			return Files.getConfig().getBoolean("Settings.Global.Misc.Can Drop Blocks");
	}

	public boolean canLooseHunger() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Misc.Can Loose Hunger"))
			return Files.getArenas().getBoolean("Arenas." + arena.getName() + ".Misc.Can Loose Hunger");
		else
			return Files.getConfig().getBoolean("Settings.Global.Misc.Can Loose Hunger");
	}

	public boolean canBreakBlock(int id) {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Breakable Blocks"))
			return Files.getArenas().getStringList("Arenas." + arena.getName() + ".Breakable Blocks").contains(id);
		else
			return Files.getConfig().getStringList("Settings.Global.Breakable Blocks").contains(id);
	}

	// ////////////////////////////////////////////-ITEMS-///////////////////////////////////////////////////////////

	public ItemStack getDefaultHead() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Equipment.Helmet"))
			return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + arena.getName() + ".Equipment.Helmet"));
		else
			return ItemHandler.getItemStack(Files.getConfig().getString("Settings.Global.Equipment.Helmet"));
	}

	public ItemStack getDefaultChest() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Equipment.Chest"))
			return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + arena.getName() + ".Equipment.Chest"));
		else
			return ItemHandler.getItemStack(Files.getConfig().getString("Settings.Global.Equipment.Chest"));
	}

	public ItemStack getDefaultLegs() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Equipment.Legs"))
			return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + arena.getName() + ".Equipment.Legs"));
		else
			return ItemHandler.getItemStack(Files.getConfig().getString("Settings.Global.Equipment.Legs"));
	}

	public ItemStack getDefaultFeet() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Equipment.Feet"))
			return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + arena.getName() + ".Equipment.Feet"));
		else
			return ItemHandler.getItemStack(Files.getConfig().getString("Settings.Global.Equipment.Feet"));
	}

	public ItemStack[] getDefaultItems() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Equipment.Items"))
			return ItemHandler.getItemStackList(Files.getArenas().getStringList("Arenas." + arena.getName() + ".Equipment.Items"));
		else
			return ItemHandler.getItemStackList(Files.getConfig().getStringList("Settings.Global.Equipment.Items"));
	}

	public ItemStack[] getRewordItems() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Rewards.Items"))
			return ItemHandler.getItemStackList(Files.getArenas().getStringList("Arenas." + arena.getName() + ".Rewards.Items"));
		else
			return ItemHandler.getItemStackList(Files.getConfig().getStringList("Settings.Global.Reward.Items"));
	}

	// /////////////////////////////////////////////////////////-STRINGS-///////////////////////////////////////////////////////

	public List<String> getRewordCommands() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Rewards.Commands"))
			return Files.getArenas().getStringList("Arenas." + arena.getName() + ".Rewards.Items");
		else
			return Files.getConfig().getStringList("Settings.Global.Rewards.Items");
	}
	// /////////////////////////////////////////////////////////-POTIONS-////////////////////////////////////////////////////////
	public ArrayList<PotionEffect> getAlphaPotionEffects() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Infecting.Alpha Potion Effects"))
			return PotionHandler.getPotions(Files.getArenas().getStringList("Arenas." + arena.getName() + ".Infecting.Alpha Potion Effects"));
		else
			return PotionHandler.getPotions(Files.getConfig().getStringList("Settings.Global.Infecting.Alpha Potion Effects"));
	}
}
