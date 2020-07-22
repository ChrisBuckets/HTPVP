package me.Buckets.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;


import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;

public class playerWarps implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("pwarp") || label.equalsIgnoreCase("pw")) {
			if(args.length <= 0) {
				createWarpList(player);
				//player.sendMessage(ChatColor.RED + "Usage: /pwarp [player warp name]");
				return true;
			}
			if(args[0].equalsIgnoreCase(player.getName())) {
				if(player.hasPermission("pwarp")) {
					
					if(combatTag.checkTagged(player)) {
						player.sendMessage(ChatColor.RED + "You can't teleport while in combat.");
						return true;
						
					}
					
					
					
					
					if(!Main.getPlugin().getConfig().contains("playerWarps." + player.getName().toLowerCase())) {
						if(!Main.getPlugin().getConfig().contains("Warp.coords")) {
							Main.getPlugin().getConfig().set("Warp.coords.x", -2000);
							Main.getPlugin().getConfig().set("Warp.coords.y", 80);
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
						Main.getPlugin().getConfig().set("Warp.coords.y", 80);
						Main.getPlugin().getConfig().set("Warp.coords.z", z + 100);
						Main.getPlugin().saveConfig();
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".name", player.getName());
						
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".uuid", player.getUniqueId().toString());
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".x", x - 56);
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".y", 51);
						Main.getPlugin().getConfig().set("playerWarps." + player.getName().toLowerCase() + ".z", z + 2);
						
