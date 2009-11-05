/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.invocation.*;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitoutil.TestBase;
@SuppressWarnings({"unchecked","serial"})
public class MockHandlerTest extends TestBase {
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        //given
        Invocation invocation = new InvocationBuilder().toInvocation();
        MockHandler handler = new MockHandler();
        handler.mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
        handler.matchersBinder = new MatchersBinder() {
            public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
                throw new InvalidUseOfMatchersException();
            }
        };
        
        try {
            //when
            handler.handle(invocation);
            
            //then
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(handler.mockingProgress.pullVerificationMode());
    }
}