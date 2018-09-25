package com.github.rvanheest.rekeningsysteem.test.businesslogic;

import com.github.rvanheest.rekeningsysteem.businesslogic.DebtorSearchEngine;
import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DebtorSearchEngineTest {

  private static Debtor deb1 = new Debtor(1, "name1", "street1", "number1", "zipcode1", "place1");
  private static Debtor deb2 = new Debtor(2, "name2", "street2", "number2", "zipcode2", "place2", "test2");
  private static Debtor deb3 = new Debtor(3, "name3", "street3", "number3", "zipcode3", "place3", "test3");

  @Mock private Database database;
  private DebtorSearchEngine searchEngine;

  @Before
  public void setUp() {
    this.searchEngine = new DebtorSearchEngine(this.database);
  }

  @Test
  public void testSuggestEmptyString() {
    this.searchEngine.suggest("")
        .test()
        .assertValue(Collections.emptyList())
        .assertNoErrors()
        .assertComplete();

    verifyZeroInteractions(this.database);
  }

  @Test
  public void testSuggestSmallString() {
    this.searchEngine.suggest("ab")
        .test()
        .assertValue(Collections.emptyList())
        .assertNoErrors()
        .assertComplete();

    verifyZeroInteractions(this.database);
  }

  @Test
  public void testSuggestLongString() {
    when(this.database.getDebtorWithName(eq("test-string"))).thenReturn(Observable.just(deb1, deb2, deb3));

    this.searchEngine.suggest("test-string")
        .test()
        .assertValue(Arrays.asList(deb1, deb2, deb3))
        .assertNoErrors()
        .assertComplete();

    verify(this.database).getDebtorWithName(eq("test-string"));
    verifyNoMoreInteractions(this.database);
  }
}
