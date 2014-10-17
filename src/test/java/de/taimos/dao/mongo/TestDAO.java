package de.taimos.dao.mongo;

public class TestDAO extends AbstractMongoDAO<TestObject> {
	
	@Override
	protected Class<TestObject> getEntityClass() {
		return TestObject.class;
	}
	
	public TestObject findByName(String name) {
		return this.findFirstByQuery("{name:#}", null, name);
	}
}
