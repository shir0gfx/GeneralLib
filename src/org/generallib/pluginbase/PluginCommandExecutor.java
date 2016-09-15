package org.generallib.pluginbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.generallib.pluginbase.apisupport.ChatlibAPISupport;
import org.generallib.pluginbase.commands.SubCommand;
import org.generallib.pluginbase.commands.SubCommandAdminReload;
import org.generallib.pluginbase.commands.SubCommandHelp;
import org.generallib.pluginbase.language.DefaultLanguages;

public final class PluginCommandExecutor implements PluginProcedure {
	private PluginBase base;
	
	public final String mainCommand;
	public final String adminPermission;
	
	private CommandMap commandMap;
	
	protected PluginCommandExecutor(String mainCommand, String adminPermission){
		this.mainCommand = mainCommand;
		this.adminPermission = adminPermission;
	}

	@Override
	public void onEnable(PluginBase base) throws Exception {
		this.base = base;
		this.commandMap = new SubCommandMap();

		addCommand(new SubCommandAdminReload(base, adminPermission));
		addCommand(new SubCommandHelp(base, adminPermission));
	}

	@Override
	public void onDisable(PluginBase base) throws Exception {

	}

	@Override
	public void onReload(PluginBase base) throws Exception {

	}

	public void addCommand(SubCommand cmd){
		commandMap.register(null, cmd);
	}
	
	public boolean onCommand(CommandSender sender, Command arg0, String label, String[] args) {
		if(!label.equalsIgnoreCase(mainCommand));
		
		String cmdLine = "";
		for(String str : args){
			cmdLine += str + " ";
		}
		
		commandMap.dispatch(sender, cmdLine);
		
		return true;
	}
	
	/**
	 * 
	 * @param sender
	 * @param page 0~size()
	 */
	public void showHelp(CommandSender sender, int page){
		List<Command> list = new ArrayList<Command>();
		for(Entry<String, Command> entry : commandList.entrySet()){
			Command cmd = entry.getValue();
			if(!cmd.testPermissionSilent(sender)){
				continue;
			}
			
			list.add(cmd);
		}
		
		base.lang.addString(base.getDescription().getName());
		base.sendMessage(sender, DefaultLanguages.General_Header);
		sender.sendMessage("");
		
		int max = base.getPluginConfig().Command_Help_SentencePerPage;
		
		int index;
		for (index = page * max; index >= 0 && index < (page + 1) * max; index++) {
			if(index >= list.size())
				break;
			
			Command c = list.get(index);
			ChatColor color = ChatColor.GOLD;
			if(c instanceof SubCommand)
				color = ((SubCommand) c).getCommandColor();
			
			base.lang.addString(color+c.toString());
			base.lang.addString(c.getDescription());
			if(base.APISupport.isHooked("ChatLib")){
				ChatlibAPISupport api = base.APISupport.getAPI("ChatLib");
				
				if (sender instanceof Player){
					api.sendFancyMessage((Player) sender,
							base.lang.parseFirstString(sender, DefaultLanguages.Command_Format_Description),
							"/" + mainCommand + " " + c.toString() + " " + c.getUsage(),
							"/" + mainCommand + " " + c.toString() + " ");
				}else{
					base.sendMessage(sender, DefaultLanguages.Command_Format_Description);
					base.lang.addString(c.getName());
					base.lang.addString(c.getUsage());
					base.sendMessage(sender, DefaultLanguages.Command_Format_Usage);
				}
			}else{
				base.sendMessage(sender, DefaultLanguages.Command_Format_Description);
				base.lang.addString(c.getName());
				base.lang.addString(c.getUsage());
				base.sendMessage(sender, DefaultLanguages.Command_Format_Usage);
			}
		}
		
		sender.sendMessage(ChatColor.LIGHT_PURPLE +"");
		
		base.lang.addInteger(page + 1);
		int remainder = list.size() % base.getPluginConfig().Command_Help_SentencePerPage;
		int divided = list.size() / base.getPluginConfig().Command_Help_SentencePerPage;
		
		base.lang.addInteger(remainder == 0 ? divided : divided + 1);
		sender.sendMessage(base.lang.parseFirstString(DefaultLanguages.Command_Help_PageDescription));
		
		sender.sendMessage(base.lang.parseFirstString(DefaultLanguages.Command_Help_TypeHelpToSeeMore));
		if(base.APISupport.isHooked("ChatLib") && sender instanceof Player){
			ChatlibAPISupport api = base.APISupport.getAPI("ChatLib");
			api.sendNextPrevious((Player) sender, 
					"/" + mainCommand + " help " + (page),
					"/" + mainCommand + " help ",
					"/" + mainCommand + " help " + (page+2));
		}
		sender.sendMessage(ChatColor.GRAY +"");
	}
	
	private final Map<String, Command> commandList = new LinkedHashMap<String, Command>();
	private final Map<String, String> aliasMap = new HashMap<String, String>();
	private class SubCommandMap implements CommandMap{
		@Override
		public void clearCommands() {
			commandList.clear();
			aliasMap.clear();
		}

		@Override
		public boolean dispatch(CommandSender arg0, String arg1) throws CommandException {
			String[] split = arg1.split(" ");
			
			String cmd = split[0];
			if(aliasMap.containsKey(cmd)){
				cmd = aliasMap.get(cmd);
			}
			
			String[] args = new String[split.length - 1];
			for(int i = 1; i < split.length; i++){
				args[i - 1] = split[i];
			}
			
			Command command = commandList.get(cmd);
			if(command != null){
				if(!arg0.hasPermission(command.getPermission())){
					base.sendMessage(arg0, DefaultLanguages.General_NotEnoughPermission);
					return true;
				}
				
				return command.execute(arg0, cmd, args);
			}else{
				base.lang.addString(cmd);
				base.sendMessage(arg0, DefaultLanguages.General_NoSuchCommand);
				return true;
			}
		}

		@Override
		public Command getCommand(String arg0) {
			return commandList.get(arg0);
		}

		@Override
		public boolean register(String arg0, Command arg1) {
			List<String> aliases = arg1.getAliases();
			for(String alias : aliases){
				aliasMap.put(alias, arg1.getLabel());
			}
			
			if(commandList.containsKey(arg1.getLabel())){
				return false;
			}
			
			commandList.put(arg1.getLabel(), arg1);
			return true;
		}

		@Override
		public boolean register(String arg0, String arg1, Command arg2) {
			if(commandList.containsKey(arg0))
				return false;
			
			commandList.put(arg0, arg2);
			return true;
		}

		@Override
		public void registerAll(String arg0, List<Command> arg1) {
			for(Command cmd : arg1){
				register(null, cmd);
			}
		}

		@Override
		public List<String> tabComplete(CommandSender arg0, String arg1) throws IllegalArgumentException {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
}
