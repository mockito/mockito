/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.util.*;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class ReturnsEmptyValuesTest extends TestBase {

    ReturnsEmptyValues values = new ReturnsEmptyValues();

    @Test public void shouldReturnEmptyCollectionsOrNullForNonCollections() {
        assertTrue(((Collection) values.returnValueFor(Collection.class)).isEmpty());

        assertTrue(((Set) values.returnValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet) values.returnValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet) values.returnValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet) values.returnValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet) values.returnValueFor(LinkedHashSet.class)).isEmpty());

        assertTrue(((List) values.returnValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList) values.returnValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList) values.returnValueFor(LinkedList.class)).isEmpty());

        assertTrue(((Map) values.returnValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap) values.returnValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap) values.returnValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap) values.returnValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap) values.returnValueFor(LinkedHashMap.class)).isEmpty());

        assertNull(values.returnValueFor(String.class));
    }

    @Test public void shouldReturnPrimitive() {
        assertEquals(false, values.returnValueFor(Boolean.TYPE));
        assertEquals((char) 0, values.returnValueFor(Character.TYPE));
        assertEquals(0, values.returnValueFor(Byte.TYPE));
        assertEquals(0, values.returnValueFor(Short.TYPE));
        assertEquals(0, values.returnValueFor(Integer.TYPE));
        assertEquals(0, values.returnValueFor(Long.TYPE));
        assertEquals(0, values.returnValueFor(Float.TYPE));
        assertEquals(0, values.returnValueFor(Double.TYPE));
    }

    @Test public void shouldReturnNonZeroForCompareToMethod() {
        //given
        Date d = mock(Date.class);
        d.compareTo(new Date());
        Invocation compareTo = this.getLastInvocation();

        //when
        Object result = values.answer(compareTo);
        
        //then
        assertTrue(result != (Object) 0);
    }

}
