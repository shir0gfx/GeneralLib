package org.generallib.main;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.generallib.nms.chunk.BlockFilter;
import org.generallib.nms.chunk.INmsChunkManager;
import org.generallib.nms.player.INmsPlayerManager;

public class FakePlugin extends JavaPlugin{
	private static final ExecutorService pool = Executors.newFixedThreadPool(4);
	
	public static Plugin instance;
	
	public static INmsChunkManager nmsChunkManager;
	public static INmsPlayerManager nmsPlayerManager;
	
	@Override
	public void onEnable() {
		instance = this;
		
		String packageName = getServer().getClass().getPackage().getName();
		String version = packageName.substring(packageName.lastIndexOf('.') + 1);
		
		try {
			initChunkNms(version);
			initPlayerNms(version);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			getLogger().severe("Version ["+version+"] is not supported by this plugin.");
			this.setEnabled(false);
		}
	}
	
	private static final String packageName = "org.generallib.nms";
	private void initChunkNms(String version) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> clazz = Class.forName(packageName+".chunk."+version+"."+"NmsChunkManager");
		nmsChunkManager = (INmsChunkManager) clazz.newInstance();
	}
	
	private void initPlayerNms(String version) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> clazz = Class.forName(packageName+".player."+version+"."+"NmsPlayerProvider");
		nmsPlayerManager = (INmsPlayerManager) clazz.newInstance();
	}
	
	private static Set<Integer> ores = new HashSet<Integer>(){{
		for(Material mat : Material.values())
			if(mat.name().endsWith("_ORE"))
				add(mat.getId());
	}};
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player && !((Player) sender).isOp())
			return true;
		
		if(!label.equals("glib"))
			return true;
		
		try{
			if(args.length == 3){
				if(args[0].equalsIgnoreCase("chunk")){
					Player player = (Player) sender;
					int i = Integer.parseInt(args[1]);
					int j = Integer.parseInt(args[2]);
					
					nmsChunkManager.regenerateChunk(player.getWorld(), i, j, new BlockFilter(){
						@Override
						public boolean allow(int blockID, byte data) {
							return ores.contains(blockID);
						}
					});
				}
			}else if(args.length == 4){
				if(args[0].equalsIgnoreCase("chunk")){
					World world = Bukkit.getWorld(args[3]);
					int i = Integer.parseInt(args[1]);
					int j = Integer.parseInt(args[2]);
					
					nmsChunkManager.regenerateChunk(world, i, j, new BlockFilter(){
						@Override
						public boolean allow(int blockID, byte data) {
							return ores.contains(blockID);
						}
					});
				}
			}
		}catch(Exception e){
			sender.sendMessage(ChatColor.RED+e.getMessage());
			return true;
		}
		
		return true;
	}
	
	public static void runAsynchronously(Runnable run){
		pool.execute(run);
	}
}
