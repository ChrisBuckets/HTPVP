package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class playerWarpMenuEvents implements Listener{
	@EventHandler()
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Boolean check = false;
		for(Inventory inv : playerWarps.getWarpMenus()) {
			if(event.getInventory().equals(inv)) check = true;
			break;
		}
		if(!check) return;
		if(check) event.setCancelled(true);
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getItemMeta() == null) return;
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		
		
			
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && Main.getPlugin().getConfig().contains("playerWarps." + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase()))) {
			player.sendMessage(ChatColor.GREEN + "You will be teleported in 5 seconds. Don't move.");
			if(Main.ServerPlayers.get(player.getUniqueId()).toWarping != 0) {
				player.sendMessage(ChatColor.RED + "You are already teleporting somewhere.");
				return;
			}
			
			int teleportDelay = 100;
			if(!kitItems.checkPvpRegion(player)) teleportDelay = 0;
			if(kitItems.checkPvpRegion(player)) player.sendMessage(ChatColor.GREEN + "You will be teleported in 5 seconds. Don't move.");
			Main.ServerPlayers.get(player.getUniqueId()).toWarping = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
	            public void run() {
	    			String playerWarp = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().toLowerCase());
	    			double x = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".x");
	    			double y = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".y");
	    			double z = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".z");
	    			Location playerWarpTp = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z, 180, 0);
	    			player.teleport(playerWarpTp);
	    			Main.ServerPlayers.get(player.getUniqueId()).toWarping = 0;
	    			player.closeInventory();
	            }
	          }, teleportDelay);
			return;
			
			
		}
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Close")) {
			player.closeInventory();
			return;
		}
		
		
		
		event.setCancelled(true);
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "<- Previous")) {
			if(playerWarps.getWarpMenus().indexOf(event.getInventory()) - 1 < 0) {
				player.sendMessage("This is the first page in the menu");
				return;
			}
			
			int index = playerWarps.getWarpMenus().indexOf(event.getInventory()) - 1;
			player.closeInventory();
			player.openInventory(playerWarps.getWarpMenus().get(index));
			
		}
		
		if(event.getCurrentItem().getItemMeta().getDisplayName() != null && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Next ->")) {
			if(playerWarps.getWarpMenus().indexOf(event.getInventory()) + 1 == playerWarps.getWarpMenus().size()) {
				player.sendMessage("This is the last page in the menu");
				return;
			}
			
			int index = playerWarps.getWarpMenus().indexOf(event.getInventory()) + 1;
			player.closeInventory();
			player.openInventory(playerWarps.getWarpMenus().get(index));
			
		}
		
	
		//player.sendMessage(Integer.toString(playerWarps.getWarpMenus().indexOf(event.getInventory())));
		
	}
}
