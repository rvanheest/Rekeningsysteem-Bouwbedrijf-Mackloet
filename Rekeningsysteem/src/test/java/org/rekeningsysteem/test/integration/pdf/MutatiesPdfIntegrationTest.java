package org.rekeningsysteem.test.integration.pdf;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.pdf.PdfExporter;

@RunWith(MockitoJUnitRunner.class)
public class MutatiesPdfIntegrationTest {

	private PdfExporter exporter;
	@Mock private Logger logger;

	@Before
	public void setUp() {
		this.exporter = new PdfExporter(false, this.logger);
	}

	@Test
	public void testExport() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 5, 9);
		String factuurnummer = "272011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		ItemList<MutatiesBon> itemList = new ItemList<>();
		itemList.add(new MutatiesBon("Bonnummer", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesBon("Bonnummer", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesBon("Bonnummer", "112308", new Geld(5510.74)));

		MutatiesFactuur factuur = new MutatiesFactuur(header, Currency.getInstance("EUR"), itemList);
		this.exporter.export(factuur, new File("src\\test\\resources\\pdf\\"
				+ "MutatiesFactuurTest123.pdf"));

		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportWithError() throws Exception {
		AbstractRekening rekening = mock(AbstractRekening.class);
		File file = mock(File.class);

		doThrow(Exception.class).when(rekening).accept(anyObject());

		this.exporter.export(rekening, file);

		verify(this.logger).error(anyString(), any(Exception.class));
	}
}
