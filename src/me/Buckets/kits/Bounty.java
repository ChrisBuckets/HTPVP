package me.Buckets.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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

import net.md_5.bungee.api.ChatColor;

public class Bounty implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("hit")) {
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /hit [player] [price]");
				return true;
			}
			
			String price = args[1];
			
			if(Long.parseLong(price) < 1000) {
				player.sendMessage(ChatColor.RED + "Minimum hit price is 1000 Credits.");
				return true;
			}
			
			
			Player target = (Player) Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(ChatColor.RED + "Player not found.");
				return true;
			}
			
			if(Main.getPlugin().getConfig().contains("Bounties." + target.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "That player already has a hit on them.");
				return true;
			}
			
			Main.getPlugin().getConfig().set("Bounties." + target.getUniqueId() + ".price", price);
			Main.getPlugin().getConfig().set("Bounties." + target.getUniqueId() + ".customer", player.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			Bukkit.broadcastMessage(ChatColor.AQUA + "[HTPVP] " + ChatColor.LIGHT_PURPLE + ChatColor.stripColor(player.getDisplayName()) +
					" placed a hit on " + ChatColor.stripColor(target.getDisplayName()) + " for " + price + " credits.");
			player.sendMessage(ChatColor.GREEN + "Hit placed.");
		}
		

		if(label.equalsIgnoreCase("hits")) {
			Bounty.createBountyList(player);
			
			return true;
		}
		return false;
	}
	
	
	
	
	public static List <Inventory> bountyInventories = new ArrayList<>();
	public static List<Inventory> getBountyMenus() {
		return bountyInventories;
	}
	public static void createBountyList(Player playerOpening) {
		
		bountyInventories = new ArrayList<>();
		List <String> Bounties = new ArrayList<String>();
		for(String path : Main.getPlugin().getConfig().getConfigurationSection("Bounties").getKeys(false)) {
			Bounties.add(path);
		}
		int list = ((int) Math.ceil(1 / 36.0));
		
		int amount = Bounties.size();
		int index = 0;
		System.out.println(list + " " + Bounties.size());
		for (int i = 0; i < list; i++) {
			Inventory BountyMenu = Bukkit.createInventory(null,  45, ChatColor.GRAY + "Bounties");
			
			bountyInventories.add(BountyMenu);
			System.out.println(bountyInventories);
			for(int j = 0; j < 36; j++) {
				if(j > amount - 1) break;
				System.out.println("hit");
				UUID playerUUID = UUID.fromString(Bounties.get(index));
				Player player = (Player) Bukkit.getPlayer(playerUUID);
				
				if(player == null) {
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
					ItemStack skull = new ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
					
					SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
					skullMeta.setOwner(offlinePlayer.getName());
					System.out.println(skullMeta);
					skullMeta.setDisplayName(ChatColor.YELLOW + offlinePlayer.getName());
					List<String> lore = new ArrayList<String>();
					String price = Main.getPlugin().getConfig().getString("Bounties." + Bounties.get(index) + ".price");
					lore.add(ChatColor.BLUE + "Price: " + ChatColor.YELLOW + price + " Credits");
					skullMeta.setLore(lore);
					skull.setItemMeta(skullMeta);
					BountyMenu.addItem(skull);
					index++;
					continue;
				}
				ItemStack skull = new ItemStack(Material.matchMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
				
				SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
				skullMeta.setOwner(player.getDisplayName());
				skullMeta.setDisplayName(ChatColor.YELLOW + player.getName());
				List<String> lore = new ArrayList<String>();
				String price = Main.getPlugin().getConfig().getString("Bounties." + Bounties.get(index) + ".price");
				lore.add(ChatColor.BLUE + "Price: " + ChatColor.YELLOW + price + " Credits");
				skullMeta.setLore(lore);
				skull.setItemMeta(skullMeta);
				
				BountyMenu.addItem(skull);
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
			
			BountyMenu.setItem(36, previous);
			BountyMenu.setItem(40, close);
			BountyMenu.setItem(44, next);
			
		}
		
		playerOpening.openInventory(bountyInventories.get(0));
		System.out.println(bountyInventories.size() + " size");
	}
}
