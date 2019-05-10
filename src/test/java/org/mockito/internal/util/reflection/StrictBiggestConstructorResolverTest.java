/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.ConstructorResolver.StrictBiggestConstructorResolver;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class StrictBiggestConstructorResolverTest extends BiggestConstructorResolverTest {

    @Override
    public void is_resolvable_should_return_true_when_match_is_not_possible_on_given_type() throws Exception {
        // This test is not relevant for LenientNoArgsConstructorResolver
    }

    @Test
    public void is_resolvable_should_return_false_when_match_is_not_possible_on_given_type() throws Exception {
        final HashMap hashMap = mock(HashMap.class);
        final StrictBiggestConstructorResolver resolver = buildBiggestConstructorResolver(MultipleConstructor.class, hashMap);

        assertThat(resolver.isResolvable()).isFalse();
    }

    @Override
    public void is_resolvable_should_return_true_when_types_are_wrappers() throws Exception {
        // This test is not relevant for LenientNoArgsConstructorResolver
    }

    @Test
    public void is_resolvable_should_return_false_when_types_are_wrappers() throws Exception {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);
        final StrictBiggestConstructorResolver resolver = buildBiggestConstructorResolver(WrapperConstructor.class, hashSet, hashMap);

        assertThat(resolver.isResolvable()).isFalse();
    }

    @Override
    public void is_resolvable_should_return_true_when_types_are_primitives() throws Exception {
        // This test is not relevant for LenientNoArgsConstructorResolver
    }

    @Test
    public void is_resolvable_should_return_false_when_types_are_primitives() throws Exception {
        final HashSet<Long> hashSet = mock(HashSet.class);
        final HashMap<String, String> hashMap = mock(HashMap.class);
        final StrictBiggestConstructorResolver resolver = buildBiggestConstructorResolver(PrimitiveConstructor.class, hashSet, hashMap);

        assertThat(resolver.isResolvable()).isFalse();
    }

    @Override
    protected StrictBiggestConstructorResolver buildBiggestConstructorResolver(Class<?> type, Object... mocks) {
        return new StrictBiggestConstructorResolver(type, new HashSet<Object>(Arrays.asList(mocks)));
    }

}
