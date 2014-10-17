package de.taimos.dao.mongo.links;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.taimos.dao.mongo.JongoFactory;

public class DLinkDAO implements IDLinkDAO {
	
	@Autowired
	private MongoClient mongo;
	
	private Jongo jongo;
	
	
	@PostConstruct
	public final void init() {
		String dbName = System.getProperty("mongodb.name");
		if (dbName == null) {
			throw new RuntimeException("Missing database name; Set system property 'mongodb.name'");
		}
		DB db = this.mongo.getDB(dbName);
		this.jongo = JongoFactory.createDefault(db);
	}
	
	@Override
	public <T extends AReferenceableEntity> T resolve(DocumentLink<T> link) {
		MongoCollection collection = this.jongo.getCollection(link.getTargetClass().getSimpleName());
		return collection.findOne(new ObjectId(link.getObjectId())).as(link.getTargetClass());
	}
	
}
