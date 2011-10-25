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
    public void tellHandyReturnValuesToReturnValueFor() throws Exception {
        //given
        final Object expected = new Object(); 
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        argumentCaptor.handyReturnValues = new HandyReturnValues() {
            @Override
            public <T> T returnFor(Class<T> clazz) {
                return (T) expected;
            }
        };
        
        //when
        Object returned = argumentCaptor.capture();
        
        //then
        assertEquals(expected, returned);
    }
    
    @After
    public void yesIKnowSomeMatchersAreMisplaced() {
        resetState();
    }
}