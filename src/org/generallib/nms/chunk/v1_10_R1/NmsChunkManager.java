package org.generallib.nms.chunk.v1_10_R1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.generator.NormalChunkGenerator;
import org.generallib.main.FakePlugin;
import org.generallib.nms.chunk.BlockFilter;
import org.generallib.nms.chunk.INmsChunkManager;

import net.minecraft.server.v1_10_R1.ChunkProviderServer;
import net.minecraft.server.v1_10_R1.IChunkLoader;
import net.minecraft.server.v1_10_R1.WorldServer;

public class NmsChunkManager implements INmsChunkManager {
	private static Map<String, ChunkProviderServer> _serv = new ConcurrentHashMap<String, ChunkProviderServer>();
	
	private void initNatural(World w)
    {
        if(!_serv.containsKey(w.getName()))
        {
            CraftWorld cw = (CraftWorld)w;
            WorldServer ws = cw.getHandle();
            IChunkLoader loader = ws.getDataManager().createChunkLoader(ws.worldProvider);
            NormalChunkGenerator _gen = new NormalChunkGenerator(ws, w.getSeed());
            _serv.put(w.getName(), new ChunkProviderServer(ws, loader, _gen));
        }
    }
	
	/* (non-Javadoc)
	 * @see org.generallib.nms.chunk.IChunkGenerator#regenerateChunk(org.bukkit.World, int, int, org.generallib.nms.chunk.ChunkRegenerator.BlockFilter)
	 */
	@Override
	public void regenerateChunk(World w, int i, int j, BlockFilter filter){
		initNatural(w);
		
		Chunk c = _serv.get(w.getName()).getChunkAt(i, j).bukkitChunk;
		Chunk chunk = w.getChunkAt(i, j);
		
		for(int x = 0; x < 16; x++){
			for(int z = 0; z < 16; z++){
				for(int y = 0; y < 128; y++){
					final Block block = c.getBlock(x, y, z);
					if(!filter.allow(block.getTypeId(), block.getData()))
						continue;
					
					final Block target = chunk.getBlock(x, y, z);
					
					FakePlugin.runAsynchronously(new Runnable(){
						@Override
						public void run() {
							Bukkit.getScheduler().runTask(FakePlugin.instance, new Runnable(){
								@Override
								public void run() {
									target.setTypeId(block.getTypeId());
									target.setData(block.getData());	
								}
							});
						}
					});

				}
			}
		}
	}
	

}
