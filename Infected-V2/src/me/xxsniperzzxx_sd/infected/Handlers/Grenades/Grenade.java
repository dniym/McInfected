
package me.xxsniperzzxx_sd.infected.Handlers.Grenades;

import java.util.ArrayList;

import me.xxsniperzzxx_sd.infected.Handlers.Misc.PotionHandler;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;


public class Grenade {

	private String name;
	private int id;
	private ArrayList<PotionEffect> effects;
	private int damage;
	private int delay;
	private int range;
	private Player thrower;
	private boolean damageThrower;

	public Grenade(int id)
	{
		this.setId(id);
		String path = "Grenades."+String.valueOf(id)+".";
		this.name = Files.getGrenades().getString(path + "Name");
		this.setDamage(Files.getGrenades().getInt(path + "Damage"));
		this.setDelay(Files.getGrenades().getInt(path + "Delay"));
		this.setRange(Files.getGrenades().getInt(path + "Range"));
		this.setDamageThrower(Files.getGrenades().getBoolean(path + "Damage Thrower"));
		ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
		for(String string : Files.getGrenades().getStringList(path + "Potion Effects"))
			potions.add(PotionHandler.getPotion(string));
		
		this.setEffects(potions);
	}
	
	public void addPotionEffect(Player p){
		for(PotionEffect PE : getEffects())
			p.addPotionEffect(PE);
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
	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	/**
	 * @return the thrower
	 */
	public Player getThrower() {
		return thrower;
	}
	/**
	 * @param thrower the thrower to set
	 */
	public void setThrower(Player thrower) {
		this.thrower = thrower;
	}
	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}
	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}
	/**
	 * @return the damageThrower
	 */
	public boolean isDamageThrower() {
		return damageThrower;
	}
	/**
	 * @param damageThrower the damageThrower to set
	 */
	public void setDamageThrower(boolean damageThrower) {
		this.damageThrower = damageThrower;
	}
	/**
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}
	/**
	 * @param damage the damage to set
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
