
package me.xxsniperzzxx_sd.infected.Handlers.Classes;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Handlers.Player.Team;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class InfClass {

	private String name;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private ArrayList<ItemStack> items;
	private ArrayList<PotionEffect> effects;
	private ArrayList<PotionEffect> transfereffects;
	private Team team;

	// TODO: Go through and change transfereffect to transfereffect
	public InfClass(String name, Team team, ItemStack helmet,
			ItemStack chestplate, ItemStack leggings, ItemStack boots,
			ArrayList<ItemStack> items, ArrayList<PotionEffect> effects,
			ArrayList<PotionEffect> transfereffects)
	{
		this.name = name;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.items = items;
		this.effects = effects;
		this.transfereffects = transfereffects;
		this.team = team;
	}

	/**
	 * @return the transfereffects
	 */
	public ArrayList<PotionEffect> getContacteffects() {
		return transfereffects;
	}

	/**
	 * @param transfereffects
	 *            the transfereffects to set
	 */
	public void setContacteffects(ArrayList<PotionEffect> transfereffects) {
		this.transfereffects = transfereffects;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
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
	 * @return the helmet
	 */
	public ItemStack getHelmet() {
		return helmet;
	}

	/**
	 * @param helmet
	 *            the helmet to set
	 */
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	/**
	 * @return the chestplate
	 */
	public ItemStack getChestplate() {
		return chestplate;
	}

	/**
	 * @param chestplate
	 *            the chestplate to set
	 */
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	/**
	 * @return the leggings
	 */
	public ItemStack getLeggings() {
		return leggings;
	}

	/**
	 * @param leggings
	 *            the leggings to set
	 */
	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	/**
	 * @return the boots
	 */
	public ItemStack getBoots() {
		return boots;
	}

	/**
	 * @param boots
	 *            the boots to set
	 */
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	/**
	 * @return the items
	 */
	public ArrayList<ItemStack> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(ArrayList<ItemStack> items) {
		this.items = items;
	}

	/**
	 * @return the effects
	 */
	public ArrayList<PotionEffect> getEffects() {
		return effects;
	}

	/**
	 * @param effects
	 *            the effects to set
	 */
	public void setEffects(ArrayList<PotionEffect> effects) {
		this.effects = effects;
	}

}
