package de.taimos.dao.mongo.links;

/**
 * DAO to generically resolve DLinks
 */
public interface IDLinkDAO {
	
	<T extends AReferenceableEntity<T>> T resolve(DocumentLink<T> link);
	
}
