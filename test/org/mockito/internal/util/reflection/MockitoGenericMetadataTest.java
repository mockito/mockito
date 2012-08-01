/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.internal.util.reflection.MockitoGenericMetadata.from;

@SuppressWarnings("unused")
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
        List<? super Integer> returning_wildcard_with_class_lower_bound();
        List<? super K> returning_wildcard_with_typeVar_lower_bound();
        List<? extends K> returning_wildcard_with_typeVar_upper_bound();
        K returningK();
        <O extends K> List<O> paramType_with_type_params();
        <S, T extends S> T two_type_params();
        <O extends K> O typeVar_with_type_params();
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
    public void typeVariable_return_type_of____get____resolved_to_Set_and_type_argument_to_Number() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("get", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Set.class);
        assertThat(genericMetadata.actualTypeArguments().values()).contains(Number.class);
    }

    @Test
    public void bounded_typeVariable_return_type_of____returningK____resolved_to_Comparable_and_with_BoundedType() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returningK", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Comparable.class);
        MockitoGenericMetadata extraInterface_0 = from(genericMetadata.extraInterfaces().get(0));
        assertThat(extraInterface_0.rawType()).isEqualTo(Cloneable.class);
    }

    @Test
    public void fixed_ParamType_return_type_of____remove____resolved_to_Set_and_type_argument_to_Number() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("remove", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Set.class);
        assertThat(genericMetadata.actualTypeArguments().values()).contains(Number.class);
    }

    @Test
    public void paramType_return_type_of____values____resolved_to_Collection_and_type_argument_to_Parameterized_Set() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("values", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Collection.class);
        MockitoGenericMetadata fromTypeVariableE = from(typeVariableValue(genericMetadata.actualTypeArguments(), "E"));
        assertThat(fromTypeVariableE.rawType()).isEqualTo(Set.class);
        assertThat(fromTypeVariableE.actualTypeArguments().values()).contains(Number.class);
    }

    @Test
    public void paramType_with_type_parameters_return_type_of____paramType_with_type_params____resolved_to_Collection_and_type_argument_to_Parameterized_Set() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("paramType_with_type_params", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(List.class);
        Type firstBoundOfE = ((MockitoGenericMetadata.TypeVarBoundedType) typeVariableValue(genericMetadata.actualTypeArguments(), "E")).firstBound();
        assertThat(from(firstBoundOfE).rawType()).isEqualTo(Comparable.class);
    }

    @Test
    public void typeVariable_with_type_parameters_return_type_of____typeVar_with_type_params____resolved_K_hence_to_Comparable_and_with_BoundedType() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("typeVar_with_type_params", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Comparable.class);
        MockitoGenericMetadata extraInterface_0 = from(genericMetadata.extraInterfaces().get(0));
        assertThat(extraInterface_0.rawType()).isEqualTo(Cloneable.class);
    }

    @Test
    public void class_return_type_of____append____resolved_to_StringBuilder_and_type_arguments() throws Exception {
        MockitoGenericMetadata genericMetadata = from(StringBuilder.class).resolveGenericReturnType(firstNamedMethod("append", StringBuilder.class));

        assertThat(genericMetadata.rawType()).isEqualTo(StringBuilder.class);
        assertThat(genericMetadata.actualTypeArguments()).isEmpty();
    }



    @Test
    public void paramType_with_wildcard_return_type_of____returning_wildcard_with_class_lower_bound____resolved_to_List_and_type_argument_to_Integer() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returning_wildcard_with_class_lower_bound", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(List.class);
        MockitoGenericMetadata.BoundedType boundedType = (MockitoGenericMetadata.BoundedType) typeVariableValue(genericMetadata.actualTypeArguments(), "E");
        assertThat(boundedType.firstBound()).isEqualTo(Integer.class);
        assertThat(boundedType.interfaceBounds()).isEmpty();
    }

    @Test
    public void paramType_with_wildcard_return_type_of____returning_wildcard_with_typeVar_lower_bound____resolved_to_List_and_type_argument_to_Integer() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returning_wildcard_with_typeVar_lower_bound", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(List.class);
        MockitoGenericMetadata.BoundedType boundedType = (MockitoGenericMetadata.BoundedType) typeVariableValue(genericMetadata.actualTypeArguments(), "E");

        assertThat(from(boundedType.firstBound()).rawType()).isEqualTo(Comparable.class);
        assertThat(boundedType.interfaceBounds()).contains(Cloneable.class);    }

    @Test
    public void paramType_with_wildcard_return_type_of____returning_wildcard_with_typeVar_upper_bound____resolved_to_List_and_type_argument_to_Integer() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returning_wildcard_with_typeVar_upper_bound", GenericsNest.class));

        assertThat(genericMetadata.rawType()).isEqualTo(List.class);
        MockitoGenericMetadata.BoundedType boundedType = (MockitoGenericMetadata.BoundedType) typeVariableValue(genericMetadata.actualTypeArguments(), "E");

        assertThat(from(boundedType.firstBound()).rawType()).isEqualTo(Comparable.class);
        assertThat(boundedType.interfaceBounds()).contains(Cloneable.class);
    }




    @Test(expected = MockitoException.class)
    public void toMock_propagate_MockitoException_if_type_unmockable() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returning_wildcard_with_class_lower_bound", GenericsNest.class));

        MockitoGenericMetadata.BoundedType boundedType = (MockitoGenericMetadata.BoundedType) typeVariableValue(genericMetadata.actualTypeArguments(), "E");

        from(boundedType.firstBound()).toMock(RETURNS_DEFAULTS);
    }

    @Test
    public void toMock_returns_mock_of_correct_type() throws Exception {
        MockitoGenericMetadata genericMetadata = from(GenericsNest.class).resolveGenericReturnType(firstNamedMethod("returningK", GenericsNest.class));

        assertThat(genericMetadata.toMock(RETURNS_DEFAULTS)).isInstanceOf(Comparable.class);
        assertThat(genericMetadata.toMock(RETURNS_DEFAULTS)).isInstanceOf(Cloneable.class);
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
