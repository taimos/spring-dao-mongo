package de.taimos.dao;

import java.util.List;

public interface ICrudDAO<T extends AEntity> {
	
	T findById(String id);
	
	T save(T object);
	
	void delete(T object);
	
	void delete(String id);
	
	List<T> findList();
	
}