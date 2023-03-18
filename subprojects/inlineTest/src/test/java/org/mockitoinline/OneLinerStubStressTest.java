/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OneLinerStubStressTest {

    public class OneLinerStubTestClass {
        public String getStuff() {
            return "A";
        }
    }

    private static String generateLargeString() {
        final int length = 2000000;
        final StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i <= length; i++) {
            stringBuilder.append("B");
        }
        return stringBuilder.toString();
    }

    @Test
    public void call_a_lot_of_mocks_using_one_line_stubbing() {
        //This requires smaller heap set for the test process, see "inline.gradle"
        final String returnValue = generateLargeString();
        for (int i = 0; i < 50000; i++) {
            // make sure that mock object does not get cleaned up prematurely
            final OneLinerStubTestClass mock =
                when(mock(OneLinerStubTestClass.class).getStuff()).thenReturn(returnValue).getMock();
            assertEquals(returnValue, mock.getStuff());
        }
    }
}
