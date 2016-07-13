package org.generallib.location.utils;

import org.bukkit.Location;

public class LocationNormalizer {
	public static Location normalizeLocation(Location loc){
		return new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5);
	}
	
	public static String toSimpleString(Location loc){
		if(loc == null)
			return "Null";
		return "["+loc.getWorld().getName()+", "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+"]";
	}
}
