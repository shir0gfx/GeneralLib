package org.generallib.pluginbase.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.generallib.pluginbase.PluginBase;

public class SubCommandAdminReload extends SubCommandAdmin{

	public SubCommandAdminReload(PluginBase base, String permission) {
		super(base, "reload", null, permission, "reload config", "to reload config", 0);
	}
	
	@Override
	protected boolean executeConsole(CommandSender sender, String[] args) {
		base.reloadPluginProcedures();
		base.getLogger().info("Plugin is reloaded.");
		
		return true;
	}

	@Override
	protected boolean executeOp(Player op, String[] args) {
		base.reloadPluginProcedures();
		base.getLogger().info("Plugin is reloaded.");
		
		return true;
	}

}
