/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {
    
    GenericMaster m = new GenericMaster();
    
    List<String> one;
    Set<Integer> two;
    Map<Double, String> map;
    String nonGeneric;
    List<Set<String>> nested;
    List<Set<Collection<String>>> multiNested;

    public interface ListSet extends List<Set<?>> {}
    public interface MapNumberString extends Map<Number, String> {}
    public class HashMapNumberString<K extends Number> extends HashMap<K, String> {}

    public List<Number> numberList() { return null; }
    public Comparable<Number> numberComparable() { return null; }
    @SuppressWarnings("rawtypes")
    public List rawList() { return null; }
    public List<? extends Type> typeList() { return null; }



    @Test
    public void should_find_generic_class() throws Exception {
        assertEquals(String.class, m.getGenericType(field("one")));
        assertEquals(Integer.class, m.getGenericType(field("two")));
        assertEquals(Double.class, m.getGenericType(field("map")));
    }
    
    @Test
    public void should_get_object_for_non_generic() throws Exception {
        assertEquals(Object.class, m.getGenericType(field("nonGeneric")));
    }
    
    @Test
    public void should_deal_with_nested_generics() throws Exception {
        assertEquals(Set.class, m.getGenericType(field("nested")));
        assertEquals(Set.class, m.getGenericType(field("multiNested")));
    }

    private Field field(String fieldName) throws SecurityException, NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }

}
