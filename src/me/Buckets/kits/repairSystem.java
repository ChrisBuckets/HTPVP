package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class repairSystem implements Listener {
	
	@EventHandler
	
	
	
	
    public void openAnvil(PlayerInteractEvent event) {
    	if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
    		
    		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) event.setCancelled(true);
    		Player player = (Player) event.getPlayer();
    		if(player.getInventory().getItemInHand().getType() == Material.AIR) {
    			player.sendMessage(ChatColor.RED + "Item must be in your hand to repair.");
    			return;
    		}
    		
    		short maxDurability = (short) (player.getInventory().getItemInHand().getType().getMaxDurability());
    	
    		if(maxDurability <= 0) {
    			player.sendMessage(ChatColor.RED + "Item can't be repaired.");
    			return;
    		}
    		
    		if(player.getInventory().getItemInHand().getDurability() == 0) {
    			player.sendMessage(ChatColor.RED + "Item isn't damaged.");
    			return;
    		}
    		
    		short getDurability = player.getInventory().getItemInHand().getDurability();
    		long price = getDurability * 2;
    		for(Enchantment enchant : player.getInventory().getItemInHand().getEnchantments().keySet()) {
    			int enchantmentLevel = player.getInventory().getItemInHand().getEnchantmentLevel(enchant);
    			price += enchantmentLevel * getDurability;
    		}
        	if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        		event.setCancelled(true);
        		player.sendMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + "Repair price: " + price + " Credits");
        		player.sendMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + "Left click the anvil to repair your item.");
        		return;
        	}
        	
        	if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
        		long playerCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
            	if(playerCredits < price) {
            		player.sendMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.RED + "Not enough credits.");
            		return;
            	}
        		player.getInventory().getItemInHand().setDurability((short) 0);
        		Economy.updateCredits(player, -price);
            	playerCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
            	Scoreboard playerBoard = player.getScoreboard();
        		playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + playerCredits);
        		player.setScoreboard(playerBoard);
        		player.sendMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.GREEN + "Item fully repaired for " + price + " Credits.");
        		return;
        	}
    	}

    }
}
