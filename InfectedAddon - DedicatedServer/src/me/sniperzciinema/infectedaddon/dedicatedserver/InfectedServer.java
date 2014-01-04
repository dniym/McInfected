
package me.sniperzciinema.infectedaddon.dedicatedserver;

import java.util.ArrayList;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class InfectedServer extends JavaPlugin {

	public static boolean update;
	public static String updateName;
	private int neededInfectedVersion = 207;
	private String neededInfectedName = "Infected v2.0.7";

	public void onEnable() {
		if (Integer.valueOf(Infected.me.getDescription().getVersion().replaceAll("\\.", "")) < neededInfectedVersion)
		{
			this.getLogger().severe("Invalid Infected Version, Please Update to " + neededInfectedName);
			getServer().getPluginManager().disablePlugin(this);

		} else
		{
			getServer().getPluginManager().registerEvents(new Listeners(), this);

			List<String> list = Settings.AllowedCommands();
			list = fillList(list);

			Files.getConfig().set("Settings.Misc.Allowed Commands", list);

			Files.getConfig().addDefault("Dedicated Server.Chat", true);
			Files.getConfig().addDefault("Dedicated Server.motd.Enabled", true);
			Files.getConfig().addDefault("Dedicated Server.motd.Line", "&x[====== &a&lGame State:&b&o <state> &x======]\n&x[====== <timeif> &x======]");

			Files.getConfig().addDefault("Dedicated Server.motd.Time if In Lobby", "&f&oHurry and Join!");
			Files.getConfig().addDefault("Dedicated Server.motd.Time if Game Started", "&e&lTime Left: &f&o<time>");

			List<String> leaveOnCmd = new ArrayList<String>();
			leaveOnCmd.add("/lobby");
			leaveOnCmd.add("/hub");
			Files.getConfig().addDefault("Dedicated Server.Leave On Command.Enabled", false);
			Files.getConfig().addDefault("Dedicated Server.Leave On Command.Commands", leaveOnCmd);

			Files.saveConfig();

			if (Settings.checkForUpdates())
			{

				Updater updater = new Updater(this, 70529, this.getFile(),
						Updater.UpdateType.NO_DOWNLOAD, true);

				update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
				updateName = updater.getLatestName();

				if (update)
				{
					System.out.println("You need to update InfectedAddon-Dedicated Server to: " + updater.getLatestFileVersion());
					for (Player player : Bukkit.getOnlinePlayers())
						player.sendMessage(RandomChatColor.getColor() + "Update for Infected Availble: " + updateName);
				}
			}
		}

	}

	private List<String> fillList(List<String> list) {
		if (!list.contains("/join"))
			list.add("/join");
		if (!list.contains("/leave"))
			list.add("/leave");
		if (!list.contains("/vote"))
			list.add("/vote");
		if (!list.contains("/chat"))
			list.add("/chat");
		if (!list.contains("/classes"))
			list.add("/classes");
		if (!list.contains("/join"))
			list.add("/join");
		if (!list.contains("/shop"))
			list.add("/shop");
		if (!list.contains("/store"))
			list.add("/store");
		if (!list.contains("/grenade"))
			list.add("/grenade");
		if (!list.contains("/grenades"))
			list.add("/grenades");
		if (!list.contains("/files"))
			list.add("/files");
		if (!list.contains("/admin"))
			list.add("/admin");
		if (!list.contains("/list"))
			list.add("/list");
		if (!list.contains("/stats"))
			list.add("/stats");
		if (!list.contains("/top"))
			list.add("/top");
		if (!list.contains("/arenas"))
			list.add("/arenas");
		if (!list.contains("/help"))
			list.add("/help");
		if (!list.contains("/setlobby"))
			list.add("/setlobby");
		if (!list.contains("/tplobby"))
			list.add("/tplobby");
		if (!list.contains("/create"))
			list.add("/create");
		if (!list.contains("/remove"))
			list.add("/remove");
		if (!list.contains("/addspawn"))
			list.add("/addspawn");
		if (!list.contains("/delspawn"))
			list.add("/delspawn");
		if (!list.contains("/spawns"))
			list.add("/spawns");
		if (!list.contains("/tpspawn"))
			list.add("/tpspawn");

		return list;
	}

	public void onDisable() {
	}

}
