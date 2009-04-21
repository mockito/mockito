/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MoreMatchersTest extends TestBase {

    @Mock private IMethods mock;
    
    @Test
    public void shouldHelpOutWithUnnecessaryCasting() {
        when(mock.objectArgMethod(any(String.class))).thenReturn("string");
        
        assertEquals("string", mock.objectArgMethod("foo"));
    }

    @Test
    public void shouldAnyBeActualAliasToAnyObject() {
        mock.simpleMethod((Object) null);

        verify(mock).simpleMethod(anyObject());
        verify(mock).simpleMethod(any(Object.class));
    }
    
    @Test
    public void shouldHelpOutWithUnnecessaryCastingOfCollections() {
//        TODO: implement after 1.7
//        when(mock.listArgMethod(anyListOf(String.class))).thenReturn("list");
//        when(mock.collectionArgMethod(anyCollectionOf(String.class))).thenReturn("collection");
//        
//        assertEquals("list", mock.listArgMethod(new LinkedList()));
//        assertEquals(null, mock.listArgMethod(new LinkedList()));
    }
}