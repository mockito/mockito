/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;

public class AllInvocationsFinderTest extends TestBase {
    
    private AllInvocationsFinder finder;
    private IMethods mockTwo;
    private IMethods mockOne;

    @Before
    public void setup() {
        finder = new AllInvocationsFinder();
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
    }
    
    @Test
    public void shouldGetAllInvocationsInOrder() throws Exception {
        mockOne.simpleMethod(100);
        mockTwo.simpleMethod(200);
        mockOne.simpleMethod(300);
        
        List<Invocation> invocations = finder.find(asList(mockOne, mockTwo));
        
        assertEquals(3, invocations.size());
        assertArgumentEquals(100, invocations.get(0));
        assertArgumentEquals(200, invocations.get(1));
        assertArgumentEquals(300, invocations.get(2));
    }

    @Test
    public void shouldNotCountDuplicatedInteractions() throws Exception {
        mockOne.simpleMethod(100);

        List<Invocation> invocations = finder.find(asList(mockOne, mockOne, mockOne));

        assertEquals(1, invocations.size());
    }

    private void assertArgumentEquals(Object argumentValue, Invocation invocation) {
        assertEquals(argumentValue, invocation.getArguments()[0]);
    }
}