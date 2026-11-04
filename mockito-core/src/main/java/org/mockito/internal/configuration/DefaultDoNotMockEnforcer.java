/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;

import org.mockito.DoNotMock;
import org.mockito.plugins.DoNotMockEnforcer;

public class DefaultDoNotMockEnforcer implements DoNotMockEnforcer {

    @Override
    public String checkTypeForDoNotMockViolation(Class<?> type) {
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation.annotationType().getName().endsWith("org.mockito.DoNotMock")) {
                if (DoNotMock.class.equals(annotation.annotationType())) {
                    return type
                            + " is annotated with @org.mockito.DoNotMock and can't be mocked. "
                            + ((DoNotMock) annotation).reason();
                }

                return type + " is annotated with " + annotation + " and can't be mocked.";
            }
        }

        return null;
    }
}
