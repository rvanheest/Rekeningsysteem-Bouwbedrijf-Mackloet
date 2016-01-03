package org.rekeningsysteem.io.xml.adapter.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.exception.GeldParseException;

public class GeldAdapter extends XmlAdapter<String, Geld> {

	@Override
	public Geld unmarshal(String string) throws GeldParseException {
		return new Geld(string);
	}

	@Override
	public String marshal(Geld geld) {
		return geld.formattedString();
	}
}
