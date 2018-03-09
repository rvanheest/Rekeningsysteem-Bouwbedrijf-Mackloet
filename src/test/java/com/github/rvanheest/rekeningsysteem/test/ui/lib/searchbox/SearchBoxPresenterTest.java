package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxView;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxViewState;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.matches;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchBoxPresenterTest extends ApplicationTest {

  @Mock private SearchEngine<String> searchEngine;
  @Mock private SearchBoxView<String> view;
  private TestSearchBoxPresenter presenter;

  @Before
  public void setUp() {
    this.presenter = new TestSearchBoxPresenter(this.searchEngine);
  }

  @After
  public void tearDown() {
    this.presenter.dispose();
  }

  @Test
  public void testTypeIntentOnce() throws InterruptedException {
    TestObserver<SearchBoxViewState<String>> testObserver = this.presenter.viewStateObservable().test();
    List<String> searchResults = Arrays.asList("foo", "bar", "baz");

    when(this.view.textTypedIntent()).thenReturn(Observable.just("abc"));
    when(this.searchEngine.suggest(matches("abc"))).thenReturn(Observable.just(searchResults));

    this.presenter.attachView(this.view);

    testObserver.await(500, TimeUnit.MILLISECONDS);
    testObserver.assertValue(new SearchBoxViewState<>(searchResults))
        .assertNoErrors()
        .assertNotComplete();

    verify(this.searchEngine).suggest(any());
  }

  @Test
  public void testTypeIntentFastTyping() throws InterruptedException {
    TestObserver<SearchBoxViewState<String>> testObserver = this.presenter.viewStateObservable().test();
    List<String> searchResults = Arrays.asList("foo", "bar", "baz");

    when(this.view.textTypedIntent()).thenReturn(Observable.just("a", "ab", "abc"));
    when(this.searchEngine.suggest(matches("abc"))).thenReturn(Observable.just(searchResults));

    this.presenter.attachView(this.view);

    testObserver.await(500, TimeUnit.MILLISECONDS);
    testObserver.assertValue(new SearchBoxViewState<>(searchResults))
        .assertNoErrors()
        .assertNotComplete();

    verify(this.searchEngine).suggest(any());
  }

  @Test
  public void testTypeIntentSlowTyping() throws InterruptedException {
    TestObserver<SearchBoxViewState<String>> testObserver = this.presenter.viewStateObservable().test();
    List<String> searchResults1 = Arrays.asList("foo1", "bar1", "baz1");
    List<String> searchResults2 = Arrays.asList("foo2", "bar2", "baz2");
    List<String> searchResults3 = Arrays.asList("foo3", "bar3", "baz3");

    Observable<String> input = Observable.just("a")
        .concatWith(Observable.timer(500, TimeUnit.MILLISECONDS).map(ignore -> "ab"))
        .concatWith(Observable.timer(500, TimeUnit.MILLISECONDS).map(ignore -> "abc"));

    when(this.view.textTypedIntent()).thenReturn(input);
    when(this.searchEngine.suggest(matches("a"))).thenReturn(Observable.just(searchResults1));
    when(this.searchEngine.suggest(matches("ab"))).thenReturn(Observable.just(searchResults2));
    when(this.searchEngine.suggest(matches("abc"))).thenReturn(Observable.just(searchResults3));

    this.presenter.attachView(this.view);

    testObserver.await(2000, TimeUnit.MILLISECONDS);
    testObserver.assertValueSequence(Arrays.asList(
        new SearchBoxViewState<>(searchResults1),
        new SearchBoxViewState<>(searchResults2),
        new SearchBoxViewState<>(searchResults3)))
        .assertNoErrors()
        .assertNotComplete();

    verify(this.searchEngine, times(3)).suggest(any());
  }
}
