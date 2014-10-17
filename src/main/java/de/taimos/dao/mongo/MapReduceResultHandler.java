package de.taimos.dao.mongo;

import org.jongo.ResultHandler;

import com.mongodb.DBObject;

public abstract class MapReduceResultHandler<T> implements ResultHandler<T> {
	
	@Override
	public T map(DBObject result) {
		return this.map(result.get("_id"), result.get("value"));
	}
	
	protected abstract T map(Object key, Object value);
	
}
