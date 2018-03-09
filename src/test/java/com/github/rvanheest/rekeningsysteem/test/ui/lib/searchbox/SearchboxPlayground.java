package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBox;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchInfoBox;
import io.reactivex.Observable;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.Collections;

public class SearchboxPlayground extends Playground {

  public SearchBox<String> uiElement() {
    SearchBox<String> searchBox = new SearchBox<String>("default text") {

      @Override
      protected SearchBoxPresenter<String> createPresenter() {
        return new SearchBoxPresenter<>(text -> {
          if (text.length() <= 2)
            return Observable.just(Collections.emptyList());
          else
            return Observable.range(1, 3)
                .map(i -> String.format("%d. %s", i, text))
                .toList()
                .toObservable();
        });
      }

      @Override
      protected Node displaySuggestion(String suggestion) {
        return new Label(String.format("label %s", suggestion));
      }

      @Override
      protected SearchInfoBox<String> createInfoBox() {
        return new TestInfoBox();
      }
    };

    searchBox.selectedItemIntent()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return searchBox;
  }

  public static void main(String[] args) {
    Application.launch(SearchboxPlayground.class);
  }
}
