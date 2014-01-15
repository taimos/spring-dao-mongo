package de.taimos.dao.mongo;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;

public class Tester {
	
	public static final TestDAO dao = new TestDAO();
	
	
	@BeforeClass
	public static void init() {
		try {
			System.setProperty("mongodb.name", "spring-dao-mongo");
			Field mongoField = AbstractMongoDAO.class.getDeclaredField("mongo");
			mongoField.setAccessible(true);
			mongoField.set(Tester.dao, new MongoClient("localhost", 27017));
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
		
		Tester.dao.delete(id);
		
		TestObject find2 = Tester.dao.findById(id);
		Assert.assertNull(find2);
	}
	
}
