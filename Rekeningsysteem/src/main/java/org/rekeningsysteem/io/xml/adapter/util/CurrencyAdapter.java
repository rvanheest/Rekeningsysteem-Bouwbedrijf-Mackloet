package org.rekeningsysteem.io.xml.adapter.util;

import java.util.Currency;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CurrencyAdapter extends XmlAdapter<String, Currency> {

	@Override
	public Currency unmarshal(String string) {
		return Currency.getInstance(string);
	}

	@Override
	public String marshal(Currency currency) {
		return currency.toString();
	}
}
