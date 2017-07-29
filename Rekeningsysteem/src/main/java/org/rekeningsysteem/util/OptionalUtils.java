package org.rekeningsysteem.util;

import java.util.Optional;

public enum OptionalUtils {;

	public static Optional<String> fromString(String s) {
		return "".equals(s) ? Optional.empty() : Optional.ofNullable(s);
	}
}
