package org.rekeningsysteem.test;

import com.github.rjeschke.txtmark.Block;
import com.github.rjeschke.txtmark.CleaningEmitter;
import com.github.rjeschke.txtmark.CleaningProcessor;
import com.github.rjeschke.txtmark.Configuration;
import com.github.rjeschke.txtmark.Decorator;
import com.github.rjeschke.txtmark.DefaultDecorator;
import com.github.rjeschke.txtmark.DefaultEmitter;
import com.github.rjeschke.txtmark.Emitter;
import com.github.rjeschke.txtmark.Line;
import com.github.rjeschke.txtmark.Processor;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

public class MarkdownTest {

  public static void main(String[] args) throws IOException {
    String input = "# This is ***TXTMARK***\n"
        + "A test with *italic* and **bold** fonts.\n\n"
        + "A new paragraph with\n"
        + "a newline"
        + "\n\n"
        + "## Subheader\n"
        + "* item #1\n"
        + "* item #2\n"
        + "* item #3\n"
        + "\n\n"
        + "with some more text!"
        + "\n\n"
        + "### Subsubheader\n"
        + "with some more text?"
        + "\n\n"
        + "#### Paragraph\n"
        + "with some more text - \"you just never have enough\"!!!\n\n"
        + "$, %, ', (xyz) * + / : [abc] {xyz} = _"
        + "\n\n"
        + "##### Subparagraph\n"
        + "1. num1;\n"
        + "2. num2;\n"
        + "3. num3.\n";

    DefaultDecorator decorator1 = new DefaultDecorator();
    Configuration config1 = Configuration.builder().setDecorator(decorator1).setSafeMode(true).build();
    String result1 = Processor.process(input, config1);

    Decorator decorator2 = latexDecorator();
    Configuration config2 = Configuration.builder().setDecorator(decorator2).setSafeMode(true).build();
    String result2 = Processor.process(new StringReader(input), latexEmitter(decorator2), config2);

    System.out.println(result1);
    System.out.println("--------");
    System.out.println(result2);
  }

  public static Emitter latexEmitter(Decorator decorator) {
    return new CleaningEmitter(decorator) {

      @Override
      public void emit(StringBuilder out, Block root) {
        Line line = root.lines;
        while (line != null) {
          if (!line.isEmpty) {
            line.value = line.value.chars()
                .mapToObj(c -> {
                  String cs = Character.toString((char) c);
                  if (cs.matches("[a-zA-Z0-9 *\\-_.,;:?!/\\[\\]=]"))
                    return cs;
                  else
                    return String.format("\\char\"%04x ", (int) c);
                })
                .collect(Collectors.joining());
          }
          line = line.next;
        }

        super.emit(out, root);
      }
    };
  }

  public static Decorator latexDecorator() {
    return new Decorator() {

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
    };
  }
}
