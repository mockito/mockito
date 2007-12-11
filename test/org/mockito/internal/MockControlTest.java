/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.state.*;

@SuppressWarnings("unchecked")
public class MockControlTest extends RequiresValidState {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        MockitoStateImpl state = new MockitoStateImpl();
        state.verifyingStarted(OngoingVerifyingMode.atLeastOnce());
        MockControl control = new MockControl(state, new ExceptionThrowingBinder());
        
        try {
            control.invoke(null, String.class.getDeclaredMethod("toString"), new Object[]{});
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(state.pullVerifyingMode());
    }
    
    private class ExceptionThrowingBinder extends MatchersBinder {
        @Override
        public InvocationMatcher bindMatchers(Invocation invocation)
                throws InvalidUseOfMatchersException {
            throw new InvalidUseOfMatchersException("");
        }
    }
}