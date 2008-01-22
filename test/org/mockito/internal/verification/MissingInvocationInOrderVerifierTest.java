/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Printable;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeBuilder;

public class MissingInvocationInOrderVerifierTest extends RequiresValidState {

    private MissingInvocationInOrderVerifier verifier;
    private ReporterStub reporterStub;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        verifier = new MissingInvocationInOrderVerifier(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }                                                                    

    @Test
    public void shouldNeverVerifyIfModeIsNotMissingInvocationInOrderMode() throws Exception {
        verifier.verify(null, null, atLeastOnce());
    }
    
    @Test
    public void shouldPassWhenMatchingInteractionFound() throws Exception {
        Invocation actual = new InvocationBuilder().toInvocation();
        finderStub.allMatchingUnverifiedChunksToReturn.add(actual);
        
        verifier.verify(invocations, wanted, new VerificationModeBuilder().inOrder());
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() throws Exception {
        assertTrue(finderStub.allMatchingUnverifiedChunksToReturn.isEmpty());
        verifier.verify(invocations, wanted, new VerificationModeBuilder().inOrder());
        
        assertEquals(wanted, reporterStub.wanted);
    }
    
    @Test
    public void shouldReportWantedDiffersFromActual() throws Exception {
        Invocation previous = new InvocationBuilder().toInvocation();
        finderStub.previousInOrderToReturn = previous;
        
        verifier.verify(invocations, wanted, new VerificationModeBuilder().inOrder());
        
        assertEquals(wanted, reporterStub.wanted);
        assertEquals(previous, reporterStub.previous);
        assertSame(previous.getStackTrace(), reporterStub.previousStackTrace);
    }
    
    class ReporterStub extends Reporter {
        private Printable wanted;
        private Object previous;
        private HasStackTrace previousStackTrace;
        
        @Override public void wantedButNotInvokedInOrder(Printable wanted, Object previous, HasStackTrace previousStackTrace) {
            this.wanted = wanted;
            this.previous = previous;
            this.previousStackTrace = previousStackTrace;
        }
        
        @Override public void wantedButNotInvoked(Printable wanted) {
            this.wanted = wanted;
        }
    }
}