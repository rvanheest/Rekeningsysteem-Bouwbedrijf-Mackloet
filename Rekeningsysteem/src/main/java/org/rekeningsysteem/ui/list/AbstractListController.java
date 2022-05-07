package org.rekeningsysteem.ui.list;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.data.util.ListItem;

public abstract class AbstractListController<M extends ListItem, U, C extends AbstractListItemController<M, ?>> implements Disposable {

	private final AbstractListPane<U> ui;
	private final Observable<List<M>> model;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public AbstractListController(AbstractListPane<U> ui, Supplier<C> func) {
		this.ui = ui;
		this.model = this.ui.getData().map(this::uiToModel);

		this.disposable.addAll(
			this.ui.getAddButtonEvent().map(e -> func.get())
				.doOnNext(C::showModalMessage)
				.flatMapMaybe(c -> c.getModel().doOnSuccess(m -> {
					c.dispose();
					Main.getMain().hideModalMessage();
				}))
				.flatMapMaybe(Maybe::fromOptional)
				.flatMapMaybe(model -> this.getModel().firstElement().doOnSuccess(list -> list.add(model)).map(Collection::stream))
				.map(this::modelToUI)
				.subscribe(this.ui::setData),

			this.ui.getUpButtonEvent()
				.flatMapMaybe(index -> this.model.firstElement().doOnSuccess(list -> Collections.swap(list, index, index - 1)).map(Collection::stream))
				.map(this::modelToUI)
				.subscribe(this.ui::setData),

			this.ui.getDownButtonEvent()
				.flatMapMaybe(index -> this.model.firstElement().doOnSuccess(list -> Collections.swap(list, index, index + 1)).map(Collection::stream))
				.map(this::modelToUI)
				.subscribe(this.ui::setData),

			this.ui
		);
	}

	protected abstract List<U> modelToUI(Stream<M> stream);

	protected abstract List<M> uiToModel(List<? extends U> list);

	public AbstractListPane<U> getUI() {
		return this.ui;
	}

	public Observable<List<M>> getModel() {
		return this.model;
	}

	protected void setData(List<U> us) {
		this.ui.setData(us);
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
