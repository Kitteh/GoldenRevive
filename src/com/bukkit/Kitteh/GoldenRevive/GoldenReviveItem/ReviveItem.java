package com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem;

public class ReviveItem {
	private int criticalHealth;
	private int item;
	private ReviveType type;
	private int healthOnRevive;
	
	public ReviveItem(int critHealth, int itemId, ReviveType t, int reviveHealth){
		criticalHealth = critHealth;
		item = itemId;
		type = t;
		healthOnRevive = reviveHealth;
	}
	
	public int getCriticalHealth(){
		return criticalHealth;
	}
	
	public int getItem(){
		return item;
	}
	
	public ReviveType getType(){
		return type;
	}
	
	public int getHealthOnRevive(){
		return healthOnRevive;
	}
}
