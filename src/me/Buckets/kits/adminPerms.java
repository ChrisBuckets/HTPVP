package me.Buckets.kits;

import java.util.Arrays;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class adminPerms implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("announce")) {
			if(!player.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			Bukkit.broadcastMessage(ChatColor.RED + "[Announcement] " + ChatColor.WHITE + String.join(" ", args));
			return true;
		}
		
		
		
		if(label.equalsIgnoreCase("kick")) {
			String reason = "";
			
			if(!player.hasPermission("group.mod")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return false;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /kick [player] [reason]");
				return true;
			}
			if(args.length >= 2) {
				String [] message = Arrays.copyOfRange(args, 1, args.length);
				reason = String.join(" ", message);
			}
			Player playerToKick = (Player) Bukkit.getPlayer(args[0]);
			if(playerToKick == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			};
			
			if(args[0].equalsIgnoreCase("FishFr0g") || args[0].equalsIgnoreCase("Scuba_S7eve")) {
				player.sendMessage(ChatColor.RED + "THAT'S A SERVER OWNER BRUH WTF");
			}
			if(playerToKick.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.RED + "That player is an admin..");
				playerToKick.sendMessage(player.getDisplayName() + " tried to kick you.");
				return true;
			}
			System.out.println(reason);
			playerToKick.kickPlayer(ChatColor.RED + "You were kicked by " + player.getName() + " for: " + reason);
			Bukkit.broadcastMessage(ChatColor.RED + ChatColor.stripColor(player.getDisplayName()) + ChatColor.RED + " has kicked " + playerToKick.getDisplayName() + ChatColor.RED + " for reason: " + ChatColor.RED + reason);
			return true;
		}
		
		if(label.equalsIgnoreCase("mute")) {
			if(!player.hasPermission("group.helper")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /mute [player]");
				return true;
			}
			
			Player playerToMute = (Player) Bukkit.getPlayer(args[0]);
			if(playerToMute == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			
			Boolean toggle = Main.getPlugin().getConfig().getBoolean("Players." + playerToMute.getUniqueId().toString() + ".Muted");
			toggle = !toggle;
			Main.getPlugin().getConfig().set("Players." + playerToMute.getPlayer().getUniqueId().toString() + ".Muted", toggle);
			String checkToggle = "";
			if(toggle) checkToggle = " muted ";
			if(!toggle) checkToggle = " unmuted ";
			Bukkit.broadcastMessage(ChatColor.RED + ChatColor.stripColor(player.getDisplayName() + checkToggle + ChatColor.stripColor(playerToMute.getDisplayName())));
			return true;
		}
		
		if(label.equalsIgnoreCase("ban")) {
			String reason = "";
			if(!player.hasPermission("group.mod")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /ban [player] [reason]");
				return true;
			}
			if(args.length >= 2) {
				String [] message = Arrays.copyOfRange(args, 1, args.length);
				reason = String.join(" ", message);
			}
			Player playerToBan = (Player) Bukkit.getPlayer(args[0]);
			if(playerToBan == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			};
			
			if(args[0].equalsIgnoreCase("FishFr0g") || args[0].equalsIgnoreCase("Scuba_S7eve")) {
				player.sendMessage(ChatColor.RED + "THAT'S A SERVER OWNER BRUH WTF");
			}
			if(playerToBan.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.RED + "That player is an admin..");
				playerToBan.sendMessage(player.getDisplayName() + " tried to ban you.");
				return true;
			}
			System.out.println(reason);
			String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);
			Bukkit.getBanList(BanList.Type.NAME).addBan(ChatColor.stripColor(playerToBan.getDisplayName()), bumper + player.getDisplayName() + " banned you. Reason: " + reason + bumper, null, null);
			playerToBan.kickPlayer(reason);
			Main.getPlugin().getConfig().set("Players." + playerToBan.getPlayer().getUniqueId().toString() + ".Banned", "Banned by: " + player.getDisplayName() + " for " + reason);
			Main.getPlugin().saveDefaultConfig();
			Bukkit.broadcastMessage(ChatColor.RED + ChatColor.stripColor(player.getDisplayName()) + ChatColor.RED + " has banned " + playerToBan.getDisplayName() + ChatColor.RED + " for reason: " + ChatColor.RED + reason);
			return true;
		}
		
		if(label.equalsIgnoreCase("unban")) {
			if(!player.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /unban [player]");
				return true;
			}
			
			OfflinePlayer playerToUnban = (OfflinePlayer) Bukkit.getOfflinePlayer(args[0]);
			if(!playerToUnban.isBanned()) {
				player.sendMessage(ChatColor.RED + "Player is not banned.");
				return true;
			}
			
			Bukkit.getBanList(BanList.Type.NAME).pardon(playerToUnban.getName());
			player.sendMessage(ChatColor.RED + playerToUnban.getName() + " has been unbanned.");
			return true;
		}
		
		if(label.equalsIgnoreCase("staff") || label.equalsIgnoreCase("s")) {
			if(!player.hasPermission("kitStaff.chat")) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /staff [msg]");
				return true;
			}
			for (Player online : Bukkit.getOnlinePlayers()) {
				if(online.hasPermission("kitStaff.chat")) {
					String name = player.getDisplayName();
					if(player.hasPermission("group.helper")) name = ChatColor.YELLOW + player.getDisplayName();
					if(player.hasPermission("group.mod")) name = ChatColor.DARK_PURPLE + player.getDisplayName();
					if(player.hasPermission("group.admin")) name = ChatColor.RED + player.getDisplayName();
					if(player.hasPermission("group.owner")) name = ChatColor.DARK_RED + player.getDisplayName();
					online.sendMessage(ChatColor.YELLOW + "[Staff Chat] " + name + ": " + ChatColor.WHITE + String.join(" ", args));
				}
			}
			return true;

		}
		
		
		
		
		if(label.equalsIgnoreCase("report")) {
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /report [message]");
				return true;
			}
			for (Player online : Bukkit.getOnlinePlayers()) {
				if(online.hasPermission("group.helper")) {
					online.sendMessage(ChatColor.RED + "[Report] " + ChatColor.GRAY + ChatColor.stripColor(player.getDisplayName()) + ": " + ChatColor.WHITE + String.join(" ", args));
				}
			}
			
			
			player.sendMessage(ChatColor.GREEN + "Your report has been sent to the staff team.");
		}
		
		if(label.equalsIgnoreCase("tp")) {
			if(!player.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /tp [player]");
				return true;
			}
			
			Player teleportPlayer = (Player) Bukkit.getPlayer(args[0]);
			if(teleportPlayer == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				
				return true;
			};
			
			player.sendMessage(ChatColor.GRAY + "Teleporting..");
			player.teleport(teleportPlayer.getLocation());
		}
		
		if(label.equalsIgnoreCase("tphere")) {
			if(!player.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /tphere [player]");
				return true;
			}
			
			Player teleportPlayer = (Player) Bukkit.getPlayer(args[0]);
			if(teleportPlayer == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			};
			
			player.sendMessage(ChatColor.GRAY + "Teleporting..");
			teleportPlayer.teleport(player.getLocation());
		}
		
		if(label.equalsIgnoreCase("ci")) {
			if(args.length <= 0) {
				player.getInventory().clear();
				player.sendMessage(ChatColor.GRAY + "Inventory cleared");
				return true;
			}
			
			if(!player.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have permission.");
				return true;
			}
			Player getInventory = (Player) Bukkit.getPlayer(args[0]);
			if(getInventory == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			getInventory.getInventory().clear();
			player.sendMessage(ChatColor.GRAY + "Inventory cleared");
			return true;
			
		}
		
		if(label.equalsIgnoreCase("invis")) {
			
			if(!player.hasPermission("group.admin")) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /invis [on/off]");
				return true;
			}
			if(args[0].equalsIgnoreCase("on") ) {
				for (Player online : Bukkit.getOnlinePlayers()) {
					if(online.hasPermission("kitStaff.admin")) {
						continue;
					}
					online.hidePlayer(player);
				}
				player.sendMessage(ChatColor.AQUA + "You are now invisible.");
				return true;
			} else if(args[0].equalsIgnoreCase("off")) {
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.showPlayer(player);
				}
				player.sendMessage(ChatColor.AQUA + "You are no longer invisible.");
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "Usage: /invis [on/off]");
				return true;
			}
			


		}
		
		if(label.equalsIgnoreCase("warp")) {
			if(combatTag.checkTagged(player)) {
				player.sendMessage(ChatColor.RED + "You can't teleport while in combat.");
				return true;
			}
			if(args.length <= 0) {
				String warpList = ChatColor.WHITE + "";
				for(String path : Main.getPlugin().getConfig().getConfigurationSection("Warps").getKeys(false)) {
					path = Character.toUpperCase(path.charAt(0)) + path.substring(1);
					warpList += path + " ";
				}
				player.sendMessage(ChatColor.YELLOW + "Warps: " + warpList);
				return true;
			}
			String name = args[0].toLowerCase();
			if(!Main.getPlugin().getConfig().contains("Warps." + name)) {
				player.sendMessage(ChatColor.RED + "Warp not found.");
				return true;
			}
			double x = Main.getPlugin().getConfig().getDouble("Warps." + name + ".x");
			double y = Main.getPlugin().getConfig().getDouble("Warps." + name + ".y");
			double z = Main.getPlugin().getConfig().getDouble("Warps." + name + ".z");
			float yaw = (float) Main.getPlugin().getConfig().getDouble("Warps." + name + ".yaw");
			float pitch = (float) Main.getPlugin().getConfig().getDouble("Warps." + name + ".pitch");
			Location loc = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z, yaw, pitch);
			player.teleport(loc);
		}
		
		if(label.equalsIgnoreCase("setwarp")) {
			if(!player.hasPermission("group.owner")) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /setwarp [warp name]");
				return true;
			}
			
			String name = args[0].toLowerCase();
			
			if(Main.getPlugin().getConfig().contains("Warps." + name)) {
				player.sendMessage(ChatColor.RED + "Warp already exists.");
				return true;
			}
			
			double x = player.getLocation().getX();
			double y = player.getLocation().getY();
			double z = player.getLocation().getZ();
			double yaw = player.getLocation().getYaw();
			double pitch = player.getLocation().getPitch();
			Main.getPlugin().getConfig().set("Warps." + name + ".x", x);
			Main.getPlugin().getConfig().set("Warps." + name + ".y", y);
			Main.getPlugin().getConfig().set("Warps." + name + ".z", z);
			Main.getPlugin().getConfig().set("Warps." + name + ".yaw", yaw);
			Main.getPlugin().getConfig().set("Warps." + name + ".pitch", pitch);
			
			Main.getPlugin().saveConfig();
			player.sendMessage(ChatColor.GREEN + "Warp '" + name + "' set, use /warp " + name + " to get to it.");
		}
		
		if(label.equalsIgnoreCase("deletewarp")) {
			if(!player.hasPermission("group.owner")) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /deletewarp [warp name]");
				return true;
			}
			
			String name = args[0].toLowerCase();
			
			if(Main.getPlugin().getConfig().contains("Warps." + name)) {
				Main.getPlugin().getConfig().set("Warps." + name, null);
				Main.getPlugin().saveConfig();
				player.sendMessage(ChatColor.GREEN + "Warp deleted.");
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "Warp not found.");
				return true;
			}
	
		}
		
		if(label.equalsIgnoreCase("shop")) {
			if(combatTag.checkTagged(player)) {
				player.sendMessage(ChatColor.RED + "You can't teleport while in combat.");
				return true;
			}
			double x = Main.getPlugin().getConfig().getDouble("Warps." + "shop" + ".x");
			double y = Main.getPlugin().getConfig().getDouble("Warps." + "shop" + ".y");
			double z = Main.getPlugin().getConfig().getDouble("Warps." + "shop" + ".z");
			float yaw = (float) Main.getPlugin().getConfig().getDouble("Warps." + "shop" + ".yaw");
			float pitch = (float) Main.getPlugin().getConfig().getDouble("Warps." + "shop" + ".pitch");
			Location loc = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z, yaw, pitch);
	        player.teleport(loc);
		}
		return false;
		
		
		
		
	}
}
