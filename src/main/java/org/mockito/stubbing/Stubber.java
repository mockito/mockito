/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Mockito;
import org.mockito.NotExtensible;

/**
 * Allows to choose a method when stubbing in doThrow()|doAnswer()|doNothing()|doReturn() style
 * <p>
 * Example:
 * <pre class="code"><code class="java">
 *   doThrow(new RuntimeException()).when(mockedList).clear();
 *
 *   //following throws RuntimeException:
 *   mockedList.clear();
 * </code></pre>
 *
 * Also useful when stubbing consecutive calls:
 *
 * <pre class="code"><code class="java">
 *   doThrow(new RuntimeException("one")).
 *   doThrow(new RuntimeException("two"))
 *   .when(mock).someVoidMethod();
 * </code></pre>
 *
 * Read more about those methods:
 * <p>
 * {@link Mockito#doThrow(Throwable[])}
 * <p>
 * {@link Mockito#doAnswer(Answer)}
 * <p>
 * {@link Mockito#doNothing()}
 * <p>
 * {@link Mockito#doReturn(Object)}
 * <p>
 *
 * See examples in javadoc for {@link Mockito}
 */
@SuppressWarnings("unchecked")
@NotExtensible
public interface Stubber extends BaseStubber {

    /**
     * Allows to choose a method when stubbing in doThrow()|doAnswer()|doNothing()|doReturn() style
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     *   doThrow(new RuntimeException())
     *   .when(mockedList).clear();
     *
     *   //following throws RuntimeException:
     *   mockedList.clear();
     * </code></pre>
     *
     * Read more about those methods:
     * <p>
     * {@link Mockito#doThrow(Throwable[])}
     * <p>
     * {@link Mockito#doAnswer(Answer)}
     * <p>
     * {@link Mockito#doNothing()}
     * <p>
     * {@link Mockito#doReturn(Object)}
     * <p>
     *
     * See examples in javadoc for {@link Mockito}
     *
     * @param mock The mock
     * @return select method for stubbing
     */
    <T> T when(T mock);
}
