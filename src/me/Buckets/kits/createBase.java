package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;

public class createBase implements CommandExecutor {

	
	
	public static WorldGuardPlugin getWorldGuard() {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if(p instanceof WorldGuardPlugin) return (WorldGuardPlugin) p;
		else return null;
	}
	
	
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
			
	    	long playerCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
	    	if(playerCredits < 25000) {
	    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Not enough credits.");
	    		return true;
	    	}
			if(!Main.getPlugin().getConfig().contains("Base.coords")) {
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
			Main.getPlugin().getConfig().set("Base.coords.z", z);
			Main.getPlugin().saveConfig();
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.preset.owned", true);
			Main.getPlugin().saveConfig();
			Economy.updateCredits(player, -25000);
			playerCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
	    	Scoreboard playerBoard = player.getScoreboard();
			playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + playerCredits);
			player.setScoreboard(playerBoard);
			player.sendMessage(ChatColor.GREEN + "Base purchased, you can warp to it by using /base or /home");
			
		}
		
		if(label.equalsIgnoreCase("base") || label.equalsIgnoreCase("home")) {
			if(combatTag.checkTagged(player)) {
				player.sendMessage(ChatColor.RED + "You can't teleport while in combat.");
				return true;
			}
			if(args.length <= 0 && !Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId().toString() + ".Base.preset.owned")) {
				player.sendMessage(ChatColor.RED + "You do not have a base from the shop, use /base mvp or /base alpha if you own a donator base.");
				return true;
			}
			
			if(args.length > 0 && args[0].equalsIgnoreCase("mvp")) {
				if(!player.hasPermission("mvpbase") && !player.hasPermission("group.mvp")) {
					player.sendMessage(""); //tell them they don't have a base, give them link to buycraft store
					return true;
				}
				
				if(!Main.getPlugin().getConfig().contains("Players." + player.getUniqueId() + ".Base.mvp")) {
					int x = Main.getPlugin().getConfig().getInt("Base.coords.x");
					int y = Main.getPlugin().getConfig().getInt("Base.coords.y");
					int z = Main.getPlugin().getConfig().getInt("Base.coords.z");
					//x + 0 y = 38 z +7
					
					Location location = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
					String base = "10x10base";
					baseSystem.loadSchematic(player, location, base);
					System.out.println(player.getLocation());			
					
					BlockVector min = new BlockVector(x - 1, 32, z - 13);
					BlockVector max = new BlockVector(x + 12, 26, z + 0);	
					ProtectedRegion region = new ProtectedCuboidRegion(ChatColor.stripColor(player.getDisplayName()) + "mvpbase", min, max);
					RegionContainer container = getWorldGuard().getRegionContainer();
					RegionManager regions = container.get(player.getWorld());
					regions.addRegion(region);
					regions.getRegion(ChatColor.stripColor(player.getDisplayName()) + "mvpbase").getOwners().addPlayer(player.getUniqueId());	
					
					Main.getPlugin().getConfig().set("Base.coords.x", x + 25);
					Main.getPlugin().getConfig().set("Base.coords.y", 40);
					Main.getPlugin().getConfig().set("Base.coords.z", z);
					Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.mvp.x", x + 6);
					Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.mvp.y", y - 13);
					Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.mvp.z", z - 6);
					Main.getPlugin().saveConfig();
					
					
					player.sendMessage(ChatColor.GREEN + "MVP base created, you can warp to it using /home mvp or /base mvp");
					return true;
				}
				
				
				int x = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.mvp.x");
				int y = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.mvp.y");
				int z = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.mvp.z");
				
				
				System.out.println(x + " " + y + " " + z);
				Location base = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
				player.teleport(base);
				return true;
			}
			
			
			
			
			
			
			
			
			
			
			
			
			if(args.length > 0 && args[0].equalsIgnoreCase("alpha")) {
				if(!player.hasPermission("alphabase") || !player.hasPermission("group.alpha")) {
					player.sendMessage(""); //tell them they don't have a base, give them link to buycraft store
					return true;
				}
				
				if(!Main.getPlugin().getConfig().contains("Players." + player.getUniqueId() + ".Base.alpha")) {
					int x = Main.getPlugin().getConfig().getInt("Base.coords.x");
					int y = Main.getPlugin().getConfig().getInt("Base.coords.y");
					int z = Main.getPlugin().getConfig().getInt("Base.coords.z");
					//x + 0 y = 38 z +7
					
					Location location = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
					String base = "25x25base";
					baseSystem.loadSchematic(player, location, base);
					System.out.println(player.getLocation());			
					
					BlockVector min = new BlockVector(x + 4, 43, z + 4);
					BlockVector max = new BlockVector(x + 32, 35, z + 33);	
					ProtectedRegion region = new ProtectedCuboidRegion(ChatColor.stripColor(player.getDisplayName()) + "alphabase", min, max);
					RegionContainer container = getWorldGuard().getRegionContainer();
					RegionManager regions = container.get(player.getWorld());
					regions.addRegion(region);
					regions.getRegion(ChatColor.stripColor(player.getDisplayName()) + "alphabase").getOwners().addPlayer(player.getUniqueId());

					
					Main.getPlugin().getConfig().set("Base.coords.x", x + 50);
					Main.getPlugin().getConfig().set("Base.coords.y", 40);
					Main.getPlugin().getConfig().set("Base.coords.z", z);
					Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.alpha.x", x + 18.5);
					Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.alpha.y", y - 3);
					Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Base.alpha.z", z + 18.5);
					Main.getPlugin().saveConfig();
					
					
					player.sendMessage(ChatColor.GREEN + "ALPHA base created, you can warp to it using /home alpha or /base alpha");
					return true;
				}
				
				
				int x = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.alpha.x");
				int y = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.alpha.y");
				int z = Main.getPlugin().getConfig().getInt("Players." + player.getUniqueId().toString() + ".Base.alpha.z");
				
				
				System.out.println(x + " " + y + " " + z);
				Location base = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z);
				player.teleport(base);
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
