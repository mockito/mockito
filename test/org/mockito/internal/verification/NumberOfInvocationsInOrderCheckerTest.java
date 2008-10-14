/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.verification.VerifcationInOrderFailure;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.checkers.NumberOfInvocationsInOrderChecker;
import org.mockitoutil.TestBase;

public class NumberOfInvocationsInOrderCheckerTest extends TestBase {

    private NumberOfInvocationsInOrderChecker verifier;
    private Reporter reporter;
    private InvocationMatcher wanted;
    private LinkedList<Invocation> invocations;
    private InvocationsFinderStub finderStub;
    
    @Before
    public void setup() {
        reporter = new Reporter();
        finderStub = new InvocationsFinderStub();
        verifier = new NumberOfInvocationsInOrderChecker(finderStub, reporter);
        
        wanted = new InvocationBuilder().toInvocationMatcher();
        invocations = new LinkedList<Invocation>(asList(new InvocationBuilder().toInvocation()));
    }
    
    @Test
    public void shouldPassIfWantedIsZeroAndMatchingChunkIsEmpty() throws Exception {
        assertTrue(finderStub.validMatchingChunkToReturn.isEmpty());
        verifier.check(invocations, wanted, 0);
    }
    
    @Test
    public void shouldPassIfChunkMatches() throws Exception {
        finderStub.validMatchingChunkToReturn.add(wanted.getInvocation());
        
        verifier.check(invocations, wanted, 1);
    }
    
    @Test
    public void shouldReportTooLittleInvocations() throws Exception {
        Invocation first = new InvocationBuilder().toInvocation();
        Invocation second = new InvocationBuilder().toInvocation();
        finderStub.validMatchingChunkToReturn.addAll(asList(first, second)); 
        
        try {
            verifier.check(invocations, wanted, 4);
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
            verifier.check(invocations, wanted, 1);
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
        
        verifier.check(invocations, wanted, 1);
        
        assertTrue(invocation.isVerifiedInOrder());
    }
}
