/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.progress.VerificationMode;

@SuppressWarnings("unchecked")
public class MockControlTest extends RequiresValidState {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        MockingProgressImpl state = new MockingProgressImpl();
        state.verificationStarted(VerificationMode.atLeastOnce());
        MockControl control = new MockControl(state, new ExceptionThrowingBinder());
        
        try {
            control.invoke(null, String.class.getDeclaredMethod("toString"), new Object[]{});
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(state.pullVerificationMode());
    }
    
    private class ExceptionThrowingBinder extends MatchersBinder {
        @Override
        public InvocationMatcher bindMatchers(Invocation invocation) {
            throw new InvalidUseOfMatchersException("");
        }
    }
}