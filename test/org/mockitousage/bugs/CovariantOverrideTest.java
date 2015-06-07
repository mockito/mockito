/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockitoutil.TestBase;

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
        final ReturnsObject mock = mock(ReturnsObject.class);
        when(mock.callMe()).thenReturn("foo");
        assertEquals("foo", mock.callMe()); // Passes
    }

    @Test 
    public void returnFoo2() {
        final ReturnsString mock = mock(ReturnsString.class);
        when(mock.callMe()).thenReturn("foo");
        assertEquals("foo", mock.callMe()); // Passes
    }

    @Test 
    public void returnFoo3() {
        final ReturnsObject mock = mock(ReturnsString.class);
        when(mock.callMe()).thenReturn("foo");
        assertEquals("foo", mock.callMe()); // Passes
    }

    @Test 
    public void returnFoo4() {
        final ReturnsString mock = mock(ReturnsString.class);
        mock.callMe(); // covariant override not generated
        final ReturnsObject mock2 = mock; // Switch to base type to call covariant override
        verify(mock2).callMe(); // Fails: java.lang.AssertionError: expected:<foo> but was:<null>
    }
}