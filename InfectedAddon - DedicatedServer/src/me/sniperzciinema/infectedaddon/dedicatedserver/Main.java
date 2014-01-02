
package me.sniperzciinema.infectedaddon.dedicatedserver;

import java.util.List;

import me.sniperzciinema.infected.Events.InfectedCommandEvent;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Messages.Time;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infectedaddon.ranks.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		List<String> list = Settings.AllowedCommands();
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

		Files.getConfig().set("Settings.Misc.Allowed Commands", list);

		Files.getConfig().addDefault("Dedicated Server.Chat", true);
		Files.getConfig().addDefault("Dedicated Server.MOTD", true);
		Files.saveConfig();
	}

	public void onDisable() {
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission("InfectedAddon.ByPass"))
		{
			event.getPlayer().performCommand("infected join");
			event.setJoinMessage("");
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage("");
	}

	@EventHandler
	public void onPlayerTryToLeave(InfectedCommandEvent event) {
		if (event.getArgs()[0].equalsIgnoreCase("Leave") && !event.getP().hasPermission("InfectedAddon.ByPass"))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerTalk(AsyncPlayerChatEvent event) {
		if (Files.getConfig().getBoolean("Dedicated Server.Chat"))
		{
			if (Bukkit.getPluginManager().getPlugin("InfectedAddon-Ranks") != null)
				event.setFormat("§7§l«§4[" + RankManager.getPlayersRank(event.getPlayer()).getPrefix() + "§4]§f§l %s§8(" + InfPlayerManager.getInfPlayer(event.getPlayer()).getScore() + ")§7§l»§r %s");
			else
				event.setFormat("§7§l«§f§l%s§8(" + InfPlayerManager.getInfPlayer(event.getPlayer()).getScore() + ")§7§l»§r %s");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onServerPing(ServerListPingEvent event) {
		if (Files.getConfig().getBoolean("Dedicated Server.MOTD"))
		{
			event.setMotd(RandomChatColor.getColor() + "[====== §a§lGame State:§b§o " + Lobby.getGameState().toString() + RandomChatColor.getColor() + " ======]" + "\n" + RandomChatColor.getColor() + "[====== " + (Lobby.getGameState() != GameState.InLobby ? "§e§lTime Left: §f§o" + Time.getTime((long) Lobby.getTimeLeft()) : "§f§lHurry and Join!") + RandomChatColor.getColor() + " ======]");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage();
		if (msg.contains(" "))
		{
			if (msg.split(" ")[0].equalsIgnoreCase("/Files"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Files");
			if (msg.split(" ")[0].equalsIgnoreCase("/Chat"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Chat");
			if (msg.split(" ")[0].equalsIgnoreCase("/Grenade"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Grenade");
			if (msg.split(" ")[0].equalsIgnoreCase("/Grenades"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Grenades");
			if (msg.split(" ")[0].equalsIgnoreCase("/Store"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Store");
			if (msg.split(" ")[0].equalsIgnoreCase("/Stores"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Stores");
			if (msg.split(" ")[0].equalsIgnoreCase("/List"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected List");
			if (msg.split(" ")[0].equalsIgnoreCase("/Help"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Help");
			if (msg.split(" ")[0].equalsIgnoreCase("/Admin"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Admin");
			if (msg.split(" ")[0].equalsIgnoreCase("/Stats"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Stats");
			if (msg.split(" ")[0].equalsIgnoreCase("/TpSpawn"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected TpSpawn");
			if (msg.split(" ")[0].equalsIgnoreCase("/DelSpawn"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected DelSpawn");
			if (msg.split(" ")[0].equalsIgnoreCase("/Create"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Create");
			if (msg.split(" ")[0].equalsIgnoreCase("/Remove"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Remove");
			if (msg.split(" ")[0].equalsIgnoreCase("/Top"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected Top");
			if (msg.split(" ")[0].equalsIgnoreCase("/SetArena"))
				msg = msg.replaceAll(msg.split(" ")[0], "/Infected SetArena");
		} else
		{
			if (msg.equalsIgnoreCase("/Join"))
				msg = "/Infected Join";
			if (msg.equalsIgnoreCase("/Rank"))
				msg = "/Infected Rank";
			if (msg.equalsIgnoreCase("/Ranks"))
				msg = "/Infected Ranks";
			if (msg.equalsIgnoreCase("/Leave"))
				msg = "/Infected Leave";
			if (msg.equalsIgnoreCase("/Vote"))
				msg = "/Infected Vote";
			if (msg.equalsIgnoreCase("/Chat"))
				msg = "/Infected Chat";
			if (msg.equalsIgnoreCase("/Files"))
				msg = "/Infected Files";
			if (msg.equalsIgnoreCase("/Grenades"))
				msg = "/Infected Grenades";
			if (msg.equalsIgnoreCase("/Grenade"))
				msg = "/Infected Grenade";
			if (msg.equalsIgnoreCase("/Shop"))
				msg = "/Infected Shop";
			if (msg.equalsIgnoreCase("/Store"))
				msg = "/Infected Store";
			if (msg.equalsIgnoreCase("/Classes"))
				msg = "/Infected Classes";
			if (msg.equalsIgnoreCase("/Info"))
				msg = "/Infected Info";
			if (msg.equalsIgnoreCase("/Suicide"))
				msg = "/Infected Suicide";
			if (msg.equalsIgnoreCase("/SetLobby"))
				msg = "/Infected SetLobby";
			if (msg.equalsIgnoreCase("/List"))
				msg = "/Infected List";
			if (msg.equalsIgnoreCase("/Help"))
				msg = "/Infected Help";
			if (msg.equalsIgnoreCase("/Start"))
				msg = "/Infected Start";
			if (msg.equalsIgnoreCase("/End"))
				msg = "/Infected End";
			if (msg.equalsIgnoreCase("/Arenas"))
				msg = "/Infected Arenas";
			if (msg.equalsIgnoreCase("/Admin"))
				msg = "/Infected Admin";
			if (msg.equalsIgnoreCase("/Stats"))
				msg = "/Infected Stats";
			if (msg.equalsIgnoreCase("/TpLobby"))
				msg = "/Infected TpLobby";
			if (msg.equalsIgnoreCase("/TpLobby"))
				msg = "/Infected SetSpawn";
			if (msg.equalsIgnoreCase("/TpLobby"))
				msg = "/Infected DelSpawn";
			if (msg.equalsIgnoreCase("/Spawns"))
				msg = "/Infected Spawns";
			if (msg.equalsIgnoreCase("/Create"))
				msg = "/Infected Create";
			if (msg.equalsIgnoreCase("/Remove"))
				msg = "/Infected Remove";
			if (msg.equalsIgnoreCase("/Top"))
				msg = "/Infected Top";
			if (msg.equalsIgnoreCase("/SetArena"))
				msg = "/Infected SetArena";
		}
		event.setMessage(msg);
	}
}
