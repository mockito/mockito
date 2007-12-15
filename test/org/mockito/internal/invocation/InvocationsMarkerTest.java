/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.progress.VerificationMode.atLeastOnce;
import static org.mockito.internal.progress.VerificationMode.times;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.VerificationMode;
import org.mockito.internal.progress.VerificationModeBuilder;

public class InvocationsMarkerTest extends RequiresValidState {

    private InvocationsMarker marker;
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private Invocation simpleMethodInvocationThree;
    private List<Invocation> invocations;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        simpleMethodInvocationThree = new InvocationBuilder().simpleMethod().seq(4).toInvocation();
        
        invocations = Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation, simpleMethodInvocationThree);
        
        marker = new InvocationsMarker();
    }

    @Test
    public void shouldMarkAllSimpleMethodsAsVerified() throws Exception {
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(simpleMethodInvocation), times(2));
        
        assertEquals(true, simpleMethodInvocation.isVerified());
        assertEquals(true, simpleMethodInvocationTwo.isVerified());
        assertEquals(false, differentMethodInvocation.isVerified());
        assertEquals(true, simpleMethodInvocationThree.isVerified());
    }
    
    @Test
    public void shouldMarkAllsimpleMethodAsVerifiedWhenAtLeastOnceIsUsed() throws Exception {
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        
        assertEquals(true, simpleMethodInvocation.isVerified());
        assertEquals(true, simpleMethodInvocationTwo.isVerified());
        assertEquals(false, differentMethodInvocation.isVerified());
        assertEquals(true, simpleMethodInvocationThree.isVerified());
    }
    
    @Test
    public void shouldNotMarkAsVerifiedStrictlyWhenVerificationIsStrict() throws Exception {
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertEquals(false, simpleMethodInvocation.isVerifiedStrictly());
    }
    
    @Test
    public void shouldNeverMarkInvocationsAsVerifiedIfExpectedCountIsZero() throws Exception {
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(simpleMethodInvocation), times(0));
        
        assertEquals(false, simpleMethodInvocation.isVerified());
        assertEquals(false, simpleMethodInvocationTwo.isVerified());
        assertEquals(false, differentMethodInvocation.isVerified());
        assertEquals(false, simpleMethodInvocationThree.isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedStrictlyAllInvocationsFromChunk() throws Exception {
        VerificationMode mode = new VerificationModeBuilder().strict();
        Invocation doesntMatter = null;
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        
        assertEquals(true, simpleMethodInvocation.isVerifiedStrictly());
        assertEquals(true, simpleMethodInvocationTwo.isVerifiedStrictly());
        assertEquals(true, differentMethodInvocation.isVerifiedStrictly());
        assertEquals(true, simpleMethodInvocationThree.isVerifiedStrictly());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromThirdChunk() throws Exception {
        VerificationMode mode = new VerificationModeBuilder().strict();
        
        Invocation doesntMatter = null;
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        
        assertEquals(true, simpleMethodInvocationThree.isVerifiedStrictly());
    }
}
