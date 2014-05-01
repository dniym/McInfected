
package me.sniperzciinema.infected.Handlers.Player;

public enum Team
{
	Human("Human"), Zombie("Zombie"), None("None"), Global("Global");
	
	private String	string;
	
	private Team(String s)
	{
		this.string = s;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
};
