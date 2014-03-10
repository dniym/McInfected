
package me.sniperzciinema.infected.Handlers.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.StringUtil;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Arena {

	private String name;
	private int Votes;
	private HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	private HashMap<Location, Material> blocks = new HashMap<Location, Material>();
	private ArenaSettings ArenaSettings = new ArenaSettings(this);

	public Arena(String name)
	{
		this.name = StringUtil.getWord(name);
	}

	/**
	 * @return the settings
	 */
	public ArenaSettings getSettings() {
		return ArenaSettings;
	}

	/**
	 * Returns the arenas creator
	 * 
	 * @return the creator
	 */
	public String getCreator() {
		return Files.getArenas().getString("Arenas." + name + ".Creator");
	}

	/**
	 * @param string
	 *            the arena maker
	 */
	public void setCreator(String maker) {
		Files.getArenas().set("Arenas." + name + ".Creator", maker);
		Files.saveArenas();
	}

	/**
	 * Set the spawns in the config
	 * 
	 * @param spawns
	 *            the spawns to set
	 */
	public void setSpawns(List<String> spawns, Team team) {
		if (team == Team.Zombie || team == Team.Human)
			Files.getArenas().set("Arenas." + name + "." + team.toString() + " Spawns", spawns);
		else
			Files.getArenas().set("Arenas." + name + ".Spawns", spawns);

		Files.saveArenas();
	}

	/**
	 * Returns the spawns from the config
	 * 
	 * @return the spawns
	 */
	public List<String> getSpawns(Team team) {
		List<String> spawns = Files.getArenas().getStringList("Arenas." + name + ".Spawns");

		if (team != Team.Global && team != Team.None)
			spawns.addAll(Files.getArenas().getStringList("Arenas." + name + "." + team.toString() + " Spawns"));

		return spawns;
	}

	public List<String> getExactSpawns(Team team) {
		List<String> spawns = new ArrayList<String>();
		if (team == Team.Global || team == Team.None)
			spawns.addAll(Files.getArenas().getStringList("Arenas." + name + ".Spawns"));
		else
			spawns.addAll(Files.getArenas().getStringList("Arenas." + name + "." + team.toString() + " Spawns"));

		return spawns;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the votes
	 */
	public int getVotes() {
		return Votes;
	}

	/**
	 * @param votes
	 *            the votes to set
	 */
	public void setVotes(int votes) {
		Votes = votes;
	}

	public HashMap<Location, Inventory> getChests() {
		return chests;
	}

	/**
	 * Get the opened chests
	 * 
	 * @param loc
	 * @return
	 */
	public Inventory getChest(Location loc) {
		return chests.get(loc);
	}

	/**
	 * Set the opened chests
	 * 
	 * @param loc
	 * @param inv
	 */
	public void setChest(Location loc, Inventory inv) {
		chests.put(loc, inv);
	}

	/**
	 * Remove a opened chest
	 * 
	 * @param loc
	 */
	public void removeChest(Location loc) {
		chests.remove(loc);
	}

	/**
	 * Get the broken blocks
	 * 
	 * @param loc
	 * @return
	 */
	public Material getBlock(Location loc) {
		return blocks.get(loc);
	}

	/**
	 * @return the broken blocks
	 */
	public HashMap<Location, Material> getBlocks() {
		return blocks;
	}

	/**
	 * Set the blocks broken
	 * 
	 * @param loc
	 * @param mat
	 */
	public void setBlock(Location loc, Material mat) {
		blocks.put(loc, mat);
	}

	/**
	 * Removes a broken block
	 * 
	 * @param loc
	 */
	public void removeBlock(Location loc) {
		blocks.remove(loc);
	}

	/**
	 * Reset the arena
	 */
	public void reset() {
		if (!this.getBlocks().isEmpty())
			for (Location loc : this.getBlocks().keySet())
				loc.getBlock().setType(getBlock(loc));

		this.getBlocks().clear();

		// Clear Chests too
		if (!this.getChests().isEmpty())
			for (Location loc : this.getChests().keySet())
				if (loc.getBlock().getType() == Material.CHEST)
					((Chest) loc.getBlock()).getBlockInventory().setContents(getChests().get(loc).getContents());

		this.getChests().clear();

		setVotes(0);
	}

	/**
	 * @return the block
	 */
	public ItemStack getBlock() {
		return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + name + ".Block"));
	}

	/**
	 * @param block
	 *            the block to set
	 */
	@SuppressWarnings("deprecation")
	public void setBlock(ItemStack is) {
		if (is.getType() == null || is.getType().getId() == 0)
			Files.getArenas().set("Arenas." + name + ".Block", "id:395");
		else
			Files.getArenas().set("Arenas." + name + ".Block", ItemHandler.getItemStackToString(is));
		Files.saveArenas();
	}

}
