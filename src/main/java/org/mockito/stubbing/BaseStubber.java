/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Mockito;
import org.mockito.NotExtensible;

/**
 * Base interface for stubbing consecutive method calls with {@link Mockito#doReturn(Object)} syntax.
 * This interface is needed so that we can reuse the same hierarchy in subinterfaces.
 *
 * @since 2.20.0
 */
@NotExtensible
public interface BaseStubber {

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doThrow(Throwable[])} style:
     * <pre class="code"><code class="java">
     *   doThrow(new RuntimeException("one")).
     *   doThrow(new RuntimeException("two"))
     *       .when(mock).someVoidMethod();
     * </code></pre>
     * See javadoc for {@link Mockito#doThrow(Throwable[])}
     *
     * @param toBeThrown to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    Stubber doThrow(Throwable... toBeThrown);

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doThrow(Class)} style:
     * <pre class="code"><code class="java">
     *   doThrow(RuntimeException.class).
     *   doThrow(IllegalArgumentException.class)
     *       .when(mock).someVoidMethod();
     * </code></pre>
     * See javadoc for {@link Mockito#doThrow(Class)}
     *
     * @param toBeThrown exception class to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     *
     * @since 2.1.0
     */
    Stubber doThrow(Class<? extends Throwable> toBeThrown);

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doThrow(Class)} style:
     * <pre class="code"><code class="java">
     *   doThrow(RuntimeException.class).
     *   doThrow(IllegalArgumentException.class)
     *       .when(mock).someVoidMethod();
     * </code></pre>
     * See javadoc for {@link Mockito#doThrow(Class)}
     *
     * @param toBeThrown exception class to be thrown when the stubbed method is called
     * @param nextToBeThrown exception class next to be thrown when the stubbed method is called
     * @return stubber - to select a method for stubbing
     *
     * @since 2.1.0
     */
    // Additional method helps users of JDK7+ to hide heap pollution / unchecked generics array creation
    @SuppressWarnings ({"unchecked", "varargs"})
    Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown);

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doAnswer(Answer)} style:
     * <pre class="code"><code class="java">
     *   doAnswer(answerOne).
     *   doAnswer(answerTwo)
     *       .when(mock).someVoidMethod();
     * </code></pre>
     * See javadoc for {@link Mockito#doAnswer(Answer)}
     *
     * @param answer to answer when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    Stubber doAnswer(Answer answer);

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doNothing()} style:
     * <pre class="code"><code class="java">
     *   doNothing().
     *   doThrow(new RuntimeException("two"))
     *       .when(mock).someVoidMethod();
     * </code></pre>
     * See javadoc for {@link Mockito#doNothing()}
     *
     * @return stubber - to select a method for stubbing
     */
    Stubber doNothing();

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doReturn(Object)} style.
     * <p>
     * See javadoc for {@link Mockito#doReturn(Object)}
     *
     * @param toBeReturned to be returned when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    Stubber doReturn(Object toBeReturned);

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doReturn(Object)} style.
     * <p>
     * See javadoc for {@link Mockito#doReturn(Object, Object...)}
     *
     * @param toBeReturned to be returned when the stubbed method is called
     * @param nextToBeReturned to be returned in consecutive calls when the stubbed method is called
     * @return stubber - to select a method for stubbing
     */
    @SuppressWarnings({"unchecked", "varargs"})
    Stubber doReturn(Object toBeReturned, Object... nextToBeReturned);

    /**
     * Use it for stubbing consecutive calls in {@link Mockito#doCallRealMethod()} style.
     * <p>
     * See javadoc for {@link Mockito#doCallRealMethod()}
     *
     * @return stubber - to select a method for stubbing
     */
    Stubber doCallRealMethod();
}
