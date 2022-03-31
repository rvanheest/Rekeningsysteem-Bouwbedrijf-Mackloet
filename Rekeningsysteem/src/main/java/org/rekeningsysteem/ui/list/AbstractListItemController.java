package org.rekeningsysteem.ui.list;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.application.Main;

import java.util.Optional;

public abstract class AbstractListItemController<M, P extends ItemPane> implements Disposable {

	private final P ui;
	private final Maybe<Optional<M>> model;

	public AbstractListItemController(P ui, Observable<M> model) {
		this.ui = ui;
		this.model = getListItem(ui, model);
	}

	protected P getUI() {
		return this.ui;
	}

	public Maybe<Optional<M>> getModel() {
		return this.model;
	}

	public void showModalMessage() {
		Main.getMain().showModalMessage(this.ui);
	}

	private static <M, P extends ItemPane> Maybe<Optional<M>> getListItem(P ui, Observable<M> modelObservable) {
		return modelObservable
			.sample(ui.getAddButtonEvent())
			.map(Optional::of)
			.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
			.firstElement();
	}

	@Override
	public boolean isDisposed() {
		return this.ui.isDisposed();
	}

	@Override
	public void dispose() {
		this.ui.dispose();
	}
}
