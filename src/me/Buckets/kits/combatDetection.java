package me.Buckets.kits;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;



public class combatDetection implements Listener{
	@EventHandler
	public static void fallDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL) {
			
			if(e.getEntity() instanceof Player) {
				
				Player player = (Player) e.getEntity();
				if(Kits.players.contains(player.getUniqueId())) {
					
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	
	
	public static void playerAttack(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
					                }
			if(e.getDamager() instanceof Arrow) {
				arrowTagPlayer(e);
				return;
			}
			
			tagPlayer(e);
			
			
	}
	
	public static void tagPlayer(EntityDamageByEntityEvent e) {
		if(e.getDamage() == 0) return;
		Player attacker = (Player) e.getDamager();	
		Player attacked = (Player) e.getEntity();
		if(Kits.players.contains(attacker.getUniqueId())) {
			Kits.players.remove(attacker.getUniqueId());
		}
		
		if(Kits.players.contains(attacked.getUniqueId())) {
			Kits.players.remove(attacked.getUniqueId());
		}
		combatTag.tagPlayer(attacker);
		combatTag.tagPlayer(attacked);
	}
	
	public static void arrowTagPlayer(EntityDamageByEntityEvent e) {
		if(e.getDamage() == 0) return;
		Arrow arrow = (Arrow) e.getDamager();
		Player attacker = (Player) arrow.getShooter();	
		Player attacked = (Player) e.getEntity();
		if(Kits.players.contains(attacker.getUniqueId())) {
			Kits.players.remove(attacker.getUniqueId());
		}
		
		if(Kits.players.contains(attacked.getUniqueId())) {
			Kits.players.remove(attacked.getUniqueId());
		}
	}
	
	@EventHandler
	public static void playerJoin(PlayerJoinEvent e) {
		Player player = (Player) e.getPlayer();
		System.out.println(Kits.players.toString() + "players");
		if(!Kits.players.contains(player.getUniqueId())) {
			Kits.players.add(player.getUniqueId());
			System.out.println("Player added");
			System.out.println(Kits.players.toString() + "players");
		}
		
	}
	
	@EventHandler
	public static void playerLeave(PlayerQuitEvent e) {
		Player player = (Player) e.getPlayer();
		System.out.println(Kits.players.toString() + "players");
		if(Kits.players.contains(player.getUniqueId())) {
			Kits.players.remove(player.getUniqueId());
			System.out.println("Removed");
			System.out.println(Kits.players.toString() + "players");
		}
	}
	
	
	
	
	
	@EventHandler
	public static void playerDeath(PlayerRespawnEvent e) {
		Player player = (Player) e.getPlayer();
		Location loc = new Location(Main.getPlugin().getServer().getWorld("Kit World"), 11.505743587904526, 156.0, -38.62288293331645);
		e.setRespawnLocation(loc);
		if(!Kits.players.contains(player.getUniqueId())) {
			Kits.players.add(player.getUniqueId());
		}
	}
}
