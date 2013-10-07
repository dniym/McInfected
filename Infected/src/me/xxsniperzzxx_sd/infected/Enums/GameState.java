package me.xxsniperzzxx_sd.infected.Enums;

public enum GameState {INLOBBY("inlobby"), VOTING("voting"), BEFOREINFECTED("beforeinfected"), STARTED("started"), GAMEOVER("gameover"), DISABLED("disabled");

	private String name;

	private GameState(String s) {
		name = s;
	}
	public String getTeam() {
		return name;
	}	
}; 