package com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;

import com.bukkit.Kitteh.GoldenRevive.GoldenRevive;

public class ItemController {
	private HashMap<Integer, ReviveItem> reviveItems;
	private List<Integer> reviveItemIds;
	
	public ItemController() {
		reviveItems = new HashMap<Integer, ReviveItem>() ;
		reviveItemIds = new ArrayList<Integer>();
	}

	public List<Integer> getReviveItemIds(){
		return reviveItemIds;
	}
	
	public ReviveItem getReviveItemById(int i){
		for (Entry<Integer, ReviveItem>entry : reviveItems.entrySet()){
			if (entry.getKey() == i){
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void addItemID (int i){
		reviveItemIds.add(i);
	}
	
	public void addReviveItem(int id, ReviveItem item){
		reviveItems.put(id, item);
	}
}
