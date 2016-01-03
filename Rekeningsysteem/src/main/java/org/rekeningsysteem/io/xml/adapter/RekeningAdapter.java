package org.rekeningsysteem.io.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;

public class RekeningAdapter extends XmlAdapter<RekeningAdapteeVisitable, AbstractRekening> {

	private final RekeningAdapteeVisitor<AbstractRekening> unmarshalVisitor = new RekeningUnmarshaller();
	private final RekeningVisitor<RekeningAdapteeVisitable> marshalVisitor = new RekeningMarshaller();

	@Override
	public AbstractRekening unmarshal(RekeningAdapteeVisitable adaptee) {
		return adaptee.accept(this.unmarshalVisitor);
	}

	@Override
	public RekeningAdapteeVisitable marshal(AbstractRekening rekening) throws Exception {
		return rekening.accept(this.marshalVisitor);
	}
}
