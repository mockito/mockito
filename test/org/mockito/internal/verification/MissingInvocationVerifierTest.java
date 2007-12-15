/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.progress.VerificationMode.atLeastOnce;
import static org.mockito.internal.progress.VerificationMode.noMoreInteractions;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.progress.VerificationMode;

public class MissingInvocationVerifierTest extends RequiresValidState {

    private MissingInvocationVerifier verifier;
    private InvocationsCalculatorStub calculatorStub;
    private ReporterStub reporterStub;
    
    private InvocationMatcher wanted;
    private List<Invocation> invocations;

    @Before
    public void setup() {
        calculatorStub = new InvocationsCalculatorStub();
        reporterStub = new ReporterStub();
        verifier = new MissingInvocationVerifier(calculatorStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }
    
    @Test
    public void shouldNeverVerifyWhenModeIsNotMissingMethodMode() {
        verifier.verify(null, null, noMoreInteractions());
    }
    
    @Test
    public void shouldAskCalculatorForActualNumberOfInvocations() {
        calculatorStub.actualCountToReturn = 1;
        verifier.verify(invocations, wanted, atLeastOnce());
        
        assertSame(invocations, calculatorStub.invocations);
        assertSame(wanted, calculatorStub.wanted);
    }
    
    @Test
    public void shouldPassBecauseActualInvocationFound() {
        calculatorStub.actualCountToReturn = 1;
        verifier.verify(invocations, wanted, atLeastOnce());
    }
    
    @Test
    public void shouldAskCalculatorForActualInvocationAndReportWantedButNotInvoked() {
        calculatorStub.actualCountToReturn = 0;
        calculatorStub.actualInvocationToReturn = null;
        verifier.verify(invocations, wanted, VerificationMode.atLeastOnce());
        
        assertSame(invocations, calculatorStub.invocations);
        assertSame(wanted, calculatorStub.wanted);
        
        assertEquals(wanted.toString(), reporterStub.wanted);
    }
    
    @Test
    public void shouldReportWantedInvocationDiffersFromActual() {
        calculatorStub.actualCountToReturn = 0;
        Invocation actualInvocation = new InvocationBuilder().toInvocation();
        calculatorStub.actualInvocationToReturn = actualInvocation;
        verifier.verify(invocations, wanted, VerificationMode.atLeastOnce());
        
        assertEquals(wanted.toString(), reporterStub.wanted);
        assertEquals(actualInvocation.toString(), reporterStub.actual);
        assertSame(actualInvocation.getStackTrace(), reporterStub.actualInvocationStackTrace);
    }
    
    class InvocationsCalculatorStub extends InvocationsCalculator {
        private List<Invocation> invocations;
        private InvocationMatcher wanted;
        private int actualCountToReturn;
        private Invocation actualInvocationToReturn;
        @Override public int countActual(List<Invocation> invocations, InvocationMatcher wanted) {
            this.invocations = invocations;
            this.wanted = wanted;
            return actualCountToReturn;
        }
        @Override public Invocation findActualInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
            this.invocations = invocations;
            this.wanted = wanted;
            return actualInvocationToReturn;
        }
    }
    
    class ReporterStub extends Reporter {
        private String wanted;
        private String actual;
        private HasStackTrace actualInvocationStackTrace;
        @Override public void wantedButNotInvoked(String wanted) {
            this.wanted = wanted;
        }
        @Override public void wantedInvocationDiffersFromActual(String wanted, String actual, HasStackTrace actualInvocationStackTrace) {
                    this.wanted = wanted;
                    this.actual = actual;
                    this.actualInvocationStackTrace = actualInvocationStackTrace;
        }
    }
}
