package de.taimos.dao.mongo;

public class TestDAO extends AbstractMongoDAO<TestObject> {

	@Override
	protected void addIndexes() {
		this.addIndex("name", true, true);
	}

	@Override
	protected Class<TestObject> getEntityClass() {
		return TestObject.class;
	}

	public TestObject findByName(String name) {
		return this.findFirstByQuery("{name:#}", null, name);
	}
}
