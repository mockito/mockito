package org.mockito.junit5;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * This annotation can be used set the {@link org.mockito.quality.Strictness} for mocks created for a test.
 * <p><p/>
 * You can place this annotation on your root-test class, on nested test classes and test methods. If you don't declare
 * this annotation the strictness will be inherited from the parent test-component (parent nested-test or root-test).
 * The default strictness is {@link org.mockito.quality.Strictness#WARN}.
 *
 * @see org.mockito.quality.Strictness
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface Strictness {
    org.mockito.quality.Strictness value(); //we define no default here since a lonely >@Strictness()< makes no sense
}

