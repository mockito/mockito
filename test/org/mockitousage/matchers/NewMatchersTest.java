/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class NewMatchersTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldAllowAnyList() {
        when(mock.forList(anyList())).thenReturn("x");
        
        assertEquals("x", mock.forList(null));
        assertEquals("x", mock.forList(Arrays.asList("x", "y")));
        
        verify(mock, times(2)).forList(anyList());
    }
    
    @Test
    public void shouldAllowAnyCollection() {
        when(mock.forCollection(anyCollection())).thenReturn("x");
        
        assertEquals("x", mock.forCollection(null));
        assertEquals("x", mock.forCollection(Arrays.asList("x", "y")));
        
        verify(mock, times(2)).forCollection(anyCollection());
    }
    
    @Test
    public void shouldAllowAnyMap() {
        when(mock.forMap(anyMap())).thenReturn("x");
        
        assertEquals("x", mock.forMap(null));
        assertEquals("x", mock.forMap(new HashMap<String, String>()));
        
        verify(mock, times(2)).forMap(anyMap());
    }
    
    @Test
    public void shouldAllowAnySet() {
        when(mock.forSet(anySet())).thenReturn("x");
        
        assertEquals("x", mock.forSet(null));
        assertEquals("x", mock.forSet(new HashSet<String>()));
        
        verify(mock, times(2)).forSet(anySet());
    }
}