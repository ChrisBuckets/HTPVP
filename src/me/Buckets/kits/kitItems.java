package me.Buckets.kits;

import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;

public class kitItems implements Listener {
	@EventHandler(priority=EventPriority.HIGH)
	public static void useItem(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(player.getInventory().getItemInHand().getType() == Material.FIREBALL) {
				if(!checkPvpRegion(player)) {
					player.sendMessage(ChatColor.RED + "You can't use this kit item here.");
					return;
				}
				
				Fireball f = player.launchProjectile(Fireball.class);
				
			}

		}
		
		
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
	    if(event.getEntity() instanceof Fireball && event.getDamager() instanceof Player)
	        event.setCancelled(true);
	}

   
	
	
	
	
	
	
	
	
	public static Boolean checkPvpRegion(Player player) {
		RegionContainer container = createBase.getWorldGuard().getRegionContainer();
		RegionQuery query = container.createQuery();

		ApplicableRegionSet set = query.getApplicableRegions(player.getLocation());
		Boolean checkPvp = true;
		if (set.allows(DefaultFlag.PVP) == false) checkPvp = false;
		return checkPvp;
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
























