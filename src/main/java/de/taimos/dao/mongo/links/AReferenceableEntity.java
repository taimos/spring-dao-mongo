package de.taimos.dao.mongo.links;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.taimos.dao.AEntity;

public abstract class AReferenceableEntity extends AEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	@JsonIgnore
	public DocumentLink<AReferenceableEntity> asLink() {
		return new DocumentLink<AReferenceableEntity>(this);
	}
	
	@JsonIgnore
	protected abstract String getLabel();
	
}
