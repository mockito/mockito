/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.exceptions.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.Equals;

@SuppressWarnings("unchecked")
public class MockControlTest {
    
    @Before
    @After
    public void resetState() {
        StateResetter.reset();
    }
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        MockitoState state = MockitoState.instance(); 
        LastArguments lastArguments = LastArguments.instance();
        
        lastArguments.reportMatcher(new Equals("test"));
        state.verifyingStarted(VerifyingMode.atLeastOnce());
        
        MockControl control = new MockControl(state, lastArguments);

        try {
            control.invoke(null, String.class.getDeclaredMethod("toString"), new Object[]{});
            fail();
        } catch (InvalidUseOfMatchersException e) {
        }
        
        assertNull(state.pullVerifyingMode());
    }
}
