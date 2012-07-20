package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class ClassEnhancedToParameterizedTypeTest {

    interface UpperBoundedTypeWithClass<E extends Number & Comparable<E>> {
        E get();
    }
    interface UpperBoundedTypeWithInterfaces<E extends Comparable<E> & Cloneable> {
        E get();
    }
    interface ListOfNumbers extends List<Number> {}
    interface ListOfAnyNumbers<N extends Number & Cloneable> extends List<N> {}

    interface MapWithNestedGenerics<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {}

    @Test
    public void can_get_raw_type() throws Exception {
        assertThat(new ClassEnhancedToParameterizedType(ListOfAnyNumbers.class).getRawType()).isEqualTo(ListOfAnyNumbers.class);
        assertThat(new ClassEnhancedToParameterizedType(ListOfNumbers.class).getRawType()).isEqualTo(ListOfNumbers.class);
        assertThat(new ClassEnhancedToParameterizedType(MapWithNestedGenerics.class).getRawType()).isEqualTo(MapWithNestedGenerics.class);
    }

    @Test
    public void can_read_actual_type_parameters() throws Exception {
        // interface MapWithNestedGenerics<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {}
        // => [0] K = Comparable<K> & Cloneable
        // => [1] V = Set<Number>



    }
}
