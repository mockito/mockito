/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * Stubbing a method whose return type is a type variable that a subclass binds to an array,
 * such as {@code class HttpDaService<T, S> extends DaService<T, S[]>}, used to fail with
 * {@code MockitoException: Raw extraction not supported for : 'S[]'}.
 *
 * @see <a href="https://github.com/mockito/mockito/issues/3690">issue 3690</a>
 */
public class GenericArrayReturnTypeMockingTest {

    abstract static class DaService<T, S> {
        S doSmth(T o, Class<S> clazz) {
            return null;
        }
    }

    abstract static class HttpDaService<T, S> extends DaService<T, S[]> {}

    static class HttpDaServiceImpl extends HttpDaService<Object, Object> {}

    @Test
    public void should_stub_method_returning_array_type_variable() {
        HttpDaServiceImpl daService = mock(HttpDaServiceImpl.class);

        when(daService.doSmth(any(), any())).thenReturn(new Object[4]);

        assertThat(daService.doSmth(new Object(), Object[].class)).hasSize(4);
    }

    @Test
    public void should_stub_method_returning_nested_array_type_variable() {
        NestedHttpDaServiceImpl daService = mock(NestedHttpDaServiceImpl.class);
        String[][] expected = {{"a", "b"}, {"c"}};

        when(daService.doSmth("id")).thenReturn(expected);

        assertThat(daService.doSmth("id")).isSameAs(expected);
    }

    abstract static class NestedDaService<T, S> {
        S doSmth(T o) {
            return null;
        }
    }

    abstract static class NestedHttpDaService<T, S> extends NestedDaService<T, S[]> {}

    static class NestedHttpDaServiceImpl extends NestedHttpDaService<String, String[]> {}
}
