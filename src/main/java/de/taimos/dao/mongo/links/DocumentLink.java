package de.taimos.dao.mongo.links;

/**
 * Link to another document of a {@link AReferenceableEntity}<br>
 * It is stored as an object containing the target class, the objectid of the target and a label to avoid joining the document for display
 * purpose.
 * 
 * @author Thorsten Hoeger
 *
 * @param <T> the target type
 */
public class DocumentLink<T extends AReferenceableEntity<T>> {
	
	private Class<T> targetClass;
	private String objectId;
	private String label;
	
	
	public DocumentLink() {
		//
	}
	
	@SuppressWarnings("unchecked")
	public DocumentLink(T object) {
		this((Class<T>) object.getClass(), object.getId(), object.getLabel());
	}
	
	public DocumentLink(Class<T> targetClass, String objectId, String label) {
		this.targetClass = targetClass;
		this.objectId = objectId;
		this.label = label;
	}
	
	public Class<T> getTargetClass() {
		return this.targetClass;
	}
	
	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
	
	public String getObjectId() {
		return this.objectId;
	}
	
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return String.format("%s [%s@%s]", this.label, this.objectId, this.targetClass.getSimpleName());
	}
	
}
