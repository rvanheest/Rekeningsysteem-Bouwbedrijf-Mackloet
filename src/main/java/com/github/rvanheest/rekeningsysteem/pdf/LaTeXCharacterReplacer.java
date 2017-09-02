package com.github.rvanheest.rekeningsysteem.pdf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LaTeXCharacterReplacer {

  private final Map<String, String> replaceMap = new HashMap<>();

  public LaTeXCharacterReplacer() {
    this.fillMapWithSmallLetters();
    this.fillMapWithCapitals();
    this.replaceMap.put("€", "\\euro");
    this.replaceMap.put("\n", "\\\\");
  }

  private void fillMapWithSmallLetters() {
    this.replaceMap.put("â", "\\^a");
    this.replaceMap.put("ä", "\\\"a");
    this.replaceMap.put("à", "\\`a");
    this.replaceMap.put("á", "\\'a");
    this.replaceMap.put("ê", "\\^e");
    this.replaceMap.put("ë", "\\\"e");
    this.replaceMap.put("è", "\\`e");
    this.replaceMap.put("é", "\\'e");
    this.replaceMap.put("î", "\\^i");
    this.replaceMap.put("ï", "\\\"i");
    this.replaceMap.put("ì", "\\`i");
    this.replaceMap.put("í", "\\'i");
    this.replaceMap.put("ô", "\\^o");
    this.replaceMap.put("ö", "\\\"o");
    this.replaceMap.put("ò", "\\`o");
    this.replaceMap.put("ó", "\\'o");
    this.replaceMap.put("û", "\\^u");
    this.replaceMap.put("ü", "\\\"u");
    this.replaceMap.put("ù", "\\`u");
    this.replaceMap.put("ú", "\\'u");
    this.replaceMap.put("ÿ", "\\\"y");
    this.replaceMap.put("ý", "\\'y");
  }

  private void fillMapWithCapitals() {
    this.replaceMap.put("Â", "\\^A");
    this.replaceMap.put("Ä", "\\\"A");
    this.replaceMap.put("À", "\\`A");
    this.replaceMap.put("Á", "\\'A");
    this.replaceMap.put("Ê", "\\^E");
    this.replaceMap.put("Ë", "\\\"E");
    this.replaceMap.put("È", "\\`E");
    this.replaceMap.put("É", "\\'E");
    this.replaceMap.put("Î", "\\^I");
    this.replaceMap.put("Ï", "\\\"I");
    this.replaceMap.put("Ì", "\\`I");
    this.replaceMap.put("Í", "\\'I");
    this.replaceMap.put("Ô", "\\^O");
    this.replaceMap.put("Ö", "\\\"O");
    this.replaceMap.put("Ò", "\\`O");
    this.replaceMap.put("Ó", "\\'O");
    this.replaceMap.put("Û", "\\^U");
    this.replaceMap.put("Ü", "\\\"U");
    this.replaceMap.put("Ù", "\\`U");
    this.replaceMap.put("Ú", "\\'U");
    this.replaceMap.put("Ý", "\\'Y");
  }

  public Object replace(Object value) {
    if (value instanceof String) {
      return ((String) value).chars()
          .parallel()
          .mapToObj(c -> String.valueOf((char) c))
          .map(s -> "\\{}_^#&$%~".contains(s) ? "\\" + s : s)
          .map(s -> this.replaceMap.getOrDefault(s, s))
          .collect(Collectors.joining());
    }
    else if (value instanceof List) {
      return ((List<?>) value).stream()
          .map(this::replace)
          .collect(Collectors.toList());
    }
    return value;
  }
}
