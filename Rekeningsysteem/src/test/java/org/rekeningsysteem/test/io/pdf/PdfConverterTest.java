package org.rekeningsysteem.test.io.pdf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.rekeningsysteem.io.pdf.PdfConverter;

@RunWith(Parameterized.class)
public class PdfConverterTest {

	private final PdfConverter converter;
	private final Object input;
	private final Object expectedOutput;

	public PdfConverterTest(Object input, Object expectedOutput) {
		this.converter = new PdfConverter(new File("resources\\LaTeX\\Offerte.tex"));
		this.input = input;
		this.expectedOutput = expectedOutput;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] values = new Object[][] {
			{ "test123", "test123" },
			{ "test{1^2$3", "test\\{1\\^2\\$3" },
			{ "testèn ís nïêt wÀt het lýkt", "test\\`en \\'is n\\\"i\\^et w\\`At het l\\'ykt" },
			{ "test\ntest123\n\ntest456", "test\\\\test123\\\\\\\\test456" },
			{
				Arrays.asList("123", "456", "{789}", "fôöbàr"),
				Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar")
			},
			{
				Arrays.asList(
					Arrays.asList("123", "456", "{789}", "fôöbàr"),
					Arrays.asList("123", "456", "{789}", "fôöbàr", Arrays.asList("123", "456", "{789}", "fôöbàr")),
					Arrays.asList("123", "456", "{789}", "fôöbàr")
				),
				Arrays.asList(
					Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar"),
					Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar", Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar")),
					Arrays.asList("123", "456", "\\{789\\}", "f\\^o\\\"ob\\`ar")
				)
			},
			{ "test with € in it", "test with \\euro in it" },
			{ 14, 14 }
		};
		return Arrays.asList(values);
	}

	@Test
	public void testPrepare() {
		assertEquals(this.expectedOutput, this.converter.prepare(this.input));
	}
}
