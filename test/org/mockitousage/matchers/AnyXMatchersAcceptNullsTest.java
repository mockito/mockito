/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class AnyXMatchersAcceptNullsTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldAnyXMatchersAcceptNull() {
        when(mock.oneArg(anyObject())).thenReturn("0");
        when(mock.oneArg(anyString())).thenReturn("1");
        when(mock.forList(anyList())).thenReturn("2");
        when(mock.forMap(anyMap())).thenReturn("3");
        when(mock.forCollection(anyCollection())).thenReturn("4");
        when(mock.forSet(anySet())).thenReturn("5");
        
        assertEquals("0", mock.oneArg((Object) null));
        assertEquals("1", mock.oneArg((String) null));
        assertEquals("2", mock.forList(null));
        assertEquals("3", mock.forMap(null));
        assertEquals("4", mock.forCollection(null));
        assertEquals("5", mock.forSet(null));
    }
    
    @Test
    public void shouldAnyPrimiteWraperMatchersAcceptNull() {
        when(mock.forInteger(anyInt())).thenReturn("0");
        when(mock.forCharacter(anyChar())).thenReturn("1");
        when(mock.forShort(anyShort())).thenReturn("2");
        when(mock.forByte(anyByte())).thenReturn("3");
        when(mock.forBoolean(anyBoolean())).thenReturn("4");
        when(mock.forLong(anyLong())).thenReturn("5");
        when(mock.forFloat(anyFloat())).thenReturn("6");
        when(mock.forDouble(anyDouble())).thenReturn("7");
        
        assertEquals("0", mock.forInteger(null));
        assertEquals("1", mock.forCharacter(null));
        assertEquals("2", mock.forShort(null));
        assertEquals("3", mock.forByte(null));
        assertEquals("4", mock.forBoolean(null));
        assertEquals("5", mock.forLong(null));
        assertEquals("6", mock.forFloat(null));
        assertEquals("7", mock.forDouble(null));
    }
}