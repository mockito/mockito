/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.progress.VerificationModeImpl.atLeastOnce;
import static org.mockito.internal.progress.VerificationModeImpl.times;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsAnalyzer;
import org.mockito.internal.progress.VerificationModeImpl;

public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    private InvocationsAnalyzerStub analyzerStub;
    private ReporterStub reporterStub;
    private InvocationMatcher wanted;
    private List<Invocation> invocations;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        analyzerStub = new InvocationsAnalyzerStub();
        verifier = new NumberOfInvocationsVerifier(reporterStub, analyzerStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }

    @Test
    public void shouldNeverVerifyWhenNotModeIsNotExactNumberOfInvocationsMode() throws Exception {
        verifier.verify(null, null, atLeastOnce());
    }
    
    @Test
    public void shouldCountActualInvocations() throws Exception {
        verifier.verify(invocations, wanted, times(4));
        assertSame(wanted, analyzerStub.wanted);
    }
    
    @Test
    public void shouldAskAnalyzerToCountActual() throws Exception {
        VerificationModeImpl mode = times(1);
        analyzerStub.actualCountToReturn = 1;
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, analyzerStub.invocations);
        assertSame(wanted, analyzerStub.wanted);
    }
    
    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        VerificationModeImpl mode = times(10);
        analyzerStub.actualCountToReturn = 5;
        MockitoException lastInvocation = new MockitoException("");
        analyzerStub.invocationTraceToReturn = lastInvocation;
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, analyzerStub.invocations);
        assertSame(wanted, analyzerStub.wanted);
        
        assertEquals(5, reporterStub.actualCount);
        assertEquals(10, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
        
        assertSame(lastInvocation, reporterStub.stackTrace);
    }
    
    @Test
    public void shouldReportTooManyInvocations() throws Exception {
        VerificationModeImpl mode = times(0);
        analyzerStub.actualCountToReturn = 5;
        MockitoException firstUndesiredInvocation = new MockitoException("");
        analyzerStub.invocationTraceToReturn = firstUndesiredInvocation;
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(invocations, analyzerStub.invocations);
        assertSame(wanted, analyzerStub.wanted);
        
        assertSame(mode, analyzerStub.mode);
        
        assertEquals(5, reporterStub.actualCount);
        assertEquals(0, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
        
        assertSame(firstUndesiredInvocation, reporterStub.stackTrace);
    }
    
    class InvocationsAnalyzerStub extends InvocationsAnalyzer {
        private HasStackTrace invocationTraceToReturn;
        private int actualCountToReturn;

        private InvocationMatcher wanted;
        private VerificationModeImpl mode;
        private List<Invocation> invocations;
        @Override
        public int countActual(List<Invocation> invocations, InvocationMatcher wanted) {
            this.invocations = invocations;
            this.wanted = wanted;
            return actualCountToReturn;
        }
        
        @Override public HasStackTrace findFirstUndesiredInvocationTrace(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
            this.wanted = wanted;
            this.mode = mode;
            return invocationTraceToReturn;
        }
        
        @Override
        public HasStackTrace findLastMatchingInvocationTrace(List<Invocation> invocations, InvocationMatcher wanted) {
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
