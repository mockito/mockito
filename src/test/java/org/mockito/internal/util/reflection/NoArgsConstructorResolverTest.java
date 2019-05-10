/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;


import java.lang.reflect.Constructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.ConstructorResolver.NoArgsConstructorResolver;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class NoArgsConstructorResolverTest {

    @Test
    public void type_should_be_instantiable_if_it_has_no_args_contructor() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(NoArgConstructor.class);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        final Object newInstance = constructor.newInstance(arguments);

        assertThat(newInstance).isNotNull().isInstanceOf(NoArgConstructor.class);
    }

    @Test
    public void type_should_be_instantiable_if_it_has_default_contructor() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(DefaultConstructor.class);
        final Constructor<?> constructor = resolver.resolveConstructor();
        final Object[] arguments = resolver.resolveArguments();
        final Object newInstance = constructor.newInstance(arguments);

        assertThat(newInstance).isNotNull().isInstanceOf(DefaultConstructor.class);
    }

    @Test
    public void is_resolvable_should_return_true_if_type_has_no_args_contructor() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(NoArgConstructor.class);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_return_true_if_type_has_default_contructor() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(DefaultConstructor.class);

        assertThat(resolver.isResolvable()).isTrue();
    }

    @Test
    public void is_resolvable_should_fail_if_no_no_args_constructor_found() throws Exception {
        try {
            buildNoArgsConstructorResolver(OneConstructor.class).isResolvable();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage()).contains("no default constructor").contains("OneConstructor");
        }
    }

    @Test
    public void is_resolvable_should_fail_with_vararg_constructor() throws Exception {
        try {
            buildNoArgsConstructorResolver(VarargConstructor.class).isResolvable();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage()).contains("no default constructor").contains("VarargConstructor");
        }
    }

    @Test
    public void resolve_constructor_should_return_no_args_constructor() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(NoArgConstructor.class);

        final Constructor<?> constructor = resolver.resolveConstructor();
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterTypes()).isNotNull().isEmpty();
    }

    @Test
    public void resolve_constructor_should_return_default_constructor() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(DefaultConstructor.class);

        final Constructor<?> constructor = resolver.resolveConstructor();
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterTypes()).isNotNull().isEmpty();
    }

    @Test
    public void resolve_constructor_should_fail_if_no_no_args_constructor_found() throws Exception {
        try {
            buildNoArgsConstructorResolver(OneConstructor.class).resolveConstructor();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage()).contains("no default constructor").contains("OneConstructor");
        }
    }

    @Test
    public void resolve_constructor_should_fail_with_vararg_constructor() throws Exception {
        try {
            buildNoArgsConstructorResolver(VarargConstructor.class).resolveConstructor();
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage()).contains("no default constructor").contains("VarargConstructor");
        }
    }

    @Test
    public void resolve_arguments_should_return_empty_object_array() throws Exception {
        final NoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(NoArgConstructor.class);

        assertThat(resolver.resolveArguments()).isNotNull().isEmpty();
    }

    protected NoArgsConstructorResolver buildNoArgsConstructorResolver(Class<?> type) {
        return new NoArgsConstructorResolver(type);
    }

    private static class NoArgConstructor {
        NoArgConstructor() {
        }
    }

    public static class DefaultConstructor {
    }

    static class OneConstructor {
        public OneConstructor(String whatever) {
        }
    }

    static class VarargConstructor {
        VarargConstructor(String... whatever) {
        }
    }

}
