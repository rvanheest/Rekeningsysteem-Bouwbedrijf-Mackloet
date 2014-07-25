package org.rekeningsysteem.io.xml.adapter.util.header;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.io.xml.adaptee.util.header.DatumAdaptee;

public class DatumAdapter extends XmlAdapter<DatumAdaptee, LocalDate> {

	@Override
	public LocalDate unmarshal(DatumAdaptee adaptee) {
		return LocalDate.of(adaptee.getJaar(), adaptee.getMaand(), adaptee.getDag());
	}

	@Override
	public DatumAdaptee marshal(LocalDate datum) {
		DatumAdaptee adaptee = new DatumAdaptee();
		adaptee.setDag(datum.getDayOfMonth());
		adaptee.setMaand(datum.getMonthValue());
		adaptee.setJaar(datum.getYear());
		return adaptee;
	}
}
