package org.easymock.internal;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class ToTypeMappingsTest {
    
    @SuppressWarnings("unchecked")
    @Test public void shouldReturnEmptyCollectionsOrNullForNonCollections() {
        assertTrue(((Collection)ToTypeMappings.emptyReturnValueFor(Collection.class)).isEmpty());
        
        assertTrue(((Set)ToTypeMappings.emptyReturnValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet)ToTypeMappings.emptyReturnValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet)ToTypeMappings.emptyReturnValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet)ToTypeMappings.emptyReturnValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet)ToTypeMappings.emptyReturnValueFor(LinkedHashSet.class)).isEmpty());        

        assertTrue(((List)ToTypeMappings.emptyReturnValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList)ToTypeMappings.emptyReturnValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList)ToTypeMappings.emptyReturnValueFor(LinkedList.class)).isEmpty());
        
        assertTrue(((Map)ToTypeMappings.emptyReturnValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap)ToTypeMappings.emptyReturnValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap)ToTypeMappings.emptyReturnValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap)ToTypeMappings.emptyReturnValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap)ToTypeMappings.emptyReturnValueFor(LinkedHashMap.class)).isEmpty());
        
        assertNull(ToTypeMappings.emptyReturnValueFor(String.class));
    }
}
