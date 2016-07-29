/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoreMatchersTest extends TestBase {

    @Mock private IMethods mock;

    @Test
    public void should_help_out_with_unnecessary_casting() {
        when(mock.objectArgMethod(any(String.class))).thenReturn("string");

        assertEquals("string", mock.objectArgMethod("foo"));
    }

    @Test
    public void any_should_be_actual_alias_to_anyObject() {
        mock.simpleMethod((Object) null);

        verify(mock).simpleMethod(any());
        verify(mock).simpleMethod(anyObject());
    }

    @Test
    public void any_class_should_be_actual_alias_to_isA() {
        mock.simpleMethod(new ArrayList());

        verify(mock).simpleMethod(isA(List.class));
        verify(mock).simpleMethod(any(List.class));


        mock.simpleMethod((String) null);
        try {
            verify(mock).simpleMethod(isA(String.class));
            fail();
        } catch (AssertionError ignored) { }
        try {
            verify(mock).simpleMethod(any(String.class));
            fail();
        } catch (AssertionError ignored) { }
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_lists() {
        //Below yields compiler warning:
        //when(mock.listArgMethod(anyList())).thenReturn("list");
        when(mock.listArgMethod(anyListOf(String.class))).thenReturn("list");

        assertEquals("list", mock.listArgMethod(new LinkedList<String>()));
        assertEquals("list", mock.listArgMethod(Collections.<String>emptyList()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_sets() {
        //Below yields compiler warning:
        //when(mock.setArgMethod(anySet())).thenReturn("set");
        when(mock.setArgMethod(anySetOf(String.class))).thenReturn("set");

        assertEquals("set", mock.setArgMethod(new HashSet<String>()));
        assertEquals("set", mock.setArgMethod(Collections.<String>emptySet()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_maps() {
        //Below yields compiler warning:
        //when(mock.setArgMethod(anySet())).thenReturn("set");
        when(mock.forMap(anyMapOf(String.class, String.class))).thenReturn("map");

        assertEquals("map", mock.forMap(new HashMap<String, String>()));
        assertEquals("map", mock.forMap(Collections.<String, String>emptyMap()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_collections() {
        //Below yields compiler warning:
        //when(mock.setArgMethod(anySet())).thenReturn("set");
        when(mock.collectionArgMethod(anyCollectionOf(String.class))).thenReturn("collection");

        assertEquals("collection", mock.collectionArgMethod(new ArrayList<String>()));
        assertEquals("collection", mock.collectionArgMethod(Collections.<String>emptyList()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_iterables() {
        //Below yields compiler warning:
        //when(mock.setArgMethod(anySet())).thenReturn("set");
        when(mock.iterableArgMethod(anyIterableOf(String.class))).thenReturn("iterable");

        assertEquals("iterable", mock.iterableArgMethod(new ArrayList<String>()));
        assertEquals("iterable", mock.iterableArgMethod(Collections.<String>emptyList()));
    }

    @Test
    public void should_help_out_with_unnecessary_casting_of_nullity_checks() {
        when(mock.objectArgMethod(isNull(LinkedList.class))).thenReturn("string");
        when(mock.objectArgMethod(notNull(LinkedList.class))).thenReturn("string");
        when(mock.objectArgMethod(isNotNull(LinkedList.class))).thenReturn("string");

        assertEquals("string", mock.objectArgMethod(null));
        assertEquals("string", mock.objectArgMethod("foo"));
        assertEquals("string", mock.objectArgMethod("foo"));
    }

}
