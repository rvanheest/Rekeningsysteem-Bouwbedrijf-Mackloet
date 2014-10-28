package org.rekeningsysteem.ui.offerte;

import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.WorkingPaneController;

import rx.Observable;
import rx.Observer;

public class TextPaneController extends WorkingPaneController implements Observer<String> {

	private final TextPane ui;
	private final Observable<String> model;

	public TextPaneController() {
		this(new TextPane());
	}

	public TextPaneController(Observable<String> input) {
		this(new TextPane(), input);
	}

	public TextPaneController(TextPane ui) {
		this(ui, Observable.empty());
	}

	public TextPaneController(TextPane ui, Observable<String> input) {
		super(new WorkingPane(ui) {

			@Override
			public String getTitle() {
				return "Offerte tekst";
			}
		});

		this.ui = ui;
		this.model = this.ui.getText();
		
		input.subscribe(this);
	}

	public Observable<String> getModel() {
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
	public void onNext(String text) {
		this.ui.setText(text);
	}
}
