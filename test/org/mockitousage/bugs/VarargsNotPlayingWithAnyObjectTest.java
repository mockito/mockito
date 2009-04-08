/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

//see issue 62
public class VarargsNotPlayingWithAnyObjectTest extends TestBase {

    interface VarargMethod {
        Object run(String... args);
    }
    
    @Mock VarargMethod mock;

    @Test
    public void shouldAllowAnyObjectForVarArgs() {
        mock.run("a", "b");
        
        verify(mock).run((String[]) anyObject());
        verify(mock).run(anyString(), anyString());
        verify(mock).run((String) anyObject(), (String) anyObject());
        verify(mock).run(new String[] {anyObject()});
        
        verify(mock, never()).run();
        verify(mock, never()).run(anyString(), eq("f"));
    }
    
    @Test
    public void shouldAllowAnyObjectForVarArgsStubbing() {
        when(mock.run((String[]) anyObject())).thenReturn("foo");
        
        assertEquals("foo", mock.run("a", "b"));
    }
}