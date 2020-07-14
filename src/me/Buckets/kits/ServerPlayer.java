package me.Buckets.kits;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ServerPlayer {

	public Player player;
	public long gaveHeals;
	public Player reply;
	public HashMap<Block, Boolean> landmines;
	public Boolean isMonked;
	
	public Boolean isInvis;
	
	
	
	
	
	public long usedFireball;
	
	
	public long usedMonk;
	
	
	public long usedEagle;
	public long usedBlindingShard;
	
	
	

	
	
	
	
	
	
	public long usedGhost;
	public long usedLandMine;

	public ServerPlayer(Player player) {
		this.player = player;
		this.gaveHeals = 0;
		this.reply = reply;
		this.isMonked = false;
		
		
		
		this.isInvis = false;
		
		
		this.usedFireball = 0;
		this.usedMonk = 0;
		this.usedEagle = 0;
		this.usedBlindingShard = 0;
		this.usedGhost = 0;
		this.usedLandMine = 0;
		
		this.landmines = new HashMap<Block, Boolean>();
	}
	
	
}
