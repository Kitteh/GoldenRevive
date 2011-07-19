package com.bukkit.Kitteh.GoldenRevive;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem.ReviveItem;
import com.bukkit.Kitteh.GoldenRevive.GoldenReviveItem.ReviveType;

/**
 * Handle events for all Player related events
 * @author Kitteh
 */
public class GoldenReviveEntityListener extends EntityListener {
    private final GoldenRevive plugin;
    public GoldenReviveEntityListener(GoldenRevive instance) {
        plugin = instance;
    }
    /**
     * Check on player damage if their health is below "Critical"
     * If they fall below Critical - As defined by the user - They are restored back to the revive item's health value
     * This is all specfied via the ReviveItem object
     */
    public void onEntityDamage(EntityDamageEvent event) {
    	if(event.getEntity() instanceof Player) {
    		Player p = (Player) event.getEntity();
    		if (p!=null){
    			if (GoldenRevive.permissionHandler!=null){
    				if (!GoldenRevive.permissionHandler.has(p, "GoldenRevive.Restore"))
    					return;
    			}else if (GoldenRevive.superPermissions == true){
    				if (!p.hasPermission("GoldenRevive.Restore"))
    					return;
    			}
	    		try{
	    			Inventory inv = p.getInventory();
	    			int playerHealth = p.getHealth() - p.getLastDamage();
	    			List<Integer> reviveItemIds = plugin.itemController.getReviveItemIds();
	    			if (reviveItemIds == null)
	    				return;
	    			for(int itemId : reviveItemIds){
	    				if (inv.contains(itemId)){
	    					ReviveItem reviveItem = plugin.itemController.getReviveItemById(itemId);
	    					if (reviveItem!=null){
	    						if (reviveItem.getType().equals(ReviveType.RESTORE))
	    						if (playerHealth < reviveItem.getCriticalHealth()){
	    							event.setCancelled(true);
	    							p.setHealth(reviveItem.getHealthOnRevive());
	    							p.sendMessage(plugin.getReviveMessage(reviveItem.getItem()));
	    							ItemStack oneReviveItem = new ItemStack(reviveItem.getItem(),1);
	    							inv.removeItem(oneReviveItem);
	    						}
	    					}
	    				}
	    			}
	    		}catch (Exception e){
	    			plugin.consoleMessage("Error while handling goldensave event Unexpected error : " + e);
	    		}
    		}else{
    			plugin.consoleMessage("Failed to determine player during entity damage event");
    		}
    	}
    }
    
    /**
     * When the player dies, Check if they had a revive item. If they did - Respawn them at their last location
     */
    
    public void onEntityDeath(EntityDeathEvent e){
    	if (e.getEntity() instanceof Player){
    	Player p = (Player) e.getEntity();
    	if (p!=null){
    		//Check if player has permission
    		if  (GoldenRevive.permissionHandler!=null){
				if (!GoldenRevive.permissionHandler.has(p, "GoldenRevive.Revive"))
					return;
    		}else if (GoldenRevive.superPermissions == true){
				if (!p.hasPermission("GoldenRevive.Revive"))
					return;
			}
    		//Check if the player had a (ReviveItem)
    		List<ItemStack> items = e.getDrops();
    		for(ItemStack item : items){
    			ReviveItem reviveItem = plugin.itemController.getReviveItemById(item.getTypeId());
    			if (reviveItem!=null){
    				if (reviveItem.getType() == ReviveType.REVIVE){
    					ItemStack oneReviveItem = new ItemStack(reviveItem.getItem(),1);
    					items.remove(oneReviveItem);
    					plugin.reviveLocations.put(p, p.getLocation());
    					return;
    				}
    			}
    		}
    	}
    	}
    }
}

