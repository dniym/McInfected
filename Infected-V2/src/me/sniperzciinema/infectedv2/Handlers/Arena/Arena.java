
package me.sniperzciinema.infectedv2.Handlers.Arena;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.sniperzciinema.infectedv2.Messages.StringUtil;
import me.sniperzciinema.infectedv2.Tools.Files;
import me.sniperzciinema.infectedv2.Tools.Settings;


public class Arena {

	private String name;
	private int Votes;
	private HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	private HashMap<Location, Material> blocks = new HashMap<Location, Material>();
	private Settings Settings = new Settings(this);

	public Arena(String name)
	{
		this.name = StringUtil.getWord(name);
	}


	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return Settings;
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
	 * 
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
	public void setSpawns(List<String> spawns) {
		Files.getArenas().set("Arenas." + name + ".Spawns", spawns);
	}

	/**
	 * Returns the spawns from the config
	 * 
	 * @return the spawns
	 */
	public List<String> getSpawns() {
		return Files.getArenas().getStringList("Arenas." + name + ".Spawns");
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
	 * 
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
	public void removeChest(Location loc){
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
	 * 
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
	public void removeBlock(Location loc){
		blocks.remove(loc);
	}
	/**
	 * Reset the arena
	 */
	public void reset(){
		if (!this.getBlocks().isEmpty())
			for (Location loc : this.getBlocks().keySet())
				this.setBlock(loc, this.getBlock(loc));

		this.getBlocks().clear();

		// Clear Chests too
		if (!this.getChests().isEmpty())
			for (Location loc : this.getChests().keySet())
				if (loc.getBlock().getType() == Material.CHEST)
					this.setChest(loc, this.getChest(loc));
	}

}
