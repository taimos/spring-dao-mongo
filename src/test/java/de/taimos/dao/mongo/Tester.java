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
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import de.taimos.dao.JodaMapping;

public class Tester {
	
	public static final TestDAO dao = new TestDAO();
	
	
	@BeforeClass
	public static void init() {
		try {
			System.setProperty("mongodb.name", "spring-dao-mongo");
			Field mongoField = AbstractMongoDAO.class.getDeclaredField("mongo");
			mongoField.setAccessible(true);
			// mongoField.set(Tester.dao, new MongoClient("localhost", 27017));
			mongoField.set(Tester.dao, new Fongo("InMemory").getMongo());
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
		System.out.println(o);
		String id = o.getId();
		
		TestObject save = Tester.dao.save(o);
		System.out.println(save);

		TestObject find = Tester.dao.findById(id);
		Assert.assertNotNull(find);
		System.out.println(find);
		
		find.setName("blubb");
		System.out.println(find);
		
		TestObject save2 = Tester.dao.save(find);
		System.out.println(save2);

		TestObject find3 = Tester.dao.findByName("blubb");
		Assert.assertNotNull(find3);
		System.out.println(find3);
		
		Tester.dao.delete(id);
		
		TestObject find2 = Tester.dao.findById(id);
		Assert.assertNull(find2);

	}

	@Test
	public void serialize() throws Exception {
		TestObject o = new TestObject();
		o.setName("bar");
		o.setValue(new BigDecimal("5"));
		System.out.println(o);

		DBObject dbObject = this.createMapper().getMarshaller().marshall(o).toDBObject();
		System.out.println(dbObject);
		String json = JSON.serialize(dbObject);
		System.out.println(json);

		Object parse = JSON.parse(json);
		System.out.println(parse);
		System.out.println(parse.getClass());
		System.out.println(((DBObject) parse).get("value"));
		System.out.println(((DBObject) parse).get("value").getClass());
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
	
}
