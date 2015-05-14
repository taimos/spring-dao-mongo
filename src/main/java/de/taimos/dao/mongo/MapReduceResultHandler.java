package de.taimos.dao.mongo;

import org.jongo.ResultHandler;

import com.mongodb.DBObject;

/**
 * Copyright 2015 Hoegernet<br>
 * <br>
 * abstract {@link ResultHandler} used in map-reduce-operations in the DAO classes
 * 
 * @author Thorsten Hoeger
 *
 * @param <T> the type of the MR result objects
 */
public abstract class MapReduceResultHandler<T> implements ResultHandler<T> {
	
	@Override
	public T map(DBObject result) {
		return this.map(result.get("_id"), result.get("value"));
	}
	
	/**
	 * map the given map-reduce result to the desired class
	 * 
	 * @param key the key of the map function
	 * @param value the value of the reduce function for the given key
	 * @return the converted element
	 */
	protected abstract T map(Object key, Object value);
	
}
