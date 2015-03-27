package de.taimos.dao.mongo;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;

/**
 *
 * Copyright 2014 Hoegernet<br>
 * <br>
 * Special factory for MongoClientOptions which is setting timeouts
 *
 * @author Thorsten Hoeger
 *
 */
public class MongoClientOptionsFactory implements FactoryBean<MongoClientOptions.Builder> {
	
	private int socketTimeout;
	private int connectTimeout;
	
	
	@Override
	public MongoClientOptions.Builder getObject() throws Exception {
		Builder builder = MongoClientOptions.builder();
		builder.socketTimeout(this.socketTimeout);
		builder.connectTimeout(this.connectTimeout);
		return builder;
	}
	
	@Override
	public Class<?> getObjectType() {
		return MongoClientOptions.Builder.class;
	}
	
	@Override
	public boolean isSingleton() {
		return false;
	}
	
	/**
	 * @param socketTimeout the socketTimeout to set
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
}
