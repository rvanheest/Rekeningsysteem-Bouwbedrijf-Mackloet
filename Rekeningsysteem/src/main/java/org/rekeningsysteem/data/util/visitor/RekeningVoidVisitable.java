package org.rekeningsysteem.data.util.visitor;

public interface RekeningVoidVisitable {

	void accept(RekeningVoidVisitor visitor) throws Exception;
}
