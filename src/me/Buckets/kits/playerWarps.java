package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class playerWarps implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("pwarp")) {
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /pwarp [player warp name]");
				return true;
			}
			if(args[0].equalsIgnoreCase(player.getName())) {
				if(player.hasPermission("pwarp")) {
					if(!Main.getPlugin().getConfig().contains("playerWarps." + player.getName().toLowerCase())) {
						if(!Main.getPlugin().getConfig().contains("Warp.coords")) {
							Main.getPlugin().getConfig().set("Warp.coords.x", -2000);
							Main.getPlugin().getConfig().set("Warp.coords.y", 40);
							Main.getPlugin().getConfig().set("Warp.coords.z", 2000); 
							Main.getPlugin().saveConfig();
							System.out.println("Saved");
						}
						
						
						
						int x = Main.getPlugin().getConfig().getInt("Warp.coords.x");
						int y = Main.getPlugin().getConfig().getInt("Warp.coords.y");
						int z = Main.getPlugin().getConfig().getInt("Warp.coords.z");
						Location location = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
						
						
						baseSystem.loadSchematic(player, location, "playerwarp");
						System.out.println(player.getLocation());
						Main.getPlugin().getConfig().set("Warp.coords.x", x);
						Main.getPlugin().getConfig().set("Warp.coords.y", 40);
						Main.getPlugin().getConfig().set("Warp.coords.z", z + 100);
						Main.getPlugin().saveConfig();
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".name", player.getName());
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".x", x - 3	);
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".y", 51);
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".z", z - 7.5);
						
						
						Main.getPlugin().saveConfig();
						player.sendMessage("Warp created");
						return true;
					}
				}
			}
			
			String playerWarp = args[0].toLowerCase();
			if(!Main.getPlugin().getConfig().contains("playerWarps." + playerWarp)) {
				player.sendMessage(ChatColor.RED + "Player warp not found.");
				return true;
			}
			
			double x = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".x");
			double y = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".y");
			double z = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".z");
			player.sendMessage(Double.toString(x));
			player.sendMessage(Float.toString(player.getLocation().getPitch()));
			Location playerWarpTp = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z, 180, 0);
			player.teleport(playerWarpTp);

			return true;
		}
		return false;
	}
}
