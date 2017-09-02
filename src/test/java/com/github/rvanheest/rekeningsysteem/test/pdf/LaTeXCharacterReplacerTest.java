package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.pdf.LaTeXCharacterReplacer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LaTeXCharacterReplacerTest {

  private final LaTeXCharacterReplacer replacer;
  private final Object input;
  private final Object expectedOutput;

  public LaTeXCharacterReplacerTest(Object input, Object expectedOutput) {
    this.replacer = new LaTeXCharacterReplacer();
    this.input = input;
    this.expectedOutput = expectedOutput;
  }

  @Parameters
  public static Collection<Object[]> data() {
    Object[][] values = new Object[][] {
        { "test123", "test123" },
        { "test{1^2$3", "test\\{1\\^2\\$3" },
        { "testìng ís nôt whÀt yóù thïnk", "test\\`ing \\'is n\\^ot wh\\`At y\\'o\\`u th\\\"ink" },
        { "test\ntest123\n\ntest456", "test\\\\test123\\\\\\\\test456" },
        { Arrays.asList("123", "456", "{789}", "fôöbàr"), Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar") },
        { Arrays.asList(
            Arrays.asList("123", "456", "{789}", "fôöbàr"),
            Arrays.asList("123", "456", "{789}", "fôöbàr"),
            Arrays.asList("123", "456", "{789}", "fôöbàr")),
          Arrays.asList(
              Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar"),
              Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar"),
              Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar"))
        },
        { "test with € in it", "test with \\euro in it" },
        { 14, 14 }
    };
    return Arrays.asList(values);
  }

  @Test
  public void testPrepare() {
    assertEquals(this.expectedOutput, this.replacer.replace(this.input));
  }
}
