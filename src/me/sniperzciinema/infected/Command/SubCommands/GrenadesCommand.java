
package me.sniperzciinema.infected.Command.SubCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.sniperzciinema.infected.Infected;
import me.sniperzciinema.infected.Command.SubCommand;
import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Handlers.Lobby;
import me.sniperzciinema.infected.Handlers.Grenades.Grenade;
import me.sniperzciinema.infected.Handlers.Grenades.GrenadeManager;
import me.sniperzciinema.infected.Handlers.Player.InfPlayer;
import me.sniperzciinema.infected.Handlers.Player.InfPlayerManager;
import me.sniperzciinema.infected.Messages.Msgs;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class GrenadesCommand extends SubCommand {

	public GrenadesCommand()
	{
		super("grenades");
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			InfPlayer ip = InfPlayerManager.getInfPlayer(p);
			if (!p.hasPermission("Infected.Grenades"))
				p.sendMessage(Msgs.Error_Misc_No_Permission.getString());

			else
				if (!Lobby.isInGame(p))
					p.sendMessage(Msgs.Error_Game_Not_In.getString());
				else
					if (args.length == 2)
					{
						if (args[1].matches("[0-9]+"))
						{
							int gi = Integer.parseInt(args[1]) - 1;
							if (GrenadeManager.getGrenades().get(gi) != null)
							{
								if (ip.getPoints(Settings.VaultEnabled()) >= GrenadeManager.getGrenades().get(gi).getCost())
								{
									Grenade grenade = GrenadeManager.getGrenades().get(gi);
									p.getInventory().addItem(grenade.getItem());
									ip.setPoints(ip.getPoints(Settings.VaultEnabled()) - grenade.getCost(), Settings.VaultEnabled());
									p.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));

								}
								else
									p.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
							}
							else
								p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
						}
						else
							p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
					}
					else
						if (args.length == 3)
						{
							int amount = Integer.parseInt(args[2]);

							if (args[1].matches("[0-9]+"))
							{
								int gi = Integer.parseInt(args[1]) - 1;
								if (GrenadeManager.getGrenades().get(gi) != null)
								{
									if (ip.getPoints(Settings.VaultEnabled()) >= (GrenadeManager.getGrenades().get(gi).getCost() * amount))
									{
										Grenade grenade = GrenadeManager.getGrenades().get(gi);
										ItemStack g = grenade.getItem();
										g.setAmount(amount);
										p.getInventory().addItem(g);
										ip.setPoints(ip.getPoints(Settings.VaultEnabled()) - (grenade.getCost() * amount), Settings.VaultEnabled());
										p.sendMessage(Msgs.Grenades_Bought.getString("<grenade>", grenade.getName()));
									}
									else
										p.sendMessage(Msgs.Grenades_Cost_Not_Enough.getString());
								}
								else
									p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
							}
							else
								p.sendMessage(Msgs.Grenades_Invalid_Id.getString());
						}
						else
							Infected.Menus.grenadeMenu.open(p);
		}
		else
			sender.sendMessage(Msgs.Error_Misc_Not_Player.getString());

	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "grenade", "explode" });
	}

	@Override
	public List<String> getTabs() {
		List<String> grenades = new ArrayList<String>();
		for (Grenade nade : GrenadeManager.getGrenades())
			grenades.add(nade.getName());

		return grenades;
	}
}
