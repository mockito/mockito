/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationsAnalyzer;
import org.mockito.internal.progress.VerificationModeImpl;

public class NoMoreInvocationsVerifierTest extends RequiresValidState {

    private NoMoreInvocationsVerifier verifier;
    private InvocationsAnalyzerStub analyzer;
    private ReporterStub reporterStub;

    @Before
    public void setup() {
        analyzer = new InvocationsAnalyzerStub();
        reporterStub = new ReporterStub();
        verifier = new NoMoreInvocationsVerifier(analyzer, reporterStub);
    }
    
    @Test
    public void shouldNeverVerifyWhenVerificationIsExplicit() throws Exception {
        verifier.verify(null, null, VerificationModeImpl.atLeastOnce());
    }
    
    @Test
    public void shouldPassVerification() throws Exception {
        analyzer.invocationToReturn = null;
        verifier.verify(null, null, VerificationModeImpl.noMoreInteractions());
    }
    
    @Test
    public void shouldReportError() throws Exception {
        Invocation firstUnverified = new InvocationBuilder().toInvocation();
        analyzer.invocationToReturn = firstUnverified;
        List<Invocation> invocations = asList(new InvocationBuilder().toInvocation());
        
        verifier.verify(invocations, null, VerificationModeImpl.noMoreInteractions());
        
        assertSame(invocations, analyzer.invocations);
        
        assertEquals(firstUnverified.toString(), reporterStub.undesired);
        assertSame(firstUnverified.getStackTrace(), reporterStub.actualInvocationStackTrace);
    }
    
    class InvocationsAnalyzerStub extends InvocationsAnalyzer {
        private List<Invocation> invocations;
        private Invocation invocationToReturn;
        @Override public Invocation findFirstUnverified(List<Invocation> invocations) {
            this.invocations = invocations;
            return invocationToReturn;
        }
    }
    
    class ReporterStub extends Reporter {
        private String undesired;
        private HasStackTrace actualInvocationStackTrace;
        @Override public void noMoreInteractionsWanted(String undesired, HasStackTrace actualInvocationStackTrace) {
            this.undesired = undesired;
            this.actualInvocationStackTrace = actualInvocationStackTrace;
        }
    }
}
