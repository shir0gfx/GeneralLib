package org.generallib.packets.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketHelper {
	public static void sendActionBarMessage(Player player, String msg){
		String str = ChatColor.translateAlternateColorCodes('&', msg);
		IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + str+ "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);
	}
}
