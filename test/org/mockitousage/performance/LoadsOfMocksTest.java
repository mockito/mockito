/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.performance;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LoadsOfMocksTest extends TestBase {

    @Ignore("Use it for performance checks")
    @Test
    public void testSomething() {
        final List mocks = new LinkedList();
        for (int i = 0; i < 50000; i++) {
            System.out.println("Mock no: " + i);
            final IMethods mock = mock(IMethods.class);
            mocks.add(mock);
            
            when(mock.simpleMethod(1)).thenReturn("one");
            when(mock.simpleMethod(2)).thenReturn("two");
            
            assertEquals("one", mock.simpleMethod(1));
            assertEquals("two", mock.simpleMethod(2));
            
            verify(mock).simpleMethod(1);
            verify(mock).simpleMethod(2);
        }
    }
}