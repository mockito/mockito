/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.mockito.RequiresValidState;

public class EmptyReturnValuesTest extends RequiresValidState {
    
    @SuppressWarnings("unchecked")
    @Test public void shouldReturnEmptyCollectionsOrNullForNonCollections() {
        assertTrue(((Collection)EmptyReturnValues.emptyValueFor(Collection.class)).isEmpty());
        
        assertTrue(((Set)EmptyReturnValues.emptyValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet)EmptyReturnValues.emptyValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet)EmptyReturnValues.emptyValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet)EmptyReturnValues.emptyValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet)EmptyReturnValues.emptyValueFor(LinkedHashSet.class)).isEmpty());        

        assertTrue(((List)EmptyReturnValues.emptyValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList)EmptyReturnValues.emptyValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList)EmptyReturnValues.emptyValueFor(LinkedList.class)).isEmpty());
        
        assertTrue(((Map)EmptyReturnValues.emptyValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap)EmptyReturnValues.emptyValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap)EmptyReturnValues.emptyValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap)EmptyReturnValues.emptyValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap)EmptyReturnValues.emptyValueFor(LinkedHashMap.class)).isEmpty());
        
        assertNull(EmptyReturnValues.emptyValueFor(String.class));
    }
}
