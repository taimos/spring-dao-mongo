package de.taimos.dao;

import java.util.UUID;

import org.jongo.marshall.jackson.oid.Id;

public class AEntity {
	
	@Id
	protected String id = UUID.randomUUID().toString();
	
	
	public String getId() {
		return this.id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
	
}