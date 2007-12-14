package org.mockito.internal.verification;

import static java.util.Arrays.asList;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationsCalculator;
import org.mockito.internal.progress.VerificationMode;
import static org.junit.Assert.*;

public class NoMoreInvocationsVerifierTest extends RequiresValidState {

    private NoMoreInvocationsVerifier verifier;
    private InvocationsCalculatorStub calculator;
    private ReporterStub reporterStub;
    private Invocation returnedByCalculator;

    @Before
    public void setup() {
        calculator = new InvocationsCalculatorStub();
        reporterStub = new ReporterStub();
        verifier = new NoMoreInvocationsVerifier(calculator, reporterStub);
        returnedByCalculator = new InvocationBuilder().toInvocation();
    }
    
    @Test
    public void shouldNeverVerifyWhenVerificationIsExplicit() throws Exception {
        verifier.verify(null, null, VerificationMode.atLeastOnce());
    }
    
    @Test
    public void shouldPassVerification() throws Exception {
        returnedByCalculator = null;
        verifier.verify(null, null, VerificationMode.noMoreInteractions());
    }
    
    @Test
    public void shouldReportError() throws Exception {
        List<Invocation> invocations = asList(new InvocationBuilder().toInvocation());
        returnedByCalculator = new InvocationBuilder().toInvocation();
        
        verifier.verify(invocations, null, VerificationMode.noMoreInteractions());
        
        assertSame(invocations, calculator.invocations);
        assertEquals(returnedByCalculator.toString(), reporterStub.undesired);
        assertSame(returnedByCalculator.getStackTrace(), reporterStub.actualInvocationStackTrace);
    }
    
    class InvocationsCalculatorStub extends InvocationsCalculator {
        private List<Invocation> invocations;
        @Override public Invocation getFirstUnverified(List<Invocation> invocations) {
            this.invocations = invocations;
            return returnedByCalculator;
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
