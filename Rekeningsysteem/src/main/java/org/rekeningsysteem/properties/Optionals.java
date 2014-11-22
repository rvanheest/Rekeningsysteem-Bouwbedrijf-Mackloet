package org.rekeningsysteem.properties;

import java.util.Optional;
import java.util.function.BiFunction;

public class Optionals {

	public static <R, S, T> Optional<T> zip(Optional<R> optR, Optional<S> optS,
			BiFunction<R, S, T> zipFunc) {
		return optR.flatMap(r -> optS.map(s -> zipFunc.apply(r, s)));
	}
}
