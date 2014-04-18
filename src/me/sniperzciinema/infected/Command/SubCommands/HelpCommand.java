
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Tools.FancyMessages.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HelpCommand extends SubCommand {

	public HelpCommand()
	{
		super("help");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (args.length != 1)
		{

			sender.sendMessage(Msgs.Format_Header.getString("<title>", "Infected Help " + args[1] + ""));
			if (sender instanceof Player)
			{
				Player p = (Player) sender;
				if (args[1].equalsIgnoreCase("1"))
				{
					if (sender.hasPermission("Infected.Join"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aJoin").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Join", " ", "§7Join Infected")).suggest("/Infected Join").send(p);
					if (sender.hasPermission("Infected.Leave"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aLeave").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Leave", " ", "§7Leave Infected")).suggest("/Infected Leave").send(p);
					if (sender.hasPermission("Infected.Vote"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aVote").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Vote [Arena]", " ", "§7Typing this command without saying", "§7an arena will open the GUI.", "§7Specifing an arena will add a vote for it.")).suggest("/Infected Vote").send(p);
					if (sender.hasPermission("Infected.Classes"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aClasses").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Classes", " ", "§7Open a GUI that allows you to choose", "§7 a class for either teams.", " ", "§eRight Click to select, but not close the GUI.")).suggest("/Infected Classes").send(p);
					if (sender.hasPermission("Infected.Shop"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aShop").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Shop", " ", "§7Open a GUI shop that allows you to", "§7purchase custom items.", " ", "§eRight Click to select, but not close the GUI.")).suggest("/Infected Shop").send(p);
					if (sender.hasPermission("Infected.Grenades"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aGrenades").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Grenades", " ", "§7Open a GUI shop that allows you to", "§7purchase custom grenades.", " ", "§eRight Click to select, but not close the GUI.")).suggest("/Infected Grenades").send(p);
					if (sender.hasPermission("Infected.List"))
						new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aList <Playing/Humans/Zombies>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected List <Playing/Humans/Zombies>", " ", "§7See a list of players for that category")).suggest("/Infected List <Playing/Humans/Zombies>").send(p);
				}
				else
					if (args[1].equals("2"))
					{
						if (sender.hasPermission("Infected.Chat"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aChat").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Chat [Message]", " ", "§7Typing this command without saying", "§7a message will toggle you into Infected's", "§7team chat.")).suggest("/Infected Chat").send(p);
						if (sender.hasPermission("Infected.Stats"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aStats").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Stats [Stat]", " ", "§7See you current stats/info.")).suggest("/Infected Stats").send(p);
						if (sender.hasPermission("Infected.Suicide"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSuicide").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Suicide", " ", "§7If you ever get stuck and you need to", "§7get out, just use this command to respawn.")).suggest("/Infected Suicide").send(p);
						if (sender.hasPermission("Infected.Info"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aInfo").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Info", " ", "§7See the current status of the game.")).suggest("/Infected Info").send(p);
						if (sender.hasPermission("Infected.Top"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aTop").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Top [Stat]", " ", "§7View the top 5 players with the", "§7highest total in that stat.", " ", "§eStats: §aKills, Deaths, Points, Score, Time, KillStreak")).suggest("/Infected Top").send(p);
						if (sender.hasPermission("Infected.Arenas"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aArenas").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Arenas", " ", "§7See the list of Arenas")).suggest("/Infected Arenas").send(p);
						if (sender.hasPermission("Infected.SetLobby"))
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSetLobby").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected SetLobby", " ", "§7Set Infected's Lobby")).suggest("/Infected SetLobby").send(p);
					}
					else
						if (args[1].equals("3"))
						{
							if (sender.hasPermission("Infected.SetLeave"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSetLeave").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected SetLeave", " ", "§7Set the location of where players are", "§7sent to after leaving Infected", " ", "§eIf this isn't set players will be sent to their", "§elast location.")).suggest("/Infected SetLeave").send(p);
							if (sender.hasPermission("Infected.SetSpawn"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSetSpawn <Zombie/Human/Global>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected SetSpawn <Zombie/Human/Global>", " ", "§7Set the specific teams spawn for this arena", " ", "§b  Global §e-> Both Teams", "§c  Zombie §e-> Spawn for just the zombies", "§a  Humans §e-> Spawn for just the humans.")).suggest("/Infected SetSpawn <Zombie/Human/Global>").send(p);
							if (sender.hasPermission("Infected.Spawns"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSpawns <Zombie/Human/Global>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Spawns [Zombie/Human/Global]", " ", "§7See how many spawns the specific teams have for this arena", " ", "§b  Global §e-> Both Teams", "§c  Zombie §e-> Spawn for just the zombies", "§a  Humans §e-> Spawn for just the humans.")).suggest("/Infected Spawns <Zombies/Humans/Global>").send(p);
							if (sender.hasPermission("Infected.TpSpawn"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aTpSpawn <Zombie/Human/Global> <#>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Spawns [Zombie/Human/Global]", " ", "§7Teleport to the spawn for the specific teams.", " ", "§b  Global §e-> Both Teams", "§c  Zombie §e-> Spawn for just the zombies", "§a  Humans §e-> Spawn for just the humans.")).suggest("/Infected TpSpawns <Zombies/Humans/Global> <#>").send(p);
							if (sender.hasPermission("Infected.DelSpawn"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aDelSpawn <Zombie/Human/Global> <#>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected DelSpawn [Zombie/Human/Global]", " ", "§7Delete the spawn for the specific teams.", " ", "§b  Global §e-> Both Teams", "§c  Zombie §e-> Spawn for just the zombies", "§a  Humans §e-> Spawn for just the humans.")).suggest("/Infected DelSpawn <Zombies/Humans/Global> <#>").send(p);
							if (sender.hasPermission("Infected.SetArena"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSetArena <Arena>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected SetArena <Arena>", " ", "§7Select an arena to be edited")).suggest("/Infected SetArena <Arena>").send(p);
							if (sender.hasPermission("Infected.Create"))
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aCreate <Arena> [Creator]").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Create <Arena> [Creator]", " ", "§7Create an arena")).suggest("/Infected Create <Arena>").send(p);
						}
						else
							if (args[1].equalsIgnoreCase("4"))
							{
								if (sender.hasPermission("Infected.Remove"))
									new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aRemove <Arena>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Remove <Arena>", " ", "§7Remove an arena")).suggest("/Infected Remove <Arena>").send(p);
								if (sender.hasPermission("Infected.Admin"))
									new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aAdmin").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Admin", " ", "§7Access Infected's Admin Menu")).suggest("/Infected Admin").send(p);
								if (sender.hasPermission("Infected.Files"))
									new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aFiles <File> [Path] [NewValue]").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected Files <File> [Path] [NewValue]", " ", "§7Edits Infected's configs/settings from in game", " ", "§eLeaving out a path and a new value will show you the file", "§eLeaving out a new value will tell you the path's current value", " ", "§eStats: §aConfig, Arenas, Classes, Grenades, Messages, Players, Shop, Signs")).suggest("/Infected Files").send(p);
								if (sender.hasPermission("Infected.SetClass"))
									new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aSetClass <ClassName> <Human/Zombie>").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected SetClass <ClassName> <Human/Zombie>", " ", "§7Create a class for Infected", " ", "§eThis will create a class out of your inventory, armor, and potions.", " ", "§c  Zombie §e-> Spawn for just the zombies", "§a  Humans §e-> Spawn for just the humans.")).suggest("/Infected SetClass <ClassName> <Human/Zombie>").send(p);
								if (sender.hasPermission("Infected.TpLobby"))
									new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aTpLobby").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected TpLobby", " ", "§7Teleport to the Lobby")).suggest("/Infected TpLobby").send(p);
								if (sender.hasPermission("Infected.TpLeave"))
									new FancyMessage(Msgs.Format_Prefix.getString()).then("§7/Infected §aTpLeave").itemTooltip(ItemHandler.getFancyMessageItem("§a§l/Infected TpLeave", " ", "§7Teleport to the Leave Location")).suggest("/Infected TpLeave").send(p);
							}
				new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Help Page").command("/Infected Help " + String.valueOf(Integer.parseInt(args[1]) - 1)).then("             ").then("§6Next >>").tooltip("Go to the next Help Page").command("/Infected Help " + String.valueOf(Integer.parseInt(args[1]) + 1)).send(p);

			}
			else
				if (args[1].equals("1"))
				{
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Join" + ChatColor.WHITE + " - Join Infected");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Leave" + ChatColor.WHITE + " - Leave Infected");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Vote" + ChatColor.WHITE + " - Vote for a map");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Classes" + ChatColor.WHITE + " - Choose a class");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Shop" + ChatColor.WHITE + " - See the purchasable items");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Grenades" + ChatColor.WHITE + " - See the purchasable grenades");
					sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "List" + ChatColor.WHITE + " - See the list of players");
				}
				else
					if (args[1].equals("2"))
					{
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Chat [Msg]" + ChatColor.WHITE + " - Chat in your team's chat");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Stats [Player]" + ChatColor.WHITE + " - Check a player's stats");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Suicide" + ChatColor.WHITE + " - Suicide if you're stuck");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Info" + ChatColor.WHITE + " - Check Infected's status");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Top [Stat]" + ChatColor.WHITE + " - Check the top 5 players stats");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Arenas" + ChatColor.WHITE + " - See all possible arenas");
						sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLobby" + ChatColor.WHITE + " - Set the main lobby");
					}
					else
						if (args[1].equals("3"))
						{
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetSpawn [Global/Human/Zombie]" + ChatColor.WHITE + " - Set the spawn for the selected arena");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetLeave" + ChatColor.WHITE + " - Set the leave location");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Spawns [Global/Human/Zombie]" + ChatColor.WHITE + " - List the number of spawns for a map");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpSpawn [Global/Human/Zombie] [#]" + ChatColor.WHITE + " - Tp to a spawn ID");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "DelSpawn [Global/Human/Zombie] [ #]" + ChatColor.WHITE + " - Delete the spawn ID");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetArena [Arena]" + ChatColor.WHITE + " - Select an arena");
							sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Create [Arena]" + ChatColor.WHITE + " - Create an arena");
						}
						else
							if (args[1].equals("4"))
							{
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Remove[Arena]" + ChatColor.WHITE + " - Remove an Arena");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Admin" + ChatColor.WHITE + " - View the admin menu");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "Files" + ChatColor.WHITE + " - Edit Files in Game");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "SetClass" + ChatColor.WHITE + " - Create a class with you inventory");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpLobby" + ChatColor.WHITE + " - Tp to the lobby");
								sender.sendMessage(Msgs.Format_Prefix.getString() + ChatColor.GRAY + "/Inf " + ChatColor.GREEN + "TpLeave" + ChatColor.WHITE + " - Tp to the leave location");
							}

			sender.sendMessage(Msgs.Format_Line.getString());
		}
		else
			Bukkit.getServer().dispatchCommand(sender, "Infected Help 1");

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "h", "?" });
	}
}
