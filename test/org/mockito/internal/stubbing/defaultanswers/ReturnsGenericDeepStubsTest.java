/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unused")
public class ReturnsGenericDeepStubsTest {
    interface ListOfInteger extends List<Integer> {}

    interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        Set<Number> remove(Object key); // override with fixed ParameterizedType
        List<? super Number> returningWildcard();
        Map<String, K> returningNonMockableNestedGeneric();
        K returningK();
        <O extends K> List<O> paramTypeWithTypeParams();
        <S extends Appendable, T extends S> T twoTypeParams(S s);
        <O extends K> O typeVarWithTypeParams();
        Number returnsNormalType();
    }

    @Test
    public void returning_deep_stubs_1() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());

        Set<? extends Map.Entry<? extends Cloneable, Set<Number>>> entries = mock.entrySet();
        Iterator<? extends Map.Entry<? extends Cloneable,Set<Number>>> entriesIterator = mock.entrySet().iterator();
        Map.Entry<? extends Cloneable, Set<Number>> nextEntry = mock.entrySet().iterator().next();

        Cloneable cloneableKey = mock.entrySet().iterator().next().getKey();
        Comparable<?> comparableKey = mock.entrySet().iterator().next().getKey();

        Set<Number> value = mock.entrySet().iterator().next().getValue();
        Iterator<Number> numbersIterator = mock.entrySet().iterator().next().getValue().iterator();
        Number number = mock.entrySet().iterator().next().getValue().iterator().next();
    }

    @Test
    public void returning_deep_stubs_2() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());

        Cloneable cloneable1 = mock.paramTypeWithTypeParams().get(0);
        Comparable<?> comparable1 = mock.paramTypeWithTypeParams().get(0);
        Cloneable cloneable2 = mock.returningK();
        Comparable<?> comparable2 = mock.returningK();
        Cloneable cloneable3 = (Cloneable) mock.typeVarWithTypeParams();
    }

    @Test
    public void returning_deep_stubs_3() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());

        List<? super Integer> objects = mock.returningWildcard();
        Number n = (Number) mock.returningWildcard().get(45);
        n.floatValue();
    }

    @Test
    public void returning_deep_stubs_4() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());

        Number n = mock.returnsNormalType();
        n.floatValue();
    }

    @Test
    public void return_default_value_on_non_mockable_nested_generic() throws Exception {
        GenericsNest<?> genericsNest = mock(GenericsNest.class, new ReturnsGenericDeepStubs());
        ListOfInteger listOfInteger = mock(ListOfInteger.class, new ReturnsGenericDeepStubs());

        assertThat(genericsNest.returningNonMockableNestedGeneric().keySet().iterator().next()).isNull();
        assertThat(listOfInteger.get(25)).isEqualTo(0);
    }

    @Test(expected = ClassCastException.class)
    public void returning_deep_stub_dont_work_because_StringBuilder_is_subject_to_erasure() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());

        StringBuilder stringBuilder = mock.twoTypeParams(new StringBuilder()).append(2).append(3); // ClassCastException
    }
}
