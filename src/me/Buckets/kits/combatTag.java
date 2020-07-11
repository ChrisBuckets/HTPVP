package me.Buckets.kits;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class combatTag {
	public static HashMap<Player, Integer> playerTags = new HashMap<Player, Integer>();
	//public static Set<UUID> combatTagged = new HashSet<>();	
	//Make it into players go into a set instead of saving to a config
	public static void tagPlayer(Player player) {
		/*if(!Main.getPlugin().getConfig().contains("Players." + player.getUniqueId() + ".combatTagged")) {
			Main.getPlugin().getConfig().set("Players" + player.getUniqueId() + ".combatTagged", true);
	        int playerTag = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
	        	
	            public void run() {
	            	player.sendMessage(ChatColor.GRAY + "You are no longer in combat and can safely log out.");
	            }
	          }, 300);
	        playerTags.put(player, playerTag);
	        
	        
	        
	        return;
		}*/
		
		if(playerTags.containsKey(player)) {
			Bukkit.getServer().getScheduler().cancelTask(playerTags.get(player));
	        int playerTag = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
	        	
	            public void run() {
	            	player.sendMessage(ChatColor.GRAY + "You are no longer in combat and can safely log out.");
	            	playerTags.remove(player);
	            }
	          }, 300);
	        playerTags.put(player, playerTag);
			return;
		}
		
		int playerTag = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	player.sendMessage(ChatColor.GRAY + "You are no longer in combat and can safely log out.");
            	playerTags.remove(player);
            }
          }, 300);


		playerTags.put(player, playerTag);

	}
	
	
	public static Boolean checkTagged(Player player) {
		if(playerTags.containsKey(player)) return true;
		return false;
	}
}

