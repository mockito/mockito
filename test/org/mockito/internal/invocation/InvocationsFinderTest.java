package org.mockito.internal.invocation;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.HasStackTrace;


public class InvocationsFinderTest extends RequiresValidState {
    
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
    public void shouldFindFirstUnverifiedChunk() throws Exception {
        List<Invocation> unverified = finder.findFirstUnverifiedChunk(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldFindFirstChunkAndSkipVerifiedInvocations() throws Exception {
        simpleMethodInvocation.markVerifiedStrictly();
        simpleMethodInvocationTwo.markVerifiedStrictly();
        
        List<Invocation> unverified = finder.findFirstUnverifiedChunk(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(differentMethodInvocation));
    }
    
    @Test
    public void shouldFindFirstChunkAndSkipAllInvocations() throws Exception {
        simpleMethodInvocation.markVerifiedStrictly();
        simpleMethodInvocationTwo.markVerifiedStrictly();
        differentMethodInvocation.markVerifiedStrictly();
        
        List<Invocation> unverified = finder.findFirstUnverifiedChunk(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertTrue(unverified.isEmpty());
    }
    
    @Test
    public void shouldFindAllInvocationsBecauseAllMatch() throws Exception {
        List<Invocation> unverified = finder.findFirstUnverifiedChunk(
                asList(simpleMethodInvocation, simpleMethodInvocationTwo), new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldReturnFirstUnverifiedInvocationIfNoMatchesFound() throws Exception {
        List<Invocation> unverified = finder.findFirstUnverifiedChunk(
                asList(differentMethodInvocation), new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(differentMethodInvocation));
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
    public void shouldFindSimilarInvocationByName() throws Exception {
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(found, simpleMethodInvocation);
    }
    
    @Test
    public void shouldFindSimilarUnverifiedInvocationByName() throws Exception {
        simpleMethodInvocation.markVerified();
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(found, simpleMethodInvocationTwo);
    }
    
    @Test
    public void shouldFindSimilarInvocationByGettingFirstUnverified() throws Exception {
        simpleMethodInvocation.markVerified();
        simpleMethodInvocationTwo.markVerified();
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertSame(found, differentMethodInvocation);
    }
    
    @Test
    public void shouldNotFindSimilarInvocationBecauseAllAreVerified() throws Exception {
        simpleMethodInvocation.markVerified();
        simpleMethodInvocationTwo.markVerified();
        differentMethodInvocation.markVerified();
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertNull(found);
    }
    
    @Test
    public void shouldLookForSimilarInvocationsOnlyOnTheSameMock() throws Exception {
        Invocation onDifferentMock = new InvocationBuilder().simpleMethod().mock("different mock").toInvocation();
        invocations.addFirst(onDifferentMock);
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertNotSame(onDifferentMock, found);
    }    
    
    @Test
    public void shouldReturnLastUnverifiedFromTheSameMockOnly() throws Exception {
        Invocation onDifferentMock = new InvocationBuilder().simpleMethod().mock("different mock").toInvocation();
        invocations.addFirst(onDifferentMock);

        simpleMethodInvocation.markVerified();
        simpleMethodInvocationTwo.markVerified();
        
        Invocation found = finder.findSimilarInvocation(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertNotSame(onDifferentMock, found);
    }  
    
    @Test
    public void shouldGetLastStackTrace() throws Exception {
        HasStackTrace last = finder.getLastStackTrace(invocations);
        assertSame(differentMethodInvocation.getStackTrace(), last);
        
        assertNull(finder.getLastStackTrace(Collections.<Invocation>emptyList()));
    } 
}