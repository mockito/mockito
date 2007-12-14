/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.progress.VerificationMode.atLeastOnce;
import static org.mockito.internal.progress.VerificationMode.times;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsCalculator;

public class NumberOfInvocationsVerifierTest extends RequiresValidState {

    private NumberOfInvocationsVerifier verifier;
    private InvocationsCalculatorStub calculatorStub;
    private InvocationMatcher wanted;
    
    @Before
    public void setup() {
        verifier = new NumberOfInvocationsVerifier(new Reporter());
        calculatorStub = new InvocationsCalculatorStub();
        wanted = new InvocationBuilder().toInvocationMatcher();
    }

    @Test
    public void shouldNotCheckForWrongNumberOfModificationsWhenAtLeastOnceVerification() throws Exception {
        verifier.verify(null, null, atLeastOnce());
    }

    @Ignore
    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        verifier.verify(calculatorStub, wanted, times(4));
        
    }
    
    class InvocationsCalculatorStub extends InvocationsCalculator {
        private InvocationMatcher wanted;
        @Override
        public int countActual(InvocationMatcher wanted) {
            this.wanted = wanted;
            return 5;
        }
    }
}
