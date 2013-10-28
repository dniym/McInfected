package me.xxsniperzzxx_sd.infected.Enums;

public enum GameState {
	INLOBBY("InLobby"), 
	VOTING("Voting"), 
	BEFOREINFECTED("BeforeInfected"), 
	STARTED("Started"), 
	GAMEOVER("GameOver"), 
	DISABLED("Disabled");

	private String name;

	private GameState(String s) {
		name = s;
	}
	public String getStatus() {
		return name;
	}	
}; 