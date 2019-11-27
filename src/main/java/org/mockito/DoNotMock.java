/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation representing a type that should not be mocked.
 * <p>When marking a type {@code @DoNotMock}, you should always point to alternative testing
 * solutions such as standard fakes or other testing utilities.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface DoNotMock {
    /**
     * The reason why the annotated type should not be mocked.
     *
     * <p>This should suggest alternative APIs to use for testing objects of this type.
     */
    String value() default "Create a real instance instead";
}
