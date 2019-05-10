/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.ConstructorResolver.LenientNoArgsConstructorResolver;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LenientNoArgsConstructorResolverTest extends NoArgsConstructorResolverTest {

    @Override
    public void is_resolvable_should_fail_if_no_no_args_constructor_found() throws Exception {
        // This test is not relevant for LenientNoArgsConstructorResolver
    }

    @Test
    public void is_resolvable_should_return_false_if_no_no_args_constructor_found() throws Exception {
        final LenientNoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(OneConstructor.class);

        assertThat(resolver.isResolvable()).isFalse();
    }

    @Override
    public void is_resolvable_should_fail_with_vararg_constructor() throws Exception {
        // This test is not relevant for LenientNoArgsConstructorResolver
    }

    @Test
    public void is_resolvable_should_return_false_with_vararg_constructor() throws Exception {
        final LenientNoArgsConstructorResolver resolver = buildNoArgsConstructorResolver(VarargConstructor.class);

        assertThat(resolver.isResolvable()).isFalse();
    }

    @Override
    protected LenientNoArgsConstructorResolver buildNoArgsConstructorResolver(Class<?> type) {
        return new LenientNoArgsConstructorResolver(type);
    }

}
