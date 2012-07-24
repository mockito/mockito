package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unused")
public class ReturnsDeepStubsTest {
    interface UpperBoundedTypeWithClass<E extends Number & Comparable<E>> {
        E get();
    }
    interface UpperBoundedTypeWithInterfaces<E extends Comparable<E> & Cloneable> {
        E get();
    }
    interface ListOfNumbers extends List<Number> {}
    interface ListOfAnyNumbers<N extends Number & Cloneable> extends List<N> {}

    interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        Set<Number> remove(Object key); // override with fixed ParameterizedType
        List<? super Number> returningWildcard();
        K returningK();
        <O extends K> List<O> paramTypeWithTypeParams();
        <S extends Appendable, T extends S> T twoTypeParams(S s);
        <O extends K> O typeVarWithTypeParams();
    }

    @Test
    public void returning_deep_stubs_1() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

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
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        Cloneable cloneable1 = mock.paramTypeWithTypeParams().get(0);
        Comparable<?> comparable1 = mock.paramTypeWithTypeParams().get(0);
        Cloneable cloneable2 = mock.returningK();
        Comparable<?> comparable2 = mock.returningK();
        Cloneable cloneable3 = (Cloneable) mock.typeVarWithTypeParams();
    }

    @Test
    @Ignore("TODO WildCard")
    public void returning_deep_stubs_3() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        List<? super Integer> objects = mock.returningWildcard();
        Number n = (Number) mock.returningWildcard().get(45);
    }

    @Test(expected = ClassCastException.class)
    public void returning_deep_stub_dont_work_because_StringBuilder_is_subject_to_erasure() throws Exception {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        StringBuilder stringBuilder = mock.twoTypeParams(new StringBuilder()).append(2).append(3); // ClassCastException
    }
}
