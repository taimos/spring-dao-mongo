package de.taimos.dao.mongo;

import java.io.IOException;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 * Copyright 2014 Taimos GmbH<br>
 * <br>
 *
 * @author Thorsten Hoeger
 *
 */
public class MongoDBInit {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBInit.class);
	
	@Autowired
	private MongoClient mongo;
	
	private boolean enabled = false;
	
	
	/**
	 * init database with demo data
	 */
	@PostConstruct
	public void initDatabase() {
		if (!this.enabled) {
			// exit if we are disabled
			return;
		}
		MongoDBInit.LOGGER.info("initializing MongoDB");
		String dbName = System.getProperty("mongodb.name");
		if (dbName == null) {
			throw new RuntimeException("Missing database name; Set system property 'mongodb.name'");
		}
		DB db = this.mongo.getDB(dbName);
		
		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath*:mongodb/*.ndjson");
			MongoDBInit.LOGGER.info("Scanning for collection data");
			for (Resource res : resources) {
				String filename = res.getFilename();
				String collection = filename.substring(0, filename.length() - 7);
				MongoDBInit.LOGGER.info("Found collection file: " + collection);
				DBCollection dbCollection = db.getCollection(collection);
				try (Scanner scan = new Scanner(res.getInputStream())) {
					int lines = 0;
					while (scan.hasNextLine()) {
						String json = scan.nextLine();
						Object parse = JSON.parse(json);
						if (parse instanceof DBObject) {
							DBObject dbObject = (DBObject) parse;
							dbCollection.save(dbObject);
						} else {
							MongoDBInit.LOGGER.error("Invalid object found: " + parse);
							throw new RuntimeException("Invalid object");
						}
						lines++;
					}
					MongoDBInit.LOGGER.info("Imported " + lines + " objects into collection " + collection);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error importing objects", e);
		}
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
