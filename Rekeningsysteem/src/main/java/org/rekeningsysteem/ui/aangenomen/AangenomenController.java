package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;

import org.rekeningsysteem.application.working.AbstractRekeningController;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.list.ListPaneController;

import rx.Observable;

import com.google.inject.Inject;

public class AangenomenController extends AbstractRekeningController {

	private final Observable<AangenomenFactuur> model;

	@Inject
	public AangenomenController(OmschrFactuurHeaderController header, ListPaneController body,
			Currency currency) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()));
		this.model = Observable.combineLatest(header.getModel(), body.getListModel(),
				body.getBtwModel(),
				(head, list, btw) -> new AangenomenFactuur(head, currency, list, btw));
	}

	public Observable<AangenomenFactuur> getModel() {
		return this.model;
	}
}
