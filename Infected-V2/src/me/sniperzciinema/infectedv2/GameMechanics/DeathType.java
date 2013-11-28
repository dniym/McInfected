
package me.sniperzciinema.infectedv2.GameMechanics;

public enum DeathType
{

	Arrow("Arrow"), Melee("Melee"), Grenade("Grenade"), Gun("Gun"), Snowball("Snowball"), Egg("Egg"), Other("Other");

	private String string;

	private DeathType(String s)
	{
		string = s;
	}
	
	@Override
	public String toString(){
		return string;
	}
};