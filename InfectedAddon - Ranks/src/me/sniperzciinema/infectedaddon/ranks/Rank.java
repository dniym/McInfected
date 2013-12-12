
package me.sniperzciinema.infectedaddon.ranks;

import java.util.List;

import org.bukkit.ChatColor;

import me.sniperzciinema.infected.Handlers.Classes.InfClass;


public class Rank {

	private String name;
	private String prefix;
	private InfClass humanClass;
	private InfClass zombieClass;
	private int scoreNeeded;
	private List<String> permissions;
	private boolean maxRank;
	private boolean defaultRank;
	
	public Rank(String name, String prefix, boolean defaultRank, boolean maxRank, int scoreNeeded, InfClass human, InfClass zombie, List<String> list)
	{
		this.setName(name);
		this.setPrefix(prefix);
		this.setMaxRank(maxRank);
		this.setDefaultRank(defaultRank);
		this.setScoreNeeded(scoreNeeded);
		this.setHumanClass(human);
		this.setZombieClass(zombie);
		this.setPermissions(list);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the scoreNeeded
	 */
	public int getScoreNeeded() {
		return scoreNeeded;
	}

	/**
	 * @param scoreNeeded the scoreNeeded to set
	 */
	public void setScoreNeeded(int scoreNeeded) {
		this.scoreNeeded = scoreNeeded;
	}

	/**
	 * @return the humanClass
	 */
	public InfClass getHumanClass() {
		return humanClass;
	}

	/**
	 * @param humanClass the humanClass to set
	 */
	public void setHumanClass(InfClass humanClass) {
		this.humanClass = humanClass;
	}

	/**
	 * @return the zombieClass
	 */
	public InfClass getZombieClass() {
		return zombieClass;
	}

	/**
	 * @param zombieClass the zombieClass to set
	 */
	public void setZombieClass(InfClass zombieClass) {
		this.zombieClass = zombieClass;
	}

	/**
	 * @return the permissions
	 */
	public List<String> getPermissions() {
		return permissions;
	}

	/**
	 * @param list the permissions to set
	 */
	public void setPermissions(List<String> list) {
		this.permissions = list;
	}

	/**
	 * @return the maxRank
	 */
	public boolean isMaxRank() {
		return maxRank;
	}

	/**
	 * @param maxRank the maxRank to set
	 */
	public void setMaxRank(boolean maxRank) {
		this.maxRank = maxRank;
	}

	/**
	 * @return the defaultRank
	 */
	public boolean isDefaultRank() {
		return defaultRank;
	}

	/**
	 * @param defaultRank the defaultRank to set
	 */
	public void setDefaultRank(boolean defaultRank) {
		this.defaultRank = defaultRank;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}