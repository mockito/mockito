/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class GenericMasterTest {
    
    GenericMaster m = new GenericMaster();
    
    List<String> one;
    Set<Integer> two;
    Map<Double, String> map;
    String nonGeneric;
    List<Set<String>> nested;
    List<Set<Collection<String>>> multiNested;

    public interface ListSet extends List<Set> {}

    public List<Number> numberList() { return null; }
    public Comparable<Number> numberComparable() { return null; }
    public List rawList() { return null; }



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

    @Test
    public void can_identify_generic_type_of_returned_collection() throws Exception {
        assertEquals(Number.class, m.identifyGenericReturnType(method("numberList"), this.getClass()));
    }

    @Test
    public void can_identify_generic_type_of_returned_user_type() throws Exception {
        assertEquals(Number.class, m.identifyGenericReturnType(method("numberComparable"), this.getClass()));
    }

    @Test
    public void can_identify_generic_type_of_returned_type_when_owner_forces_generic_type() throws Exception {
        assertEquals(Set.class, m.identifyGenericReturnType(method(ListSet.class, "iterator"), ListSet.class));
    }

    @Test
    public void will_return_null_if_return_type_not_generic() throws Exception {
        assertEquals(null, m.identifyGenericReturnType(method("rawList"), this.getClass()));
    }




    private Method method(String noArgMethod) throws NoSuchMethodException {
        return method(this.getClass(), noArgMethod);
    }

    private Method method(Class<?> clazz, String noArgMethod) throws NoSuchMethodException {
        return clazz.getMethod(noArgMethod);
    }

    private Field field(String fieldName) throws SecurityException, NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }


}
