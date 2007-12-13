package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.progress.OngoingVerifyingMode.atLeastOnce;
import static org.mockito.internal.progress.OngoingVerifyingMode.inOrder;
import static org.mockito.internal.progress.OngoingVerifyingMode.times;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class InvocationsMarkerTest extends RequiresValidState {

    private InvocationsMarker marker;
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private Invocation simpleMethodInvocationThree;
    private List<Invocation> invocations;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().method("simpleMethod").seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().method("simpleMethod").seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().method("differentMethod").seq(3).toInvocation();
        simpleMethodInvocationThree = new InvocationBuilder().method("simpleMethod").seq(4).toInvocation();
        
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
    public void shouldMarkAsVerifiedInOrderWhenVerificationIsNotInOrder() throws Exception {
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertEquals(false, simpleMethodInvocation.isVerifiedInOrder());
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
    public void shouldMarkAsVerifedInOrderAllInvocationsFromChunk() throws Exception {
        OngoingVerifyingMode mode = inOrder(null, Arrays.asList(new Object()));
        Invocation doesntMatter = null;
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        
        assertEquals(true, simpleMethodInvocation.isVerifiedInOrder());
        assertEquals(true, simpleMethodInvocationTwo.isVerifiedInOrder());
        assertEquals(true, differentMethodInvocation.isVerifiedInOrder());
        assertEquals(true, simpleMethodInvocationThree.isVerifiedInOrder());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromThirdChunk() throws Exception {
        OngoingVerifyingMode mode = inOrder(null, Arrays.asList(new Object()));
        
        Invocation doesntMatter = null;
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        marker.markInvocationsAsVerified(invocations, new InvocationMatcher(doesntMatter), mode);
        
        assertEquals(true, simpleMethodInvocationThree.isVerifiedInOrder());
    }
}
