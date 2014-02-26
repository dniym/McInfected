
package me.sniperzciinema.infectedaddon.dedicatedserver;

import me.sniperzciinema.infected.Events.InfectedCommandEvent;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Messages.Time;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infectedaddon.ranks.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;


public class Listeners implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission("InfectedAddon.ByPass"))
		{
			event.getPlayer().performCommand("infected join");
			event.setJoinMessage("");
		} 
		if (InfectedServer.update && event.getPlayer().hasPermission("Infected.Admin"))
		{
			event.getPlayer().sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "An update is available: " + InfectedServer.updateName);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("InfectedAddon.ByPass"))
			for (Player player : Bukkit.getOnlinePlayers())
				if (!Lobby.isInGame(player))
					player.sendMessage(ChatColor.YELLOW + event.getPlayer().getName() + " has left the server.");
		event.setQuitMessage("");
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage("");
	}

	@EventHandler
	public void onPlayerTryToLeave(InfectedCommandEvent event) {
		if (event.getArgs().length >= 1 && event.getArgs()[0].equalsIgnoreCase("Leave") && !event.getP().hasPermission("InfectedAddon.ByPass"))
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
		if (Files.getConfig().getBoolean("Dedicated Server.motd.Enabled"))
		{
			String line = Files.getConfig().getString("Dedicated Server.motd.Line");

			line = line.replaceAll("<state>", Lobby.getGameState().toString());

			line = line.replaceAll("<timeif>", Lobby.getGameState() == GameState.InLobby ? Files.getConfig().getString("Dedicated Server.motd.Time if In Lobby") : Files.getConfig().getString("Dedicated Server.motd.Time if Game Started"));

			line = line.replaceAll("<time>", "" + Time.getTime((long) Lobby.getTimeLeft()));

			line = getColors(line);

			event.setMotd(line);
		}

	}

	private String getColors(String line) {

		while (line.contains("&x"))
			line = line.replaceFirst("&x", "&" + RandomChatColor.getColor(ChatColor.RED, ChatColor.GREEN, ChatColor.WHITE, ChatColor.BLUE, ChatColor.YELLOW, ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.GRAY).getChar());
		while (line.contains("&y"))
			line = line.replaceFirst("&y", "&" + RandomChatColor.getFormat().getChar());

		line = ChatColor.translateAlternateColorCodes('&', line);
		return line;
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
		if (Files.getConfig().getStringList("Dedicated Server.Leave On Command").contains(msg.toLowerCase()))
			InfPlayerManager.getInfPlayer(event.getPlayer()).leaveInfected();

		event.setMessage(msg);
	}
}
