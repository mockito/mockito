/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation is not supposed to be used by Mockito end-users. Instead, we
 * use it to annotate methods for Static Analysis tools, including FindBugs and ErrorProne.
 * These tools can check whether the return value of our Mockito methods are actually
 * used. As such, Mockito State Validation can be performed at compile-time rather than run-time.
 * This annotation is public, because we have to use it in multiple packages.
 *
 * @see <a href="https://github.com/findbugsproject/findbugs/blob/264ae7baf890d2b347d91805c90057062b5dcb1e/findbugs/src/java/edu/umd/cs/findbugs/detect/BuildCheckReturnAnnotationDatabase.java#L120">Findbugs source code</a>
 * @see <a href="http://errorprone.info/bugpattern/CheckReturnValue">ErrorProne check</a>
 * @since 2.11.4
 */
@Target({
    ElementType.CONSTRUCTOR,
    ElementType.METHOD,
    ElementType.PACKAGE,
    ElementType.TYPE
})
@Retention(RetentionPolicy.CLASS)
public @interface CheckReturnValue {
}
