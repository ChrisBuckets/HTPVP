package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class bountyBoard implements Listener {
	@EventHandler
	public void openedBountyBoard(PlayerInteractEntityEvent event){
	 
		Player player = event.getPlayer();
		 
		 
		if(event.getRightClicked() instanceof ItemFrame){
			ItemFrame frame = (ItemFrame) event.getRightClicked();
			Item item = (Item) frame.getItem();
			
			
			
			if(item.equals(Material.MAP)) {
				
				player.sendMessage("yo");
			}
			player.sendMessage("You right clicked an item frame!");
			
		}
	 
	}
}
