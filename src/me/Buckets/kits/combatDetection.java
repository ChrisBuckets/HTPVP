package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.md_5.bungee.api.ChatColor;



public class combatDetection implements Listener{
	
	@EventHandler
	
	public static void playerMove(PlayerMoveEvent e) {
		Player player = (Player) e.getPlayer();
		if(e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
			
			if(Main.ServerPlayers.get(player.getUniqueId()).toWarping != 0) {
				player.sendMessage(ChatColor.RED + "Teleport to spawn cancelled.");
				Bukkit.getServer().getScheduler().cancelTask(Main.ServerPlayers.get(player.getUniqueId()).toWarping);
				Main.ServerPlayers.get(player.getUniqueId()).toWarping = 0;
			}
		}
	}
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
			
			if(e.getDamager() instanceof Fireball) {
				fireballTagPlayer(e);
				return;
			}
			tagPlayer(e);
			
			
	}
	
	public static void tagPlayer(EntityDamageByEntityEvent e) {
		if(e.getDamage() == 0) return;
		Player attacker = (Player) e.getDamager();
		if(Main.ServerPlayers.get(attacker.getUniqueId()).isInvis) {
			e.setCancelled(true);
			return;
		}
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
	
	public static void fireballTagPlayer(EntityDamageByEntityEvent e) {
		if(e.getDamage() == 0) return;
		Fireball f = (Fireball) e.getDamager();
		Player attacker = (Player) f.getShooter();
		if(Main.ServerPlayers.get(attacker.getUniqueId()).isInvis) {
			e.setCancelled(true);
			return;
		}
		if(!(e.getEntity() instanceof Player)) return;
		Player attacked = (Player) e.getEntity();
		attacked.damage(13.4, attacker);
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
		if(Main.ServerPlayers.get(attacker.getUniqueId()).isInvis) {
			e.setCancelled(true);
			return;
		}
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
		kitItems.clearLandMines(player);
		Main.ServerPlayers.remove(player.getUniqueId());
		System.out.println(Main.ServerPlayers);
		System.out.println(Kits.players.toString() + "players");
		if(Kits.players.contains(player.getUniqueId())) {
			Kits.players.remove(player.getUniqueId());
			System.out.println("Removed");
			System.out.println(Kits.players.toString() + "players");
		}
		if(combatTag.checkTagged(player)) {
			player.setHealth(0);
			Bukkit.broadcastMessage(ChatColor.GRAY + player.getName() + " died from combat logging.");
			Bukkit.getServer().getScheduler().cancelTask(combatTag.playerTags.get(player));
			combatTag.playerTags.remove(player);
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
		if(combatTag.checkTagged(player)) {
			Bukkit.getServer().getScheduler().cancelTask(combatTag.playerTags.get(player));
			combatTag.playerTags.remove(player);
		}
	}
}
