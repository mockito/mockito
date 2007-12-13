/*
 * Copyright (c) 2007, Szczepan Faber. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsMarker;
import org.mockito.internal.progress.OngoingVerifyingMode;

import static org.mockito.util.ExtraMatchers.*;
import static org.junit.Assert.*;

public class VerifyingRecorderTest {
    
    private List<Invocation> invocationsMarkedAsVerified;
    
    class InvocationsMarkerExtension extends InvocationsMarker {
        @Override
        public void markInvocationsAsVerified(List<Invocation> invocations, InvocationMatcher wanted,
                OngoingVerifyingMode mode) {
            invocationsMarkedAsVerified = invocations;
        }
    }

    @Test
    public void shouldMarkInvocationsAsVerified() {
        VerifyingRecorder<Object> recorder = new VerifyingRecorder<Object>(new InvocationsMarkerExtension(), Collections.<Verifier>emptyList());
        Invocation recorded = new InvocationBuilder().method("simpleMethod").toInvocation();
        recorder.recordInvocation(recorded);
        
        InvocationMatcher wanted = new InvocationBuilder().method("differentMethod").toInvocationMatcher();
        recorder.verify(wanted, OngoingVerifyingMode.atLeastOnce());
        
        assertThat(invocationsMarkedAsVerified, collectionHasExactlyInOrder(recorded));
    }
}
