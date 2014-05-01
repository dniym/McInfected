
package me.sniperzciinema.infected.Messages;

import me.sniperzciinema.infected.GameMechanics.Settings;
import me.sniperzciinema.infected.Tools.Files;

import org.bukkit.ChatColor;


public enum Msgs
{
	// List of all Messages, and beside are some variables for them.
	Format_Header("Format.Header"),	// <title>>
	Format_Line("Format.Line"),
	Format_Prefix("Format.Prefix"),
	Format_List("Format.List")/* <player> */,
	Format_InfChat("Format.InfChat")/* <team>, <player>, <message> */,
	Format_Time_Second("Format.Time.Second"),
	Format_Time_Seconds("Format.Time.Seconds"),
	Format_Time_Minute("Format.Time.Minute"),
	Format_Time_Minutes("Format.Time.Minutes"),
	Command_Arena_List("Command.Arena.List")/* <valid>, <invalid> */,
	Command_Arena_Created("Command.Arena.Created")/* <arena> */,
	Command_Arena_Removed("Command.Arena.Removed")/* <arena> */,
	Command_Arena_Set("Command.Arena.Set")/* <arena> */,
	Command_Arena_SetBlock("Command.Arena.SetBlock"),
	Command_Lobby_Set("Command.Lobby.Set"),
	Command_Lobby_Tp("Command.Lobby.Tp"),
	Command_Leave_Location_Set("Command.Leave Location.Set"),
	Command_Leave_Location_Tp("Command.Leave Location.Tp"),
	Command_Spawn_Set("Command.Spawn.Set")/* <spawn> */,
	Command_Spawn_Tp("Command.Spawn.Tp")/* <spawn> */,
	Command_Spawn_Spawns("Command.Spawn.Spawns")/* <spawns> */,
	Command_Spawn_Deleted("Command.Spawn.Deleted")/* <spawn> */,
	Command_Vote("Command.Vote")/* <player>, <arena> */,
	Command_InfChat("Command.InfChat")/* <state> */,
	Command_Info_Players("Command.Info.Players") /* <players> */,
	Command_Info_State("Command.Info.State")/* <state> */,
	Command_Info_Time_Left("Command.Info.Time Left")/* <time> */,
	Command_Admin_Shutdown("Command.Admin.Shutdown")/* <state> */,
	Command_Admin_Reload("Command.Admin.Reload"),
	Command_Admin_Kicked_You("Command.Admin.Kicked.You"),
	Command_Admin_Kicked_Them("Command.Admin.Kicked.Them")/* <player> */,
	Command_Admin_Changed_Stat("Command.Admin.Changed Stat")/* <player>, <stat>,
																													 * <value> */,
	Command_Classes_SetClass("Command.Classes.SetClass")/* <class>, <team> */,
	Command_Files_Value("Command.Files.Value")/* <path> <value> */,
	Command_Files_Changed("Command.Files.Changed")/* <path> <newvalue> <value> */,
	Command_Arena_SetCreator("Command.Arena.SetCreator"),
	Error_Misc_No_Permission("Error.Misc.No Permission"),
	Error_Misc_Plugin_Unloaded("Error.Misc.Plugin Unloaded"),
	Error_Misc_Plugin_Disabled("Error.Misc.Plugin Disabled"),
	Error_Misc_Use_Command("Error.Misc.Cant Use Command"),
	Error_Misc_Not_Player("Error.Misc.Not Player"),
	Error_Misc_Unkown_Command("Error.Misc.Unkown Command"),
	Error_Misc_Not_A_File("Error.Misc.Not A File")/* <files> */,
	Error_Misc_Not_A_Path("Error.Misc.Not A Path"),
	Error_Misc_Not_A_Team("Error.Misc.Not A Team"),
	Error_Misc_Joining_While_Game_Started("Error.Misc.Joining While Game Started"),
	Error_Misc_Editing_Inventory("Error.Misc.Editing Inventory"),
	Error_Misc_Not_A_Block("Error.Misc.Not A Block"),
	Error_Game_Started("Error.Game.Started"),
	Error_Game_Not_Started("Error.Game.Not Started"),
	Error_Game_Not_In("Error.Game.Not In"),
	Error_Game_In("Error.Game.In"),
	Error_Game_They_Are_Not_In("Error.Game.They Are Not In"),
	Error_Top_Not_Stat("Error.Top.Not Stat")/* <stats> */,
	Error_Lobby_Doesnt_Exist("Error.Lobby.Doesnt Exist"),
	Error_Arena_Doesnt_Exist("Error.Arena.Doesnt Exist")/* <arena> */,
	Error_Arena_No_Valid("Error.Arena.No Valid")/* <arena> */,
	Error_Arena_None_Set("Error.Arena.None Set"),
	Error_Arena_Not_Valid("Error.Arena.Not Valid")/* <arena> */,
	Error_Arena_Already_Exists("Error.Arena.Already Exist"),
	Error_Arena_No_Spawns("Error.Arena.No Spawns"),
	Error_Arena_Not_In_Lobbys_World("Error.Arena.Not In Lobbys World"),
	Error_Arena_Not_A_Spawn("Error.Arena.Not A Spawn"),
	Error_Sign_Not_Valid("Error.Sign.Not Valid"),
	Error_Already_Voted("Error.Already Voted"),
	Menu_Classes_Click_To_Choose("Menu.Classes.Click To Choose"),
	Menu_Classes_Click_For_None("Menu.Classes.Click For None"),
	Menu_Classes_Click_To_Return("Menu.Classes.Click To Return"),
	Menu_Team_Choose("Menu.Team.Choose")/* <team> */,
	Menu_Vote_Choose("Menu.Vote.Choose"),
	Menu_Vote_Random("Menu.Vote.Random"),
	Menu_Shop_Click_To_Buy("Menu.Shop.Click To Buy") /* <Cost> */,
	Shop_Bought_Item("Shop.Bought Item")/* <item> */,
	Shop_Cost_Not_Enough("Shop.Cost.Not Enough"),
	Shop_Cost_Needed("Shop.Cost.Needed") /* <needed > */,
	Game_KillStreak_Value("Game.KillStreak.Value")/* <player>, <killstreak> */,
	Game_KillStreak_Reward("Game.KillStreak.Reward")/* <item> */,
	Game_Time_Left_Voting("Game.Time Left.Voting")/* <time> */,
	Game_Starting_In_5("Game.Starting In 5")/* <time> */,
	Game_Time_Left_Infecting("Game.Time Left.Infecting")/* <time> */,
	Game_Time_Left_Game("Game.Time Left.Game")/* <time> */,
	Game_Death_Before_Game("Game.Death.Before Game"),
	Game_Over_Humans_Win("Game.Over.Humans Win"),
	Game_Over_Winners("Game.Over.Winners")/* < winners > */,
	Game_Over_Zombies_Win("Game.Over.Zombies Win"),
	Game_Players_Left("Game.Players Left")/* < humans > , < zombies > */,
	Game_Alpha_You("Game.Alpha.You"),
	Game_Alpha_They("Game.Alpha.They")/* <player> */,
	Game_Joined_You("Game.Joined.You"),
	Game_Joined_They("Game.Joined.They")/* <player> */,
	Game_Left_You("Game.Left.You"),
	Game_Left_They("Game.Left.They")/* <player> */,
	Game_Info_Arena("Game.Info.Arena")/* < arena > , < creator > */,
	Game_End_Not_Enough_Players("Game.End.Not Enough Players"),
	Help_Vote("Help.Vote"),
	Help_Lists("Help.Lists")/* <lists> */,
	Help_TpSpawn("Help.TpSpawn"),
	Help_DelSpawn("Help.DelSpawn"),
	Help_SetSpawn("Help.SetSpawn"),
	Help_Spawns("Help.Spawns"),
	Help_Create("Help.Create"),
	Help_Remove("Help.Remove"),
	Help_SetArena("Help.SetArena"),
	Help_Top("Help.Top")/* <stats> */,
	Help_Files("Help.Files")/* Files */,
	Help_SetClass("Help.SetClass"),
	Help_SetBlock("Help.SetBlock"),
	Help_SetCreator("Help.SetCreator"),
	Classes_None("Classes.None"),
	Classes_Chosen("Classes.Chosen")/* <class> */,
	Sign_CmdSet_Not_Enough("Sign.CmdSet.Cost.Not Enough"),
	Sign_CmdSet_Cost_Needed("Sign.CmdSet.Cost.Needed") /* <needed> */,
	Grenades_Bought("Grenades.Bought"),
	Grenades_Cost_Not_Enough("Grenades.Cost.Not Enough"),
	Grenades_Invalid_Id("Grenades.Invalid Id"),
	Picture_Infected_To_Win("Picture.Infected.To Win"),
	Picture_Infected_You("Picture.Infected.You"),
	Picture_Survivor_To_Win("Picture.Survivor.To Win"),
	Picture_Survivor_You("Picture.Survivor.You");
	
