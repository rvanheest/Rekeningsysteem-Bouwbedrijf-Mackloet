package org.rekeningsysteem.ui.aangenomen;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.aangenomen.AangenomenListPane.AangenomenModel;
import org.rekeningsysteem.ui.aangenomen.guice.AangenomenListItemControllerFactory;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

import com.google.inject.Inject;

public class AangenomenListController implements Observer<List<AangenomenListItem>> {

	private final AangenomenListPane ui;
	private final Observable<ItemList<AangenomenListItem>> model;

	private static final Func1<List<AangenomenListItem>, List<AangenomenModel>> convert = list ->
			list.stream().map(item -> new AangenomenModel(item.getOmschrijving(),
					item.getLoon().getBedrag(), item.getMateriaal().getBedrag()))
					.collect(Collectors.toList());

	@Inject
	public AangenomenListController(AangenomenListItemControllerFactory paneMaker) {
		this.ui = new AangenomenListPane();
		this.model = this.ui.getData()
				.map(list -> list.stream()
						.map(m -> new AangenomenListItem(m.getOmschrijving(),
								new Geld(m.getLoon()), new Geld(m.getMateriaal())))
						.collect(Collectors.toCollection(ItemList::new)));

		this.ui.getAddButtonEvent()
				.map(event -> paneMaker.create())
				.doOnNext(controller -> Main.getMain().showModalMessage(controller.getUI()))
				.flatMap(controller -> controller.getModel())
				.doOnNext(optItem -> Main.getMain().hideModalMessage())
				.filter(Optional::isPresent)
				.flatMap(optItem -> this.model.first()
						.doOnNext(list -> optItem.map(list::add)))
				.map(convert)
				.subscribe(this.ui::setData);

		this.ui.getUpButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index - 1)))
				.map(convert)
				.subscribe(this.ui::setData);

		this.ui.getDownButtonEvent()
				.flatMap(index -> this.model.first()
						.doOnNext(list -> Collections.swap(list, index, index + 1)))
				.map(convert)
				.subscribe(this.ui::setData);
	}

	public AangenomenListPane getUI() {
		return this.ui;
	}

	public Observable<ItemList<AangenomenListItem>> getModel() {
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
	public void onNext(List<AangenomenListItem> list) {
		this.ui.setData(convert.call(list));
	}
}
