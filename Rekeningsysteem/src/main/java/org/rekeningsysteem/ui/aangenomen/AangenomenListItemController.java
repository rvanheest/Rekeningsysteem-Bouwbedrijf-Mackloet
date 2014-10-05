package org.rekeningsysteem.ui.aangenomen;

import java.util.Optional;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;

import com.google.inject.Inject;

import rx.Observable;
import rx.Observer;

public class AangenomenListItemController implements Observer<AangenomenListItem> {

	private AangenomenListItemPane ui;
	private Observable<Optional<AangenomenListItem>> model;

	@Inject
	public AangenomenListItemController(AangenomenListItemPane ui) {
		this.ui = ui;

		Observable<Optional<AangenomenListItem>> item = Observable.combineLatest(
				this.ui.getOmschrijving(), this.ui.getLoon().map(Geld::new),
				this.ui.getMateriaal().map(Geld::new), AangenomenListItem::new)
				.sample(this.ui.getAddButtonEvent())
				.map(Optional::of);
		Observable<Optional<AangenomenListItem>> cancel = this.ui.getCancelButtonEvent()
				.map(event -> Optional.empty());

		this.model = Observable.merge(item, cancel).first();
	}

	public AangenomenListItemPane getUI() {
		return this.ui;
	}

	public Observable<Optional<AangenomenListItem>> getModel() {
		return this.model;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(AangenomenListItem item) {
		this.ui.setOmschrijving(item.getOmschrijving());
		this.ui.setLoon(item.getLoon().getBedrag());
		this.ui.setMateriaal(item.getMateriaal().getBedrag());
	}
}
