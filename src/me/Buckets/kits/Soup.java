package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Soup implements Listener {
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event){
		System.out.println(event.getAction());
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
		    Player player = (Player) event.getPlayer();
		    if(player.getInventory().getItemInHand().getType() == Material.matchMaterial("MUSHROOM_SOUP")) {
		    	if(!soupHeal(player)) return;
		    	player.getInventory().getItemInHand().setType(Material.BOWL);
		    	player.updateInventory();
		    	
		    }
		}	
		
		
	}
	
	
	@EventHandler
	public void disableHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
		e.setFoodLevel(20);
	}
	
	
	public Boolean soupHeal(Player player) {
		if(player.getHealth() > 19.0) {
			return false;
		}
		
		double missingHealth = 20.0 - player.getHealth();
		if(missingHealth < 7.0) {
			player.setHealth(20.0);
			return true;
		}
		player.setHealth(player.getHealth() + 7.0);
		return true;
	}
}
