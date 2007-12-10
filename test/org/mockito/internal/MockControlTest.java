/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.Equals;

@SuppressWarnings("unchecked")
public class MockControlTest extends RequiresValidState {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        LastArguments.instance().reportMatcher(new Equals("test"));
        
        MockitoStateImpl state = new MockitoStateImpl();
        state.verifyingStarted(VerifyingMode.atLeastOnce());
        MockControl control = new MockControl(state);
        
        //TODO fix this test make it stub invocation factory with thrown exception

        try {
            control.invoke(null, String.class.getDeclaredMethod("toString"), new Object[]{});
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(state.pullVerifyingMode());
    }
}