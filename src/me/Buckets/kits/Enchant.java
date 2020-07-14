package me.Buckets.kits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Enchant implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("enchant")) {
			Player player = (Player) sender;
			if(!player.hasPermission("group.owner")) return true;
			player.getInventory().getItemInHand().addUnsafeEnchantment(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
			return true;
		}
		return false;
	}
}
