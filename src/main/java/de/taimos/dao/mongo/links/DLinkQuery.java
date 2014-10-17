package de.taimos.dao.mongo.links;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jongo.MongoCollection;
import org.jongo.ResultHandler;

import com.mongodb.DBObject;

public class DLinkQuery<T extends AReferenceableEntity> {
	
	private Class<T> targetClass;
	
	private String labelField;
	
	
	/**
	 * @param targetClass the links target class
	 * @param labelField the name of the label field
	 */
	public DLinkQuery(Class<T> targetClass, String labelField) {
		this.targetClass = targetClass;
		this.labelField = labelField;
	}
	
	public List<DocumentLink<T>> find(MongoCollection collection, String query, Object... parameter) {
		ResultHandler<DocumentLink<T>> handler = new ResultHandler<DocumentLink<T>>() {
			
			@Override
			public DocumentLink<T> map(DBObject result) {
				if (!result.containsField("_id") || !result.containsField(DLinkQuery.this.labelField)) {
					throw new RuntimeException("Fields missing to construct DocumentLink");
				}
				String id = result.get("_id").toString();
				String label = result.get(DLinkQuery.this.labelField).toString();
				return new DocumentLink<T>(DLinkQuery.this.targetClass, id, label);
			}
		};
		Iterator<DocumentLink<T>> it = collection.find(query, parameter).projection(String.format("{%s:1}", this.labelField)).map(handler).iterator();
		
		List<DocumentLink<T>> objects = new ArrayList<>();
		while (it.hasNext()) {
			DocumentLink<T> link = it.next();
			objects.add(link);
		}
		return objects;
	}
	
}
