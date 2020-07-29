package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class itemDrop implements Listener {
	
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getItemDrop().getItemStack().getType() == Material.BOWL) {
			//player.sendMessage("test");
			event.getItemDrop().remove();
		}
	}
}
