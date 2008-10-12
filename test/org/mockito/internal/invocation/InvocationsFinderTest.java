/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.mockitoutil.ExtraMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.verification.MockitoVerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;


public class InvocationsFinderTest extends TestBase {
    
    private LinkedList<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private InvocationsFinder finder;
    
    @Mock private IMethods mock;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().mock(mock).simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().mock(mock).simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().mock(mock).differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));
        finder = new InvocationsFinder();
    }

    @Test
    public void shouldFindActualInvocations() throws Exception {
        List<Invocation> actual = finder.findInvocations(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertThat(actual, hasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
        
        actual = finder.findInvocations(invocations, new InvocationMatcher(differentMethodInvocation));
        assertThat(actual, hasExactlyInOrder(differentMethodInvocation));
    }
    
    @Test
    public void shouldFindFirstUnverifiedInvocation() throws Exception {
        assertSame(simpleMethodInvocation, finder.findFirstUnverified(invocations));
        
        simpleMethodInvocationTwo.markVerified();
        simpleMethodInvocation.markVerified();
        
        assertSame(differentMethodInvocation, finder.findFirstUnverified(invocations));
        
        differentMethodInvocation.markVerified();
        assertNull(finder.findFirstUnverified(invocations));
    }
    
    @Test
    public void shouldFindFirstUnverifiedInvocationOnMock() throws Exception {
        assertSame(simpleMethodInvocation, finder.findFirstUnverified(invocations, simpleMethodInvocation.getMock()));
        assertNull(finder.findFirstUnverified(invocations, "different mock"));
    }
    
    @Test
    public void shouldFindFirstSimilarInvocationByName() throws Exception {
        Invocation overloadedSimpleMethod = new InvocationBuilder().mock(mock).simpleMethod().arg("test").toInvocation();
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(overloadedSimpleMethod), VerificationModeFactory.atLeastOnce());
        assertSame(found, simpleMethodInvocation);
    }
    
    @Test
    public void shouldFindInvocationWithTheSameMethod() throws Exception {
        Invocation overloadedDifferentMethod = new InvocationBuilder().differentMethod().arg("test").toInvocation();
        
        invocations.add(overloadedDifferentMethod);
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(overloadedDifferentMethod), VerificationModeFactory.atLeastOnce());
        assertSame(found, overloadedDifferentMethod);
    }
    
    @Test
    public void shouldGetLastStackTrace() throws Exception {
        HasStackTrace last = finder.getLastStackTrace(invocations);
        assertSame(differentMethodInvocation.getStackTrace(), last);
        
        assertNull(finder.getLastStackTrace(Collections.<Invocation>emptyList()));
    } 
    
    @Test
    public void shouldFindAllMatchingUnverifiedChunks() throws Exception {
        List<Invocation> allMatching = finder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertThat(allMatching, hasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
        
        simpleMethodInvocation.markVerifiedInOrder();
        allMatching = finder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertThat(allMatching, hasExactlyInOrder(simpleMethodInvocationTwo));
        
        simpleMethodInvocationTwo.markVerifiedInOrder();
        allMatching = finder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertTrue(allMatching.isEmpty());
    }
    
    @Test
    public void shouldFindMatchingChunk() throws Exception {
        MockitoVerificationMode inOrderMode = new VerificationModeBuilder().times(2).inOrder();
        List<Invocation> chunk = finder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), inOrderMode);
        assertThat(chunk, hasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldReturnAllChunksWhenModeIsAtLeastOnce() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().mock(mock).toInvocation();
        invocations.add(simpleMethodInvocationThree);
        
        MockitoVerificationMode atLeastOnceInOrder = new VerificationModeBuilder().inOrder();
        List<Invocation> chunk = finder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnceInOrder);
        assertThat(chunk, hasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo, simpleMethodInvocationThree));
    }
    
    @Test
    public void shouldReturnAllChunksWhenWantedCountDoesntMatch() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().mock(mock).toInvocation();
        invocations.add(simpleMethodInvocationThree);
        
        MockitoVerificationMode atLeastOnceInOrder = new VerificationModeBuilder().times(100).inOrder();
        List<Invocation> chunk = finder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnceInOrder);
        assertThat(chunk, hasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo, simpleMethodInvocationThree));
    }
    
    @Test
    public void shouldFindPreviousInOrder() throws Exception {
        Invocation previous = finder.findPreviousVerifiedInOrder(invocations);
        assertNull(previous);
        
        simpleMethodInvocation.markVerifiedInOrder();
        simpleMethodInvocationTwo.markVerifiedInOrder();
        
        previous = finder.findPreviousVerifiedInOrder(invocations);
        assertSame(simpleMethodInvocationTwo, previous);
    }
}