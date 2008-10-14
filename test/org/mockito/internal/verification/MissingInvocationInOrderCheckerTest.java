/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.verification.checkers.MissingInvocationInOrderChecker;
import org.mockitoutil.TestBase;

public class MissingInvocationInOrderCheckerTest extends TestBase {

    private MissingInvocationInOrderChecker verifier;
    private ReporterStub reporterStub;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        verifier = new MissingInvocationInOrderChecker(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }                                                                    

    @Test
    public void shouldPassWhenMatchingInteractionFound() throws Exception {
        Invocation actual = new InvocationBuilder().toInvocation();
        finderStub.allMatchingUnverifiedChunksToReturn.add(actual);
        
        verifier.check(invocations, wanted, new VerificationModeBuilder().inOrder());
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() throws Exception {
        assertTrue(finderStub.allMatchingUnverifiedChunksToReturn.isEmpty());
        verifier.check(invocations, wanted, new VerificationModeBuilder().inOrder());
        
        assertEquals(wanted, reporterStub.wanted);
    }
    
    @Test
    public void shouldReportWantedDiffersFromActual() throws Exception {
        Invocation previous = new InvocationBuilder().toInvocation();
        finderStub.previousInOrderToReturn = previous;
        
        verifier.check(invocations, wanted, new VerificationModeBuilder().inOrder());
        
        assertEquals(wanted, reporterStub.wanted);
        assertEquals(previous, reporterStub.previous);
        assertSame(previous.getStackTrace(), reporterStub.previousStackTrace);
    }
    
    class ReporterStub extends Reporter {
        private PrintableInvocation wanted;
        private PrintableInvocation previous;
        private HasStackTrace previousStackTrace;
        
        @Override public void wantedButNotInvokedInOrder(PrintableInvocation wanted, PrintableInvocation previous, HasStackTrace previousStackTrace) {
            this.wanted = wanted;
            this.previous = previous;
            this.previousStackTrace = previousStackTrace;
        }
        
        @Override public void wantedButNotInvoked(PrintableInvocation wanted) {
            this.wanted = wanted;
        }
    }
}