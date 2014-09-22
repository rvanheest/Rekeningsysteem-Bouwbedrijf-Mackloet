package org.rekeningsysteem.test.integration.pdf;

import java.io.File;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.guice.PdfExporterModule;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class OffertePdfIntegrationTest {

	private FactuurSaver exporter;

	protected String makeText() {
		return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce quis quam tortor. "
				+ "Nunc id leo in eros tincidunt lobortis eget a erat. Mauris id velit ut diam "
				+ "tincidunt auctor ut sed sapien. Morbi in tellus ut sapien molestie sodales "
				+ "vitae molestie libero. Sed sodales augue nulla, ut pulvinar odio placerat eu. "
				+ "Nunc at arcu euismod, pulvinar orci nec, fermentum tellus. In hac habitasse "
				+ "platea dictumst. Interdum et malesuada fames ac ante ipsum primis in faucibus. "
				+ "Nullam eu sagittis libero, quis pretium tortor. Aliquam at placerat dolor. "
				+ "Nunc fringilla quam venenatis lacus condimentum, vitae mattis risus "
				+ "ullamcorper. Maecenas pulvinar gravida libero, ac scelerisque justo "
				+ "dignissim in.\n\n"
				
				+ "Maecenas pretium mi id magna convallis, vel auctor erat pulvinar. Cras et "
				+ "tellus nec lacus pellentesque rutrum eget sodales nisi. In vitae sagittis "
				+ "urna. Sed vestibulum suscipit vulputate. Vivamus commodo augue at dolor "
				+ "volutpat blandit. Nunc in justo bibendum, cursus purus quis, scelerisque est. "
				+ "Aenean ut accumsan arcu.\n\n"
				
				+ "Cras volutpat auctor mollis. Sed aliquam elit et accumsan dictum. Pellentesque "
				+ "sollicitudin, turpis sollicitudin tincidunt porttitor, velit quam mollis sem, "
				+ "non volutpat nunc arcu quis magna. Fusce vitae mattis nisl. Nulla sit amet "
				+ "mollis neque, et hendrerit eros. Cras id risus adipiscing, sollicitudin odio "
				+ "non, mollis metus. Pellentesque vulputate tempus nibh a ultricies. Quisque "
				+ "ullamcorper dictum velit, sit amet pellentesque risus facilisis vel.";
	}

	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new PdfExporterModule(),
				new ConfigPropertiesModule(), new ConsoleLoggerModule());
		this.exporter = injector.getInstance(FactuurSaver.class);
	}

	@Test
	public void testExportWithOndertekenen() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place");
		LocalDate datum = LocalDate.of(2011, 8, 11);
		String factuurnummer = "107";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);
		Offerte offerte = new Offerte(header, this.makeText(), true);
		
		this.exporter.save(offerte, new File("src\\test\\resources\\pdf\\"
				+ "OfferteTest123True.pdf"));
	}

	@Test
	public void testExportWithoutOndertekenen() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place");
		LocalDate datum = LocalDate.of(2011, 8, 11);
		String factuurnummer = "107";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);
		Offerte offerte = new Offerte(header, this.makeText(), false);
		
		this.exporter.save(offerte, new File("src\\test\\resources\\pdf\\"
				+ "OfferteTest123False.pdf"));
	}
}