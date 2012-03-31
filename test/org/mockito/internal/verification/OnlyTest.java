/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;

public class OnlyTest {

    Only only = new Only();
    
    public class VerificationDataStub implements VerificationData {
        private final InvocationImpl invocation;
        private final InvocationMatcher wanted;

        public VerificationDataStub(InvocationMatcher wanted, InvocationImpl invocation) {
            this.invocation = invocation;
            this.wanted = wanted;
        }

        public List<InvocationImpl> getAllInvocations() {
            return Arrays.asList(invocation);
        }

        public InvocationMatcher getWanted() {
            return wanted;
        }
    }

    @Test
    public void shouldMarkAsVerified() {
        //given
        InvocationImpl invocation = new InvocationBuilder().toInvocation();
        assertFalse(invocation.isVerified());
        
        //when
        only.verify(new VerificationDataStub(new InvocationMatcher(invocation), invocation));
        
        //then
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void shouldNotMarkAsVerifiedWhenAssertionFailed() {
        //given
        InvocationImpl invocation = new InvocationBuilder().toInvocation();
        assertFalse(invocation.isVerified());
        
        //when
        try {
            only.verify(new VerificationDataStub(new InvocationBuilder().toInvocationMatcher(), invocation));
            fail();
        } catch (MockitoAssertionError e) {}
        
        //then
        assertFalse(invocation.isVerified());
    }
}