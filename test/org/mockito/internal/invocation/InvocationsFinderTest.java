/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.progress.VerificationModeImpl;


public class InvocationsFinderTest extends TestBase {
    
    private LinkedList<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private InvocationsFinder finder;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));
        finder = new InvocationsFinder();
    }

    @Test
    public void shouldFindActualInvocations() throws Exception {
        List<Invocation> actual = finder.findInvocations(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertThat(actual, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
        
        actual = finder.findInvocations(invocations, new InvocationMatcher(differentMethodInvocation), atLeastOnce());
        assertThat(actual, collectionHasExactlyInOrder(differentMethodInvocation));
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
        Invocation overloadedSimpleMethod = new InvocationBuilder().simpleMethod().arg("test").toInvocation();
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(overloadedSimpleMethod), atLeastOnce());
        assertSame(found, simpleMethodInvocation);
    }
    
    @Test
    public void shouldFindInvocationWithTheSameMethod() throws Exception {
        Invocation overloadedDifferentMethod = new InvocationBuilder().differentMethod().arg("test").toInvocation();
        
        invocations.add(overloadedDifferentMethod);
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(overloadedDifferentMethod), atLeastOnce());
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
        assertThat(allMatching, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
        
        simpleMethodInvocation.markVerifiedInOrder();
        allMatching = finder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertThat(allMatching, collectionHasExactlyInOrder(simpleMethodInvocationTwo));
        
        simpleMethodInvocationTwo.markVerifiedInOrder();
        allMatching = finder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertTrue(allMatching.isEmpty());
    }
    
    @Test
    public void shouldFindMatchingChunk() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().toInvocation();
        invocations.add(simpleMethodInvocationThree);
        
        VerificationModeImpl inOrderMode = new VerificationModeBuilder().times(2).inOrder();
        List<Invocation> chunk = finder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), inOrderMode);
        assertThat(chunk, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldReturnAllChunksWhenModeIsAtLeastOnce() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().toInvocation();
        invocations.add(simpleMethodInvocationThree);
        
        VerificationModeImpl atLeastOnceInOrder = new VerificationModeBuilder().inOrder();
        List<Invocation> chunk = finder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnceInOrder);
        assertThat(chunk, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo, simpleMethodInvocationThree));
    }
    
    @Test
    public void shouldReturnAllChunksWhenWantedCountDoesntMatch() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().toInvocation();
        invocations.add(simpleMethodInvocationThree);
        
        VerificationModeImpl atLeastOnceInOrder = new VerificationModeBuilder().times(100).inOrder();
        List<Invocation> chunk = finder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnceInOrder);
        assertThat(chunk, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo, simpleMethodInvocationThree));
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