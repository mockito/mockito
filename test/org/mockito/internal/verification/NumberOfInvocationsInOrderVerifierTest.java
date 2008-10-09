/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.mockito.internal.verification.VerificationModeImpl.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.verification.VerifcationInOrderFailure;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockitoutil.TestBase;

public class NumberOfInvocationsInOrderVerifierTest extends TestBase {

    private NumberOfInvocationsInOrderVerifier verifier;
    private Reporter reporter;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporter = new Reporter();
        finderStub = new InvocationsFinderStub();
        verifier = new NumberOfInvocationsInOrderVerifier(finderStub, reporter);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }
    
    @Test
    public void shouldNeverVerifyIfModeIsNotInOrder() throws Exception {
        assertFalse(verifier.appliesTo(atLeastOnce()));
    }
    
    @Test
    public void shouldPassIfWantedIsZeroAndMatchingChunkIsEmpty() throws Exception {
        assertTrue(finderStub.validMatchingChunkToReturn.isEmpty());
        verifier.verify(invocations, wanted, new VerificationModeBuilder().times(0).inOrder());
    }
    
    @Test
    public void shouldPassIfChunkMatches() throws Exception {
        finderStub.validMatchingChunkToReturn.add(wanted.getInvocation());
        
        verifier.verify(invocations, wanted, new VerificationModeBuilder().times(1).inOrder());
    }
    
    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        Invocation first = new InvocationBuilder().toInvocation();
        Invocation second = new InvocationBuilder().toInvocation();
        finderStub.validMatchingChunkToReturn.addAll(asList(first, second)); 
        
        try {
            verifier.verify(invocations, wanted, new VerificationModeBuilder().times(4).inOrder());
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e, messageContains("Wanted 4 times but was 2"));
        }
    }
    
    @Test
    public void shouldReportTooManyInvocations() throws Exception {
        Invocation first = new InvocationBuilder().toInvocation();
        Invocation second = new InvocationBuilder().toInvocation();
        finderStub.validMatchingChunkToReturn.addAll(asList(first, second)); 
        
        try {
            verifier.verify(invocations, wanted, new VerificationModeBuilder().times(1).inOrder());
            fail();
        } catch (VerifcationInOrderFailure e) {
            assertThat(e, messageContains("Wanted 1 time but was 2"));
        }
    }
    
    @Test
    public void shouldMarkAsVerifiedInOrder() throws Exception {
        Invocation invocation = new InvocationBuilder().toInvocation();
        assertFalse(invocation.isVerifiedInOrder());
        finderStub.validMatchingChunkToReturn.addAll(asList(invocation)); 
        
        verifier.verify(invocations, wanted, new VerificationModeBuilder().times(1).inOrder());
        
        assertTrue(invocation.isVerifiedInOrder());
    }
}
