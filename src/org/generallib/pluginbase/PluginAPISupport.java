package org.generallib.pluginbase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.generallib.pluginbase.apisupport.ChatlibAPISupport;

public final class PluginAPISupport implements PluginProcedure{
	private final Queue<Runnable> hookQueue = new LinkedList<Runnable>();
	final Map<String, APISupport> apis = new HashMap<String, APISupport>();
	
	private PluginBase base;
	@Override
	public void onEnable(PluginBase base) throws Exception {
		this.base = base;
		
		if(isExist("ChatLib"))
			hookAPI("ChatLib", new ChatlibAPISupport(null));
		
		while(!hookQueue.isEmpty()){
			Runnable run = hookQueue.poll();
			
			run.run();
		}
	}

	@Override
	public void onDisable(PluginBase base) throws Exception {
		
	}

	@Override
	public void onReload(PluginBase base) throws Exception {
		
	}
	
	/**
	 * 
	 * @param pluginName
	 */
	public void hookAPI(final String pluginName, final APISupport api){
		hookQueue.add(new Runnable(){
			@Override
			public void run() {
				PluginManager pm = base.getServer().getPluginManager();
				
				Plugin plugin = pm.getPlugin(pluginName);
				if(plugin != null && plugin.isEnabled()){
					base.getLogger().info("Hooked API ["+pluginName+"]");
					base.getLogger().info("Info: "+plugin.getDescription().getFullName());
					
					pm.registerEvents(api, base);
					apis.put(pluginName, api);
				} else {
					base.getLogger().info("Plugin [" + pluginName
							+ "] is not found or enabled. Related supports will not be provided.");
				}
			}
		});
	}
	
	public boolean isExist(String pluginName){
		return base.getServer().getPluginManager().getPlugin(pluginName) != null;
	}
	
	/**
	 * 
	 * @param pluginName
	 * @return check whether the api is registered or not
	 */
	public boolean isHooked(String pluginName){
		return apis.containsKey(pluginName);
	}
	
	/**
	 * 
	 * @param pluginName
	 * @return
	 */
	public <T extends APISupport> T getAPI(String pluginName){
		return (T) apis.get(pluginName);
	}
	
	public static abstract class APISupport implements Listener{
		protected final Object api;
		public APISupport(Object api) {
			this.api = api;
		}
		public <T> T getApi() {
			return (T) api;
		}
		
	}
}
