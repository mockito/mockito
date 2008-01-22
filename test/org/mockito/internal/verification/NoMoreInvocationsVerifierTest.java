/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Printable;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.progress.VerificationModeImpl;

public class NoMoreInvocationsVerifierTest extends RequiresValidState {

    private NoMoreInvocationsVerifier verifier;
    private InvocationsFinderStub finder;
    private ReporterStub reporterStub;

    @Before
    public void setup() {
        finder = new InvocationsFinderStub();
        reporterStub = new ReporterStub();
        verifier = new NoMoreInvocationsVerifier(finder, reporterStub);
    }
    
    @Test
    public void shouldNeverVerifyWhenVerificationIsExplicit() throws Exception {
        verifier.verify(null, null, VerificationModeImpl.atLeastOnce());
    }
    
    @Test
    public void shouldPassVerification() throws Exception {
        finder.firstUnverifiedToReturn = null;
        verifier.verify(null, null, VerificationModeImpl.noMoreInteractions());
    }
    
    @Test
    public void shouldReportError() throws Exception {
        Invocation firstUnverified = new InvocationBuilder().toInvocation();
        finder.firstUnverifiedToReturn = firstUnverified;
        List<Invocation> invocations = asList(new InvocationBuilder().toInvocation());
        
        verifier.verify(invocations, null, VerificationModeImpl.noMoreInteractions());
        
        assertSame(invocations, finder.invocations);
        
        assertEquals(firstUnverified, reporterStub.undesired);
        assertSame(firstUnverified.getStackTrace(), reporterStub.actualInvocationStackTrace);
    }
    
    class ReporterStub extends Reporter {
        private Printable undesired;
        private HasStackTrace actualInvocationStackTrace;
        @Override public void noMoreInteractionsWanted(Printable undesired, HasStackTrace actualInvocationStackTrace) {
            this.undesired = undesired;
            this.actualInvocationStackTrace = actualInvocationStackTrace;
        }
    }
}
