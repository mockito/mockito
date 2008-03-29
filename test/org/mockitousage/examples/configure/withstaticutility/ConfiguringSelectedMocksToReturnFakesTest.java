/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withstaticutility;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockitousage.examples.configure.withstaticutility.AllowsFakingReturnValues.*;

import org.junit.Test;

public class ConfiguringSelectedMocksToReturnFakesTest {
    
    @Test
    public void shouldAllowConfiguringMocksToReturnFakes() throws Exception {
        MyObject fakeReturningMock = mock(MyObject.class);
        //configure mock to return fakes
        fakeReturnValues(fakeReturningMock);
        
        //create ordinary mock
        MyObject ordinaryMock = mock(MyObject.class);
        
        //returns mock instead of null
        assertNotNull(fakeReturningMock.returnMyObject());
        //returns empty string instead of null
        assertEquals("", fakeReturningMock.returnString());
        
        //returns defaults
        assertNull(ordinaryMock.returnMyObject());
        assertNull(ordinaryMock.returnString());
    }
    
    interface MyObject {
        MyObject returnMyObject();
        String returnString();
        boolean returnBoolean();
    }
}