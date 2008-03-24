/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.TestBase;

public class DefaultReturnValuesTest extends TestBase {
    
    @SuppressWarnings("unchecked")
    @Test public void shouldReturnEmptyCollectionsOrNullForNonCollections() {
        DefaultReturnValues values = new DefaultReturnValues();
        
        assertTrue(((Collection) values.emptyValueFor(Collection.class)).isEmpty());

        assertTrue(((Set) values.emptyValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet) values.emptyValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet) values.emptyValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet) values.emptyValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet) values.emptyValueFor(LinkedHashSet.class)).isEmpty());

        assertTrue(((List) values.emptyValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList) values.emptyValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList) values.emptyValueFor(LinkedList.class)).isEmpty());

        assertTrue(((Map) values.emptyValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap) values.emptyValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap) values.emptyValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap) values.emptyValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap) values.emptyValueFor(LinkedHashMap.class)).isEmpty());
        
        assertNull(values.emptyValueFor(String.class));
    }
}
