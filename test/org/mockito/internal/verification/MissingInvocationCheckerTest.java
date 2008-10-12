/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockitoutil.TestBase;

public class MissingInvocationCheckerTest extends TestBase {

    private MissingInvocationChecker verifier;
    
    private InvocationsFinderStub finderStub;
    private ReporterStub reporterStub;
    
    private InvocationMatcher wanted;
    private List<Invocation> invocations;

    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        verifier = new MissingInvocationChecker(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }
    
    @Test
    public void shouldAskFinderForActualInvocations() {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        verifier.verify(invocations, wanted);
        
        assertSame(invocations, finderStub.invocations);
    }
    
    @Test
    public void shouldPassBecauseActualInvocationFound() {
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        verifier.verify(invocations, wanted);
    }
    
    @Test
    public void shouldAskAnalyzerForSimilarInvocation() {
        verifier.verify(invocations, wanted);
        
        assertSame(invocations, finderStub.invocations);
    }
    
    @Test
    public void shouldReportWantedButNotInvoked() {
        assertTrue(finderStub.actualToReturn.isEmpty());
        finderStub.similarToReturn = null;
        
        verifier.verify(invocations, wanted);
        
        assertEquals(wanted, reporterStub.wanted);
        assertNull(reporterStub.actualInvocationStackTrace);
    }
    
    @Test
    public void shouldReportWantedInvocationDiffersFromActual() {
        assertTrue(finderStub.actualToReturn.isEmpty());
        Invocation actualInvocation = new InvocationBuilder().toInvocation();
        finderStub.similarToReturn = actualInvocation;
        
        verifier.verify(invocations, wanted);
        
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
