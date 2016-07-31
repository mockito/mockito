/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class NewMatchersTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldAllowAnyList() {
        when(mock.forList(anyList())).thenReturn("matched");
        
        assertEquals("matched", mock.forList(Arrays.asList("x", "y")));
        assertEquals(null, mock.forList(null));

        verify(mock, times(1)).forList(anyList());
    }
    
    @Test
    public void shouldAllowAnyCollection() {
        when(mock.forCollection(anyCollection())).thenReturn("matched");
        
        assertEquals("matched", mock.forCollection(Arrays.asList("x", "y")));
        assertEquals(null, mock.forCollection(null));

        verify(mock, times(1)).forCollection(anyCollection());
    }
    
    @Test
    public void shouldAllowAnyMap() {
        when(mock.forMap(anyMap())).thenReturn("matched");
        
        assertEquals("matched", mock.forMap(new HashMap<String, String>()));
        assertEquals(null, mock.forMap(null));

        verify(mock, times(1)).forMap(anyMap());
    }
    
    @Test
    public void shouldAllowAnySet() {
        when(mock.forSet(anySet())).thenReturn("matched");
        
        assertEquals("matched", mock.forSet(new HashSet<String>()));
        assertEquals(null, mock.forSet(null));

        verify(mock, times(1)).forSet(anySet());
    }

    @Test
    public void shouldAllowAnyIterable() {
        when(mock.forIterable(anyIterable())).thenReturn("matched");

        assertEquals("matched", mock.forIterable(new HashSet<String>()));
        assertEquals(null, mock.forIterable(null));

        verify(mock, times(1)).forIterable(anyIterable());
    }
}