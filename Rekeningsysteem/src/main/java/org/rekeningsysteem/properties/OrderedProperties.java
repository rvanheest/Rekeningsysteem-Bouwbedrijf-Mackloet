package org.rekeningsysteem.properties;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class OrderedProperties extends Properties {

	private final Vector<Object> keys;

	public OrderedProperties() {
		super();
		this.keys = new Vector<>();
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		if (!this.keys.contains(key)) {
			this.keys.add(key);
		}
		return super.put(key, value);
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return this.keys.elements();
	}

	@Override
	public Enumeration<?> propertyNames() {
		return this.keys();
	}
}
