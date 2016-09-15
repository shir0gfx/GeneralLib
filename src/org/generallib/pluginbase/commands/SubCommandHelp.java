package org.generallib.pluginbase.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.generallib.pluginbase.PluginBase;
import org.generallib.pluginbase.language.DefaultLanguages;

public class SubCommandHelp extends SubCommand {

	public SubCommandHelp(PluginBase base, String permission) {
		super(base, "help", null, permission, 
				base.lang.parseFirstString(DefaultLanguages.Command_Help_Description),
				base.lang.parseFirstString(DefaultLanguages.Command_Help_Usage),
				-1);
	}

	@Override
	protected boolean executeConsole(CommandSender sender, String[] args) {
		int page = 0;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
				page -= 1;
			}catch(NumberFormatException e){
				base.lang.addString(args[0]);
				base.sendMessage(sender, DefaultLanguages.General_NotANumber);
				return true;
			}
		}
		
		show(sender, page);
		
		return true;
	}

	@Override
	protected boolean executeOp(Player op, String[] args) {
		int page = 0;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
				page -= 1;
			}catch(NumberFormatException e){
				base.lang.addString(args[0]);
				base.sendMessage(op, DefaultLanguages.General_NotANumber);
				return true;
			}
		}
		
		show(op, page);
		
		return true;
	}

	@Override
	protected boolean executeUser(Player player, String[] args) {
		int page = 0;
		if(args.length > 0){
			try{
				page = Integer.parseInt(args[0]);
				page -= 1;
			}catch(NumberFormatException e){
				base.lang.addString(args[0]);
				base.sendMessage(player, DefaultLanguages.General_NotANumber);
				return true;
			}
		}
		
		show(player, page);
		
		return true;
	}

	private void show(CommandSender sender, int page){
		base.executor.showHelp(sender, page);
	}
}
