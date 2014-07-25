package org.rekeningsysteem.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;
import org.rekeningsysteem.utils.Try;

@RunWith(MockitoJUnitRunner.class)
public class TrySuccessTest extends EqualsHashCodeTest {

	private Try<Integer> success;
	@Mock private Stub stub;
	
	@Override
	protected Try<Integer> makeInstance() {
		return Try.of(12);
	}
	
	@Override
	protected Try<Integer> makeNotInstance() {
		return Try.of(13);
	}

	@Before
	public void setUp() {
		super.setUp();
		this.success = this.makeInstance();
	}

	@Test
	public void testIsSuccessSuccess() {
		assertTrue(this.success.isSuccess());
	}
	
	@Test
	public void testIsFailureSuccess() {
		assertFalse(this.success.isFailure());
	}
	
	@Test
	public void testGetSuccess() {
		assertEquals(new Integer(12), this.success.get());
	}

	@Test
	public void testThrowExceptionSuccess() {
		this.success.throwException();
	}

	@Test
	public void testIfPresentSuccess() {
		this.success.ifPresent(this.stub::consumer);
		verify(this.stub).consumer(eq(12));
		verifyNoMoreInteractions(this.stub);
	}

	@Test
	public void testFilterSuccess() {
		assertEquals(this.success, this.success.filter(i -> i % 2 == 0));
	}

	@Test (expected = IllegalStateException.class)
	public void testFilterSuccessNot() {
		this.success.filter(i -> i % 2 == 1).throwException();
	}

	@Test
	public void testFilterSuccessNull() {
		assertTrue(this.success.filter(null).isFailure());
	}

	@Test
	public void testMapSuccess() {
		this.success.map(this.stub::mapper);
		verify(this.stub).mapper(eq(12));
		verifyNoMoreInteractions(this.stub);
	}
	
	@Test
	public void testMapSuccessNull() {
		assertTrue(this.success.map(null).isFailure());
	}

	@Test
	public void testFlatMapSuccess() {
		this.success.flatMap(this.stub::flatMapper);
		verify(this.stub).flatMapper(eq(12));
		verifyNoMoreInteractions(this.stub);
	}
	
	@Test
	public void testFlatMapSuccessNull() {
		assertTrue(this.success.flatMap(null).isFailure());
	}

	@Test
	public void testOrElseSuccess() {
		assertEquals(this.success.get(), this.success.orElse(13));
	}

	@Test
	public void testOrElseGetSuccess() {
		this.success.orElseGet(this.stub::supplier);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testOrElseThrowSuccess() throws Exception {
		assertEquals(this.success.get(), this.success.orElseThrow(() -> new Exception()));
	}

	@Test
	public void testToStringSuccess() {
		assertEquals("Try.Success(12)", this.success.toString());
	}

	private class Stub {
		
		public void consumer(Integer i) {
			// do nothing
		}

		public Integer mapper(Integer i) {
			return i;
		}

		public Try<Integer> flatMapper(Integer i) {
			return Try.of(i);
		}

		public Integer supplier() {
			return 1;
		}
	}
}
