package org.generallib.database.serialize;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface Serializer<T> extends JsonSerializer<T>, JsonDeserializer<T>{

}
