/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.progress.VerificationMode.atLeastOnce;
import static org.mockito.internal.progress.VerificationMode.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.progress.VerificationMode;

public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    private InvocationsCalculatorStub calculatorStub;
    private ReporterStub reporterStub;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        verifier = new NumberOfInvocationsVerifier(reporterStub);
        calculatorStub = new InvocationsCalculatorStub();
    }

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenAtLeastOnceVerification() throws Exception {
        verifier.verify(null, null, atLeastOnce());
    }

    @Test
    public void shouldCountActualInvocations() throws Exception {
        InvocationMatcher wanted = new InvocationBuilder().toInvocationMatcher();
        verifier.verify(calculatorStub, wanted, times(4));
        assertSame(wanted, calculatorStub.wantedForCountingActual);
    }
    
    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        InvocationMatcher wanted = new InvocationBuilder().toInvocationMatcher();
        VerificationMode mode = times(10);
        verifier.verify(calculatorStub, wanted, mode);
        
        assertSame(wanted, calculatorStub.wantedForGettingTrace);
        
        assertEquals(5, reporterStub.actualCount);
        assertEquals(10, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
        
        assertSame(calculatorStub.lastInvocation, reporterStub.stackTrace);
    }
    
    @Test
    public void shouldReportTooManyInvocations() throws Exception {
        InvocationMatcher wanted = new InvocationBuilder().toInvocationMatcher();
        VerificationMode mode = times(0);
        verifier.verify(calculatorStub, wanted, mode);
        
        assertSame(wanted, calculatorStub.wantedForGettingTrace);
        assertSame(mode, calculatorStub.mode);
        
        assertEquals(5, reporterStub.actualCount);
        assertEquals(0, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
        
        assertSame(calculatorStub.firstUndesired, reporterStub.stackTrace);
    }
    
    class InvocationsCalculatorStub extends InvocationsCalculator {
        private final HasStackTrace firstUndesired = new MockitoException("");
        private final HasStackTrace lastInvocation = new MockitoException("");

        private InvocationMatcher wantedForCountingActual;
        private InvocationMatcher wantedForGettingTrace;
        private VerificationMode mode;
        @Override
        public int countActual(InvocationMatcher wanted) {
            this.wantedForCountingActual = wanted;
            return 5;
        }
        
        @Override public HasStackTrace getFirstUndesiredInvocationStackTrace(InvocationMatcher wanted, VerificationMode mode) {
            wantedForGettingTrace = wanted;
            this.mode = mode;
            return firstUndesired;
        }
        
        @Override
        public HasStackTrace getLastInvocationStackTrace(InvocationMatcher wanted) {
            wantedForGettingTrace = wanted;
            return lastInvocation;
        }
    }
    
    class ReporterStub extends Reporter {
        private int wantedCount;
        private int actualCount;
        private String wanted;
        private HasStackTrace stackTrace;
        @Override public void tooLittleActualInvocations(int wantedCount, int actualCount, String wanted, HasStackTrace lastActualInvocationStackTrace) {
                    this.wantedCount = wantedCount;
                    this.actualCount = actualCount;
                    this.wanted = wanted;
                    this.stackTrace = lastActualInvocationStackTrace;
        }
        
        @Override public void tooManyActualInvocations(int wantedCount, int actualCount, String wanted, HasStackTrace firstUndesired) {
                    this.wantedCount = wantedCount;
                    this.actualCount = actualCount;
                    this.wanted = wanted;
                    this.stackTrace = firstUndesired;
        }
    }
}
