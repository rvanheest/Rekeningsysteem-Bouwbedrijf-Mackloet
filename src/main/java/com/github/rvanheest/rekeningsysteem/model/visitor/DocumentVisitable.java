package com.github.rvanheest.rekeningsysteem.model.visitor;

public interface DocumentVisitable {

	<T> T accept(DocumentVisitor<T> visitor) throws Exception;
}
