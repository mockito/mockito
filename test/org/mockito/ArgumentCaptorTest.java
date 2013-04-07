/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.After;
import org.junit.Test;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class ArgumentCaptorTest extends TestBase {
    
    @Test
    public void tell_handy_return_values_to_return_value_for() throws Exception {
        //given
        final Object expected = new Object(); 
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        argumentCaptor.handyReturnValues = will_return(expected);
        
        //when
        Object returned = argumentCaptor.capture();

        //then
        assertEquals(expected, returned);
    }

    private HandyReturnValues will_return(final Object expected) {
        return new HandyReturnValues() {
            @Override
            public <T> T returnFor(Class<T> clazz) {
                return (T) expected;
            }
        };
    }

    @After
    public void yes_I_know_some_matchers_are_misplaced() {
        resetState();
    }
}