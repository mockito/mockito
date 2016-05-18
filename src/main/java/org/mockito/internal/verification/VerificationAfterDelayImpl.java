/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.lang.Math.max;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.TimeUnit;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Verifies that another verification mode (the delegate) is satisfied after a
 * certain timeframe, measured from the call to verify().
 */
public class VerificationAfterDelayImpl implements VerificationMode {

	private final long delayMillis;
	private final VerificationMode delegate;

	/**
	 * Create this verification mode, to be used to verify invocation ongoing
	 * data later.
	 *
	 * @param delayMillis
	 *            The frequency to poll delegate.verify(), to check whether the
	 *            delegate has been satisfied
	 * @param delegate
	 *            The verification mode to delegate overall success or failure
	 *            to
	 */
	public VerificationAfterDelayImpl(long delayMillis, VerificationMode delegate) {
		checkNotNegative(delayMillis);
		this.delayMillis = delayMillis;
		this.delegate = delegate;
	}

	/**
	 * Verify the given ongoing verification data, and confirm that it satisfies
	 * the delegate verification mode after a given delay.
	 */
	public void verify(VerificationData data) {
		boolean interrupted = false;
	    try {
	      long remainingMillis = delayMillis;
	      long end = System.currentTimeMillis() + remainingMillis;

	      while (true) {
	        try {
	          Thread.sleep(remainingMillis);
	          break;
	        } catch (InterruptedException e) {
	          interrupted = true;
	          remainingMillis = end - System.currentTimeMillis();
	        }
	      }
	      delegate.verify(data);
	    } finally {
	      if (interrupted) {
	        currentThread().interrupt();
	      }
	    }

		

	}

	public VerificationMode description(String description) {
		return VerificationModeFactory.description(this, description);
	}

	public VerificationAfterDelayImpl copyWithVerificationMode(VerificationMode verificationMode) {
		return new VerificationAfterDelayImpl(delayMillis, verificationMode);
	}

	private static void checkNotNegative(long delayMillis) {
		if (delayMillis < 0)
			new Reporter().cannotCreateTimerWithNegativeDurationTime(delayMillis);
	}

}