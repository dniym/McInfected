package me.sniperzciinema.infectedv2.Tools;


public enum Events{
	Kill("Kill"),
	Death("Death"),
	GameEnds("Game Ends"),
	Infected("Zombies Infect"),
	Survive("Humans Survive");
	
	private String string;
	
	private Events(String string){
		this.string = string;
	}
	@Override
	public String toString(){
		return string;
	}
};
