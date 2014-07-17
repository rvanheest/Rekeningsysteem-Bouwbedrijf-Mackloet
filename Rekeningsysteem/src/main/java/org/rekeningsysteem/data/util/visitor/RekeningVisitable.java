package org.rekeningsysteem.data.util.visitor;

public interface RekeningVisitable {

	void accept(RekeningVisitor visitor) throws Exception;
}
