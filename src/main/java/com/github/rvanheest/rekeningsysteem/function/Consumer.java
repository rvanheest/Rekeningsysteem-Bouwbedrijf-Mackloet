package com.github.rvanheest.rekeningsysteem.function;

import java.util.Objects;

@FunctionalInterface
public interface Consumer<T> {

  void accept(T t) throws Exception;

  default Consumer<T> andThen(Consumer<? super T> after) {
    Objects.requireNonNull(after);
    return t -> {
      accept(t);
      after.accept(t);
    };
  }
}
