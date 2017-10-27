package org.mockito;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Findbugs handles any annotation with name "CheckReturnValue" in return value check.
 *
 * @see <a href="https://github.com/findbugsproject/findbugs/blob/264ae7baf890d2b347d91805c90057062b5dcb1e/findbugs/src/java/edu/umd/cs/findbugs/detect/BuildCheckReturnAnnotationDatabase.java#L120">Findbugs source code</a>
 * @see <a href="http://errorprone.info/bugpattern/CheckReturnValue">ErrorProne check</a>
 */
@Target({
    ElementType.CONSTRUCTOR,
    ElementType.METHOD,
    ElementType.PACKAGE,
    ElementType.TYPE
})
@Retention(RetentionPolicy.CLASS)
@interface CheckReturnValue {
}
