package me.Buckets.kits;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class buildBlocks implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("give")) {
			if(!player.hasPermission("build")) {
				player.sendMessage(ChatColor.RED + "You do not have permission.");
				return true;
			}
			
			if(args.length <= 0) {
				player.sendMessage(ChatColor.RED + "Usage: /give [item] [amount]");
				return true;
			}
			
			if(player.getInventory().firstEmpty() == -1) {
					player.sendMessage(ChatColor.RED + "You do not have enough space in your inventory.");
					return true;
			}
			
			
			List<String> bannedBlocks = Main.getPlugin().getConfig().getStringList("bannedBlocks");
			for(int i = 0; i < bannedBlocks.size(); i++) {
				if(player.hasPermission("group.owner")) break;
				String materialName = args[0];
				if(Material.matchMaterial(materialName) != null && Material.matchMaterial(materialName).getId() == Material.matchMaterial(bannedBlocks.get(i)).getId()) {
					player.sendMessage(ChatColor.RED + "You do not have permission to spawn in this item.");
					return true;
				}
				if(materialName.equalsIgnoreCase(bannedBlocks.get(i))) {
					player.sendMessage(ChatColor.RED + "You do not have permission to spawn in this item.");
					return true;
				}
			}
			
			try {
				Integer.parseInt(args[1]);
			} catch (Exception e){
				player.sendMessage(ChatColor.RED + "Invalid amount given.");
				return true;
			}
			
			if(Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[1]) > 64) {
				player.sendMessage(ChatColor.RED + "Amount parameters: 1-64");
			}
			if(Material.matchMaterial(args[0].toUpperCase()) == null && !args[0].equalsIgnoreCase("sprucelog") && !args[0].equalsIgnoreCase("sprucewood") && 
					!args[0].equalsIgnoreCase("birchlog") && !args[0].equalsIgnoreCase("birchwood") && 
					!args[0].equalsIgnoreCase("junglelog") && !args[0].equalsIgnoreCase("junglewood") &&
					!args[0].equalsIgnoreCase("acacialog") && !args[0].equalsIgnoreCase("acaciawood")) {
				player.sendMessage(ChatColor.RED + "Invalid item.");
				return true;
			}
			ItemStack item = new ItemStack(Material.DIAMOND_BLOCK, Integer.parseInt(args[1]));
			
			if(args[0].equalsIgnoreCase("sprucelog")) item = new ItemStack(Material.LOG, Integer.parseInt(args[1]), (short) 1);
			if(args[0].equalsIgnoreCase("sprucewood")) item = new ItemStack(Material.WOOD, Integer.parseInt(args[1]), (short) 1);
			if(args[0].equalsIgnoreCase("birchlog")) item = new ItemStack(Material.LOG, Integer.parseInt(args[1]), (short) 2);
			if(args[0].equalsIgnoreCase("birchwood")) item = new ItemStack(Material.WOOD, Integer.parseInt(args[1]), (short) 2);
			if(args[0].equalsIgnoreCase("junglelog")) item = new ItemStack(Material.LOG, Integer.parseInt(args[1]), (short) 3);
			if(args[0].equalsIgnoreCase("junglewood")) item = new ItemStack(Material.WOOD, Integer.parseInt(args[1]), (short) 3);
			if(args[0].equalsIgnoreCase("acacialog")) item = new ItemStack(Material.LOG_2, Integer.parseInt(args[1]));
			if(args[0].equalsIgnoreCase("acaciawood")) item = new ItemStack(Material.WOOD, Integer.parseInt(args[1]), (short) 4);
			if(item.getType().equals(Material.DIAMOND_BLOCK)) item = new ItemStack(Material.matchMaterial(args[0]), Integer.parseInt(args[1]));
			player.getInventory().addItem(item);
			player.sendMessage(ChatColor.GREEN + "Item received.");
			player.sendMessage(player.getInventory().getItemInHand().toString());
		}
		return false;
	}
}
