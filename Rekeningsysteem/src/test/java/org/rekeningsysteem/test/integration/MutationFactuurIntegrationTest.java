package org.rekeningsysteem.test.integration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Currency;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;

@RunWith(MockitoJUnitRunner.class)
public class MutationFactuurIntegrationTest extends AbstractIntegrationTest {

	@Override
	protected MutatiesFactuur makeRekening() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 5, 9);
		String factuurnummer = "272011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("Inkooporder", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesInkoopOrder("Inkooporder", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesInkoopOrder("Inkooporder", "112308", new Geld(5510.74)));

		return new MutatiesFactuur(header, Currency.getInstance("EUR"), itemList);
	}

	@Override
	protected Path pdfFile() {
		return Paths.get("src", "test", "resources", "pdf", "MutationFactuurIntegrationTest.pdf").toAbsolutePath();
	}

	@Override
	protected Path xmlFile() {
		return Paths.get("src", "test", "resources", "xml", "MutationFactuurIntegrationTest.xml").toAbsolutePath();
	}
}
