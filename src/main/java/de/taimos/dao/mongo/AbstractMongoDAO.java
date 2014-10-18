package de.taimos.dao.mongo;

/*
 * #%L Spring DAO Mongo %% Copyright (C) 2013 Taimos GmbH %% Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License. #L%
 */

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.jongo.Find;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;

import de.taimos.dao.AEntity;
import de.taimos.dao.ICrudDAO;

public abstract class AbstractMongoDAO<T extends AEntity> implements ICrudDAO<T> {
	
	@Autowired
	private MongoClient mongo;
	
	private Jongo jongo;
	protected MongoCollection collection;
	
	
	@PostConstruct
	public final void init() {
		String dbName = System.getProperty("mongodb.name");
		if (dbName == null) {
			throw new RuntimeException("Missing database name; Set system property 'mongodb.name'");
		}
		DB db = this.mongo.getDB(dbName);
		this.jongo = this.createJongo(db);
		this.collection = this.jongo.getCollection(this.getCollectionName());
		this.addIndexes();
	}
	
	/**
	 * use MongoBee instead
	 */
	@Deprecated
	protected void addIndexes() {
		// Override to add indexes
	}
	
	/**
	 * use Mongobee and {@link ChangelogUtil} instead
	 * 
	 * @param field the field
	 * @param asc <code>true</code> for ascending; <code>false</code> otherwise
	 * @param background index async
	 */
	@Deprecated
	protected final void addIndex(String field, boolean asc, boolean background) {
		ChangelogUtil.addIndex(this.collection.getDBCollection(), field, asc, background);
	}
	
	/**
	 * use Mongobee and {@link ChangelogUtil} instead
	 * 
	 * @param field the field
	 * @param ttl the TTL in seconds
	 */
	@Deprecated
	protected final void addTTLIndex(String field, int ttl) {
		ChangelogUtil.addTTLIndex(this.collection.getDBCollection(), field, ttl);
	}
	
	/**
	 * @return the name of the mongo collection<br/>
	 *         defaults to the entity's simple name
	 */
	protected String getCollectionName() {
		return this.getEntityClass().getSimpleName();
	}
	
	protected abstract Class<T> getEntityClass();
	
	protected final <R> Iterable<R> mapReduce(String name, final MapReduceResultHandler<R> conv) {
		return this.mapReduce(name, null, null, conv);
	}
	
	protected final <R> Iterable<R> mapReduce(String name, DBObject query, DBObject sort, final MapReduceResultHandler<R> conv) {
		String map = this.getMRFunction(name, "map");
		String reduce = this.getMRFunction(name, "reduce");
		
		MapReduceCommand mrc = new MapReduceCommand(this.collection.getDBCollection(), map, reduce, null, OutputType.INLINE, query);
		mrc.setFinalize(this.getMRFunction(name, "finalize"));
		mrc.setSort(sort);
		MapReduceOutput mr = this.collection.getDBCollection().mapReduce(map, reduce, null, OutputType.INLINE, query);
		return new ConverterIterable<R>(mr.results().iterator(), conv);
	}
	
	private String getMRFunction(String name, String type) {
		try {
			InputStream stream = this.getClass().getResourceAsStream("/mongodb/" + name + "." + type + ".js");
			if (stream != null) {
				return StreamUtils.copyToString(stream, Charset.defaultCharset());
			}
			return null;
		} catch (IOException e) {
			throw new RuntimeException("Failed to read resource", e);
		}
	}
	
	@Override
	public final List<T> findList() {
		Iterable<T> as = this.collection.find().sort("{_id:1}").as(this.getEntityClass());
		return this.convertIterable(as);
	}
	
	protected final <P> List<P> convertIterable(Iterable<P> as) {
		List<P> objects = new ArrayList<>();
		for (P mp : as) {
			objects.add(mp);
		}
		return objects;
	}
	
	protected final List<T> findByQuery(String query, Object... params) {
		return this.findSortedByQuery(query, null, params);
	}
	
	protected final List<T> findSortedByQuery(String query, String sort, Object... params) {
		return this.findSortedByQuery(query, sort, null, this.getEntityClass(), params);
	}
	
	protected final <P> List<P> findSortedByQuery(String query, String sort, String projection, Class<P> as, Object... params) {
		Find find = createFind(query, sort, projection, params);
		return this.convertIterable(find.as(as));
	}
	
	protected final <P> List<P> findSortedByQuery(String query, String sort, String projection, ResultHandler<P> handler, Object... params) {
		Find find = createFind(query, sort, projection, params);
		return this.convertIterable(find.map(handler));
	}

	private Find createFind(String query, String sort, String projection, Object... params) {
		Find find = this.collection.find(query, params);
		if ((sort != null) && !sort.isEmpty()) {
			find.sort(sort);
		}
		if ((projection != null) && !projection.isEmpty()) {
			find.projection(projection);
		}
		return find;
	}
	
	protected final T findFirstByQuery(String query, String sort, Object... params) {
		Find find = this.collection.find(query, params);
		if ((sort != null) && !sort.isEmpty()) {
			find.sort(sort);
		}
		Iterable<T> as = find.limit(1).as(this.getEntityClass());
		Iterator<T> iterator = as.iterator();
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	
	@Override
	public final T findById(String id) {
		return this.collection.findOne(new ObjectId(id)).as(this.getEntityClass());
	}
	
	@Override
	public final T save(T object) {
		this.beforeSave(object);
		this.collection.save(object);
		this.afterSave(object);
		return object;
	}
	
	@SuppressWarnings("unused")
	protected void afterSave(T object) {
		//
	}
	
	@SuppressWarnings("unused")
	protected void beforeSave(T object) {
		//
	}
	
	@Override
	public final void delete(T object) {
		this.delete(object.getId());
	}
	
	@Override
	public final void delete(String id) {
		this.beforeDelete(id);
		this.collection.remove(new ObjectId(id));
		this.afterDelete(id);
	}
	
	@SuppressWarnings("unused")
	protected void beforeDelete(String id) {
		//
	}
	
	@SuppressWarnings("unused")
	protected void afterDelete(String id) {
		//
	}
	
	protected Jongo createJongo(DB db) {
		return JongoFactory.createDefault(db);
	}
}