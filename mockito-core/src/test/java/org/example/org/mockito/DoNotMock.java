package org.example.org.mockito;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Test-only custom DoNotMock annotation.
 * Its FQN ends with "org.mockito.DoNotMock",
 * so DefaultDoNotMockEnforcer will treat it as a DoNotMock-style annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DoNotMock {
    String reason() default "";
}
