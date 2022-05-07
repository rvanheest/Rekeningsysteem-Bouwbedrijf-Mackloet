package org.rekeningsysteem.test.integration;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.Loon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.Materiaal;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ParticulierFactuurTwoMetVerlegdBtwIntegrationTest extends AbstractIntegrationTest {

	protected List<Materiaal> materiaalArtikels() {
		List<Materiaal> list = new ArrayList<>();

		EsselinkArtikel sub1 = new EsselinkArtikel("2018021117", "Product 1", 1, "Zak", new Geld(5.16));
		EsselinkArtikel sub2 = new EsselinkArtikel("2003131360", "Product 2", 1, "zak", new Geld(129.53));
		EsselinkArtikel sub3 = new EsselinkArtikel("2003131060", "Product 3", 1, "set", new Geld(35.96));
		EsselinkArtikel sub4 = new EsselinkArtikel("2003131306", "Product 4", 1, "zak", new Geld(9.47));
		EsselinkArtikel sub5 = new EsselinkArtikel("4010272112", "Product 5", 1, "Stuks", new Geld(17.18));
		EsselinkArtikel sub6 = new EsselinkArtikel("2009200131", "Product 6", 1, "Stuks", new Geld(6.84));
		EsselinkArtikel sub7 = new EsselinkArtikel("2009200105", "Product 7", 1, "Stuks", new Geld(7.44));

		list.add(new GebruiktEsselinkArtikel(sub1, 8, new BtwPercentage(21, false)));
		list.add(new GebruiktEsselinkArtikel(sub2, 1, new BtwPercentage(21, false)));
		list.add(new GebruiktEsselinkArtikel(sub3, 1, new BtwPercentage(21, true)));
		list.add(new GebruiktEsselinkArtikel(sub4, 1, new BtwPercentage(21, false)));
		list.add(new GebruiktEsselinkArtikel(sub5, 1, new BtwPercentage(21, false)));
		list.add(new GebruiktEsselinkArtikel(sub6, 1, new BtwPercentage(21, false)));
		list.add(new GebruiktEsselinkArtikel(sub7, 1, new BtwPercentage(21, false)));
		list.add(new AnderArtikel("Stucloper + trapfolie", new Geld(15.00), new BtwPercentage(21, false)));
		list.add(new AnderArtikel("Kitwerk", new Geld(149.50), new BtwPercentage(21, true)));

		return list;
	}

	protected List<Loon> loonArtikels() {
		List<Loon> list = new ArrayList<>();

		list.add(new ProductLoon("Uurloon à 38.50", 25, new Geld(38.50), new BtwPercentage(6, true)));
		list.add(new ProductLoon("test123", 12, new Geld(12.50), new BtwPercentage(6, false)));
		list.add(new InstantLoon("foobar", new Geld(40.00), new BtwPercentage(6, false)));

		return list;
	}

	@Override
	protected ParticulierFactuur makeDocument() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place");
		LocalDate datum = LocalDate.of(2011, 4, 2);
		String factuurnummer = "22011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);
		String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie badkamervloer i.v.m. lekkage";
		ItemList<ParticulierArtikel> itemList = new ItemList<>(Currency.getInstance("EUR"), Stream.of(
				this.materiaalArtikels(),
				this.loonArtikels()
		).flatMap(Collection::stream).collect(Collectors.toList()));

		return new ParticulierFactuur(header, omschrijving, itemList);
	}

	@Override
	protected Path pdfFile() {
		return Paths.get("src", "test", "resources", "pdf", "ParticulierFactuurTwoMetVerlegdBtwIntegrationTest.pdf").toAbsolutePath();
	}

	@Override
	protected Path xmlFile() {
		return Paths.get("src", "test", "resources", "xml", "ParticulierFactuurTwoMetVerlegdBtwIntegrationTest.pdf").toAbsolutePath();
	}
}
