package org.generallib.pluginbase.apisupport;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.chatlib.main.ChatLibAPI;
import org.chatlib.utils.chat.JsonMessage;
import org.chatlib.utils.chat.JsonMessagePlain;
import org.chatlib.utils.chat.handlers.JsonMessageClickEvent;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent;
import org.chatlib.utils.chat.handlers.JsonMessageClickEvent.ClickAction;
import org.chatlib.utils.chat.handlers.JsonMessageHoverEvent.HoverAction;
import org.generallib.pluginbase.PluginAPISupport.APISupport;

public class ChatlibAPISupport extends APISupport{
	public ChatlibAPISupport(Object api) {
		super(api);
	}
	
	public void sendFancyMessage(Player player, String msg, String hover, String click){
		JsonMessage jsonMessage = new JsonMessagePlain(msg);
		jsonMessage.setHoverEvent(new JsonMessageHoverEvent(HoverAction.show_text, hover));
		jsonMessage.setClickEvent(new JsonMessageClickEvent(ClickAction.suggest_command, click));
		ChatLibAPI.sendJsonMessage(player, ChatLibAPI.toJsonString(jsonMessage));
	}
	
	public void sendNextPrevious(Player player, String previousCmd, String homeCmd, String nextCmd){
		String leftArrow = ChatColor.DARK_GRAY+"["+ChatColor.GREEN+"<---"+ChatColor.DARK_GRAY+"]";
		String home = ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Home"+ChatColor.DARK_GRAY+"]";
		String rightArrow = ChatColor.DARK_GRAY+"["+ChatColor.GREEN+"--->"+ChatColor.DARK_GRAY+"]";
		
		JsonMessage prev = new JsonMessagePlain(leftArrow);
		JsonMessage middle = new JsonMessagePlain(home);
		JsonMessage nxt = new JsonMessagePlain(rightArrow);
		
		prev.setHoverEvent(new JsonMessageHoverEvent(HoverAction.show_text, previousCmd));
		prev.setClickEvent(new JsonMessageClickEvent(ClickAction.run_command, previousCmd));
		
		middle.setHoverEvent(new JsonMessageHoverEvent(HoverAction.show_text, homeCmd));
		middle.setClickEvent(new JsonMessageClickEvent(ClickAction.run_command, homeCmd));
		
		nxt.setHoverEvent(new JsonMessageHoverEvent(HoverAction.show_text, nextCmd));
		nxt.setClickEvent(new JsonMessageClickEvent(ClickAction.run_command, nextCmd));
		
		ChatLibAPI.sendJsonMessage(player, ChatLibAPI.toJsonString(new JsonMessage[]{prev, middle, nxt}));
	}
}