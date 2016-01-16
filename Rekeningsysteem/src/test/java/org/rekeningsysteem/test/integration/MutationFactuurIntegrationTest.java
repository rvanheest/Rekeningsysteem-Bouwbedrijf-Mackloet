package org.rekeningsysteem.test.integration;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;

@RunWith(MockitoJUnitRunner.class)
public class MutationFactuurIntegrationTest extends AbstractIntegrationTest {

	@Override
	protected MutatiesFactuur makeRekening() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 5, 9);
		String factuurnummer = "272011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		ItemList<MutatiesBon> itemList = new ItemList<>();
		itemList.add(new MutatiesBon("Bonnummer", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesBon("Bonnummer", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesBon("Bonnummer", "112308", new Geld(5510.74)));

		return new MutatiesFactuur(header, Currency.getInstance("EUR"), itemList);
	}

	@Override
	protected File pdfFile() {
		return new File("src\\test\\resources\\pdf\\MutationFactuurIntegrationTest.pdf");
	}

	@Override
	protected File xmlFile() {
		return new File("src\\test\\resources\\xml\\MutationFactuurIntegrationTest.xml");
	}
}
