package me.xxsniperzzxx_sd.infected.Enums;

public enum Msgs {FORMAT_LINE("Format_Line"), 
		ERROR_NOPERMISSION("Error_NoPermission"),
		ERROR_CANTUSECOMMAND("Error_CantUseCommand"), 
		ERROR_GAMESTARTED("Error_GameStarted"), 
		ERROR_NOTINGAME("Error_NotInGame"),
		ERROR_ALREADYINGAME("Error_AlreadyInGame"),
		ERROR_INFECTEDDISABLED("Error_InfectedDisabled"),
		ERROR_NOTASTAT("Error_NOTASTAT"),
		ERROR_JOINWELLSTARTEDBLOCKED("Error_JoinWellStartedBlocked"),
		ERROR_CANTEDITINVENTORYYET("Error_CantEditInventoryYet"),
		CLASSES_DISABLED("Classes_Disabled"),
		CLASSES_CHOOSEN("Classes_Chosen"),
		SHOP_PURCHASE("Shop_Purchase"),
		SHOP_INVALIDPOINTS("Shop_InvalidPoints"),
		SHOP_NEEDMOREPOINTS("Shop_NeedMorePoints"),
		GRENADE_DISABLED("Grenade_Disabled"),
		GRENADE_ONLYBUYINGAME("Grenade_OnlyBuyInGame"),
		GRENADE_NOZOMBIES("Grenade_NoZombies"),
		LOBBY_OTHERJOINEDLOBBY("Lobby_OtherJoinedLobby"),
		LOBBY_JOINLOBBY("Lobby_JoinLobby"),
		VOTE_VOTETIME("Vote_Time"),
		VOTE_HOWTOVOTE("Vote_HowToVote"),
		VOTE_TIMELEFT("Vote_TimeLeft"),
		VOTE_NOTENABLED("Vote_NotEnabled"),
		VOTE_ALLREADYVOTED("Vote_AlreadyVoted"),
		VOTE_VOTEDFOR("Vote_VotedFor"),
		VOTE_GAMEALREADYSTARTED("Vote_GameAlreadyStarted"),
		GAME_MAP("Game_Map"),
		GAME_FIRSTINFECTEDIN("Game_FirstInfectedIn"),
		GAME_INFECTIONTIMER("Game_InfectionTimer"),
		GAME_YOURAREFIRSTINFECTED("Game_YouAreFirstInfected"),
		GAME_FIRSTINFECTED("Game_FirstInfected"),
		GAME_GOTINFECTED("Game_GotInfected"),
		GAME_TIME("Game_Time"),
		GAME_TIMELEFT("Game_TimeLeft"),
		GAME_TEAMS("Game_Teams"),
		GAME_FORCEDTOSTOP("Game_ForcedToStop"),
		AFTERGAME_HUMANSWIN("AfterGame_HumansWin"),
		AFTERGAME_ZOMBESWIN("AfterGame_ZombiesWin"),
		LEAVE_NOEFFECT("Leave_NoEffect"),
		LEAVE_YOUHAVELEFT("Leave_YouHaveLeft"),
		LEAVE_NOTENOUGHPLAYERS("Leave_NotEnoughPlayers"),
		ADMIN_YOUAREKICKED("Admin_YouAreKicked");

	private String string;

	private Msgs(String s) {
		string = s;
	}
	public String getStatus() {
		return string;
	}	
}; 