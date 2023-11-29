/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter.resolver;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.List;
import java.util.Optional;

public class CompositeParameterResolver implements ParameterResolver {

    private final List<ParameterResolver> delegates;

    public CompositeParameterResolver(final ParameterResolver... delegates) {
        this.delegates = List.of(delegates);
    }

    @Override
    public boolean supportsParameter(
            ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return findDelegate(parameterContext, extensionContext).isPresent();
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Object resolveParameter(
            ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final ParameterResolver delegate = findDelegate(parameterContext, extensionContext).get();
        return delegate.resolveParameter(parameterContext, extensionContext);
    }

    private Optional<ParameterResolver> findDelegate(
            final ParameterContext parameterContext, final ExtensionContext extensionContext) {
        return delegates.stream()
                .filter(delegate -> delegate.supportsParameter(parameterContext, extensionContext))
                .findFirst();
    }
}
