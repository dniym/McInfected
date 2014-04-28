
package me.sniperzciinema.infected.Command.SubCommands;

import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Extras.Menus;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.GameMechanics.Stats;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Lobby.GameState;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.UUID.UUIDManager;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.AddonManager;
import me.sniperzciinema.infected.Tools.Files;
import me.sniperzciinema.infected.Tools.MySQL.MySQL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class AdminCommand extends SubCommand {

	public AdminCommand()
	{
		super("admin");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (!sender.hasPermission("Infected.Admin"))
			sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());
		else
			if (args.length == 2)
			{
				// SHUTDOWN
				if (args[1].equalsIgnoreCase("Shutdown"))
				{
					if (Lobby.getGameState() != GameState.Disabled)
					{
						Lobby.setGameState(GameState.Disabled);
						sender.sendMessage(Msgs.Command_Admin_Shutdown.getString("<state>", "Disabled"));
					}
					else
					{
						Lobby.setGameState(GameState.InLobby);
						sender.sendMessage(Msgs.Command_Admin_Shutdown.getString("<state>", "Enabled"));
					}
				}
				// RELOAD
				else
					if (args[1].equalsIgnoreCase("Reload"))
					{
						System.out.println(Msgs.Format_Header.getString("<title>", "Infected"));

						Lobby.loadAllArenas();
						Files.reloadAll();
						AddonManager.getAddons();

						InfClassManager.loadConfigClasses();
						InfClassManager.loadDefaultClasses();
						GrenadeManager.loadConfigGrenades();

						Infected.Menus.destroyAllMenus();
						Infected.Menus = new Menus();

						if (Settings.MySQLEnabled())
						{
							System.out.println("Attempting to connect to MySQL");
							Infected.MySQL = new MySQL(Infected.me,
									Files.getConfig().getString("MySQL.Host"),
									Files.getConfig().getString("MySQL.Port"),
									Files.getConfig().getString("MySQL.Database"),
									Files.getConfig().getString("MySQL.Username"),
									Files.getConfig().getString("MySQL.Password"));

							try
							{
								Infected.connection = Infected.MySQL.openConnection();
								Statement statement = Infected.connection.createStatement();

								statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "Infected" + " (Player VARCHAR(20), Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10));");
								System.out.println("MySQL Table has been loaded");
							}
							catch (Exception e)
							{
								Files.getConfig().set("MySQL.Enabled", false);
								Files.saveConfig();
								System.out.println("Unable to connect to MySQL");
							}
						}

						System.out.println(Msgs.Format_Line.getString());
						sender.sendMessage(Msgs.Command_Admin_Reload.getString());

					}
					// CODE
					else
						if (args[1].equalsIgnoreCase("Code"))
						{
							if (sender instanceof Player)
							{
								Player p = (Player) sender;
								p.sendMessage(Msgs.Format_Prefix.getString() + "Code: " + ChatColor.WHITE + ItemHandler.getItemStackToString(p.getItemInHand()));
								p.sendMessage(Msgs.Format_Prefix.getString() + "This code has also been sent to your console to allow for copy and paste!");
								System.out.println(ItemHandler.getItemStackToString(p.getItemInHand()));
							}
							else
								sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());
						}
						else
							sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());
			}
			else
				if (args.length == 3)
				{
					// KICK
					if (args[1].equalsIgnoreCase("Kick"))
					{
						@SuppressWarnings("deprecation")
						Player u = Bukkit.getPlayer(args[2]);
						if ((u == null) || !Lobby.isInGame(u))
							sender.sendMessage(Msgs.Error_Game_They_Are_Not_In.getString());
						else
						{
							u.performCommand("Infected Leave");
							u.sendMessage(Msgs.Command_Admin_Kicked_You.getString());
							sender.sendMessage(Msgs.Command_Admin_Kicked_Them.getString("<player>", u.getName()));
						}
					}
					else
						sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());

				}
				else
					if (args.length == 4)
					{
						String user = args[2];

						int i = Integer.parseInt(args[3]);
						UUID id = UUIDManager.getPlayerUUID(user);
						if (args[1].equalsIgnoreCase("Points"))
						{
							int newValue = Stats.getPoints(id, Settings.VaultEnabled()) + i;
							Stats.setPoints(id, newValue, Settings.VaultEnabled());
							sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "points", "<value>", String.valueOf(newValue)));
						}
						else
							if (args[1].equalsIgnoreCase("Score"))
							{
								int newValue = Stats.getScore(id) + i;
								Stats.setScore(id, newValue);
								sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "score", "<value>", String.valueOf(newValue)));
							}
							else
								if (args[1].equalsIgnoreCase("Kills"))
								{
									int newValue = Stats.getKills(id) + i;
									Stats.setKills(id, newValue);
									sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "kills", "<value>", String.valueOf(newValue)));
								}
								else
									if (args[1].equalsIgnoreCase("Deaths"))
									{
										int newValue = Stats.getDeaths(id) + i;
										Stats.setDeaths(id, newValue);
										sender.sendMessage(Msgs.Command_Admin_Changed_Stat.getString("<player>", user, "<stat>", "deaths", "<value>", String.valueOf(newValue)));
									}
									else
										sender.sendMessage(Msgs.Error_Misc_Unkown_Command.getString());

					}
					else
					{
						sender.sendMessage(Msgs.Format_Header.getString("<title>", "Admin CMDs"));
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.AQUA + "/Inf Admin Points <Player> <#>");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add points to a player(Also goes negative)");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.BLUE + "/Inf Admin Score <Player> <#>");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add score to a player(Also goes negative)");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_AQUA + "/Inf Admin Kills <Player> <#>");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add kills to a player(Also goes negative)");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_BLUE + "/Inf Admin Deaths <Player> <#>");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Add deaths to a player(Also goes negative)");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_GRAY + "/Inf Admin Kick <Player>");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Kick a player out of Infected");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_PURPLE + "/Inf Admin Shutdown");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Prevent joining Infected");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.DARK_RED + "/Inf Admin Reload");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "Reload the config");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GOLD + "/Inf Admin Code");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.RED + "-> " + ChatColor.WHITE + ChatColor.ITALIC + "See Infected's item code for the item in hand");
					}
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "administrator" });
	}

	@Override
	public List<String> getTabs() {

		return Arrays.asList(new String[] { "Points", "Score", "Kills", "Deaths", "Kick", "Shutdown", "Reload", "Code" });
	}
}
