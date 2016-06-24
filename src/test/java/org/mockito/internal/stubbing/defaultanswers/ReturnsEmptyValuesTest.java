/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assume;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import java.util.*;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesTest extends TestBase {

    private final ReturnsEmptyValues values = new ReturnsEmptyValues();

    @Test
    public void should_return_empty_collections_or_null_for_non_collections() {
        assertTrue(((Collection<?>) values.returnValueFor(Collection.class)).isEmpty());

        assertTrue(((Set<?>) values.returnValueFor(Set.class)).isEmpty());
        assertTrue(((SortedSet<?>) values.returnValueFor(SortedSet.class)).isEmpty());
        assertTrue(((HashSet<?>) values.returnValueFor(HashSet.class)).isEmpty());
        assertTrue(((TreeSet<?>) values.returnValueFor(TreeSet.class)).isEmpty());
        assertTrue(((LinkedHashSet<?>) values.returnValueFor(LinkedHashSet.class)).isEmpty());

        assertTrue(((List<?>) values.returnValueFor(List.class)).isEmpty());
        assertTrue(((ArrayList<?>) values.returnValueFor(ArrayList.class)).isEmpty());
        assertTrue(((LinkedList<?>) values.returnValueFor(LinkedList.class)).isEmpty());

        assertTrue(((Map<?, ?>) values.returnValueFor(Map.class)).isEmpty());
        assertTrue(((SortedMap<?, ?>) values.returnValueFor(SortedMap.class)).isEmpty());
        assertTrue(((HashMap<?, ?>) values.returnValueFor(HashMap.class)).isEmpty());
        assertTrue(((TreeMap<?, ?>) values.returnValueFor(TreeMap.class)).isEmpty());
        assertTrue(((LinkedHashMap<?, ?>) values.returnValueFor(LinkedHashMap.class)).isEmpty());

        assertNull(values.returnValueFor(String.class));
    }

    @Test
    public void should_return_empty_iterable() throws Exception {
        assertFalse(((Iterable<?>) values.returnValueFor(Iterable.class)).iterator().hasNext());
    }

    @Test
    public void should_return_primitive() {
        assertEquals(false, values.returnValueFor(Boolean.TYPE));
        assertEquals((char) 0, values.returnValueFor(Character.TYPE));
        assertEquals((byte) 0, values.returnValueFor(Byte.TYPE));
        assertEquals((short) 0, values.returnValueFor(Short.TYPE));
        assertEquals(0, values.returnValueFor(Integer.TYPE));
        assertEquals(0L, values.returnValueFor(Long.TYPE));
        assertEquals(0F, values.returnValueFor(Float.TYPE));
        assertEquals(0D, values.returnValueFor(Double.TYPE));
    }

    @Test
    public void should_return_non_zero_for_compareTo_method() {
        //
        // given
        Date d = mock(Date.class);
        d.compareTo(new Date());
        Invocation compareTo = this.getLastInvocation();

        //when
        Object result = values.answer(compareTo);

        //then
        assertTrue(result != (Object) 0);
    }

    @Test
    public void should_return_zero_if_mock_is_compared_to_itself() {
        //given
        Date d = mock(Date.class);
        d.compareTo(d);
        Invocation compareTo = this.getLastInvocation();

        //when
        Object result = values.answer(compareTo);

        //then
        assertEquals(0, result);
    }

    @Test
    public void should_return_empty_optional() throws Exception {
        Class<?> streamType = getClassOrSkipTest("java.util.stream.Stream");

        //given
        Object stream = mock(streamType);
        Object optional = streamType.getMethod("findAny").invoke(stream);
        assertNotNull(optional);
        assertFalse((Boolean) Class.forName("java.util.Optional").getMethod("isPresent").invoke(optional));

        Invocation findAny = this.getLastInvocation();

        //when
        Object result = values.answer(findAny);

        //then
        assertEquals(optional, result);
    }

    @Test
    public void should_return_empty_stream() throws Exception {
        // given
        Class<?> streamType = getClassOrSkipTest("java.util.stream.Stream");

        // when
        Object stream = values.returnValueFor(streamType);
        long count = (Long) streamType.getMethod("count").invoke(stream);

        // then
        assertEquals("count of empty Stream", 0L, count);
    }

    /**
     * Tries to load the given class. If the class is not found, the complete test is skipped.
     */
    private Class<?> getClassOrSkipTest(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Assume.assumeNoException("JVM does not support " + className, e);
            return null;
        }
    }

}
