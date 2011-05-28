package com.bukkit.Kitteh.GoldenRevive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.zip.ZipException;

import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem.ReviveItem;
import com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem.ReviveType;



public class GoldenReviveConfig {
	
	File file;
	Configuration config;
	GoldenRevive plugin;
	
	/*
	 * Define a new Config file using the directory path specified
	 */
	public GoldenReviveConfig(String dir, String filename, GoldenRevive instance){
		plugin = instance;
		File folder = new File(dir);
		
		if (!folder.exists()){
			folder.mkdir();
		}
		file = new File(dir + filename);
		if (file.exists())
			checkVersion(file);
		if (!file.exists()){
			//Need to create the file
			createFile();
		}
		
		config = new Configuration(file);
		if (config!=null){
			config.load();
		}else{
			plugin.consoleMessage("Error while reading configuration file, delete it and retry");
		}
		setupGoldenItems();
	}
	
	/*
	 * Create a new file 
	 */
	private void createFile(){
		try {
			file.createNewFile();
		} catch (IOException e) {
			plugin.consoleMessage(" Error creating new config file, Are you sure the correct file permissions are in place?");
		}
		//Begin copying the file
		try{
			InputStream is = GoldenReviveConfig.class.getClassLoader().getResourceAsStream("config.yml");
			
			if (is!=null){
				
				int len;
				FileOutputStream fos = new FileOutputStream(file);
				byte buf[] = new byte[1024];
				while ( (len=is.read(buf)) > 0 ){
					fos.write(buf,0,len);
				}
				fos.close();
				is.close();
				plugin.consoleMessage("new config file written");
				
			}else{
				plugin.consoleMessage("Failed to read config file from jar");
			}
		} catch (ZipException e){
			plugin.consoleMessage("Error while creating new config file - try restarting your server! and report this error: " + e);
		} catch (IOException e){
			plugin.consoleMessage(" Error creating new config file, Are you sure the correct file permissions are in place?" + e);
		} catch (Exception e ){
			plugin.consoleMessage("Error while writing config file " + e);
		}
	}
	

	/*
	 * Reload the configuration File
	 */
	public void reloadConfig(){
		config.load();
	}
	
	/*
	 * GoldenRevive Specific Configuration
	 */
	
	private void setupGoldenItems(){
		Map<String, ConfigurationNode> reviveItems = config.getNodes("ReviveItems");
		if (reviveItems == null)
			plugin.consoleMessage("There are no revive items in your config");
    	for (Map.Entry<String, ConfigurationNode>entry : reviveItems.entrySet()){
    		String key = entry.getKey();
    		ConfigurationNode node = entry.getValue();
    		
    		//Start creating and populating the handler
    		
    		int criticalHealth = node.getInt("CriticalHealth", 6);
    		int reviveItemId = node.getInt("ReviveItem", 322);
    		String rawReviveType = node.getString("ReviveType");
    		ReviveType reviveType = ReviveType.RESTORE;;
    	
    		//Handle revive type
    		if (rawReviveType!=null){
    			if (rawReviveType.toLowerCase().equals("restore"))
    				reviveType = ReviveType.RESTORE;
    			if (rawReviveType.toLowerCase().equals("revive"))
    				reviveType = ReviveType.REVIVE;
    		}
    		
    		int healthOnRevive = node.getInt("HealthOnRevive", 20);
    		
    		//Create a new Revive Item
    		ReviveItem reviveItem = new ReviveItem(criticalHealth, reviveItemId, reviveType, healthOnRevive);
    		
    		plugin.itemController.addReviveItem(reviveItemId,reviveItem);
    		plugin.itemController.addItemID(reviveItemId);
    	}
	}
	private void checkVersion(File f){
		Configuration c = new Configuration(f);
		c.load();
		double version = c.getDouble("Info.Version", 0);
		if (version == 0){
			plugin.consoleMessage("Config file is incompatible with current version - Rewriting config file");
			f.delete();
		}
	}
}
