package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class BasePresenter<V extends View, ViewState> implements Presenter<V> {

  private final BehaviorSubject<ViewState> viewStateBehaviorSubject;
  private final List<IntentRelayBinderPair<V, ?>> intentRelayBinders = new ArrayList<>();
  private boolean subscribeViewStateMethodCalled = false;
  private CompositeDisposable intentDisposables;
  private Disposable viewRelayConsumerDisposable;
  private Disposable viewStateDisposable;
  private boolean viewAttachedFirstTime = true;
  private ViewStateConsumer<V, ViewState> viewStateConsumer;

  private BasePresenter(BehaviorSubject<ViewState> viewStateBehaviorSubject) {
    this.viewStateBehaviorSubject = viewStateBehaviorSubject;
    this.reset();
  }

  public BasePresenter() {
    this(BehaviorSubject.create());
  }

  public BasePresenter(ViewState viewState) {
    this(BehaviorSubject.createDefault(viewState));
  }

  protected Observable<ViewState> viewStateObservable() {
    return this.viewStateBehaviorSubject;
  }

  @Override
  public void attachView(V view) {
    if (this.viewAttachedFirstTime)
      this.bindIntents();

    if (this.viewStateConsumer != null)
      this.subscribeViewStateConsumerActually(view);

    this.intentRelayBinders.forEach(pair -> this.bindIntentActually(view, pair));

    this.viewAttachedFirstTime = false;
  }

  @Override
  public boolean isDisposed() {
    return Stream.of(this.viewRelayConsumerDisposable, this.intentDisposables, this.viewStateDisposable)
        .filter(Objects::nonNull)
        .allMatch(Disposable::isDisposed);
  }

  @Override
  public void dispose() {
    if (this.viewRelayConsumerDisposable != null) {
      this.viewRelayConsumerDisposable.dispose();
      this.viewRelayConsumerDisposable = null;
    }

    if (this.intentDisposables != null) {
      this.intentDisposables.dispose();
      this.intentDisposables = null;
    }

    if (this.viewStateDisposable != null) // Cancel the overall observable stream
      this.viewStateDisposable.dispose();

    this.unbindIntents();
    this.reset();
  }

  private void reset() {
    this.viewAttachedFirstTime = true;
    this.intentRelayBinders.clear();
    this.subscribeViewStateMethodCalled = false;
  }

  protected void subscribeViewState(Observable<ViewState> viewStateObservable,
      ViewStateConsumer<V, ViewState> consumer) {
    if (this.subscribeViewStateMethodCalled)
      throw new IllegalStateException("subscribeViewState() method is only allowed to be called once");
    this.subscribeViewStateMethodCalled = true;

    if (viewStateObservable == null)
      throw new NullPointerException("ViewState Observable is null");

    if (consumer == null)
      throw new NullPointerException("ViewStateBinder is null");

    this.viewStateConsumer = consumer;

    this.viewStateDisposable = viewStateObservable.subscribeWith(
        new DisposableViewStateObserver<>(viewStateBehaviorSubject));
  }

  private void subscribeViewStateConsumerActually(V view) {
    if (view == null)
      throw new NullPointerException("View is null");

    if (this.viewStateConsumer == null)
      throw new NullPointerException(String.format("%s is null. This is an internal bug. Please let me know!",
          ViewStateConsumer.class.getSimpleName()));

    this.viewRelayConsumerDisposable = this.viewStateBehaviorSubject.subscribe(
        viewState -> this.viewStateConsumer.accept(view, viewState));
  }

  protected abstract void bindIntents();

  protected void unbindIntents() {
  }

  public <Intent> Observable<Intent> intent(ViewIntentBinder<V, Intent> binder) {
    PublishSubject<Intent> intentRelay = PublishSubject.create();
    this.intentRelayBinders.add(new IntentRelayBinderPair<>(intentRelay, binder));
    return intentRelay;
  }

  private <Intent> Observable<Intent> bindIntentActually(V view, IntentRelayBinderPair<V, Intent> relayBinderPair) {
    if (view == null)
      throw new NullPointerException("View is null. This is an internal bug. Please let me know!");

    if (relayBinderPair == null) {
      throw new NullPointerException("IntentRelayBinderPair is null. This is an internal bug. Please let me know!");
    }

    PublishSubject<Intent> intentRelay = relayBinderPair.intentRelaySubject;
    if (intentRelay == null)
      throw new NullPointerException(
          "IntentRelay from binderPair is null. This is an internal bug. Please let me know!");

    ViewIntentBinder<V, Intent> intentBinder = relayBinderPair.intentBinder;
    if (intentBinder == null)
      throw new NullPointerException("ViewIntentBinder is null. This is an internal bug. Please let me know!");

    Observable<Intent> intent = intentBinder.apply(view);
    if (intent == null)
      throw new NullPointerException(String.format("Intent Observable returned from Binder %s is null", intentBinder));

    if (intentDisposables == null)
      intentDisposables = new CompositeDisposable();
    intentDisposables.add(intent.subscribeWith(new DisposableIntentObserver<>(intentRelay)));

    return intentRelay;
  }
}
