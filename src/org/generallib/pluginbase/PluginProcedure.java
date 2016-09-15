package org.generallib.pluginbase;

public interface PluginProcedure {

	void onEnable(PluginBase base) throws Exception;

	void onDisable(PluginBase base) throws Exception;

	void onReload(PluginBase base) throws Exception;

}