package org.generallib.pluginbase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.generallib.main.FakePlugin;
import org.generallib.pluginbase.PluginAPISupport.APISupport;
import org.generallib.pluginbase.PluginLanguage.Language;
import org.generallib.pluginbase.language.DefaultLanguages;

public abstract class PluginBase extends JavaPlugin{
	final Map<Class<? extends PluginManager>, PluginManager> pluginManager = new HashMap<Class<? extends PluginManager>, PluginManager>();
	
	private final PluginConfig config;
	public final PluginLanguage lang;
	public final PluginCommandExecutor executor;
	public final PluginAPISupport APISupport;
	
	public PluginBase(final PluginConfig config, String mainCommand, String adminPermission) {
		this.config = config;
		
		try {
			config.onEnable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While loading config:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
			this.setEnabled(false);
		}
		
		String def = config.Plugin_Language_Default;
		Set<String> list = new HashSet<String>(){{addAll(config.Plugin_Language_List);}};
		
		this.lang = new PluginLanguage(list, def);
		this.executor = new PluginCommandExecutor(mainCommand, adminPermission);
		this.APISupport = new PluginAPISupport();
	}
	
	private void initiatePluginProcedures(){
		try {
			lang.onEnable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While loading lang:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
			this.setEnabled(false);
		}
		
		try {
			executor.onEnable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While loading command executor:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
			this.setEnabled(false);
		}
		
		try {
			APISupport.onEnable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While loading APISupport:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
			this.setEnabled(false);
		}
	}
	
	private void finalizeDisableProcedures(){
		try {
			config.onDisable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While disabling config:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
		
		try {
			lang.onDisable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While disabling lang:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
		
		try {
			executor.onDisable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While disabling command executor:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
		
		try {
			APISupport.onDisable(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While disabling APISupport:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
	}
	
	public void reloadPluginProcedures(){
		try {
			config.onReload(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While reloading config:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
		
		try {
			lang.onReload(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While reloading lang:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
		
		try {
			executor.onReload(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While reloading command executor:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
		
		try {
			APISupport.onReload(this);
		} catch (Exception e) {
			e.printStackTrace();
			this.getLogger().severe("While reloading APISupport:");
			this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
		}
	}

	@Override
	public void onEnable() {
		initiatePluginProcedures();
		
		for(Entry<Class<? extends PluginManager>, PluginManager> entry : pluginManager.entrySet()){
			try {
				entry.getValue().onEnable();
			} catch (Exception e) {
				e.printStackTrace();
				this.getLogger().severe("While Enabling ["+entry.getKey().getSimpleName()+"]:");
				this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
				this.setEnabled(false);
				
				this.getLogger().info(lang.parseFirstString(DefaultLanguages.Plugin_WillBeDisabled));
				return;
			}
			
			if(entry.getValue() instanceof Listener){
				getServer().getPluginManager().registerEvents((Listener) entry.getValue(), this);
			}
		}
	}

	@Override
	public void onDisable() {
		for(Entry<Class<? extends PluginManager>, PluginManager> entry : pluginManager.entrySet()){
			try {
				entry.getValue().onDisable();
			} catch (Exception e) {
				e.printStackTrace();
				this.getLogger().severe("While Disabling ["+entry.getKey().getSimpleName()+"]:");
				this.getLogger().severe(e.getClass().getSimpleName()+"@"+e.getMessage());
			}
		}
		
		finalizeDisableProcedures();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return executor.onCommand(sender, command, label, args);
	}

	public void sendMessage(CommandSender sender, Language language){
		if(sender instanceof Player){
			sendMessage((Player)sender, language);
		}else{
			sender.sendMessage(lang.parseStrings(language));
		}
	}
	
	public void sendMessage(Player player, Language language){
		String localeSimplified = FakePlugin.nmsPlayerManager.getLocale(player).split("_")[0];
		player.sendMessage(lang.parseStrings(player, language, localeSimplified));
	}
	
	/**
	 * 
	 * @param clazz
	 * @return the Manager; null if nothing found.
	 */
	@SuppressWarnings("unchecked")
	public <T extends PluginManager> T getManagerByClass(Class<? extends PluginManager> clazz){
		return (T) pluginManager.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T extends PluginConfig> T getPluginConfig() {
		return (T) config;
	}
}
