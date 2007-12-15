/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

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

public class EmptyReturnValues {
    
    public static Object emptyValueFor(Class<?> type) {
        return emptyReturnValueToType.get(type);
    }
    
    @SuppressWarnings("unchecked")
    private static Map<Class, Object> emptyReturnValueToType = new HashMap<Class, Object>();
    
    static {
        emptyReturnValueToType.put(Collection.class, new LinkedList<Object>());
        
        emptyReturnValueToType.put(Set.class, new HashSet<Object>());
        emptyReturnValueToType.put(HashSet.class, new HashSet<Object>());
        emptyReturnValueToType.put(SortedSet.class, new TreeSet<Object>());        
        emptyReturnValueToType.put(TreeSet.class, new TreeSet<Object>());
        emptyReturnValueToType.put(LinkedHashSet.class, new LinkedHashSet<Object>());        

        emptyReturnValueToType.put(List.class, new LinkedList<Object>());       
        emptyReturnValueToType.put(LinkedList.class, new LinkedList<Object>());
        emptyReturnValueToType.put(ArrayList.class, new ArrayList<Object>());
        
        emptyReturnValueToType.put(Map.class, new HashMap<Object, Object>());
        emptyReturnValueToType.put(HashMap.class, new HashMap<Object, Object>());
        emptyReturnValueToType.put(SortedMap.class, new TreeMap<Object, Object>());        
        emptyReturnValueToType.put(TreeMap.class, new TreeMap<Object, Object>());
        emptyReturnValueToType.put(LinkedHashMap.class, new LinkedHashMap<Object, Object>());
    }
}