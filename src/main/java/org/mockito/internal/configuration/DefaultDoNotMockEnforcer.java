/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.util.Arrays;
import java.util.Optional;

import org.mockito.DoNotMock;
import org.mockito.plugins.DoNotMockEnforcer;

public class DefaultDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public Optional<String> checkTypeForDoNotMockViolation(Class<?> type) {
        return Arrays.stream(type.getAnnotations()).filter(
            annotation -> annotation.annotationType().getName().endsWith("org.mockito.DoNotMock"))
            .findFirst()
            .map(annotation -> {
                String exceptionMessage = type + " is annotated with @DoNoMock and can't be mocked.";
                if (DoNotMock.class.equals(annotation.annotationType())) {
                    exceptionMessage += " " + type.getAnnotation(DoNotMock.class).value();
                }
                return exceptionMessage;
            });
    }
}
