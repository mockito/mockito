/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


public class InvocationsFinderTest extends TestBase {

    private LinkedList<Invocation> invocations = new LinkedList<Invocation>();
    private Invocation simpleMethodInvocation;
    private Invocation simpleMethodInvocationTwo;
    private Invocation differentMethodInvocation;


    private final InOrderContext context = new InOrderContextImpl();

    @Mock private IMethods mock;

    @Before
    public void setup() throws Exception {
        simpleMethodInvocation = new InvocationBuilder().mock(mock).simpleMethod().seq(1).toInvocation();
        simpleMethodInvocationTwo = new InvocationBuilder().mock(mock).simpleMethod().seq(2).toInvocation();
        differentMethodInvocation = new InvocationBuilder().mock(mock).differentMethod().seq(3).toInvocation();
        invocations.addAll(Arrays.asList(simpleMethodInvocation, simpleMethodInvocationTwo, differentMethodInvocation));

    }

    @Test
    public void shouldFindActualInvocations() throws Exception {
        List<Invocation> actual = InvocationsFinder.findInvocations(invocations, new InvocationMatcher(simpleMethodInvocation));
        Assertions.assertThat(actual).containsSequence(simpleMethodInvocation, simpleMethodInvocationTwo);

        actual = InvocationsFinder.findInvocations(invocations, new InvocationMatcher(differentMethodInvocation));
        Assertions.assertThat(actual).containsSequence(differentMethodInvocation);
    }

    @Test
    public void shouldFindFirstUnverifiedInvocation() throws Exception {
        assertSame(simpleMethodInvocation, InvocationsFinder.findFirstUnverified(invocations));

        simpleMethodInvocationTwo.markVerified();
        simpleMethodInvocation.markVerified();

        assertSame(differentMethodInvocation, InvocationsFinder.findFirstUnverified(invocations));

        differentMethodInvocation.markVerified();
        assertNull(InvocationsFinder.findFirstUnverified(invocations));
    }

    @Test
    public void shouldFindFirstUnverifiedInOrder() throws Exception {
        //given
        InOrderContextImpl context = new InOrderContextImpl();
        assertSame(simpleMethodInvocation, InvocationsFinder.findFirstUnverifiedInOrder(context, invocations));

        //when
        context.markVerified(simpleMethodInvocationTwo);
        context.markVerified(simpleMethodInvocation);

        //then
        assertSame(differentMethodInvocation, InvocationsFinder.findFirstUnverifiedInOrder(context, invocations));

        //when
        context.markVerified(differentMethodInvocation);

        //then
        assertNull(InvocationsFinder.findFirstUnverifiedInOrder(context, invocations));
    }

    @Test
    public void shouldFindFirstUnverifiedInOrderAndRespectSequenceNumber() throws Exception {
        //given
        InOrderContextImpl context = new InOrderContextImpl();
        assertSame(simpleMethodInvocation, InvocationsFinder.findFirstUnverifiedInOrder(context, invocations));

        //when
        //skipping verification of first invocation, then:
        context.markVerified(simpleMethodInvocationTwo);
        context.markVerified(differentMethodInvocation);

        //then
        assertSame(null, InvocationsFinder.findFirstUnverifiedInOrder(context, invocations));
    }

    @Test
    public void shouldFindFirstUnverifiedInvocationOnMock() throws Exception {
        assertSame(simpleMethodInvocation, InvocationsFinder.findFirstUnverified(invocations, simpleMethodInvocation.getMock()));
        assertNull(InvocationsFinder.findFirstUnverified(invocations, "different mock"));
    }

    @Test
    public void shouldFindFirstSimilarInvocationByName() throws Exception {
        Invocation overloadedSimpleMethod = new InvocationBuilder().mock(mock).simpleMethod().arg("test").toInvocation();

        Invocation found = InvocationsFinder.findSimilarInvocation(invocations, new InvocationMatcher(overloadedSimpleMethod));
        assertSame(found, simpleMethodInvocation);
    }

    @Test
    public void shouldFindInvocationWithTheSameMethod() throws Exception {
        Invocation overloadedDifferentMethod = new InvocationBuilder().differentMethod().arg("test").toInvocation();

        invocations.add(overloadedDifferentMethod);

        Invocation found = InvocationsFinder.findSimilarInvocation(invocations, new InvocationMatcher(overloadedDifferentMethod));
        assertSame(found, overloadedDifferentMethod);
    }

    @Test
    public void shouldGetLastStackTrace() throws Exception {
        Location last = InvocationsFinder.getLastLocation(invocations);
        assertSame(differentMethodInvocation.getLocation(), last);

        assertNull(InvocationsFinder.getLastLocation(Collections.<Invocation>emptyList()));
    }

    @Test
    public void shouldFindAllMatchingUnverifiedChunks() throws Exception {
        List<Invocation> allMatching = InvocationsFinder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation), context);
        Assertions.assertThat(allMatching).containsSequence(simpleMethodInvocation, simpleMethodInvocationTwo);

        context.markVerified(simpleMethodInvocation);
        allMatching = InvocationsFinder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation), context);
        Assertions.assertThat(allMatching).containsSequence(simpleMethodInvocationTwo);

        context.markVerified(simpleMethodInvocationTwo);
        allMatching = InvocationsFinder.findAllMatchingUnverifiedChunks(invocations, new InvocationMatcher(simpleMethodInvocation), context);
        assertTrue(allMatching.isEmpty());
    }

    @Test
    public void shouldFindMatchingChunk() throws Exception {
        List<Invocation> chunk = InvocationsFinder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), 2, context);
        Assertions.assertThat(chunk).containsSequence(simpleMethodInvocation, simpleMethodInvocationTwo);
    }

    @Test
    public void shouldReturnAllChunksWhenModeIsAtLeastOnce() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().mock(mock).toInvocation();
        invocations.add(simpleMethodInvocationThree);

        List<Invocation> chunk = InvocationsFinder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), 1, context);
        Assertions.assertThat(chunk).containsSequence(simpleMethodInvocation, simpleMethodInvocationTwo, simpleMethodInvocationThree);
    }

    @Test
    public void shouldReturnAllChunksWhenWantedCountDoesntMatch() throws Exception {
        Invocation simpleMethodInvocationThree = new InvocationBuilder().mock(mock).toInvocation();
        invocations.add(simpleMethodInvocationThree);

        List<Invocation> chunk = InvocationsFinder.findMatchingChunk(invocations, new InvocationMatcher(simpleMethodInvocation), 1, context);
        Assertions.assertThat(chunk).containsSequence(simpleMethodInvocation, simpleMethodInvocationTwo, simpleMethodInvocationThree);
    }

    @Test
    public void shouldFindPreviousInOrder() throws Exception {
        Invocation previous = InvocationsFinder.findPreviousVerifiedInOrder(invocations, context);
        assertNull(previous);

        context.markVerified(simpleMethodInvocation);
        context.markVerified(simpleMethodInvocationTwo);

        previous = InvocationsFinder.findPreviousVerifiedInOrder(invocations, context);
        assertSame(simpleMethodInvocationTwo, previous);
    }

    @Test
    public void shouldFindAllStackTraces() {
        List<Location> all = InvocationsFinder.getAllLocations(invocations);
        Assertions.assertThat(all).contains(simpleMethodInvocation.getLocation(), simpleMethodInvocationTwo.getLocation(), differentMethodInvocation.getLocation());
    }

    @Test
    public void shouldNotFindLocationsForEmptyInvocationsList() {
        Assertions.assertThat(InvocationsFinder.getAllLocations(Collections.<Invocation>emptyList())).isEmpty();
    }
}
