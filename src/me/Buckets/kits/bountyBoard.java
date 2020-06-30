package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class bountyBoard implements Listener {
	@EventHandler
	public void openedBountyBoard(PlayerInteractEntityEvent event){
	 
		Player player = event.getPlayer();
		 
		 
		if(event.getRightClicked() instanceof ItemFrame){
			ItemFrame frame = (ItemFrame) event.getRightClicked();
			ItemStack item = frame.getItem();
			
			System.out.println(item);
			if(item.getType() == Material.matchMaterial("EMPTY_MAP")) Bounty.createBountyList(player);
			
		}
	 
	}
}
