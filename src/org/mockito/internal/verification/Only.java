/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class Only implements VerificationMode {

	private final InvocationsFinder finder = new InvocationsFinder();
	private final InvocationMarker marker = new InvocationMarker();
	private final Reporter reporter = new Reporter();

	@SuppressWarnings("unchecked")
    public void verify(VerificationData data) {
		InvocationMatcher wantedMatcher = data.getWanted();
		List<Invocation> invocations = data.getAllInvocations();
		List<Invocation> chunk = finder.findInvocations(invocations,wantedMatcher);
		if (invocations.size() != 1 && chunk.size() > 0) {			
			Invocation unverified = finder.findFirstUnverified(invocations);
			reporter.noMoreInteractionsWanted(unverified, (List) invocations);
		} else if (invocations.size() != 1 || chunk.size() == 0) {
			reporter.wantedButNotInvoked(wantedMatcher);
		}
		marker.markVerified(chunk.get(0), wantedMatcher);
	}
}
