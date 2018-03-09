package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import java.util.List;
import java.util.Objects;

public class SearchBoxViewState<T> {

  private final List<T> searchSuggestions;

  public SearchBoxViewState(List<T> searchSuggestions) {
    this.searchSuggestions = searchSuggestions;
  }

  public List<T> getSearchSuggestions() {
    return this.searchSuggestions;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof SearchBoxViewState) {
      SearchBoxViewState that = (SearchBoxViewState) other;
      return Objects.equals(this.searchSuggestions, that.searchSuggestions);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.searchSuggestions);
  }

  @Override
  public String toString() {
    return String.format("SearchBoxViewState[suggestions=%s]", this.searchSuggestions);
  }
}
