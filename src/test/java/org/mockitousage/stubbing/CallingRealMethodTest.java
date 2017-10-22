/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CallingRealMethodTest extends TestBase {

    @Mock
    TestedObject mock;

    static class TestedObject {

        String value;

        void setValue(String value) {
            this.value = value;
        }

        String getValue() {
            return "HARD_CODED_RETURN_VALUE";
        }

        String callInternalMethod() {
            return getValue();
        }
    }

    @Test
    public void shouldAllowCallingInternalMethod() {
        when(mock.getValue()).thenReturn("foo");
        when(mock.callInternalMethod()).thenCallRealMethod();

        assertEquals("foo", mock.callInternalMethod());
    }

    @Test
    public void shouldReturnRealValue() {
        when(mock.getValue()).thenCallRealMethod();

        assertEquals("HARD_CODED_RETURN_VALUE", mock.getValue());
    }

    @Test
    public void shouldExecuteRealMethod() {
        doCallRealMethod().when(mock).setValue(anyString());

        mock.setValue("REAL_VALUE");

        assertEquals("REAL_VALUE", mock.value);
    }

    @Test
    public void shouldCallRealMethodByDefault() {
        TestedObject mock = mock(TestedObject.class, CALLS_REAL_METHODS);

        assertEquals("HARD_CODED_RETURN_VALUE", mock.getValue());
    }

    @Test
    public void shouldNotCallRealMethodWhenStubbedLater() {
        TestedObject mock = mock(TestedObject.class);

        when(mock.getValue()).thenCallRealMethod();
        when(mock.getValue()).thenReturn("FAKE_VALUE");

        assertEquals("FAKE_VALUE", mock.getValue());
    }
}
