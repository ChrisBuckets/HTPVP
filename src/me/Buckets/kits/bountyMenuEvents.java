package me.Buckets.kits;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class bountyMenuEvents implements Listener{
	@EventHandler()
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Boolean check = false;
		for(Inventory inv : Bounty.getBountyMenus()) {
			if(event.getInventory().equals(inv)) check = true;
			break;
		}
		if(!check) return;
		
		
		
		
		if(check) event.setCancelled(true);
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getItemMeta() == null) return;
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Close")) {
			player.closeInventory();
			return;
		}
		
		
		
		event.setCancelled(true);
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "<- Previous")) {
			if(Bounty.getBountyMenus().indexOf(event.getInventory()) - 1 < 0) {
				player.sendMessage("This is the first page in the menu");
				return;
			}
			
			int index = Bounty.getBountyMenus().indexOf(event.getInventory()) - 1;
			player.closeInventory();
			player.openInventory(Bounty.getBountyMenus().get(index));
			return;
		}
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Next ->")) {
			if(Bounty.getBountyMenus().indexOf(event.getInventory()) + 1 == Bounty.getBountyMenus().size()) {
				player.sendMessage("This is the last page in the menu");
				return;
			}
			
			int index = Bounty.getBountyMenus().indexOf(event.getInventory()) + 1;
			player.closeInventory();
			player.openInventory(Bounty.getBountyMenus().get(index));
			return;
		}
		//player.sendMessage(Integer.toString(Bounty.getBountyMenus().indexOf(event.getInventory())));
		
	}
}
