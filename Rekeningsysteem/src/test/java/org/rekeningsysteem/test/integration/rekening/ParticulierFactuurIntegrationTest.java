package org.rekeningsysteem.test.integration.rekening;

import java.io.File;
import java.time.LocalDate;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;

public class ParticulierFactuurIntegrationTest extends AbstractRekeningIntegrationTest {

	protected ItemList<ParticulierArtikel> addArtikels() {
		ItemList<ParticulierArtikel> list = new ItemList<>();
		
		EsselinkArtikel sub1 = new EsselinkArtikel("2018021117", "Product 1", 1, "Zak",
				new Geld(5.16));
		EsselinkArtikel sub2 = new EsselinkArtikel("2003131360", "Product 2", 1, "zak",
				new Geld(129.53));
		EsselinkArtikel sub3 = new EsselinkArtikel("2003131060", "Product 3", 1, "set",
				new Geld(35.96));
		EsselinkArtikel sub4 = new EsselinkArtikel("2003131306", "Product 4", 1, "zak",
				new Geld(9.47));
		EsselinkArtikel sub5 = new EsselinkArtikel("4010272112", "Product 5", 1, "Stuks",
				new Geld(17.18));
		EsselinkArtikel sub6 = new EsselinkArtikel("2009200131", "Product 6", 1, "Stuks",
				new Geld(6.84));
		EsselinkArtikel sub7 = new EsselinkArtikel("2009200105", "Product 7", 1, "Stuks",
				new Geld(7.44));

		list.add(new GebruiktEsselinkArtikel(sub1, 8));
		list.add(new GebruiktEsselinkArtikel(sub2, 1));
		list.add(new GebruiktEsselinkArtikel(sub3, 1));
		list.add(new GebruiktEsselinkArtikel(sub4, 1));
		list.add(new GebruiktEsselinkArtikel(sub5, 1));
		list.add(new GebruiktEsselinkArtikel(sub6, 1));
		list.add(new GebruiktEsselinkArtikel(sub7, 1));
		list.add(new AnderArtikel("Stucloper + trapfolie", new Geld(15.00)));
		list.add(new AnderArtikel("Kitwerk", new Geld(149.50)));
		
		return list;
	}

	protected ItemList<AbstractLoon> addLoon() {
		ItemList<AbstractLoon> list = new ItemList<>();
		
		list.add(new ProductLoon("Uurloon à 38.50", 25, new Geld(38.50)));
		list.add(new ProductLoon("test123", 12, new Geld(12.50)));
		list.add(new InstantLoon("foobar", new Geld(40.00)));
		
		return list;
	}

	@Override
	protected ParticulierFactuur makeRekening() {
			Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
					"Place");
			LocalDate datum = LocalDate.of(2011, 4, 2);
			String factuurnummer = "22011";
			String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie "
					+ "badkamervloer i.v.m. lekkage";
			OmschrFactuurHeader header = new OmschrFactuurHeader(debiteur, datum, factuurnummer,
					omschrijving);

			BtwPercentage btwPercentage = new BtwPercentage(6, 21);
			ItemList<ParticulierArtikel> itemList = this.addArtikels();
			ItemList<AbstractLoon> loonList = this.addLoon();

			return new ParticulierFactuur(header, "dollar", itemList, loonList, btwPercentage);
	}

	@Override
	protected File makeFile() {
		return new File("particulierFactuurXMLTest.xml");
	}
}
