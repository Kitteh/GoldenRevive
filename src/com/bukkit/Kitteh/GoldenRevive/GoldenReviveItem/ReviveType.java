package com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem;

public enum ReviveType {
	RESTORE(1),REVIVE(2);
	
	private final int code;

	ReviveType(int c){
		code = c;
	}
	public int getCode(){
		return code;
	}
}
