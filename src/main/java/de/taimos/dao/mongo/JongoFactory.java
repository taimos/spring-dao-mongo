package de.taimos.dao.mongo;

import org.joda.time.DateTime;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.JacksonMapper.Builder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DB;

import de.taimos.dao.JodaMapping;

/**
 * Copyright 2015 Hoegernet<br>
 * <br>
 * factory creating the JacksonMapper used by Jongo driver
 * 
 * @author Thorsten Hoeger
 *
 */
public final class JongoFactory {
	
	private JongoFactory() {
		// private utility class constructor
	}
	
	public static Jongo createDefault(DB db) {
		Builder builder = new JacksonMapper.Builder();
		builder.enable(MapperFeature.AUTO_DETECT_GETTERS);
		builder.addSerializer(DateTime.class, new JodaMapping.MongoDateTimeSerializer());
		builder.addDeserializer(DateTime.class, new JodaMapping.MongoDateTimeDeserializer());
		builder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return new Jongo(db, builder.build());
	}
	
}
