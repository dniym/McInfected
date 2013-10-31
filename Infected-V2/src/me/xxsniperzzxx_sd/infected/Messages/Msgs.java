
package me.xxsniperzzxx_sd.infected.Messages;

import me.xxsniperzzxx_sd.infected.Main;
import me.xxsniperzzxx_sd.infected.Tools.Files;

import org.bukkit.ChatColor;


public enum Msgs
{
	// List of all Messages, and beside are some variables for them.
	Format_Header("Format.Header"),	// <title>
	Format_Line("Format.Line"), Arena_Created("Arena.Created"),
	// <arena>
	Arena_Creator("Arena.Creator"), // <creator>>
	Arena_Removed("Arena.Removed"), // <arena>
	Arena_How_To_Set_More_Spawns("Arena.How To Set More Spawns"), Arena_Information(
			"Arena.Information"), // <arena> // <creator>>
	Arena_Spawn_Set("Arena.Spawn Set"), // <spawn>
	Arena_Arena_Is_Set("Arena.Arena Is Set"), Arena_Spawns("Arena.Spawns"), 
	// <spawns>>
	Arena_Spawn_Removed("Arena.Spawn Removed"), // <spawn>>
	Arena_Tpd_To_Spawn("Arena.Tpd To Spawn"),	// <spawn>
	Arena_List_Arenas("Arena.List Arenas"), // <validarenas> // <notvalidarenas>
	Error_No_Permission("Error.No Permission"), Error_Game_Already_Started(
			"Error.Games Already Started"), Error_Not_In_A_Game(
			"Error.Not In A Game"), Error_Already_In_A_Game(
			"Error.Already In A Game"), Error_Not_An_Arena("Error.Not An Arena"), 
	// <arena>
	Error_Already_An_Arena("Error.Already An Arena"), Error_Missing_Spawns(
			"Error.Missing Spawns"), Error_Plugin_Unload("Error.Plugin Unload"), Error_Cant_Use_Command(
			"Error.Cant Use Command"), Error_Not_Enough_Players(
			"Error.Not Enough Players"), Error_Max_Speed("Error.Max Speed"), GameOver_Winners(
			"GameOver.Winners"), GameOver_Times_Up("GameOver.Times Up"), GameOver_Max_Points_Reached(
			"GameOver.Max Points Reached"), Game_Start("Game.Start"), Game_Starting(
			"Game.Starting"), Game_You_Joined_A_Game("Game.You Joined A Game"), Game_StatusUpdate(
			"Game.Status Update"), 
	// <current>> // <needed>
	Game_They_Joined_A_Game("Game.They Joined A Game"),	// <player>
	Game_You_Left_A_Game("Game.You Left A Game"), Game_They_Left_A_Game(
			"Game.They Left A Game"), // <player>>
	Game_Not_Enough_Players("Game.Not Enough Players"), Game_Players_Needed(
			"Game.Players Needed"), 
	// <current>> // <needed>
	Game_PreGame_Time_Left("Game.PreGame Time Left"),	// <time>
	Game_Time_Left("Game.Game Time Left"), // <time>
	Game_Ended("Game.Ended"), Commands_How_To_Set_Spawn(
			"Commands.How To Set Spawn"), Commands_How_To_Set_Arena(
			"Commands.How To Set Arena"), Commands_How_To_Create(
			"Commands.How To Create"), Commands_How_To_Remove(
			"Commands.How To Remove"), Commands_How_To_Info(
			"Commands.How To Info"), Commands_How_To_Join(
			"Commands.How To Join"), Commands_How_To_Leave(
			"Commands.How To Leave"), Commands_How_To_Suicide(
			"Commands.How To Suicide"), Commands_How_To_Arenas(
			"Commands.How To Arenas"), Commands_How_To_Show_Arena_Spawns(
			"Commands.How To Show Arena Spawns"), Commands_How_To_Tp_To_Arena_Spawns(
			"Commands.How To Tp To Arena Spawns"), Commands_How_To_Delete_Arena_Spawns(
			"Commands.How To Delete Arena Spawns"), Commands_How_To_Admin(
			"Commands.How To Admin"), Info_Players_In("Info.Players In"),  
	// <current>>
	Info_Game_State("Info.Game State"),	// <state>
	Info_Time_Left("Info.Time Left"), // <time>
	Info_Required_Players_To_Start("Info.Required Players To Start"), Info_Time_Limit(
			"Info.Time Limit"),
	// <time>
	Info_Max_Points("Info.Max Points"),	// <points>
	Info_Creator("Info.Max Creator"),	// <creator>
	Stats_Score("Stats.Score"),	// <value>
	Stats_Kills("Stats.Kills"),	// <value>
	Stats_Deaths("Stats.Deaths"),	// <value>
	;

	private String string;

	private Msgs(String s)
	{
		string = s;
	}

	public String getString(String... variables) {
		String prefix = Main.I;
		try
		{
			String message = prefix + ChatColor.translateAlternateColorCodes('&', Files.getMessages().getString(string));
			int i = 0;
			String replace = null;
			for (String variable : variables)
			{
				if (i == 0)
				{
					replace = variable;
					i++;
				} else
				{
					message = message.replaceAll(replace, variable);
					i = 0;
				}
			}
			return message;
		} catch (NullPointerException npe)
		{
			return prefix + "Unable to find message: " + string;
		}
	}

	// Get the message from the Messages.yml, well replacing and variables given
	public String getString() {
		String prefix = Main.I;
		try
		{
			return prefix + ChatColor.translateAlternateColorCodes('&', Files.getMessages().getString(string));
		} catch (NullPointerException npe)
		{
			return prefix + "Unable to find message: " + string;
		}
	}
};
