package de.taimos.dao.mongo;

import java.util.Iterator;

import com.mongodb.DBObject;

final class ConverterIterable<T> implements Iterable<T> {
	
	private final Iterator<DBObject> iterator;
	private final IObjectConverter<T> conv;
	
	
	ConverterIterable(Iterator<DBObject> iterator, IObjectConverter<T> conv) {
		this.iterator = iterator;
		this.conv = conv;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			@Override
			public void remove() {
				ConverterIterable.this.iterator.remove();
			}
			
			@Override
			public T next() {
				return ConverterIterable.this.conv.convert(ConverterIterable.this.iterator.next());
			}
			
			@Override
			public boolean hasNext() {
				return ConverterIterable.this.iterator.hasNext();
			}
		};
	}
}