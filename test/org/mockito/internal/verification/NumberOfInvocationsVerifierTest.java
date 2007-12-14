/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.progress.VerificationMode.atLeastOnce;
import static org.mockito.internal.progress.VerificationMode.times;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.progress.VerificationMode;

public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    private InvocationsCalculatorStub calculatorStub;
    private ReporterStub reporterStub;
    private InvocationMatcher wanted;
    private List<Invocation> invocations;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        calculatorStub = new InvocationsCalculatorStub();
        verifier = new NumberOfInvocationsVerifier(reporterStub, calculatorStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }

    @Test
    public void shouldNeverVerifyWhenAtLeastOnceVerification() throws Exception {
        verifier.verify(null, null, atLeastOnce());
    }
    
    @Test
    public void shouldVerifyOnlyWhenModeIsExplicit() {
        //TODO refactor to VM
        verifier.verify(null, null, VerificationMode.noMoreInteractions());
    }

    @Test
    public void shouldCountActualInvocations() throws Exception {
        verifier.verify(invocations, wanted, times(4));
        assertSame(wanted, calculatorStub.wanted);
    }
    
    @Test
    public void shouldAskCalculatorToCountActual() throws Exception {
        VerificationMode mode = times(1);
        calculatorStub.actualCountToReturn = 1;
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, calculatorStub.invocations);
        assertSame(wanted, calculatorStub.wanted);
    }
    
    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        VerificationMode mode = times(10);
        calculatorStub.actualCountToReturn = 5;
        MockitoException lastInvocation = new MockitoException("");
        calculatorStub.invocationTraceToReturn = lastInvocation;
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, calculatorStub.invocations);
        assertSame(wanted, calculatorStub.wanted);
        
        assertEquals(5, reporterStub.actualCount);
        assertEquals(10, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
        
        assertSame(lastInvocation, reporterStub.stackTrace);
    }
    
    @Test
    public void shouldReportTooManyInvocations() throws Exception {
        VerificationMode mode = times(0);
        calculatorStub.actualCountToReturn = 5;
        MockitoException firstUndesiredInvocation = new MockitoException("");
        calculatorStub.invocationTraceToReturn = firstUndesiredInvocation;
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, calculatorStub.invocations);
        assertSame(wanted, calculatorStub.wanted);
        
        assertSame(mode, calculatorStub.mode);
        
        assertEquals(5, reporterStub.actualCount);
        assertEquals(0, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
        
        assertSame(firstUndesiredInvocation, reporterStub.stackTrace);
    }
    
    class InvocationsCalculatorStub extends InvocationsCalculator {
        private HasStackTrace invocationTraceToReturn;
        private int actualCountToReturn;

        private InvocationMatcher wanted;
        private VerificationMode mode;
        private List<Invocation> invocations;
        @Override
        public int countActual(List<Invocation> invocations, InvocationMatcher wanted) {
            this.invocations = invocations;
            this.wanted = wanted;
            return actualCountToReturn;
        }
        
        @Override public HasStackTrace getFirstUndesiredInvocationStackTrace(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
            this.wanted = wanted;
            this.mode = mode;
            return invocationTraceToReturn;
        }
        
        @Override
        public HasStackTrace getLastInvocationStackTrace(List<Invocation> invocations, InvocationMatcher wanted) {
            this.wanted = wanted;
            return invocationTraceToReturn;
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
