package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

import io.reactivex.subjects.PublishSubject;

class IntentRelayBinderPair<V extends View, Intent> {

  final PublishSubject<Intent> intentRelaySubject;
  final ViewIntentBinder<V, Intent> intentBinder;

  IntentRelayBinderPair(PublishSubject<Intent> intentRelaySubject, ViewIntentBinder<V, Intent> intentBinder) {
    this.intentRelaySubject = intentRelaySubject;
    this.intentBinder = intentBinder;
  }
}
