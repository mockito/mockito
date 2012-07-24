package org.mockito.internal.util.reflection;

import org.junit.Ignore;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.reflection.MockitoGenericMetadata.from;

public class MockitoGenericMetadataTest {

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
        List<? super Integer> returningWildcard();
        K returningK();
        <O extends K> O paramTypeWithTypeParams();
    }

    @Test
    public void can_get_raw_type_from_Class() throws Exception {
        assertThat(from(ListOfAnyNumbers.class).rawType()).isEqualTo(ListOfAnyNumbers.class);
        assertThat(from(ListOfNumbers.class).rawType()).isEqualTo(ListOfNumbers.class);
        assertThat(from(GenericsNest.class).rawType()).isEqualTo(GenericsNest.class);
    }


    @Test
    public void can_get_raw_type_from_ParameterizedType() throws Exception {
        assertThat(from(ListOfAnyNumbers.class.getGenericInterfaces()[0]).rawType()).isEqualTo(List.class);
        assertThat(from(ListOfNumbers.class.getGenericInterfaces()[0]).rawType()).isEqualTo(List.class);
        assertThat(from(GenericsNest.class.getGenericInterfaces()[0]).rawType()).isEqualTo(Map.class);
    }

    @Test
    @Ignore("not the right unit test")
    public void returning_deep_stubs() throws Exception {
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
    public void can_get_type_variables_from_Class() throws Exception {
        assertThat(from(GenericsNest.class).actualTypeArguments().keySet()).hasSize(1).onProperty("name").contains("K");
        assertThat(from(ListOfNumbers.class).actualTypeArguments().keySet()).isEmpty();
        assertThat(from(ListOfAnyNumbers.class).actualTypeArguments().keySet()).hasSize(1).onProperty("name").contains("N");
        assertThat(from(Map.class).actualTypeArguments().keySet()).hasSize(2).onProperty("name").contains("K", "V");
        assertThat(from(Serializable.class).actualTypeArguments().keySet()).isEmpty();
    }

    @Test
    public void can_get_type_variables_from_ParameterizedType() throws Exception {
        assertThat(from(GenericsNest.class.getGenericInterfaces()[0]).actualTypeArguments().keySet()).hasSize(2).onProperty("name").contains("K", "V");
        assertThat(from(ListOfAnyNumbers.class.getGenericInterfaces()[0]).actualTypeArguments().keySet()).hasSize(1).onProperty("name").contains("E");
        assertThat(from(Integer.class.getGenericInterfaces()[0]).actualTypeArguments().keySet()).hasSize(1).onProperty("name").contains("T");
        assertThat(from(StringBuilder.class.getGenericInterfaces()[0]).actualTypeArguments().keySet()).isEmpty();
    }

    @Test
    public void typeVariable_return_type_of___get___resolved_to_Set_and_type_argument_to_Number() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("get", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Set.class);
        assertThat(genericMetadata.actualTypeArguments().values()).contains(Number.class);
    }

    @Test
    public void typeVariable_return_type_of___returningK___resolved_to_Comparable_and_with_BoundedType() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returningK", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Comparable.class);
        BoundedType boundedType = (BoundedType) typeVariableValue(genericMetadata.actualTypeArguments(), "T");
        assertThat(boundedType.firstBound()); // use MockitoGenericMetadata ?
        fail("API design to do");
    }

    @Test
    public void fixed_ParamType_return_type_of___remove___resolved_to_Set_and_type_argument_to_Number() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("remove", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Set.class);
        assertThat(genericMetadata.actualTypeArguments().values()).contains(Number.class);
    }

    @Test
    public void paramType_return_type_of___values___resolved_to_Collection_and_type_argument_to_Parameterized_Set() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("values", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Collection.class);
        MockitoGenericMetadata fromTypeVariableE = from(typeVariableValue(genericMetadata.actualTypeArguments(), "E"));
        assertThat(fromTypeVariableE.rawType()).isEqualTo(Set.class);
        assertThat(fromTypeVariableE.actualTypeArguments().values()).contains(Number.class);
    }

    @Test
    public void paramType_return_type_of___returningWildcard___resolved_to_Collection_and_type_argument_to_Parameterized_Set() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returningWildcard", GenericsNest.class));

        fail("TODO");
    }

    @Test
    public void paramType_return_type_of___paramTypeWithTypeParams___resolved_to_Collection_and_type_argument_to_Parameterized_Set() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("paramTypeWithTypeParams", GenericsNest.class));

        fail("TODO");
    }


    @Test
    public void class_return_type_of___append___resolved_to_StringBuilder_and_type_arguments() throws Exception {
        MockitoGenericMetadata genericMetadata = from(StringBuilder.class).resolveGenericReturnType(firstNamedMethod("append", StringBuilder.class));

        assertThat(genericMetadata.rawType()).isEqualTo(StringBuilder.class);
        assertThat(genericMetadata.actualTypeArguments()).isEmpty();
    }


    private Type typeVariableValue(Map<TypeVariable, Type> typeVariables, String typeVariableName) {
        for (Map.Entry<TypeVariable, Type> typeVariableTypeEntry : typeVariables.entrySet()) {
            if (typeVariableTypeEntry.getKey().getName().equals(typeVariableName)) {
                return typeVariableTypeEntry.getValue();
            }
        }

        fail("'" + typeVariableName + "' was not found in " + typeVariables);
        return null; // unreachable
    }

    private Method firstNamedMethod(String methodName, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().contains(methodName)) {
                return method;
            }
        }
        throw new IllegalStateException("The method : '" + methodName + "' do not exist in '" + clazz.getSimpleName() + "'");
    }


}
