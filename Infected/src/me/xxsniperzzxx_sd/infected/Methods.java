package me.xxsniperzzxx_sd.infected;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import me.xxsniperzzxx_sd.infected.Disguise.DisguisePlayer;
import me.xxsniperzzxx_sd.infected.Events.InfectedPlayerDieEvent;
import me.xxsniperzzxx_sd.infected.Main.GameState;
import me.xxsniperzzxx_sd.infected.Tools.Files;
import me.xxsniperzzxx_sd.infected.Tools.ItemHandler;
import me.xxsniperzzxx_sd.infected.Tools.ItemSerialization;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.kitteh.tag.TagAPI;

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
		if(Main.config.getBoolean("Disguise Support.Enabled"))
			DisguisePlayer.disguisePlayer(player);

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

	public static void playerDies(Player Killer, Player Killed) {
		
		Bukkit.getServer().getPluginManager().callEvent(new InfectedPlayerDieEvent(Killer, Killed, Infected.playerGetGroup(Killed), Infected.isPlayerHuman(Killed) ? true : false));
		
		Methods.stats(Killer, 1, 0);
		Methods.stats(Killed, 0, 1);
		
		Methods.handleKillStreaks(true, Killed);
		Methods.handleKillStreaks(false, Killer);
		
		String kill = getKillType(Infected.playerGetGroup(Killer) + "s", Killer.getName(), Killed.getName());
		for (Player playing : Bukkit.getServer().getOnlinePlayers())
			if (Main.inGame.contains(playing.getName()))
				playing.sendMessage(kill);
		
		if(Infected.isPlayerHuman(Killed)){
			if(Main.config.getBoolean("New Zombies Tp")){
				Methods.zombifyPlayer(Killed);
				Methods.respawn(Killed);
					
			}else
				Methods.zombifyPlayer(Killed);	

			
			Killed.sendMessage(Main.I + "You have become infected!");
			Killed.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,20, 2));
			
			Methods.equipZombies(Killed);
			Killed.setHealth(20);
			Killed.setFoodLevel(20);
		}
		else{
			Methods.respawn(Killed);
			Methods.equipZombies(Killed);
		}
			
		Killed.setFallDistance(0F);

		if (Infected.isPlayerHuman(Killed))
			Infected.delPlayerHuman(Killed);
		
		if (!Infected.isPlayerZombie(Killed))
			Infected.addPlayerZombie(Killed);
		
		if(Main.Lasthit.containsKey(Killed.getName()))
			Main.Lasthit.remove(Killed.getName());

		if (Main.Winners.contains(Killed.getName()))
			Main.Winners.remove(Killed.getName());
		
		
		if (Main.humans.size() == 0 && Infected.getGameState() == GameState.STARTED)
			Game.endGame(false);
		
		else
		{
			Methods.equipZombies(Killed);
			Methods.zombifyPlayer(Killed);
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
			for (Player online : Bukkit.getServer().getOnlinePlayers())
				if (Infected.isPlayerInGame(online) && Main.config.getBoolean("Disguise Support.Enabled"))
					if (DisguisePlayer.isPlayerDisguised(online))
						DisguisePlayer.unDisguisePlayer(online);
			int alpha = r.nextInt(Main.inGame.size());
			String name = Main.inGame.get(alpha);
			Player zombie = Bukkit.getServer().getPlayer(name);
			zombie.sendMessage(Methods.sendMessage("Game_YouAreFirstInfected", null, null, null));
			Main.zombies.clear();
			Main.humans.clear();
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
			Methods.zombifyPlayer(zombie);
			zombie.setHealth(20);
			Methods.equipZombies(zombie);
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
						online.sendMessage(Methods.sendMessage("Game_FirstInfected", zombie, null, null));
						online.setHealth(20);
						online.playEffect(online.getLocation(), Effect.SMOKE, 2);

					}
				}
			}
		}
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			updateScoreBoard();
		}
	}

	public static void updateScoreBoard() {
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			if (Infected.getGameState() == GameState.STARTED)
			{

				Main.possibleArenas.clear();
				for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
				{
					// Check if the string matchs an arena

					if (Main.possibleArenas.contains(parenas))
					{
						Main.possibleArenas.remove(parenas);
					}
					if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
					{
						Main.possibleArenas.remove(parenas);
					} else if (!parenas.contains("."))
					{
						Score score = Main.voteList.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + ""+ ChatColor.ITALIC + parenas));
						if(Main.Votes.get(parenas) != null) score.setScore(Main.Votes.get(parenas));
						else{
							score.setScore(1);
							score.setScore(0);
						}	
					}
				}

				Score score = Main.playingList.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN +""+ ChatColor.ITALIC + "Humans:"));
				score.setScore(Main.humans.size());
				Score score2 = Main.playingList.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + ""+ ChatColor.ITALIC +"Zombies:"));
				score2.setScore(Main.zombies.size());

			} else
			{
				Main.possibleArenas.clear();
				for (String parenas : Infected.filesGetArenas().getConfigurationSection("Arenas").getKeys(true))
				{
					// Check if the string matchs an arena

					if (Main.possibleArenas.contains(parenas))
					{
						Main.possibleArenas.remove(parenas);
					}
					if (!Infected.filesGetArenas().contains("Arenas." + parenas + ".Spawns"))
					{
						Main.possibleArenas.remove(parenas);
					} else if (!parenas.contains("."))
					{
						Score score = Main.voteList.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW +""+ ChatColor.ITALIC + parenas));
						if(Main.Votes.get(parenas) != null) score.setScore(Main.Votes.get(parenas));
						else{
							score.setScore(1);
							score.setScore(0);
							}
						
					}
				}

				// Reset Team board
				Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + ""+ ChatColor.ITALIC +"Humans:"));
				Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + ""+ ChatColor.ITALIC +"Zombies:"));

			}
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
		Methods.equipZombies(newzombie);
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
		Methods.equipZombies(newzombie);
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
			updateScoreBoard();
		}
	}

	@SuppressWarnings("deprecation")
	public static void equipHumans(Player human) {
		if (Main.config.getBoolean("Default Classes.Use"))
			Main.humanClasses.put(human.getName(), Main.config.getString("Default Classes.Human"));

		if (Main.config.getBoolean("TagAPI Support.Enable"))
			TagAPI.refreshPlayer(human);
		if (Main.humanClasses.containsKey(human.getName()))
		{
			for (String s : Infected.filesGetClasses().getStringList("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Items"))
			{
				human.getInventory().addItem(ItemHandler.getItemStack(s));
				human.updateInventory();
			}
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Head") != null)
				human.getInventory().setHelmet(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Head")));
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Chest") != null)
				human.getInventory().setChestplate(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Chest")));
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Legs") != null)
				human.getInventory().setLeggings(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Legs")));
			if (Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Feet") != null)
				human.getInventory().setBoots(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Human." + Main.humanClasses.get(human.getName()) + ".Feet")));
		} else
		{
			for (String s : Main.config.getStringList("Armor.Human.Items"))
			{
				human.getInventory().addItem(ItemHandler.getItemStack(s));
				human.updateInventory();
			}
			if (Main.config.getString("Armor.Human.Head") != null)
				human.getInventory().setHelmet(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Head")));
			if (Main.config.getString("Armor.Human.Chest") != null)
				human.getInventory().setChestplate(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Chest")));
			if (Main.config.getString("Armor.Human.Legs") != null)
				human.getInventory().setLeggings(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Legs")));
			if (Main.config.getString("Armor.Human.Feet") != null)
				human.getInventory().setBoots(ItemHandler.getItemStack(Main.config.getString("Armor.Human.Feet")));
		}
		human.updateInventory();
		applyClassAbility(human);
	}

	@SuppressWarnings("deprecation")
	public static void equipZombies(Player zombie) {

		if (Main.config.getBoolean("Default Classes.Use"))
			Main.zombieClasses.put(zombie.getName(), Main.config.getString("Default Classes.Zombie"));

		updateScoreBoard();
		if (Main.config.getBoolean("TagAPI Support.Enable"))
			TagAPI.refreshPlayer(zombie);
		// Give infected their armor

		// Take away humans items
		for (String s : Main.config.getStringList("Armor.Human.Items"))
		{
			if (zombie.getInventory().contains(ItemHandler.getItem(s).getType()))
			{
				zombie.getInventory().remove(ItemHandler.getItem(s).getType());
			}
		}
		// Take away any items from their human class
		if (Main.humanClasses.containsKey(zombie.getName()))
		{
			for (String s : Infected.filesGetClasses().getStringList("Classes.Human." + Main.humanClasses.get(zombie.getName()) + ".Items"))
			{
				if (zombie.getInventory().contains(ItemHandler.getItem(s).getType()))
				{
					zombie.getInventory().remove(ItemHandler.getItem(s).getType());
				}
			}
		}
		for (ItemStack armor : zombie.getInventory().getArmorContents())
		{
			if (!(armor == null || armor.getType() == Material.AIR) && (armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Head")) || armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Chest")) || armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Legs")) || armor == ItemHandler.getItem(Main.config.getString("Armor.Zombie.Feet"))))
			{
				zombie.getInventory().addItem(armor);
			}
		}

		// Add armor from the zombie class
		if (Main.zombieClasses.containsKey(zombie.getName()))
		{
			for (String s : Infected.filesGetClasses().getStringList("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Items"))
			{
				if (!zombie.getInventory().contains(ItemHandler.getItem(s)))
					zombie.getInventory().addItem(ItemHandler.getItemStack(s));
				zombie.updateInventory();
			}
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Head") != null)
				zombie.getInventory().setHelmet(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Head")));
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Chest") != null)
				zombie.getInventory().setChestplate(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Chest")));
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Legs") != null)
				zombie.getInventory().setLeggings(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Legs")));
			if (Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Feet") != null)
				zombie.getInventory().setBoots(ItemHandler.getItemStack(Infected.filesGetClasses().getString("Classes.Zombie." + Main.zombieClasses.get(zombie.getName()) + ".Feet")));
		} else
		{
			for (String s : Main.config.getStringList("Armor.Zombie.Items"))
			{
				if (!zombie.getInventory().contains(ItemHandler.getItem(s)))
					zombie.getInventory().addItem(ItemHandler.getItemStack(s));
			}
			if (Main.config.getString("Armor.Zombie.Head") != null)
				zombie.getInventory().setHelmet(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Head")));
			if (Main.config.getString("Armor.Zombie.Chest") != null)
				zombie.getInventory().setChestplate(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Chest")));
			if (Main.config.getString("Armor.Zombie.Legs") != null)
				zombie.getInventory().setLeggings(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Legs")));
			if (Main.config.getString("Armor.Zombie.Feet") != null)
				zombie.getInventory().setBoots(ItemHandler.getItemStack(Main.config.getString("Armor.Zombie.Feet")));
		}
		zombie.updateInventory();
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

	public static String getKillType(String group, String human, String zombie) {
		Random r = new Random();
		int i = r.nextInt(Files.getKills().getStringList(group).size());
		String killtype = ChatColor.GRAY + Files.getKills().getStringList(group).get(i);
		String msg = null;
		msg = killtype.replaceAll("<zombie>", ChatColor.RED + zombie).replaceAll("<human>", ChatColor.GREEN + human);
		String cmsg = ChatColor.translateAlternateColorCodes('&', msg);
		return cmsg;
	}


	 public static String getLocationToString(Location loc)
	 {
		 int ix = (int) loc.getX();
		 int iy = (int) loc.getY();
		 int iz = (int) loc.getZ();
		 World world = loc.getWorld();
		 String s = world.getName() + "," + ix + "," + iy + "," + iz;
		 return s;
	 }
	 
	 public static Location getLocationFromString(String loc)
	 {
		 if(loc.contains(",")){
			 String[] floc = loc.split(",");
			 World world = Bukkit.getServer().getWorld(floc[0]);
			 Location Loc = new Location(world, Integer.valueOf(floc[1]), Integer.valueOf(floc[2]), Integer.valueOf(floc[3]));
			 return Loc;
		 }
		 else
			 return null;
	 }
	
	
	
	public static void handleKillStreaks(boolean killed, Player player){
		if(killed){
			if(!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), 0);
			
			if(Main.KillStreaks.get(player.getName()) > Files.getPlayers().getInt("Players." + player.getName().toLowerCase() + ".KillStreak")){
				Files.getPlayers().set("Players." + player.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(player.getName()));
				Files.savePlayers();
			}
			
			Main.KillStreaks.put(player.getName(), 0);
		}else{
			if(!Main.KillStreaks.containsKey(player.getName()))
				Main.KillStreaks.put(player.getName(), 0);
			Main.KillStreaks.put(player.getName(), Main.KillStreaks.get(player.getName())+1);

			if (Main.KillStreaks.get(player.getName()) >= 3)
				for (Player playing : Bukkit.getServer().getOnlinePlayers())
					if (Main.inGame.contains(playing.getName()))
						playing.sendMessage(Main.I + (Infected.isPlayerHuman(player) ?  ChatColor.RED + player.getName() : ChatColor.GREEN + player.getName()) + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + Main.KillStreaks.get(player.getName()));


			if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(Main.KillStreaks.get(player.getName())))))
			{
				String command = null;
				command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + Main.KillStreaks.get(player.getName()))).replaceAll("<player>", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
			if(Infected.isPlayerHuman(player))
				Methods.rewardPointsAndScore(player, "Kill");

			else{
				Methods.rewardPointsAndScore(player, "Kill");

				for(String playing : Infected.listInGame()){
					if(Infected.isPlayerHuman(Bukkit.getPlayer(playing)))
						Methods.rewardPointsAndScore(Bukkit.getPlayer(playing), "Survive");
					
					else if(Infected.isPlayerZombie(Bukkit.getPlayer(playing)))
						Methods.rewardPointsAndScore(Bukkit.getPlayer(playing), "Zombies Infected");
				}
				
			}

		}
	}

	@SuppressWarnings("deprecation")
	public static void tp2LobbyAfter(Player player) {
		
		Infected.arenaReset();
		
		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			player.setScoreboard(Main.voteBoard);
			Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Humans:"));
			Main.playingBoard.resetScores(Bukkit.getOfflinePlayer(ChatColor.DARK_RED + "Zombies:"));
		}
		player.teleport(Methods.getLocation(Main.config.getString("Lobby")));
		resetPlayersInventory(player);
		if (Infected.filesGetShop().getBoolean("Save Items"))
			player.getInventory().setContents(Infected.playerGetShopInventory(player));
		Main.Lasthit.remove(player.getName());
		Main.humanClasses.remove(player.getName());
		Main.zombieClasses.remove(player.getName());
		player.setGameMode(GameMode.ADVENTURE);
		player.updateInventory();
		player.setLevel(0);
		Main.Votes.clear();
		Main.inLobby.add(player.getName());
		Main.zombies.clear();
		Main.humans.clear();
		player.setFireTicks(0);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		Main.KillStreaks.remove(player.getName());
		Infected.setGameState(GameState.INLOBBY);
		Main.Voted4.clear();
		Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
		Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
		if (Main.config.getBoolean("Disguise Support.Enabled"))
			if (DisguisePlayer.isPlayerDisguised(player))
				DisguisePlayer.unDisguisePlayer(player);

		if (Main.inGame.size() == 0)
			Main.Winners.clear();

	}

	public static void resetInf() {
		Infected.setGameState(GameState.INLOBBY);
		Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
		Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
		Main.Winners.clear();
		Main.humans.clear();
		Main.zombies.clear();
		Main.KillStreaks.clear();
		Infected.arenaReset();
	}

	@SuppressWarnings("deprecation")
	public static void resetp(Player player) {

		if (Main.config.getBoolean("ScoreBoard Support"))
		{
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
		player.setHealth(20.0);
		player.setFoodLevel(20);
		for (PotionEffect reffect : player.getActivePotionEffects())
		{
			player.removePotionEffect(reffect.getType());
		}
		resetPlayersInventory(player);
		player.updateInventory();
		if (GameMode.valueOf(Main.gamemode.get(player.getName())) == null)
			player.setGameMode(GameMode.SURVIVAL);
		else
			player.setGameMode(GameMode.valueOf(Main.gamemode.get(player.getName())));
		Main.Lasthit.remove(player.getName());
		if (Main.Inventory.containsKey(player.getName()))
			player.getInventory().setContents(Main.Inventory.get(player.getName()));
		if (Main.Armor.containsKey(player.getName()))
			player.getInventory().setArmorContents(Main.Armor.get(player.getName()));
		player.updateInventory();
		player.setExp(Main.Exp.get(player.getName()));
		player.setLevel(Main.Levels.get(player.getName()));
		if (Main.Spot.containsKey(player.getName()))
			player.teleport(Main.Spot.get(player.getName()));
		if (Main.Food.containsKey(player.getName()))
			player.setFoodLevel(Main.Food.get(player.getName()));
		if (Main.Health.containsKey(player.getName()))
			player.setHealth(Main.Health.get(player.getName()));
		Main.humanClasses.remove(player.getName());
		Main.zombieClasses.remove(player.getName());
		Main.inLobby.remove(player.getName());
		Main.zombies.remove(player.getName());
		Main.humans.remove(player.getName());
		Main.inGame.remove(player.getName());
		Main.Creating.remove(player.getName());
		Main.Health.remove(player.getName());
		Main.Food.remove(player.getName());
		Main.Armor.remove(player.getName());
		Main.Inventory.remove(player.getName());
		Main.Spot.remove(player.getName());
		Main.Winners.remove(player.getName());
		if (Infected.isPlayerInGame(player) && Main.config.getBoolean("Disguise Support.Enabled"))
			if (DisguisePlayer.isPlayerDisguised(player))
				DisguisePlayer.unDisguisePlayer(player);

		if (Main.Voted4.containsKey(player.getName()))
		{
			if (Main.Votes.containsKey(Main.Voted4.get(player.getName())))
				Main.Votes.put(Main.Voted4.get(player.getName()).toString(), Main.Votes.get(Main.Voted4.get(player.getName())) - 1);
			Main.Voted4.remove(player.getName());

		}
	}

	// Reset the game(Method)
	public static void reset() {
		for (Player players : Bukkit.getOnlinePlayers())
		{
			if (Infected.isPlayerInGame(players))
			{
				Infected.resetPlayer(players);
				if (Main.config.getBoolean("ScoreBoard Support"))
				{
					updateScoreBoard();
				}
			}
		}
		Infected.arenaReset();
		
		Main.KillStreaks.clear();
		Main.possibleArenas.clear();
		Main.inLobby.clear();
		Main.zombieClasses.clear();
		Main.humanClasses.clear();
		Main.Winners.clear();
		Main.zombies.clear();
		Main.humans.clear();
		Main.inGame.clear();
		Main.Voted4.clear();
		Main.Votes.clear();
		Main.Health.clear();
		Main.Food.clear();
		Main.Inventory.clear();
		Main.Spot.clear();
		Main.Armor.clear();
		Infected.setGameState(GameState.INLOBBY);
		Bukkit.getServer().getScheduler().cancelTask(Main.timestart);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeLimit);
		Bukkit.getServer().getScheduler().cancelTask(Main.timeVote);
		Bukkit.getServer().getScheduler().cancelTask(Main.queuedtpback);
		resetInf();
	}

}