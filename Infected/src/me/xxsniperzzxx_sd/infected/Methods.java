
package me.xxsniperzzxx_sd.infected;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import me.xxsniperzzxx_sd.infected.Disguise.Disguises;
import me.xxsniperzzxx_sd.infected.GameMechanics.Equip;
import me.xxsniperzzxx_sd.infected.GameMechanics.ScoreBoard;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.ItemSerialization;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Methods {

	private static HashMap<String, Integer> Stats = new HashMap<String, Integer>();

	// ===============================================================================
	public static void rewardPointsAndScore(Player player, String PointsCause) {
		if (Main.config.getBoolean("Points.Use"))
		{
			if (Main.config.getBoolean("Debug"))
				System.out.print(Main.KillStreaks.toString());
			if (Main.KillStreaks.containsKey(player.getName()))
			{
				if (!(Main.KillStreaks.get(player.getName()) == 0))
				{
					int times = Main.KillStreaks.get(player.getName()) / 2;
					Main.timest = times;
				} else
				{
					int times = 1;
					Main.timest = times;
				}
			} else
				Main.timest = 1;
			int score = Main.config.getInt("Score." + PointsCause) * Main.timest;
			int reward = Main.config.getInt("Points." + PointsCause);

			reward = Main.config.getInt("Points." + PointsCause);
			if (Infected.playerGetPoints(player) > Main.config.getInt("Points.Max Points"))
				player.sendMessage(Main.I + ChatColor.RED + "You have exceded the max points!");
			if (Infected.playerGetScore(player) > Main.config.getInt("Score.Max Score"))
				player.sendMessage(Main.I + ChatColor.RED + "You have exceded the max score!");
			else
			{
				Infected.playerSetPoints(player, Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Points") + reward, 0);
				Infected.playerSetScore(player, Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Score") + score);
				Files.savePlayers();
				player.sendMessage(Main.I + ChatColor.AQUA + "Points +" + reward);
				Files.savePlayers();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void applyAbilities(Player player) {
		Random r = new Random();
		int n = 0;
		for (String abilities : Infected.filesGetAbilities().getConfigurationSection("Abilities").getKeys(true))
		{
			if (!abilities.contains("."))
			{
				n += Infected.filesGetAbilities().getInt("Abilities." + abilities + ".Chance");
			}
		}
		int i = r.nextInt(n);
		n = 0;
		for (String abilities : Infected.filesGetAbilities().getConfigurationSection("Abilities").getKeys(true))
		{
			n += Infected.filesGetAbilities().getInt("Abilities." + abilities + ".Chance");
			if (n > i)
			{
				Integer id = 0;
				Integer time = 0;
				Integer power = 0;
				int max = Infected.filesGetAbilities().getStringList("Abilities." + abilities + ".Potion Effects").size();
				for (int x = 0; x < max; x++)
				{
					String path = Infected.filesGetAbilities().getStringList("Abilities." + abilities + ".Potion Effects").get(x);
					String[] strings = path.split(":");
					id = Integer.valueOf(strings[0]);
					time = Integer.valueOf(strings[1]) * 20;
					power = Integer.valueOf(strings[2]) - 1;
					player.addPotionEffect(new PotionEffect(
							PotionEffectType.getById(id), time, power));
				}
				player.sendMessage(Main.I + abilities);
				break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void applyClassAbility(Player player) {

		for (PotionEffect reffect : player.getActivePotionEffects())
		{
			player.removePotionEffect(reffect.getType());
		}
		Integer id = 0;
		Integer time = 0;
		Integer power = 0;
		int max = 0;
		String path = "";
		if (Infected.isPlayerZombie(player))
		{
			max = Infected.filesGetClasses().getStringList("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Potion Effects").size();
		} else
		{
			max = Infected.filesGetClasses().getStringList("Classes.Human." + Main.humanClasses.get(player.getName()) + ".Potion Effects").size();
		}
		for (int x = 0; x < max; x++)
		{
			if (Infected.isPlayerZombie(player))
				path = Infected.filesGetClasses().getStringList("Classes.Zombie." + Main.zombieClasses.get(player.getName()) + ".Potion Effects").get(x);
			else
				path = Infected.filesGetClasses().getStringList("Classes.Human." + Main.humanClasses.get(player.getName()) + ".Potion Effects").get(x);

			String[] strings = path.split(":");
			id = Integer.valueOf(strings[0]);
			time = Integer.valueOf(strings[1]) * 20;
			power = Integer.valueOf(strings[2]) - 1;
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.getById(id), time, power));
		}
	}

	public static void zombifyPlayer(Player player) {
		if (Main.zombieClasses.containsKey(player.getName()))
		{
			applyClassAbility(player);
		} else if (Main.config.getBoolean("Zombie Abilities") == true)
		{
			applyAbilities(player);
		}
		if (Main.config.getBoolean("Disguise Support.Enabled"))
			Disguises.disguisePlayer(player);

	}

	public static String grenadeGetName(Integer id) {
		return Files.getGrenades().getString(id + ".Name");
	}

	public static boolean grenadeTakeAfter(Integer id) {
		return Files.getGrenades().getBoolean(id + ".Take After Thrown");
	}

	public static int grenadeGetDamage(Integer id) {
		return Files.getGrenades().getInt(id + ".Damage");
	}

	public static int grenadeGetCost(Integer id) {
		return Files.getGrenades().getInt(id + ".Cost");
	}

	public static int grenadeGetRange(Integer id) {
		return Files.getGrenades().getInt(id + ".Range");
	}

	public static int grenadeGetDelay(Integer id) {
		return Files.getGrenades().getInt(id + ".Delay") * 20;
	}

	public static boolean grenadeDamageSelf(Integer id) {
		return Files.getGrenades().getBoolean(id + ".Damage Self");
	}

	@SuppressWarnings("deprecation")
	public static void grenadeAddPotion(Player player, Integer Itemid) {
		Integer id = 0;
		Integer time = 0;
		Integer power = 0;
		int max = Files.getGrenades().getStringList(Itemid + ".Effects").size();
		for (int x = 0; x < max; x = x + 1)
		{
			String path = Files.getGrenades().getStringList(Itemid + ".Effects").get(x);
			String[] strings = path.split(":");
			id = Integer.valueOf(strings[0]);
			time = Integer.valueOf(strings[1]) * 20;
			power = Integer.valueOf(strings[2]);
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.getById(id), time, power));
		}

	}


	public static void saveInventory(Player player, String loc) {
		String data = ItemSerialization.toBase64(player.getInventory());
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + "." + loc, data);
		Files.savePlayers();
	}

	public static void addItemToInventory(Player player, String loc, ItemStack items) {
		if (Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc) == null)
		{
			saveInventory(player, loc);
		}
		Inventory copy = ItemSerialization.fromBase64(Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc));
		copy.addItem(items);
		String done = ItemSerialization.toBase64(copy);
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + "." + loc, done);
		Files.savePlayers();
	}

	public static ItemStack[] getInventory(Player player, String loc) {
		if (Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc) == null)
		{
			saveInventory(player, loc);
		}
		String data = Files.getPlayers().getString("Players." + player.getName().toLowerCase() + "." + loc);
		Inventory copy = ItemSerialization.fromBase64(data);
		return copy.getContents();
	}

	@SuppressWarnings("deprecation")
	public static void resetPlayersInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.updateInventory();
	}

	public static void SetOnlineTime(Player player) {
		long time = Main.Timein.get(player.getName());
		long timeon = (System.currentTimeMillis() / 1000) - time;
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Time", Files.getPlayers().getLong("Players." + player.getName().toLowerCase() + ".Time") + timeon);
	}

	public static String getOnlineTime(String player) {
		Long time = Files.getPlayers().getLong("Players." + player.toLowerCase() + ".Time");
		Long seconds = time;
		long minutes = seconds / 60;
		seconds %= 60;
		long hours = minutes / 60;
		minutes %= 60;
		long days = hours / 24;
		hours %= 24;
		String times = days + "D, " + hours + "H, " + minutes + "M " + seconds + "S";
		return times;

	}

	public static String getTime(Long Time) {
		String times = null;
		Long time = Time;
		Long seconds = time;
		long minutes = seconds / 60;
		seconds %= 60;
		if (seconds == 0)
		{
			if (minutes <= 1)
				times = minutes + " Minute";
			else
				times = minutes + " Minutes";
		} else if (minutes == 0)
		{
			if (seconds <= 1)
				times = seconds + " Second";
			else
				times = seconds + " Seconds";
		} else
		{
			times = minutes + " Minutes " + seconds + " Seconds";
		}
		return times;
	}

	@SuppressWarnings("deprecation")
	public static void newZombieSetUpEveryOne() {
		Random r = new Random();
		if (Main.inGame.size() <= 0)
		{
			Infected.resetPlugin();
		} else
		{
			Main.zombies.clear();
			Main.humans.clear();
			for (Player online : Bukkit.getServer().getOnlinePlayers())
				if (Infected.isPlayerInGame(online) && Main.config.getBoolean("Disguise Support.Enabled"))
					if (Disguises.isPlayerDisguised(online))
						Disguises.unDisguisePlayer(online);
			int alphazombies = 1;
			if(Main.config.getBoolean("Percent to Infected.Enable"))
					alphazombies = Main.inGame.size()/Main.config.getInt("Percent to Infect");
			int temp;
			for(temp = 0; temp != alphazombies && Main.zombies.size() != alphazombies; temp ++){
			int alpha = r.nextInt(Main.inGame.size());
			String name = Main.inGame.get(alpha);
			Player zombie = Bukkit.getServer().getPlayer(name);
			zombie.sendMessage(Methods.sendMessage("Game_YouAreFirstInfected", null, null, null));
			Main.zombies.add(zombie.getName());
			Main.Winners.remove(zombie.getName());
			if (Main.config.getBoolean("New Zombie Tp"))
				Methods.respawn(zombie);

			zombie.playEffect(zombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
			if (Main.config.getBoolean("Zombie Abilities") == true)
			{
				zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
						2000, 2), true);
				zombie.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
						2000, 1), true);
			}
			zombie.setHealth(20);
			Equip.equipZombies(zombie);
			Methods.zombifyPlayer(zombie);
			for (Player online : Bukkit.getServer().getOnlinePlayers())
				if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
						online.sendMessage(Methods.sendMessage("Game_FirstInfected", zombie, null, null));
			
			}
			// Inform humans of infected, prepare them
			for (Player online : Bukkit.getServer().getOnlinePlayers())
			{
				if (Infected.isPlayerInGame(online))
				{
					if (Main.config.getBoolean("ScoreBoard Support"))
						if (!Main.KillStreaks.containsKey(online.getName()))
							Main.KillStreaks.put(online.getName(), Integer.valueOf("0"));
					int timeleft = Main.GtimeLimit;
					online.sendMessage(Main.I + ChatColor.WHITE + "You have " + ChatColor.YELLOW + getTime(Long.valueOf(timeleft)) + ChatColor.WHITE + ". Good luck!");
					if (Main.inGame.contains(online.getName()) && (!(Main.zombies.contains(online.getName()))))
					{
						// if(Main.humans.contains(online)) {
						Main.humans.add(online.getName());
						if (!Main.Winners.contains(online.getName()))
						{
							Main.Winners.add(online.getName());
						}
						online.setHealth(20);
						online.playEffect(online.getLocation(), Effect.SMOKE, 2);

					}
				}
			}
		}
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			ScoreBoard.updateScoreBoard();
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public static void joinInfectHuman(Player player) {
		Player newzombie = player;
		if (!Main.Timein.containsKey(newzombie.getName()))
			Main.Timein.put(newzombie.getName(), System.currentTimeMillis() / 1000);
		Main.humans.remove(newzombie.getName());
		if (!Main.KillStreaks.containsKey(newzombie.getName()))
			Main.KillStreaks.put(newzombie.getName(), Integer.valueOf("0"));
		newzombie.sendMessage(Main.I + "You have became infected!");
		Equip.equipZombies(newzombie);
		newzombie.setHealth(20);
		newzombie.setFoodLevel(20);
		Main.KillStreaks.remove(newzombie.getName());
		for (Player playing : Bukkit.getServer().getOnlinePlayers())
		{
			if ((!(playing == newzombie)) && Main.inGame.contains(playing.getName()))
				playing.sendMessage(Methods.sendMessage("Game_GotInfected", newzombie, null, null));
		}
		newzombie.setFallDistance(0F);
		Methods.respawn(newzombie);
		newzombie.setFallDistance(0F);

		Main.zombies.add(newzombie.getName());
		Main.Winners.remove(newzombie.getName());
		Main.inLobby.remove(newzombie.getName());
		newzombie.playEffect(newzombie.getLocation(), Effect.MOBSPAWNER_FLAMES, 2);
		Methods.zombifyPlayer(newzombie);
		newzombie.setHealth(20);
		Equip.equipZombies(newzombie);
	}

	public static Double KD(Player player) {
		int kills = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
		int deaths = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
		double ratio = Math.round(((double) kills / (double) deaths) * 100.0D) / 100.0D;
		if (deaths == 0)
			ratio = kills;
		else if (kills == 0)
			ratio = 0.00;
		return ratio;
	}

	public static void stats(Player player, Integer Kills, Integer Deaths) {
		int kills = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Kills");
		int deaths = Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".Deaths");
		if (kills == 0)
			kills = 0;
		if (deaths == 0)
			deaths = 0;
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Kills", kills + Kills);
		Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".Deaths", deaths + Deaths);
		Files.savePlayers();
	}

	public static String countdown(HashMap<String, Integer> map) {
		String top = null;
		int maxValueInMap = (Collections.max(map.values())); // This will return
																// max value in
																// the Hashmap
		for (Entry<String, Integer> entry : map.entrySet())
		{ // Itrate through hashmap
			if (entry.getValue() == maxValueInMap)
			{ // Print the key with max value
				top = entry.getKey();
			}
		}

		return top;
	}

	public static String[] getTop5(String stat) {
		String Stat = stat;
		char[] stringArray = Stat.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		Stat = new String(stringArray);
		for (String user : Files.getPlayers().getConfigurationSection("Players").getKeys(true))
		{
			if (!user.contains("."))
			{
				Stats.put(user, Files.getPlayers().getInt("Players." + user + "." + Stat));
			}
		}
		if (Stats.size() < 6)
		{
			Stats.put(" ", 0);
			Stats.put("  ", 0);
			Stats.put("   ", 0);
			Stats.put("    ", 0);
			Stats.put("     ", 0);
		}
		String name1 = Methods.countdown(Stats);
		Stats.remove(name1);
		String name2 = Methods.countdown(Stats);
		Stats.remove(name2);
		String name3 = Methods.countdown(Stats);
		Stats.remove(name3);
		String name4 = Methods.countdown(Stats);
		Stats.remove(name4);
		String name5 = Methods.countdown(Stats);
		Stats.remove(name5);
		String[] top = { name1, name2, name3, name4, name5 };
		Stats.clear();
		return top;
	}

	public static void saveLocation(Location loc, String saveto) {
		int ix = (int) loc.getX();
		int iy = (int) loc.getY();
		int iz = (int) loc.getZ();
		World world = loc.getWorld();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		String s = world.getName() + "," + ix + "," + iy + "," + iz + "," + yaw + "," + pitch;
		Main.config.set(saveto, s);
	}

	public static Location getLocation(String loc) {
		String[] floc = loc.split(",");
		World world = Bukkit.getServer().getWorld(floc[0]);
		Location Loc = new Location(world, Integer.valueOf(floc[1]) + .5,
				Integer.valueOf(floc[2]) + .5, Integer.valueOf(floc[3]) + .5,
				Float.valueOf(floc[4]), Float.valueOf(floc[5]));
		return Loc;
	}

	public static void respawn(Player player) {
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		Random r = new Random();
		int i = r.nextInt(Files.getArenas().getStringList("Arenas." + Main.playingin + ".Spawns").size());
		String loc = Files.getArenas().getStringList("Arenas." + Main.playingin + ".Spawns").get(i);
		String[] floc = loc.split(",");
		World world = Bukkit.getServer().getWorld(floc[0]);
		Location Loc = new Location(world, Integer.valueOf(floc[1]) + .5,
				Integer.valueOf(floc[2]) + .5, Integer.valueOf(floc[3]) + .5,
				Float.valueOf(floc[4]), Float.valueOf(floc[5]));
		player.teleport(Loc);
		Main.Lasthit.remove(player.getName());
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			ScoreBoard.updateScoreBoard();
		}
	}


	public static String sendMessage(String message, Player player, String Time, String List) {
		String msg = String.valueOf(Files.getMessages().getString(message));
		String msg1 = msg;
		if (msg1.contains("<player>") && !(player == null)) /**/
			msg1 = msg1.replaceAll("<player>", player.getName());

		if (msg1.contains("<timeleft>") && !(Time == null)) /**/
			msg1 = msg1.replaceAll("<timeleft>", String.valueOf(Time));

		if (msg1.contains("<list>") && !(List == null)) /**/
			msg1 = msg1.replaceAll("<list>", List);

		if (msg.contains("<humansize>")) /**/
			msg1 = msg1.replaceAll("<humansize>", String.valueOf(Main.humans.size())).replaceAll("<zombiesize>", String.valueOf(Main.zombies.size())).replaceAll("<map>", Main.playingin);

		if (msg.contains("&")) /**/
			msg1 = ChatColor.translateAlternateColorCodes('&', msg1);

		String newMsg = Main.I + msg1;
		return newMsg;
	}
	
	public static String getLocationToString(Location loc) {
		int ix = (int) loc.getX();
		int iy = (int) loc.getY();
		int iz = (int) loc.getZ();
		World world = loc.getWorld();
		String s = world.getName() + "," + ix + "," + iy + "," + iz;
		return s;
	}

	public static Location getLocationFromString(String loc) {
		if (loc.contains(","))
		{
			String[] floc = loc.split(",");
			World world = Bukkit.getServer().getWorld(floc[0]);
			Location Loc = new Location(world, Integer.valueOf(floc[1]),
					Integer.valueOf(floc[2]), Integer.valueOf(floc[3]));
			return Loc;
		} else
			return null;
	}

	public static void handleKillStreaks(boolean killed, Player player) {
		if (killed)
		{
			if (!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), 0);

			if (Main.KillStreaks.get(player.getName()) > Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".KillStreak"))
			{
				Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(player.getName()));
				Files.savePlayers();
			}

			Main.KillStreaks.put(player.getName(), 0);
		} else
		{
			if (!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), 0);
			Main.KillStreaks.put(player.getName(), Main.KillStreaks.get(player.getName()) + 1);

			if (Main.KillStreaks.get(player.getName()) >= 3)
				for (Player playing : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(playing.getName()))
						playing.sendMessage(Main.I + (Infected.isPlayerHuman(player) ? ChatColor.RED + player.getName() : ChatColor.GREEN + player.getName()) + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + Main.KillStreaks.get(player.getName()));

			if ((Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(Main.KillStreaks.get(player.getName())))))
			{
				String command = null;
				command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + Main.KillStreaks.get(player.getName()))).replaceAll("<player>", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			if (Infected.isPlayerHuman(player))
				Methods.rewardPointsAndScore(player, "Kill");

			else
			{
				Methods.rewardPointsAndScore(player, "Kill");

				for (String playing : Infected.listInGame())
				{
					if (Infected.isPlayerHuman(Bukkit.getPlayer(playing)))
						Methods.rewardPointsAndScore(Bukkit.getPlayer(playing), "Survive");

					else if (Infected.isPlayerZombie(Bukkit.getPlayer(playing)))
						Methods.rewardPointsAndScore(Bukkit.getPlayer(playing), "Zombies Infected");
				}

			}

		}
	}


}