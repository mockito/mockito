/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing;

import org.mockito.Mockito;
import org.mockito.internal.progress.IOngoingStubbing;

/**
 * Stubs a method call with return value or an exception. E.g:
 *
 * <pre class="code"><code class="java">
 * stub(mock.someMethod()).toReturn(10);
 *
 * //you can use flexible argument matchers, e.g:
 * stub(mock.someMethod(<b>anyString()</b>)).toReturn(10);
 *
 * //setting exception to be thrown:
 * stub(mock.someMethod("some arg")).toThrow(new RuntimeException());
 *
 * //you can stub with different behavior for consecutive method calls.
 * //Last stubbing (e.g: toReturn("foo")) determines the behavior for further consecutive calls.
 * stub(mock.someMethod("some arg"))
 *  .toThrow(new RuntimeException())
 *  .toReturn("foo");
 *
 * </code></pre>
 *
 * See examples in javadoc for {@link Mockito#stub}
 */
public interface DeprecatedOngoingStubbing<T> extends IOngoingStubbing {

    /**
     * Set a return value for the stubbed method. E.g:
     * <pre class="code"><code class="java">
     * stub(mock.someMethod()).toReturn(10);
     * </code></pre>
     *
     * See examples in javadoc for {@link Mockito#stub}
     *
     * @param value return value
     *
     * @return iOngoingStubbing object that allows stubbing consecutive calls
     */
    DeprecatedOngoingStubbing<T> toReturn(T value);

    /**
     * Set a Throwable to be thrown when the stubbed method is called. E.g:
     * <pre class="code"><code class="java">
     * stub(mock.someMethod()).toThrow(new RuntimeException());
     * </code></pre>
     *
     * If throwable is a checked exception then it has to
     * match one of the checked exceptions of method signature.
     *
     * See examples in javadoc for {@link Mockito#stub}
     *
     * @param throwable to be thrown on method invocation
     *
     * @return iOngoingStubbing object that allows stubbing consecutive calls
     */
    DeprecatedOngoingStubbing<T> toThrow(Throwable throwable);

    /**
     * Set a generic Answer for the stubbed method. E.g:
     * <pre class="code"><code class="java">
     * stub(mock.someMethod(10)).toAnswer(new Answer&lt;Integer&gt;() {
     *     public Integer answer(InvocationOnMock invocation) throws Throwable {
     *         return (Integer) invocation.getArguments()[0];
     *     }
     * }
     * </code></pre>
     *
     * @param answer the custom answer to execute.
     *
     * @return iOngoingStubbing object that allows stubbing consecutive calls
     */
    DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer);
}