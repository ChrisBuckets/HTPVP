package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class signShop implements Listener {
	
	@EventHandler
	public void shopSign(PlayerInteractEvent event) {
		System.out.println(event.getAction());
		if(event.getClickedBlock().getType() == Material.matchMaterial("SIGN_POST") || event.getClickedBlock().getType() == Material.matchMaterial("WALL_SIGN") || event.getClickedBlock().getType() == Material.matchMaterial("SIGN_POST")) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
			    Player player = (Player) event.getPlayer();
			    Sign sign = (Sign) event.getClickedBlock().getState();
			    if(sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[BUY]")) {
			    	String itemName = ChatColor.stripColor(sign.getLine(1));
			    	String checkPotion[] = sign.getLine(1).split(":");
			    	String getAmount[] = sign.getLine(2).split("\\s+");
			    	String getCredits[] = sign.getLine(3).split("\\s+");
			    	if(Material.matchMaterial(itemName) == null) {

			    		if(checkPotion[0].equals("POT")) {
				    		signShop.buyPotion(player, checkPotion, getAmount, getCredits);
				    		return;
					    } else {
				    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid item listed.");
				    		return;
					    }
			    	}
			    	
			    	if(getAmount.length <= 1) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid amount listed.");
			    		return;
			    	}
			    	
			    	if(getCredits.length <= 1) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid credits listed.");
			    		return;
			    	}
			    	
			    	String amount = ChatColor.stripColor(getAmount[1]);
			    	String credits = ChatColor.stripColor(getCredits[0]);
			    	
			    	try {
			    		Integer.parseInt(amount);
			    	} catch (final NumberFormatException e) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid amount listed.");
			    		return;
			    	}
			    	
			    	try {
			    		Integer.parseInt(credits);
			    	} catch (final NumberFormatException e) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid credits listed.");
			    		return;
			    	}
			    	
					if(player.getInventory().firstEmpty() == -1) {
						player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "You do not have enough space in your inventory.");
						return;
					}
			    	
			    	long playerCredits = Main.getPlugin().getConfig().getLong("Player." + player.getUniqueId() + ".credits");
			    	if(playerCredits < Integer.parseInt(credits)) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Not enough credits.");
			    		return;
			    	}
			    	player.getInventory().addItem(new ItemStack(Material.matchMaterial(itemName), Integer.parseInt(amount)));
			    	player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.GREEN + "Item purchased.");
			    	Main.getPlugin().getConfig().set("Player." + player.getUniqueId() + ".credits", playerCredits - Integer.parseInt(credits));
			    	playerCredits = Main.getPlugin().getConfig().getLong("Player." + player.getUniqueId() + ".credits");
			    	Scoreboard playerBoard = player.getScoreboard();
					playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + playerCredits);
					player.setScoreboard(playerBoard);
			    	return;
			    	
			    }
			    
			    if(sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[ENCHANT]")) {
			    	
			    	String getEnchant[] = sign.getLine(1).split(":");
			    	String enchantName = ChatColor.stripColor(getEnchant[0]);
			    	if(!enchantName.equals("SHARP") && !enchantName.equals("PROT") && !enchantName.equals("THORNS") && !enchantName.equals("FIRE") && 
			    			!enchantName.equals("UNBREAKING") && !enchantName.equals("KNOCKBACK") && !enchantName.equals("FFALL") && !enchantName.equals("POWER") && !enchantName.equals("FLAME") && !enchantName.equals("INFINITY")) {
			    			
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid enchant listed.");
			    		return;
			    	}
			    	
			    	if(enchantName.equals("SHARP")) enchantName = "DAMAGE_ALL";
			    	if(enchantName.equals("INFINITY")) enchantName = "ARROW_INFINITE";
			    	if(enchantName.equals("PROT")) enchantName = "PROTECTION_ENVIRONMENTAL";
			    	if(enchantName.equals("THORNS")) enchantName = "THORNS";
			    	if(enchantName.equals("FIRE")) enchantName = "FIRE_ASPECT";
			    	if(enchantName.equals("FLAME")) enchantName = "ARROW_FIRE";
			    	if(enchantName.equals("POWER")) enchantName = "ARROW_DAMAGE";
			    	if(enchantName.equals("FFALL")) enchantName = "PROTECTION_FALL";
			    	if(enchantName.equals("KNOCKBACK")) enchantName = "KNOCKBACK";
			    	if(enchantName.equals("UNBREAKING")) enchantName = "DURABILITY";
			    	
			    	String getCredits[] = sign.getLine(3).split("\\s+");
			    	
			    	
			    	if(getCredits.length <= 1) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid credits listed.");
			    		return;
			    	}
			    	
			    	String enchantLevel = ChatColor.stripColor(getEnchant[1]);
			    	String credits = ChatColor.stripColor(getCredits[0]);
			    	
			    	try {
			    		Integer.parseInt(enchantLevel);
			    	} catch (final NumberFormatException e) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid amount listed.");
			    		return;
			    	}
			    	
			    	try {
			    		Integer.parseInt(credits);
			    	} catch (final NumberFormatException e) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Invalid credits listed.");
			    		return;
			    	}
			    	
			    	long playerCredits = Main.getPlugin().getConfig().getLong("Player." + player.getUniqueId() + ".credits");
			    	if(playerCredits < Integer.parseInt(credits)) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Not enough credits.");
			    		return;
			    	}
			    	
			    	if(!Enchantment.getByName(enchantName).canEnchantItem(player.getInventory().getItemInHand())) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Unable to enchant this item.");
			    		return;
			    	}
			    	
			    	ItemStack playerItem = player.getInventory().getItemInHand();
			    	if(playerItem.getItemMeta().getEnchantLevel(Enchantment.getByName(enchantName)) == Integer.parseInt(enchantLevel)) {
			    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "You already have this enchant.");
			    		return;
			    	}
			    	playerItem.addEnchantment(Enchantment.getByName(enchantName), Integer.parseInt(enchantLevel));
			    	player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.GREEN + "Enchant purchased.");
			    	Main.getPlugin().getConfig().set("Player." + player.getUniqueId() + ".credits", playerCredits - Integer.parseInt(credits));
			    	playerCredits = Main.getPlugin().getConfig().getLong("Player." + player.getUniqueId() + ".credits");
			    	Scoreboard playerBoard = player.getScoreboard();
					playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + playerCredits);
					player.setScoreboard(playerBoard);
			    	return;
			    	
			    }

			}	
		}
		
		
	}
	
	@EventHandler
	public void signChange(SignChangeEvent sign) {
	    Player player = sign.getPlayer();
	    if (sign.getLine(0).equalsIgnoreCase("buy")) {
	    	if(player.hasPermission("group.owner")) {
				sign.setLine(0, ChatColor.GREEN + "[BUY]");
				player.sendMessage(ChatColor.GREEN + "Shop created.");
				return;
	    	}
	    }
	    
	    if (sign.getLine(0).equalsIgnoreCase("enchant")) {
	    	if(player.hasPermission("group.owner")) {
				sign.setLine(0, ChatColor.GREEN + "[ENCHANT]");
				player.sendMessage(ChatColor.GREEN + "Shop created.");
				return;
	    	}
			 
	    }
	    
	    if(sign.getLine(0).equals(ChatColor.GREEN + "[BUY]") || sign.getLine(0).equals(ChatColor.GREEN + "[ENCHANT]")) {
	    	if(!player.hasPermission("group.owner")) {
	    		sign.setLine(0, ChatColor.stripColor(sign.getLine(0)));
	    		player.sendMessage(ChatColor.RED + "You do not have permission.");
	    		return;
	    	}
	    }
	}
	
	public static void buyPotion(Player player, String[] checkPotion, String[] getAmount, String[] getCredits) {
		if(!checkPotion[1].equals("SPD") && !checkPotion[1].equals("STR") && !checkPotion[1].equals("FIRE") && !checkPotion[1].equals("REG") && !checkPotion[1].equals("POIS")) {
    		player.sendMessage(ChatColor.RED + "Invalid potion listed.");
    		return;
		}
		if(!checkPotion[2].equals("1") && !checkPotion[2].equals("2") && !checkPotion[2].equals("E")) {
    		player.sendMessage(ChatColor.RED + "Invalid potion level listed.");
    		return;
		}
    	if(getAmount.length <= 1) {
    		player.sendMessage(ChatColor.RED + "Invalid amount listed.");
    		return;
    	}
    	
    	if(getCredits.length <= 1) {
    		player.sendMessage(ChatColor.RED + "Invalid credits listed.");
    		return;
    	}
    	
    	String amount = ChatColor.stripColor(getAmount[1]);
    	String credits = ChatColor.stripColor(getCredits[0]);
    	
    	try {
    		Integer.parseInt(amount);
    	} catch (final NumberFormatException e) {
    		player.sendMessage(ChatColor.RED + "Invalid amount listed.");
    		return;
    	}
    	
    	try {
    		Integer.parseInt(credits);
    	} catch (final NumberFormatException e) {
    		player.sendMessage(ChatColor.RED + "Invalid credits listed.");
    		return;
    	}
    	
		if(player.getInventory().firstEmpty() == -1) {
			player.sendMessage(ChatColor.RED + "You do not have enough space in your inventory.");
			return;
		}
		
    	long playerCredits = Main.getPlugin().getConfig().getLong("Player." + player.getUniqueId() + ".credits");
    	if(playerCredits < Integer.parseInt(credits)) {
    		player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.RED + "Not enough credits.");
    		return;
    	}
    	
		Potion potion = new Potion(PotionType.NIGHT_VISION, 2);
		int potionLevel = 0;
		
    	try {
    		if(!checkPotion[2].equals("E")) potionLevel = Integer.parseInt(checkPotion[2]);
    	} catch (final NumberFormatException e) {
    		player.sendMessage(ChatColor.RED + "Invalid potion level listed.");
    		return;
    	}
    	
    	
    	
		if(checkPotion[1].equals("SPD")) {
			potion = new Potion(PotionType.SPEED, potionLevel);
		}
		if(checkPotion[1].equals("STR")) {
			potion = new Potion(PotionType.STRENGTH, potionLevel);
		}
		if(checkPotion[1].equals("FIRE")) {
			potion = new Potion(PotionType.FIRE_RESISTANCE, 2);
			potion.setHasExtendedDuration(true);
		}
		if(checkPotion[1].equals("REG")) {
			potion = new Potion(PotionType.REGEN, potionLevel);
		}
		if(checkPotion[1].equals("POIS")) {
			potion = new Potion(PotionType.POISON, potionLevel);
			potion.setSplash(true);
		}
		
		ItemStack potionStack = potion.toItemStack(Integer.parseInt(amount));
    	player.getInventory().addItem(potionStack);
    	player.sendMessage(ChatColor.GOLD + "[SHOP] " + ChatColor.GREEN + "Item purchased.");
    	Main.getPlugin().getConfig().set("Player." + player.getUniqueId() + ".credits", playerCredits - Integer.parseInt(credits));
    	playerCredits = Main.getPlugin().getConfig().getLong("Player." + player.getUniqueId() + ".credits");
    	Scoreboard playerBoard = player.getScoreboard();
		playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + playerCredits);
		player.setScoreboard(playerBoard);
    	return;
	}

}
