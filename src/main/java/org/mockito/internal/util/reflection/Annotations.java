/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;

public class Annotations {

    public static void assertNoIncompatibleAnnotations(Class<? extends Annotation> annotation,
                                                       Field field,
                                                       Class<? extends Annotation>... undesiredAnnotations) {
        for (Class<? extends Annotation> undesired : undesiredAnnotations) {
            if (field.isAnnotationPresent(undesired)) {
                throw unsupportedCombinationOfAnnotations(field,
                                                          annotation.getSimpleName(),
                                                          undesired.getSimpleName());
            }
        }
    }
}
