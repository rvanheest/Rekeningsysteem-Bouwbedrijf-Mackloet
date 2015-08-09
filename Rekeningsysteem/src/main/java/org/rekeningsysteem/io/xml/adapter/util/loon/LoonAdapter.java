package org.rekeningsysteem.io.xml.adapter.util.loon;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.io.xml.adaptee.util.loon.AbstractLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.util.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.util.loon.ProductLoonAdaptee;

public class LoonAdapter extends XmlAdapter<AbstractLoonAdaptee, AbstractLoon> {

	@Override
	public AbstractLoon unmarshal(AbstractLoonAdaptee adaptee) {
		if (adaptee instanceof InstantLoonAdaptee) {
			return this.unmarshall((InstantLoonAdaptee) adaptee);
		}
		else if (adaptee instanceof ProductLoonAdaptee) {
			return this.unmarshall((ProductLoonAdaptee) adaptee);
		}
		return null;
	}

	private InstantLoon unmarshall(InstantLoonAdaptee adaptee) {
		return new InstantLoon(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getLoonBtwPercentage());
	}

	private ProductLoon unmarshall(ProductLoonAdaptee adaptee) {
		return new ProductLoon(adaptee.getOmschrijving(), adaptee.getUren(), adaptee.getUurloon(),
				adaptee.getLoonBtwPercentage());
	}

	@Override
	public AbstractLoonAdaptee marshal(AbstractLoon loon) {
		if (loon instanceof InstantLoon) {
			return this.marshal((InstantLoon) loon);
		}
		else if (loon instanceof ProductLoon) {
			return this.marshal((ProductLoon) loon);
		}
		return null;
	}

	private InstantLoonAdaptee marshal(InstantLoon loon) {
		InstantLoonAdaptee adaptee = new InstantLoonAdaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setLoon(loon.getLoon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());

		return adaptee;
	}

	private ProductLoonAdaptee marshal(ProductLoon loon) {
		ProductLoonAdaptee adaptee = new ProductLoonAdaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setUren(loon.getUren());
		adaptee.setUurloon(loon.getUurloon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());
		return adaptee;
	}
}
