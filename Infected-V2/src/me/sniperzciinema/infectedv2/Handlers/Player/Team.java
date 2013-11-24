
package me.sniperzciinema.infectedv2.Handlers.Player;

import java.util.List;

import me.sniperzciinema.infectedv2.GameMechanics.DeathType;
import me.sniperzciinema.infectedv2.Tools.Files;


public enum Team
{
	Human("Human"), Zombie("Zombie"), None("None"), Global("Global");

	private String string;

	private Team(String s)
	{
		string = s;
	}
	
	@Override
	public String toString(){
		return string;
	}
	public List<String> getKillMessages(DeathType death) {
		return Files.getMessages().getStringList("Deaths." + string + "." + death);
	}

};
