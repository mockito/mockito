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
 *
 * Mockito enforces {@code @DoNotMock} with the {@link org.mockito.plugins.DoNotMockEnforcer}.
 *
 * If you want to use a custom {@code @DoNotMock} annotation, the {@link org.mockito.plugins.DoNotMockEnforcer}
 * will match on annotations with a type ending in "org.mockito.DoNotMock". You can thus place
 * your custom annotation in {@code com.my.package.org.mockito.DoNotMock} and Mockito will enforce
 * that types annotated by {@code @com.my.package.org.mockito.DoNotMock} can not be mocked.
 *
 * <pre class="code"><code class="java">
 * &#064;DoNotMock(reason = "Use a real instance instead")
 * class DoNotMockMe {}
 * </code></pre>
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
    String reason() default "Create a real instance instead.";
}
