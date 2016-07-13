package org.generallib.database.serialize;

import java.io.IOException;
import java.lang.reflect.Type;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.generallib.database.serialize.Serializer;
import org.generallib.serializetools.Utf8YamlConfiguration;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public class ItemStackSerializer implements Serializer<ItemStack>{

	@Override
	public JsonElement serialize(ItemStack arg0, Type arg1, JsonSerializationContext arg2) {
		JsonObject obj = new JsonObject();
		FileConfiguration fc = new Utf8YamlConfiguration();
		fc.set("ItemStack", arg0);
		obj.addProperty("IS", fc.saveToString());
		
		return obj;
	}

	@Override
	public ItemStack deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject obj = (JsonObject) arg0;
		FileConfiguration fc = new Utf8YamlConfiguration();
		try {
			fc.loadFromString(obj.get("IS").getAsString());
			return fc.getItemStack("ItemStack");
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
