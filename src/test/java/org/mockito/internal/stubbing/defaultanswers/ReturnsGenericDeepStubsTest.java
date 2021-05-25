/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.junit.Test;
import org.mockitousage.examples.use.Article;

@SuppressWarnings("unused")
public class ReturnsGenericDeepStubsTest {
    interface ListOfInteger extends List<Integer> {}

    interface AnotherListOfInteger extends ListOfInteger {}

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
    public void generic_deep_mock_frenzy__look_at_these_chained_calls() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        Set<? extends Map.Entry<? extends Cloneable, Set<Number>>> entries = mock.entrySet();
        Iterator<? extends Map.Entry<? extends Cloneable, Set<Number>>> entriesIterator =
                mock.entrySet().iterator();
        Map.Entry<? extends Cloneable, Set<Number>> nextEntry = mock.entrySet().iterator().next();

        Cloneable cloneableKey = mock.entrySet().iterator().next().getKey();
        Comparable<?> comparableKey = mock.entrySet().iterator().next().getKey();

        Set<Number> value = mock.entrySet().iterator().next().getValue();
        Iterator<Number> numbersIterator = mock.entrySet().iterator().next().getValue().iterator();
        Number number = mock.entrySet().iterator().next().getValue().iterator().next();
    }

    @Test
    public void
            can_create_mock_from_multiple_type_variable_bounds_when_return_type_of_parameterized_method_is_a_parameterizedType_that_is_referencing_a_typeVar_on_class() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        Cloneable
                cloneable_bound_that_is_declared_on_typevar_K_in_the_class_which_is_referenced_by_typevar_O_declared_on_the_method =
                        mock.paramTypeWithTypeParams().get(0);
        Comparable<?>
                comparable_bound_that_is_declared_on_typevar_K_in_the_class_which_is_referenced_by_typevar_O_declared_on_the_method =
                        mock.paramTypeWithTypeParams().get(0);
    }

    @Test
    public void
            can_create_mock_from_multiple_type_variable_bounds_when_method_return_type_is_referencing_a_typeVar_on_class() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        Cloneable cloneable_bound_of_typevar_K = mock.returningK();
        Comparable<?> comparable_bound_of_typevar_K = mock.returningK();
    }

    @Test
    public void
            can_create_mock_from_multiple_type_variable_bounds_when_return_type_of_parameterized_method_is_a_typeVar_that_is_referencing_a_typeVar_on_class() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        Cloneable cloneable_bound_of_typevar_K_referenced_by_typevar_O =
                (Cloneable) mock.typeVarWithTypeParams();
        Comparable<?> comparable_bound_of_typevar_K_referenced_by_typevar_O =
                (Comparable<?>) mock.typeVarWithTypeParams();
    }

    @Test
    public void can_create_mock_from_return_types_declared_with_a_bounded_wildcard() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        List<? super Integer> objects = mock.returningWildcard();
        Number type_that_is_the_upper_bound_of_the_wildcard =
                (Number) mock.returningWildcard().get(45);
        type_that_is_the_upper_bound_of_the_wildcard.floatValue();
    }

    @Test
    public void can_still_work_with_raw_type_in_the_return_type() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        Number the_raw_type_that_should_be_returned = mock.returnsNormalType();
        the_raw_type_that_should_be_returned.floatValue();
    }

    @Test
    public void will_return_default_value_on_non_mockable_nested_generic() {
        GenericsNest<?> genericsNest = mock(GenericsNest.class, RETURNS_DEEP_STUBS);
        ListOfInteger listOfInteger = mock(ListOfInteger.class, RETURNS_DEEP_STUBS);
        AnotherListOfInteger anotherListOfInteger =
                mock(AnotherListOfInteger.class, RETURNS_DEEP_STUBS);

        assertThat(genericsNest.returningNonMockableNestedGeneric().keySet().iterator().next())
                .isNull();
        assertThat(listOfInteger.get(25)).isEqualTo(0);
        assertThat(anotherListOfInteger.get(25)).isEqualTo(0);
    }

    @Test(expected = ClassCastException.class)
    public void
            as_expected_fail_with_a_CCE_on_call_site_when_erasure_takes_place_for_example___StringBuilder_is_subject_to_erasure() {
        GenericsNest<?> mock = mock(GenericsNest.class, RETURNS_DEEP_STUBS);

        // following assignment needed to create a ClassCastException on the call site (i.e. : here)
        StringBuilder stringBuilder_assignment_that_should_throw_a_CCE =
                mock.twoTypeParams(new StringBuilder()).append(2).append(3);
    }

    class WithGenerics<T> {
        T execute() {
            throw new IllegalArgumentException();
        }
    }

    class SubClass<S> extends WithGenerics<S> {}

    class UserOfSubClass {
        SubClass<String> generate() {
            return null;
        }
    }

    @Test
    public void can_handle_deep_stubs_with_generics_at_end_of_deep_invocation() {
        UserOfSubClass mock = mock(UserOfSubClass.class, RETURNS_DEEP_STUBS);

        when(mock.generate().execute()).thenReturn("sub");

        assertThat(mock.generate().execute()).isEqualTo("sub");
    }

    public interface TopInterface<T> {
        T generic();
    }

    public interface MiddleInterface<T> extends TopInterface<T> {}

    public class OwningClassWithDeclaredUpperBounds<
            T extends Iterable<Article> & Callable<Article> & Closeable> {
        public abstract class AbstractInner implements MiddleInterface<T> {}
    }

    @Test
    public void
            cannot_handle_deep_stubs_with_generics_declared_upper_bounds_at_end_of_deep_invocation()
                    throws Exception {
        OwningClassWithDeclaredUpperBounds.AbstractInner mock =
                mock(OwningClassWithDeclaredUpperBounds.AbstractInner.class, RETURNS_DEEP_STUBS);

        // It seems that while the syntax used on OwningClassWithDeclaredUpperBounds.AbstractInner
        // appear to be legal, the javac compiler does not follow through
        // hence we need casting, this may also explain why GenericMetadataSupport has trouble to
        // extract matching data as well.

        assertThat(mock.generic())
                .describedAs("mock should implement first bound : 'Iterable'")
                .isInstanceOf(Iterable.class);
        assertThat(((Iterable<Article>) mock.generic()).iterator())
                .describedAs("Iterable returns Iterator")
                .isInstanceOf(Iterator.class);
        assertThat(((Iterable<Article>) mock.generic()).iterator().next())
                .describedAs(
                        "Cannot yet extract Type argument 'Article' so return null instead of a mock "
                                + "of type Object (which would raise CCE on the call-site)")
                .isNull();

        assertThat(mock.generic())
                .describedAs("mock should implement second interface bound : 'Callable'")
                .isInstanceOf(Callable.class);
        assertThat(((Callable<Article>) mock.generic()).call())
                .describedAs(
                        "Cannot yet extract Type argument 'Article' so return null instead of a mock "
                                + "of type Object (which would raise CCE on the call-site)")
                .isNull();

        assertThat(mock.generic())
                .describedAs("mock should implement third interface bound : 'Closeable'")
                .isInstanceOf(Closeable.class);
    }
}
