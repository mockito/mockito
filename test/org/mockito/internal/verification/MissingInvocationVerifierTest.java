/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockitoutil.TestBase;

public class MissingInvocationVerifierTest extends TestBase {

    private MissingInvocationVerifier verifier;
    
    private InvocationsFinderStub finderStub;
    private ReporterStub reporterStub;
    
    private InvocationMatcher wanted;
    private List<Invocation> invocations;

    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        verifier = new MissingInvocationVerifier(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }
    
    @Test
    public void shouldNeverVerifyWhenModeIsNotMissingMethodMode() {
        verifier.verify(null, null, noMoreInteractions());
    }
    
    @Test
    public void shouldAskFinderForActualInvocations() {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        VerificationModeImpl mode = atLeastOnce();
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, finderStub.invocations);
    }
    
    @Test
    public void shouldPassBecauseActualInvocationFound() {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        verifier.verify(invocations, wanted, atLeastOnce());
    }
    
    @Test
    public void shouldAskAnalyzerForSimilarInvocation() {
        verifier.verify(invocations, wanted, VerificationModeImpl.atLeastOnce());
        
        assertSame(invocations, finderStub.invocations);
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() {
        assertTrue(finderStub.actualToReturn.isEmpty());
        finderStub.similarToReturn = null;
        
        verifier.verify(invocations, wanted, VerificationModeImpl.atLeastOnce());
        
        assertEquals(wanted, reporterStub.wanted);
        assertNull(reporterStub.actualInvocationStackTrace);
    }
    
    @Test
    public void shouldReportWantedInvocationDiffersFromActual() {
        assertTrue(finderStub.actualToReturn.isEmpty());
        Invocation actualInvocation = new InvocationBuilder().toInvocation();
        finderStub.similarToReturn = actualInvocation;
        
        verifier.verify(invocations, wanted, VerificationModeImpl.atLeastOnce());
        
        assertNotNull(reporterStub.wanted);
        assertNotNull(reporterStub.actual);
        
        assertSame(actualInvocation.getStackTrace(), reporterStub.actualInvocationStackTrace);
    }
    
    class ReporterStub extends Reporter {
        private PrintableInvocation wanted;
        private PrintableInvocation actual;
        private HasStackTrace actualInvocationStackTrace;
        @Override public void wantedButNotInvoked(PrintableInvocation wanted) {
            this.wanted = wanted;
        }
        
        @Override public void argumentsAreDifferent(PrintableInvocation wanted, PrintableInvocation actual, HasStackTrace actualInvocationStackTrace) {
                    this.wanted = wanted;
                    this.actual = actual;
                    this.actualInvocationStackTrace = actualInvocationStackTrace;
        }
    }
}
