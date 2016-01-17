package org.rekeningsysteem.io.xml.adaptee.reparaties;

import java.util.Currency;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.ItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

@XmlRootElement(name = "reparaties-factuur")
@XmlType(propOrder = { "factuurHeader", "currency", "list" })
public class ReparatiesFactuurAdaptee extends RekeningAdapteeVisitable {

	private FactuurHeader factuurHeader;
	private Currency currency;
	private ItemList<ReparatiesBon> list = new ItemList<>();

	private ReparatiesFactuurAdaptee() {
	}

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.factuurHeader;
	}

	public ReparatiesFactuurAdaptee setFactuurHeader(FactuurHeader factuurHeader) {
		this.factuurHeader = factuurHeader;
		return this;
	}

	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	public Currency getCurrency() {
		return this.currency;
	}

	public ReparatiesFactuurAdaptee setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	@XmlJavaTypeAdapter(ItemListAdapter.class)
	public ItemList<ReparatiesBon> getList() {
		return this.list;
	}

	public ReparatiesFactuurAdaptee setList(ItemList<ReparatiesBon> list) {
		this.list = list;
		return this;
	}

	@Override
	public <T> T accept(RekeningAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static ReparatiesFactuurAdaptee build(
			Function<ReparatiesFactuurAdaptee, ReparatiesFactuurAdaptee> builder) {
		return builder.apply(new ReparatiesFactuurAdaptee());
	}
}
