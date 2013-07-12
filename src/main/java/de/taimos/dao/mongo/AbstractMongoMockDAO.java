package de.taimos.dao.mongo;

/*
 * #%L
 * Spring DAO Mongo
 * %%
 * Copyright (C) 2013 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
		this.customSave(object);
		this.objects.put(object.getId(), object);
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
		this.customDelete(id);
		this.objects.remove(id);
	}
}
