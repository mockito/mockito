/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation.org.mockito;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Test to make sure that we are matching on name rather than only {@link org.mockito.DoNotMock}
 * type equality.
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface DoNotMock {}
