
package me.sniperzciinema.infectedv2.Handlers.Grenades;

import java.util.ArrayList;
import java.util.UUID;

import me.sniperzciinema.infectedv2.Tools.Files;


public class GrenadeManager {

	private static ArrayList<Grenade> grenades = new ArrayList<Grenade>();
	private static ArrayList<UUID> thrownGrenades = new ArrayList<UUID>();

	public static void addGrenade(int id) {
		Grenade grenade = new Grenade(id);
		grenades.add(grenade);
	}
	public static ArrayList<Grenade> getGrenades(){
		return grenades;
	}
	public static ArrayList<UUID> getThrownGrenade(){
		return thrownGrenades;
	}
	public static void addThrownGrenade(UUID uuid){
		thrownGrenades.add(uuid);
	}
	public static void delThrownGrenade(UUID uuid){
		thrownGrenades.remove(uuid);
	}
	public static boolean isThrownGrenade(UUID uuid){
		return thrownGrenades.contains(uuid);
	}

	public static boolean isGrenade(int id) {
		for(Grenade grenade : grenades){
			if(grenade.getId() == id)
				return true;
		}
		return false;
	}

	public static Grenade getGrenade(int id) {
			for (Grenade grenade : grenades)
			{
				if (grenade.getId() == id)
					return grenade;
			}
		return null;
	}


	public static void loadConfigGrenades() {
		for (String s : Files.getClasses().getConfigurationSection("Grenades").getKeys(false))
		{
			if (!isGrenade(Integer.valueOf(s))){
				addGrenade(Integer.valueOf(s));
			}
		}
	}
}
