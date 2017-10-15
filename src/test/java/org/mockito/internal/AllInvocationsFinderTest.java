/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Stubbing;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.invocation.finder.AllInvocationsFinder.find;
import static org.mockito.internal.invocation.finder.AllInvocationsFinder.findStubbings;

public class AllInvocationsFinderTest extends TestBase {

    private IMethods mockTwo;
    private IMethods mockOne;

    @Before
    public void setup() {
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
    }

    @Test
    public void no_interactions() throws Exception {
        //expect
        assertTrue(find(asList(mockOne, mockTwo)).isEmpty());
        assertTrue(findStubbings(asList(mockOne, mockTwo)).isEmpty());
    }

    @Test
    public void provides_invocations_in_order() throws Exception {
        //given
        mockOne.simpleMethod(100);
        mockTwo.simpleMethod(200);
        mockOne.simpleMethod(300);

        //when
        List<Invocation> invocations = find(asList(mockOne, mockTwo));

        //then
        assertEquals(3, invocations.size());
        assertArgumentEquals(100, invocations.get(0));
        assertArgumentEquals(200, invocations.get(1));
        assertArgumentEquals(300, invocations.get(2));
    }

    @Test
    public void deduplicates_interactions_from_the_same_mock() throws Exception {
        //given
        mockOne.simpleMethod(100);

        //when
        List<Invocation> invocations = find(asList(mockOne, mockOne, mockOne));

        //then
        assertEquals(1, invocations.size());
    }

    @Test
    public void provides_stubbings_in_order() throws Exception {
        //given
        mockOne.simpleMethod(50); //ignored, not a stubbing
        when(mockOne.simpleMethod(100)).thenReturn("100");
        when(mockOne.simpleMethod(200)).thenReturn("200");
        when(mockTwo.simpleMethod(300)).thenReturn("300");

        //when
        List<Stubbing> stubbings = new ArrayList<Stubbing>(findStubbings(asList(mockOne, mockOne, mockTwo)));

        //then
        assertEquals(3, stubbings.size());
        assertArgumentEquals(100, stubbings.get(0).getInvocation());
        assertArgumentEquals(200, stubbings.get(1).getInvocation());
        assertArgumentEquals(300, stubbings.get(2).getInvocation());
    }

    private void assertArgumentEquals(Object argumentValue, Invocation invocation) {
        assertEquals(argumentValue, invocation.getArgument(0));
    }
}
