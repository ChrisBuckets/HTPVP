package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Messages implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("whisper") || label.equalsIgnoreCase("w") || label.equalsIgnoreCase("msg") 
				|| label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("pm")) {
			
			Boolean checkMuted = Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId().toString() + ".Muted");
			if(checkMuted) {
				player.sendMessage(ChatColor.RED + "You are muted, make a /report or use our discord to appeal.");
				return true;
			}
			if(args.length <= 1) {
				player.sendMessage(ChatColor.RED + "Usage: /pm [player] [msg]");
				return true;
			}
			
			
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			
			String message = "";
		
			for(int i = 0; i < args.length - 1; i++) {
				message += " " + args[i + 1];
			}
			
			target.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.GREEN + " -> " + ChatColor.GRAY + target.getName() + ":" + message);
			player.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.GREEN + " -> " + ChatColor.GRAY + target.getName() + ":" + message);
			Main.ServerPlayers.get(player.getUniqueId()).reply = target;
			Main.ServerPlayers.get(target.getUniqueId()).reply = player;
			sendMessageToStaff(ChatColor.GRAY + player.getName() + ChatColor.GREEN + " -> " + ChatColor.GRAY + target.getName() + ":" + message);
		}
		
		
		
		
		
		
		
		if(label.equalsIgnoreCase("reply") || label.equalsIgnoreCase("r")) {
			Player target = Bukkit.getPlayer(Main.ServerPlayers.get(player.getUniqueId()).reply.getName());
			Boolean checkMuted = Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId().toString() + ".Muted");
			if(checkMuted) {
				player.sendMessage(ChatColor.RED + "You are muted, make a /report or use our discord to appeal.");
				return true;
			}
			if(target == null) {
				player.sendMessage(ChatColor.RED + "You have no one to reply to.");
				return true;
			}
			
			String message = "";
			
			for(int i = 0; i < args.length; i++) {
				message += " " + args[i];
			}
			
			target.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.GREEN + " -> " + ChatColor.GRAY + target.getName() + ":" + message);
			player.sendMessage(ChatColor.GRAY + player.getName() + ChatColor.GREEN + " -> " + ChatColor.GRAY + target.getName() + ":" + message);
			Main.ServerPlayers.get(player.getUniqueId()).reply = target;
			Main.ServerPlayers.get(target.getUniqueId()).reply = player;
			sendMessageToStaff(ChatColor.GRAY + player.getName() + ChatColor.GREEN + " -> " + ChatColor.GRAY + target.getName() + ":" + message);
		}
		return false;
		
	
		
		
	}
	
	
	public static void sendMessageToStaff(String message) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			if(online.hasPermission("group.admin")) {
				online.sendMessage(ChatColor.RED + "[PLAYER MSG] " + message);
			}
		}
	}
}


	

	
	

