/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

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
		this.delayMillis = delayMillis;
		this.delegate = delegate;
	}

	/**
	 * Verify the given ongoing verification data, and confirm that it satisfies
	 * the delegate verification mode after a given delay.
	 */
	public void verify(VerificationData data) {
		try {
			Thread.sleep(delayMillis);
			delegate.verify(data);
		} catch (InterruptedException e) {
			throw new RuntimeException("Thread sleep has been interrupted", e);
		}

	}

	public VerificationAfterDelayImpl copyWithVerificationMode(VerificationMode verificationMode) {
		return new VerificationAfterDelayImpl(delayMillis,  verificationMode);
	}

	@Override
	public VerificationMode description(String description) {
		return VerificationModeFactory.description(this, description);
	}
}