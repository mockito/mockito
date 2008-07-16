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
        stub(mock.oneArg(anyObject())).toReturn("0");
        stub(mock.oneArg(anyString())).toReturn("1");
        stub(mock.forList(anyList())).toReturn("2");
        stub(mock.forMap(anyMap())).toReturn("3");
        stub(mock.forCollection(anyCollection())).toReturn("4");
        
        assertEquals("0", mock.oneArg((Object) null));
        assertEquals("1", mock.oneArg((String) null));
        assertEquals("2", mock.forList(null));
        assertEquals("3", mock.forMap(null));
        assertEquals("4", mock.forCollection(null));
    }
    
    @Test
    public void shouldAnyPrimiteWraperMatchersAcceptNull() {
        stub(mock.forInteger(anyInt())).toReturn("0");
        stub(mock.forCharacter(anyChar())).toReturn("1");
        stub(mock.forShort(anyShort())).toReturn("2");
        stub(mock.forByte(anyByte())).toReturn("3");
        stub(mock.forBoolean(anyBoolean())).toReturn("4");
        stub(mock.forLong(anyLong())).toReturn("5");
        stub(mock.forFloat(anyFloat())).toReturn("6");
        stub(mock.forDouble(anyDouble())).toReturn("7");
        
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