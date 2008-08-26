package org.mockito;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows shorthand mock creation, see examples in javadoc for {@link MockitoAnnotations} class.
 */
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Mock {}

