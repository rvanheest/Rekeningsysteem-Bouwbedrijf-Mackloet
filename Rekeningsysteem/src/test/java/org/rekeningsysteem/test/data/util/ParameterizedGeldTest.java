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
	private final boolean zero;

	public ParameterizedGeldTest(Geld geld, double bedrag, String formattedString, boolean zero) {
		this.geld = geld;
		this.bedrag = bedrag;
		this.formattedString = formattedString;
		this.zero = zero;
	}

	@Parameters
	public static Collection<Object[]> data() throws GeldParseException {
		Object[][] values = new Object[][] {
				// klein positief
				{ new Geld(0), 0.0, "0,00", true },
				{ new Geld(0.47), 0.47, "0,47", false },
				{ new Geld(2.00), 2.00, "2,00", false },
				{ new Geld(2), 2.00, "2,00", false },
				{ new Geld(3.5), 3.50, "3,50", false },
				{ new Geld(3.52), 3.52, "3,52", false },
				{ new Geld(4.369), 4.37, "4,37", false },
				{ new Geld(45.491), 45.49, "45,49", false },
				{ new Geld(112.4346), 112.43, "112,43", false },
				{ new Geld(123.44567), 123.45, "123,45", false },
				// groot positief
				{ new Geld(1245.40), 1245.40, "1.245,40", false },
				{ new Geld(6378.4357), 6378.44, "6.378,44", false },
				{ new Geld(76543.23), 76543.23, "76.543,23", false },
				{ new Geld(23947502487.123), 23947502487.12, "23.947.502.487,12", false },
				// String-constructor positief
				{ new Geld("12"), 12.00, "12,00", false },
				{ new Geld("1245,30"), 1245.30, "1.245,30", false },
				{ new Geld("6.578,45"), 6578.45, "6.578,45", false },
				{ new Geld("123456789,50"), 123456789.50, "123.456.789,50", false },
				{ new Geld("12.578.694,203"), 12578694.20, "12.578.694,20", false },
				// klein negatief
				{ new Geld(-0), 0.0, "0,00", true },
				{ new Geld(-0.47), -0.47, "-0,47", false },
				{ new Geld(-2.00), -2.00, "-2,00", false },
				{ new Geld(-2), -2.00, "-2,00", false },
				{ new Geld(-3.5), -3.50, "-3,50", false },
				{ new Geld(-3.52), -3.52, "-3,52", false },
				{ new Geld(-4.369), -4.37, "-4,37", false },
				{ new Geld(-45.491), -45.49, "-45,49", false },
				{ new Geld(-112.4346), -112.43, "-112,43", false },
				{ new Geld(-123.44567), -123.45, "-123,45", false },
				// groot negatief
				{ new Geld(-1245.40), -1245.40, "-1.245,40", false },
				{ new Geld(-6378.4357), -6378.44, "-6.378,44", false },
				{ new Geld(-76543.23), -76543.23, "-76.543,23", false },
				{ new Geld(-23947502487.123), -23947502487.12, "-23.947.502.487,12", false },
				// String-constructor negatief
				{ new Geld("0"), 0.0, "0,00", true },
				{ new Geld("0,00"), 0.0, "0,00", true },
				{ new Geld("-12"), -12.00, "-12,00", false },
				{ new Geld("-1245,30"), -1245.30, "-1.245,30", false },
				{ new Geld("-6.578,45"), -6578.45, "-6.578,45", false },
				{ new Geld("-123456789,50"), -123456789.50, "-123.456.789,50", false },
				{ new Geld("-12.578.694,203"), -12578694.20, "-12.578.694,20", false },
				// Amerikaanse notatie
				{ new Geld("0.00"), 0.0, "0,00", true },
				{ new Geld("6,578.45"), 6578.45, "6.578,45", false },
				{ new Geld("-6,578.45"), -6578.45, "-6.578,45", false },
				{ new Geld("12,578,694.203"), 12578694.20, "12.578.694,20", false },
				{ new Geld("-12,578,694.203"), -12578694.20, "-12.578.694,20", false }
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
	public void testIsZero() {
		assertEquals(this.zero, this.geld.isZero());
	}

	@Test
	public void testToString() {
		assertEquals("<Geld[" + this.formattedString + "]>", this.geld.toString());
	}
}
