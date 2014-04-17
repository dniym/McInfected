
package me.sniperzciinema.infected.Enums;

public enum Events
{
	// Different events that can happen during Infected that rewards points
	// and/or score
	Kill("Kill"),
	Death("Death"),
	GameEnds("Game Ends"),
	Infected("Zombies Infect"),
	Survive("Humans Survive");

	private String	string;

	private Events(String string)
	{
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}
};
