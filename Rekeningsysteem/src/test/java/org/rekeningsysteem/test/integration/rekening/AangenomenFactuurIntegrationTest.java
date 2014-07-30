package org.rekeningsysteem.test.integration.rekening;

import java.io.File;
import java.time.LocalDate;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;

public class AangenomenFactuurIntegrationTest extends AbstractRekeningIntegrationTest {

	@Override
	protected AangenomenFactuur makeRekening() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number",
				"Zipcode", "Place");
		LocalDate datum = LocalDate.of(2013, 4, 5);
		String factuurnummer = "122013";
		String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie badkamer "
				+ "volgens offertenr. 22012";
		OmschrFactuurHeader header = new OmschrFactuurHeader(debiteur, datum, factuurnummer,
				omschrijving);

		AangenomenListItem item1 = new AangenomenListItem("Hoofdaannemer",
				new Geld(5183.75), new Geld(2791.25));
		AangenomenListItem item2 = new AangenomenListItem("Onderaannemer 1",
				new Geld(1314.80), new Geld(1972.20));
		AangenomenListItem item3 = new AangenomenListItem("Onderaannemer 2",
				new Geld(2300.00), new Geld(5667.00));
		AangenomenListItem item4 = new AangenomenListItem(
				"In mindering gebracht 16 uur arbeid + container", new Geld(-800.00),
				new Geld(0.0));

		BtwPercentage btwPercentage = new BtwPercentage(6, 21);
		ItemList<AangenomenListItem> itemList = new ItemList<>();
		itemList.add(item1);
		itemList.add(item2);
		itemList.add(item3);
		itemList.add(item4);

		AangenomenFactuur factuur = new AangenomenFactuur(header, "euro", itemList,
				btwPercentage);

		return factuur;
	}

	@Override
	protected File makeFile() {
		return new File("aangenomenFactuurXMLTest.xml");
	}

//	@Test
//	public void testMakeInitFactuurWithGuice() {
//		Injector injector = Guice.createInjector(new AangenomenFactuurModule(),
//				new ConfigPropertiesModule(), new ConsoleLoggerModule());
//		injector.getInstance(AangenomenFactuur.class);
//	}
}
