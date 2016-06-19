/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

//see issue 101
public class CovariantOverrideTest extends TestBase {

    public interface ReturnsObject {

        Object callMe();
    }

    public interface ReturnsString extends ReturnsObject {

        // Java 5 covariant override of method from parent interface
        String callMe();
    }

    @Test
    public void returnFoo1() {
        ReturnsObject mock = mock(ReturnsObject.class);
        when(mock.callMe()).thenReturn("foo");
        assertEquals("foo", mock.callMe()); // Passes
    }

    @Test
    public void returnFoo2() {
        ReturnsString mock = mock(ReturnsString.class);
        when(mock.callMe()).thenReturn("foo");
        assertEquals("foo", mock.callMe()); // Passes
    }

    @Test
    public void returnFoo3() {
        ReturnsObject mock = mock(ReturnsString.class);
        when(mock.callMe()).thenReturn("foo");
        assertEquals("foo", mock.callMe()); // Passes
    }

    @Test
    public void returnFoo4() {
        ReturnsString mock = mock(ReturnsString.class);
        mock.callMe(); // covariant override not generated
        ReturnsObject mock2 = mock; // Switch to base type to call covariant override
        verify(mock2).callMe(); // Fails: java.lang.AssertionError: expected:<foo> but was:<null>
    }
}