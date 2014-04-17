
package me.sniperzciinema.infected.Enums;

public enum DeathType
{

	// All the death possible death types.

	// Other includes, suicides, lava, falling, joining well the game is
	// going(As we need to infect the player)

	Arrow("Arrow"),
	Melee("Melee"),
	Grenade("Grenade"),
	Gun("Gun"),
	Other("Other");

	private String	string;

	private DeathType(String s)
	{
		string = s;
	}

	@Override
	public String toString() {
		return string;
	}
};
