package org.mockito.junit.jupiter.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mock;
import org.mockito.ScopedMock;
import org.mockito.internal.configuration.MockAnnotationProcessor;

import java.lang.reflect.Parameter;
import java.util.Set;

public class MockParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        return parameterContext.isAnnotated(Mock.class);
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();

        return MockAnnotationProcessor.processAnnotationForMock(
            parameterContext.findAnnotation(Mock.class).get(),
            parameter.getType(),
            parameter::getParameterizedType,
            parameter.getName());
    }
}
