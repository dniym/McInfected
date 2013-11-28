
package me.sniperzciinema.infectedv2.Handlers.Player;


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

};
