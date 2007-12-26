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
    public void shouldFindFirstStrictlyUnverified() throws Exception {
        List<Invocation> unverified = finder.findFirstStrictlyUnverified(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(simpleMethodInvocation, simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldFindFirstStrictlyUnverifiedAndSkipVerified() throws Exception {
        simpleMethodInvocation.markVerifiedStrictly();
        
        List<Invocation> unverified = finder.findFirstStrictlyUnverified(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(simpleMethodInvocationTwo));
    }
    
    @Test
    public void shouldFindFirstStrictlyUnverifiedAndSkipTwoVerifiedInvocations() throws Exception {
        simpleMethodInvocation.markVerifiedStrictly();
        simpleMethodInvocationTwo.markVerifiedStrictly();
        
        List<Invocation> unverified = finder.findFirstStrictlyUnverified(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertThat(unverified, collectionHasExactlyInOrder(differentMethodInvocation));
    }
    
    @Test
    public void shouldFindFirstStrictlyUnverifiedAndSkipAllInvocations() throws Exception {
        simpleMethodInvocation.markVerifiedStrictly();
        simpleMethodInvocationTwo.markVerifiedStrictly();
        differentMethodInvocation.markVerifiedStrictly();
        
        List<Invocation> unverified = finder.findFirstStrictlyUnverified(invocations, new InvocationMatcher(simpleMethodInvocation));
        
        assertTrue(unverified.isEmpty());
    }
}
