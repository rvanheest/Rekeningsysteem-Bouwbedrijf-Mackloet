package org.rekeningsysteem.test.integration.pdf;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.pdf.guice.PdfExporterModule;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MutatiesPdfIntegrationTest {

	private PdfExporter exporter;

	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new PdfExporterModule(),
				new ConfigPropertiesModule(), new ConsoleLoggerModule());
		this.exporter = injector.getInstance(PdfExporter.class);
	}

	@Test
	public void testExport() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 5, 9);
		String factuurnummer = "272011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		BtwPercentage btwPercentage = new BtwPercentage(0, 0);
		ItemList<MutatiesBon> itemList = new ItemList<>();
		itemList.add(new MutatiesBon("Bonnummer", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesBon("Bonnummer", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesBon("Bonnummer", "112308", new Geld(5510.74)));

		MutatiesFactuur factuur = new MutatiesFactuur(header, Currency.getInstance("EUR"), itemList, btwPercentage);
		this.exporter.save(factuur, new File("src\\test\\resources\\pdf\\"
				+ "MutatiesFactuurTest123.pdf"));
	}
}
