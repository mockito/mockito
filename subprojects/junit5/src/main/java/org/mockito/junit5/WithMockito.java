package org.mockito.junit5;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Place this annotation on your root test class in order to initialize fields annotated with {@link Mock @Mock},
 * {@link Spy @Spy} } or {@link Captor @Captor}. To specify the stubbing stricness you can declare the
 * annotation {@link Strictness, @Stricness} <p></p>
 * This is the Junit5 equivalent to {@link MockitoRule} and {@link MockitoJUnitRunner} for JUnit4.
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, TYPE})
@ExtendWith(MockitoExtension.class)
public @interface WithMockito {}
