package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {

    interface MapWithNestedGenerics<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {}


    @Test
    public void testName() throws Exception {
        MapWithNestedGenerics<?> mock = mock(MapWithNestedGenerics.class, RETURNS_DEEP_STUBS);

        Set<? extends Map.Entry<? extends Cloneable, Set<Number>>> entries = mock.entrySet();
        Iterator<? extends Map.Entry<? extends Cloneable,Set<Number>>> entriesIterator = mock.entrySet().iterator();
        Map.Entry<? extends Cloneable, Set<Number>> nextEntry = mock.entrySet().iterator().next();

        Cloneable cloneableKey = mock.entrySet().iterator().next().getKey();
        Comparable<?> comparableKey = mock.entrySet().iterator().next().getKey();

        Set<Number> value = mock.entrySet().iterator().next().getValue();
        Iterator<Number> numbersIterator = mock.entrySet().iterator().next().getValue().iterator();
        Number number = mock.entrySet().iterator().next().getValue().iterator().next();
    }



}
