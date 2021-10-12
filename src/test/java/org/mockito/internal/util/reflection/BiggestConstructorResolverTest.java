/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Date;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.ConstructorResolver.BiggestConstructorResolver;
import org.mockito.junit.MockitoJUnitRunner;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class BiggestConstructorResolverTest {

    @Test
    public void type_should_be_instantiable_if_resolver_provide_matching_types() throws Exception {
        Date date = mock(Date.class);
        Map<Object, Object> map = mock(Map.class);

        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, date, map);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        final Object newInstance = constructor.newInstance(arguments);

        assertThat(newInstance).isNotNull().isInstanceOf(MultipleConstructor.class);
        final MultipleConstructor multipleConstructor = (MultipleConstructor) newInstance;
        assertThat(multipleConstructor.date).isNotNull().isEqualTo(date);
        assertThat(multipleConstructor.map).isNotNull().isEqualTo(map);
    }

    @Test
    public void type_should_be_instantiable_with_null_if_an_argument_do_not_match_wanted_type()
            throws Exception {
        Date date = mock(Date.class);
        Set<?> wrongArg = mock(Set.class);

        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, date, wrongArg);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        final Object newInstance = constructor.newInstance(arguments);

        assertThat(newInstance).isNotNull().isInstanceOf(MultipleConstructor.class);
        final MultipleConstructor multipleConstructor = (MultipleConstructor) newInstance;
        assertThat(multipleConstructor.date).isNotNull().isEqualTo(date);
        assertThat(multipleConstructor.map).isNull();
    }

    @Test
    public void type_should_be_instantiable_with_vararg_constructor() throws Exception {
        Date[] vararg = new Date[] {};
        String string = "";

        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(VarargConstructor.class, string, vararg);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        final Object newInstance = constructor.newInstance(arguments);

        assertThat(newInstance).isNotNull().isInstanceOf(VarargConstructor.class);
        final VarargConstructor varargConstructor = (VarargConstructor) newInstance;
        assertThat(varargConstructor.whatever).isNotNull().isEqualTo(string);
        assertThat(varargConstructor.dates).isNotNull().isEqualTo(vararg);
    }

    @Test
    public void type_should_be_instantiable_with_wrapper_constructor() throws Exception {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);

        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(WrapperConstructor.class, hashSet, hashMap);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        final Object newInstance = constructor.newInstance(arguments);

        assertThat(newInstance).isNotNull().isInstanceOf(WrapperConstructor.class);
        final WrapperConstructor wrapperConstructor = (WrapperConstructor) newInstance;
        assertThat(wrapperConstructor.set).isNotNull().isEqualTo(hashSet);
        assertThat(wrapperConstructor.map).isNotNull().isEqualTo(hashMap);
        assertThat(wrapperConstructor.bool).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_fail_to_instantiate_type_with_primitive_constructor() throws Exception {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);

        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(PrimitiveConstructor.class, hashSet, hashMap);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        constructor.newInstance(arguments);
    }

    @Test
    public void is_resolvable_should_return_true_if_type_has_single_parameterized_constructor() {
        Date date = mock(Date.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(OneConstructor.class, date);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_return_true_if_type_has_parameterized_constructor() {
        final Date date = mock(Date.class);
        final HashMap<Object, Object> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, date, hashMap);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_return_true_with_vararg_constructor() {
        Date[] vararg = new Date[] {};
        String string = "";
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(VarargConstructor.class, vararg, string);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_return_true_when_match_is_not_possible_on_given_type() {
        final HashMap<Object, Object> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, hashMap);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_return_true_when_types_are_wrappers() {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(WrapperConstructor.class, hashSet, hashMap);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_return_true_when_types_are_primitives() {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(PrimitiveConstructor.class, hashSet, hashMap);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_fail_if_no_parameterized_constructor_found() {
        try {
            buildBiggestConstructorResolver(NoArgConstructor.class).isResolvable();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("no parameterized constructor")
                    .contains("NoArgConstructor");
        }
    }

    @Test
    public void is_resolvable_should_fail_if_type_has_default_constructor() {
        try {
            buildBiggestConstructorResolver(DefaultConstructor.class).isResolvable();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("no parameterized constructor")
                    .contains("DefaultConstructor");
        }
    }

    @Test
    public void resolve_constructor_should_return_single_parameterized_constructor() {
        Date date = mock(Date.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(OneConstructor.class, date);

        final Constructor<?> constructor = resolver.resolveConstructor();
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterTypes()).isNotNull().hasSize(1);
    }

    @Test
    public void resolve_constructor_should_return_biggest_constructor() {
        final Date date = mock(Date.class);
        final HashMap<Object, Object> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, date, hashMap);

        final Constructor<?> constructor = resolver.resolveConstructor();
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterTypes()).isNotNull().hasSize(2);
    }

    @Test
    public void resolve_constructor_should_return_vararg_constructor() {
        Date[] vararg = new Date[] {};
        String string = "";
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(VarargConstructor.class, vararg, string);

        final Constructor<?> constructor = resolver.resolveConstructor();
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterTypes()).isNotNull().hasSize(2);
    }

    @Test
    public void resolve_constructor_should_fail_if_no_parameterized_constructor_found() {
        try {
            buildBiggestConstructorResolver(NoArgConstructor.class).resolveConstructor();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("no parameterized constructor")
                    .contains("NoArgConstructor");
        }
    }

    @Test
    public void resolve_constructor_should_fail_if_type_has_default_constructor() {
        try {
            buildBiggestConstructorResolver(DefaultConstructor.class).resolveConstructor();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("no parameterized constructor")
                    .contains("DefaultConstructor");
        }
    }

    @Test
    public void resolve_arguments_should_return_one_matching_object() {
        Date date = mock(Date.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(OneConstructor.class, date);

        assertThat(resolver.resolveArguments()).isNotNull().hasSize(1).containsExactly(date);
    }

    @Test
    public void resolve_arguments_should_return_object_matching_given_types() {
        final Date date = mock(Date.class);
        final HashMap<Object, Object> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, hashMap, date);

        assertThat(resolver.resolveArguments())
                .isNotNull()
                .hasSize(2)
                .containsExactly(date, hashMap);
    }

    @Test
    public void resolve_arguments_should_return_vararg_matching_given_types() {
        Date[] vararg = new Date[] {};
        String string = "";
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(VarargConstructor.class, vararg, string);

        assertThat(resolver.resolveArguments())
                .isNotNull()
                .hasSize(2)
                .containsExactly(string, vararg);
    }

    @Test
    public void resolve_arguments_should_return_null_when_match_is_not_possible_on_given_type() {
        final HashMap<Object, Object> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(MultipleConstructor.class, hashMap);

        assertThat(resolver.resolveArguments())
                .isNotNull()
                .hasSize(2)
                .containsExactly(null, hashMap);
    }

    @Test
    public void resolve_arguments_should_return_null_when_types_are_wrappers() {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(WrapperConstructor.class, hashSet, hashMap);

        assertThat(resolver.resolveArguments())
                .isNotNull()
                .hasSize(3)
                .containsExactly(hashSet, hashMap, null);
    }

    @Test
    public void resolve_arguments_should_return_null_when_types_are_primitives() {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);
        final BiggestConstructorResolver resolver =
                buildBiggestConstructorResolver(PrimitiveConstructor.class, hashSet, hashMap);

        assertThat(resolver.resolveArguments())
                .isNotNull()
                .hasSize(3)
                .containsExactly(hashSet, hashMap, null);
    }

    protected BiggestConstructorResolver buildBiggestConstructorResolver(
            Class<?> type, Object... mocks) {
        return new BiggestConstructorResolver(type, new HashSet<>(Arrays.asList(mocks)));
    }

    private static class NoArgConstructor {
        NoArgConstructor() {}
    }

    public static class DefaultConstructor {}

    private static class OneConstructor {
        public OneConstructor(Date date) {}
    }

    static class MultipleConstructor extends OneConstructor {
        Date date;
        Map<Object, Object> map;

        public MultipleConstructor(Date date) {
            this(date, null);
        }

        public MultipleConstructor(Date date, Map<Object, Object> map) {
            super(date);
            this.date = date;
            this.map = map;
        }

        public MultipleConstructor(Date date, boolean nonMockable) {
            super(date);
            this.date = date;
        }
    }

    private static class VarargConstructor {
        String whatever;
        Date[] dates;

        VarargConstructor(String whatever, Date... dates) {
            this.whatever = whatever;
            this.dates = dates;
        }
    }

    static class WrapperConstructor {
        Set<Object> set;
        Map<Object, Object> map;
        Boolean bool;

        WrapperConstructor(Set<Object> set, Map<Object, Object> map, Boolean bool) {
            this.set = set;
            this.map = map;
            this.bool = bool;
        }
    }

    static class PrimitiveConstructor {
        Set<Object> set;
        Map<Object, Object> map;
        boolean bool;

        PrimitiveConstructor(Set<Object> set, Map<Object, Object> map, boolean bool) {
            this.set = set;
            this.map = map;
            this.bool = bool;
        }
    }
}
