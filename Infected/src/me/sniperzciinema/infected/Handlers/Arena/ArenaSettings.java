
package me.sniperzciinema.infected.Handlers.Arena;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.infected.Enums.Events;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Handlers.Potions.PotionHandler;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class ArenaSettings {

	private Arena arena;

	public ArenaSettings(Arena arena)
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
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Percent to Infect"))
			return Files.getArenas().getInt("Arenas." + arena.getName() + ".Percent to Intect");
		else
			return Files.getConfig().getInt("Settings.Global.Percent to Infect");
	}

	// ////////////////////////////////////////////////-BOOLEANS-////////////////////////////////////////////////////

	public boolean canDropBlocks() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Misc.Can Drop Blocks"))
			return Files.getArenas().getBoolean("Arenas." + arena.getName() + ".Misc.Can Drop Items");
		else
			return Files.getConfig().getBoolean("Settings.Global.Misc.Can Drop Items");
	}

	public boolean canLooseHunger() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Misc.Can Loose Hunger"))
			return Files.getArenas().getBoolean("Arenas." + arena.getName() + ".Misc.Can Loose Hunger");
		else
			return Files.getConfig().getBoolean("Settings.Global.Misc.Can Loose Hunger");
	}

	public boolean canEnchant() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Misc.Can Enchant"))
			return Files.getArenas().getBoolean("Arenas." + arena.getName() + ".Misc.Can Enchant");
		else
			return Files.getConfig().getBoolean("Settings.Global.Misc.Can Enchant");
	}
	public boolean canInteract() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Misc.Can Interact"))
			return Files.getArenas().getBoolean("Arenas." + arena.getName() + ".Misc.Can Interact");
		else
			return Files.getConfig().getBoolean("Settings.Global.Misc.Can Interact");
	}	
	public Boolean canBreakBlock(Team team, int id) {
		if(Files.getArenas().contains("Arenas." + arena.getName() + ".Breakable Blocks.Global"))
			return Files.getArenas().getStringList("Arenas." + arena.getName() + ".Breakable Blocks.Global").contains(id);
		else if (Files.getArenas().contains("Arenas." + arena.getName() + ".Breakable Blocks."+team.toString()))
			return Files.getArenas().getStringList("Arenas." + arena.getName() + ".Breakable Blocks."+team.toString()).contains(id);
		else if(Files.getConfig().getStringList("Settings.Global.Breakable Blocks.Global").contains(id))
			return Files.getConfig().getStringList("Settings.Global.Breakable Blocks.Global").contains(id);
		else
			return Files.getConfig().getStringList("Settings.Global.Breakable Blocks."+team.toString()).contains(id);
	}

	// ////////////////////////////////////////////-ITEMS-///////////////////////////////////////////////////////////

	public ArrayList<ItemStack> getRewordItems() {
		if (Files.getArenas().contains("Arenas." + arena.getName() + ".Rewards"))
			return ItemHandler.getItemStackList(Files.getArenas().getStringList("Arenas." + arena.getName() + ".Rewards"));
		else
			return ItemHandler.getItemStackList(Files.getConfig().getStringList("Settings.Global.Rewards"));
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
	
	// /////////////////////////////////////////////////////////-LIST-////////////////////////////////////////////////////////

}

