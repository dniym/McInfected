package me.xxsniperzzxx_sd.infected;

import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AttackingManager {

	
	public static void HumanAttackZombie(Player human, Player zombie){
		
        //Get a random killtype then set as string and send it out
        String kill = Methods.getKillType("Humans", human.getName(), zombie.getName());
        for (Player playing: Bukkit.getServer().getOnlinePlayers())
        {
            if (Main.inGame.contains(playing.getName()))
                playing.sendMessage(kill);
        }
        Methods.stats(human, 1, 0);
        Main.KillStreaks.put(human.getName(), Main.KillStreaks.get(human.getName()) + 1);
        Files.getPlayers().set("Players." + human.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(human.getName()));
        if (Main.KillStreaks.get(human.getName()) > 2)
            for (Player playing: Bukkit.getServer().getOnlinePlayers())
                if (Main.inGame.contains(playing.getName()))
                    playing.sendMessage(Main.I + ChatColor.GREEN + human.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + Main.KillStreaks.get(human.getName()));
        if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(Main.KillStreaks.get(human.getName())))))
        {
            String command = null;
            command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + Main.KillStreaks.get(human.getName()))).replaceAll("<player>", human.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        Methods.stats(zombie, 0, 1);
        if (Main.KillStreaks.containsKey(zombie.getName()))
        {
            if (Main.KillStreaks.get(zombie.getName()) > Files.getPlayers().getInt("Players." + zombie.getName().toLowerCase() + ".KillStreak"))
            {
                Files.getPlayers().set("Players." + zombie.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(zombie.getName()));
                Files.savePlayers();
            }

            Main.KillStreaks.put(zombie.getName(), 0);
        }
        Methods.rewardPoints(human, "Kill");
        Main.Lasthit.remove(zombie.getName());
        //Reset the zombie
        Methods.equipZombies(zombie);
        zombie.setFoodLevel(20);
        zombie.setHealth(20);
        zombie.setFallDistance(0F);
        Methods.respawn(zombie);
        Methods.zombifyPlayer(zombie);
		
	}
	
	
	
	
	//=====================================================================================================================
	
	private static void prepareNewZombie(Player newzombie){

		Main.humans.remove(newzombie.getName());
        Main.zombies.add(newzombie.getName());
        Main.Winners.remove(newzombie.getName());
        Main.Lasthit.remove(newzombie.getName());
        newzombie.sendMessage(Main.I + "You have become infected!");
        newzombie.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 2));
        Methods.equipZombies(newzombie);
        newzombie.setHealth(20);
        newzombie.setFoodLevel(20);
        Methods.stats(newzombie, 0, 1);
	}
	
	public static void ZombieAttackHuman(Player zombie, Player newzombie){
		
		prepareNewZombie(newzombie);
        Methods.rewardPoints(zombie, "Kill");
        Methods.stats(zombie, 1, 0);
        
         if (Main.KillStreaks.get(newzombie.getName()) > Files.getPlayers().getInt("Players." + newzombie.getName().toLowerCase() + ".KillStreak"))
         {
             Files.getPlayers().set("Players." + newzombie.getName().toLowerCase() + ".KillStreak", Main.KillStreaks.get(newzombie.getName()));
             Files.savePlayers();
         }

         if (!Main.KillStreaks.containsKey(zombie.getName()))
             Main.KillStreaks.put(zombie.getName(), Integer.valueOf("0"));
         
         Main.KillStreaks.put(newzombie.getName(), 0);
         Main.KillStreaks.put(zombie.getName(), Main.KillStreaks.get(zombie.getName()) + 1);

         if (Main.KillStreaks.get(zombie.getName()) > 2)
             for (Player playing: Bukkit.getServer().getOnlinePlayers())
                 if (Main.inGame.contains(playing.getName()))
                     playing.sendMessage(Main.I + ChatColor.RED + zombie.getName() + ChatColor.GOLD + " has a killstreak of " + ChatColor.YELLOW + Main.KillStreaks.get(zombie.getName()));
       
         if (!(Infected.filesGetKillTypes().contains("KillSteaks." + String.valueOf(Main.KillStreaks.get(zombie.getName())))))
         {
             String command = null;
             command = String.valueOf(Infected.filesGetKillTypes().getInt("KillSteaks." + Main.KillStreaks.get(zombie.getName()))).replaceAll("<player>", zombie.getName());
             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
         }
         String kill = Methods.getKillType("Zombies", newzombie.getName(), zombie.getName());
         for (Player playing: Bukkit.getServer().getOnlinePlayers())
         {
             if (Main.inGame.contains(playing.getName()))
             {
                 playing.sendMessage(kill);
             }
             if (Main.humans.contains(playing.getName())) Methods.rewardPoints(playing, "Survive");

         }
         if (Main.humans.size() == 0)
         {
             Methods.endGame(false);
         }
         else
         {
             for (Player zombies: Bukkit.getOnlinePlayers())
             {
                 if (Infected.isPlayerZombie(zombies) && !(zombies == newzombie)) Methods.rewardPoints(zombies, "Zombies Infected");
             }
             //If New Zombies Tp is true, tp the zombie on infection
             if (Main.config.getBoolean("New Zombies Tp") == true)
             {

                 Methods.respawn(newzombie);
                 newzombie.setFallDistance(0F);
             }
             Methods.zombifyPlayer(newzombie);
         }
     }
		
}
