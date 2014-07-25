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
public class TryFailureTest extends EqualsHashCodeTest {

	private Try<Integer> failure;
	@Mock private Stub stub;
	
	@Override
	protected Try<Integer> makeInstance() {
		return Try.failure(new Exception("foobar"));
	}
	
	@Override
	protected Try<Integer> makeNotInstance() {
		return Try.failure(new IllegalArgumentException("foobaasfr"));
	}

	@Before
	public void setUp() {
		super.setUp();
		this.failure = this.makeInstance();
	}

	@Test
	public void testIsSuccessFailure() {
		assertFalse(this.failure.isSuccess());
	}

	@Test
	public void testIsFailureFailure() {
		assertTrue(this.failure.isFailure());
	}

	@Test (expected = IllegalStateException.class)
	public void testGetFailure() {
		this.failure.get();
	}

	@Test (expected = IllegalStateException.class)
	public void testThrowExceptionFailure() {
		this.failure.throwException();
	}

	@Test
	public void testIfPresentFailure() {
		this.failure.ifPresent(this.stub::consumer);
		verifyZeroInteractions(this.stub);
	}

	@Test (expected = IllegalStateException.class)
	public void testFilterFailure() {
		this.failure.filter(i -> i % 2 == 1).throwException();
	}

	@Test
	public void testMapFailure() {
		this.failure.map(this.stub::mapper);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testFlatMapFailure() {
		this.failure.flatMap(this.stub::flatMapper);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testOrElseFailure() {
		assertEquals(new Integer(13), this.failure.orElse(13));
	}

	@Test
	public void testOrElseGetFailure() {
		this.failure.orElseGet(this.stub::supplier);
		verify(this.stub).supplier();
		verifyNoMoreInteractions(this.stub);
	}

	@Test (expected = Exception.class)
	public void testOrElseThrowFailure() throws Exception {
		this.failure.orElseThrow(() -> new Exception());
	}

	@Test
	public void testToStringFailure() {
		assertEquals("Try.Failure(java.lang.Exception: foobar)", this.failure.toString());
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
