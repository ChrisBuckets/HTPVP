package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;


public class fillHeals implements Listener {
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
		    Player player = (Player) event.getPlayer();
	        if(event.getClickedBlock().getType() == Material.matchMaterial("SIGN_POST") || event.getClickedBlock().getType() == Material.matchMaterial("WALL_SIGN") || event.getClickedBlock().getType() == Material.matchMaterial("SIGN_POST")){
	            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	                Sign sign = (Sign) event.getClickedBlock().getState();
	                System.out.println(sign.getLine(1).toString());
	                if(sign.getLine(1).equalsIgnoreCase(ChatColor.AQUA + "[FREE SOUP]") && sign.getLine(2).equalsIgnoreCase(ChatColor.AQUA + "[FREE POTS]")) {
	            		if(!Main.getPlugin().getConfig().contains("Players." + player.getPlayer().getUniqueId().toString() + ".getHealsCooldown")) {
	            			Main.getPlugin().getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".getHealsCooldown", System.currentTimeMillis());
	            			Main.getPlugin().saveConfig();
	            			System.out.println("Saved");
	            		}
	            		if(player.getInventory().firstEmpty() == -1) {
	            			player.sendMessage(ChatColor.RED + "Your inventory is full.");
	            			return;
	            		}
	            	
	            		long getHealCooldown = Main.getPlugin().getConfig().getLong("Players." + player.getPlayer().getUniqueId().toString() + ".getHealsCooldown") + 125;
	                	if(System.currentTimeMillis() < getHealCooldown + 125) return;
	                	String healType = Main.getPlugin().getConfig().getString("Players." + player.getUniqueId().toString() + ".Heals");
	    				if(healType.equalsIgnoreCase("soup")) {
	    					player.getInventory().addItem(new ItemStack(Material.matchMaterial("MUSHROOM_SOUP")));
	    				}
	    				if(healType.equalsIgnoreCase("pots")) {
	    					Potion healPot = new Potion(PotionType.INSTANT_HEAL, 2);
	    					healPot.setSplash(true);
	    					ItemStack potion = healPot.toItemStack(1);
	    					player.getInventory().addItem(new ItemStack(potion));
	    				}
	                	Main.getPlugin().getConfig().set("Players." + player.getPlayer().getUniqueId().toString() + ".getHealsCooldown", System.currentTimeMillis());
	                	player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f,  1f);
	                	return;
	                }
	                
	                if(sign.getLine(1).equalsIgnoreCase(ChatColor.AQUA + "[FILL SOUP]") && sign.getLine(2).equalsIgnoreCase(ChatColor.AQUA + "[FILL POTS]")) {
	            		if(!Main.getPlugin().getConfig().contains("Players." + player.getPlayer().getUniqueId().toString() + ".getHealsCooldown")) {
	            			Main.getPlugin().getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".getHealsCooldown", System.currentTimeMillis());
	            			Main.getPlugin().saveConfig();
	            			System.out.println("Saved");
	            		}
	            		if(player.getInventory().firstEmpty() == -1) {
	            			player.sendMessage(ChatColor.RED + "Your inventory is full.");
	            			return;
	            		}
	            		long getHealCooldown = Main.getPlugin().getConfig().getInt("Players." + player.getPlayer().getUniqueId().toString() + ".getHealsCooldown") + 125;
	                	if(System.currentTimeMillis() < getHealCooldown + 125) return;
	                	Main.getPlugin().getConfig().set("Players." + player.getPlayer().getUniqueId().toString() + ".getHealsCooldown", System.currentTimeMillis());
	                	Kits.giveHeals(player);
	                	player.playSound(player.getLocation(), Sound.valueOf("NOTE_PLING"), 1f,  1f);
	                }
	            }
	        }	
		
		}

		
	}
}
