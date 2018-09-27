package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import io.reactivex.Observable;

public interface DocumentManager<Doc extends AbstractDocument> extends HeaderManager {

  Observable<Doc> getDocument();
}
