/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.*;
import static org.mockito.internal.progress.OngoingVerifyingMode.*;

import java.util.*;

import org.junit.*;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationChunk;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.OngoingVerifyingMode;
import org.mockito.internal.verification.RegisteredInvocations;

@SuppressWarnings("unchecked")
public class RegisteredInvocationsTest extends RequiresValidState {

    private RegisteredInvocations registered;
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private Invocation simpleMethodInvocationThree;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().method("simpleMethod").seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().method("simpleMethod").seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().method("differentMethod").seq(3).toInvocation();
        simpleMethodInvocationThree = new InvocationBuilder().method("simpleMethod").seq(4).toInvocation();
        
        registered = new RegisteredInvocations(new InvocationsFinder() {
            public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
                return Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation, simpleMethodInvocationThree) ;
            }});
        
        registered.add(simpleMethodInvocation);
        registered.add(simpleMethodInvocationTwo);
        registered.add(differentMethodInvocation);
        registered.add(simpleMethodInvocationThree);
    }

    @Test
    public void shouldAskInvocationFinderForAllInvocations() throws Exception {
        final List<Object> dummyMocks = asList(new Object(), new Object());
        final StringBuilder s = new StringBuilder();
        
        registered = new RegisteredInvocations(new InvocationsFinder() {
            public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
                s.append("InvocationsFinder was asked");
                assertSame(dummyMocks, mocks);
                return EMPTY_LIST;
            }});
        
        registered.unverifiedInvocationChunks(inOrder(null, dummyMocks));
        assertEquals("InvocationsFinder was asked", s.toString());
    }    
    
    @Test
    public void shouldGetUnverifiedInvocationChunks() throws Exception {
        Object mock = new Object();
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(inOrder(null, asList(mock)));
        
        assertEquals(3, chunks.size());
        
        InvocationChunk firstChunk = chunks.get(0);
        assertEquals(2, firstChunk.getCount());
        assertEquals(simpleMethodInvocation, firstChunk.getInvocation());
        assertEquals(simpleMethodInvocationTwo, firstChunk.getInvocation());
        
        InvocationChunk secondChunk = chunks.get(1);
        assertEquals(1, secondChunk.getCount());
        assertEquals(differentMethodInvocation, secondChunk.getInvocation());
        
        InvocationChunk thirdChunk = chunks.get(2);
        assertEquals(1, thirdChunk.getCount());
        assertEquals(simpleMethodInvocationThree, thirdChunk.getInvocation());
    }
    
    @Test
    public void shouldGetTwoUnverifiedInvocationChunks() throws Exception {
        Object mock = new Object();
        
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), inOrder(null, asList(mock)));
        
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(inOrder(null, asList(mock)));
        
        assertEquals(2, chunks.size());
        
        InvocationChunk firstChunk = chunks.get(0);
        assertEquals(1, firstChunk.getCount());
        assertEquals(differentMethodInvocation, firstChunk.getInvocation());
        
        InvocationChunk secondChunk = chunks.get(1);
        assertEquals(1, secondChunk.getCount());
        assertEquals(simpleMethodInvocationThree, secondChunk.getInvocation());
    }
    
    @Test
    public void shouldGetOneUnverifiedInvocationChunk() throws Exception {
        Object mock = new Object();
        
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), inOrder(null, asList(mock)));
        registered.markInvocationsAsVerified(new InvocationMatcher(differentMethodInvocation), inOrder(null, asList(mock)));
        
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(inOrder(null, asList(mock)));
        
        assertEquals(1, chunks.size());
        
        InvocationChunk firstChunk = chunks.get(0);
        assertEquals(1, firstChunk.getCount());
        assertEquals(simpleMethodInvocationThree, firstChunk.getInvocation());
    }
    
    @Test
    public void shouldNotGetAnyInvocationChunks() throws Exception {
        Object mock = new Object();
        
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), inOrder(null, asList(mock)));
        registered.markInvocationsAsVerified(new InvocationMatcher(differentMethodInvocation), inOrder(null, asList(mock)));
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), inOrder(null, asList(mock)));
        
        List<InvocationChunk> chunks = registered.unverifiedInvocationChunks(inOrder(null, asList(mock)));
        
        assertEquals(0, chunks.size());
    }
    
    @Test
    public void shouldMarkAllsimpleMethodAsVerified() throws Exception {
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation),times(2));
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAllsimpleMethodAsVerifiedWhenAtLeastOnceIsUsed() throws Exception {
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldNeverMarkInvocationsAsVerifiedIfExpectedCountIsZero() throws Exception {
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), times(0));
        
        List<Invocation> invocations = registered.all();
        assertEquals(false, invocations.get(0).isVerified());
        assertEquals(false, invocations.get(1).isVerified());
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromFirstChunk() throws Exception {
        OngoingVerifyingMode mode = inOrder(null, Arrays.asList(new Object()));
        assertTrue(mode.orderOfInvocationsMatters());
        registered.markInvocationsAsVerified(new InvocationMatcher(null), mode);
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(0).isVerified());
        assertEquals(true, invocations.get(0).isVerifiedInOrder());
        assertEquals(true, invocations.get(1).isVerified());
        assertEquals(true, invocations.get(1).isVerifiedInOrder());
        
        assertEquals(false, invocations.get(2).isVerified());
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromSecondChunk() throws Exception {
        OngoingVerifyingMode mode = inOrder(null, Arrays.asList(new Object()));
        assertTrue(mode.orderOfInvocationsMatters());
        
        Invocation doesntMatter = null;
        registered.markInvocationsAsVerified(new InvocationMatcher(doesntMatter), mode);
        registered.markInvocationsAsVerified(new InvocationMatcher(doesntMatter), mode);
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(2).isVerified());
        assertEquals(true, invocations.get(2).isVerifiedInOrder());
        
        assertEquals(false, invocations.get(3).isVerified());
    }
    
    @Test
    public void shouldMarkAsVerifedAllInvocationsFromThirdChunk() throws Exception {
        OngoingVerifyingMode mode = inOrder(null, Arrays.asList(new Object()));
        assertTrue(mode.orderOfInvocationsMatters());
        
        Invocation doesntMatter = null;
        registered.markInvocationsAsVerified(new InvocationMatcher(doesntMatter), mode);
        registered.markInvocationsAsVerified(new InvocationMatcher(doesntMatter), mode);
        registered.markInvocationsAsVerified(new InvocationMatcher(doesntMatter), mode);
        
        List<Invocation> invocations = registered.all();
        assertEquals(true, invocations.get(3).isVerified());
        assertEquals(true, invocations.get(3).isVerifiedInOrder());
    }
    
    @Test
    public void shouldRemoveLastInvocation() throws Exception {
        registered.removeLast();
        assertEquals(3, registered.all().size());
    }
    
    @Test
    public void shouldGetFirstUnverifiedInvocation() throws Exception {
        assertSame(simpleMethodInvocation, registered.getFirstUnverified());
        
        registered.markInvocationsAsVerified(new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(differentMethodInvocation, registered.getFirstUnverified());
        
        registered.markInvocationsAsVerified(new InvocationMatcher(differentMethodInvocation), atLeastOnce());
        assertNull(registered.getFirstUnverified());
    }
    
    @Test
    public void shouldGetFirstUndesiredWhenWantedNumberOfTimesIsZero() throws Exception {
        HasStackTrace firstUndesired = registered.getFirstUndesiredInvocationStackTrace(new InvocationMatcher(simpleMethodInvocation), OngoingVerifyingMode.times(0));
        HasStackTrace expected = simpleMethodInvocation.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test
    public void shouldGetFirstUndesiredWhenWantedNumberOfTimesIsOne() throws Exception {
        HasStackTrace firstUndesired = registered.getFirstUndesiredInvocationStackTrace(new InvocationMatcher(simpleMethodInvocation), OngoingVerifyingMode.times(1));
        HasStackTrace expected = simpleMethodInvocationTwo.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test
    public void shouldGetFirstUndesiredWhenWantedNumberOfTimesIsTwo() throws Exception {
        HasStackTrace firstUndesired = registered.getFirstUndesiredInvocationStackTrace(new InvocationMatcher(simpleMethodInvocation), OngoingVerifyingMode.times(2));
        HasStackTrace expected = simpleMethodInvocationThree.getStackTrace();
        assertSame(firstUndesired, expected);
    }
}
