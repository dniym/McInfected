
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Arena.Arena;
import me.sniperzciinema.infected.Handlers.Classes.InfClassManager;
import me.sniperzciinema.infected.Handlers.Items.ItemHandler;
import me.sniperzciinema.infected.Handlers.Location.LocationHandler;
import me.sniperzciinema.infected.Handlers.Player.Team;
import me.sniperzciinema.infected.Messages.Msgs;
import me.sniperzciinema.infected.Messages.RandomChatColor;
import me.sniperzciinema.infected.Tools.FancyMessages.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetupCommand extends SubCommand {

	public SetupCommand()
	{
		super("setup");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			if (!sender.hasPermission("Infected.Setup"))
				sender.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
			{
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage(Msgs.Format_Header.getString("<title>", " Setup "));
				sender.sendMessage("");
				
				if (args.length == 1)
				{
					new FancyMessage(Msgs.Format_Prefix.getString()).then("" + ChatColor.GOLD + ChatColor.BOLD + "Edit The Lobby").tooltip(ChatColor.GOLD + "Edit the Lobby").command("/Infected Setup Lobby").send(p);
					for (Arena arena : Lobby.getArenas())
						new FancyMessage(Msgs.Format_Prefix.getString()).then("" + ChatColor.YELLOW + ChatColor.BOLD + "Edit Arena: " + RandomChatColor.getColor() + arena.getName()).tooltip(ChatColor.YELLOW + "Edit " + arena.getName()).command("/Infected Setup " + arena.getName()).send(p);
				} else
				{
					if (Lobby.getArena(args[1]) != null)
					{
						Arena arena = Lobby.getArena(args[1]);
						if (args.length == 2)
						{
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.GREEN + "SetArena").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.GREEN + arena.getName() + " Select the Arena")).command("/Infected SetArena "+arena.getName()).send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.YELLOW + "Spawns").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.YELLOW + arena.getName() + " Spawns", "   §eGlobal: " + arena.getExactSpawns(Team.Global).size(), "   §aHuman: " + arena.getExactSpawns(Team.Human).size(), "   §cZombie: " + arena.getExactSpawns(Team.Zombie).size())).command("/Infected Setup "+arena.getName()+" Spawns").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.RED + "Creator").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.RED + arena.getName() + " Creator", "   §eCreator: " + arena.getCreator())).command("/Infected Setup "+arena.getName()+" Creator").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.DARK_AQUA + "Block").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.DARK_AQUA + arena.getName() + " Creator", "   §eBlock: " + ItemHandler.getItemStackToString(arena.getBlock()))).command("/Infected Setup "+arena.getName()+" Block").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.LIGHT_PURPLE + "Time").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.LIGHT_PURPLE + arena.getName() + " Time", "   §eVoting Time: " + arena.getSettings().getVotingTime(), "   §cInfecting Time: " + arena.getSettings().getInfectingTime(), "   §aGame Time: " + arena.getSettings().getGameTime())).command("/Infected Setup "+arena.getName()+" Time").send(p);
							//new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.AQUA + "Booleans").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.AQUA + arena.getName() + " Booleans", "   §eInteract Blocked: " + arena.getSettings().interactDisabled(), "   §cEnchant Blocked: " + arena.getSettings().enchantDisabled(), "   §6Hunger Blocked: " + arena.getSettings().hungerDisabled())).command("/Infected Setup "+arena.getName()+" Booleans").send(p);
							p.sendMessage("");
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup").send(p);
						}
						else{

							if (args[2].equals("Spawns"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Tp to a Global Spawn").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Teleport to a Global Spawn")).suggest("/Infected TpSpawn Global #").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Tp to a Human Spawn").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Teleport to a Human Spawn")).suggest("/Infected TpSpawn Human #").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Tp to a Zombie Spawn").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Teleport to a Zombie Spawn")).suggest("/Infected TpSpawn Zombie #").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup "+ arena.getName()).send(p);
							}
							else if (args[2].equals("Creator"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Creator").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() +" Set a creator for the arena")).suggest("/Infected SetCreator <Creator>").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup "+ arena.getName()).send(p);
							}
							else if (args[2].equals("Block"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set The Block").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() +"Set the block you see in the vote menu")).suggest("/Infected SetBlock <ItemCode>").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup "+ arena.getName()).send(p);
							}
							else if (args[2].equals("Time"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Voting Time").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() +" Set the time you have to vote")).suggest("/Infected Files Arenas Arenas."+arena.getName()+".Time.Voting <#>").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Infecting Time").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() +" Set the time you have to wait for the first Infected")).suggest("/Infected Files Arenas Arenas."+arena.getName()+".Time.Infecting <#>").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Play Time").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() +" Set the time you have to play")).suggest("/Infected Files Arenas Arenas."+arena.getName()+".Time.Game <#>").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup "+ arena.getName()).send(p);
							}
						}
					} else if (args[1].equals("Lobby"))
					{
						if (args.length == 2)
						{
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.GREEN + "Arenas").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.GREEN + "Lobby Arenas", "Total Arenas: " + Lobby.getArenas().size())).command("/Infected Setup Lobby Arenas").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.DARK_AQUA + "Classes").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.DARK_AQUA + "Lobby Classes", "§aHuman: " + InfClassManager.getClasses(Team.Human).size(), "§cZombies: " + InfClassManager.getClasses(Team.Zombie).size())).command("/Infected Setup Lobby Classes").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.WHITE + "Location").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.WHITE + "Lobby Location", "§7Location: " + LocationHandler.getRoundedLocation(Lobby.getLocation()))).command("/Infected Setup Lobby Location").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.GRAY + "Leave").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.GRAY + "Lobby Leave", "§fLeave: " + LocationHandler.getRoundedLocation(Lobby.getLeave()))).command("/Infected Setup Lobby Leave").send(p);
							new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.RED + "Booleans").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.RED + "Lobby Booleans", "§7Can Join Well Started: " + Settings.isJoiningDuringGamePrevented(), "§5Can Edit Inventory: " + !Settings.isEditingInventoryPrevented())).command("/Infected Setup Lobby Booleans").send(p);
							p.sendMessage("");
							new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup").send(p);
						} else
						{
							if (args[2].equals("Arenas"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.GREEN + "See all Arenas").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.GREEN + "See all the arenas")).command("/Infected Arenas").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.YELLOW + "Create an Arena").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.YELLOW + "Create an arena")).suggest("/Infected Create <Arena Name>").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.RED + "Remove an Arena").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.RED + "Remove an arena")).suggest("/Infected Remove <Arena Name>").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(ChatColor.AQUA + "Select an Arena").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.AQUA + "Select an arena")).suggest("/Infected SetArena <Arena Name>").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup Lobby").send(p);
							} else if (args[2].equals("Classes"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Create a Class").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Create a class with your inventory")).suggest("/Infected SetClass <ClassName> <Human/Zombie>").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup Lobby").send(p);
							} else if (args[2].equals("Location"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Location").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Set to where you are")).command("/Infected SetLobby").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Teleport to Location").itemTooltip(ItemHandler.getFancyMessageItem(ChatColor.GREEN + "Tp to the lobby")).command("/Infected TpLobby").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup Lobby").send(p);
							} else if (args[2].equals("Leave"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Leave").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Set to where you are")).command("/Infected SetLeave").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Teleport to Leave").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Tp to the leave")).command("/Infected TpLeave").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup Lobby").send(p);
							} else if (args[2].equals("Booleans"))
							{
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Can Join Well Started").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Set Can Join When Started")).suggest("/Infected Files Config Settings.Misc.Prevent_Joining_During_Game <True/False>").send(p);
								new FancyMessage(Msgs.Format_Prefix.getString()).then(RandomChatColor.getColor() + "Set Can Edit Inventory").itemTooltip(ItemHandler.getFancyMessageItem(RandomChatColor.getColor() + "Set if they can edit their inventory")).suggest("/Infected Files Config Settings.Misc.Prevent_Editing_Inventory <True/False>").send(p);
								p.sendMessage("");
								new FancyMessage(Msgs.Format_Prefix.getString()).then("§4<< Back").tooltip("Go back a Setup Page").command("/Infected Setup Lobby").send(p);
							}
						}
						sender.sendMessage("");
						sender.sendMessage(Msgs.Format_Line.getString());
					} else
					{
						p.performCommand("Infected Setup");
					}
				}
			}
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "manage" });
	}
}
