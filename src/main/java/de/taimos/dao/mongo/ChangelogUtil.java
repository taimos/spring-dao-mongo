package de.taimos.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public final class ChangelogUtil {
	
	private ChangelogUtil() {
		// private utility class constructor
	}
	
	public static void addTTLIndex(DBCollection collection, String field, int ttl) {
		if (ttl <= 0) {
			throw new IllegalArgumentException("TTL must be positive");
		}
		collection.createIndex(new BasicDBObject(field, 1), new BasicDBObject("expireAfterSeconds", ttl));
	}
	
	public static void addIndex(DBCollection collection, String field, boolean asc, boolean background) {
		int dir = (asc) ? 1 : -1;
		collection.createIndex(new BasicDBObject(field, dir), new BasicDBObject("background", background));
	}
}
