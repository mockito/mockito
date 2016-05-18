/*
 * Copyright (c) 2007 Mockito contributors This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class VerificationAfterDelayTest {
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Mock
	private List<Integer> mock;

	@Captor
	private ArgumentCaptor<Integer> captor;
	private ScheduledFuture<?> f;

	@After
	public void teardown() throws InterruptedException, ExecutionException {
		// making sure there are no threading related exceptions
		assertNoAsyncExceptionWereThrown();
	}

	@Test
	public void shouldVerifyNormallyWithSpecificTimes() throws Exception {
		clearAsyncAfter(1, MILLISECONDS);

		verify(mock, after(50).times(1)).clear();
	}

	@Test
	public void shouldVerifyNormallyWithAtLeast() throws Exception {
		clearAsyncAfter(1, MILLISECONDS);

		verify(mock, after(50).atLeast(1)).clear();
	}

	@Test
	public void shouldFailVerificationWithWrongTimes() throws Exception {
		clearAsyncAfter(10, MILLISECONDS);

		expected.expect(MockitoAssertionError.class);
		verify(mock, after(50).times(2)).clear();
	}

	@Test
	public void shouldWaitTheFullTimeIfTheTestCouldPass() throws Exception {
		clearAsyncAfter(1, MILLISECONDS);

		long startTime = currentTimeMillis();

		try {
			verify(mock, after(50).atLeast(2)).clear();
			fail();
		} catch (MockitoAssertionError e) {
		}

		assertThat(currentTimeMillis() - startTime).isGreaterThanOrEqualTo(50);
	}

	@Test
	public void shouldWaitTheFullTimeEvenIfInterrupted() throws Exception {

		interruptCurrentThreadAfter(30, MILLISECONDS);

		long startTime = currentTimeMillis();

		try {
			verify(mock, after(50).never()).clear();
		} catch (MockitoAssertionError e) {
		}

		assertThat(currentTimeMillis() - startTime).isGreaterThanOrEqualTo(50);
	}

	@Ignore("This test case is wrong! Mockito.after() must not stop if the verification fails, that is only true for Mockito.timeout().")
	@Test(timeout = 100)
	public void shouldStopEarlyIfTestIsDefinitelyFailed() throws Exception {
		clearAsyncAfter(1, MILLISECONDS);

		expected.expect(MockitoAssertionError.class);
		verify(mock, after(10000).never()).clear();
	}

	/**
	 * Test for issue #345.
	 */
	@Test
	public void shouldCaptureAllArgumentsAfterAtMostVerification() {
		// given
		int n = 3;

		// when
		exerciseMockNTimes(n);

		// then
		verify(mock, after(10).atMost(n)).add(captor.capture());
		List<Integer> values = captor.getAllValues();

		assertThat(values).containsExactly(0, 1, 2);
	}

	@Test
	public void shouldCaptureAllArgumentsAfterAtTimesVerification() {
		// given
		int n = 3;

		// when
		exerciseMockNTimes(n);

		// then
		verify(mock, after(10).times(n)).add(captor.capture());
		List<Integer> values = captor.getAllValues();
		assertThat(values).containsExactly(0, 1, 2);
	}

	private void exerciseMockNTimes(int n) {
		for (int i = 0; i < n; i++) {
			mock.add(i);
		}
	}

	/**
	 * Calls mock.clear() with an other Thread after the given delay.
	 */
	private void clearAsyncAfter(final int delay, TimeUnit unit) {
		ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
		f = e.schedule(new Runnable() {

			public void run() {
				mock.clear();
			}
		}, delay, unit);
	}

	private void assertNoAsyncExceptionWereThrown() throws InterruptedException, ExecutionException {
		if (f == null)
			return;
		f.cancel(true);
		try {
			f.get();
		} catch (CancellationException ignored) {
		}
	}

	/**
	 * Interrupts the current Thread after the given delay.
	 */
	private void interruptCurrentThreadAfter(int delay, TimeUnit unit) {
		final Thread testThread = Thread.currentThread();

		final ScheduledExecutorService e = newSingleThreadScheduledExecutor();
		e.schedule(new Runnable() {
			public void run() {
				try {
					testThread.interrupt();
				} finally {
					e.shutdownNow();
				}
			}
		}, delay, unit);
	}
}