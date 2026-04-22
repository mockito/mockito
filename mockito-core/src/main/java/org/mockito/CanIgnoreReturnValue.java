/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is not intended to be used by Mockito end-users directly.
 * Instead, we use it to annotate methods or classes for Static Analysis tools,
 * including FindBugs and ErrorProne.
 * <p>These tools can check whether the return value of a method is unnecessarily
 * ignored. By annotating a method (or a whole class) with {@code @CanIgnoreReturnValue},
 * we indicate that ignoring the return value of this method is acceptable and
 * should not trigger a compile-time warning or error.
 * <p>This annotation is public because we need to use it across multiple packages.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface CanIgnoreReturnValue {}
