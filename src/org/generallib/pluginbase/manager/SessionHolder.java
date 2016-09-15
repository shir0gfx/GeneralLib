package org.generallib.pluginbase.manager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class SessionHolder<T extends PlayerSession> implements Listener{
	private final Map<UUID, PlayerSession> sessions = new ConcurrentHashMap<UUID, PlayerSession>();
	private final SessionConstructor con;
	
	private boolean deleteOnSessionLost = false;
	
	public SessionHolder(boolean deleteOnSessionLost, SessionConstructor con){
		this.con = con;
		this.deleteOnSessionLost = deleteOnSessionLost;
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent e){
		PlayerSession session = sessions.get(e.getPlayer().getUniqueId());
		
		if(session == null){
			session = con.createNewInstance(e.getPlayer());
		}
		
		session.setPlayer(e.getPlayer());
		sessions.put(e.getPlayer().getUniqueId(), session);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onQuit(PlayerQuitEvent e){
		if(deleteOnSessionLost){
			sessions.remove(e.getPlayer().getUniqueId());
		}else{
			PlayerSession session = sessions.get(e.getPlayer().getUniqueId());
			if(session != null){
				session.setPlayer(null);
			}
		}
	}
	
	public T getSession(Player player){
		return getSession(player.getUniqueId());
	}
	@SuppressWarnings("unchecked")
	public T getSession(UUID uuid){
		return (T) sessions.get(uuid);
	}
	
	public interface SessionConstructor{
		PlayerSession createNewInstance(UUID uuid);
		PlayerSession createNewInstance(Player player);
	}
}
