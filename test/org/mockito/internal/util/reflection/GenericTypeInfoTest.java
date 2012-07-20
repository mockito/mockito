package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.internal.util.reflection.GenericTypeInfo.on;

public class GenericTypeInfoTest {

    interface UpperBoundedTypeWithClass<E extends Number & Comparable<E>> {
        E get();
    }
    interface UpperBoundedTypeWithInterfaces<E extends Comparable<E> & Cloneable> {
        E get();
    }
    interface ListOfNumbers extends List<Number> {}
    interface ListOfAnyNumbers<N extends Number & Cloneable> extends List<N> {}

    @Test
    public void can_get_raw_type_on_simple_return_type() throws Exception {
        assertThat(on(List.class).methodName("iterator").genericReturnTypeInfo().asRawType()).isEqualTo(Iterator.class);
        assertThat(on(List.class).methodName("size").genericReturnTypeInfo().asRawType()).isEqualTo(int.class);
        assertThat(on(Number.class).methodName("toString").genericReturnTypeInfo().asRawType()).isEqualTo(String.class);
    }

    @Test
    public void can_get_raw_type_of_parameterized_return_type() throws Exception {
        assertThat(on(List.class).methodName("get").genericReturnTypeInfo().asRawType()).isEqualTo(Object.class);
    }

    @Test
    public void can_get_raw_type_of_parameterized_return_type_with_upper_bounded_type_variable() throws Exception {
        assertThat(on(UpperBoundedTypeWithInterfaces.class).methodName("get").genericReturnTypeInfo().asRawType()).isEqualTo(Comparable.class);
        assertThat(on(UpperBoundedTypeWithClass.class).methodName("get").genericReturnTypeInfo().asRawType()).isEqualTo(Number.class);
    }

    @Test
    public void can_get_raw_type_of_parameterized_return_type_with_fixed_type_argument() throws Exception {
        assertThat(on(ListOfNumbers.class).methodName("get").genericReturnTypeInfo().asRawType()).isEqualTo(Number.class);
    }

    @Test
    public void can_get_raw_type_of_parameterized_return_type_with_bounded_type_parameter() throws Exception {
        assertThat(on(ListOfAnyNumbers.class).methodName("get").genericReturnTypeInfo().asRawType()).isEqualTo(Number.class);
    }

    interface MapWithNestedGenerics<K extends Comparable<K> & Cloneable & Set<Number>> extends Map<K, Set<Number>> {}


    @Test
    public void test() throws Exception {
        GenericTypeInfo entrySetGenericTypeInfo = on(MapWithNestedGenerics.class).methodName("entrySet").genericReturnTypeInfo();
        System.out.println(entrySetGenericTypeInfo); // Set<Entry<K extends Comparable<K>, Set<Number>>
//        entrySetGenericTypeInfo.asRawType(); // Set
//        entrySetGenericTypeInfo.getTypeVariables(); // { 'K' : 'K extends Comparable<K>' , 'V' : 'Set<Number' }
        System.out.println("======================================================");

        GenericTypeInfo iteratorGenericTypeInfo = on(entrySetGenericTypeInfo).methodName("iterator").genericReturnTypeInfo();
        System.out.println(iteratorGenericTypeInfo);
    }

}
