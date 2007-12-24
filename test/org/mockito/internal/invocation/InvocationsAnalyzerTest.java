/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationModeBuilder;
import org.mockito.internal.progress.VerificationModeImpl;

public class InvocationsAnalyzerTest extends RequiresValidState {
    
    private LinkedList<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private InvocationsAnalyzer analyzer;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));
        analyzer = new InvocationsAnalyzer();
    }
    
    @Test
    public void shouldFindFirstUnverifiedInvocation() throws Exception {
        assertSame(simpleMethodInvocation, analyzer.findFirstUnverified(invocations));
        
        simpleMethodInvocationTwo.markVerified();
        simpleMethodInvocation.markVerified();
        
        assertSame(differentMethodInvocation, analyzer.findFirstUnverified(invocations));
        
        differentMethodInvocation.markVerified();
        assertNull(analyzer.findFirstUnverified(invocations));
    }
    
    @Test
    public void shouldFindFirstUnverifiedInvocationOnMock() throws Exception {
        assertSame(simpleMethodInvocation, analyzer.findFirstUnverified(invocations, simpleMethodInvocation.getMock()));
        assertNull(analyzer.findFirstUnverified(invocations, "different mock"));
    }
    
    @Test
    public void shouldFindFirstUndesiredWhenWantedNumberOfTimesIsZero() throws Exception {
        HasStackTrace firstUndesired = analyzer.findFirstUndesiredInvocationTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationModeImpl.times(0));
        HasStackTrace expected = simpleMethodInvocation.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test
    public void shouldFindFirstUndesiredWhenWantedNumberOfTimesIsOne() throws Exception {
        HasStackTrace firstUndesired = analyzer.findFirstUndesiredInvocationTrace(invocations, new InvocationMatcher(simpleMethodInvocation), VerificationModeImpl.times(1));
        HasStackTrace expected = simpleMethodInvocationTwo.getStackTrace();
        assertSame(firstUndesired, expected);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldBreakWhenThereAreNoUndesiredInvocations() throws Exception {
        analyzer.findFirstUndesiredInvocationTrace(invocations, new InvocationMatcher(simpleMethodInvocation), times(2));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldBreakWhenWantedInvocationsFigureIsBigger() throws Exception {
        analyzer.findFirstUndesiredInvocationTrace(invocations, new InvocationMatcher(simpleMethodInvocation), times(100));
    }
    
    @Test
    public void shouldFindSimilarInvocationByName() throws Exception {
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(found, simpleMethodInvocation);
    }
    
    @Test
    public void shouldFindSimilarUnverifiedInvocationByName() throws Exception {
        simpleMethodInvocation.markVerified();
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(found, simpleMethodInvocationTwo);
    }
    
    @Test
    public void shouldFindSimilarInvocationByGettingFirstUnverified() throws Exception {
        simpleMethodInvocation.markVerified();
        simpleMethodInvocationTwo.markVerified();
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(found, differentMethodInvocation);
    }
    
    @Test
    public void shouldNotFindSimilarInvocationBecauseAllAreVerified() throws Exception {
        simpleMethodInvocation.markVerified();
        simpleMethodInvocationTwo.markVerified();
        differentMethodInvocation.markVerified();
        
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertNull(found);
    }
    
    @Test
    public void shouldLookForSimilarInvocationsOnlyOnTheSameMock() throws Exception {
        Invocation onDifferentMock = new InvocationBuilder().simpleMethod().mock("different mock").toInvocation();
        invocations.addFirst(onDifferentMock);
        
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertNotSame(onDifferentMock, found);
    }    
    
    @Test
    public void shouldReturnLastUnverifiedFromTheSameMockOnly() throws Exception {
        Invocation onDifferentMock = new InvocationBuilder().simpleMethod().mock("different mock").toInvocation();
        invocations.addFirst(onDifferentMock);

        simpleMethodInvocation.markVerified();
        simpleMethodInvocationTwo.markVerified();
        
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertNotSame(onDifferentMock, found);
    }  
    
    @Test
    public void shouldLookForSimilarOnlyAfterLastStrictlyVerified() throws Exception {
        VerificationModeImpl mode = new VerificationModeBuilder().strict();
        
        simpleMethodInvocationTwo.markVerifiedStrictly();
        
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), mode);
        assertSame(differentMethodInvocation, found);
    }
    
    @Test
    public void shouldFindSimilarAfterLastStrictlyVerified() throws Exception {
        VerificationModeImpl mode = new VerificationModeBuilder().strict();
        
        Invocation lastInvocation = new InvocationBuilder().simpleMethod().toInvocation();
        invocations.add(lastInvocation);
        
        simpleMethodInvocationTwo.markVerifiedStrictly();
        
        Invocation found = analyzer.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), mode);
        assertSame(found, lastInvocation);
    }
    
    @Test
    public void shouldFindLastMatchingInvocationTrace() throws Exception {
        HasStackTrace found = analyzer.findLastMatchingInvocationTrace(invocations, new InvocationMatcher(simpleMethodInvocation));
        assertSame(simpleMethodInvocationTwo.getStackTrace(), found);
        
        found = analyzer.findLastMatchingInvocationTrace(invocations, new InvocationMatcher(differentMethodInvocation));
        assertSame(differentMethodInvocation.getStackTrace(), found);
    }
    
    @Test
    public void shouldNotFindLastMatchingInvocationTrace() throws Exception {
        InvocationMatcher doesntMatch = new InvocationBuilder().otherMethod().toInvocationMatcher();
        HasStackTrace found = analyzer.findLastMatchingInvocationTrace(invocations, doesntMatch);
        assertNull(found);
    }
}
