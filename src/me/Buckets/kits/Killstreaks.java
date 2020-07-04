package me.Buckets.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class Killstreaks {
	public static void addKillstreak(Player player) {
		long playerKillstreak = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".killstreak");
		Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".killstreak", playerKillstreak + 1);
		Main.getPlugin().saveConfig();
		Scoreboard playerBoard = player.getScoreboard();
		playerKillstreak = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".killstreak");
		Killstreaks.killstreakRewards(player);
		playerBoard.getTeam("statsKillstreak").setSuffix(ChatColor.GOLD + Long.toString(playerKillstreak));
		player.setScoreboard(playerBoard);
		leaderboardStatues.checkKillstreak(player);	
	}
	
	public static void removeKillstreak(Player player) {
		Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".killstreak", 0);
		Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".creditBoost", "1.0");
		Main.getPlugin().saveConfig();
		Scoreboard playerBoard = player.getScoreboard();
		
		
		long playerKillstreak = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".killstreak");
		player.sendMessage(Long.toString(playerKillstreak));
		playerBoard.getTeam("statsKillstreak").setSuffix(ChatColor.GOLD + Long.toString(playerKillstreak));
		player.setScoreboard(playerBoard);
	}
	
	
	public static void killstreakRewards(Player player) {
		long playerKillstreak = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".killstreak");
		if(playerKillstreak == 3) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + player.getDisplayName() + " is on a " + playerKillstreak + " killstreak!");
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".creditBoost", "1.1");
			Main.getPlugin().saveConfig();
			player.sendMessage(ChatColor.GREEN + "You received a 1.1x Credit boost for your 3 killstreak!");
		}
		
		if(playerKillstreak == 5) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + player.getDisplayName() + " is on a " + playerKillstreak + " killstreak!");
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".creditBoost", "1.2");
			Main.getPlugin().saveConfig();
			player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 1200, 0)));
			player.sendMessage(ChatColor.GREEN + "You received a 1.2x Credit boost and speed for your 5 killstreak!");
		}
		
		if(playerKillstreak == 10) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + player.getDisplayName() + " is on a " + playerKillstreak + " killstreak!");
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".creditBoost", "1.3");
			Main.getPlugin().saveConfig();
			player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 2400, 0)));
			player.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 0)));
			player.sendMessage(ChatColor.GREEN + "You received a 1.3x Credit boost, speed, and strength for your 10 killstreak!");
		}
		
		if(playerKillstreak == 20) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + player.getDisplayName() + " is on a " + playerKillstreak + " killstreak!");
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + "creditBoost", "1.4");
			Main.getPlugin().saveConfig();
			player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 2400, 1)));
			player.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 1)));
			player.sendMessage(ChatColor.GREEN + "You received a 1.4x Credit boost, speed 2, strength 1 for your 30 killstreak!");
		}
		if(playerKillstreak == 30) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "[HTPVP] " + ChatColor.AQUA + player.getDisplayName() + " is on a " + playerKillstreak + " killstreak!");
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".creditBoost", "1.5");
			Main.getPlugin().saveConfig();
			player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 2400, 1)));
			player.addPotionEffect((new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 2)));
			player.addPotionEffect((new PotionEffect(PotionEffectType.REGENERATION, 2400, 1)));
			player.sendMessage(ChatColor.GREEN + "You received a 1.5x Credit boost, speed 2, strength 2, and regeneration 3 for your 30 killstreak!");
		}
	}
	
}