						BlockVector min = new BlockVector(x - 91, 75, z - 58);
						BlockVector max = new BlockVector(x - 13, 36, z + 14);	
						ProtectedRegion region = new ProtectedCuboidRegion(ChatColor.stripColor(player.getDisplayName()) + "playerwarp", min, max);
						region.setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
						region.setFlag(DefaultFlag.POTION_SPLASH, StateFlag.State.ALLOW);
						region.setFlag(DefaultFlag.USE, StateFlag.State.ALLOW);
						region.setFlag(DefaultFlag.DENY_MESSAGE, "-e");
						region.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY);
						region.setFlag(DefaultFlag.FIRE_SPREAD, StateFlag.State.DENY);
						region.setFlag(DefaultFlag.LAVA_FIRE, StateFlag.State.DENY);
						region.setFlag(DefaultFlag.TNT, StateFlag.State.DENY);
						region.setFlag(DefaultFlag.GHAST_FIREBALL, StateFlag.State.DENY);
						RegionContainer container = createBase.getWorldGuard().getRegionContainer();
						RegionManager regions = container.get(player.getWorld());
						regions.addRegion(region);
						min = new BlockVector(x - 55, 75, z + 3);
						max = new BlockVector(x - 58, 50, z + 0);
						region = new ProtectedCuboidRegion(ChatColor.stripColor(player.getDisplayName()) + "playerwarpspawn", min, max);
						region.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
						regions.addRegion(region);
						regions.getRegion(ChatColor.stripColor(player.getDisplayName()) + "playerwarp").getOwners().addPlayer(player.getUniqueId());	
						regions.getRegion(ChatColor.stripColor(player.getDisplayName()) + "playerwarpspawn").getOwners().addPlayer(player.getUniqueId());
						
						Main.getPlugin().saveConfig();
						player.sendMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + "Warp created, you can go to your warp using /pwarp " + player.getName());
						return true;
					}
				}
			}
			
			String playerWarp = args[0].toLowerCase();
			if(!Main.getPlugin().getConfig().contains("playerWarps." + playerWarp)) {
				player.sendMessage(ChatColor.RED + "Player warp not found.");
				return true;
			}
			
			
			
			if(combatTag.checkTagged(player)) {
				player.sendMessage(ChatColor.RED + "You can't teleport while in combat.");
				return true;
			}
			double x = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".x");
			double y = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".y");
			double z = Main.getPlugin().getConfig().getDouble("playerWarps." + playerWarp + ".z");
			Location playerWarpTp = new Location(Main.getPlugin().getServer().getWorld("Kit World"), x, y, z, 180, 0);
			
			player.teleport(playerWarpTp);

			return true;
		}
		
		return false;
	}
	
	public static List <Inventory> warpInventories = new ArrayList<>();
	public static List<Inventory> getWarpMenus() {
		return warpInventories;
	}
	public static void createWarpList(Player playerOpening) {
		
		warpInventories = new ArrayList<>();
		List <String> playerWarps = new ArrayList<String>();
		if(Main.getPlugin().getConfig().getConfigurationSection("playerWarps") == null) {
			playerOpening.sendMessage(ChatColor.RED + "There are no player warps available.");
			return;
		}
		for(String path : Main.getPlugin().getConfig().getConfigurationSection("playerWarps").getKeys(false)) {
			playerWarps.add(path);
		}
		int list = ((int) Math.ceil(1 / 36.0));
		
		int amount = playerWarps.size();
		
		int index = 0;
		int getIndex = index;
		System.out.println(list + " " + playerWarps.size());
		for (int i = 0; i < list; i++) {
			Inventory warpMenu = Bukkit.createInventory(null,  45, ChatColor.BLUE + "Player Warps");
			
			warpInventories.add(warpMenu);
			System.out.println(warpInventories);
			for(int j = 0; j < amount - getIndex; j++) {
				if(j > amount) break;
				String uuid = Main.getPlugin().getConfig().getString("playerWarps." + playerWarps.get(index) + ".uuid");
				UUID playerUUID = UUID.fromString(uuid);
				Player player = (Player) Bukkit.getPlayer(playerUUID);
				
				if(player == null) {
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
					ItemStack skull = new ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
					
					SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
					skullMeta.setOwner(offlinePlayer.getName());
					System.out.println(skullMeta);
					skullMeta.setDisplayName(ChatColor.BLUE + offlinePlayer.getName());
					List<String> lore = new ArrayList<String>();
					//String name = Main.getPlugin().getConfig().getString("playerWarps." + playerWarps.get(index) + ".name");
					//lore.add(ChatColor.BLUE + name);
					skullMeta.setLore(lore);
					skull.setItemMeta(skullMeta);
					warpMenu.addItem(skull);
					index++;
					continue;
				}
				ItemStack skull = new ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
				
				SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
				skullMeta.setOwner(player.getDisplayName());
				skullMeta.setDisplayName(ChatColor.BLUE + player.getName());
				List<String> lore = new ArrayList<String>();
				//String name = Main.getPlugin().getConfig().getString("playerWarps." + playerWarps.get(index) + ".name");
				//lore.add(ChatColor.BLUE + name);
				skullMeta.setLore(lore);
				skull.setItemMeta(skullMeta);
				
				warpMenu.addItem(skull);
				index++;
			}
			ItemStack previous = new ItemStack(Material.REDSTONE_BLOCK, 1);
			ItemMeta previousMeta = previous.getItemMeta();
			previousMeta.setDisplayName(ChatColor.RED + "<- Previous");
			previous.setItemMeta(previousMeta);
			
			ItemStack close = new ItemStack(Material.matchMaterial("BARRIER"), 1);
			ItemMeta closeMeta = close.getItemMeta();
			closeMeta.setDisplayName(ChatColor.DARK_RED + "Close");
			close.setItemMeta(closeMeta);
			
			ItemStack next = new ItemStack(Material.EMERALD_BLOCK, 1);
			ItemMeta nextMeta = next.getItemMeta();
			nextMeta.setDisplayName(ChatColor.GREEN + "Next ->");
			next.setItemMeta(nextMeta);
			
			warpMenu.setItem(36, previous);
			warpMenu.setItem(40, close);
			warpMenu.setItem(44, next);
			
			getIndex = index;
			
		}
		
		playerOpening.openInventory(warpInventories.get(0));
		
		System.out.println(warpInventories.size() + " size");
	}
}
