/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class MatchersMixedWithRawArgumentsTest extends TestBase {

    private interface Methods {
        void int_int_String(int a, int b, String c);

        void int_String_int(int a, String b, int c);

    }

    @Mock
    private Methods mock;

    @Test
    public void int1() {
        mock.int_int_String(1, 2, "c");

        verify(mock).int_int_String(1, anyInt(), anyString());
    }

    @Test
    public void int2() {
        mock.int_int_String(1, 2, "c");

        verify(mock).int_int_String(1, 2, anyString());
    }

    @Test
    public void int3() {
        mock.int_int_String(1, 2, "c");

        verify(mock).int_int_String(anyInt(), anyInt(), anyString());
    }

    @Test
    public void int4() {
        mock.int_String_int(1, "b", 3);

        verify(mock).int_String_int(anyInt(), anyString(), 3);
    }

    @Test
    public void int5() {
        mock.int_String_int(1, "b", 3);

        verify(mock).int_String_int(anyInt(), anyString(), eq(3));
    }

    @Test
    public void int6() {
        mock.int_String_int(1, "b", 3);

        verify(mock).int_String_int(1, anyString(), 3);
    }

    /**
     * The value 0 is used by int-matchers as return value, therefore the raw-value 0 is interpreted as matcher.
     * In other words 0 is used as matcher indicator, to reduce the risk of a clash e.g. Integer.MIN+3 should be used.
     * This problem can be solved partially in case we know the type the matcher can process.
     */
    @Test
    public void int7_markerValueClash() {
        mock.int_String_int(0, "b", 3);

        assertThatThrownBy(new ThrowingCallable() { public void call(){

                verify(mock).int_String_int(0, anyString(), anyInt());

        }})
        .hasMessageContaining("3 matchers expected, 2 recorded");


    }
}
