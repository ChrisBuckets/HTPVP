package me.Buckets.kits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;

public class Kits implements CommandExecutor {
	
	
	public static Inventory kitSelection;
	
	
	public static Inventory getKitSelection() {
		return kitSelection;
	}
	
	public static Set<UUID> players = new HashSet<>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("spawn")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(combatTag.checkTagged(player)) {
					player.sendMessage(ChatColor.RED + "You can't teleport while in combat.");
					return true;
				}
				System.out.println(player.getLocation());
				Location loc = new Location(Main.getPlugin().getServer().getWorld("Kit World"), 11.505743587904526, 156.0, -38.62288293331645);
				player.teleport(loc);
				if(!Kits.players.contains(player.getUniqueId())) {
					players.add(player.getUniqueId());
				}
				
				//leaderboardStatues.addJoinPacket(player);
				return true;
			}
		}
		
		if(label.equalsIgnoreCase("soup")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(Main.getPlugin().getConfig().getString("Players." + player.getUniqueId().toString() + ".Heals").equalsIgnoreCase("soup")) {
					player.sendMessage(ChatColor.RED + "You already have soup enabled.");
					return true;
				}
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Heals", "soup");
				Main.getPlugin().saveConfig();
				player.sendMessage(ChatColor.GREEN + "Switched to soup.");
				return true;
			}
		}
		
		if(label.equalsIgnoreCase("pots")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(Main.getPlugin().getConfig().getString("Players." + player.getUniqueId().toString() + ".Heals").equalsIgnoreCase("pots")) {
					player.sendMessage(ChatColor.RED + "You already have pots enabled.");
					return true;
				}
				Main.getPlugin().getConfig().set("Players." + player.getUniqueId().toString() + ".Heals", "pots");
				Main.getPlugin().saveConfig();
				player.sendMessage(ChatColor.GREEN + "Switched to pots.");
				return true;
			}
		}
		
		if(label.equalsIgnoreCase("kit")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length <= 0) {
					String kitList = ChatColor.YELLOW + "Kits: " + ChatColor.WHITE + "";
					for(String path : Main.getPlugin().getConfig().getConfigurationSection("kits").getKeys(false)) {
						path = Character.toUpperCase(path.charAt(0)) + path.substring(1);
						kitList += path + " ";
					}
					player.sendMessage(kitList);
					return true;
				}
				if(Main.getPlugin().getConfig().getStringList("kits." + args[0].toLowerCase() + ".items").size() <= 0) {
					player.sendMessage(ChatColor.RED + "Kit not found");
					return true;
				}
				

				//System.out.println(this.getConfig().getStringList("kits." + args[0] + ".items")); 
				
				List<String> kitItems = Main.getPlugin().getConfig().getStringList("kits." + args[0].toLowerCase() + ".items");
				List<String> kitArmor = Main.getPlugin().getConfig().getStringList("kits." + args[0].toLowerCase() + ".armor");
				int kitSize = kitItems.size() + kitArmor.size();
				if(player.getInventory().firstEmpty() == -1 || Kits.getEmptySlots(player) < kitSize) {
					player.sendMessage(ChatColor.RED + "You do not have enough space in your inventory.");
					return true;
				}
				for(int i = 0; i < kitItems.size(); i++) {
					String[] itemParams = kitItems.get(i).split(" ");
					giveItem(player, itemParams);
				}
				
				if(player.getInventory().getHelmet() == null && player.getInventory().getChestplate() == null && player.getInventory().getLeggings() == null && player.getInventory().getBoots() == null) {
					Kits.equipArmor(player, kitArmor);
				} else {
					for(int i = 0; i < kitArmor.size(); i++) {
						String[] itemParams = kitArmor.get(i).split(" ");
						giveItem(player, itemParams);
					}
				}

				
				giveHeals(player);
				
				
				player.sendMessage(ChatColor.GRAY + "Kit redeemed");
				return true;
				
				
			}
		}
		
		
		
		if(label.equalsIgnoreCase("kits") ) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.openInventory(kitSelection);
				return true;
			}
			
		}
		return false;
	}
	
	
	public static void createKitSelection() {
		kitSelection = Bukkit.createInventory(null,  9, ChatColor.GOLD + "Kits");
		
		int index = 0;
		for(String path : Main.getPlugin().getConfig().getConfigurationSection("kits").getKeys(false)) {
			System.out.println(index + "index" + path); 
			String [] menuItemParams = Main.getPlugin().getConfig().getString("kits." + path + ".menuItem").split(" ");
			String menuItem = menuItemParams[0];
			ItemStack item = new ItemStack(Material.matchMaterial(menuItem));
			ItemMeta meta = item.getItemMeta();
			
			String kitName = Main.getPlugin().getConfig().getString("kits." + path + ".menuName");
			System.out.println(kitName + "kit name");
			String kitColorCode = Main.getPlugin().getConfig().getString("kits." + path + ".menuColorCode");
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&" + kitColorCode + kitName));
			List<String> lore = new ArrayList<String>();
			
			String kitLore = Main.getPlugin().getConfig().getString("kits." + path + ".menuLore");
			lore.add(ChatColor.GRAY + kitLore);
			meta.setLore(lore);
			item.setItemMeta(meta);
			kitSelection.setItem(index, item);
			index++;
		}
		
		/*for (Material material : Material.values()) {
			  System.out.println(material.toString());
			}*/
		ItemStack item = new ItemStack(Material.REDSTONE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Close Menu");
		item.setItemMeta(meta);
		kitSelection.setItem(8, item);
		
	}
	
	public static void giveItem(Player player, String [] itemParams) {
		String itemName = itemParams[0];
		int itemAmount = 1;
		if(itemParams.length >= 2) itemAmount = Integer.parseInt(itemParams[1]);
		ItemStack item = new ItemStack(Material.matchMaterial(itemName), itemAmount);
		if(itemParams.length >= 3) {
			String[] enchantmentList = itemParams[2].split("/");
			System.out.println(enchantmentList.length + "length");
			for(int j = 0; j < enchantmentList.length; j++) {
				String[] enchantmentParams = enchantmentList[j].split(":");
				String enchantmentName = enchantmentParams[0];
				String enchantmentLevel = enchantmentParams[1];
				System.out.println(enchantmentName + enchantmentLevel);
				item.addEnchantment(Enchantment.getByName(enchantmentName), Integer.parseInt(enchantmentLevel));
			}
		}

		player.getInventory().addItem(item);
		
	}
	
	public static void giveHeals(Player player) {
		String healType = Main.getPlugin().getConfig().getString("Players." + player.getUniqueId().toString() + ".Heals");
		for(int slot = 0; slot < player.getInventory().getSize(); slot++){
			if(player.getInventory().getItem(slot) == null) {
				if(healType.equalsIgnoreCase("soup")) {
					player.getInventory().setItem(slot, new ItemStack(Material.matchMaterial("MUSHROOM_SOUP")));
				}
				
				if(healType.equalsIgnoreCase("pots")) {
					Potion healPot = new Potion(PotionType.INSTANT_HEAL, 2);
					healPot.setSplash(true);
					ItemStack potion = healPot.toItemStack(1);
					player.getInventory().setItem(slot, potion);
				}
			}
	        
		}
	}
	
	public static void equipArmor(Player player, List <String> kitArmor) {
		for(int i = 0; i < kitArmor.size(); i++) {
			String[] armorParams = kitArmor.get(i).split(" ");
			String itemName = armorParams[0];
			int itemAmount = 1;
			if(armorParams.length >= 2) itemAmount = Integer.parseInt(armorParams[1]);
			ItemStack item = new ItemStack(Material.matchMaterial(itemName), itemAmount);
			if(armorParams.length >= 3) {
				String[] enchantmentList = armorParams[2].split("/");
				System.out.println(enchantmentList.length + "length");
				for(int j = 0; j < enchantmentList.length; j++) {
					String[] enchantmentParams = enchantmentList[j].split(":");
					String enchantmentName = enchantmentParams[0];
					String enchantmentLevel = enchantmentParams[1];
					System.out.println(enchantmentName + enchantmentLevel);
					item.addEnchantment(Enchantment.getByName(enchantmentName), Integer.parseInt(enchantmentLevel));
				}
			}
			
			if(itemName.equalsIgnoreCase("IRON_HELMET") || itemName.equalsIgnoreCase("DIAMOND_HELMET") || itemName.equalsIgnoreCase("CHAINMAIL_HELMET")) player.getInventory().setHelmet(item);
			if(itemName.equalsIgnoreCase("IRON_CHESTPLATE") || itemName.equalsIgnoreCase("DIAMOND_CHESTPLATE") || itemName.equalsIgnoreCase("CHAINMAIL_CHESTPLATE")) player.getInventory().setChestplate(item);
			if(itemName.equalsIgnoreCase("IRON_LEGGINGS") || itemName.equalsIgnoreCase("DIAMOND_LEGGINGS") || itemName.equalsIgnoreCase("CHAINMAIL_LEGGINGS")) player.getInventory().setLeggings(item);
			if(itemName.equalsIgnoreCase("IRON_BOOTS") || itemName.equalsIgnoreCase("DIAMOND_BOOTS") || itemName.equalsIgnoreCase("CHAINMAIL_BOOTS")) player.getInventory().setBoots(item);
			

		}
		
	}
	
    public static int getEmptySlots(Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack[] cont = inventory.getContents();
        int i = 0;
        for (ItemStack item : cont)
          if (item != null && item.getType() != Material.AIR) {
            i++;
          }
        return 36 - i;
    }
}




