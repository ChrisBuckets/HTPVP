package me.Buckets.kits;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.NMS;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityPlayer;



public class leaderboardStatues implements CommandExecutor {
	public static List<EntityPlayer> Statues = new ArrayList<EntityPlayer>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("npcadd")) {
			if(!player.hasPermission("group.owner")) {
				player.sendMessage(ChatColor.RED + "You don't have permission.");
				return true;
			}
			/*NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.RED + "1111111111111111");
			npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, UUID.randomUUID().toString());
			String[] name = leaderboardStatues.getOnlineSkin(player);
			npc.getTrait(SkinTrait.class).setSkinPersistent(player.getName(), name[1], name[0]);
			
			npc.spawn(player.getLocation());
			npc.data().set("kills", 1);
			if(npc.data().has("kills")) player.sendMessage("yo");
			Hologram hologram = HologramsAPI.createHologram(Main.getPlugin(), player.getLocation());
			hologram.appendTextLine("A hologram line");
			//save hologram locations, loop through and spawn em in onEnable
			//leaderboardStatues.createNPC(player);
			player.sendMessage("NPC created");*/
			for(NPC npc : CitizensAPI.getNPCRegistry()) {
				if(npc.data().has("kills")) leaderboardStatues.updateKillStatue(npc);
				
				if(npc.data().has("killstreak")) leaderboardStatues.updateKillstreakStatue(npc);
				
				if(npc.data().has("kdr")) leaderboardStatues.updateKDR(npc);
				
				if(npc.data().has("credits")) leaderboardStatues.updateCredits(npc);
			}
		}
		
		return true;
	}
	
	public static Hologram killsHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	public static Hologram killsLabelHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	public static Hologram killstreakHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	public static Hologram killstreakLabelHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	
	public static Hologram kdrHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	public static Hologram kdrLabelHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	public static Hologram creditsHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	public static Hologram creditsLabelHologram = HologramsAPI.createHologram(Main.getPlugin(), new Location(Main.getPlugin().getServer().getWorld("Kit World"), 0, 0, 0));
	
	
	
	public static void updateStatues() {
		for(NPC npc : CitizensAPI.getNPCRegistry()) {
			if(npc.data().has("kills")) leaderboardStatues.updateKillStatue(npc);
			
			if(npc.data().has("killstreak")) leaderboardStatues.updateKillstreakStatue(npc);
			
			if(npc.data().has("kdr")) leaderboardStatues.updateKDR(npc);
			
			if(npc.data().has("credits")) leaderboardStatues.updateCredits(npc);
		}
	}
	
	
	public static void updateKillStatue(NPC npc) {
		Location text = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 3, npc.getStoredLocation().getZ());
		Location labelText = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 4, npc.getStoredLocation().getZ());
		killsHologram.teleport(text);
		killsLabelHologram.teleport(labelText);
		long mostKills = 0;
		String uuid = Bukkit.getOfflinePlayer("FishFr0g").getUniqueId().toString();
		for(String path : Main.getPlugin().getConfig().getConfigurationSection("Players.").getKeys(false)) {
			long kills = Main.getPlugin().getConfig().getLong("Players." + path + ".kills");
			if(kills > mostKills) {
				mostKills = kills;
				uuid = path;
			}
		}
		
		Player playerMostKills = Bukkit.getPlayer(UUID.fromString(uuid));
		OfflinePlayer offlinePlayerMostKills =  Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		killsHologram.clearLines();
		killsLabelHologram.clearLines();
		if(playerMostKills == null) {
			killsHologram.appendTextLine(ChatColor.GOLD + "" + mostKills + " Kills");
			
			
			killsLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "MOST KILLS");
			npc.setName(offlinePlayerMostKills.getName());
			System.out.println(offlinePlayerMostKills.getName() + "name");
			npc.getTrait(SkinTrait.class).setSkinName(offlinePlayerMostKills.getName());
			Main.getPlugin().getConfig().set("Leaderboards." + "kills" + ".amount", mostKills);
			Main.getPlugin().getConfig().set("Leaderboards." + "kills" + ".player", offlinePlayerMostKills.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			return;
		}
		
		killsLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "MOST KILLS");
		killsHologram.appendTextLine(ChatColor.GOLD + "" + mostKills + " Kills");
		String[] name = leaderboardStatues.getOnlineSkin(playerMostKills);
		npc.setName(playerMostKills.getName());
		npc.getTrait(SkinTrait.class).setSkinPersistent(playerMostKills.getName(), name[1], name[0]);
		
		Main.getPlugin().getConfig().set("Leaderboards." + "kills" + ".amount", mostKills);
		Main.getPlugin().getConfig().set("Leaderboards." + "kills" + ".player", playerMostKills.getUniqueId().toString());
		Main.getPlugin().saveConfig();
	}
	
	public static void updateKillstreakStatue(NPC npc) {
		Location text = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 3, npc.getStoredLocation().getZ());
		Location labelText = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 4, npc.getStoredLocation().getZ());
		killstreakHologram.teleport(text);
		killstreakLabelHologram.teleport(labelText);
		long bestKillstreak = Main.getPlugin().getConfig().getLong("Leaderboards." + "killstreak" + ".amount");
		String uuid = Main.getPlugin().getConfig().getString("Leaderboards." + "killstreak" + ".player");
		if(uuid == null) uuid = Bukkit.getOfflinePlayer("FishFr0g").getUniqueId().toString();
		if(!Main.getPlugin().getConfig().contains("Leaderboards." + "killstreak" + ".amount")) bestKillstreak = 0;
		
		Player playerBestStat = Bukkit.getPlayer(UUID.fromString(uuid));
		OfflinePlayer offlinePlayerBestStat =  Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		killstreakHologram.clearLines();
		killstreakLabelHologram.clearLines();
		if(playerBestStat == null) {
			killstreakHologram.appendTextLine(ChatColor.GOLD + "" + bestKillstreak + " Killstreak");
			killstreakLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "HIGHEST KILLSTREAK");
			npc.setName(offlinePlayerBestStat.getName());
			npc.getTrait(SkinTrait.class).setSkinName(offlinePlayerBestStat.getName());
			Main.getPlugin().getConfig().set("Leaderboards." + "killstreak" + ".amount", bestKillstreak);
			Main.getPlugin().getConfig().set("Leaderboards." + "killstreak" + ".player", offlinePlayerBestStat.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			return;
		}
		killstreakHologram.appendTextLine(ChatColor.GOLD + "" + bestKillstreak + " Killstreak");
		killstreakLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "HIGHEST KILLSTREAK");
		String[] name = leaderboardStatues.getOnlineSkin(playerBestStat);
		npc.setName(playerBestStat.getName());
		npc.getTrait(SkinTrait.class).setSkinPersistent(playerBestStat.getName(), name[1], name[0]);
		
		Main.getPlugin().getConfig().set("Leaderboards." + "killstreak" + ".amount", bestKillstreak);
		Main.getPlugin().getConfig().set("Leaderboards." + "killstreak" + ".player", playerBestStat.getUniqueId().toString());
		Main.getPlugin().saveConfig();
	} 
	
	
	
	public static void updateKDR(NPC npc) {
		Location text = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 3, npc.getStoredLocation().getZ());
		Location labelText = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 4, npc.getStoredLocation().getZ());
		kdrHologram.teleport(text);
		kdrLabelHologram.teleport(labelText);
		double bestStat = 0.00;
		String uuid = Bukkit.getPlayer("FishFr0g").getUniqueId().toString();
		for(String path : Main.getPlugin().getConfig().getConfigurationSection("Players.").getKeys(false)) {
			long kills = Main.getPlugin().getConfig().getLong("Players." + path + ".kills");
			long deaths = Main.getPlugin().getConfig().getLong("Players." + path + ".deaths");
			double kdr = (double) kills / (double) deaths;
			if(kdr > bestStat) {
				bestStat = kdr;
				uuid = path;
			}
		}
		
		Player playerBestStat = Bukkit.getPlayer(UUID.fromString(uuid));
		OfflinePlayer offlinePlayerBestStat =  Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		kdrHologram.clearLines();
		kdrLabelHologram.clearLines();
		if(playerBestStat == null) {
			DecimalFormat df = new DecimalFormat("#.##");
			String kdrFormat = df.format(bestStat);
			kdrHologram.appendTextLine(ChatColor.GOLD + "" + kdrFormat + " KDR");
			kdrLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "BEST KDR");
			npc.setName(offlinePlayerBestStat.getName());
			npc.getTrait(SkinTrait.class).setSkinName(offlinePlayerBestStat.getName());
			Main.getPlugin().getConfig().set("Leaderboards." + "kdr" + ".amount", bestStat);
			Main.getPlugin().getConfig().set("Leaderboards." + "kdr" + ".player", offlinePlayerBestStat.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			return;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		String kdrFormat = df.format(bestStat);
		kdrHologram.appendTextLine(ChatColor.GOLD + "" + kdrFormat + " KDR");
		kdrLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "BEST KDR");
		String[] name = leaderboardStatues.getOnlineSkin(playerBestStat);
		npc.setName(playerBestStat.getName());
		npc.getTrait(SkinTrait.class).setSkinPersistent(playerBestStat.getName(), name[1], name[0]);

		
		Main.getPlugin().getConfig().set("Leaderboards." + "kdr" + ".amount", bestStat);
		Main.getPlugin().getConfig().set("Leaderboards." + "kdr" + ".player", playerBestStat.getUniqueId().toString());
		Main.getPlugin().saveConfig();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void updateCredits(NPC npc) {
		Location text = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 3, npc.getStoredLocation().getZ());
		Location labelText = new Location(Main.getPlugin().getServer().getWorld("Kit World"), npc.getStoredLocation().getX(), npc.getStoredLocation().getY() + 4, npc.getStoredLocation().getZ());
		creditsHologram.teleport(text);
		creditsLabelHologram.teleport(labelText);
		long bestStat = 0;
		String uuid = Bukkit.getPlayer("FishFr0g").getUniqueId().toString();
		for(String path : Main.getPlugin().getConfig().getConfigurationSection("Players.").getKeys(false)) {
			long credits= Main.getPlugin().getConfig().getLong("Players." + path + ".credits");
			if(credits > bestStat) {
				bestStat = credits;
				uuid = path;
			}
		}
		
		Player playerBestStat = Bukkit.getPlayer(UUID.fromString(uuid));
		OfflinePlayer offlinePlayerBestStat =  Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		creditsHologram.clearLines();
		creditsLabelHologram.clearLines();
		if(playerBestStat == null) {
			creditsHologram.appendTextLine(ChatColor.GOLD + "" + bestStat + " Credits");
			creditsLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "MOST CREDITS");
			String[] name = leaderboardStatues.getOfflineSkin(offlinePlayerBestStat);
			npc.setName(offlinePlayerBestStat.getName());
			npc.getTrait(SkinTrait.class).setSkinPersistent(offlinePlayerBestStat.getName(), name[1], name[0]);
			Main.getPlugin().getConfig().set("Leaderboards." + "credits" + ".amount", bestStat);
			Main.getPlugin().getConfig().set("Leaderboards." + "credits" + ".player", offlinePlayerBestStat.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			return;
		}
		creditsHologram.appendTextLine(ChatColor.GOLD + "" + bestStat + " Credits");
		creditsLabelHologram.appendTextLine(ChatColor.DARK_RED + "" + ChatColor.BOLD + "MOST CREDITS");
		npc.setName(playerBestStat.getName());
		npc.getTrait(SkinTrait.class).setSkinName(playerBestStat.getName());
		
		Main.getPlugin().getConfig().set("Leaderboards." + "credits" + ".amount", bestStat);
		Main.getPlugin().getConfig().set("Leaderboards." + "credits" + ".player", playerBestStat.getUniqueId().toString());
		Main.getPlugin().saveConfig();
	}
	public static String[] getOnlineSkin(Player player) {
		EntityPlayer p = ((CraftPlayer) player).getHandle();
		GameProfile profile = p.getProfile();
		Property property = profile.getProperties().get("textures").iterator().next();
		String texture = property.getValue();
		String signature = property.getSignature();
		return new String[] {texture, signature};
	}
	
	public static String[] getOfflineSkin(OfflinePlayer player) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName());
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
			
			URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();
			return new String[] {texture, signature};
		} catch (Exception e) {
			System.out.println(e);
			return new String[] {null, null};
		}
		/*CraftOfflinePlayer p = ((CraftOfflinePlayer) player);
		GameProfile profile = p.getProfile();
		Property property = profile.getProperties().get("textures").iterator().next();
		String texture = property.getValue();
		String signature = property.getSignature();
		return new String[] {texture, signature};*/
	}
	
	
	public static void checkKills(Player player) {
		//Check kills/other states and if someone has a new top stat, save to config and update
		
		long mostKills = Main.getPlugin().getConfig().getLong("Leaderboards.kills.amount");
		long playerKills = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".kills");
		Player mostKillsPlayer = (Player) Bukkit.getPlayer(UUID.fromString(Main.getPlugin().getConfig().getString("Leaderboards.kills.player")));
		NPC npc = leaderboardStatues.getNPC("kills");
		if(mostKillsPlayer != null && mostKillsPlayer == player && playerKills < mostKills) {
			leaderboardStatues.updateKillStatue(npc);
			return;
		}
		if(playerKills > mostKills) {
			Main.getPlugin().getConfig().set("Leaderboards.kills.amount", playerKills);
			Main.getPlugin().getConfig().set("Leaderboards.kills.players", playerKills);
			killsHologram.clearLines();
			killsHologram.appendTextLine(ChatColor.GOLD + "" + playerKills + " Kills");
			String[] name = leaderboardStatues.getOnlineSkin(player);
			if(npc == null) {
				System.out.println("Statue not found.");
				return;
			}
			
			
			npc.setName(player.getName());
			npc.getTrait(SkinTrait.class).setSkinPersistent(player.getName(), name[1], name[0]);
		}
		
	}
	
	


	
	
	
	public static void checkKillstreak(Player player) {
		//Check kills/other states and if someone has a new top stat, save to config and update
		
		long bestKillstreak = Main.getPlugin().getConfig().getLong("Leaderboards.killstreak.amount");
		long playerKillstreak = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".killstreak");
		Player mostKillstreakPlayer = (Player) Bukkit.getPlayer(UUID.fromString(Main.getPlugin().getConfig().getString("Leaderboards.killstreak.player")));
		NPC npc = leaderboardStatues.getNPC("killstreak");
		if(mostKillstreakPlayer != null && mostKillstreakPlayer == player && playerKillstreak < bestKillstreak) {
			leaderboardStatues.updateKillstreakStatue(npc);
			return;
		}
		if(playerKillstreak > bestKillstreak) {
			Main.getPlugin().getConfig().set("Leaderboards.killstreak.amount", playerKillstreak);
			Main.getPlugin().getConfig().set("Leaderboards.killstreak.player", player.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			killstreakHologram.clearLines();
			killstreakHologram.appendTextLine(ChatColor.GOLD + "" + playerKillstreak + " Killstreak");
			String[] name = leaderboardStatues.getOnlineSkin(player);
			if(npc == null) {
				System.out.println("Statue not found.");
				return;
			}
			
			
			npc.setName(player.getName());
			npc.getTrait(SkinTrait.class).setSkinPersistent(player.getName(), name[1], name[0]);
		}
		
	}
	
	
	public static void checkKDR(Player player) {
		//Check kills/other states and if someone has a new top stat, save to config and update
		
		double mostKDR = Main.getPlugin().getConfig().getDouble("Leaderboards.kdr.amount");
		long kills = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".kills");
		long deaths = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".deaths");
		double playerKDR = (double) kills / (double) deaths;
		DecimalFormat df = new DecimalFormat("#.##");
		String kdrFormat = df.format(playerKDR);
		Player mostKDRPlayer = (Player) Bukkit.getPlayer(UUID.fromString(Main.getPlugin().getConfig().getString("Leaderboards.kdr.player")));
		NPC npc = leaderboardStatues.getNPC("kdr");
		if(mostKDRPlayer != null && mostKDRPlayer == player && playerKDR < mostKDR) {
			leaderboardStatues.updateKDR(npc);
			return;
		}
		System.out.println("most " + mostKDR + " player " + playerKDR);
		if(playerKDR > mostKDR) {
			Main.getPlugin().getConfig().set("Leaderboards.kdr.amount", playerKDR);
			Main.getPlugin().getConfig().set("Leaderboards.kdr.player", player.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			kdrHologram.clearLines();
			kdrHologram.appendTextLine(ChatColor.GOLD + "" + kdrFormat + " KDR");
			String[] name = leaderboardStatues.getOnlineSkin(player);
			Bukkit.broadcastMessage("Statue updated");
			if(npc == null) {
				System.out.println("Statue not found.");
				return;
			}
			
			
			npc.setName(player.getName());
			npc.getTrait(SkinTrait.class).setSkinPersistent(player.getName(), name[1], name[0]);
		}
		
	}
	
	
	public static void checkCredits(Player player) {
		//Check kills/other states and if someone has a new top stat, save to config and update
		
		long mostCredits = Main.getPlugin().getConfig().getLong("Leaderboards.credits.amount");
		Player mostCreditsPlayer = (Player) Bukkit.getPlayer(UUID.fromString(Main.getPlugin().getConfig().getString("Leaderboards.credits.player")));
		long playerCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
		NPC npc = leaderboardStatues.getNPC("credits");
		if(mostCreditsPlayer != null && mostCreditsPlayer == player && playerCredits < mostCredits) {
			leaderboardStatues.updateCredits(npc);
			return;
		}
		if(playerCredits > mostCredits) {
			Main.getPlugin().getConfig().set("Leaderboards.credits.amount", playerCredits);
			Main.getPlugin().getConfig().set("Leaderboards.credits.player", player.getUniqueId().toString());
			Main.getPlugin().saveConfig();
			creditsHologram.clearLines();
			creditsHologram.appendTextLine(ChatColor.GOLD + "" + playerCredits + " Credits");
			String[] name = leaderboardStatues.getOnlineSkin(player);
			if(npc == null) {
				System.out.println("Statue not found.");
				return;
			}
			
			
			npc.setName(player.getName());
			npc.getTrait(SkinTrait.class).setSkinPersistent(player.getName(), name[1], name[0]);
		}
		
	}
	
	
	
	

	
	
	
	public static NPC getNPC(String name) {
		for(NPC npc : CitizensAPI.getNPCRegistry()) {
			if(npc.data().has(name)) return npc;
			
			if(npc.data().has(name)) return npc;
			
			if(npc.data().has(name)) return npc;
			
			if(npc.data().has(name)) return npc;
		}
		return null;
	}
	
	
}
