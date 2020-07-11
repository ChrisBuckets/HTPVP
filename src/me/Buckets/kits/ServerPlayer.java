package me.Buckets.kits;

import org.bukkit.entity.Player;

public class ServerPlayer {

	public Player player;
	public long gaveHeals;
	public Player reply;
	
	

	
	public Boolean isMonked;
	public Boolean isInvis;
	
	public ServerPlayer(Player player) {
		this.player = player;
		this.gaveHeals = 0;
		this.reply = reply;
		this.isMonked = false;
		
		
		
		this.isInvis = false;
	}
	
	
}
