package org.rekeningsysteem.test.integration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Currency;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;

@RunWith(MockitoJUnitRunner.class)
public class ReparationFactuurIntegrationTest extends AbstractIntegrationTest {

	protected void addOrders(ItemList<ReparatiesInkoopOrder> list) {
		list.add(new ReparatiesInkoopOrder("Inkooporder", "110543", new Geld(77.00), new Geld(6.50)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111558", new Geld(77.00), new Geld(9.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111518", new Geld(57.75), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111660", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111563", new Geld(115.50), new Geld(13.50)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111625", new Geld(57.75), new Geld(15.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111764", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111751", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111745", new Geld(38.50), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111571", new Geld(57.75), new Geld(3.50)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111876", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111716", new Geld(154.00), new Geld(7.50)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111854", new Geld(154.00), new Geld(183.50)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111912", new Geld(38.50), new Geld(9.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111940", new Geld(154.00), new Geld(9.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111928", new Geld(77.00), new Geld(4.50)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111723", new Geld(115.50), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111963", new Geld(299.26), new Geld(448.88)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111739", new Geld(408.16), new Geld(136.52)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111091", new Geld(1451.27), new Geld(967.51)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111409", new Geld(2546.57), new Geld(1697.72)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111272", new Geld(3630.66), new Geld(2420.44)));
		list.add(new ReparatiesInkoopOrder("Inkooporder", "111148", new Geld(3878.20), new Geld(2585.46)));
	}

	@Override
	protected ReparatiesFactuur makeRekening() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 4, 5);
		String factuurnummer = "232011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		ItemList<ReparatiesInkoopOrder> itemList = new ItemList<>(Currency.getInstance("EUR"));
		this.addOrders(itemList);
		this.addOrders(itemList);

		return new ReparatiesFactuur(header, itemList);
	}

	@Override
	protected Path pdfFile() {
		return Paths.get("src", "test", "resources", "pdf", "ReparationFactuurIntegrationTest.pdf").toAbsolutePath();
	}

	@Override
	protected Path xmlFile() {
		return Paths.get("src", "test", "resources", "xml", "ReparationFactuurIntegrationTest.pdf").toAbsolutePath();
	}
}
