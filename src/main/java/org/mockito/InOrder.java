/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.mockito.verification.VerificationMode;

/**
 * Allows verification in order. E.g:
 * 
 * <pre class="code"><code class="java">
 * InOrder inOrder = inOrder(firstMock, secondMock);
 * 
 * inOrder.verify(firstMock).add("was called first");
 * inOrder.verify(secondMock).add("was called second");
 * </code></pre>
 * 
 * As of Mockito 1.8.4 you can verifyNoMoreInteractions() in order-sensitive way. Read more: {@link InOrder#verifyNoMoreInteractions()}
 * <p>
 * 
 * See examples in javadoc for {@link Mockito} class
 */
public interface InOrder {
    /**
     * Verifies interaction <b>happened once</b> in order.
     * <p>
     * Alias to <code>inOrder.verify(mock, times(1))</code>
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     * InOrder inOrder = inOrder(firstMock, secondMock);
     * 
     * inOrder.verify(firstMock).someMethod("was called first");
     * inOrder.verify(secondMock).someMethod("was called second");
     * </code></pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mock to be verified
     * 
     * @return mock object itself
     */
    <T> T verify(T mock);

    /**
     * Verifies interaction in order. E.g:
     * 
     * <pre class="code"><code class="java">
     * InOrder inOrder = inOrder(firstMock, secondMock);
     * 
     * inOrder.verify(firstMock, times(2)).someMethod("was called first two times");
     * inOrder.verify(secondMock, atLeastOnce()).someMethod("was called second at least once");
     * </code></pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mock to be verified
     * @param mode for example times(x) or atLeastOnce()
     * 
     * @return mock object itself
     */
    <T> T verify(T mock, VerificationMode mode);

    
    /**
     * Verifies that no more interactions happened <b>in order</b>. 
     * Different from {@link Mockito#verifyNoMoreInteractions(Object...)} because the order of verification matters.
     * <p>
     * Example:
     * <pre class="code"><code class="java">
     * mock.foo(); //1st
     * mock.bar(); //2nd
     * mock.baz(); //3rd
     * 
     * InOrder inOrder = inOrder(mock);
     * 
     * inOrder.verify(mock).bar(); //2n
     * inOrder.verify(mock).baz(); //3rd (last method)
     * 
     * //passes because there are no more interactions after last method:
     * inOrder.verifyNoMoreInteractions();
     * 
     * //however this fails because 1st method was not verified:
     * Mockito.verifyNoMoreInteractions(mock);
     * </code></pre>
     */
    void verifyNoMoreInteractions();
}