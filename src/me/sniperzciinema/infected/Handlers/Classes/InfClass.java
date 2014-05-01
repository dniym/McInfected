
package me.sniperzciinema.infected.Handlers.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.StringUtil;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


public class InfClass {
	
	private String											name;
	private ItemStack										helmet;
	private ItemStack										chestplate;
	private ItemStack										leggings;
	private ItemStack										boots;
	private ItemStack										icon;
	private List<String>								desc	= new ArrayList<String>();
	private ArrayList<ItemStack>				items;
	private ArrayList<PotionEffect>			effects;
	private ArrayList<PotionEffect>			transfereffects;
	private Team												team;
	private String											disguise;
	private HashMap<Integer, ItemStack>	killstreaks;
	
	public InfClass(String name, Team team, ItemStack helmet, ItemStack chestplate,
			ItemStack leggings, ItemStack boots, ArrayList<ItemStack> items,
			ArrayList<PotionEffect> effects, ArrayList<PotionEffect> transfereffects,
			HashMap<Integer, ItemStack> killstreaks, String disguise, ItemStack icon, List<String> desc)
	{
		this.name = name;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		this.icon = icon;
		this.items = items;
		this.effects = effects;
		this.transfereffects = transfereffects;
		this.team = team;
		setKillstreaks(killstreaks);
		setDisguise(disguise);
		if (!desc.isEmpty())
			for (String string : desc)
				this.desc.add(StringUtil.format(string));
	}
	
	/**
	 * @return the boots
	 */
	public ItemStack getBoots() {
		return this.boots;
	}
	
	/**
	 * @return the chestplate
	 */
	public ItemStack getChestplate() {
		return this.chestplate;
	}
	
	/**
	 * @return the transfereffects
	 */
	public ArrayList<PotionEffect> getContactEffects() {
		return this.transfereffects;
	}
	
	/**
	 * @return the desc
	 */
	public List<String> getDesc() {
		return this.desc;
	}
	
	/**
	 * @return the disguise
	 */
	public String getDisguise() {
		return this.disguise;
	}
	
	/**
	 * @return the effects
	 */
	public ArrayList<PotionEffect> getEffects() {
		return this.effects;
	}
	
	/**
	 * @return the helmet
	 */
	public ItemStack getHelmet() {
		return this.helmet;
	}
	
	/**
	 * @return the icon
	 */
	public ItemStack getIcon() {
		if (this.icon == null)
			this.icon = this.items.get(0).clone();
		return this.icon;
	}
	
	/**
	 * @return the items
	 */
	public ArrayList<ItemStack> getItems() {
		return this.items;
	}
	
	/**
	 * @return the killstreaks
	 */
	public HashMap<Integer, ItemStack> getKillstreaks() {
		return this.killstreaks;
	}
	
	/**
	 * @return the leggings
	 */
	public ItemStack getLeggings() {
		return this.leggings;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the team
	 */
	public Team getTeam() {
		return this.team;
	}
	
	/**
	 * @param boots
	 *          the boots to set
	 */
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}
	
	/**
	 * @param chestplate
	 *          the chestplate to set
	 */
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}
	
	/**
	 * @param transfereffects
	 *          the transfereffects to set
	 */
	public void setContactEffects(ArrayList<PotionEffect> transfereffects) {
		this.transfereffects = transfereffects;
	}
	
	/**
	 * @param desc
	 *          the desc to set
	 */
	public void setDesc(List<String> desc) {
		this.desc = desc;
	}
	
	/**
	 * @param disguise
	 *          the disguise to set
	 */
	public void setDisguise(String disguise) {
		this.disguise = disguise;
	}
	
	/**
	 * @param effects
	 *          the effects to set
	 */
	public void setEffects(ArrayList<PotionEffect> effects) {
		this.effects = effects;
	}
	
	/**
	 * @param helmet
	 *          the helmet to set
	 */
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}
	
	/**
	 * @param icon
	 *          the icon to set
	 */
	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}
	
	/**
	 * @param items
	 *          the items to set
	 */
	public void setItems(ArrayList<ItemStack> items) {
		this.items = items;
	}
	
	/**
	 * @param killstreaks
	 *          the killstreaks to set
	 */
	public void setKillstreaks(HashMap<Integer, ItemStack> killstreaks) {
		this.killstreaks = killstreaks;
	}
	
	/**
	 * @param leggings
	 *          the leggings to set
	 */
	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}
	
	/**
	 * @param name
	 *          the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param team
	 *          the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}
	
}
