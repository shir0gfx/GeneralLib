package org.generallib.pluginbase;

public abstract class PluginManager{
	public static final int MIN_PRIORITY = 0;
	public static final int NORM_PRIORITY = 5;
	public static final int MAX_PRIORITY = 10;
	
	protected final PluginBase base;
	private final int loadPriority;
	
	public PluginManager(PluginBase base, int loadPriority){
		this.base = base;
		if(loadPriority < MIN_PRIORITY || loadPriority > MAX_PRIORITY)
			throw new IllegalArgumentException("load priority out of bound.");
		
		this.loadPriority = loadPriority;
		
		base.pluginManager.put(this.getClass(), this);
	}
	
	public int getLoadPriority() {
		return loadPriority;
	}

	protected abstract void onEnable() throws Exception;
	protected abstract void onDisable() throws Exception;
	protected abstract void onReload() throws Exception;
	
	
}
