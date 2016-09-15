package org.generallib.nms.chunk;

import org.bukkit.World;

public interface INmsChunkManager {
	void regenerateChunk(World w, int i, int j, BlockFilter filter);
}