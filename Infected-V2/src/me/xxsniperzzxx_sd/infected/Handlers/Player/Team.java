
package me.xxsniperzzxx_sd.infected.Handlers.Player;

import java.util.List;

import me.xxsniperzzxx_sd.infected.Tools.Files;


public enum Team
{
	Human("Human"), Zombie("Zombie"), None("None");

	private String string;

	private Team(String s)
	{
		string = s;
	}

	public List<String> getKillMessages() {
		return Files.getMessages().getStringList("Deaths." + string);
	}

};
