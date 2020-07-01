package me.Buckets.kits;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import net.md_5.bungee.api.ChatColor;


public class Main extends JavaPlugin implements Listener {
	
	public static Main plugin;
	public static Main getPlugin() {
		return plugin;
	}
	@Override
	public void onEnable() {
		//startup
		//reloads
		//plugin reloads
		
		
		this.getServer().getPluginManager().registerEvents(this, this);
		plugin = this;
		this.getCommand("kit").setExecutor(new Kits());

		this.getCommand("Kits").setExecutor(new Kits());
		
		this.getCommand("announce").setExecutor(new adminPerms());
		
		this.getCommand("staff").setExecutor(new adminPerms());
		
		this.getCommand("s").setExecutor(new adminPerms());
		
		this.getCommand("invis").setExecutor(new adminPerms());
		
		this.getCommand("spawn").setExecutor(new Kits());
		
		
		this.getCommand("buybase").setExecutor(new createBase());
		this.getCommand("base").setExecutor(new createBase());
		this.getCommand("home").setExecutor(new createBase());
		this.getCommand("kick").setExecutor(new adminPerms());
		this.getCommand("tp").setExecutor(new adminPerms());
		this.getCommand("tphere").setExecutor(new adminPerms());
		this.getCommand("ci").setExecutor(new adminPerms());
		this.getCommand("money").setExecutor(new Economy());
		this.getCommand("balance").setExecutor(new Economy());
		this.getCommand("credits").setExecutor(new Economy());
		this.getCommand("givecredits").setExecutor(new Economy());
		this.getCommand("warp").setExecutor(new adminPerms());
		this.getCommand("setwarp").setExecutor(new adminPerms());;
		
		this.getCommand("npcadd").setExecutor(new leaderboardStatues());
		
		this.getCommand("hit").setExecutor(new Bounty());
		this.getCommand("hits").setExecutor(new Bounty());
		this.getCommand("ban").setExecutor(new adminPerms());
		this.getCommand("unban").setExecutor(new adminPerms());
		this.getCommand("mute").setExecutor(new adminPerms());
		this.getCommand("report").setExecutor(new adminPerms());
		this.getCommand("soup").setExecutor(new Kits());
		this.getCommand("pots").setExecutor(new Kits());
		
		
		
		
		
		
		
		
		
		this.getCommand("deletewarp").setExecutor(new adminPerms());
		
		this.getCommand("shop").setExecutor(new adminPerms());
		this.getServer().getPluginManager().registerEvents(new combatDetection(), this);
		this.getServer().getPluginManager().registerEvents(new Soup(), this);
		this.getServer().getPluginManager().registerEvents(new fillHeals(), this);
		this.getServer().getPluginManager().registerEvents(new baseSystem(), this);
		this.getServer().getPluginManager().registerEvents(new signShop(), this);
		this.getServer().getPluginManager().registerEvents(new bountyMenuEvents(), this);
		
		
		this.getServer().getPluginManager().registerEvents(new bountyBoard(), this);
		
		
		this.getServer().getPluginManager().registerEvents(new shopPortal(), this);
		
		this.saveDefaultConfig();
		
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	leaderboardStatues.updateStatues();
            }
          }, 5);
		
		
		Kits.createKitSelection();
		
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			kitScoreboard.createBoard(online);
		}
		

		
		
		
		
		runnable();
		
		

	}
	
	@Override
	public void onDisable() {
		//shutdowns
		//reloads
		//plugin reloads
	}
	
	public void runnable() {
		new BukkitRunnable() {
			@Override
			public void run() {
				
				//online.sendMessage("Yo");
				
				if(Main.getPlugin().getConfig().getConfigurationSection("Bounties") == null) return;
				
				for(String path : Main.getPlugin().getConfig().getConfigurationSection("Bounties").getKeys(false)) {
					UUID playerUUID = UUID.fromString(path);
					Player player = (Player) Bukkit.getPlayer(playerUUID);
					if(player == null) {
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
						long placed = Main.getPlugin().getConfig().getLong("Bounties." + path + ".customer");
						if(placed + 172800000 < System.currentTimeMillis()) {
							Economy.updateOfflinePlayerCredits(offlinePlayer, placed);
							Main.getPlugin().getConfig().set("Bounties." + path, null);
							saveConfigFile(Main.getPlugin().getConfig(), new File(getDataFolder(), "config.yml"));
							
						}
						
					}
					
					long placed = Main.getPlugin().getConfig().getLong("Bounties." + path + ".placed");
					//172800000
					if(placed + 172800000 < System.currentTimeMillis()) {
						long price = Long.parseLong(Main.getPlugin().getConfig().getString("Bounties." + path + ".price"));
						System.out.println(price + "price");
						Economy.updateCredits(player, price);
				        Scoreboard playerBoard = player.getScoreboard();
				        long updatedCredits = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".credits");
						playerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + updatedCredits);
						player.setScoreboard(playerBoard);
						
						saveConfigFile(Main.getPlugin().getConfig(), new File(getDataFolder(), "config.yml"));
					}
					
					
				}
				
			}
		}.runTaskTimerAsynchronously(this, 0, 40);
	}
	
    private void saveConfigFile(FileConfiguration config, File file) {
        try {
            String text = config.saveToString();
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] strToBytes = text.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	@EventHandler()
	public void onJoin(PlayerJoinEvent event) {
		if(!getConfig().contains("Players." + event.getPlayer().getUniqueId().toString())) {
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".Muted", false);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".Heals", "soup");
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".Base.preset.owned", false);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".Base.custom.owned", false);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".credits", 0);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".creditBoost", 1);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".kills", 0);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".killstreak", 0);
			getConfig().set("Players." + event.getPlayer().getUniqueId().toString() + ".deaths", 0);
			saveConfig();
			reloadConfig();
			System.out.println("Saved");
			Location loc = new Location(Main.getPlugin().getServer().getWorld("Kit World"), 11.505743587904526, 156.0, -38.62288293331645);
			event.getPlayer().teleport(loc);
		}
		kitScoreboard.createBoard(event.getPlayer());
		System.out.println(getConfig().getBoolean("Players." + event.getPlayer().getUniqueId().toString() + ".Muted"));
		if(leaderboardStatues.Statues == null) return;
		if(leaderboardStatues.Statues.isEmpty()) return;
		
		
		
	}
	
	@EventHandler()
	public void entityDeath(EntityDeathEvent e) {
		System.out.println("entity died");
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			Killstreaks.removeKillstreak(player);
		}
		if(e.getEntity() != null && e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			Scoreboard board = player.getScoreboard();
			long killsStat = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".kills");
			long deathsStat = Main.getPlugin().getConfig().getLong("Players." + player.getUniqueId() + ".deaths") + 1;
			Main.getPlugin().getConfig().set("Players." + player.getUniqueId() + ".deaths", deathsStat);
			Main.getPlugin().saveConfig();
			board.getTeam("statsDeaths").setSuffix(ChatColor.GOLD + Long.toString(deathsStat));
			board.getTeam("statsKDR").setSuffix(ChatColor.GOLD + kitScoreboard.calculateKDR(player, killsStat, deathsStat));
			player.setScoreboard(board);
			leaderboardStatues.checkKDR(player);
			if(e.getEntity().getKiller() != null) {
				Player killer = (Player) e.getEntity().getKiller();
				if(killer == null) return;
				if(killer == player) return;
				double creditBoost = Main.getPlugin().getConfig().getDouble("Players." + killer.getUniqueId() + ".creditBoost");
				long randomCredits = ThreadLocalRandom.current().nextLong(90, 120 + 1);
				long multipliedCredits = (long) Math.round(randomCredits * creditBoost);		
				long oldCredits =  Main.getPlugin().getConfig().getLong("Players." + killer.getUniqueId() + ".credits");
				killer.sendMessage(Long.toString(randomCredits));
				killer.sendMessage(Long.toString(multipliedCredits));
				Main.getPlugin().getConfig().set("Players." + killer.getUniqueId() + ".credits", oldCredits + multipliedCredits);
				Main.getPlugin().saveConfig();
				String playerName = ChatColor.stripColor(player.getDisplayName());
				killer.sendMessage(ChatColor.GREEN + "You received " + ChatColor.GOLD + multipliedCredits + " credits " + ChatColor.GREEN + "for killing " + playerName);
				
				Scoreboard killerBoard = killer.getScoreboard();
				killsStat = Main.getPlugin().getConfig().getLong("Players." + killer.getUniqueId() + ".kills") + 1;
				Main.getPlugin().getConfig().set("Players." + killer.getUniqueId() + ".kills", killsStat);
				Main.getPlugin().saveConfig();
				deathsStat = Main.getPlugin().getConfig().getLong("Players." + killer.getUniqueId() + ".deaths");
				killerBoard.getTeam("statsKills").setPrefix(ChatColor.AQUA + "Kills: " + ChatColor.GOLD + Long.toString(killsStat));
				System.out.println(kitScoreboard.calculateKDR(killer, killsStat, deathsStat) + "kdr");
				killerBoard.getTeam("statsKDR").setSuffix(ChatColor.GOLD + kitScoreboard.calculateKDR(killer, killsStat, deathsStat));
				
				long credits = Main.getPlugin().getConfig().getInt("Players." + killer.getUniqueId() + ".credits");
				killerBoard.getTeam("statsCredits").setSuffix(ChatColor.GOLD + "" + credits);
				killer.setScoreboard(killerBoard);
				leaderboardStatues.checkKills(killer);
				leaderboardStatues.checkKDR(killer);
				Killstreaks.addKillstreak(killer);
				if(Bounty.hasBounty(player)) {
					Bounty.giveBountyReward(killer, player);
				}
			}
		}
		
	}
	
	/*@EventHandler()
	public void playerDeath(PlayerDeathEvent e) {
		Player killer = (Player)e.getEntity().getKiller();
		if(killer == null) return;
		Random r = new Random();
		int randomCredits = ThreadLocalRandom.current().nextInt(90, 120 + 1);
		String player = ChatColor.stripColor(e.getEntity().getDisplayName());
		killer.sendMessage(ChatColor.GREEN + "You received " + ChatColor.GOLD + randomCredits + " credits " + ChatColor.GREEN + "for killing " + player);
	}*/
	
	@EventHandler()
	public void onClick(InventoryClickEvent event) {
		if(event.getInventory().equals(event.getWhoClicked().getInventory())) return;
		if(!event.getInventory().equals(Kits.getKitSelection())) return;
		/*if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getItemMeta() == null) return;
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;*/
		
		event.setCancelled(true);
		
		Player player = (Player) event.getWhoClicked();
		
		if(event.getCurrentItem().getType().toString() == "REDSTONE") {
			player.closeInventory();
			return;
		}
		
		String [] itemClicked = event.getCurrentItem().getItemMeta().getDisplayName().split(" ");
		String kitName = itemClicked[1].toLowerCase();
		

		List<String> kitItems = Main.getPlugin().getConfig().getStringList("kits." + kitName + ".items");
		List<String> kitArmor = Main.getPlugin().getConfig().getStringList("kits." + kitName + ".armor");
		//Set<String> kitItems = this.getConfig().getConfigurationSection("kits." + args[0]).getKeys(false);
		System.out.println(kitItems);
		int kitSize = kitItems.size() + kitArmor.size();
		if(player.getInventory().firstEmpty() == -1 || Kits.getEmptySlots(player) < kitSize) {
			player.sendMessage(ChatColor.RED + "You do not have enough space in your inventory.");
			player.closeInventory();
			return;
		}
		
		for(int i = 0; i < kitItems.size(); i++) {
			String [] itemParams = kitItems.get(i).split(" ");
			Kits.giveItem(player, itemParams);
		}
		
		if(player.getInventory().getHelmet() == null && player.getInventory().getChestplate() == null && player.getInventory().getLeggings() == null && player.getInventory().getBoots() == null) {
			Kits.equipArmor(player, kitArmor);
		} else {
			for(int i = 0; i < kitArmor.size(); i++) {
				String[] itemParams = kitArmor.get(i).split(" ");
				Kits.giveItem(player, itemParams);
			}
		}


		
		Kits.giveHeals(player);
		player.closeInventory();
		return;
	}
	
	
	
	@EventHandler()
	public void playerChat(PlayerChatEvent event) {
		Player player = (Player) event.getPlayer();
		Boolean checkMuted = Main.getPlugin().getConfig().getBoolean("Players." + player.getUniqueId().toString() + ".Muted");
		if(checkMuted) {
			player.sendMessage(ChatColor.RED + "You are muted, make a /report or use our discord to appeal.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {

       //do all the checking and what not
		if(Bukkit.getBanList(Type.NAME).isBanned(event.getName())) {
			OfflinePlayer player = (OfflinePlayer) Bukkit.getOfflinePlayer(event.getName());
			String reason = getConfig().getString("Players." + player.getUniqueId().toString() + ".Banned");
			event.disallow(PlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + reason + "\nVisit https://discord.gg/bWamYz7 to appeal.");
		}
    }

	

}

