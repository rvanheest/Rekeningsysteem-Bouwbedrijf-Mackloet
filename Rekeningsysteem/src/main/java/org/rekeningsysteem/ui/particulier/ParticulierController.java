package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.particulier.loon.LoonListPaneController;

import rx.Observable;

public class ParticulierController extends AbstractRekeningController<ParticulierFactuur> {

	private final OmschrFactuurHeaderController headerController;

	public ParticulierController() {
		this(Currency.getInstance("EUR"), new BtwPercentage(6.0, 21.0));
	}

	public ParticulierController(Currency currency, BtwPercentage btw) {
		this(new OmschrFactuurHeaderController(), new ParticulierListPaneController(currency, btw),
				new LoonListPaneController(currency));
	}

	public ParticulierController(ParticulierFactuur input) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader()),
				new ParticulierListPaneController(input.getCurrency(), input.getItemList(), input.getBtwPercentage()),
				new LoonListPaneController(input.getCurrency(), input.getLoonList()));
	}

	public ParticulierController(OmschrFactuurHeaderController header,
			ParticulierListPaneController body, LoonListPaneController loon) {
		super(new RekeningSplitPane(header.getUI(), body.getUI(), loon.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						loon.getModel(), body.getBtwModel(),
						(head, list, loonList, btw) -> new ParticulierFactuur(head, body.getCurrency(), list, loonList, btw)));
		this.headerController = header;
	}

	@Override
	public void initFactuurnummer() {
		String factuurnummer = this.getFactuurnummerFactory()
				.create(PropertyModelEnum.FACTUURNUMMER)
				.getFactuurnummer();
		this.headerController.initFactuurnummer(Optional.ofNullable(factuurnummer));
	}
}
