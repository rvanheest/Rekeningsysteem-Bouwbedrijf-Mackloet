package org.rekeningsysteem.io.xml.adaptee.mutaties;

import java.util.Currency;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.ItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

@XmlRootElement(name = "mutaties-factuur")
@XmlType(propOrder = { "factuurHeader", "currency", "list" })
public class MutatiesFactuurAdaptee extends RekeningAdapteeVisitable {

	private FactuurHeader factuurHeader;
	private Currency currency;
	private ItemList<MutatiesInkoopOrder> list = new ItemList<>();

	private MutatiesFactuurAdaptee() {
	}

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.factuurHeader;
	}

	public MutatiesFactuurAdaptee setFactuurHeader(FactuurHeader factuurHeader) {
		this.factuurHeader = factuurHeader;
		return this;
	}

	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	public Currency getCurrency() {
		return this.currency;
	}

	public MutatiesFactuurAdaptee setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	@XmlJavaTypeAdapter(ItemListAdapter.class)
	public ItemList<MutatiesInkoopOrder> getList() {
		return this.list;
	}

	public MutatiesFactuurAdaptee setList(ItemList<MutatiesInkoopOrder> list) {
		this.list = list;
		return this;
	}

	@Override
	public <T> T accept(RekeningAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static MutatiesFactuurAdaptee build(
			Function<MutatiesFactuurAdaptee, MutatiesFactuurAdaptee> builder) {
		return builder.apply(new MutatiesFactuurAdaptee());
	}
}
