package me.Buckets.kits;

import org.bukkit.entity.Player;

public class ServerPlayer {

	public Player player;
	public long gaveHeals;

	public ServerPlayer(Player player) {
		this.player = player;
		this.gaveHeals = 0;
	}
	
	
}
