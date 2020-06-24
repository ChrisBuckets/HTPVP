package me.Buckets.kits;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class createBase implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("buybase")) {
			if(Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId().toString() + ".Base.preset.owned")) {
				player.sendMessage(ChatColor.RED + "You already have a base, use /base or /home to warp to it");
				return true;
			}
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /buybase [number]");
				return true;
			}
			if(!args[0].equalsIgnoreCase("1") && !args[0].equalsIgnoreCase("2") && !args[0].equalsIgnoreCase("3") && !args[0].equalsIgnoreCase("4")) {
				player.sendMessage(ChatColor.RED + "Invalid base to purchase.");
				return true;
			}
			if(!Main.getPlugin().getConfig().contains("Base.coords")) {
				Location loc = new Location(Main.getPlugin().getServer().getWorld("Kit World"), 2000, 40, 2000);
				Main.getPlugin().getConfig().set("Base.coords.x", 2000);
				Main.getPlugin().getConfig().set("Base.coords.y", 40);
				Main.getPlugin().getConfig().set("Base.coords.z", 2000); 
				Main.getPlugin().saveConfig();
				System.out.println("Saved");
			}
			int x = Main.getPlugin().getConfig().getInt("Base.coords.x");
			int y = Main.getPlugin().getConfig().getInt("Base.coords.y");
			int z = Main.getPlugin().getConfig().getInt("Base.coords.z");
			Location location = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
			if(args[0].equalsIgnoreCase("1")) {
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.x", x + 6.5);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.y", y + 2);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.z", z + 8);
			}
			if(args[0].equalsIgnoreCase("2")) {
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.x", x + 7.57);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.y", y + 2);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.z", z + 7);
			}
			if(args[0].equalsIgnoreCase("3")) {
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.x", x + 7.45);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.y", y + 2);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.z", z + 8.35);
			}
			if(args[0].equalsIgnoreCase("4")) {
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.x", x + 6.5);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.y", y + 2);
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.z", z + 8.5);
			}
			
			String base = args[0];
			baseSystem.loadSchematic(player, location, base);
			System.out.println(player.getLocation());
			Main.getPlugin().getConfig().set("Base.coords.x", x + 25);
			Main.getPlugin().getConfig().set("Base.coords.y", 40);
			Main.getPlugin().getConfig().set("Base.coords.z", z + 25);
			Main.getPlugin().saveConfig();
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.owned", true);
			Main.getPlugin().saveConfig();
			player.sendMessage(ChatColor.GREEN + "Base purchased, you can warp to it by using /base or /home");
		}
		
		if(label.equalsIgnoreCase("base") || label.equalsIgnoreCase("home")) {
			if(!Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId().toString() + ".Base.preset.owned")) {
				player.sendMessage(ChatColor.RED + "You do not have a base, go to /warp shop to purchase one.");
				return true;
			}
			
			int x = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.preset.x");
			int y = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.preset.y");
			int z = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.preset.z");
			
			
			System.out.println(x + " " + y + " " + z);
			Location base = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
			player.teleport(base);
			return true;
		}
		return false;
	}
}
