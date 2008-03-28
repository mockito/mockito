/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ConfiguringMocksToReturnFakesTest extends AllowsFakingReturnValues {
    
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