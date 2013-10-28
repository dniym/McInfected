
package me.xxsniperzzxx_sd.infected.Handlers.Arena;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.xxsniperzzxx_sd.infected.Handlers.Lobby;
import me.xxsniperzzxx_sd.infected.Tools.Files;


public class Arena {

	private Lobby Lobby;
	private String name;
	private int Votes;
	private HashMap<Location, Inventory> chests = new HashMap<Location, Inventory>();
	private HashMap<Location, Material> blocks = new HashMap<Location, Material>();

	public Arena(Lobby Lobby, String name)
	{
		this.Lobby = Lobby;
		this.name = name;
	}

	/**
	 * @param spawns
	 *            the spawns to set
	 */
	public void setSpawns(List<String> spawns) {
		Files.getArenas().set("Arenas." + name + ".Spawns", spawns);
	}

	/**
	 * @return the spawns
	 */
	public List<String> getSpawns() {
		return Files.getArenas().getStringList("Arenas." + name + ".Spawns");
	}

	/**
	 * @return the lobby
	 */
	public Lobby getLobby() {
		return Lobby;
	}

	/**
	 * @param lobby
	 *            the lobby to set
	 */
	public void setLobby(Lobby lobby) {
		Lobby = lobby;
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

	// Get the saved chests
	public HashMap<Location, Inventory> getChests() {
		return chests;
	}

	// Get the chest at Loc
	public Inventory getChest(Location loc) {
		return chests.get(loc);
	}

	// Set a chest at Loc
	public void setChest(Location loc, Inventory inv) {
		chests.put(loc, inv);
	}

	// Get the saved Blocks
	public Material getBlock(Location loc) {
		return blocks.get(loc);
	}

	// Get the block at Loc
	public HashMap<Location, Material> getBlocks() {
		return blocks;
	}

	// Set the block at Loc
	public void setBlock(Location loc, Material mat) {
		blocks.put(loc, mat);
	}

}
