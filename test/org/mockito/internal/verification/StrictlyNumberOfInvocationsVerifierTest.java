package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeBuilder;

public class StrictlyNumberOfInvocationsVerifierTest extends RequiresValidState {

    private StrictlyNumberOfInvocationsVerifier verifier;
    private ReporterStub reporterStub;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporterStub = new ReporterStub();
        finderStub = new InvocationsFinderStub();
        verifier = new StrictlyNumberOfInvocationsVerifier(finderStub, reporterStub);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }
    
    @Test
    public void shouldNeverVerifyIfModeIsNotStrict() throws Exception {
        verifier.verify(null, wanted, atLeastOnce());
    }
    
    @Test
    public void shouldPassIfWantedIsZeroAndFirstUnverifiedChunkIsEmpty() throws Exception {
        assertTrue(finderStub.firstUnverifiedChunkToReturn.isEmpty());
        verifier.verify(invocations, wanted, new VerificationModeBuilder().times(0).strict());
    }
    
    @Test
    public void shouldPassIfWantedIsZeroAndFirstUnverifiedChunkDoesNotMatch() throws Exception {
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        finderStub.firstUnverifiedChunkToReturn.add(differentMethod); 
        
        assertFalse(wanted.matches(differentMethod));
        verifier.verify(invocations, wanted, new VerificationModeBuilder().times(0).strict());
    }
    
    class ReporterStub extends Reporter {
    }
}
