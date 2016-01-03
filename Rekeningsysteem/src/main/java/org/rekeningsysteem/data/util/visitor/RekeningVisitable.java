package org.rekeningsysteem.data.util.visitor;

public interface RekeningVisitable {

	<T> T accept(RekeningVisitor<T> visitor) throws Exception;
}
