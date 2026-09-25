/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.mockito.Mockito;
import org.mockito.NotExtensible;

/**
 * An invocation on a mock.
 *
 * <p>
 * A placeholder for mock, the method that was called and the arguments that were passed.
 */
@NotExtensible
public interface InvocationOnMock extends Serializable {

    /**
     * returns the mock object
     *
     * @return mock object
     */
    Object getMock();

    /**
     * returns the method
     *
     * @return method
     */
    Method getMethod();

    /**
     * Returns unprocessed arguments whereas {@link #getArguments()} returns
     * arguments already processed (e.g. varargs expended, etc.).
     *
     * @return unprocessed arguments, exactly as provided to this invocation.
     * @since 4.7.0
     */
    Object[] getRawArguments();

    /**
     * Returns arguments passed to the method.
     *
     * Vararg are expanded in this array.
     *
     * @return arguments
     */
    Object[] getArguments();

    /**
     * Returns casted argument at the given index.
     *
     * Can lookup in expanded arguments form {@link #getArguments()}.
     *
     * This method is preferred over {@link #getArgument(int, Class)} for readability. Please read
     * the documentation of {@link #getArgument(int, Class)} for an overview of situations when
     * that method is preferred over this one.
     *
     * @param index argument index
     * @return casted argument at the given index
     * @since 2.1.0
     */
    <T> T getArgument(int index);

    /**
     * Returns casted argument at the given index. This method is analogous to
     * {@link #getArgument(int)}, but is necessary to circumvent issues when dealing with generics.
     *
     * In general, {@link #getArgument(int)} is the appropriate function to use. This particular
     * function is only necessary if you are doing one of the following things:
     *
     * <ol>
     *  <li>You want to directly invoke a method on the result of {@link #getArgument(int)}.</li>
     *  <li>You want to directly pass the result of the invocation into a function that accepts a generic parameter.</li>
     * </ol>
     *
     * If you prefer to use {@link #getArgument(int)} instead, you can circumvent the compilation
     * issues by storing the intermediate result into a local variable with the correct type.
     *
     * @param index argument index
     * @param clazz class to cast the argument to
     * @return casted argument at the given index
     */
    <T> T getArgument(int index, Class<T> clazz);

    /**
     * calls real method
     * <p>
     * <b>Warning:</b> depending on the real implementation it might throw exceptions
     *
     * @return whatever the real method returns / throws
     * @throws Throwable in case real method throws
     */
    Object callRealMethod() throws Throwable;

    /**
     * Returns the number of times this method has been invoked on this mock with the
     * same arguments, including the current invocation.
     *
     * <p>
     * This is useful when an {@link org.mockito.stubbing.Answer} needs to behave
     * differently depending on how many times it has been called. For example, to fail
     * the first few calls and then return a value:
     *
     * <pre class="code"><code class="java">
     * when(mock.get("foo")).thenAnswer(invocation -&gt; {
     *     if (invocation.getInvocationCount() &lt;= 2) {
     *         throw new RuntimeException("not ready yet");
     *     }
     *     return 200;
     * });
     * </code></pre>
     *
     * <p>
     * Counts are scoped to the same mock, the same method, and the same arguments
     * (compared with {@link java.util.Arrays#deepEquals(Object[], Object[])}). Calls to
     * other methods, other mocks, or the same method with different arguments are not
     * included.
     *
     * <p>
     * The current invocation is included in the count, so the first call returns
     * {@code 1}.
     *
     * @return the 1-based count of matching invocations on this mock, including this one
     * @since 5.x.x
     */
    default int getInvocationCount() {
        return (int)
                Mockito.mockingDetails(getMock()).getInvocations().stream()
                        .filter(this::sameMethod)
                        .filter(this::sameArguments)
                        .count();
    }

    private boolean sameMethod(Invocation invocation) {
        return invocation.getMethod().equals(getMethod());
    }

    private boolean sameArguments(Invocation invocation) {
        return Arrays.deepEquals(invocation.getArguments(), getArguments());
    }
}
