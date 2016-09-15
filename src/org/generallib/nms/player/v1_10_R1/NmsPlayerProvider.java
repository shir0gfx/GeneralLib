package org.generallib.nms.player.v1_10_R1;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.generallib.nms.player.INmsPlayerManager;

public class NmsPlayerProvider implements INmsPlayerManager {
	@Override
	public String getLocale(Player player) {
		CraftPlayer cp = (CraftPlayer) player;
		return cp.getHandle().locale;
	}

}
