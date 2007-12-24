package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.progress.VerificationModeBuilder;

public class MarkingVerifierTest extends RequiresValidState {

    private ActualInvocationsFinderStub finderStub;
    private MarkingVerifier verifier;
    
    @Before
    public void setup() {
        finderStub = new ActualInvocationsFinderStub();
        verifier = new MarkingVerifier(finderStub);
    }

    @Test
    public void shouldWorkOnlyWhenModeIsExplicit() {
        verifier = new MarkingVerifier(null);
        verifier.verify(null, null, noMoreInteractions());
    }
    
    @Test
    public void shouldMarkAsVerified() {
        Invocation invocation = new InvocationBuilder().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().toInvocation();
        
        assertFalse(invocation.isVerified());
        assertFalse(invocationTwo.isVerified());
        
        finderStub.actualToReturn.addAll(asList(invocation, invocationTwo));
        
        verifier.verify(asList(invocation, invocationTwo), null, atLeastOnce());
        
        assertTrue(invocation.isVerified());
        assertTrue(invocationTwo.isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifiedStrictly() {
        Invocation invocation = new InvocationBuilder().toInvocation();
        Invocation invocationTwo = new InvocationBuilder().toInvocation();
        
        assertFalse(invocation.isVerifiedStrictly());
        assertFalse(invocationTwo.isVerifiedStrictly());
        
        finderStub.actualToReturn.addAll(asList(invocation, invocationTwo));
        
        verifier.verify(asList(invocation, invocationTwo), null, new VerificationModeBuilder().strict());
        
        assertTrue(invocation.isVerifiedStrictly());
        assertTrue(invocationTwo.isVerifiedStrictly());
    }
}
