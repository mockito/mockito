/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@RunWith(MockitoJUnitRunner.class)
public class EarlyPointOfFailureWhenStubNotCalledTest extends TestBase {

    @Mock private IMethods mock;

    @Ignore
    @Test
    public void shouldProvideExtraPointOfFailure() throws Exception {
        when(mock.simpleMethod()).thenReturn("foo");
        
        String result = getFromSimpleMethod();
        
        assertEquals("foo", result);
    }

    private String getFromSimpleMethod() {
        //Let's say you've called wrong method on the mock
        return mock.differentMethod();
    }
    
    @Ignore
    @Test
    public void shouldProvideExtraPointOfFailureWhenVerificationFails() throws Exception {
        when(mock.simpleMethod()).thenReturn("foo");
        
        verify(mock).differentMethod();
    }
}