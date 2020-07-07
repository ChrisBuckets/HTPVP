package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class Economy implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("money") || label.equalsIgnoreCase("balance") || label.equalsIgnoreCase("credits")) {
			long credits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
			player.sendMessage(ChatColor.GOLD + "Credits: " + ChatColor.WHITE + credits);
		}
		
		if(label.equalsIgnoreCase("givecredits")) {
			if(!player.hasPermission("group.owner")) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			
			
			if(args.length <= 1) {
				player.sendMessage(ChatColor.RED + "Usage: /givecredits [player] [amount]");
				return true;
			}
			
			
			
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			
			
		    try {
		        Integer.parseInt(args[1]);
		        Economy.updateCredits(target, Integer.parseInt(args[1]));
		        Scoreboard targetBoard = target.getScoreboard();
		        long updatedCredits = Main.getPlugin().getConfig().getLong("Players." + target.getUniqueId() + ".credits");
				targetBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + updatedCredits);
				target.setScoreboard(targetBoard);
				player.sendMessage(ChatColor.GREEN + "You gave " + args[1] + " credits to " + ChatColor.stripColor(target.getDisplayName()));
				target.sendMessage(ChatColor.GREEN + "You received " + args[1] + " credits from " + player.getDisplayName());
		        return true;
		    } catch (final NumberFormatException e) {
		    	player.sendMessage(ChatColor.RED + "Not a number.");
		        return true;
		    }
			
			
		}
		
		if(label.equalsIgnoreCase("pay")) {
			if(args.length <= 1) {
				player.sendMessage(ChatColor.RED + "Usage: /pay [player] [amount]");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			
		    try {
		    	long amount = Long.parseLong(args[1]);
		    	if(amount <=0) {
		    		player.sendMessage(ChatColor.RED + "Invalid number.");
		    		return true;
		    	}
			    if(!checkPlayerMoney(player, amount)) {
			    	player.sendMessage(ChatColor.RED + "Not enough credits.");
			    	return true;
			    }
			    updateCredits(player, -amount);
			    updateCredits(target, amount);
			    
			    Scoreboard playerBoard = player.getScoreboard();
			    Scoreboard targetBoard = target.getScoreboard();
			    long playerCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
			    long targetCredits = Main.getPlugin().getConfig().getLong("Players." + target.getUniqueId() + ".credits");
			    playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + playerCredits);
				targetBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + targetCredits);
				player.setScoreboard(playerBoard);
				target.setScoreboard(targetBoard);
				player.sendMessage(ChatColor.GREEN + "You gave " + args[1] + " credits to " + ChatColor.stripColor(target.getDisplayName()));
				target.sendMessage(ChatColor.GREEN + "You received " + args[1] + " credits from " + player.getDisplayName());
		    } catch(Exception e) {
		    	player.sendMessage(ChatColor.RED + "Not a number.");
		        return true;
		    }
		    

		    

			
		}
		return false;
	}
	
	public static void updateCredits(Player player, long amount) {
		long credits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
		System.out.println(amount);
        Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".credits", credits + amount);
        Main.getPlugin().saveConfig();
        leaderboardStatues.checkCredits(player);
	}
	
	public static void updateOfflinePlayerCredits(OfflinePlayer player, long amount) {
		long credits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
        Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".credits", credits + amount);
        Main.getPlugin().saveConfig();
	}
	
	public static Boolean checkPlayerMoney(Player player, long price) {
		long credits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
		if(credits < price) return false;
		return true;
	}
}
