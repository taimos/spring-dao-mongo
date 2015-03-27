package de.taimos.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * Copyright 2015 Hoegernet<br>
 * <br>
 * Utils for changelog creation
 * 
 * @author Thorsten Hoeger
 *
 */
public final class ChangelogUtil {
	
	private ChangelogUtil() {
		// private utility class constructor
	}
	
	/**
	 * adds a TTL index to the given collection. The TTL must be a positive integer.
	 * 
	 * @param collection the collection to use for the TTL index
	 * @param field the field to use for the TTL index
	 * @param ttl the TTL to set on the given field
	 * @throws IllegalArgumentException if the TTL is less or equal 0
	 */
	public static void addTTLIndex(DBCollection collection, String field, int ttl) {
		if (ttl <= 0) {
			throw new IllegalArgumentException("TTL must be positive");
		}
		collection.createIndex(new BasicDBObject(field, 1), new BasicDBObject("expireAfterSeconds", ttl));
	}
	
	/**
	 * Add an index on the given collection and field
	 * 
	 * @param collection the collection to use for the index
	 * @param field the field to use for the index
	 * @param asc the sorting direction. <code>true</code> to sort ascending; <code>false</code> to sort descending
	 * @param background iff <code>true</code> the index is created in the background
	 */
	public static void addIndex(DBCollection collection, String field, boolean asc, boolean background) {
		int dir = (asc) ? 1 : -1;
		collection.createIndex(new BasicDBObject(field, dir), new BasicDBObject("background", background));
	}
}
