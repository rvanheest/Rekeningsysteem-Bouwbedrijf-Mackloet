package com.github.rvanheest.rekeningsysteem.businesslogic;

import io.reactivex.Observable;

import java.util.List;

public interface SearchEngine<T> {
  Observable<List<T>> suggest(String text);
}
