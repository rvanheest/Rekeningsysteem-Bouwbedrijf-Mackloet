package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.pdf.LaTeXCharacterReplacer;
import com.github.rvanheest.rekeningsysteem.pdf.PdfConverter;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PdfConverterTest implements TestSupportFixture {

  private PdfConverter converter;
  private Path path;
  @Mock private LaTeXCharacterReplacer replacer;

  @Before
  public void setUp() throws IOException {
    this.resetTestDir();
    this.path = this.writeTemplate(this.getTestDir().resolve("template.tex"));
    this.converter = new PdfConverter(this.path.getParent(), this.replacer);
  }

  private Path writeTemplate(Path path) throws IOException {
    List<String> content = Arrays.asList(
        "Hello $name,",
        "",
        "How are you?",
        "",
        "Kind regards,",
        "$myname"
    );
    return Files.write(path, content, StandardCharsets.UTF_8);
  }

  @Test
  public void testReplacePlaceholders() throws IOException {
    when(this.replacer.replace("tést")).thenReturn("t\\'est");
    when(this.replacer.replace("foobar")).thenReturn("foobar");

    this.converter.replace("name", "tést");
    this.converter.replace("myname", "foobar");

    Path resultPath = Files.createFile(this.getTestDir().resolve("result.tex"));
    this.converter.parse(this.path.toFile(), resultPath.toFile());

    List<String> lines = Files.readAllLines(resultPath);
    List<String> expected = Arrays.asList(
        "Hello t\'est,",
        "",
        "How are you?",
        "",
        "Kind regards,",
        "foobar"
    );

    assertEquals(expected, lines);
  }
}
