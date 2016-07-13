package org.generallib.database.serialize;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class UUIDSerializer implements Serializer<UUID>{

	@Override
	public JsonElement serialize(UUID arg0, Type arg1, JsonSerializationContext arg2) {
		return new JsonPrimitive(arg0.toString());
	}

	@Override
	public UUID deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		return UUID.fromString(arg0.getAsString());
	}

}
