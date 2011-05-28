package com.bukkit.Kitteh.GoldenRevive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;


import com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem.ItemController;
import com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem.ReviveItem;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * GoldenRevive for Bukkit
 * "Revive" a player onPlayerDeath if they have a (user specified item) in their inventory
 * @author Kitteh
 */
public class GoldenRevive extends JavaPlugin {
    private final GoldenReviveEntityListener entityListener = new GoldenReviveEntityListener(this);
    private final GoldenRevivePlayerListener playerListener = new GoldenRevivePlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public static PermissionHandler permissionHandler;
    
    public ItemController itemController;
    public HashMap<Player, Location> reviveLocations;
    /**
     * When plugin enabled - Register entity damage
     * Get plugin information from plugin.yml and display it to the console
     */
    public void onEnable() {
    	PluginManager pm = getServer().getPluginManager();
    	try{
	        /* Register Events */
	        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
	        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
	        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.High, this);
	        /* Load config */
	        itemController = new ItemController();
	        reviveLocations = new HashMap<Player,Location>();
	        PluginDescriptionFile pdfFile = this.getDescription();
	        loadConfiguration();
	        if (setupPermissions()){
	        	//permissions detected
	        	consoleMessage("Permissions Detected");
	        }else{
	        	consoleMessage("Permissions not detected - Defaulting to everyone");
	        }
	        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " is enabled!" );
    	}catch (Exception e){
    		consoleMessage("Failed to load plugin error : " + e);
    		consoleMessage("Plugin disabling");
    		pm.disablePlugin(this);
    	}
    }
    public void onDisable() {
    	PluginManager pm = getServer().getPluginManager();
    	pm.disablePlugin(this);
        consoleMessage("Plugin Disabled");
    }
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }
    /**
     * Load config from yml file plugins/GoldenRevive/config.yml
     * if the file doesn't exist - create it
     */
    private void loadConfiguration(){
    	//Specifiy the main configuration file directory and filename
    	String dir = "plugins/GoldenRevive/";
    	String filename = "config.yml";
    	//Create a new configuration file and load it
    	GoldenReviveConfig mainConfig = new GoldenReviveConfig(dir,filename,this);
    	
    	//begin to load all the values into the program
    	
    	//setReviveMessage(reviveItem);
    }
    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public void consoleMessage(String s){
    	PluginDescriptionFile pdfFile = this.getDescription();
    	System.out.println("[" + pdfFile.getName() + "] " + s);
    }
    public String getReviveMessage(int reviveItem){
    	String i = new ItemStack(reviveItem).getType().name().replace("_", " ");
	    if (i.startsWith("A") | i.startsWith("E") | i.startsWith("I") | i.startsWith("U") | i.startsWith("O"))
			return ChatColor.GOLD + "You were saved by an " + i.toLowerCase();
	    else
	    	return ChatColor.GOLD + "You were saved by a " + i.toLowerCase();
    }
	private boolean setupPermissions() {
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

        if (permissionHandler == null) {
            if (permissionsPlugin != null) {
                permissionHandler = ((Permissions) permissionsPlugin).getHandler();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

