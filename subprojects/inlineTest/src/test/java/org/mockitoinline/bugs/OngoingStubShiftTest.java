/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline.bugs;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

public class OngoingStubShiftTest {

    private static class StaticInt {
        static int getInt() {
            return 1;
        }
    }

    private static class StaticStr {
        static String getStr() {
            return Integer.toString(StaticInt.getInt());
        }
    }

    @Test
    public void keep_ongoing_stub_when_spy() {
        try (MockedStatic<StaticInt> mockInt = mockStatic(StaticInt.class);
             MockedStatic<StaticStr> mockStr = mockStatic(StaticStr.class, CALLS_REAL_METHODS)) {

            mockStr.when(StaticStr::getStr).thenReturn("1");
            assertEquals("1", StaticStr.getStr());
        }
    }

    private static class StaticWithException {
        static String getString() {
            return Integer.toString(getInt());
        }

        static int getInt() {
            throw new NullPointerException();
        }
    }

    @Test
    public void keep_ongoing_stub_when_exception() {
        try (MockedStatic<StaticWithException> mock = mockStatic(StaticWithException.class, CALLS_REAL_METHODS)) {
            mock.when(StaticWithException::getString).thenReturn("1");
            assertEquals("1", StaticWithException.getString());
        }
    }
}
