package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class combatTag {
	public static void tagPlayer(Player player) {
		if(!Main.getPlugin().getConfig().contains("Players." + player.getUniqueId() + ".combatTagged")) {
			Main.getPlugin().getConfig().set("Players" + player.getUniqueId() + ".combatTagged", true);
	        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
	            public void run() {
	            	player.sendMessage(ChatColor.GRAY + "You are no longer in combat. You can safely log out or teleport.");
	            	Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".combatTagged", false);
	            }
	          }, 300);
		}
		Boolean playerTagged = Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId() + ".combatTagged");
		if(playerTagged) return;
		Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".combatTagged", true);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	player.sendMessage(ChatColor.GRAY + "You are no longer in combat. You can safely log out or teleport.");
            	Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".combatTagged", false);
            }
          }, 300);

	}
}

