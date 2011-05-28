package com.bukkit.Kitteh.GoldenRevive;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GoldenRevivePlayerListener extends PlayerListener{
	GoldenRevive plugin;
	
	public GoldenRevivePlayerListener(GoldenRevive instance){
		plugin = instance;
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		if (p!=null)
		if (plugin.reviveLocations.containsKey(p)){
			p.sendMessage(ChatColor.AQUA + "You have been revived");
			e.setRespawnLocation(plugin.reviveLocations.get(p));
			
			plugin.reviveLocations.remove(p);
		}
	}
}
