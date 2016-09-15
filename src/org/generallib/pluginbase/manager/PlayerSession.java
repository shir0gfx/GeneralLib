package org.generallib.pluginbase.manager;

import java.util.UUID;

import org.bukkit.entity.Player;

public abstract class PlayerSession {
	private final UUID uuid;
	private Player player = null;
	
	public PlayerSession(UUID uuid){
		this.uuid = uuid;
	}
	
	public PlayerSession(Player player){
		this(player.getUniqueId());
		this.player = player;
	}
	
	public boolean isOnline(){
		return player == null || player.isOnline();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
}
