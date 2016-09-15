package org.generallib.pluginbase.commands;

import java.util.Queue;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.generallib.database.tasks.DatabaseTransferTask;
import org.generallib.database.tasks.DatabaseTransferTask.TransferPair;
import org.generallib.pluginbase.PluginBase;

public class SubCommandAdminImport extends SubCommandAdmin {
	private final Set<String> dbTypes;
	private final TransferPairProvider provider;
	
	public SubCommandAdminImport(PluginBase base, String permission, Set<String> dbTypes, TransferPairProvider provider) {
		super(base, "import", null, permission, "db types : " + dbTypes, "/v admin import <from>" , 1);
		
		this.dbTypes = dbTypes;
		this.provider = provider;
	}

	@Override
	protected boolean executeConsole(CommandSender sender, String[] args) {
		String fromName = args[0];
		if(!dbTypes.contains(fromName)){
			base.getLogger().severe("Invalid db type -- "+fromName);
			return false;
		}
		
		Set<TransferPair> pairs = provider.getTransferPair(fromName);
		if(pairs == null){
			base.getLogger().severe("Cannot obtain transfer pair from DB: "+fromName);
			return false;
		}
		
		new Thread(new DatabaseTransferTask(base, pairs)).start();
		
		return true;
	}

	public interface TransferPairProvider{
		Set<TransferPair> getTransferPair(String dbTypeFrom);
	}
}
