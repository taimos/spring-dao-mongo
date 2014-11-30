package de.taimos.dao.mongo;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import de.taimos.dao.AEntity;

public class TestObject extends AEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private BigDecimal value;
	
	private DateTime dt = DateTime.now();
	
	
	public DateTime getDt() {
		return this.dt;
	}
	
	public void setDt(DateTime dt) {
		this.dt = dt;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getValue() {
		return this.value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "TestObject [name=" + this.name + ", value=" + this.value + ", id=" + this.id + "]";
	}
	
}
