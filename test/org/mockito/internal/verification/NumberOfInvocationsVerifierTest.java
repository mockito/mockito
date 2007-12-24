/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

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
    private ActualInvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        analyzerStub = new InvocationsAnalyzerStub();
        finderStub = new ActualInvocationsFinderStub();
        verifier = new NumberOfInvocationsVerifier(reporterStub, analyzerStub, finderStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = asList(new InvocationBuilder().toInvocation());
    }

    @Test
    public void shouldNeverVerifyWhenNotModeIsNotExactNumberOfInvocationsMode() throws Exception {
        verifier.verify(null, null, atLeastOnce());
    }
    
    @Test
    public void shouldReportTooLittleActual() throws Exception {
        VerificationModeImpl mode = times(100);
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        
        verifier.verify(invocations, wanted, mode);
        
        assertEquals(1, reporterStub.actualCount);
        assertEquals(100, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
    }

    @Test
    public void shouldReportWithLastInvocationStackTrace() throws Exception {
        VerificationModeImpl mode = times(100);
        MockitoException lastInvocation = new MockitoException("");
        analyzerStub.invocationTraceToReturn = lastInvocation;
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(lastInvocation, reporterStub.stackTrace);
    }
    
    @Test
    public void shouldAskAnalyzerWithActualInvocationsWhenTooLittleActual() throws Exception {
        VerificationModeImpl mode = times(100);
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(finderStub.actualToReturn, analyzerStub.invocations);
    }
    
    @Test
    public void shouldReportWithFirstUndesiredInvocationStackTrace() throws Exception {
        VerificationModeImpl mode = times(0);
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        MockitoException firstUndesiredInvocation = new MockitoException("");
        analyzerStub.invocationTraceToReturn = firstUndesiredInvocation;
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(firstUndesiredInvocation, reporterStub.stackTrace);
    }
    
    @Test
    public void shouldAskAnalyzerWithActualInvocationsWhenTooManyActual() throws Exception {
        VerificationModeImpl mode = times(0);
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        
        verifier.verify(invocations, wanted, mode);
        
        assertSame(finderStub.actualToReturn, analyzerStub.invocations);
    }
    
    @Test
    public void shouldReportTooManyActual() throws Exception {
        VerificationModeImpl mode = times(0);
        finderStub.actualToReturn.add(new InvocationBuilder().toInvocation());
        
        verifier.verify(invocations, wanted, mode);
        
        assertEquals(1, reporterStub.actualCount);
        assertEquals(0, reporterStub.wantedCount);
        assertEquals(wanted.toString(), reporterStub.wanted);
    }
    
    class InvocationsAnalyzerStub extends InvocationsAnalyzer {
        private HasStackTrace invocationTraceToReturn;
        private List<Invocation> invocations;
        @Override public HasStackTrace findFirstUndesiredInvocationTrace(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
            this.invocations = invocations;
            return invocationTraceToReturn;
        }
        @Override public HasStackTrace findLastMatchingInvocationTrace(List<Invocation> invocations, InvocationMatcher wanted) {
            this.invocations = invocations;
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
