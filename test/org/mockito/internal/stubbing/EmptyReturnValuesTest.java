/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class EmptyReturnValuesTest extends TestBase {
    
    @SuppressWarnings("unchecked")
    @Test public void shouldReturnEmptyCollectionsOrNullForNonCollections() {
        assertTrue(((Collection) EmptyReturnValues.emptyValueFor(Collection.class)).isEmpty());

        assertTrue(((Set) EmptyReturnValues.emptyValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet) EmptyReturnValues.emptyValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet) EmptyReturnValues.emptyValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet) EmptyReturnValues.emptyValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet) EmptyReturnValues.emptyValueFor(LinkedHashSet.class)).isEmpty());

        assertTrue(((List) EmptyReturnValues.emptyValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList) EmptyReturnValues.emptyValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList) EmptyReturnValues.emptyValueFor(LinkedList.class)).isEmpty());

        assertTrue(((Map) EmptyReturnValues.emptyValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap) EmptyReturnValues.emptyValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap) EmptyReturnValues.emptyValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap) EmptyReturnValues.emptyValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap) EmptyReturnValues.emptyValueFor(LinkedHashMap.class)).isEmpty());
        
        assertNull(EmptyReturnValues.emptyValueFor(String.class));
    }
}
