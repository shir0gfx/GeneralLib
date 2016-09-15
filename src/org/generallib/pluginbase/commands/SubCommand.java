package org.generallib.pluginbase.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.generallib.pluginbase.PluginBase;

public abstract class SubCommand extends Command{
	protected final PluginBase base;
	protected ChatColor commandColor = ChatColor.GOLD;
	
	private int arguments  = -1;

	public SubCommand(PluginBase base, String name, List<String> aliases, String permission, String description,
			String usage, int arguments) {
		super(name, description, usage, aliases == null ? new ArrayList<String>() : aliases);
		this.base = base;
		this.arguments = arguments;
		
		setPermission(permission);
	}

	public ChatColor getCommandColor() {
		return commandColor;
	}

	/**
	 * 
	 * @param alias
	 * @return true if not already contain the specified alias
	 */
	protected boolean addAlias(String alias){
		return getAliases().add(alias);
	}
	
	/**
	 *
	 * @param alias
	 * @return true if contained the specified alias
	 */
	protected boolean removeAlias(String alias){
		return getAliases().remove(alias);
	}

	public int getArguments() {
		return arguments;
	}

	protected void setArguments(int arguments) {
		this.arguments = arguments;
	}

	public boolean execute(CommandSender sender, String commandLabel, String[] args) {	
		if(arguments != -1 && args.length != arguments)
			return false;
		
		if (sender == null || sender instanceof ConsoleCommandSender) {
			return executeConsole(sender, args);
		} else {
			Player player = (Player) sender;
			if (player.isOp()) {
				return executeOp(player, args);
			} else {
				return executeUser(player, args);
			}
		}
	}
	
	protected boolean executeConsole(CommandSender sender, String[] args){
		base.getLogger().info("Not allowed to execute from Console.");
		return true;
	}
	protected boolean executeOp(Player op, String[] args){
		op.sendMessage(ChatColor.RED+"Not allowed to execute from OP.");
		return true;
	}
	protected boolean executeUser(Player player, String[] args){
		player.sendMessage(ChatColor.RED+"Not allowed to execute from User.");
		return true;
	}
/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubCommand other = (SubCommand) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}
*/

	@Override
	public String toString() {
		return this.getLabel();
	}
}
