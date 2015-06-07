/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

@SuppressWarnings("unused")
public class ReturnsGenericDeepStubsTest {
    interface ListOfInteger extends List<Integer> {}

    interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        Set<Number> remove(final Object key); // override with fixed ParameterizedType
        List<? super Number> returningWildcard();
        Map<String, K> returningNonMockableNestedGeneric();
        K returningK();
        <O extends K> List<O> paramTypeWithTypeParams();
        <S extends Appendable, T extends S> T twoTypeParams(final S s);
        <O extends K> O typeVarWithTypeParams();
        Number returnsNormalType();
    }

    @Test
    public void generic_deep_mock_frenzy__look_at_these_chained_calls() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        final Set<? extends Map.Entry<? extends Cloneable, Set<Number>>> entries = mock.entrySet();
        final Iterator<? extends Map.Entry<? extends Cloneable, Set<Number>>> entriesIterator = mock.entrySet().iterator();
        final Map.Entry<? extends Cloneable, Set<Number>> nextEntry = mock.entrySet().iterator().next();

        final Cloneable cloneableKey = mock.entrySet().iterator().next().getKey();
        final Comparable<?> comparableKey = mock.entrySet().iterator().next().getKey();

        final Set<Number> value = mock.entrySet().iterator().next().getValue();
        final Iterator<Number> numbersIterator = mock.entrySet().iterator().next().getValue().iterator();
        final Number number = mock.entrySet().iterator().next().getValue().iterator().next();
    }

    @Test
    public void can_create_mock_from_multiple_type_variable_bounds_when_return_type_of_parameterized_method_is_a_parameterizedtype_that_is_referencing_a_typevar_on_class() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        final Cloneable cloneable_bound_that_is_declared_on_typevar_K_in_the_class_which_is_referenced_by_typevar_O_declared_on_the_method =
                mock.paramTypeWithTypeParams().get(0);
        final Comparable<?> comparable_bound_that_is_declared_on_typevar_K_in_the_class_which_is_referenced_by_typevar_O_declared_on_the_method =
                mock.paramTypeWithTypeParams().get(0);
    }

    @Test
    public void can_create_mock_from_multiple_type_variable_bounds_when_method_return_type_is_referencing_a_typevar_on_class() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        final Cloneable cloneable_bound_of_typevar_K = mock.returningK();
        final Comparable<?> comparable_bound_of_typevar_K = mock.returningK();
    }

    @Test
    public void can_create_mock_from_multiple_type_variable_bounds_when_return_type_of_parameterized_method_is_a_typevar_that_is_referencing_a_typevar_on_class() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        final Cloneable cloneable_bound_of_typevar_K_referenced_by_typevar_O = mock.typeVarWithTypeParams();
        final Comparable<?> comparable_bound_of_typevar_K_referenced_by_typevar_O = mock.typeVarWithTypeParams();
    }

    @Test
    public void can_create_mock_from_return_types_declared_with_a_bounded_wildcard() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        final List<? super Integer> objects = mock.returningWildcard();
        final Number type_that_is_the_upper_bound_of_the_wildcard = (Number) mock.returningWildcard().get(45);
        type_that_is_the_upper_bound_of_the_wildcard.floatValue();
    }

    @Test
    public void can_still_work_with_raw_type_in_the_return_type() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        final Number the_raw_type_that_should_be_returned = mock.returnsNormalType();
        the_raw_type_that_should_be_returned.floatValue();
    }

    @Test
    public void will_return_default_value_on_non_mockable_nested_generic() throws Exception {
        final GenericsNest<?> genericsNest = mock(GenericsNest.class, RETURNS_DEEP_STUBS);
        final ListOfInteger listOfInteger = mock(ListOfInteger.class, RETURNS_DEEP_STUBS);

        assertThat(genericsNest.returningNonMockableNestedGeneric().keySet().iterator().next()).isNull();
        assertThat(listOfInteger.get(25)).isEqualTo(0);
    }

    @Test(expected = ClassCastException.class)
    public void as_expected_fail_with_a_CCE_on_callsite_when_erasure_takes_place_for_example___StringBuilder_is_subject_to_erasure() throws Exception {
        final GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        // following assignment needed to create a ClassCastException on the call site (i.e. : here)
        final StringBuilder stringBuilder_assignment_that_should_throw_a_CCE =
                mock.twoTypeParams(new StringBuilder()).append(2).append(3);
    }
}
