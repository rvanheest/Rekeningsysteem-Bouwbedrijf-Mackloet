package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;

import rx.Observable;

public class MutatiesController extends AbstractRekeningController<MutatiesFactuur> {

	private final FactuurHeaderController headerController;

	public MutatiesController() {
		this(Currency.getInstance("EUR"), new BtwPercentage(0.0, 0.0));
	}

	public MutatiesController(Currency currency, BtwPercentage btw) {
		this(new FactuurHeaderController(), new MutatiesListPaneController(currency, btw));
	}

	public MutatiesController(MutatiesFactuur input) {
		this(new FactuurHeaderController(input.getFactuurHeader()),
				new MutatiesListPaneController(input.getCurrency(), input.getItemList(),
						input.getBtwPercentage()));
	}

	public MutatiesController(FactuurHeaderController header,
			MutatiesListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
				body.getBtwModel(),
				(head, list, btw) -> new MutatiesFactuur(head, body.getCurrency(), list, btw)));
		this.headerController = header;
	}

	@Override
	public void initFactuurnummer() {
		String factuurnummer = this.getFactuurnummerFactory()
				.call(PropertyModelEnum.FACTUURNUMMER)
				.getFactuurnummer();
		this.headerController.initFactuurnummer(Optional.ofNullable(factuurnummer));
	}
}
