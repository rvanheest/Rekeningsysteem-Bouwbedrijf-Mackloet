package org.rekeningsysteem.rxjavafx;

import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;

public enum Observables {
	; // no class instances

	public static <T extends Event> Observable<T> fromNodeEvents(Node source, EventType<T> eventType) {
		return Observable.<T> create(emitter -> {
			EventHandler<T> handler = emitter::onNext;
			source.addEventHandler(eventType, handler);
			emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread(() -> source.removeEventHandler(eventType, handler)));
		}).subscribeOn(JavaFxScheduler.getInstance());
	}

	public static <T extends Event> Observable<T> fromNodeEvents(Window source, EventType<T> eventType) {
		return Observable.<T> create(emitter -> {
			EventHandler<T> handler = emitter::onNext;
			source.addEventHandler(eventType, handler);
			emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread(() -> source.removeEventHandler(eventType, handler)));
		}).subscribeOn(JavaFxScheduler.getInstance());
	}

	public static <T extends Event> Observable<T> fromNodeEvents(MenuItem source, EventType<T> eventType) {
		return Observable.<T> create(emitter -> {
			EventHandler<T> handler = emitter::onNext;
			source.addEventHandler(eventType, handler);
			emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread(() -> source.removeEventHandler(eventType, handler)));
		}).subscribeOn(JavaFxScheduler.getInstance());
	}

	public static <T> Observable<T> fromProperty(ObservableValue<T> fxObservable) {
		return Observable.create(emitter -> {
			Optional.ofNullable(fxObservable.getValue()).ifPresent(emitter::onNext);
			ChangeListener<T> listener = (obs, prev, current) -> emitter.onNext(current);
			fxObservable.addListener(listener);
			emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread(() -> fxObservable.removeListener(listener)));
		});
	}

	public static <T> Observable<List<? extends T>> fromObservableList(ObservableList<T> fxObservable) {
		return Observable.create(emitter -> {
			emitter.onNext(fxObservable);
			ListChangeListener<T> listener = change -> emitter.onNext(change.getList());
			fxObservable.addListener(listener);
			emitter.setDisposable(JavaFxSubscriptions.unsubscribeInEventDispatchThread(() -> fxObservable.removeListener(listener)));
		});
	}
}
