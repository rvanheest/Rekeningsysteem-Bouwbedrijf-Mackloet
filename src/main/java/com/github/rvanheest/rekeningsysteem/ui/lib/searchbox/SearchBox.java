package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;

import java.util.Objects;
import java.util.stream.Stream;

public abstract class SearchBox<T> extends Region implements SearchBoxView<T>, Disposable {

  private final TextField textfield = new TextField();
  private final Button clearButton = new Button();
  private final ContextMenu contextMenu = new ContextMenu();
  private final Popup infoPopup = new Popup();
  private final SearchInfoBox<T> infoBox;

  private final PublishSubject<T> selectedItem = PublishSubject.create();

  private CompositeDisposable suggestionsDisposables;
  private final CompositeDisposable uiDisposables = new CompositeDisposable();

  private final SearchBoxPresenter<T> presenter;

  public SearchBox(String promptText, SearchBoxPresenter<T> presenter) {
    this.setId("searchbox");
    this.textfield.setPromptText(promptText);
    this.clearButton.setVisible(false);
    this.getChildren().addAll(this.textfield, this.clearButton);

    this.infoBox = this.createInfoBox();
    this.infoPopup.getContent().add(this.infoBox);

    Disposable clearTextDisposable = this.clearButtonIntent()
        .mergeWith(this.escapeTypedIntent())
        .subscribe(ignore -> this.clearText());
    Disposable clearButtonVisible = this.textTypedIntent()
        .map(s -> !s.isEmpty())
        .distinctUntilChanged()
        .subscribe(this.clearButton::setVisible);
    Disposable contextMenuHiddenDisposable = JavaFxObservable.eventsOf(this.contextMenu, WindowEvent.WINDOW_HIDDEN)
        .subscribe(ignore -> this.infoPopup.hide());

    this.uiDisposables.addAll(clearTextDisposable, clearButtonVisible, contextMenuHiddenDisposable);

    this.presenter = presenter;
    this.presenter.attachView(this);
  }

  @Override
  protected void layoutChildren() {
    this.textfield.resize(this.getWidth(), this.getHeight());
    this.clearButton.resizeRelocate(this.getWidth() - 18, 6, 12, 13);
  }

  private void clearText() {
    this.contextMenu.hide();
    this.textfield.clear();
    this.textfield.requestFocus();
  }

  @Override
  public Observable<String> textTypedIntent() {
    return JavaFxObservable.valuesOf(this.textfield.textProperty())
        .skip(1)
        .distinctUntilChanged();
  }

  @Override
  public Observable<Boolean> clearButtonIntent() {
    return JavaFxObservable.actionEventsOf(this.clearButton).map(ignore -> true);
  }

  @Override
  public Observable<Boolean> escapeTypedIntent() {
    return JavaFxObservable.eventsOf(this, KeyEvent.KEY_PRESSED)
        .filter(event -> event.getCode() == KeyCode.ESCAPE && !this.textfield.getText().isEmpty())
        .map(ignore -> true);
  }

  @Override
  public Observable<T> selectedItemIntent() {
    return this.selectedItem;
  }

  @Override
  public void render(SearchBoxViewState<T> viewState) {
    if (this.suggestionsDisposables != null && !this.suggestionsDisposables.isDisposed())
      this.suggestionsDisposables.dispose();
    this.suggestionsDisposables = null;

    ObservableList<MenuItem> items = this.contextMenu.getItems();
    items.clear();

    for (T suggestion : viewState.getSearchSuggestions()) {
      if (this.suggestionsDisposables == null)
        this.suggestionsDisposables = new CompositeDisposable();

      SearchMenuItem item = new SearchMenuItem(displaySuggestion(suggestion));
      items.add(item);

      Disposable actionDisposable = JavaFxObservable.actionEventsOf(item).subscribe(ignore -> {
        this.selectedItem.onNext(suggestion);
        this.clearText();
      });
      Disposable infoBoxDisposable = item.hover()
          .skip(1) // skip initial property value
          .subscribe(ignore -> onHover(suggestion, item));

      this.suggestionsDisposables.addAll(actionDisposable, infoBoxDisposable);
    }

    if (!this.contextMenu.isShowing())
      this.contextMenu.show(this, Side.BOTTOM, 10, -5);
  }

  private void onHover(T suggestion, SearchMenuItem item) {
    this.infoBox.setContent(suggestion);

    Scene contextMenuScene = this.contextMenu.getScene();
    Point2D infoBoxPosition = item.getContent().localToScene(0, 0)
        .add(contextMenuScene.getX(), contextMenuScene.getY())
        .add(this.contextMenu.getX(), this.contextMenu.getY())
        .subtract(this.infoBox.getPrefWidth() + 10, 10);

    this.infoPopup.show(this.getScene().getWindow(), infoBoxPosition.getX(), infoBoxPosition.getY());
  }

  protected abstract Node displaySuggestion(T suggestion);

  protected abstract SearchInfoBox<T> createInfoBox();

  @Override
  public boolean isDisposed() {
    return Stream.of(this.presenter, this.uiDisposables, this.suggestionsDisposables)
        .filter(Objects::nonNull)
        .allMatch(Disposable::isDisposed);
  }

  @Override
  public void dispose() {
    Stream.of(this.presenter, this.uiDisposables, this.suggestionsDisposables)
        .filter(Objects::nonNull)
        .forEach(Disposable::dispose);
  }
}

