/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.VerificationMode;

//TODO add pattern for not creating it for all mocks

/**
 * Allows verification in order. E.g:
 * 
 * <pre>
 * InOrder inOrder = inOrder(firstMock, secondMock);
 * 
 * inOrder.verify(firstMock).add("should be called first");
 * inOrder.verify(secondMock).add("should be called second");
 * </pre>
 * 
 * See examples {@link Mockito#inOrder(Object...)}
 */
public interface InOrder {
    /**
     * Verifies mock object in order. E.g:
     * 
     * <pre>
     * InOrder inOrder = inOrder(firstMock, secondMock);
     * 
     * inOrder.verify(firstMock).someMethod("should be called first");
     * inOrder.verify(secondMock).someMethod("should be called second");
     * </pre>
     * <p>
     * 
     * See examples {@link Mockito}
     * 
     * @param mock to be verified
     * 
     * @return mock object itself
     */
    <T> T verify(T mock);

    /**
     * Verifies mock object in order. E.g:
     * 
     * <pre>
     * InOrder inOrder = inOrder(firstMock, secondMock);
     * 
     * inOrder.verify(firstMock, times(2)).someMethod("should be called first two times");
     * inOrder.verify(secondMock, atLeastOnce()).someMethod("should be called second");
     * </pre>
     * <p>
     * 
     * See examples {@link Mockito}
     * 
     * @param mock to be verified
     * @param mode times(x) or atLeastOnce()
     * 
     * @return mock object itself
     */
    <T> T verify(T mock, VerificationMode mode);
}