	private String	string;
	
	private Msgs(String s)
	{
		this.string = s;
	}
	
	public String getString(String... variables) {
		String prefix = ChatColor.translateAlternateColorCodes('&', Files.getMessages().getString("Format.Prefix").replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar()))) + " ";
		try
		{
			String message = (this.string.startsWith("Format") || this.string.startsWith("Picture") || this.string.startsWith("Menu") || (Settings.PictureEnabled() && (this.string.startsWith("Game.Alpha") || this.string.startsWith("Game.Survior"))) ? "" : prefix) + ChatColor.translateAlternateColorCodes('&', Files.getMessages().getString(this.string).replaceAll("&x", "&" + String.valueOf(RandomChatColor.getColor().getChar())).replaceAll("&y", "&" + String.valueOf(RandomChatColor.getFormat().getChar())));
			int i = 0;
			String replace = null;
			
			for (String variable : variables)
				if (i == 0)
				{
					replace = variable;
					i++;
				}
				else
				{
					message = message.replaceAll(replace, variable);
					i = 0;
				}
			return message;
		}
		catch (NullPointerException npe)
		{
			return (this.string.startsWith("Format") || this.string.startsWith("Menu") ? "" : prefix) + "Either theres something wrong with the variables or we're unable to find message: " + this.string;
		}
	}
	
};
