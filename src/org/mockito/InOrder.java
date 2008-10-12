/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.verification.api.VerificationMode;

/**
 * Allows verification in order. E.g:
 * 
 * <pre>
 * InOrder inOrder = inOrder(firstMock, secondMock);
 * 
 * inOrder.verify(firstMock).add("was called first");
 * inOrder.verify(secondMock).add("was called second");
 * </pre>
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
     * <pre>
     * InOrder inOrder = inOrder(firstMock, secondMock);
     * 
     * inOrder.verify(firstMock).someMethod("was called first");
     * inOrder.verify(secondMock).someMethod("was called second");
     * </pre>
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
     * <pre>
     * InOrder inOrder = inOrder(firstMock, secondMock);
     * 
     * inOrder.verify(firstMock, times(2)).someMethod("was called first two times");
     * inOrder.verify(secondMock, atLeastOnce()).someMethod("was called second at least once");
     * </pre>
     * 
     * See examples in javadoc for {@link Mockito} class
     * 
     * @param mock to be verified
     * @param mode for example times(x) or atLeastOnce()
     * 
     * @return mock object itself
     */
    <T> T verify(T mock, VerificationMode mode);
}