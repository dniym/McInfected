
package me.sniperzciinema.infectedv2.Handlers.Player;

import java.util.List;

import me.sniperzciinema.infectedv2.GameMechanics.DeathType;
import me.sniperzciinema.infectedv2.Tools.Files;


public enum Team
{
	Human("Human"), Zombie("Zombie"), None("None");

	private String string;

	private Team(String s)
	{
		string = s;
	}

	public List<String> getKillMessages(DeathType death) {
		return Files.getMessages().getStringList("Deaths." + string + "." + death);
	}

};
