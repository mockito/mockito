/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.mockito.Mock;
import org.mockito.internal.configuration.MockAnnotationProcessor;

import java.lang.reflect.Parameter;

public class MockParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(
        final ParameterContext parameterContext,
        final ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        return parameterContext.isAnnotated(Mock.class);
    }

    @Override
    public Object resolveParameter(
        final ParameterContext parameterContext,
        final ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        final Mock annotation = parameterContext.findAnnotation(Mock.class).get();
        return MockAnnotationProcessor.processAnnotationForMock(annotation, parameter.getType(), parameter.getName());

    }
}
