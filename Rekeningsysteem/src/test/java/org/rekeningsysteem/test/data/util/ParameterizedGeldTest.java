package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.exception.GeldParseException;

@RunWith(Parameterized.class)
public final class ParameterizedGeldTest {

	private final Geld geld;
	private final double bedrag;
	private final String formattedString;

	public ParameterizedGeldTest(Geld geld, double bedrag, String formattedString) {
		this.geld = geld;
		this.bedrag = bedrag;
		this.formattedString = formattedString;
	}

	@Parameters
	public static Collection<Object[]> data() throws GeldParseException {
		Object[][] values = new Object[][] {
				// klein positief
				{ new Geld(0.47), 0.47, "0,47" },
				{ new Geld(2.00), 2.00, "2,00" },
				{ new Geld(2), 2.00, "2,00" },
				{ new Geld(3.5), 3.50, "3,50" },
				{ new Geld(3.52), 3.52, "3,52" },
				{ new Geld(4.369), 4.37, "4,37" },
				{ new Geld(45.491), 45.49, "45,49" },
				{ new Geld(112.4346), 112.43, "112,43" },
				{ new Geld(123.44567), 123.45, "123,45" },
				// groot positief
				{ new Geld(1245.40), 1245.40, "1.245,40" },
				{ new Geld(6378.4357), 6378.44, "6.378,44" },
				{ new Geld(76543.23), 76543.23, "76.543,23" },
				{ new Geld(23947502487.123), 23947502487.12, "23.947.502.487,12" },
				// String-constructor positief
				{ new Geld("12"), 12.00, "12,00" },
				{ new Geld("1245,30"), 1245.30, "1.245,30" },
				{ new Geld("6.578,45"), 6578.45, "6.578,45" },
				{ new Geld("123456789,50"), 123456789.50, "123.456.789,50" },
				{ new Geld("12.578.694,203"), 12578694.20, "12.578.694,20" },
				// klein negatief
				{ new Geld(-0.47), -0.47, "-0,47" },
				{ new Geld(-2.00), -2.00, "-2,00" },
				{ new Geld(-2), -2.00, "-2,00" },
				{ new Geld(-3.5), -3.50, "-3,50" },
				{ new Geld(-3.52), -3.52, "-3,52" },
				{ new Geld(-4.369), -4.37, "-4,37" },
				{ new Geld(-45.491), -45.49, "-45,49" },
				{ new Geld(-112.4346), -112.43, "-112,43" },
				{ new Geld(-123.44567), -123.45, "-123,45" },
				// groot negatief
				{ new Geld(-1245.40), -1245.40, "-1.245,40" },
				{ new Geld(-6378.4357), -6378.44, "-6.378,44" },
				{ new Geld(-76543.23), -76543.23, "-76.543,23" },
				{ new Geld(-23947502487.123), -23947502487.12, "-23.947.502.487,12" },
				// String-constructor negatief
				{ new Geld("-12"), -12.00, "-12,00" },
				{ new Geld("-1245,30"), -1245.30, "-1.245,30" },
				{ new Geld("-6.578,45"), -6578.45, "-6.578,45" },
				{ new Geld("-123456789,50"), -123456789.50, "-123.456.789,50" },
				{ new Geld("-12.578.694,203"), -12578694.20, "-12.578.694,20" },
				// Amerikaanse notatie
				{ new Geld("6,578.45"), 6578.45, "6.578,45" },
				{ new Geld("-6,578.45"), -6578.45, "-6.578,45" },
				{ new Geld("12,578,694.203"), 12578694.20, "12.578.694,20" },
				{ new Geld("-12,578,694.203"), -12578694.20, "-12.578.694,20" }
		};
		return Arrays.asList(values);
	}

	@Test
	public void testAfronden() {
		assertEquals(this.formattedString, this.geld.formattedString());
	}

	@Test
	public void testGetBedrag() {
		assertEquals(this.bedrag, this.geld.getBedrag(), 0);
	}

	@Test
	public void testStringConstructor() throws GeldParseException {
		assertEquals(this.geld, new Geld(this.formattedString));
	}

	@Test
	public void testToString() {
		assertEquals("<Geld[" + this.formattedString + "]>", this.geld.toString());
	}
}
