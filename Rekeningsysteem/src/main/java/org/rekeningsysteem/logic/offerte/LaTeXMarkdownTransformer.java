package org.rekeningsysteem.logic.offerte;

import com.github.rjeschke.txtmark.CleaningEmitter;
import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.Processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class LaTeXMarkdownTransformer implements MarkdownTransformer {

  @Override
  public String transform(String markdown) throws IOException {
    Decorator decorator2 = new LatexDecorator();
    Configuration config2 = Configuration.builder().setDecorator(decorator2).setSafeMode(true).build();

    try (StringReader stringReader = new StringReader(markdown);
        BufferedReader bufferedReader = new BufferedReader(stringReader)) {
      return Processor.process(bufferedReader, new CleaningEmitter(decorator2), config2);
    }
  }
}

class LatexDecorator implements Decorator {

  @Override
  public void openParagraph(StringBuilder out) {
    // do nothing
  }

  @Override
  public void closeParagraph(StringBuilder out) {
    out.append("\n\n");
  }

  @Override
  public void openBlockquote(StringBuilder out) {
    // not support
  }

  @Override
  public void closeBlockquote(StringBuilder out) {
    // not support
  }

  @Override
  public void openCodeBlock(StringBuilder out) {
    // not support
  }

  @Override
  public void closeCodeBlock(StringBuilder out) {
    // not support
  }

  @Override
  public void openCodeSpan(StringBuilder out) {
    // not support
  }

  @Override
  public void closeCodeSpan(StringBuilder out) {
    // not support
  }

  @Override
  public void openHeadline(StringBuilder out, int level) {
    switch (level) {
      case 1:
        out.append("\\section*{");
        break;
      case 2:
        out.append("\\subsection*{");
        break;
      case 3:
        out.append("\\subsubsection*{");
        break;
      case 4:
        out.append("\\paragraph*{");
        break;
      case 5:
        out.append("\\subparagraph*{");
        break;
      default:
        break;
    }
  }

  @Override
  public void closeHeadline(StringBuilder out, int level) {
    out.append("}\n\n");
  }

  @Override
  public void openStrong(StringBuilder out) {
    out.append("\\textbf{");
  }

  @Override
  public void closeStrong(StringBuilder out) {
    out.append("}");
  }

  @Override
  public void openEmphasis(StringBuilder out) {
    out.append("\\textit{");
  }

  @Override
  public void closeEmphasis(StringBuilder out) {
    out.append("}");
  }

  @Override
  public void openSuper(StringBuilder out) {
    // not support
  }

  @Override
  public void closeSuper(StringBuilder out) {
    // not support
  }

  @Override
  public void openOrderedList(StringBuilder out) {
    out.append("\\begin{enumerate}\n");
  }

  @Override
  public void closeOrderedList(StringBuilder out) {
    out.append("\\end{enumerate}\n");
  }

  @Override
  public void openUnorderedList(StringBuilder out) {
    out.append("\\begin{itemize}\n");
  }

  @Override
  public void closeUnorderedList(StringBuilder out) {
    out.append("\\end{itemize}\n");
  }

  @Override
  public void openListItem(StringBuilder out) {
    out.append("\\item{");
  }

  @Override
  public void closeListItem(StringBuilder out) {
    out.append("}\n");
  }

  @Override
  public void horizontalRuler(StringBuilder out) {
    // not support
  }

  @Override
  public void openLink(StringBuilder out) {
    // not support
  }

  @Override
  public void closeLink(StringBuilder out) {
    // not support
  }

  @Override
  public void openImage(StringBuilder out) {
    // not support
  }

  @Override
  public void closeImage(StringBuilder out) {
    // not support
  }
}
