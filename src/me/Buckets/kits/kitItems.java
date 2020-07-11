package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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
				return;
			}
			
			if(player.getInventory().getItemInHand().getType() == Material.QUARTZ) {
				for (Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(player.getLocation(), Sound.valueOf("GHAST_FIREBALL"), 1f,  1f);
				}
				player.setVelocity(player.getLocation().getDirection().multiply(4).setY(1));

				new BukkitRunnable() {
					int counter = 0;
					@Override
					public void run() {
						player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, 1);
						counter++;
						if(counter == 20) {
							this.cancel();
						}
					}
				}.runTaskTimer(Main.getPlugin(), 0, 1);
				return;
			}
			
			if(player.getInventory().getItemInHand().getType() == Material.NETHER_STAR) {
				if(Main.ServerPlayers.get(player.getUniqueId()).isInvis) {
					player.sendMessage(ChatColor.AQUA + "You are already invisible!");
					return;
				}
				player.sendMessage(ChatColor.AQUA + "You are invisible for 5 seconds!");
				for (Player online : Bukkit.getOnlinePlayers())
				{
					online.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1f,  1f);
					online.hidePlayer(player);
				}
				
				Main.ServerPlayers.get(player.getUniqueId()).isInvis = true;
        		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    public void run() {
                    	
                    	player.sendMessage(ChatColor.AQUA + "You are no longer invisible!");
        				for (Player online : Bukkit.getOnlinePlayers())
        				{
        					online.playSound(player.getLocation(), Sound.LEVEL_UP, 1f,  1f);
        					online.showPlayer(player);
        				}
        				Main.ServerPlayers.get(player.getUniqueId()).isInvis = false;
                    }
                  }, 100);
				return;
			}

		}
		
		
		
		
		
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
	    if(event.getEntity() instanceof Fireball && event.getDamager() instanceof Player)
	        event.setCancelled(true);
	}

   
	
	
	
	
	
	
	@EventHandler
	public static void playerFallDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL) {
			
			if(e.getEntity() instanceof Player) {
				
				Player player = (Player) e.getEntity();
				if(player.getInventory().getItemInHand().getType() == Material.QUARTZ) {
					
					e.setCancelled(true);
				}
			}
		}
		
		
	}
	
	
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {

        if (event.getRightClicked().getType().equals(EntityType.PLAYER)) {
        	Player player = (Player) event.getRightClicked();
        	if(event.getPlayer().getInventory().getItemInHand().getType() == Material.BLAZE_ROD) {
            	if(player.getInventory().getBoots() != null) {
            		if (Main.ServerPlayers.get(player.getUniqueId()).isMonked) return;
            		Main.ServerPlayers.get(player.getUniqueId()).isMonked = true;
            		ItemStack boots = player.getInventory().getBoots();
            		player.getInventory().setBoots(new ItemStack(Material.AIR));
            		
            		
            		
            		
    				for (Player online : Bukkit.getOnlinePlayers()) online.playSound(player.getLocation(), Sound.ANVIL_LAND, 1f,  1f);
            		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                        public void run() {
                        	Player getOnline = (Player) Bukkit.getPlayer(player.getUniqueId());
                        	if(getOnline != null && Main.ServerPlayers.get(player.getUniqueId()).isMonked) {
                        		getOnline.getInventory().setBoots(new ItemStack(boots));
                        		Main.ServerPlayers.get(player.getUniqueId()).isMonked = false;
                        	}
                        }
                      }, 60);
            	}
        	}
        	
        	
        	if(event.getPlayer().getInventory().getItemInHand().getType() == Material.FLINT) {
        		for (Player online : Bukkit.getOnlinePlayers()) online.playSound(player.getLocation(), Sound.GLASS, 1f,  1f);
        		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
        	}
        }
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
























