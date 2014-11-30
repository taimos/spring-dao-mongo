package de.taimos.dao.mongo;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.JacksonMapper.Builder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.fakemongo.Fongo;
import com.github.mongobee.Mongobee;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import de.taimos.dao.JodaMapping;

public class Tester {
	
	private static final String dbName = "spring-dao-mongo";
	
	public static final TestDAO dao = new TestDAO();
	
	// public static final Mongo mongo = new MongoClient("localhost", 27017));
	public static final Mongo mongo = new Fongo("InMemory").getMongo();
	
	
	@BeforeClass
	public static void init() {
		try {
			System.setProperty("mongodb.name", Tester.dbName);
			Field mongoField = AbstractMongoDAO.class.getDeclaredField("mongo");
			mongoField.setAccessible(true);
			mongoField.set(Tester.dao, Tester.mongo);
			
			Mongobee bee = new Mongobee(Tester.mongo);
			bee.setChangeLogsScanPackage("de.taimos.dao.mongo.changelog");
			bee.setDbName(Tester.dbName);
			bee.setEnabled(true);
			bee.execute();
			Tester.dao.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdate() {
		TestObject o = new TestObject();
		o.setName("bar");
		o.setValue(new BigDecimal("5"));
		Assert.assertEquals("bar", o.getName());
		String id = o.getId();
		
		TestObject save = Tester.dao.save(o);
		Assert.assertEquals("bar", save.getName());
		Tester.assertEquals(new BigDecimal("5"), save.getValue());
		Assert.assertNotNull(save.getId());
		Assert.assertNotNull(save.getDt());
		
		TestObject find = Tester.dao.findById(id);
		Assert.assertNotNull(find);
		Assert.assertEquals("bar", find.getName());
		Tester.assertEquals(new BigDecimal("5"), find.getValue());
		Assert.assertEquals(id, find.getId());
		Assert.assertNotNull(find.getDt());
		
		find.setName("blubb");
		
		TestObject save2 = Tester.dao.save(find);
		Assert.assertNotNull(save2);
		Assert.assertEquals("blubb", save2.getName());
		Tester.assertEquals(new BigDecimal("5"), save2.getValue());
		Assert.assertEquals(id, save2.getId());
		Assert.assertNotNull(save2.getDt());
		
		TestObject find3 = Tester.dao.findByName("blubb");
		Assert.assertNotNull(find3);
		Assert.assertEquals("blubb", find3.getName());
		Tester.assertEquals(new BigDecimal("5"), find3.getValue());
		Assert.assertEquals(id, find3.getId());
		Assert.assertNotNull(find3.getDt());
		
		Tester.dao.delete(id);
		
		TestObject find2 = Tester.dao.findById(id);
		Assert.assertNull(find2);
		
		System.out.println(Tester.mongo.getDB(Tester.dbName).getCollection("TestObject").getIndexInfo());
	}
	
	@Test
	public void serialize() throws Exception {
		TestObject o = new TestObject();
		o.setName("bar");
		o.setValue(new BigDecimal("5"));
		Assert.assertEquals("bar", o.getName());
		
		DBObject dbObject = this.createMapper().getMarshaller().marshall(o).toDBObject();
		System.out.println(dbObject);
		String json = JSON.serialize(dbObject);
		System.out.println(json);
		
		Object parse = JSON.parse(json);
		System.out.println(parse);
		Assert.assertEquals(BasicDBObject.class, parse.getClass());
		Assert.assertEquals(Double.class, ((DBObject) parse).get("value").getClass());
		Assert.assertEquals(5.0D, (double) ((DBObject) parse).get("value"), 0);
	}
	
	protected Mapper createMapper() {
		Builder builder = new JacksonMapper.Builder();
		builder.enable(MapperFeature.AUTO_DETECT_GETTERS);
		builder.addSerializer(DateTime.class, new JodaMapping.MongoDateTimeSerializer());
		builder.addDeserializer(DateTime.class, new JodaMapping.MongoDateTimeDeserializer());
		builder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return builder.build();
	}
	
	private static void assertEquals(BigDecimal bd1, BigDecimal bd2) {
		Assert.assertEquals(bd1.doubleValue(), bd2.doubleValue(), 0);
	}
	
}
