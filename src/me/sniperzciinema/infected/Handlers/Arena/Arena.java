
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

	private String							name;
	private int								Votes;
	private HashMap<Location, Inventory>	chests			= new HashMap<Location, Inventory>();
	private HashMap<Location, Material>		blocks			= new HashMap<Location, Material>();
	private ArenaSettings					ArenaSettings	= new ArenaSettings(this);

	public Arena(String name)
	{
		this.name = StringUtil.getWord(name);
	}

	/**
	 * @return the block
	 */
	public ItemStack getBlock() {
		return ItemHandler.getItemStack(Files.getArenas().getString("Arenas." + this.name + ".Block"));
	}

	/**
	 * Get the broken blocks
	 * 
	 * @param loc
	 * @return
	 */
	public Material getBlock(Location loc) {
		return this.blocks.get(loc);
	}

	/**
	 * @return the broken blocks
	 */
	public HashMap<Location, Material> getBlocks() {
		return this.blocks;
	}

	/**
	 * Get the opened chests
	 * 
	 * @param loc
	 * @return
	 */
	public Inventory getChest(Location loc) {
		return this.chests.get(loc);
	}

	public HashMap<Location, Inventory> getChests() {
		return this.chests;
	}

	/**
	 * Returns the arenas creator
	 * 
	 * @return the creator
	 */
	public String getCreator() {
		return Files.getArenas().getString("Arenas." + this.name + ".Creator");
	}

	public List<String> getExactSpawns(Team team) {
		List<String> spawns = new ArrayList<String>();
		if ((team == Team.Global) || (team == Team.None))
			spawns.addAll(Files.getArenas().getStringList("Arenas." + this.name + ".Spawns"));
		else
			spawns.addAll(Files.getArenas().getStringList("Arenas." + this.name + "." + team.toString() + " Spawns"));

		return spawns;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the settings
	 */
	public ArenaSettings getSettings() {
		return this.ArenaSettings;
	}

	/**
	 * Returns the spawns from the config
	 * 
	 * @return the spawns
	 */
	public List<String> getSpawns(Team team) {
		List<String> spawns = Files.getArenas().getStringList("Arenas." + this.name + ".Spawns");

		if ((team != Team.Global) && (team != Team.None))
			spawns.addAll(Files.getArenas().getStringList("Arenas." + this.name + "." + team.toString() + " Spawns"));

		return spawns;
	}

	/**
	 * @return the votes
	 */
	public int getVotes() {
		return this.Votes;
	}

	/**
	 * Removes a broken block
	 * 
	 * @param loc
	 */
	public void removeBlock(Location loc) {
		this.blocks.remove(loc);
	}

	/**
	 * Remove a opened chest
	 * 
	 * @param loc
	 */
	public void removeChest(Location loc) {
		this.chests.remove(loc);
	}

	/**
	 * Reset the arena
	 */
	public void reset() {
		if (!getBlocks().isEmpty())
			for (Location loc : getBlocks().keySet())
				loc.getBlock().setType(getBlock(loc));

		getBlocks().clear();

		// Clear Chests too
		if (!getChests().isEmpty())
			for (Location loc : getChests().keySet())
				if (loc.getBlock().getType() == Material.CHEST)
					((Chest) loc.getBlock()).getBlockInventory().setContents(getChests().get(loc).getContents());

		getChests().clear();

		setVotes(0);
	}

	/**
	 * @param block
	 *            the block to set
	 */
	@SuppressWarnings("deprecation")
	public void setBlock(ItemStack is) {
		if ((is.getType() == null) || (is.getType().getId() == 0))
			Files.getArenas().set("Arenas." + this.name + ".Block", "id:395");
		else
			Files.getArenas().set("Arenas." + this.name + ".Block", ItemHandler.getItemStackToString(is));
		Files.saveArenas();
	}

	/**
	 * Set the blocks broken
	 * 
	 * @param loc
	 * @param mat
	 */
	public void setBlock(Location loc, Material mat) {
		this.blocks.put(loc, mat);
	}

	/**
	 * Set the opened chests
	 * 
	 * @param loc
	 * @param inv
	 */
	public void setChest(Location loc, Inventory inv) {
		this.chests.put(loc, inv);
	}

	/**
	 * @param string
	 *            the arena maker
	 */
	public void setCreator(String maker) {
		Files.getArenas().set("Arenas." + this.name + ".Creator", maker);
		Files.saveArenas();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the spawns in the config
	 * 
	 * @param spawns
	 *            the spawns to set
	 */
	public void setSpawns(List<String> spawns, Team team) {
		if ((team == Team.Zombie) || (team == Team.Human))
			Files.getArenas().set("Arenas." + this.name + "." + team.toString() + " Spawns", spawns);
		else
			Files.getArenas().set("Arenas." + this.name + ".Spawns", spawns);

		Files.saveArenas();
	}

	/**
	 * @param votes
	 *            the votes to set
	 */
	public void setVotes(int votes) {
		this.Votes = votes;
	}

}
