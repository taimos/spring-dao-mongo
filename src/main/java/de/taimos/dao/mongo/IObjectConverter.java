package de.taimos.dao.mongo;

import com.mongodb.DBObject;

public interface IObjectConverter<T> {
	
	T convert(DBObject object);
	
}
