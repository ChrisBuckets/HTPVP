package me.Buckets.kits;

import org.bukkit.Bukkit;
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
		        return false;
		    }
			
			
		}
		return false;
	}
	
	public static void updateCredits(Player player, long amount) {
		long credits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
        Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".credits", credits + amount);
        Main.getPlugin().saveConfig();
	}
}
