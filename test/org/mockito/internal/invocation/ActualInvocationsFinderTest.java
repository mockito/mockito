package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.internal.progress.VerificationModeBuilder;


public class ActualInvocationsFinderTest extends RequiresValidState {
    
    private LinkedList<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;
    private ActualInvocationsFinder finder;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));
        finder = new ActualInvocationsFinder();
    }

    @Test
    public void shouldFindActualInvocations() throws Exception {
        List<Invocation> actual = finder.findInvocations(invocations, new InvocationMatcher(simpleMethodInvocation), atLeastOnce());
        assertThat(actual, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
        
        actual = finder.findInvocations(invocations, new InvocationMatcher(differentMethodInvocation), atLeastOnce());
        assertThat(actual, collectionHasExactlyInOrder(differentMethodInvocation));
    }
    
    @Test
    public void shouldSearchOnlyAfterLastStrictlyVerified() throws Exception {
        simpleMethodInvocation.markVerifiedStrictly();
        simpleMethodInvocationTwo.markVerifiedStrictly();
        
        List<Invocation> actual = finder.findInvocations(invocations, new InvocationMatcher(simpleMethodInvocation), new VerificationModeBuilder().strict());
        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void shouldSearchAndFindOnlyAfterLastStrictlyVerified() throws Exception {
        differentMethodInvocation.markVerifiedStrictly();
        
        Invocation lastInvocation = new InvocationBuilder().simpleMethod().toInvocation();
        invocations.add(lastInvocation);
        
        List<Invocation> actual = finder.findInvocations(invocations, new InvocationMatcher(simpleMethodInvocation), new VerificationModeBuilder().strict());
        assertThat(actual, collectionHasExactlyInOrder(lastInvocation));
    }

    @Test
    public void shouldFindLastInvocationWhenModeIsOneTimeStrictly() throws Exception {
        Invocation lastSimpleMethodInvocation = new InvocationBuilder().toInvocation();
        invocations.add(lastSimpleMethodInvocation);
        
        List<Invocation> actual = finder.findInvocations(
                invocations, new InvocationMatcher(simpleMethodInvocation), new VerificationModeBuilder().times(1).strict());
        assertThat(actual, collectionHasExactlyInOrder(lastSimpleMethodInvocation));
    }
    
    @Test
    public void shouldFindFirstTwoInvocationsWhenModeIsTwoTimesStrictly() throws Exception {
        Invocation lastSimpleMethodInvocation = new InvocationBuilder().toInvocation();
        invocations.add(lastSimpleMethodInvocation);
        
        List<Invocation> actual = finder.findInvocations(
                invocations, new InvocationMatcher(simpleMethodInvocation), new VerificationModeBuilder().times(2).strict());
        assertThat(actual, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldFindFirstMatchingChunkWhenWantedCountDoesNotMatch() throws Exception {
        Invocation lastSimpleMethodInvocation = new InvocationBuilder().toInvocation();
        invocations.add(lastSimpleMethodInvocation);
        
        List<Invocation> actual = finder.findInvocations(
                invocations, new InvocationMatcher(simpleMethodInvocation), new VerificationModeBuilder().times(20).strict());
        assertThat(actual, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldFindFirstTwoInvocationsWhenModeIsAtLeastOnceStrictly() throws Exception {
        Invocation lastSimpleMethodInvocation = new InvocationBuilder().toInvocation();
        invocations.add(lastSimpleMethodInvocation);
        
        List<Invocation> actual = finder.findInvocations(
                invocations, new InvocationMatcher(simpleMethodInvocation), new VerificationModeBuilder().atLeastOnce().strict());
        assertThat(actual, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
}
