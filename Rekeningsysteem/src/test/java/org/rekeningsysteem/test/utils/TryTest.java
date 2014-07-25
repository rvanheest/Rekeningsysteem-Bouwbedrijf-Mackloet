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
import org.rekeningsysteem.utils.Try;

@RunWith(MockitoJUnitRunner.class)
public class TryTest {

	private Try<Integer> success;
	private Try<Integer> failure;
	@Mock private Stub stub;

	@Before
	public void setUp() {
		this.success = Try.of(12);
		this.failure = Try.failure(new Exception());
	}

	@Test
	public void testIsSuccessSuccess() {
		assertTrue(this.success.isSuccess());
	}
	
	@Test
	public void testIsSuccessFailure() {
		assertFalse(this.failure.isSuccess());
	}

	@Test
	public void testIsFailureSuccess() {
		assertFalse(this.success.isFailure());
	}
	
	@Test
	public void testIsFailureFailure() {
		assertTrue(this.failure.isFailure());
	}

	@Test
	public void testGetSuccess() {
		assertEquals(new Integer(12), this.success.get());
	}

	@Test (expected = IllegalStateException.class)
	public void testGetFailure() {
		this.failure.get();
	}

	@Test
	public void testThrowExceptionSuccess() {
		this.success.throwException();
	}

	@Test (expected = IllegalStateException.class)
	public void testThrowExceptionFailure() {
		this.failure.throwException();
	}

	@Test
	public void testIfPresentSuccess() {
		this.success.ifPresent(this.stub::consumer);
		verify(this.stub).consumer(eq(12));
		verifyNoMoreInteractions(this.stub);
	}

	@Test
	public void testIfPresentFailure() {
		this.failure.ifPresent(this.stub::consumer);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testFilterSuccess() {
		assertEquals(this.success, this.success.filter(i -> i % 2 == 0));
	}

	@Test (expected = IllegalStateException.class)
	public void testFilterSuccessNot() {
		this.success.filter(i -> i % 2 == 1).throwException();
	}

	@Test (expected = IllegalStateException.class)
	public void testFilterFailure() {
		this.failure.filter(i -> i % 2 == 1).throwException();
	}

	@Test
	public void testMapSuccess() {
		this.success.map(this.stub::mapper);
		verify(this.stub).mapper(eq(12));
		verifyNoMoreInteractions(this.stub);
	}

	@Test
	public void testMapFailure() {
		this.failure.map(this.stub::mapper);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testFlatMapSuccess() {
		this.success.flatMap(this.stub::flatMapper);
		verify(this.stub).flatMapper(eq(12));
		verifyNoMoreInteractions(this.stub);
	}

	@Test
	public void testFlatMapFailure() {
		this.failure.flatMap(this.stub::flatMapper);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testOrElseSuccess() {
		assertEquals(this.success.get(), this.success.orElse(13));
	}

	@Test
	public void testOrElseFailure() {
		assertEquals(new Integer(13), this.failure.orElse(13));
	}

	@Test
	public void testOrElseGetSuccess() {
		this.success.orElseGet(this.stub::supplier);
		verifyZeroInteractions(this.stub);
	}

	@Test
	public void testOrElseGetFailure() {
		this.failure.orElseGet(this.stub::supplier);
		verify(this.stub).supplier();
		verifyNoMoreInteractions(this.stub);
	}

	@Test
	public void testOrElseThrowSuccess() throws Exception {
		assertEquals(this.success.get(), this.success.orElseThrow(() -> new Exception()));
	}

	@Test (expected = Exception.class)
	public void testOrElseThrowFailure() throws Exception {
		this.failure.orElseThrow(() -> new Exception());
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
