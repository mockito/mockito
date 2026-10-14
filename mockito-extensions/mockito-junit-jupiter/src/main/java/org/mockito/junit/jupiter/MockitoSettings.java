/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit.jupiter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.quality.Strictness;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that can configure Mockito as invoked by the {@link MockitoExtension}.
 */
@ExtendWith(MockitoExtension.class)
@Inherited
@Retention(RUNTIME)
public @interface MockitoSettings {

    /**
     * Configure the strictness used in this test.
     * @return The strictness to configure, by default {@link Strictness#STRICT_STUBS}
     */
    Strictness strictness() default Strictness.STRICT_STUBS;

    /**
     * Controls which mocks the extension attaches to the session for strictness reporting.
     * Defaults to {@link MockTracking#ANNOTATED} for backward compatibility.
     *
     * @return The mock tracking mode, by default {@link MockTracking#ANNOTATED}
     * @since 5.21.0
     */
    MockTracking mockTracking() default MockTracking.ANNOTATED;
}
