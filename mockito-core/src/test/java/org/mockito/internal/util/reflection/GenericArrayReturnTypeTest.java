/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;

public class GenericArrayReturnTypeTest {

    @Test
    public void toArrayTypedDoesNotWork() throws Exception {
        Container container = mock(Container.class, Answers.RETURNS_DEEP_STUBS);
        container.getInnerContainer().getTheProblem().toArray(new String[] {});
    }

    @Test
    public void raw_type_of_type_variable_bound_to_array_is_resolved() {
        GenericMetadataSupport genericMetadata =
                GenericMetadataSupport.inferFrom(HttpDaServiceImpl.class)
                        .resolveGenericReturnType(namedMethod("doSmth", HttpDaService.class));

        assertThat(genericMetadata.rawType()).isEqualTo(Object[].class);
    }

    @Test
    public void stubbing_method_with_generic_array_return_type_is_allowed() {
        HttpDaServiceImpl service = mock(HttpDaServiceImpl.class);
        Object[] expected = new Object[4];

        when(service.doSmth(any(), any())).thenReturn(expected);

        assertThat(service.doSmth(new Object(), Object[].class)).isSameAs(expected);
    }

    private Method namedMethod(String methodName, Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && !method.isBridge()) {
                return method;
            }
        }
        throw new IllegalStateException(
                "The method : '" + methodName + "' does not exist in '" + clazz + "'");
    }

    class Container {

        private InnerContainer innerContainer;

        public InnerContainer getInnerContainer() {
            return innerContainer;
        }
    }

    class InnerContainer {

        private Set<String> theProblem;

        public Set<String> getTheProblem() {
            return theProblem;
        }
    }

    public abstract static class DaService<T, S> {
        public S doSmth(T o, Class<S> clazz) {
            return null;
        }
    }

    public abstract static class HttpDaService<T, S> extends DaService<T, S[]> {}

    public static class HttpDaServiceImpl extends HttpDaService<Object, Object> {}
}
