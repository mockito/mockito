/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.exceptions.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.Equals;
import org.mockito.util.RequiresValidState;

@SuppressWarnings("unchecked")
public class MockControlTest extends RequiresValidState {
    
    @Before
    @After
    public void resetState() {
        StateResetter.reset();
    }
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        LastArguments.instance().reportMatcher(new Equals("test"));
        MockitoState.instance().verifyingStarted(VerifyingMode.atLeastOnce());
        
        MockControl control = new MockControl();

        try {
            control.invoke(null, String.class.getDeclaredMethod("toString"), new Object[]{});
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(MockitoState.instance().pullVerifyingMode());
    }
}
