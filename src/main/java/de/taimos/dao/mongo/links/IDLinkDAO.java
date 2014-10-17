package de.taimos.dao.mongo.links;

public interface IDLinkDAO {
	
	<T extends AReferenceableEntity> T resolve(DocumentLink<T> link);
	
}
