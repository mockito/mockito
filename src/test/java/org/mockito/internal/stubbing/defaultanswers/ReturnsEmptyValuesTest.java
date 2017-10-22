/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.defaultanswers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesTest extends TestBase {

    private final ReturnsEmptyValues values = new ReturnsEmptyValues();

    @Test
    public void should_return_empty_collections_or_null_for_non_collections() {
        assertTrue(((Number[]) values.returnValueFor(Number[].class)).length == 0);

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
    public void should_return_empty_Optional() throws Exception {
        assertThat((Optional) values.returnValueFor(Optional.class)).isEmpty();
        assertThat((OptionalInt) values.returnValueFor(OptionalInt.class)).isEmpty();
        assertThat((OptionalLong) values.returnValueFor(OptionalLong.class)).isEmpty();
        assertThat((OptionalDouble) values.returnValueFor(OptionalDouble.class)).isEmpty();
    }

    @Test
    public void should_return_empty_Streams() throws Exception {
        assertThat((Stream) values.returnValueFor(Stream.class)).isEmpty();
        assertThat(((DoubleStream) values.returnValueFor(DoubleStream.class)).count()).isEqualTo(0);
        assertThat(((IntStream) values.returnValueFor(IntStream.class)).count()).isEqualTo(0);
        assertThat(((LongStream) values.returnValueFor(LongStream.class)).count()).isEqualTo(0);
    }

}
