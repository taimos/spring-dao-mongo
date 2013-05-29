package de.taimos.dao.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;

import de.taimos.dao.AEntity;
import de.taimos.dao.ICrudDAO;

public abstract class AbstractMongoMockDAO<T extends AEntity> implements ICrudDAO<T> {
	
	protected ConcurrentHashMap<String, T> objects = new ConcurrentHashMap<>();
	
	
	@Override
	public List<T> findList() {
		System.out.println("Find by mock");
		List<T> res = new ArrayList<>();
		for (T mp : this.objects.values()) {
			res.add(mp);
		}
		return res;
	}
	
	@Override
	public T findById(String id) {
		return this.objects.get(id);
	}
	
	@Override
	public T save(T object) {
		if (object.getId() == null) {
			object.setId(ObjectId.get().toString());
		}
		this.objects.put(object.getId(), object);
		this.customSave(object);
		return object;
	}
	
	@SuppressWarnings("unused")
	protected void customSave(T object) {
		//
	}
	
	@SuppressWarnings("unused")
	protected void customDelete(String id) {
		//
	}
	
	@Override
	public void delete(T object) {
		this.delete(object.getId());
	}
	
	@Override
	public void delete(String id) {
		this.customDelete(null);
		this.objects.remove(id);
	}
}