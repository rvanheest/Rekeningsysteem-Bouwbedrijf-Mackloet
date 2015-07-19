package org.rekeningsysteem.io.database;

@FunctionalInterface
public interface QueryEnumeration {

	String getQuery();
	
	default QueryEnumeration append(QueryEnumeration other) {
		return () -> this.getQuery() + "\n" + other.getQuery();
	}
}
