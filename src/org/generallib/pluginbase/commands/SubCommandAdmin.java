package org.generallib.pluginbase.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.generallib.pluginbase.PluginBase;

public abstract class SubCommandAdmin extends SubCommand {

	public SubCommandAdmin(PluginBase base, String name, List<String> aliases, String permission,
			String description, String usage, int arguments) {
		super(base, name, aliases, permission, description, usage, arguments);
		commandColor = ChatColor.LIGHT_PURPLE;
	}


}
