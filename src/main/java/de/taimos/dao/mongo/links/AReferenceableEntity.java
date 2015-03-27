package de.taimos.dao.mongo.links;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.taimos.dao.AEntity;

/**
 * extension of {@link AEntity} which is referenceable via DocumentLink
 * 
 * @author Thorsten Hoeger
 *
 * @param <T>
 */
public abstract class AReferenceableEntity<T extends AReferenceableEntity<T>> extends AEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	@JsonIgnore
	@SuppressWarnings("unchecked")
	public DocumentLink<T> asLink() {
		return new DocumentLink<T>((T) this);
	}
	
	@JsonIgnore
	protected abstract String getLabel();
	
}
