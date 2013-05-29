package de.taimos.dao;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class JodaMapping {
	
	public static class MongoDateTimeSerializer extends JsonSerializer<DateTime> {
		
		@Override
		public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeObject(value.toDate());
		}
		
	}
	
	public static class MongoDateTimeDeserializer extends JsonDeserializer<DateTime> {
		
		@Override
		public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			Object object = jp.getEmbeddedObject();
			if (object instanceof Date) {
				Date date = (Date) object;
				return new DateTime(date.getTime(), DateTimeZone.forTimeZone(ctxt.getTimeZone()));
			}
			return null;
		}
		
	}
	
}
