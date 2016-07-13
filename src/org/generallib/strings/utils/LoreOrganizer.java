package org.generallib.strings.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class LoreOrganizer {
	private static final int MAX = 30;
	
	public static List<String> organize(List<String> lores){
		List<String> newLore = new ArrayList<String>();
		
		String color = "§f";
		String str = "";
		for(String lore : lores){
			String[] sentences = ChatColor.stripColor(lore).split(" ");
			if(lore.startsWith("§")){
				color = lore.substring(0, 2);
			}
			
			int index = 0;
			for(String sentence : sentences){
				
				if((str + " " +sentence).length() <= MAX){
					str += index == 0 ? sentence : " "+sentence;
				}else{
					newLore.add(color+str);
					str = sentence;
				}
				index++;
			}
			
			newLore.add(color+str);
			str = "";
		}
		
		return newLore;
	}
	
	public static List<String> organize(String[] lores){
		List<String> newLore = new ArrayList<String>();
		
		String color = "§f";
		String str = "";
		for(String lore : lores){
			String[] sentences = ChatColor.stripColor(lore).split(" ");
			if(lore.startsWith("§")){
				color = lore.substring(0, 2);
			}
			
			int index = 0;
			for(String sentence : sentences){
				
				if((str + " " +sentence).length() <= MAX){
					str += index == 0 ? sentence : " "+sentence;
				}else{
					newLore.add(color+str);
					str = sentence;
				}
				index++;
			}
			
			newLore.add(color+str);
			str = "";
		}
		
		return newLore;
	}
	
	public static List<String> organize(String lore){
		List<String> newLore = new ArrayList<String>();
		
		String color = "§f";
		String str = "";
		String[] sentences = ChatColor.stripColor(lore).split(" ");
		if (lore.startsWith("§")) {
			color = lore.substring(0, 2);
		}

		int index = 0;
		for (String sentence : sentences) {

			if ((str + " " + sentence).length() <= MAX) {
				str += index == 0 ? sentence : " " + sentence;
			} else {
				newLore.add(color + str);
				str = sentence;
			}
			index++;
		}

		newLore.add(color + str);
		str = "";
		
		return newLore;
	}
}
		
