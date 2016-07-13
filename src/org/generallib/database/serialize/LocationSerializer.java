package org.generallib.database.serialize;

import java.lang.reflect.Type;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.generallib.database.Database;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class LocationSerializer implements Serializer<Location> {
	@Override
	public JsonElement serialize(Location arg0, Type arg1, JsonSerializationContext arg2) {
		JsonObject json = new JsonObject();
		
		json.addProperty("world", arg0.getWorld().getName());
		json.addProperty("x", arg0.getX());
		json.addProperty("y", arg0.getY());
		json.addProperty("z", arg0.getZ());
		
		return json;
	}

	@Override
	public Location deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject json = (JsonObject) arg0;
		
		World world = Bukkit.getWorld(json.get("world").getAsString());
		double x = json.get("x").getAsDouble();
		double y = json.get("y").getAsDouble();
		double z = json.get("z").getAsDouble();
		
		return new Location(world, x, y, z);
	}

}
