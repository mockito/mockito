/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    public interface ListSet extends List<Set> {}
    public interface MapNumberString extends Map<Number, String> {}
    public class HashMapNumberString<K extends Number> extends HashMap<K, String> {}

    public List<Number> numberList() { return null; }
    public Comparable<Number> numberComparable() { return null; }
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

    @Test
    public void can_identify_generic_type_of_returned_collection() throws Exception {
        assertEquals(Number.class, m.identifyGenericReturnType(method("numberList"), this.getClass()));
    }

    @Test
    public void can_identify_generic_type_of_returned_user_type() throws Exception {
        assertEquals(Number.class, m.identifyGenericReturnType(method("numberComparable"), this.getClass()));
    }

    @Test
    public void can_identify_generic_type_of_returned_type_when_owner_type_forces_generic_type() throws Exception {
        assertEquals(Set.class, m.identifyGenericReturnType(method(ListSet.class, "iterator"), ListSet.class));
        assertEquals(Number.class, m.identifyGenericReturnType(method(MapNumberString.class, "keySet"), MapNumberString.class));
        assertEquals(String.class, m.identifyGenericReturnType(method(MapNumberString.class, "values"), MapNumberString.class));
        assertEquals(String.class, m.identifyGenericReturnType(method(MapNumberString.class, "remove"), MapNumberString.class));
        assertEquals(Map.Entry.class, m.identifyGenericReturnType(method(MapNumberString.class, "entrySet"), MapNumberString.class));
    }

    @Test
    public void can_identify_type_variable_upper_bound() throws Exception {
        assertEquals(Number.class, m.identifyGenericReturnType(method(HashMapNumberString.class, "keySet"), HashMapNumberString.class));
        assertEquals(Type.class, m.identifyGenericReturnType(method("typeList"), this.getClass()));
    }

    @Test
    @Ignore("Internal API not ready for nested generics, doesn't work")
    public void can_identify_nested_generic_type_of_returned_type_when_owner_forces_generic_type() throws Exception {
    }

    @Test
    public void will_return_null_if_return_type_not_generic() throws Exception {
        assertEquals(null, m.identifyGenericReturnType(method("rawList"), this.getClass()));
    }




    private Method method(String noArgMethod) throws NoSuchMethodException {
        return method(this.getClass(), noArgMethod);
    }

    private Method method(Class<?> clazz, String noArgMethod) throws NoSuchMethodException {
        for (Method method : clazz.getMethods()) {
            if (method.getName().contains(noArgMethod)) {
                return method;
            }
        }
        throw new NoSuchMethodException("method " + noArgMethod + " do not exist in " + clazz.getSimpleName());
    }

    private Field field(String fieldName) throws SecurityException, NoSuchFieldException {
        return this.getClass().getDeclaredField(fieldName);
    }


}
