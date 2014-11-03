package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;

import rx.Observable;

public class AangenomenController extends AbstractRekeningController {

	private final Observable<AangenomenFactuur> model;
	private final OmschrFactuurHeaderController headerController;

	public AangenomenController() {
		this(Currency.getInstance("EUR"));
	}

	public AangenomenController(Currency currency) {
		this(new OmschrFactuurHeaderController(), new AangenomenListPaneController(currency));
	}

	public AangenomenController(AangenomenFactuur input) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader()),
				new AangenomenListPaneController(input.getCurrency(), input.getItemList(),
						input.getBtwPercentage()));
	}

	public AangenomenController(OmschrFactuurHeaderController header,
			AangenomenListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()));
		this.model = Observable.combineLatest(header.getModel(), body.getListModel(),
				body.getBtwModel(),
				(head, list, btw) -> new AangenomenFactuur(head, body.getCurrency(), list, btw));
		this.headerController = header;
	}

	public Observable<AangenomenFactuur> getModel() {
		return this.model;
	}

	@Override
	public void initFactuurnummer() {
		String factuurnummer = this.getFactuurnummerFactory()
				.create(PropertyModelEnum.FACTUURNUMMER)
				.getFactuurnummer();
		this.headerController.initFactuurnummer(Optional.ofNullable(factuurnummer));
	}
}
