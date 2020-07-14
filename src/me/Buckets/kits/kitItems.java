package me.Buckets.kits;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
			if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
					player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Fireball") ||
					player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Landmine")) {
				if(!checkPvpRegion(player)) {
					player.sendMessage(ChatColor.RED + "You can't use this kit item here.");
					return;
				}
			}
			
			if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
					player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Fireball")) {
				if(!kitItems.checkKitItemCooldown(player, 5000, Main.ServerPlayers.get(player.getUniqueId()).usedFireball)) return;
				Main.ServerPlayers.get(player.getUniqueId()).usedFireball = System.currentTimeMillis();
				Fireball f = player.launchProjectile(Fireball.class); 
				return;
			}
			
			if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
					player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Eagle Wing")) {
				if(!kitItems.checkKitItemCooldown(player, 10000, Main.ServerPlayers.get(player.getUniqueId()).usedEagle)) return;
				Main.ServerPlayers.get(player.getUniqueId()).usedEagle = System.currentTimeMillis();
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
			
			if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
					player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Invisible Gem")) {
				if(Main.ServerPlayers.get(player.getUniqueId()).isInvis) {
					player.sendMessage(ChatColor.AQUA + "You are already invisible!");
					return;
				}
				if(!kitItems.checkKitItemCooldown(player, 35000, Main.ServerPlayers.get(player.getUniqueId()).usedGhost)) return;
				Main.ServerPlayers.get(player.getUniqueId()).usedGhost = System.currentTimeMillis();
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
			
			if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
					player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Landmine")) {
				if(!kitItems.checkKitItemCooldown(player, 10000, Main.ServerPlayers.get(player.getUniqueId()).usedLandMine)) return;
				Main.ServerPlayers.get(player.getUniqueId()).usedLandMine = System.currentTimeMillis();
				
				
				
				
				
				
				
				
				Block above = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
				
				
				//player.sendMessage(event.getClickedBlock().getType().toString());
				if(above.getType() != Material.AIR || event.getClickedBlock().getType().toString().contains("GLASS") || event.getBlockFace() != BlockFace.UP ||
						 event.getClickedBlock().getType().toString().contains("LEAVES") ||  event.getClickedBlock().getType().toString().contains("PLATE") ||
						 event.getClickedBlock().getType().toString().contains("CARPET") ||  event.getClickedBlock().getType().toString().contains("STAIRS") ||
						 event.getClickedBlock().getType().toString().contains("SLAB") ||  event.getClickedBlock().getType().toString().contains("STEP") ||
						 event.getClickedBlock().getType() == Material.TORCH) return;
				if(Main.ServerPlayers.get(player.getUniqueId()).landmines.size() >= 3) {
					System.out.println(Main.ServerPlayers.get(player.getUniqueId()).landmines);
					player.sendMessage(ChatColor.RED + "You have placed the maximum amount of landmines.");
					event.setCancelled(true);
					return;
				}
				
				above.setType(Material.IRON_PLATE);
				System.out.println(above + " above block");
				Main.ServerPlayers.get(player.getUniqueId()).landmines.put(above, true); 
				for (Player online : Bukkit.getOnlinePlayers()) online.playSound(player.getLocation(), Sound.ANVIL_USE, 1f,  1f);
		
		
				
				Main.ServerPlayers.get(player.getUniqueId()).landmines.put(above, true);
				
			}
			


		}
		
		if(event.getAction() == Action.PHYSICAL && event.getPlayer().getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
				event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Landmine")) {
			player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1, false, false);
			event.setCancelled(true);
			//String landmineOwner = event.getClickedBlock().getMetadata("landmine").get(0).asString();
			System.out.println(event.getClickedBlock().getMetadata("landmine") + "landmine");
			//event.getPlayer().sendMessage(landmineOwner);
			for (Player online : Bukkit.getOnlinePlayers()) {
				if(Main.ServerPlayers.get(online.getUniqueId()).landmines.size() > 0) {
					Block landmine = event.getClickedBlock();
					if(Main.ServerPlayers.get(online.getUniqueId()).landmines.containsKey(landmine)) {
						event.getPlayer().damage(40, online);
						online.sendMessage(ChatColor.AQUA + "You damaged " + event.getPlayer().getName() + " with your landmine!");
						Main.ServerPlayers.get(online.getUniqueId()).landmines.remove(landmine);
					}
				}
			}
			event.getClickedBlock().setType(Material.AIR);


		}
		
		
		
		
		
	}
	
	
	
	
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
    	if(event.getPlayer().getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
				event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Landmine")) {
    		event.setCancelled(true);
			/*Player player = (Player) event.getPlayer();
			Block above = event.getBlock().getLocation().add(0, 0, 0).getBlock();
			if(above.getType() != Material.AIR) return;
			if(Main.ServerPlayers.get(player.getUniqueId()).landmines.size() >= 3) {
				player.sendMessage(ChatColor.RED + "You have placed the maximum amount of landmines.");
				event.setCancelled(true);
				return;
			}
			
			if(!player.hasPermission("group.owner")) above.setType(Material.IRON_PLATE);
			System.out.println(above + " above block");
			Main.ServerPlayers.get(player.getUniqueId()).landmines.put(above, true); 
			for (Player online : Bukkit.getOnlinePlayers()) online.playSound(player.getLocation(), Sound.ANVIL_USE, 1f,  1f);
	
	
			
			Main.ServerPlayers.get(player.getUniqueId()).landmines.put(above, true);
			System.out.println(Main.ServerPlayers.get(player.getUniqueId()).landmines);*/
			//player.teleport(above.getLocation());
    	}
    	return;
    	
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
        	if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() &&
        			player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Blinding Shard") ||
        			player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Monk Staff")) {
				if(!checkPvpRegion(player)) {
					player.sendMessage(ChatColor.RED + "You can't use this kit item here.");
					return;
				}
        	}
        	if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
        			player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Monk Staff")) {
            	if(player.getInventory().getBoots() != null) {
            		if (Main.ServerPlayers.get(player.getUniqueId()).isMonked) return;
            		if(!kitItems.checkKitItemCooldown(player, 45000, Main.ServerPlayers.get(player.getUniqueId()).usedMonk)) return;
            		Main.ServerPlayers.get(event.getPlayer().getUniqueId()).usedMonk = System.currentTimeMillis();
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
        	
        	
        	if(player.getInventory().getItemInHand().getItemMeta().hasDisplayName() && 
        			player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Blinding Shard")) {
        		if(!kitItems.checkKitItemCooldown(player, 15000, Main.ServerPlayers.get(player.getUniqueId()).usedBlindingShard)) return;
        		Main.ServerPlayers.get(event.getPlayer().getUniqueId()).usedBlindingShard = System.currentTimeMillis();
        		for (Player online : Bukkit.getOnlinePlayers()) online.playSound(player.getLocation(), Sound.GLASS, 1f,  1f);
        		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
        	}
        }
    }
	
	
	/*@EventHandler(priority = EventPriority.HIGH)
	public static void playerLeave(PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		kitItems.clearLandMines(player);
	}*/
	
	
	
	public static Boolean checkPvpRegion(Player player) {
		RegionContainer container = createBase.getWorldGuard().getRegionContainer();
		RegionQuery query = container.createQuery();

		ApplicableRegionSet set = query.getApplicableRegions(player.getLocation());
		Boolean checkPvp = true;
		if (set.allows(DefaultFlag.PVP) == false) checkPvp = false;
		return checkPvp;
	}
	
	
	
	
	
	public static void clearLandMines(Player player) {
		if(Main.ServerPlayers.get(player.getUniqueId()).landmines.size() > 0) {
			for (Block block : Main.ServerPlayers.get(player.getUniqueId()).landmines.keySet()) {
				block.setType(Material.AIR);
			}
		}
	}
	
	
	public static Boolean checkKitItemCooldown(Player player, long cooldown, long lastUsed) {
		if(System.currentTimeMillis() < lastUsed + cooldown) {
			long seconds = (long) Math.floor(((lastUsed + cooldown) - System.currentTimeMillis()) / 1000);
			player.sendMessage(ChatColor.RED + "You have " + seconds + " seconds until you can use this kit item again.");
			return false;
		}
		return true;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
























