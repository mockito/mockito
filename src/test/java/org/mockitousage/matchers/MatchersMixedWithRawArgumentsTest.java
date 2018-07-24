/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class MatchersMixedWithRawArgumentsTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private interface Methods {
        void int_int_String(int a, int b, String c);

        void int_Integer(int a, Integer b);

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

        assertThatThrownBy(new ThrowingCallable() {
            public void call() {

                verify(mock).int_String_int(0, anyString(), anyInt());

            }
        })
            .hasMessageContaining("3 matchers expected, 2 recorded");
    }

    @Test
    public void int8() {
        mock.int_Integer(1, 1);

        verify(mock).int_Integer(1, anyInt());
    }


    @Test
    public void int9() {
        mock.int_Integer(1, 1);

        verify(mock).int_Integer( anyInt(),1);
    }


    @Test
    public void int10() {
        mock.int_Integer(1, null);

        verify(mock).int_Integer( anyInt(),null);
    }

    @Test
    public void int11() {
        mock.int_Integer(1, 0);

        assertThatThrownBy(new ThrowingCallable() {
            public void call() {
                verify(mock).int_Integer(anyInt(), eq(1));
            }
        }).hasMessageContaining("Argument(s) are different!");
    }
}
