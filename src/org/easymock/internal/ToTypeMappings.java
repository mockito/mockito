package org.easymock.internal;

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

public class ToTypeMappings {
    
    @SuppressWarnings("unchecked")
    protected static Map<Class, Object> emptyReturnValueToPrimitiveType = new HashMap<Class, Object>();

    static {
        emptyReturnValueToPrimitiveType.put(Void.TYPE, null);
        emptyReturnValueToPrimitiveType.put(Boolean.TYPE, Boolean.FALSE);
        emptyReturnValueToPrimitiveType.put(Byte.TYPE, new Byte((byte) 0));
        emptyReturnValueToPrimitiveType.put(Short.TYPE, new Short((short) 0));
        emptyReturnValueToPrimitiveType.put(Character.TYPE, new Character((char) 0));
        emptyReturnValueToPrimitiveType.put(Integer.TYPE, new Integer(0));
        emptyReturnValueToPrimitiveType.put(Long.TYPE, new Long(0));
        emptyReturnValueToPrimitiveType.put(Float.TYPE, new Float(0));
        emptyReturnValueToPrimitiveType.put(Double.TYPE, new Double(0));
    }

    @SuppressWarnings("unchecked")
    protected static Map<Class, Class> primitiveToWrapperType = new HashMap<Class, Class>();

    static {
        primitiveToWrapperType.put(Boolean.TYPE, Boolean.class);
        primitiveToWrapperType.put(Byte.TYPE, Byte.class);
        primitiveToWrapperType.put(Short.TYPE, Short.class);
        primitiveToWrapperType.put(Character.TYPE, Character.class);
        primitiveToWrapperType.put(Integer.TYPE, Integer.class);
        primitiveToWrapperType.put(Long.TYPE, Long.class);
        primitiveToWrapperType.put(Float.TYPE, Float.class);
        primitiveToWrapperType.put(Double.TYPE, Double.class);
    }
    
    public static Object emptyReturnValueFor(Class type) {
        if (type.isPrimitive()) {
            return emptyReturnValueToPrimitiveType.get(type);
        }
        
        return emptyReturnValueToType.get(type);
    }
    
    @SuppressWarnings("unchecked")
    protected static Map<Class, Object> emptyReturnValueToType = new HashMap<Class, Object>();
    
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