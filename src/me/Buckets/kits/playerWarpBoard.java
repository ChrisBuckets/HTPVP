package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class playerWarpBoard implements Listener {
	@EventHandler
	public void openedPlayerWarpBoard(PlayerInteractEntityEvent event){
	 
		Player player = event.getPlayer();
		 
		 
		if(event.getRightClicked() instanceof ItemFrame){
			ItemFrame frame = (ItemFrame) event.getRightClicked();
			ItemStack item = frame.getItem();
			
			System.out.println(item);
			if(item.getType() == Material.matchMaterial("SKULL_ITEM")) playerWarps.createWarpList(player);
			
		}
	 
	}
}
