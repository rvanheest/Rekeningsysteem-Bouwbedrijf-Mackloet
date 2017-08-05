package com.github.rvanheest.rekeningsysteem.model.visitor;

public interface DocumentVoidVisitable {

	void accept(DocumentVoidVisitor visitor) throws Exception;
}
