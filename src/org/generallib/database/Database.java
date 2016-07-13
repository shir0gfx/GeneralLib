package org.generallib.database;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.generallib.database.serialize.ItemStackSerializer;
import org.generallib.database.serialize.LocationSerializer;
import org.generallib.database.serialize.UUIDSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Database<T> {
	private static GsonBuilder builder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(Location.class, new LocationSerializer())
			.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
			.registerTypeAdapter(UUID.class, new UUIDSerializer());
	public static void registerTypeAdapter(Class<?> clazz, Object obj){
		synchronized(builder){
			builder.registerTypeAdapter(clazz, obj);
			//Bukkit.getLogger().info("Serializer -- ["+clazz.getSimpleName()+", "+obj+"]");
		}
	}
	
	public abstract T load(String key, T def);
	public abstract void save(String key, T value);
	public abstract boolean has(String key);
	public abstract Set<String> getKeys();
	
	private Gson gson;
	public String serialize(Object obj){
		if(gson == null) gson = builder.create();
		
		return gson.toJson(obj);
	}
	
	public String serialize(Object obj, Type clazz){
		if(gson == null) gson = builder.create();
		
		return gson.toJson(obj, clazz);
	}
	
	public Object deserialize(String ser, Type clazz){
		if(gson == null) gson = builder.create();
		
		return gson.fromJson(ser, clazz);
	}

}
